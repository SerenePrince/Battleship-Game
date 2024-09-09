package battleship.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import battleship.FontLoader;
import battleship.LocalizationLoader;
import battleship.controller.BattleshipController;
import battleship.controller.ServerController;
import battleship.model.BattleshipModel;
import battleship.model.CoordinateModel;
import battleship.model.ShipModel;

/**
 * The BattleshipView class represents the main frame of the Battleship game.
 */
public class BattleshipView extends JFrame {

	private static final long serialVersionUID = 1L;

	private BattleshipController controller;
	private BattleshipModel model;
	private LocalizationLoader loader;

	private Color backgroundColor = Color.BLACK;
	private Color textColor = new Color(0x00FF00);

	private static Font agencyBold24;
	private static Font agencyBold54;
	private static Font agencyPlain20;
	private static Font agencyPlain24;
	private static Font agencyPlain28;

	private JPanel chatPanel;
	private JTextArea chatHistory;
	private JTextField chatInput;

	private JPanel gamePanel;
	private JPanel gridPanel;
	private JButton[][] gridButtons;
	private JLabel[] gridColLabels;
	private JLabel[] gridRowLabels;

	private JPanel infoPanel;
	private JPanel gameInfoPanel;
	private JPanel infoPanels;
	private JPanel playerInfoPanel;
	private JPanel opponentInfoPanel;

	private JLabel logoLabel;
	private JPanel logoPanel;
	private JLabel loseLabel;
	private JLabel winLabel;
	private JLabel turnLabel;
	private JLabel timeLabel;
	private JLabel playerInfoLabel;
	private JLabel opponentInfoLabel;
	private JLabel[] playerInfoLabels;
	private JLabel[] opponentInfoLabels;

	private JWindow hostWindow;
	private JLabel hostNameLabel;
	private JTextField hostNameInput;
	private JLabel hostPortLabel;
	private JTextField hostPortInput;
	private JButton hostStartButton;
	private JButton hostCloseButton;
	private JWindow clientWindow;
	private JLabel clientNameLabel;
	private JTextField clientNameInput;
	private JLabel clientPortLabel;
	private JTextField clientPortInput;
	private JLabel clientAddressLabel;
	private JTextField clientAddressInput;
	private JButton clientJoinButton;
	private JButton clientCloseButton;

	private JMenuBar menuBar;

	private JMenu settings;
	private JMenuItem settingsItem1;
	private JMenuItem settingsItem2;
	private JMenuItem settingsItem3;
	private JMenuItem settingsItem4;
	private JMenuItem settingsItem5;
	private JMenuItem settingsItem6;

	private JMenu info;
	private JMenuItem infoItem1;
	private JMenuItem infoItem2;

	private JMenu game;
	private JMenuItem gameItem1;
	private JMenuItem gameItem2;
	private JMenuItem gameItem3;
	private JMenuItem gameItem4;

	private ImageIcon hitIndicator = new ImageIcon("resources/images/hit.png");
	private ImageIcon missIndicator = new ImageIcon("resources/images/miss.png");
	private ImageIcon horizontalShipHead = new ImageIcon(getClass().getResource("/resources/images/HShipHead.png"));
	private ImageIcon horizontalShipBody = new ImageIcon(getClass().getResource("/resources/images/HShipBody.png"));
	private ImageIcon horizontalShipEnd = new ImageIcon(getClass().getResource("/resources/images/HShipEnd.png"));
	private ImageIcon verticalShipHead = new ImageIcon(getClass().getResource("/resources/images/VShipHead.png"));
	private ImageIcon verticalShipBody = new ImageIcon(getClass().getResource("/resources/images/VShipBody.png"));
	private ImageIcon verticalShipEnd = new ImageIcon(getClass().getResource("/resources/images/VShipEnd.png"));

	/**
	 * Constructor for the BattleshipView class.
	 * 
	 * @param loader 	The LocalizationLoader instance.
	 */
	public BattleshipView(LocalizationLoader loader) {
		this.loader = loader;

		initializeFonts();

		setPreferredSize(new Dimension(1280, 714));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		getContentPane().setBackground(backgroundColor);
		setLayout(new BorderLayout());

		initializeGamePanel();
		initializeInfoPanel();
		initializeMenu();

		pack();
		setLocationRelativeTo(null);
		setTitle("Battleship");

		setVisible(true);

		chatHistory.append("Welcome to Battleship!\n\n"
				+ "For instructions, please navigate to the \"Info\" menu and select \"Help.\"\n\n"
				+ "To commence the game, proceed to the \"Game\" menu and select \"Start.\"\n\n"
				+ "To change the language, access the \"Settings\" menu and select \"Switch Language\".\n\n");
		chatHistory.append("Bienvenue dans Battleship !\n\n"
				+ "Pour les instructions, rendez-vous dans le menu \"Info\" et sélectionnez \"Aide\".\n\n"
				+ "Pour démarrer le jeu, allez dans le menu \"Jeu\" et sélectionnez \"Démarrer\".\n\n"
				+ "Pour changer la langue, accédez au menu \"Paramètres\" et sélectionnez \"Changer de Langue\".\n\n");
	}

	/**
	 * Initializes the fonts used in the Battleship game.
	 */
	private void initializeFonts() {
		agencyBold24 = FontLoader.loadFont("resources/fonts/AgencyFB-Bold.ttf", Font.BOLD, 24f);
		agencyBold54 = FontLoader.loadFont("resources/fonts/AgencyFB-Bold.ttf", Font.BOLD, 54f);
		agencyPlain20 = FontLoader.loadFont("resources/fonts/AgencyFB-Plain.ttf", Font.PLAIN, 20f);
		agencyPlain24 = FontLoader.loadFont("resources/fonts/AgencyFB-Plain.ttf", Font.PLAIN, 24f);
		agencyPlain28 = FontLoader.loadFont("resources/fonts/AgencyFB-Plain.ttf", Font.PLAIN, 28f);
	}

