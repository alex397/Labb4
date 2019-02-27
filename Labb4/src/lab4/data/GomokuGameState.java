package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * @author Alexander Liljeborg & Kristoffer Eriksson
 * 
 *         Represents the state of a game during play. Model of the game. The
 *         model is an object (of this class) that holds all relevant data that
 *         describe a game in progress. Contains a reference to an object of the
 *         class GameGrid.
 */

public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;

	// 4 Possible game states. currentState is any of 4. That is (0, 1, 2 or 3).
	public final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
	private final int OTHERS_TURN = 2;
	private final int FINISHED = 3;
	public int currentState;

	// Reference variable for the GomokuClient of this game. object that is
	// responsible for communicating with player "other".
	private GomokuClient client;

	// Represents the message showing in the GUI below the board.
	private String message;

	/**
	 * The constructor. Initialize client, currentState, gameGrid. Also makes the
	 * created object (instance of this class) an observer.
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc) {
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString() {
		return message;
	}

	/**
	 * Returns the game grid. The actual model of the game board, all player
	 * locations.
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location. Moves the player ME, used
	 * in GUI to move ME when ME press the board. This only if its ME's turn and a
	 * game is consider started. When a move is made both ME and OTHER is notified.
	 * The method also always check if the move made ME the winner. The second
	 * if-statement itself both checks if a move is possible and actually makes the
	 * move if it is. So that statment alone run the entire method move in GameGrid.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y) {
		if (currentState == MY_TURN) {
			if (gameGrid.move(x, y, GameGrid.ME)) {
				client.sendMoveMessage(x, y);

				if (gameGrid.isWinner(GameGrid.ME)) {
					message = "Winner Winner Chicken Dinner!";
					currentState = FINISHED;
				} else {
					message = "Opponents turn!";
					currentState = OTHERS_TURN;
				}

				setChanged();
				notifyObservers();
			} else {
				message = "Move was not possible!";
				setChanged();
				notifyObservers();
			}

		} else if (currentState == NOT_STARTED) {
			message = "Game is not started!";
			setChanged();
			notifyObservers();
			return;

		} else if (currentState == FINISHED) {
			message = "Game is finished!";
			setChanged();
			notifyObservers();
			return;
		}

	}

	/**
	 * Starts a new game with the current client. Called when player ME press "New
	 * Game" button.
	 */
	public void newGame() {
		gameGrid.clearGrid();
		currentState = OTHERS_TURN;
		message = "You started a new game, it's your opponents turn!";
		setChanged();
		notifyObservers();
	}

	/**
	 * Other player has requested a new game, so the game state is changed
	 * accordingly. Received when opponent press button "New Game".
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		currentState = MY_TURN;
		message = "Opponent started a new game, please make your move!";
		setChanged();
		notifyObservers();
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted.
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "Opponent disconnected!";
		setChanged();
		notifyObservers();
	}

	/**
	 * The player disconnects from the client. Called when ME clicks the button
	 * "Disconnect".
	 */
	public void disconnect() {
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "You disconnected!";
		client.disconnect();
		setChanged();
		notifyObservers();
	}

	/**
	 * The player receives a move from the other player. Like method move above we check if that move is possible and also if that move made OTHER the winner.
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		if (gameGrid.move(x, y, GameGrid.OTHER)) {

			if (gameGrid.isWinner(GameGrid.OTHER)) {
				message = "Your opponent won!";
				currentState = FINISHED;
			} else {
				message = "Your turn!";
				currentState = MY_TURN;
			}

			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * Method used when observers are notified of a change. In this case used to determine the initial game state when a game starts.
	 */
	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHERS_TURN;
			break;
		}
		setChanged();
		notifyObservers();

	}
	
	public int getcurrentState() {
		return currentState;
	}

}