package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid. An object of this class represents a board on
 * which players can make moves.
 */

public class GameGrid extends Observable {

	// 2D-array representing the board.
	private int[][] boardModel;

	// Represents the possible content of a square.
	public static int EMPTY = 0;
	public static int ME = 1;
	public static int OTHER = 2;

	// Represents the amount of squares a player need to combine to win.
	public int INROW = 5;

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid.
	 */
	public GameGrid(int size) {
		boardModel = new int[size][size];

		// We initially set all squares to be empty.
		for (int i = 0; i < boardModel.length; i++) {
			for (int y = 0; y < boardModel[i].length; y++) {
				boardModel[i][y] = EMPTY;
			}
		}
	}

	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return boardModel[x][y];

	}

	/**
	 * Returns the size of the grid. Since the grid is a square (rows = columns)
	 * boardModel.lenght will always give us the proper value. We could also do
	 * boardModel[0].lenght.
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return boardModel.length;
	}

	/**
	 * Enters a move in the game grid. If the wanted move is to an occupied square
	 * the method returns false. After the move we notify observers.
	 * 
	 * @param x      the x position
	 * @param y      the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		if (boardModel[x][y] == EMPTY) {
			boardModel[x][y] = player;
			setChanged();
			notifyObservers();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears the grid of pieces. This by setting all squares to the EMPTY (i.e the
	 * integer value 0).
	 */
	public void clearGrid() {
		for (int i = 0; i < boardModel.length; i++) {
			for (int y = 0; y < boardModel[i].length; y++) {
				boardModel[i][y] = EMPTY;
			}
		}

		setChanged();
		notifyObservers();

	}

	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player) {

		// Inte klar
		return true;

	}

}