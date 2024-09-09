package battleship.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import battleship.LocalizationLoader;
import battleship.controller.BattleshipController;
import battleship.controller.ServerController.MessageType;
import battleship.view.BattleshipView;

/**
 * The BattleshipModel class represents the game model for Battleship. It
 * manages the game grids, fleets, ship placement, firing actions, and game
 * state checks.
 */
public class BattleshipModel implements Serializable {

	private static final long serialVersionUID = 1L;

	// Constants for grid states
	public static final int EMPTY = 0;
	public static final int HIT = 3;
	public static final int MISS = 2;
	public static final int OCCUPIED = 1;

	private BattleshipController controller;
	private LocalizationLoader loader;
	private BattleshipView view;

	private ImageIcon hitIndicator = new ImageIcon("resources/images/hit.png");
	private ImageIcon missIndicator = new ImageIcon("resources/images/miss.png");

	private List<ShipModel> opponentFleet;
	private int[][] opponentGrid;

	private List<ShipModel> playerFleet;
	private int[][] playerGrid;

	private int loses;
	private int wins;

	/**
	 * Constructor for BattleshipModel. Initializes player and opponent grids and
	 * fleets.
	 *
	 * @param loader The localization loader instance to load game resources.
	 */
	public BattleshipModel(LocalizationLoader loader) {
		this.loader = loader;
		this.playerGrid = new int[10][10];
		this.opponentGrid = new int[10][10];
		this.playerFleet = new ArrayList<>();
		this.opponentFleet = new ArrayList<>();
		initializeGrid(playerGrid);
		initializeGrid(opponentGrid);
		initializeFleet(playerFleet);
		initializeFleet(opponentFleet);
		this.wins = 0;
		this.loses = 0;
	}

	/**
	 * Sets the BattleshipController instance associated with this model.
	 *
	 * @param controller The BattleshipController instance to set.
	 */
	public void setController(BattleshipController controller) {
		this.controller = controller;
	}

	/**
	 * Increases the number of loses by 1.
	 */
	public void setLoses(int loses) {
		if (loses == 1) {
			this.loses++;
		} else {
			this.loses = loses;
		}
	}

	/**
	 * Sets the opponent's fleet of ships.
	 *
	 * @param opponentFleet The list of ShipModel objects to set as the opponent's
	 *                      fleet.
	 */
	public void setOpponentFleet(List<ShipModel> opponentFleet) {
		this.opponentFleet = opponentFleet;
	}

	/**
	 * Sets the opponent's grid representation.
	 *
	 * @param opponentGrid The 2D integer array to set as the opponent's grid.
	 */
	public void setOpponentGrid(int[][] opponentGrid) {
		this.opponentGrid = opponentGrid;
	}

	/**
	 * Sets the player's fleet of ships.
	 *
	 * @param playerFleet The list of ShipModel objects to set as the player's
	 *                    fleet.
	 */
	public void setPlayerFleet(List<ShipModel> playerFleet) {
		this.playerFleet = playerFleet;
	}

	/**
	 * Sets the player's grid representation.
	 *
	 * @param playerGrid The 2D integer array to set as the player's grid.
	 */
	public void setPlayerGrid(int[][] playerGrid) {
		this.playerGrid = playerGrid;
	}

	/**
	 * Sets the BattleshipView instance associated with this model.
	 *
	 * @param view The BattleshipView instance to set.
	 */
	public void setView(BattleshipView view) {
		this.view = view;
	}

	/**
	 * Increases the number of wins by 1.
	 */
	public void setWins(int wins) {
		if (wins == 1) {
			this.wins++;
		} else {
			this.wins = wins;
		}
	}

	/**
	 * Retrieves the BattleshipController instance associated with this model.
	 *
	 * @return The BattleshipController instance.
	 */
	public BattleshipController getController() {
		return controller;
	}

	/**
	 * Retrieves the number of loses.
	 *
	 * @return The number of loses.
	 */
	public int getLoses() {
		return loses;
	}

	/**
	 * Retrieves the opponent's fleet of ships.
	 *
	 * @return The list of ShipModel objects representing the opponent's fleet.
	 */
	public List<ShipModel> getOpponentFleet() {
		return opponentFleet;
	}

	/**
	 * Retrieves the opponent's grid representation.
	 *
	 * @return The 2D integer array representing the opponent's grid.
	 */
	public int[][] getOpponentGrid() {
		return opponentGrid;
	}

	/**
	 * Retrieves the player's fleet of ships.
	 *
	 * @return The list of ShipModel objects representing the player's fleet.
	 */
	public List<ShipModel> getPlayerFleet() {
		return playerFleet;
	}

