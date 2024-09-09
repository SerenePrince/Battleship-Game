package battleship.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import battleship.LocalizationLoader;
import battleship.controller.ServerController.MessageType;
import battleship.model.BattleshipModel;
import battleship.view.BattleshipView;

/**
 * The GameController class manages the game logic for Battleship.
 */
public class GameController {

	private BattleshipController controller;
	private LocalizationLoader loader;
	private BattleshipModel model;
	private BattleshipView view;


	public boolean playerTurn = true;
	public boolean showingPlayerGrid;

	private Timer timer;
	
	/**
	 * Time remaining for the current turn.
	 */
	public int timeRemaining = 10;

	private int row;
	private int col;

	// Icons for displaying hit, miss, and ship images
	private ImageIcon hitIndicator = new ImageIcon("resources/images/hit.png");

	/**
	 * Constructor for GameController.
	 * @param loader	 The Localization instance.
	 * @param model      The BattleshipModel instance.
	 * @param view       The BattleshipView instance.
	 * @param controller The BattleshipController instance.
	 */
	public GameController(LocalizationLoader loader, BattleshipModel model, BattleshipView view,
			BattleshipController controller) {
		this.loader = loader;
		this.model = model;
		this.view = view;
		this.controller = controller;
	}

	/**
	 * Retrieves the current game timer.
	 *
	 * @return The Timer object managing the game timer.
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Retrieves the remaining time in seconds for the current player's turn.
	 *
	 * @return The remaining time in seconds.
	 */
	public int getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * Sets the remaining time in seconds for the current player's turn.
	 *
	 * @param timeRemaining The remaining time in seconds to set.
	 */
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	/**
	 * Checks if the player's grid is currently being shown.
	 *
	 * @return true if the player's grid is shown, false otherwise.
	 */
	public boolean isShowingPlayerGrid() {
		return showingPlayerGrid;
	}

	/**
	 * Sets whether to show the player's grid or the opponent's grid.
	 *
	 * @param showingPlayerGrid true to show the player's grid, false to show the
	 *                          opponent's grid.
	 */
	public void setShowingPlayerGrid(boolean showingPlayerGrid) {
		this.showingPlayerGrid = showingPlayerGrid;
	}

	/**
	 * Starts the game by placing opponent's ships and initiating player's
	 * turn.
	 */
	public void startGame() {
		if (controller.getServerController() != null) {
			controller.getServerController().sendData(MessageType.START, "PLACED");
			controller.getServerController().synchronize();
		}
		timer = new Timer();
		TimerTask task = new TimerTask() {
			int seconds = 3;

			@Override
			public void run() {
				if (seconds > 0) {
					controller.getChatController().receiveChatMessage(seconds + "...");
					seconds--;
				} else {
					timer.cancel();
					view.updateStartMenu();
					showingPlayerGrid = true;
					swapGridView();
					if (controller.getServerController() == null) {
						controller.getChatController()
								.receiveChatMessage(loader.getResourceBundle().getString("chat.start"));
						playerTurn = true;
						view.updateTurnLabel();
						playerFires();
						return;
					}
					controller.getChatController()
							.receiveChatMessage(loader.getResourceBundle().getString("chat.start"));
					if (controller.getServerController().isHost == 1) {
						playerTurn = true;
						view.updateTurnLabel();
						playerFires();
						return;
					} else {
						playerTurn = false;
						view.updateTurnLabel();
						opponentFires();
						return;
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000); // Schedule the task to run every second
	}

	/**
	 * Sets up event listeners for player firing actions.
	 */
	public void playerFires() {
		timeRemaining = 10;
		view.updateTurnLabel();
		enableButtons();
		timer = new Timer();
		TimerTask task = new TimerTask() {
			int seconds = 10;

			@Override
			public void run() {
				if (seconds > 0) {
					seconds--;
					timeRemaining--;
					view.updateTimeLabel();
					JButton[][] gridButtons = view.getGridButtons();
					for (int row = 0; row < gridButtons.length; row++) {
						for (int col = 0; col < gridButtons[row].length; col++) {
							final int currentRow = row;
							final int currentCol = col;
							// Remove existing listeners to avoid duplicates
							for (MouseListener listener : gridButtons[row][col].getMouseListeners()) {
								gridButtons[row][col].removeMouseListener(listener);
							}
							// Add new listener for mouse events on each button
							gridButtons[row][col].addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									handleMouseHover(currentRow, currentCol);
								}

								@Override
								public void mouseExited(MouseEvent e) {
									handleMouseExit(currentRow, currentCol);
								}

								@Override
								public void mousePressed(MouseEvent e) {
									if (SwingUtilities.isLeftMouseButton(e)) {
										timer.cancel();
										timeRemaining = 10;
										view.updateTimeLabel();
										handleLeftClick(currentRow, currentCol);
									}
								}
							});
						}
					}
				} else {
					view.getGridButtons()[row][col].setIcon(null);
					timer.cancel();
					timeRemaining = 10;
					view.updateTimeLabel();
					if (controller.getServerController() == null) {
						controller.getGameController().playerTurn = false; // Switch turn to opponent after player's
						view.updateTurnLabel();
						model.opponentFires(-1, -1);
					} else {
						model.playerFires(-1, -1);
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000); // Schedule the task to run every second
	}

	/**
	 * Sets up event listeners for opponent firing actions.
	 */
	public void opponentFires() {
		view.updateTurnLabel();
		if (controller.getGameController().isShowingPlayerGrid()) {
			controller.getGameController().swapGridView();
		}
		controller.getGameController().disableButtons(); // Disable player's grid buttons during opponent's turn
		timeRemaining = 10;
		view.updateTimeLabel();
	}

	/**
	 * Switches between showing the player's grid and opponent's grid.
	 */
	public void swapGridView() {
		showingPlayerGrid = !showingPlayerGrid; // Toggle between player's and opponent's grid view
		view.updateGridView(); // Update the UI to show the current grid view
	}

	/**
	 * Disables all grid buttons.
	 */
	public void disableButtons() {
		JButton[][] gridButtons = view.getGridButtons();
		for (int row = 0; row < gridButtons.length; row++) {
			for (int col = 0; col < gridButtons[row].length; col++) {
				gridButtons[row][col].setEnabled(false);
			}
		}
	}

	/**
	 * Enables grid buttons to allow player interaction.
	 */
	public void enableButtons() {
		JButton[][] gridButtons = view.getGridButtons();
		for (int row = 0; row < gridButtons.length; row++) {
			for (int col = 0; col < gridButtons[row].length; col++) {
				gridButtons[row][col].setEnabled(true);
				// Disable buttons that have already been fired upon
				if (model.getOpponentGrid()[row][col] == BattleshipModel.HIT
						|| model.getOpponentGrid()[row][col] == BattleshipModel.MISS) {
					gridButtons[row][col].setEnabled(false);
				}
			}
		}
	}

	/**
	 * Handles left-click event on grid button to initiate player's firing action.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleLeftClick(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			model.playerFires(row, col); // Perform player's firing action
		}
	}

	/**
	 * Handles mouse exit event to remove hit indicator from grid button.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleMouseExit(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			button.setIcon(null); // Remove icon when mouse exits
		}
	}

	/**
	 * Handles mouse hover event to display hit indicator on grid button.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleMouseHover(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			this.row = row;
			this.col = col;
			button.setIcon(hitIndicator); // Display hit indicator icon
		}
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
