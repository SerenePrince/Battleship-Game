package battleship.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;

import battleship.LocalizationLoader;
import battleship.controller.ServerController.MessageType;
import battleship.model.BattleshipModel;
import battleship.view.BattleshipView;

/**
 * The MenuController class handles the menu options for the Battleship game.
 */
public class MenuController {

	private BattleshipController controller;
	private LocalizationLoader loader;
	private BattleshipModel model;
	private BattleshipView view;

	public boolean gameStart = false;
	private int currentColor = 0;

	/**
	 * Constructs a MenuController with the specified loader, model, view, and
	 * controller.
	 *
	 * @param loader     The LocalizationLoader instance.
	 * @param model      The BattleshipModel instance.
	 * @param view       The BattleshipView instance.
	 * @param controller The BattleshipController instance.
	 */
	public MenuController(LocalizationLoader loader, BattleshipModel model, BattleshipView view,
			BattleshipController controller) {
		this.loader = loader;
		this.model = model;
		this.view = view;
		this.controller = controller;
	}

	/**
	 * Hosts a new game session.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void hostGame(ActionEvent e) {
		if (controller.getServerController() == null && !gameStart) {
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("host.explain"));
			view.displayHostWindow();
		}
	}

	/**
	 * Joins an existing game session.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void joinGame(ActionEvent e) {
		if (controller.getServerController() == null && !gameStart) {
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("join.explain"));
			view.displayClientWindow();
		}
	}

	/**
	 * Disconnects from the current game session.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void disconnect(ActionEvent e) {
		if (controller.getServerController() != null) {
			controller.getServerController().disconnect();
			controller.getChatController().sendChatMessage(loader.getResourceBundle().getString("host.disconnected"));
		}
	}

	/**
	 * Switches the language of the game interface.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void switchLanguage(ActionEvent e) {
		loader.switchLanguage(loader.getLanguage().equals("en") ? "fr" : "en");
		view.reloadText();
	}

	/**
	 * Changes the background color of the game frame.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void changeBackgroundColor(ActionEvent e) {
		switch (currentColor) {
		case 0:
			view.updateColors(new Color(0x0827F5), Color.WHITE);
			currentColor = 1;
			break;
		case 1:
			view.updateColors(Color.BLACK, new Color(0x00FF00));
			currentColor = 0;
			break;
		}
	}

	/**
	 * Exits the game application.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void exitGame(ActionEvent e) {
		System.exit(0);
	}

	/**
	 * Saves the current game state.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void saveGame(ActionEvent e) {
		controller.getChatController().receiveChatMessage("In development...");
	}

	/**
	 * Loads a previously saved game state.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void loadGame(ActionEvent e) {
		controller.getChatController().receiveChatMessage("In development...");
	}

	/**
	 * Starts or restarts the game.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void startOrRestartGame(ActionEvent e) {
		if (gameStart) {
			if (controller.getServerController() == null) {
				gameStart = false;
				controller.getGameController().getTimer().cancel();
				controller.getGameController().setTimeRemaining(10);
				view.updateTimeLabel();
				view.updateWinLoseLabel();
				view.updateStartMenu();
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.restart"));
				controller.getShipController().resetGrid();
				model.resetModel();
				return;
			} else if (controller.getServerController().getRestartFlag() >= 1) {
				gameStart = false;
				controller.getGameController().getTimer().cancel();
				controller.getGameController().setTimeRemaining(10);
				view.updateTimeLabel();
				view.updateWinLoseLabel();
				view.updateStartMenu();
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.restart"));
				controller.getShipController().resetGrid();
				model.resetModel();
				return;
			} else if (controller.getServerController().getRestartFlag() == 0) {
				controller.getServerController().sendData(MessageType.RESTART, "1");
				gameStart = false;
				controller.getGameController().getTimer().cancel();
				controller.getGameController().setTimeRemaining(10);
				view.updateTimeLabel();
				view.updateWinLoseLabel();
				view.updateStartMenu();
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.restart"));
				controller.getShipController().resetGrid();
				model.resetModel();
				controller.getServerController().setRestartFlag(-1);
				return;
			} else {
				controller.getChatController().sendChatMessage(loader.getResourceBundle().getString("game.request"));
				controller.getServerController().sendData(MessageType.RESTART, "0");
				return;
			}
		}
		if (controller.getServerController() == null) {
			gameStart = true;
			controller.getShipController().resetGrid();
			model.resetModel();
			view.updateStartMenu();
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.place"));
			controller.getShipController().initializeGridListeners();
			return;
		}
		if (!controller.getServerController().clientConnected) {
			controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("join.start"));
			return;
		}
		if (controller.getServerController().isHost == 1 && controller.getServerController().clientConnected) {
			controller.getServerController().sendData(MessageType.NAME, controller.getServerController().getName());
			controller.getServerController().sendData(MessageType.START, "START");
			return;
		}
	}

	/**
	 * Swaps between different grid views.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void swapGridView(ActionEvent e) {
		if (gameStart) {
			controller.getGameController().swapGridView();
		}
	}

	/**
	 * Displays information about the game.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void displayAbout(ActionEvent e) {
		String message = loader.getResourceBundle().getString("chat.about");
		controller.getChatController().receiveChatMessage(message);
	}

	/**
	 * Displays help information for the game.
	 *
	 * @param e The ActionEvent triggered by the user.
	 */
	public void displayHelp(ActionEvent e) {
		String message = loader.getResourceBundle().getString("chat.help");
		controller.getChatController().receiveChatMessage(message);
	}
}