	/**
	 * Retrieves the player's grid representation.
	 *
	 * @return The 2D integer array representing the player's grid.
	 */
	public int[][] getPlayerGrid() {
		return playerGrid;
	}

	/**
	 * Retrieves the BattleshipView instance associated with this model.
	 *
	 * @return The BattleshipView instance.
	 */
	public BattleshipView getView() {
		return view;
	}

	/**
	 * Retrieves the number of wins.
	 *
	 * @return The number of wins.
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * Initializes the player and opponent fleets with default ships.
	 *
	 * @param fleet The list to initialize with ships.
	 */
	private void initializeFleet(List<ShipModel> fleet) {
		ShipModel ship1 = new ShipModel(loader, "aircraft", 5);
		ship1.setController(controller);
		ShipModel ship2 = new ShipModel(loader, "battleship", 4);
		ship2.setController(controller);
		ShipModel ship3 = new ShipModel(loader, "cruiser", 3);
		ship3.setController(controller);
		ShipModel ship4 = new ShipModel(loader, "destroyer", 3);
		ship4.setController(controller);
		ShipModel ship5 = new ShipModel(loader, "submarine", 2);
		ship5.setController(controller);
		fleet.add(ship1);
		fleet.add(ship2);
		fleet.add(ship3);
		fleet.add(ship4);
		fleet.add(ship5);
	}

