package battleship.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import battleship.LocalizationLoader;
import battleship.model.BattleshipModel;

/**
 * The ServerController class handles the server-side and client-side networking
 * for the Battleship game. It manages connections, data transmission, and game
 * synchronization between the host and the client.
 */
public class ServerController {

	/**
	 * Enum to define the types of messages that can be sent between the client and
	 * server.
	 */
	public enum MessageType {
		NAME, PLACE, MOVE, CHAT, START, RESTART, DISCONNECT
	}

	private BattleshipController controller;
	private BattleshipModel model;
	private LocalizationLoader loader;

	private String name;
	private String opponentName;
	private int port;
	private String address;

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Thread serverThread;
	private Thread clientThread;
	
	/**
	 * Indicates whether the user is a host or client.
	 */
	public int isHost = -1;

	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Indicates if the user would like to restart.
	 */
	public int restartFlag = -1;
	
	/**
	 * Indicates if a client is connected.
	 */
	public boolean clientConnected = false;
	
	private volatile boolean running = true; // Flag to control accepting connections

	private CyclicBarrier barrier = new CyclicBarrier(2);

	/**
	 * Constructor for the ServerController class.
	 *
	 * @param controller The BattleshipController instance.
	 * @param model      The BattleshipModel instance.
	 * @param loader     The LocalizationLoader instance for loading localization
	 *                   strings.
	 * @param name       The name of the player.
	 * @param port       The port number to host/connect to the server.
	 * @param address    The address of the server. If null, the local host address
	 *                   is used.
	 */
	public ServerController(BattleshipController controller, BattleshipModel model, LocalizationLoader loader,
			String name, int port, String address) {
		this.controller = controller;
		this.model = model;
		this.loader = loader;
		this.name = name;
		this.port = port;
		if (address == null) {
			try {
				this.address = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			this.address = address;
		}
		// Add a shutdown hook to disconnect on exit
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			disconnect();
		}));
	}

	/**
	 * Starts hosting the game server.
	 */
	public void host() {
		isHost = 1;
		serverThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(port);
				InetAddress address = InetAddress.getLocalHost();
				this.address = address.getHostAddress();
				// Notify the chat controller about the server status
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("host.server")
						+ " " + loader.getResourceBundle().getString("host.status"));
				controller.getChatController()
						.receiveChatMessage(loader.getResourceBundle().getString("join.address") + ": " + this.address);
				controller.getChatController()
						.receiveChatMessage(loader.getResourceBundle().getString("host.port") + ": " + port);
				while (running) { // Check the flag before accepting connections
					try {
						clientSocket = serverSocket.accept();
						clientConnected = true;

						out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
						in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						// Start receiving data
						receiveData();
					} catch (SocketException e) {
						if (!running) {
							controller.getChatController()
									.receiveChatMessage(loader.getResourceBundle().getString("host.server") + " "
											+ loader.getResourceBundle().getString("host.disconnected"));
							controller.setServerController(null);
						} else {
							e.printStackTrace();
							controller.setServerController(null);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				controller.setServerController(null);
			}
		});
		serverThread.start();
	}

	/**
	 * Connects to a game server.
	 */
	public void connect() {
		isHost = 0;
		clientThread = new Thread(() -> {
			try {
				clientSocket = new Socket(address, port);
				controller.getChatController()
						.receiveChatMessage(loader.getResourceBundle().getString("host.connected"));
				controller.getChatController()
						.receiveChatMessage(loader.getResourceBundle().getString("join.address") + ": " + address);
				controller.getChatController()
						.receiveChatMessage(loader.getResourceBundle().getString("host.port") + ": " + port);

				out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				// Start receiving data
				receiveData();

				sendData(MessageType.CHAT, name + " " + loader.getResourceBundle().getString("host.connected"));

			} catch (IOException e) {
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("join.error"));
				controller.setServerController(null);
			}
		});
		clientThread.start();
	}

	/**
	 * Sends data to the connected client or server.
	 *
	 * @param type    The type of the message being sent.
	 * @param message The message content.
	 */
	public void sendData(MessageType type, String message) {
		if (out != null) {
			out.println(type + "::" + message);
			out.flush();
		}
	}

	/**
	 * Receives data from the connected client or server.
	 */
	public void receiveData() {
		Thread receiveThread = new Thread(() -> {
			try {
				String receivedMessage;
				while ((receivedMessage = in.readLine()) != null) {
					String[] parts = receivedMessage.split("::", 2);
					MessageType type = MessageType.valueOf(parts[0]);
					String message = parts.length > 1 ? parts[1] : "";

					switch (type) {
					case NAME:
						this.opponentName = message;
						break;
					case PLACE:
						handlePlace(message);
						break;
					case MOVE:
						handleMove(message);
						break;
					case CHAT:
						controller.getChatController().receiveChatMessage(message);
						break;
					case START:
						handleStart(message);
						break;
					case RESTART:
						handleRestart(message);
						break;
					case DISCONNECT:
						disconnect();
						break;
					}
				}
			} catch (IOException e) {
				if (running) {
					e.printStackTrace();
				}
			} finally {
				disconnect();
			}
		});
		receiveThread.start();
	}

	/**
	 * Handles ship placement messages.
	 *
	 * @param message The ship placement message.
	 */
	private void handlePlace(String message) {
		// Parse the message to get the ship placement details and update the model
		String[] details = message.split(",");
		int row = Integer.parseInt(details[0]);
		int col = Integer.parseInt(details[1]);
		int length = Integer.parseInt(details[2]);
		boolean isHorizontal = Boolean.parseBoolean(details[3]);

		// Notify the model to place the ship
		model.placeOpponentShips(row, col, length, isHorizontal);
	}

	/**
	 * Handles move messages.
	 *
	 * @param data The move message.
	 */
	public void handleMove(String data) {
		String[] parts = data.split(",");
		int row = Integer.parseInt(parts[0]);
		int col = Integer.parseInt(parts[1]);
		model.opponentFires(row, col);
	}

	/**
	 * Handles start game messages.
	 *
	 * @param message The start game message.
	 */
	private void handleStart(String message) {
		if ("START".equals(message)) {
			controller.getServerController().sendData(MessageType.NAME, controller.getServerController().getName());
			sendData(MessageType.START, "ACK_START");
			controller.getMenuController().gameStart = true;
			controller.getShipController().resetGrid();
			model.resetModel();
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.place"));
			controller.getShipController().initializeGridListeners();
		} else if ("ACK_START".equals(message)) {
			controller.getMenuController().gameStart = true;
			controller.getShipController().resetGrid();
			model.resetModel();
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.place"));
			controller.getShipController().initializeGridListeners();
		} else if ("PLACED".equals(message)) {
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Synchronizes the threads using the CyclicBarrier.
	 */
	public void synchronize() {
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles restart game messages.
	 *
	 * @param message The restart game message.
	 */
	private void handleRestart(String message) {
		if ("0".equals(message)) {
			restartFlag = 0;
		}
		if ("1".equals(message)) {
			restartFlag = 1;
			controller.getMenuController().startOrRestartGame(null);
			restartFlag = -1;
		}
		if ("2".equals(message)) {
			restartFlag = 1;
			controller.getMenuController().startOrRestartGame(null);
			restartFlag = -1;
		}
	}

	/**
	 * Disconnects the client or server from the game.
	 */
	public void disconnect() {
		try {
			if (isHost == 0) { // If the current instance is the client, close the client socket

				// Notify the chat controller about the disconnection
				sendData(MessageType.CHAT, getName() + " " + loader.getResourceBundle().getString("host.disconnected"));

				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}

				// Stop the client thread
				if (clientThread != null && clientThread.isAlive()) {
					clientThread.interrupt();
				}
			} // If the current instance is the host, close everything

			else if (isHost == 1) {

				sendData(MessageType.CHAT, getName() + " " + loader.getResourceBundle().getString("host.disconnected"));

				// Send a disconnect message to the client
				if (clientConnected) {
					sendData(MessageType.DISCONNECT, "");
				}

				running = false;
				clientConnected = false;

				// Close the server socket
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}

				// Close the client socket if connected
				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}

				// Stop the server thread
				if (serverThread != null && serverThread.isAlive()) {
					serverThread.interrupt();
				}

				// Stop the client thread if connected
				if (clientThread != null && clientThread.isAlive()) {
					clientThread.interrupt();
				}

			}

			// Close the input and output streams
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (controller.getMenuController().gameStart) {
				restartFlag = 1;
				controller.getMenuController().startOrRestartGame(null);
				restartFlag = -1;
			}
			controller.setServerController(null);
		}
	}

	/**
	 * Gets the name of the player.
	 *
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the name of the opponent.
	 *
	 * @return The opponent's name.
	 */
	public String getOpponentName() {
		return opponentName;
	}

	/**
	 * Gets the restart flag value.
	 *
	 * @return The restart flag value.
	 */
	public int getRestartFlag() {
		return restartFlag;
	}

	/**
	 * Sets the restart flag value.
	 *
	 * @param restartFlag The new restart flag value.
	 */
	public void setRestartFlag(int restartFlag) {
		this.restartFlag = restartFlag;
	}

	/**
	 * Gets the client thread.
	 *
	 * @return The client thread.
	 */
	public Thread getClientThread() {
		return clientThread;
	}
	
	/**
	 * Sets the BattleshipModel instance.
	 * 
	 * @param model The BattleshipModel instance.
	 */
	public void setModel(BattleshipModel model) {
		this.model = model;
	}
}
