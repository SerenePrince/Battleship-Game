package battleship.model;

/**
 * Represents a coordinate in a two-dimensional space.
 */
public class CoordinateModel {
	private int column;
	private int row;

	/**
	 * Constructs a CoordinateModel with the specified row and column.
	 *
	 * @param row The row index of the coordinate.
	 * @param column The column index of the coordinate.
	 */
	public CoordinateModel(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the column index of the coordinate.
	 *
	 * @return The column index.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Gets the row index of the coordinate.
	 *
	 * @return The row index.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Sets the column index of the coordinate.
	 *
	 * @param col The new column index.
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Sets the row index of the coordinate.
	 *
	 * @param row The new row index.
	 */
	public void setRow(int row) {
		this.row = row;
	}
}
