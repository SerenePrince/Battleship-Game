package battleship.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import battleship.controller.ServerController.MessageType;
import battleship.model.BattleshipModel;
import battleship.model.ShipModel;
import battleship.view.BattleshipView;

/**
 * The ShipController class handles ship placement and interaction on the
 * Battleship grid.
 */
public class ShipController {

	private BattleshipController controller;
	private BattleshipModel model;
	private BattleshipView view;

	private boolean horizontalOrientation = true; // Default orientation

	private ImageIcon horizontalShipBody = new ImageIcon(getClass().getResource("/resources/images/HShipBody.png"));
	private ImageIcon horizontalShipEnd = new ImageIcon(getClass().getResource("/resources/images/HShipEnd.png"));
	private ImageIcon horizontalShipHead = new ImageIcon(getClass().getResource("/resources/images/HShipHead.png"));
	private ImageIcon verticalShipBody = new ImageIcon(getClass().getResource("/resources/images/VShipBody.png"));
	private ImageIcon verticalShipEnd = new ImageIcon(getClass().getResource("/resources/images/VShipEnd.png"));
	private ImageIcon verticalShipHead = new ImageIcon(getClass().getResource("/resources/images/VShipHead.png"));

	/**
	 * @param model 	 The BattleshipModel instance.
	 * @param view       The BattleshipView instance.
	 * @param controller The BattleshipController instance.
	 */
	public ShipController(BattleshipModel model, BattleshipView view, BattleshipController controller) {
		this.model = model;
		this.view = view;
		this.controller = controller;
	}

	/**
	 * Initializes mouse listeners for grid buttons to handle ship placement.
	 */
	public void initializeGridListeners() {
		view.updateStartMenu();
		JButton[][] gridButtons = view.getGridButtons();
		for (int row = 0; row < gridButtons.length; row++) {
			for (int col = 0; col < gridButtons[row].length; col++) {
				final int currentRow = row;
				final int currentCol = col;
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
						if (SwingUtilities.isRightMouseButton(e)) {
							handleRightClick(currentRow, currentCol);
						} else if (SwingUtilities.isLeftMouseButton(e)) {
							handleLeftClick(currentRow, currentCol);
						}
					}
				});
			}
		}
	}

	/**
	 * Handles left-click event to place a ship on the grid.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleLeftClick(int row, int col) {
		ShipModel currentShip = getNextShip();
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			if (model.canPlaceShip(model.getPlayerGrid(), row, col, currentShip.getLength(), horizontalOrientation)) {
				if (controller.getServerController() != null) {
					controller.getServerController().sendData(MessageType.PLACE,
							row + "," + col + "," + currentShip.getLength() + "," + horizontalOrientation);
				}
				model.placeShip(model.getPlayerGrid(), currentShip, row, col, horizontalOrientation);
				placeShipOnGrid(row, col, currentShip.getLength(), horizontalOrientation);
			}
		}
		if (getNextShip() == null) {
			if (controller.getServerController() == null) {
				model.placeOpponentShipsRandomly();
			}
			controller.getGameController().disableButtons();
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				int seconds = 1;

				@Override
				public void run() {
					if (seconds > 0) {
						seconds--;
					} else {
						timer.cancel();
						controller.getGameController().startGame();
					}

				}

			};
			timer.scheduleAtFixedRate(task, 0, 1000);
			return;

		}
	}

	/**
	 * Handles mouse exit event to remove ship head icon.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleMouseExit(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			button.setIcon(null);
		}
	}

	/**
	 * Handles mouse hover event to display ship head icon.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleMouseHover(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			ImageIcon icon = horizontalOrientation ? horizontalShipHead : verticalShipHead;
			button.setIcon(icon);
		}
	}

	/**
	 * Handles right-click event to toggle ship orientation.
	 *
	 * @param row The row index of the grid cell.
	 * @param col The column index of the grid cell.
	 */
	private void handleRightClick(int row, int col) {
		JButton button = view.getGridButtons()[row][col];
		if (button.isEnabled()) {
			horizontalOrientation = !horizontalOrientation;
			ImageIcon icon = horizontalOrientation ? horizontalShipHead : verticalShipHead;
			button.setIcon(icon);
		}
	}

	/**
	 * Retrieves the next ship that has not been placed on the grid.
	 *
	 * @return The next ShipModel instance or null if all ships are placed.
	 */
	private ShipModel getNextShip() {
		for (ShipModel ship : model.getPlayerFleet()) {
			if (!ship.isPlaced()) {
				return ship;
			}
		}
		return null;
	}

	/**
	 * Places the ship on the grid with appropriate icons and disables corresponding
	 * buttons.
	 *
	 * @param row          The starting row index of the ship.
	 * @param col          The starting column index of the ship.
	 * @param length       The length of the ship.
	 * @param isHorizontal Flag indicating if the ship is placed horizontally.
	 */
	private void placeShipOnGrid(int row, int col, int length, boolean isHorizontal) {
		JButton[][] gridButtons = view.getGridButtons();
		for (int i = 0; i < length; i++) {
			JButton button;
			if (isHorizontal) {
				button = gridButtons[row][col + i];
			} else {
				button = gridButtons[row + i][col];
			}

			// Determine which icon to set based on ship orientation and position
			ImageIcon icon;
			if (i == 0) {
				icon = isHorizontal ? horizontalShipHead : verticalShipHead;
			} else if (i == length - 1) {
				icon = isHorizontal ? horizontalShipEnd : verticalShipEnd;
			} else {
				icon = isHorizontal ? horizontalShipBody : verticalShipBody;
			}

			button.setIcon(icon);
			button.setEnabled(false);
		}
	}

	/**
	 * Resets the grid buttons in the view by removing all existing mouse listeners
	 * and clearing icons. Useful for resetting the visual representation of the
	 * game grid.
	 */
	public void resetGrid() {
		JButton[][] gridButtons = view.getGridButtons();

		// Iterate through all grid buttons
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				JButton button = gridButtons[row][col];

				// Remove all existing MouseListeners from the button
				for (MouseListener listener : button.getMouseListeners()) {
					button.removeMouseListener(listener);
				}

				// Clear the icon on the button
				button.setIcon(null);
				button.setEnabled(true);
			}
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
