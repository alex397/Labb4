package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

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
		
		
		
		GomokuClient client = new GomokuClient(portnumber);
		GomokuGameState gameState = new GomokuGameState(client);
		GomokuGUI GUI = new GomokuGUI(gameState, client);
		
		

	}

}