	/**
	 * Initializes a grid with EMPTY cells.
	 *
	 * @param grid The grid to initialize.
	 */
	private void initializeGrid(int[][] grid) {
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				grid[row][col] = EMPTY;
			}
		}
	}

	/**
	 * Checks if a ship can be placed at the specified position on the grid.
	 *
	 * @param grid       The grid to check.
	 * @param row        The starting row index.
	 * @param col        The starting column index.
	 * @param length     The length of the ship.
	 * @param horizontal Flag indicating if the ship is placed horizontally or
	 *                   vertically.
	 * @return true if the ship can be placed, false otherwise.
	 */
	public boolean canPlaceShip(int[][] grid, int row, int col, int length, boolean horizontal) {
		// Check if a ship can be placed at the specified position
		if (horizontal) {
			if (col + length > 10)
				return false;
			for (int i = 0; i < length; i++) {
				if (col + i >= 10 || grid[row][col + i] != EMPTY)
					return false;
			}
		} else {
			if (row + length > 10)
				return false;
			for (int i = 0; i < length; i++) {
				if (row + i >= 10 || grid[row + i][col] != EMPTY)
					return false;
			}
		}
		return true;
	}

	/**
	 * Checks if all ships in a fleet are sunk.
	 *
	 * @param fleet The fleet to check.
	 * @return true if all ships in the fleet are sunk, false otherwise.
	 */
	public boolean isFleetSunk(List<ShipModel> fleet) {
		for (ShipModel ship : fleet) {
			if (!ship.isSunk()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the player won the game.
	 *
	 * @return true if the opponent's fleet is sunk, false otherwise.
	 */
	public boolean playerWon() {
		return isFleetSunk(getOpponentFleet());
	}

	/**
	 * Checks if the opponent won the game.
	 *
	 * @return true if the player's fleet is sunk, false otherwise.
	 */
	public boolean opponentWon() {
		return isFleetSunk(getPlayerFleet());
	}

	/**
	 * Checks if the game is over.
	 *
	 * @return true if either player's or opponent's fleet is sunk, false otherwise.
	 */
	public boolean isGameOver() {
		return isFleetSunk(getPlayerFleet()) || isFleetSunk(getOpponentFleet());
	}

	/**
	 * Checks if the game is over and displays the result.
	 */
	public void checkGameOver() {
		if (isGameOver()) {
			String message = playerWon() ? loader.getResourceBundle().getString("chat.win")
					: loader.getResourceBundle().getString("chat.lose");
			controller.getChatController().receiveChatMessage(message);
			if (playerWon()) {
				setWins(1);
			} else {
				setLoses(1);
			}
			view.updateWinLoseLabel();
			view.viewOpponentGrid();
			controller.getMenuController().gameStart = false;
			view.updateStartMenu();
			controller.getGameController().getTimer().cancel();
			controller.getGameController().setTimeRemaining(10);
			view.updateTimeLabel();
			controller.getGameController().disableButtons(); // Disable all grid buttons when game is over
			if (controller.getServerController() != null) {
				controller.getServerController().setRestartFlag(-1);
			}
		}
	}

	/**
	 * Places a ship on the grid at the specified position.
	 *
	 * @param grid       The grid to place the ship on.
	 * @param ship       The ship to place.
	 * @param row        The starting row index.
	 * @param col        The starting column index.
	 * @param horizontal Flag indicating if the ship is placed horizontally or
	 *                   vertically.
	 */
	public void placeShip(int[][] grid, ShipModel ship, int row, int col, boolean horizontal) {
		ship.setCoordinateHead(new CoordinateModel(row, col));
		int length = ship.getLength();
		ship.setHorizontal(horizontal);
		if (horizontal) {
			for (int i = 0; i < length; i++) {
				grid[row][col + i] = OCCUPIED;
				ship.addCoordinate(row, col + i);
			}
		} else {
			for (int i = 0; i < length; i++) {
				grid[row + i][col] = OCCUPIED;
				ship.addCoordinate(row + i, col);
			}
		}
		ship.setPlaced(true);
	}

	/**
	 * Randomly places opponent's ships on the grid.
	 */
	public void placeOpponentShipsRandomly() {
		for (ShipModel ship : opponentFleet) {
			if (!ship.isPlaced()) {
				Random random = new Random();
				boolean placed = false;
				while (!placed) {
					int row = random.nextInt(10);
					int col = random.nextInt(10);
					boolean horizontal = random.nextBoolean();
					if (canPlaceShip(opponentGrid, row, col, ship.getLength(), horizontal)) {
						placeShip(opponentGrid, ship, row, col, horizontal);
						ship.setHorizontal(horizontal);
						placed = true;
					}
				}
			}
		}
	}

	/**
	 * Places the opponent's ships on the grid.
	 *
	 * @param row          The starting row for placing the ship.
	 * @param col          The starting column for placing the ship.
	 * @param length       The length of the ship to be placed.
	 * @param isHorizontal True if the ship is to be placed horizontally, false if
	 *                     vertically.
	 */
	public void placeOpponentShips(int row, int col, int length, boolean isHorizontal) {
		for (ShipModel ship : opponentFleet) {
			if (!ship.isPlaced()) {
				if (canPlaceShip(opponentGrid, row, col, ship.getLength(), isHorizontal)
						&& length == ship.getLength()) {
					placeShip(opponentGrid, ship, row, col, isHorizontal);
					ship.setHorizontal(isHorizontal);
				}
			}
		}
	}

	/**
	 * Processes player's fire action on opponent's grid.
	 *
	 * @param row The row index of the target cell.
	 * @param col The column index of the target cell.
	 * @return HIT if the target cell contains an opponent's ship, MISS if it's
	 *         empty or already fired upon, -1 if the move is invalid.
	 */
	public int playerFire(int row, int col) {
		switch (opponentGrid[row][col]) {
		case EMPTY:
			opponentGrid[row][col] = MISS;
			return MISS;
		case OCCUPIED:
			opponentGrid[row][col] = HIT;
			return HIT;
		case MISS:
			return -1;
		case HIT:
			return -1;
		default:
			return -1;
		}
	}

	/**
	 * Initiates player's firing action at specified row and column.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	public void playerFires(int row, int col) {
		if (row == -1 && col == -1) {
			controller.getChatController().sendChatMessage(loader.getResourceBundle().getString("game.timeout"));
			controller.getServerController().sendData(MessageType.MOVE, row + "," + col);
		} else if (controller.getGameController().playerTurn) {
			JButton button = view.getGridButtons()[row][col];
			char columnLabel = (char) ('A' + col); // Convert column index to character label (A-J)
			controller.getChatController().receiveChatMessage(
					loader.getResourceBundle().getString("chat.fire") + " " + (row + 1) + columnLabel);
			int result = playerFire(row, col); // Perform player's firing action
			switch (result) {
			case BattleshipModel.HIT:
				for (ShipModel ship : getOpponentFleet()) {
					ship.registerHit(row, col);
				}
				button.setIcon(hitIndicator); // Display hit indicator icon on grid button
				button.setEnabled(false); // Disable button after hitting
				break;
			case BattleshipModel.MISS:
				button.setIcon(missIndicator); // Display miss indicator icon on grid button
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.miss"));
				button.setEnabled(false); // Disable button after miss
				break;
			case -1:
				controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached.
				return;
			default:
				controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached.
				return;
			}
			if (controller.getServerController() != null) {
				controller.getServerController().sendData(MessageType.MOVE, row + "," + col);
			}
		}
		controller.getGameController().playerTurn = false; // Switch turn to opponent after player's action
		view.updateOpponentInfo(getOpponentFleet());
		checkGameOver(); // Check if the game is over after each turn
		if (!isGameOver()) {
			view.updateTurnLabel();
			if (controller.getServerController() == null) {
				opponentFires(-1, -1); // Opponent continues firing if game is not over
			} else {
				controller.getGameController().opponentFires();
			}
		}
	}

	/**
	 * Processes opponent's fire action on player's grid.
	 *
	 * @param row The row index of the target cell.
	 * @param col The column index of the target cell.
	 * @return HIT if the target cell contains a player's ship, MISS if it's empty
	 *         or already fired upon, -1 if the move is invalid.
	 */
	public int opponentFire(int row, int col) {
		switch (playerGrid[row][col]) {
		case EMPTY:
			playerGrid[row][col] = MISS;
			return MISS;
		case OCCUPIED:
			playerGrid[row][col] = HIT;
			return HIT;
		case MISS:
			return -1;
		case HIT:
			return -1;
		default:
			return -1;
		}
	}

	/**
	 * Initiates opponent's firing action randomly.
	 * 
	 * @param row The grid row.
	 * @param col The grid column.
	 */
	public void opponentFires(int row, int col) {
		if (controller.getGameController().isShowingPlayerGrid()) {
			controller.getGameController().swapGridView();
		}
		controller.getGameController().disableButtons(); // Disable player's grid buttons during opponent's turn
		if (controller.getServerController() == null) {
			Random random = new Random();
			boolean validMove = false;
			while (!validMove) {
				int r = random.nextInt(10);
				int c = random.nextInt(10);
				int result = opponentFire(r, c); // Perform opponent's firing action
				if (result != -1) {
					validMove = true; // Valid move if opponent hits a valid target
					char columnLabel = (char) ('A' + c); // Convert column index to character label (A-J)
					controller.getChatController().receiveChatMessage(
							loader.getResourceBundle().getString("chat.opponent") + " " + (r + 1) + columnLabel);
					switch (result) {
					case BattleshipModel.HIT:
						for (ShipModel ship : getPlayerFleet()) {
							ship.registerHit(r, c);
						}
						break;
					case BattleshipModel.MISS:
						controller.getChatController()
								.receiveChatMessage(loader.getResourceBundle().getString("chat.miss"));
						break;
					case -1:
						controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached
						return;
					default:
						controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached
						return;
					}
				}
			}
			controller.getGameController().playerTurn = true; // Switch turn back to player after opponent's action
			view.updatePlayerInfo(getPlayerFleet());
			checkGameOver(); // Check if the game is over after each turn
			if (!isGameOver()) {
				view.updateTurnLabel();
				controller.getGameController().playerFires(); // Player continues firing if game is not over
				return;
			}
		} else {
			if (row == -1 && col == -1) {
				// do nothing
			} else {
				int result = opponentFire(row, col); // Perform opponent's firing action
				if (result != -1) {
					char columnLabel = (char) ('A' + col); // Convert column index to character label (A-J)
					controller.getChatController().receiveChatMessage(controller.getServerController().getOpponentName()
							+ " " + loader.getResourceBundle().getString("chat.name") + " " + (row + 1) + columnLabel);
					switch (result) {
					case BattleshipModel.HIT:
						for (ShipModel ship : getPlayerFleet()) {
							ship.registerHit(row, col);
						}
						break;
					case BattleshipModel.MISS:
						controller.getChatController()
								.receiveChatMessage(loader.getResourceBundle().getString("chat.miss"));
						break;
					case -1:
						controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached
						return;
					default:
						controller.getChatController().receiveChatMessage("Invalid move."); // Should never be reached
						return;
					}
				}
			}
			controller.getGameController().playerTurn = true; // Switch turn back to player after opponent's action
			view.updatePlayerInfo(getPlayerFleet());
			checkGameOver(); // Check if the game is over after each turn
			if (!isGameOver()) {
				view.updateTurnLabel();
				controller.getGameController().playerFires(); // Player continues firing if game is not over
				return;
			}
		}
	}

	/**
	 * Resets the game model by initializing new grids and fleets for both the
	 * player and opponent. Clears existing grids and fleets and initializes them
	 * with default values.
	 */
	public void resetModel() {
		playerGrid = new int[10][10];
		opponentGrid = new int[10][10];

		playerFleet = new ArrayList<>();
		opponentFleet = new ArrayList<>();

		initializeGrid(playerGrid);
		initializeGrid(opponentGrid);

		initializeFleet(playerFleet);
		initializeFleet(opponentFleet);
	}
}
