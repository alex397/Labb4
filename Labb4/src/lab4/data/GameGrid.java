package lab4.data;

import java.util.Observable;

/**
 * @author Alexander Liljeborg & Kristoffer Eriksson
 * 
 *         Represents the 2-d game grid. An object of this class represents a
 *         board on which players can make moves. The class contains methods
 *         necessary for the model of the game.
 */
public class GameGrid extends Observable {

	// 2D-array representing the board. Every element in the 2D array is a array of
	// similar size as the 2D-array. So if the 2D array contains 3 elements (3
	// arrays) then every 1D array contains 3 integers. Each 1D array will represent
	// a row in the board game and hence elements in the 1D arrays will form
	// columns, and since each 1D-array is similar size as the
	// amount of 1D-arrays we get a grid. Otherwise we would get ragged arrays.
	private int[][] boardModel;

	// Represents the possible content of a square.
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;

	// Represents the amount of squares a player need to combine to win.
	public int INROW = 5;

	/**
	 * Constructor. Defines the size of a board by initializing the array boardModel
	 * with the input argument. Also initialize all elements in the 2D-array
	 * (squares on the board) by changing their value to 0 (EMPTY) instead of null.
	 * 
	 * @param size The width/height of the game grid.
	 */
	public GameGrid(int size) {
		boardModel = new int[size][size];

		// We initially set all squares to be empty.
		for (int row = 0; row < boardModel.length; row++) {
			for (int col = 0; col < boardModel[row].length; col++) {
				boardModel[row][col] = EMPTY;
			}
		}
	}

	/**
	 * Reads a location of the grid. Given two input arguments (x, y) coordinates,
	 * return the value of that location i.e returns what player has that location
	 * occupied or if its EMPTY. Note that the input arguments are used as indices
	 * (not coordinates) in the array boardModel. Hence (0, 0) represents the the
	 * very first element located in the top left corner of the grid.
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return boardModel[y][x];

	}

	/**
	 * Returns the size of the grid. This by returns the size of the 2D-array(amount
	 * of rows). Since the grid is a square (rows = columns) boardModel.lenght will
	 * always give us the proper value. We could also do boardModel[0].lenght which
	 * would give us the amount of columns.
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return boardModel.length;
	}

	/**
	 * Enters a move in the game grid. This by change the value of the specified
	 * square (element) to be equal to the value of the specified player. If the
	 * wanted move is to an occupied square the method returns false. After the move
	 * we notify observers.
	 * 
	 * @param x      the x position
	 * @param y      the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		if (boardModel[y][x] == EMPTY) {
			boardModel[y][x] = player;
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
		for (int row = 0; row < boardModel.length; row++) {
			for (int col = 0; col < boardModel[row].length; col++) {
				boardModel[row][col] = EMPTY;
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Check if a player has 5 in row. A player has 5 in a row if 5 consecutive
	 * squares have the same value as the player. This is checked by looping through
	 * every square of the board checking all possible victory conditions. This by
	 * looking at every square and check if that square have the value of the
	 * specific player. When finding a square that has the same value (1 or 2) we
	 * check the next rows and columns for a set win condition (INROW) without
	 * crossing the grid borders. If we find any win condition we return true,
	 * otherwise false.
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player) {
		for (int row = 0; row < boardModel.length; row++) {
			for (int col = 0; col < boardModel[row].length; col++) {

				if (boardModel[row][col] == player) {
					int stack = 1;

					// Check vertical victory of player.
					while (stack < INROW && row + stack < boardModel.length) {
						if (boardModel[row + stack][col] == player) {
							stack++;
						} else {
							stack = 1;
							break;
						}
					}

					// Check horizontal victory of player.
					while (stack < INROW && col + stack < boardModel.length) {
						if (boardModel[row][col + stack] == player) {
							stack++;
						} else {
							stack = 1;
							break;
						}
					}

					// Check diagonal victory of player. (\)
					while (stack < INROW && row + stack < boardModel.length && col + stack < boardModel[row].length) {
						if (boardModel[row + stack][col + stack] == player) {
							stack++;
						} else {
							stack = 1;
							break;
						}
					}

					// Check diagonal victory of player. (/)
					while (stack < INROW && row + stack < boardModel.length && col - stack >= 0) {
						if (boardModel[row + stack][col - stack] == player) {
							stack++;
						} else {
							stack = 1;
							break;
						}
					}

					// When the loops ends (either with break or with our condition) we check if
					// stack == inrow, if not we move on to check the next square.
					if (stack == INROW) {
						return true;
					} else {
						continue;
					}

					// If the square does not have the value of the player we check the next square.
				} else {
					continue;
				}
			}
		}
		// If we have gone through all squares (both for loops) and no iteration
		// returned true we will return false since the player dont have met the
		// winconditions.
		return false;
	}

	// Koverterar till str‰ng fˆr att snabbt visualisera vÂrat spel. (Test kˆra
	// metoder i paketet lab4.data utan GUI)
//	public String toString() {
//		String result = "";
//		for (int row = 0; row < boardModel.length; row++) {
//			for (int col = 0; col < boardModel[row].length; col++) {
//				result = result + boardModel[row][col];
//			}
//			result = result + "\n"; // New row for each column.
//		}
//		System.out.print(result);
//		return result;
//	}

}