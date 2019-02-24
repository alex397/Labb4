package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;
/**
 * @author Alexander Liljeborg	&	Kristoffer Eriksson
 * 
 * The class is used to initiate the program (Gomoku Game) by creating 3 objects (a client, a model, a view).
 */
public class GomokuMain {

	// Task 4. Vi hämtar portnumber som inmatat argument om och endast om det enbart
	// finns ett givet argument och argumentet är ett positivt heltal. Annars hämtas
	// portnumber som defult till 4000.
	public static void main(String[] args) {
		int portnumber;

		if (args.length == 1 && Integer.parseInt(args[0]) >= 0) {
			portnumber = Integer.parseInt(args[0]);
		} else {
			portnumber = 4000;
		}
		
		
		// Vi skapar objekten client, gameState, GUI och på så vis initierar vårat program (Gomoku).
		GomokuClient client = new GomokuClient(portnumber);
		GomokuGameState gameState = new GomokuGameState(client);
		GomokuGUI GUI = new GomokuGUI(gameState, client);
		
		

	}

}
