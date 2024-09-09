package battleship.model;

import java.util.ArrayList;
import java.util.List;

import battleship.LocalizationLoader;
import battleship.controller.BattleshipController;

/**
 * Represents a ship in the Battleship game.
 */
public class ShipModel {
	private BattleshipController controller;
	private LocalizationLoader loader;

	private CoordinateModel coordinateHead;
	private List<CoordinateModel> coordinates;
	
	private int hits;
	private boolean isHorizontal;
	private boolean isPlaced;
	private int length;
	private String name;

	/**
	 * Constructs a ShipModel with the specified name and length.
	 *
	 * @param loader The localization loader instance to load game resources.
	 * @param name   The name of the ship.
	 * @param length The length of the ship.
	 */
	public ShipModel(LocalizationLoader loader, String name, int length) {
		this.loader = loader;
		this.name = name;
		this.length = length;
		this.hits = 0;
		this.isPlaced = false;
		this.isHorizontal = true;
		this.coordinates = new ArrayList<>();
	}

	/**
	 * Adds a coordinate to the list of coordinates occupied by the ship.
	 *
	 * @param row The row coordinate to add.
	 * @param col The column coordinate to add.
	 */
	public void addCoordinate(int row, int col) {
		coordinates.add(new CoordinateModel(row, col));
	}

	/**
	 * Retrieves the coordinate of the head of the ship.
	 *
	 * @return The coordinate of the head of the ship.
	 */
	public CoordinateModel getCoordinateHead() {
		return coordinateHead;
	}

	/**
	 * Retrieves the list of coordinates occupied by the ship.
	 *
	 * @return The list of coordinates.
	 */
	public List<CoordinateModel> getCoordinates() {
		return coordinates;
	}

	/**
	 * Retrieves the number of hits received by the ship.
	 *
	 * @return The number of hits received.
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * Retrieves the length of the ship.
	 *
	 * @return The length of the ship.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Retrieves the name of the ship.
	 *
	 * @return The name of the ship.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if the ship is oriented horizontally on the grid.
	 *
	 * @return true if the ship is horizontal, false if vertical.
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}

	/**
	 * Checks if the ship has been placed on the grid.
	 *
	 * @return true if the ship is placed, false otherwise.
	 */
	public boolean isPlaced() {
		return isPlaced;
	}

	/**
	 * Checks if the ship has been sunk.
	 *
	 * @return true if the ship has been sunk (hits >= length), false otherwise.
	 */
	public boolean isSunk() {
		return hits >= length;
	}

	/**
	 * Sets the controller of the ship.
	 *
	 * @param controller The BattleshipController instance to set.
	 */
	public void setController(BattleshipController controller) {
		this.controller = controller;
	}

	/**
	 * Sets the coordinate of the head of the ship.
	 *
	 * @param coordinateHead The coordinate of the head of the ship.
	 */
	public void setCoordinateHead(CoordinateModel coordinateHead) {
		this.coordinateHead = coordinateHead;
	}

	/**
	 * Sets the number of hits received by the ship.
	 *
	 * @param hits The new number of hits received.
	 */
	public void setHits(int hits) {
		this.hits = hits;
	}

	/**
	 * Sets the orientation of the ship on the grid.
	 *
	 * @param horizontal true for horizontal orientation, false for vertical.
	 */
	public void setHorizontal(boolean horizontal) {
		isHorizontal = horizontal;
	}

	/**
	 * Sets the name of the ship.
	 *
	 * @param name The new name of the ship.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets whether the ship is placed on the grid or not.
	 *
	 * @param placed true to place the ship, false otherwise.
	 */
	public void setPlaced(boolean placed) {
		isPlaced = placed;
	}

	/**
	 * Checks if the given coordinates are part of this ship and registers a hit if
	 * they are.
	 *
	 * @param row The row coordinate to check.
	 * @param col The column coordinate to check.
	 * @return true if the coordinates are part of this ship and a hit was
	 *         registered, false otherwise.
	 */
	public boolean registerHit(int row, int col) {
		for (CoordinateModel coordinate : coordinates) {
			if (coordinate.getRow() == row && coordinate.getColumn() == col) {
				hits++;
				controller.getChatController().receiveChatMessage(loader.getResourceBundle().getString("chat.hit"));
				if (isSunk()) {
					controller.getChatController()
							.receiveChatMessage(loader.getResourceBundle().getString("chat.sunk"));
				}
				return true;
			}
		}
		return false;
	}
}
