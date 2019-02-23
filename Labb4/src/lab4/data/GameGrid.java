package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid. An object of this class represents a board on
 * which players can make moves.
 */

public class GameGrid extends Observable {

	// 2D-array representing the board. Every element in the 2D array is a array of
	// similar size as the 2D-array. So if the 2D array contains 3 elements (3
	// arrays) then every 1D array contains 3 integers. Each 1D array will represent
	// a row in the board game, and since each 1D-array is similar size as the
	// amount of 1D-arrays we get a grid. Othewise we would get ragged arrays.
	private int[][] boardModel;

	// Represents the possible content of a square.
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;

	// Represents the amount of squares a player need to combine to win.
	public int INROW = 3; // !ÄNDRA TILL 5!

	/**
	 * Constructor
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
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return boardModel[y][x];

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
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player) {

		// We loop through every square of the board and check all possible victory
		// conditions. This by looking at every square and check if that square have the
		// value of the specific player. When finding a square that has the same value
		// (1 or 2) we check the next rows and columns for a set win condition (INROW)
		// without crossing the grid borders. If we find any win condition we return
		// true, otherwise false.
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
					while (stack < INROW && row + stack < boardModel.length) {
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

					// When the loops ends (either with break or with our condition we check if
					// stack == inrow.
					if (stack == INROW) {
						return true;
					} else {
						continue;
					}

				} else {
					continue;
				}
			}
		}
		return false;
	}

	// Koverterar till sträng för att snabbt visualisera vårat spel. (Test köra)
	public String toString() {
		String result = "";
		for (int row = 0; row < boardModel.length; row++) {
			for (int col = 0; col < boardModel[row].length; col++) {
				result = result + boardModel[row][col];
			}
			result = result + "\n"; // New row for each column.
		}
		return result;
	}
	

	public static void main(String[] args) {
		GameGrid test = new GameGrid(3); // !ÄNDRA TILL 19!
		test.move(0, 0, ME);
		test.move(1, 1, ME);
		test.move(2, 2, ME);
		

		
		
		System.out.println(test.isWinner(ME));
		//Shows a string version of the game.
		System.out.println(test.toString());
	}

}