	/**
	 * Initializes the game panel, including the logo and the grid.
	 */
	private void initializeGamePanel() {
		// Game panel setup
		gamePanel = new JPanel(new BorderLayout());
		gamePanel.setBackground(backgroundColor);
		gamePanel.setBorder(new LineBorder(textColor));

		// Logo panel
		logoPanel = new JPanel();
		logoPanel.setBackground(backgroundColor);
		logoLabel = new JLabel(loader.getResourceBundle().getString("panel.title"), SwingConstants.CENTER);
		logoLabel.setPreferredSize(new Dimension(742, 153));
		logoLabel.setFont(agencyBold54);
		logoLabel.setForeground(textColor);
		logoPanel.add(logoLabel);

		// Grid panel
		gridPanel = new JPanel(new GridLayout(11, 11));
		gridPanel.setBackground(Color.BLACK);
		gridPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(0, 51, 0, 106)));
		gridButtons = new JButton[10][10];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				JButton button = new JButton();
				button.setActionCommand(row + "," + col);
				gridButtons[row][col] = button;
			}
		}

		gridColLabels = new JLabel[10];

		// Adding labels on the left side (numbers 1 to 10)
		for (int row = 0; row < gridButtons.length; row++) {
			JLabel label = new JLabel(String.valueOf(row + 1), SwingConstants.CENTER);
			label.setFont(agencyBold24);
			label.setForeground(textColor);
			label.setBorder(new LineBorder(Color.BLACK));
			gridPanel.add(label);
			gridColLabels[row] = label;

			for (int col = 0; col < gridButtons[row].length; col++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(53, 51));
				button.setBackground(Color.BLACK);
				button.setForeground(textColor);
				button.setBorder(new LineBorder(textColor));
				gridButtons[row][col] = button;
				gridPanel.add(button);
			}
		}

		// Adding labels at the bottom (letters A to J)
		gridPanel.add(new JLabel());
		gridRowLabels = new JLabel[10];

		char[] columnLabels = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		for (int col = 0; col < 10; col++) {
			JLabel label = new JLabel(String.valueOf(columnLabels[col]), SwingConstants.CENTER);
			label.setFont(agencyBold24);
			label.setForeground(textColor);
			label.setBorder(new LineBorder(Color.BLACK));
			gridPanel.add(label);
			gridRowLabels[col] = label;
		}

		gamePanel.add(logoPanel, BorderLayout.NORTH);
		gamePanel.add(gridPanel, BorderLayout.CENTER);

		add(gamePanel, BorderLayout.WEST);
	}

	/**
	 * Initializes the information panel, including game info, player info, and
	 * opponent info.
	 */
	private void initializeInfoPanel() {
		// Info panel setup
		infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBackground(backgroundColor);
		infoPanel.setBorder(new LineBorder(textColor));

		// Game info panel
		gameInfoPanel = new JPanel(new GridLayout(2, 2));
		gameInfoPanel.setPreferredSize(new Dimension(530, 102));
		gameInfoPanel.setBackground(backgroundColor);
		gameInfoPanel.setBorder(new LineBorder(textColor));

		turnLabel = new JLabel(loader.getResourceBundle().getString("game.turn"), SwingConstants.CENTER);
		turnLabel.setFont(agencyPlain28);
		turnLabel.setForeground(textColor);

		timeLabel = new JLabel(loader.getResourceBundle().getString("game.time"), SwingConstants.CENTER);
		timeLabel.setFont(agencyPlain28);
		timeLabel.setForeground(textColor);

		winLabel = new JLabel(loader.getResourceBundle().getString("game.wins") + " : 0", SwingConstants.CENTER);
		winLabel.setFont(agencyPlain28);
		winLabel.setForeground(textColor);

		loseLabel = new JLabel(loader.getResourceBundle().getString("game.losses") + " : 0", SwingConstants.CENTER);
		loseLabel.setFont(agencyPlain28);
		loseLabel.setForeground(textColor);

		gameInfoPanel.add(turnLabel);
		gameInfoPanel.add(winLabel);
		gameInfoPanel.add(timeLabel);
		gameInfoPanel.add(loseLabel);

		// Player and opponent info panels
		infoPanels = new JPanel(new GridLayout(1, 2));

		playerInfoPanel = new JPanel(new GridLayout(6, 1));
		playerInfoPanel.setPreferredSize(new Dimension(265, 255));
		playerInfoPanel.setBackground(backgroundColor);
		playerInfoPanel.setFont(agencyPlain20);
		playerInfoPanel.setForeground(textColor);
		playerInfoPanel.setBorder(new LineBorder(textColor));

		playerInfoLabel = new JLabel(loader.getResourceBundle().getString("player.info"), SwingConstants.CENTER);
		playerInfoLabel.setFont(agencyPlain20);
		playerInfoLabel.setForeground(textColor);

		playerInfoPanel.add(playerInfoLabel);

		String[] shipTypes = { loader.getResourceBundle().getString("ship.aircraft"),
				loader.getResourceBundle().getString("ship.battleship"),
				loader.getResourceBundle().getString("ship.cruiser"),
				loader.getResourceBundle().getString("ship.destroyer"),
				loader.getResourceBundle().getString("ship.submarine") };
		playerInfoLabels = new JLabel[shipTypes.length];
		for (int i = 0; i < shipTypes.length; i++) {
			JLabel label = new JLabel(shipTypes[i], SwingConstants.CENTER);
			label.setFont(agencyPlain20);
			label.setForeground(textColor);
			playerInfoPanel.add(label);
			playerInfoLabels[i] = label;
		}

		opponentInfoPanel = new JPanel(new GridLayout(6, 1));
		opponentInfoPanel.setPreferredSize(new Dimension(265, 255));
		opponentInfoPanel.setBackground(backgroundColor);
		opponentInfoPanel.setFont(agencyPlain20);
		opponentInfoPanel.setForeground(textColor);
		opponentInfoPanel.setBorder(new LineBorder(textColor));

		opponentInfoLabel = new JLabel(loader.getResourceBundle().getString("opponent.info"), SwingConstants.CENTER);
		opponentInfoLabel.setFont(agencyPlain20);
		opponentInfoLabel.setForeground(textColor);

		opponentInfoPanel.add(opponentInfoLabel);

		opponentInfoLabels = new JLabel[shipTypes.length];
		for (int i = 0; i < shipTypes.length; i++) {
			JLabel label = new JLabel(shipTypes[i], SwingConstants.CENTER);
			label.setFont(agencyPlain20);
			label.setForeground(textColor);
			opponentInfoPanel.add(label);
			opponentInfoLabels[i] = label;
		}

		infoPanels.add(playerInfoPanel);
		infoPanels.add(opponentInfoPanel);

		// Chat panel
		chatPanel = createChatPanel();

		infoPanel.add(gameInfoPanel, BorderLayout.NORTH);
		infoPanel.add(infoPanels, BorderLayout.CENTER);
		infoPanel.add(chatPanel, BorderLayout.SOUTH);

		add(infoPanel, BorderLayout.EAST);
	}

	/**
	 * Initializes the chat panel
	 * 
	 * @return Completed chat panel
	 */
	private JPanel createChatPanel() {
		// Chat panel setup
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.setPreferredSize(new Dimension(530, 357));
		chatPanel.setBackground(backgroundColor);
		chatPanel.setBorder(new LineBorder(textColor));

		// Chat history area (text area)
		chatHistory = new JTextArea();
		chatHistory.setEditable(false);
		chatHistory.setBackground(backgroundColor);
		chatHistory.setForeground(textColor);
		chatHistory.setFont(agencyPlain20);
		chatHistory.setLineWrap(true);
		chatHistory.setWrapStyleWord(true);
		chatHistory.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JScrollPane chatScrollPane = new JScrollPane(chatHistory);
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setBackground(backgroundColor);

		DefaultCaret caret = (DefaultCaret) chatHistory.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		// Chat input area
		chatInput = new JTextField();
		chatInput.setPreferredSize(new Dimension(800, 51));
		chatInput.setBackground(backgroundColor);
		chatInput.setForeground(textColor);
		chatInput.setFont(agencyPlain24);
		chatInput.setBorder(BorderFactory.createCompoundBorder(chatHistory.getBorder(),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));

		chatPanel.add(chatScrollPane, BorderLayout.CENTER);
		chatPanel.add(chatInput, BorderLayout.SOUTH);

		return chatPanel;
	}

	/**
	 * Initializes the menu bar and its items.
	 */
	private void initializeMenu() {
		// Menu bar setup
		menuBar = new JMenuBar();
		menuBar.setBackground(backgroundColor);

		// Settings menu
		settings = new JMenu(loader.getResourceBundle().getString("settings.menu"));
		settings.setFont(agencyPlain20);
		settings.setForeground(textColor);
		settings.setBackground(backgroundColor);

		settingsItem1 = new JMenuItem(loader.getResourceBundle().getString("settings.host"));
		settingsItem2 = new JMenuItem(loader.getResourceBundle().getString("settings.join"));
		settingsItem3 = new JMenuItem(loader.getResourceBundle().getString("settings.disconnect"));
		settingsItem4 = new JMenuItem(loader.getResourceBundle().getString("settings.language"));
		settingsItem5 = new JMenuItem(loader.getResourceBundle().getString("settings.background"));
		settingsItem6 = new JMenuItem(loader.getResourceBundle().getString("settings.exit"));

		hostWindow = new JWindow();
		hostWindow.add(createHostPanel());
		clientWindow = new JWindow();
		clientWindow.add(createClientPanel());

		// Set foreground and background colors for each menu item
		settingsItem1.setForeground(textColor);
		settingsItem1.setBackground(backgroundColor);

		settingsItem2.setForeground(textColor);
		settingsItem2.setBackground(backgroundColor);

		settingsItem3.setForeground(textColor);
		settingsItem3.setBackground(backgroundColor);

		settingsItem4.setForeground(textColor);
		settingsItem4.setBackground(backgroundColor);

		settingsItem5.setForeground(textColor);
		settingsItem5.setBackground(backgroundColor);

		settingsItem6.setForeground(textColor);
		settingsItem6.setBackground(backgroundColor);

		settingsItem1.addActionListener(e -> controller.getMenuController().hostGame(e));
		settingsItem2.addActionListener(e -> controller.getMenuController().joinGame(e));
		settingsItem3.addActionListener(e -> controller.getMenuController().disconnect(e));
		settingsItem4.addActionListener(e -> controller.getMenuController().switchLanguage(e));
		settingsItem5.addActionListener(e -> controller.getMenuController().changeBackgroundColor(e));
		settingsItem6.addActionListener(e -> controller.getMenuController().exitGame(e));

		settings.add(settingsItem1);
		settings.add(settingsItem2);
		settings.add(settingsItem3);
		settings.add(settingsItem4);
		settings.add(settingsItem5);
		settings.add(settingsItem6);

		// Info menu
		info = new JMenu(loader.getResourceBundle().getString("info.menu"));
		info.setFont(agencyPlain20);
		info.setForeground(textColor);
		info.setBackground(backgroundColor);

		infoItem1 = new JMenuItem(loader.getResourceBundle().getString("info.help"));
		infoItem2 = new JMenuItem(loader.getResourceBundle().getString("info.about"));

		// Set foreground and background colors for each menu item
		infoItem1.setForeground(textColor);
		infoItem1.setBackground(backgroundColor);

		infoItem2.setForeground(textColor);
		infoItem2.setBackground(backgroundColor);

		infoItem1.addActionListener(e -> controller.getMenuController().displayHelp(e));
		infoItem2.addActionListener(e -> controller.getMenuController().displayAbout(e));

		info.add(infoItem1);
		info.add(infoItem2);

		// Game menu
		game = new JMenu(loader.getResourceBundle().getString("game.menu"));
		game.setFont(agencyPlain20);
		game.setForeground(textColor);
		game.setBackground(backgroundColor);

		gameItem1 = new JMenuItem(loader.getResourceBundle().getString("game.save"));
		gameItem2 = new JMenuItem(loader.getResourceBundle().getString("game.load"));
		gameItem3 = new JMenuItem(loader.getResourceBundle().getString("game.start"));
		gameItem4 = new JMenuItem(loader.getResourceBundle().getString("game.swap"));

		// Set foreground and background colors for each menu item
		gameItem1.setForeground(textColor);
		gameItem1.setBackground(backgroundColor);

		gameItem2.setForeground(textColor);
		gameItem2.setBackground(backgroundColor);

		gameItem3.setForeground(textColor);
		gameItem3.setBackground(backgroundColor);

		gameItem4.setForeground(textColor);
		gameItem4.setBackground(backgroundColor);

		gameItem1.addActionListener(e -> controller.getMenuController().saveGame(e));
		gameItem2.addActionListener(e -> controller.getMenuController().loadGame(e));
		gameItem3.addActionListener(e -> controller.getMenuController().startOrRestartGame(e));
		gameItem4.addActionListener(e -> controller.getMenuController().swapGridView(e));

		game.add(gameItem1);
		game.add(gameItem2);
		game.add(gameItem3);
		game.add(gameItem4);

		// Add menus to the menu bar
		menuBar.add(settings);
		menuBar.add(info);
		menuBar.add(game);

		setJMenuBar(menuBar);
	}

	/**
	 * Displays the host window for setting up the game server. If the client window
	 * is visible, it hides and disposes of it. Creates and displays the host window
	 * with the necessary UI components.
	 */
	public void displayHostWindow() {
		if (clientWindow.isVisible()) {
			clientWindow.setVisible(false);
		}
		clientWindow.dispose();
		hostWindow = new JWindow(this);
		hostWindow.add(createHostPanel());
		hostWindow.pack();
		hostWindow.setLocationRelativeTo(this);
		hostWindow.setVisible(true);
	}

	/**
	 * Creates and returns a JPanel containing the UI components for the host panel.
	 * This includes input fields for the host name and port, and buttons for
	 * starting the hosting and exiting the setup.
	 *
	 * @return The JPanel containing the host panel UI components.
	 */
	private JPanel createHostPanel() {
		JPanel hostPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		hostPanel.setBackground(backgroundColor);
		hostPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		hostNameLabel = new JLabel(loader.getResourceBundle().getString("host.name"), SwingConstants.RIGHT);
		hostNameLabel.setForeground(textColor);
		hostNameLabel.setFont(agencyPlain24);

		hostNameInput = new JTextField();
		hostNameInput.setBackground(backgroundColor);
		hostNameInput.setForeground(textColor);
		hostNameInput.setBorder(new LineBorder(textColor));
		hostNameInput.setFont(agencyPlain24);
		hostNameInput.setPreferredSize(new Dimension(100, 30));

		hostPortLabel = new JLabel(loader.getResourceBundle().getString("host.port"), SwingConstants.RIGHT);
		hostPortLabel.setForeground(textColor);
		hostPortLabel.setFont(agencyPlain24);

		hostPortInput = new JTextField();
		hostPortInput.setBackground(backgroundColor);
		hostPortInput.setForeground(textColor);
		hostPortInput.setBorder(new LineBorder(textColor));
		hostPortInput.setFont(agencyPlain24);
		hostPortInput.setPreferredSize(new Dimension(100, 30));

		hostStartButton = new JButton(loader.getResourceBundle().getString("host.host"));
		hostStartButton.setBackground(backgroundColor);
		hostStartButton.setForeground(textColor);
		hostStartButton.setBorder(new LineBorder(textColor));
		hostStartButton.setFont(agencyPlain24);
		hostStartButton.setPreferredSize(new Dimension(100, 40));
		hostStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = hostNameInput.getText();
				String portString = hostPortInput.getText();
				try {
					int port = Integer.parseInt(portString);
					if (port < 10000 || port > 65535) {
						chatHistory.append(loader.getResourceBundle().getString("host.error") + "\n");
						return;
					}
					controller.setServerController(new ServerController(controller, model, loader, name, port, null));
					controller.getServerController().host();
					hostWindow.dispose();
				} catch (NumberFormatException ex) {
					chatHistory.append(loader.getResourceBundle().getString("host.error") + "\n");
				}
			}
		});

		hostCloseButton = new JButton(loader.getResourceBundle().getString("settings.exit"));
		hostCloseButton.setBackground(backgroundColor);
		hostCloseButton.setForeground(textColor);
		hostCloseButton.setBorder(new LineBorder(textColor));
		hostCloseButton.setFont(agencyPlain24);
		hostCloseButton.setPreferredSize(new Dimension(100, 40));
		hostCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hostWindow != null) {
					hostWindow.dispose();
				}
			}
		});

		hostPanel.add(hostNameLabel);
		hostPanel.add(hostNameInput);
		hostPanel.add(hostPortLabel);
		hostPanel.add(hostPortInput);
		hostPanel.add(new JLabel());
		hostPanel.add(hostStartButton);
		hostPanel.add(new JLabel());
		hostPanel.add(hostCloseButton);

		return hostPanel;
	}

	/**
	 * Displays the client window for joining a game server. If the host window is
	 * visible, it hides and disposes of it. Creates and displays the client window
	 * with the necessary UI components.
	 */
	public void displayClientWindow() {
		if (hostWindow.isVisible()) {
			hostWindow.setVisible(false);
		}
		hostWindow.dispose();
		clientWindow = new JWindow(this);
		clientWindow.add(createClientPanel());
		clientWindow.pack();
		clientWindow.setLocationRelativeTo(this);
		clientWindow.setVisible(true);
	}

	/**
	 * Creates and returns a JPanel containing the UI components for the client
	 * panel. This includes input fields for the client name, port, address, and
	 * buttons for the joining and exiting the setup.
	 *
	 * @return The JPanel containing the client panel UI components.
	 */
	private JPanel createClientPanel() {
		JPanel clientPanel = new JPanel(new GridLayout(5, 2, 5, 5));
		clientPanel.setBackground(backgroundColor);
		clientPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		clientNameLabel = new JLabel(loader.getResourceBundle().getString("join.name"), SwingConstants.RIGHT);
		clientNameLabel.setForeground(textColor);
		clientNameLabel.setFont(agencyPlain24);

		clientNameInput = new JTextField();
		clientNameInput.setBackground(backgroundColor);
		clientNameInput.setForeground(textColor);
		clientNameInput.setBorder(new LineBorder(textColor));
		clientNameInput.setFont(agencyPlain24);
		clientNameInput.setPreferredSize(new Dimension(100, 30));

		clientPortLabel = new JLabel(loader.getResourceBundle().getString("join.port"), SwingConstants.RIGHT);
		clientPortLabel.setForeground(textColor);
		clientPortLabel.setFont(agencyPlain24);

		clientPortInput = new JTextField();
		clientPortInput.setBackground(backgroundColor);
		clientPortInput.setForeground(textColor);
		clientPortInput.setBorder(new LineBorder(textColor));
		clientPortInput.setFont(agencyPlain24);
		clientPortInput.setPreferredSize(new Dimension(100, 30));

		clientAddressLabel = new JLabel(loader.getResourceBundle().getString("join.address"), SwingConstants.RIGHT);
		clientAddressLabel.setForeground(textColor);
		clientAddressLabel.setFont(agencyPlain24);

		clientAddressInput = new JTextField();
		clientAddressInput.setBackground(backgroundColor);
		clientAddressInput.setForeground(textColor);
		clientAddressInput.setBorder(new LineBorder(textColor));
		clientAddressInput.setFont(agencyPlain24);
		clientAddressInput.setPreferredSize(new Dimension(100, 30));

		clientJoinButton = new JButton(loader.getResourceBundle().getString("join.connect"));
		clientJoinButton.setBackground(backgroundColor);
		clientJoinButton.setForeground(textColor);
		clientJoinButton.setBorder(new LineBorder(textColor));
		clientJoinButton.setFont(agencyPlain24);
		clientJoinButton.setPreferredSize(new Dimension(100, 40));
		clientJoinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = clientNameInput.getText();
				String portString = clientPortInput.getText();
				String address = clientAddressInput.getText();
				try {
					int port = Integer.parseInt(portString);
					if (port < 10000 || port > 65535) {
						chatHistory.append(loader.getResourceBundle().getString("host.error") + "\n");
						return;
					}
					controller
							.setServerController(new ServerController(controller, model, loader, name, port, address));
					controller.getServerController().connect();
					if (controller.getServerController().getClientThread() == null) {
						return;
					}
					clientWindow.dispose();
				} catch (NumberFormatException ex) {
					chatHistory.append(loader.getResourceBundle().getString("host.error")  + "\n");
				}
			}
		});

		clientCloseButton = new JButton(loader.getResourceBundle().getString("settings.exit"));
		clientCloseButton.setBackground(backgroundColor);
		clientCloseButton.setForeground(textColor);
		clientCloseButton.setBorder(new LineBorder(textColor));
		clientCloseButton.setFont(agencyPlain24);
		clientCloseButton.setPreferredSize(new Dimension(100, 40));
		clientCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (clientWindow != null) {
					clientWindow.dispose();
				}
			}
		});

		clientPanel.add(clientNameLabel);
		clientPanel.add(clientNameInput);
		clientPanel.add(clientPortLabel);
		clientPanel.add(clientPortInput);
		clientPanel.add(clientAddressLabel);
		clientPanel.add(clientAddressInput);
		clientPanel.add(new JLabel());
		clientPanel.add(clientJoinButton);
		clientPanel.add(new JLabel());
		clientPanel.add(clientCloseButton);

		return clientPanel;
	}

	/**
	 * Sets the controller for the BattleshipView.
	 *
	 * @param controller the BattleshipController to be set
	 */
	public void setController(BattleshipController controller) {
		this.controller = controller;
	}

	/**
	 * Sets the model for the BattleshipView.
	 *
	 * @param model the BattleshipModel to be set
	 */
	public void setModel(BattleshipModel model) {
		this.model = model;
	}

	/**
	 * Sets the time label.
	 *
	 * @param timeLabel the JLabel to be set
	 */
	public void setTimeLabel(JLabel timeLabel) {
		this.timeLabel = timeLabel;
	}

	/**
	 * Sets the background color.
	 * 
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets the textColor.
	 * 
	 * @param textColor
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * Updates the UI to show the current grid view.
	 */
	public void updateGridView() {
		if (!controller.getGameController().showingPlayerGrid) {
			// Show opponent's grid view
			for (int row = 0; row < gridButtons.length; row++) {
				for (int col = 0; col < gridButtons[row].length; col++) {
					JButton button = gridButtons[row][col];
					button.setEnabled(true); // Enable buttons for opponent's grid
					int opponentCell = model.getOpponentGrid()[row][col];
					if (opponentCell == BattleshipModel.HIT) {
						button.setIcon(hitIndicator); // Display hit indicator
						button.setEnabled(false); // Disable button after hit
					} else if (opponentCell == BattleshipModel.MISS) {
						button.setIcon(missIndicator); // Display miss indicator
						button.setEnabled(false); // Disable button after miss
					} else {
						button.setIcon(null); // Clear icon if neither hit nor miss
					}
				}
			}
		} else {
			// Show player's grid view
			controller.getGameController().disableButtons(); // Disable all buttons on player's grid
			for (int row = 0; row < gridButtons.length; row++) {
				for (int col = 0; col < gridButtons[row].length; col++) {
					gridButtons[row][col].setIcon(null);
				}
			}
			for (ShipModel ship : model.getPlayerFleet()) {
				CoordinateModel coordinate = ship.getCoordinateHead();
				int row = coordinate.getRow();
				int col = coordinate.getColumn();
				for (int i = 0; i < ship.getLength(); i++) {
					JButton button;
					if (ship.isHorizontal()) {
						button = gridButtons[row][col + i];
					} else {
						button = gridButtons[row + i][col];
					}

					// Determine which icon to set based on ship orientation and position
					ImageIcon icon;
					if (i == 0) {
						icon = ship.isHorizontal() ? horizontalShipHead : verticalShipHead;
					} else if (i == ship.getLength() - 1) {
						icon = ship.isHorizontal() ? horizontalShipEnd : verticalShipEnd;
					} else {
						icon = ship.isHorizontal() ? horizontalShipBody : verticalShipBody;
					}
					button.setIcon(icon);
				}
			}
			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 10; col++) {
					if (model.getPlayerGrid()[row][col] == BattleshipModel.HIT) {
						gridButtons[row][col].setIcon(hitIndicator);
					} else if (model.getPlayerGrid()[row][col] == BattleshipModel.MISS) {
						gridButtons[row][col].setIcon(missIndicator);
					}
				}
			}
		}
	}

	/**
	 * Updates the opponent's fleet information with the given ship counts.
	 *
	 * @param opponentFleet a list of ShipModels representing the remaining ships
	 *                      for the opponent
	 */
	public void updateOpponentInfo(List<ShipModel> opponentFleet) {
		opponentInfoLabel.setText(loader.getResourceBundle().getString("opponent.info"));
		for (ShipModel ship : opponentFleet) {
			if (ship.isSunk()) {
				switch (ship.getName()) {
				case "aircraft":
					opponentInfoLabels[0].setText(loader.getResourceBundle().getString("ship.aircraft") + " X");
					break;
				case "battleship":
					opponentInfoLabels[1].setText(loader.getResourceBundle().getString("ship.battleship") + " X");
					break;
				case "cruiser":
					opponentInfoLabels[2].setText(loader.getResourceBundle().getString("ship.cruiser") + " X");
					break;
				case "destroyer":
					opponentInfoLabels[3].setText(loader.getResourceBundle().getString("ship.destroyer") + " X");
					break;
				case "submarine":
					opponentInfoLabels[4].setText(loader.getResourceBundle().getString("ship.submarine") + " X");
					break;
				}
			} else {
				switch (ship.getName()) {
				case "aircraft":
					opponentInfoLabels[0].setText(loader.getResourceBundle().getString("ship.aircraft"));
					break;
				case "battleship":
					opponentInfoLabels[1].setText(loader.getResourceBundle().getString("ship.battleship"));
					break;
				case "cruiser":
					opponentInfoLabels[2].setText(loader.getResourceBundle().getString("ship.cruiser"));
					break;
				case "destroyer":
					opponentInfoLabels[3].setText(loader.getResourceBundle().getString("ship.destroyer"));
					break;
				case "submarine":
					opponentInfoLabels[4].setText(loader.getResourceBundle().getString("ship.submarine"));
					break;
				}
			}
		}
		opponentInfoPanel.setBorder(new LineBorder(textColor));
	}

	/**
	 * Updates the player's fleet information with the given ship counts.
	 *
	 * @param playerFleet a list of ShipModels representing the remaining ships for
	 *                    the player
	 */
	public void updatePlayerInfo(List<ShipModel> playerFleet) {
		playerInfoLabel.setText(loader.getResourceBundle().getString("player.info"));
		for (ShipModel ship : playerFleet) {
			if (ship.isSunk()) {
				switch (ship.getName()) {
				case "aircraft":
					playerInfoLabels[0].setText(loader.getResourceBundle().getString("ship.aircraft") + " X");
					break;
				case "battleship":
					playerInfoLabels[1].setText(loader.getResourceBundle().getString("ship.battleship") + " X");
					break;
				case "cruiser":
					playerInfoLabels[2].setText(loader.getResourceBundle().getString("ship.cruiser") + " X");
					break;
				case "destroyer":
					playerInfoLabels[3].setText(loader.getResourceBundle().getString("ship.destroyer") + " X");
					break;
				case "submarine":
					playerInfoLabels[4].setText(loader.getResourceBundle().getString("ship.submarine") + " X");
					break;
				}
			} else {
				switch (ship.getName()) {
				case "aircraft":
					playerInfoLabels[0].setText(loader.getResourceBundle().getString("ship.aircraft"));
					break;
				case "battleship":
					playerInfoLabels[1].setText(loader.getResourceBundle().getString("ship.battleship"));
					break;
				case "cruiser":
					playerInfoLabels[2].setText(loader.getResourceBundle().getString("ship.cruiser"));
					break;
				case "destroyer":
					playerInfoLabels[3].setText(loader.getResourceBundle().getString("ship.destroyer"));
					break;
				case "submarine":
					playerInfoLabels[4].setText(loader.getResourceBundle().getString("ship.submarine"));
					break;
				}
			}
		}
		playerInfoPanel.setBorder(new LineBorder(textColor));
	}

	/**
	 * Updates the turn start menu item.
	 */
	public void updateStartMenu() {
		if (controller.getMenuController().gameStart) {
			gameItem3.setText(loader.getResourceBundle().getString("game.restart"));
		} else {
			gameItem3.setText(loader.getResourceBundle().getString("game.start"));
		}
	}

	/**
	 * Updates the time label.
	 */
	public void updateTimeLabel() {
		timeLabel.setText(loader.getResourceBundle().getString("game.time") + " : "
				+ controller.getGameController().getTimeRemaining());
		gameInfoPanel.setBorder(new LineBorder(textColor));
	}

	/**
	 * Updates the turn label to indicate whose turn it is.
	 */
	public void updateTurnLabel() {
		if (controller.getGameController().playerTurn) {
			turnLabel.setText(loader.getResourceBundle().getString("game.turn"));
		} else {
			turnLabel.setText(loader.getResourceBundle().getString("game.next"));
		}
		gameInfoPanel.setBorder(new LineBorder(textColor));
	}

	/**
	 * Updates the win/loss labels.
	 */
	public void updateWinLoseLabel() {
		winLabel.setText(loader.getResourceBundle().getString("game.wins") + " : " + model.getWins());
		loseLabel.setText(loader.getResourceBundle().getString("game.losses") + " : " + model.getLoses());
	}

	/**
	 * Switches to the opponent's grid view.
	 */
	public void viewOpponentGrid() {
		// Show player's grid view
		controller.getGameController().disableButtons(); // Disable all buttons on player's grid
		for (int row = 0; row < gridButtons.length; row++) {
			for (int col = 0; col < gridButtons[row].length; col++) {
				gridButtons[row][col].setIcon(null);
			}
		}
		for (ShipModel ship : model.getOpponentFleet()) {
			CoordinateModel coordinate = ship.getCoordinateHead();
			int row = coordinate.getRow();
			int col = coordinate.getColumn();
			for (int i = 0; i < ship.getLength(); i++) {
				JButton button;
				if (ship.isHorizontal()) {
					button = gridButtons[row][col + i];
				} else {
					button = gridButtons[row + i][col];
				}

				// Determine which icon to set based on ship orientation and position
				ImageIcon icon;
				if (i == 0) {
					icon = ship.isHorizontal() ? horizontalShipHead : verticalShipHead;
				} else if (i == ship.getLength() - 1) {
					icon = ship.isHorizontal() ? horizontalShipEnd : verticalShipEnd;
				} else {
					icon = ship.isHorizontal() ? horizontalShipBody : verticalShipBody;
				}
				button.setIcon(icon);
			}
		}
	}

	/**
	 * Updates the host panel with the appropriate localization strings.
	 */
	private void updateHostPanel() {
		hostNameLabel.setText(loader.getResourceBundle().getString("host.name"));
		hostPortLabel.setText(loader.getResourceBundle().getString("host.port"));
		hostStartButton.setText(loader.getResourceBundle().getString("host.host"));
		hostCloseButton.setText(loader.getResourceBundle().getString("settings.exit"));
	}

	/**
	 * Updates the client panel with the appropriate localization strings.
	 */
	private void updateClientPanel() {
		clientNameLabel.setText(loader.getResourceBundle().getString("join.name"));
		clientPortLabel.setText(loader.getResourceBundle().getString("join.port"));
		clientAddressLabel.setText(loader.getResourceBundle().getString("join.address"));
		clientJoinButton.setText(loader.getResourceBundle().getString("join.connect"));
		clientCloseButton.setText(loader.getResourceBundle().getString("settings.exit"));
	}

	/**
	 * Returns the chat history text area.
	 *
	 * @return a JTextArea representing the chat history
	 */
	public JTextArea getChatHistory() {
		return chatHistory;
	}

	/**
	 * Returns the chat input text field.
	 *
	 * @return a JTextField representing the chat input field
	 */
	public JTextField getChatInput() {
		return chatInput;
	}

	/**
	 * Returns the grid buttons used in the game.
	 *
	 * @return a 2D array of JButton representing the game grid
	 */
	public JButton[][] getGridButtons() {
		return gridButtons;
	}

	/**
	 * Returns the time label.
	 *
	 * @return the time label as JLabel
	 */
	public JLabel getTimeLabel() {
		return timeLabel;
	}

	/**
	 * Reloads text from the resource bundle.
	 */
	public void reloadText() {
		// Update logo label
		logoLabel.setText(loader.getResourceBundle().getString("panel.title"));

		updateTurnLabel();
		updateTimeLabel();
		updateWinLoseLabel();

		// Update infoLabels for player and opponent fleets
		updatePlayerInfo(model.getPlayerFleet());
		updateOpponentInfo(model.getOpponentFleet());

		// Update menu items
		settings.setText(loader.getResourceBundle().getString("settings.menu"));
		settingsItem1.setText(loader.getResourceBundle().getString("settings.host"));
		settingsItem2.setText(loader.getResourceBundle().getString("settings.join"));
		settingsItem4.setText(loader.getResourceBundle().getString("settings.language"));
		settingsItem5.setText(loader.getResourceBundle().getString("settings.background"));
		settingsItem6.setText(loader.getResourceBundle().getString("settings.exit"));

		updateHostPanel();
		updateClientPanel();

		info.setText(loader.getResourceBundle().getString("info.menu"));
		infoItem1.setText(loader.getResourceBundle().getString("info.help"));
		infoItem2.setText(loader.getResourceBundle().getString("info.about"));

		game.setText(loader.getResourceBundle().getString("game.menu"));
		gameItem1.setText(loader.getResourceBundle().getString("game.save"));
		gameItem2.setText(loader.getResourceBundle().getString("game.load"));
		if (controller.getMenuController().gameStart) {
			gameItem3.setText(loader.getResourceBundle().getString("game.restart"));
		} else {
			gameItem3.setText(loader.getResourceBundle().getString("game.start"));
		}
		gameItem4.setText(loader.getResourceBundle().getString("game.swap"));
	}

	/**
	 * Changes the background color, text color, and border color of the various UI
	 * components.
	 *
	 * @param newBackgroundColor The new background color to set.
	 * @param newTextColor       The new text color to set.
	 */
	public void updateColors(Color newBackgroundColor, Color newTextColor) {
		// Update background color
		backgroundColor = newBackgroundColor;
		textColor = newTextColor;

		// Update game panel
		gamePanel.setBackground(backgroundColor);
		gamePanel.setBorder(new LineBorder(textColor));

		// Update logo panel
		logoPanel.setBackground(backgroundColor);
		logoLabel.setForeground(textColor);

		// Update grid panel
		gridPanel.setBackground(backgroundColor);
		gridPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(0, 51, 0, 106)));
		
		// Update grid buttons
		for (int row = 0; row < gridButtons.length; row++) {
			for (int col = 0; col < gridButtons[row].length; col++) {
				gridButtons[row][col].setBackground(backgroundColor);
				gridButtons[row][col].setForeground(textColor);
				gridButtons[row][col].setBorder(new LineBorder(textColor));
			}
		}

		// Update grid labels
		for (JLabel label : gridColLabels) {
			label.setForeground(textColor);
			label.setBorder(new LineBorder(backgroundColor));
		}
		for (JLabel label : gridRowLabels) {
			label.setForeground(textColor);
			label.setBorder(new LineBorder(backgroundColor));
		}

		// Update info panel
		infoPanel.setBackground(backgroundColor);
		infoPanel.setBorder(new LineBorder(textColor));

		// Update game info panel
		gameInfoPanel.setBackground(backgroundColor);
		gameInfoPanel.setBorder(new LineBorder(textColor));

		// Update labels in game info panel
		turnLabel.setForeground(textColor);
		timeLabel.setForeground(textColor);
		winLabel.setForeground(textColor);
		loseLabel.setForeground(textColor);

		// Update player info panel
		playerInfoLabel.setForeground(textColor);
		playerInfoPanel.setBackground(backgroundColor);
		playerInfoPanel.setBorder(new LineBorder(textColor));
		playerInfoPanel.setForeground(textColor);

		// Update player info labels
		for (JLabel label : playerInfoLabels) {
			label.setForeground(textColor);
		}

		// Update opponent info panel
		opponentInfoLabel.setForeground(textColor);
		;
		opponentInfoPanel.setBackground(backgroundColor);
		opponentInfoPanel.setBorder(new LineBorder(textColor));
		opponentInfoPanel.setForeground(textColor);

		// Update opponent info labels
		for (JLabel label : opponentInfoLabels) {
			label.setForeground(textColor);
		}

		// Update chat panel
		chatPanel.setBackground(backgroundColor);
		chatPanel.setBorder(new LineBorder(textColor));

		// Update chat history
		chatHistory.setBackground(backgroundColor);
		chatHistory.setForeground(textColor);
		chatHistory.setBorder(BorderFactory.createCompoundBorder(new LineBorder(textColor),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// Update chat input
		chatInput.setBackground(backgroundColor);
		chatInput.setForeground(textColor);
		chatInput.setBorder(BorderFactory.createCompoundBorder(chatHistory.getBorder(),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));

		// Update menu bar
		menuBar.setBackground(backgroundColor);

		// Update settings menu
		settings.setBackground(backgroundColor);
		settings.setForeground(textColor);
		settingsItem1.setBackground(backgroundColor);
		settingsItem1.setForeground(textColor);
		settingsItem2.setBackground(backgroundColor);
		settingsItem2.setForeground(textColor);
		settingsItem3.setBackground(backgroundColor);
		settingsItem3.setForeground(textColor);
		settingsItem4.setBackground(backgroundColor);
		settingsItem4.setForeground(textColor);
		settingsItem5.setBackground(backgroundColor);
		settingsItem5.setForeground(textColor);
		settingsItem6.setBackground(backgroundColor);
		settingsItem6.setForeground(textColor);

		// Update info menu
		info.setBackground(backgroundColor);
		info.setForeground(textColor);
		infoItem1.setBackground(backgroundColor);
		infoItem1.setForeground(textColor);
		infoItem2.setBackground(backgroundColor);
		infoItem2.setForeground(textColor);

		// Update game menu
		game.setBackground(backgroundColor);
		game.setForeground(textColor);
		gameItem1.setBackground(backgroundColor);
		gameItem1.setForeground(textColor);
		gameItem2.setBackground(backgroundColor);
		gameItem2.setForeground(textColor);
		gameItem3.setBackground(backgroundColor);
		gameItem3.setForeground(textColor);
		gameItem4.setBackground(backgroundColor);
		gameItem4.setForeground(textColor);
	}

	public BattleshipController getController() {
		return controller;
	}

}