package battleship.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import battleship.LocalizationLoader;
import battleship.controller.ServerController.MessageType;
import battleship.view.BattleshipView;

/**
 * The ChatController class handles chat functionality for the Battleship game.
 */
public class ChatController {
	private BattleshipView view;
	private BattleshipController controller;
	private LocalizationLoader loader;

	/**
	 * Constructor for ChatController
	 * 
	 * @param view       The BattleshipView instance.
	 * @param controller The BattleshipController instance.
	 * @param loader 	 The LocalizationLoader instance.
	 */
	public ChatController(BattleshipView view, BattleshipController controller, LocalizationLoader loader) {
		this.view = view;
		this.controller = controller;
		this.loader = loader;
		initializeChatInputListener();
	}

	/**
	 * Initializes the chat input listener to send messages when Enter is pressed.
	 */
	private void initializeChatInputListener() {
		view.getChatInput().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = view.getChatInput().getText();
				sendChatMessage(text);
				view.getChatInput().setText("");
			}
		});
	}

	/**
	 * Receives a chat message and displays it in the chat history.
	 *
	 * @param message The message received.
	 */
	public void receiveChatMessage(String message) {
		view.getChatHistory().append(message + "\n");
	}

	/**
	 * Sends a chat message.
	 *
	 * @param message The message to send.
	 */
	public void sendChatMessage(String message) {
		if (controller.getServerController() != null) {
			controller.getServerController().sendData(MessageType.CHAT,
					controller.getServerController().getName() + " : " + message);
			receiveChatMessage(controller.getServerController().getName() + " : " + message);
		} else {
			receiveChatMessage(loader.getResourceBundle().getString("chat.you") + " : " + message);
		}
	}
}
