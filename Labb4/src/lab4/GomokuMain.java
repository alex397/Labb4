package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

/**
 * @author Alexander Liljeborg & Kristoffer Eriksson
 * 
 *         The class is used to initiate the program (Gomoku Game) by creating 3
 *         objects (a client, a model, a view).
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

		// Vi skapar objekten client, gameState, GUI och på så vis initierar vårat
		// program (Gomoku).
		GomokuClient client = new GomokuClient(portnumber);
		GomokuGameState gameState = new GomokuGameState(client);
		GomokuGUI GUI = new GomokuGUI(gameState, client);

		// Kan användas för att testa programmet på samma dator. Båda spel startas här.
		// Vi skriver in localhost under client och portnumber för den andra körningen i
		// rutan "portnumber". ALltså 4000 eller 4001.
		GomokuClient client2 = new GomokuClient(4001);
		GomokuGameState gameState2 = new GomokuGameState(client2);
		GomokuGUI GUI2 = new GomokuGUI(gameState2, client2);
		
		GomokuClient client3 = new GomokuClient(4002);
		GomokuGameState gameState3 = new GomokuGameState(client3);
		GomokuGUI GUI3 = new GomokuGUI(gameState3, client3);

	}

}
