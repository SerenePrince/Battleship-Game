package battleship.controller;

import battleship.LocalizationLoader;
import battleship.model.BattleshipModel;
import battleship.view.BattleshipView;

/**
 * Controller class for the Battleship game, responsible for initializing and
 * coordinating various components like menus, chat, game logic, and ship
 * placement.
 */
public class BattleshipController {

	private ChatController chatController;
	private GameController gameController;
	private MenuController menuController;
	private ShipController shipController;
	private ServerController serverController;

	/**
	 * Constructor for BattleshipController.
	 *
	 * @param loader LocalizationLoader instance for language settings.
	 * @param model  BattleshipModel instance for game logic.
	 * @param view   BattleshipView instance for UI management.
	 */
	public BattleshipController(LocalizationLoader loader, BattleshipModel model, BattleshipView view) {
		this.menuController = new MenuController(loader, model, view, this);
		this.chatController = new ChatController(view, this, loader);
		this.gameController = new GameController(loader, model, view, this);
		this.shipController = new ShipController(model, view, this);
	}

	/**
	 * Retrieves the ChatController instance.
	 *
	 * @return ChatController instance.
	 */
	public ChatController getChatController() {
		return chatController;
	}

	/**
	 * Retrieves the GameController instance.
	 *
	 * @return GameController instance.
	 */
	public GameController getGameController() {
		return gameController;
	}

	/**
	 * Retrieves the MenuController instance.
	 *
	 * @return MenuController instance.
	 */
	public MenuController getMenuController() {
		return menuController;
	}

	/**
	 * Retrieves the ShipController instance.
	 *
	 * @return ShipController instance.
	 */
	public ShipController getShipController() {
		return shipController;
	}

	/**
	 * Retrieves the ServerController instance.
	 *
	 * @return ServerController instance.
	 */
	public ServerController getServerController() {
		return serverController;
	}

	/**
	 * Sets the ServerController instance.
	 *
	 * @param serverController ServerController instance to be set.
	 */
	public void setServerController(ServerController serverController) {
		this.serverController = serverController;
	}
}
