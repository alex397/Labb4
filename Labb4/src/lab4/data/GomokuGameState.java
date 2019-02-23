package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game during play. Model of the game. The model is
 * an object (of this class) that holds all relevant data that describe a game
 * in progress. Contains a reference to an object of the class GameGrid.
 */

public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;

	// 4 Possible game states. currentState is any of 4. That is (0, 1, 2 or 3).
	private final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
	private final int OTHERS_TURN = 2;
	private final int FINISHED = 3;
	private int currentState;

	// Reference variable for the GomokuClient of this game. object that is
	// responsible for communicating with player "other".
	private GomokuClient client;

	// Represents the message showing in the GUI below the board.
	private String message;

	/**
	 * The constructor
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
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {	
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location. Moves the player ME make, made when ME clicks on the board.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y) {
		if (currentState == MY_TURN) {
			if(gameGrid.move(x, y, GameGrid.ME)) {
				client.sendMoveMessage(x, y);
				message = "Move made!";
				
				if (gameGrid.isWinner(GameGrid.ME)){
					message = "Winner Winner Chicken Dinner!";
					currentState = FINISHED;
				}else {
					message = "Opponents turn!";
					currentState = OTHERS_TURN;
				}
				
				setChanged();
				notifyObservers();		
			}else {
				System.out.print("Move not possible");
				message = "Move was not possible!";	
			}
			
		}else if (currentState == NOT_STARTED) {
			message = "Game is not started!";
			setChanged();
			notifyObservers();
			return;
			
		}else if (currentState == FINISHED) {
			message = "Game is finished!";
			setChanged();
			notifyObservers();
			return;		
		}
		
	}

	/**
	 * Starts a new game with the current client. Called when player ME press "New Game" button.
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
	 * The player disconnects from the client. Called when ME clicks the button "Disconnect".
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
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		if(gameGrid.move(x, y, GameGrid.OTHER)){

			if (gameGrid.isWinner(GameGrid.OTHER)){
				message = "Your opponent won!";
				currentState = FINISHED;
			}else {
				message = "Your turn!";
				currentState = MY_TURN;
			}

			setChanged();
			notifyObservers();		
		}	
	}

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
	
	public static void main(String[] args) {
		GomokuClient client = new GomokuClient(4008);
		GomokuGameState test = new GomokuGameState(client);
		
		test.receivedNewGame();
		test.move(0, 0);
		test.move(1, 1);
		test.move(2, 2);
		
		test.gameGrid.toString();
		
	}

}