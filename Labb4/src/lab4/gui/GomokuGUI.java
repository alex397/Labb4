package lab4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/*
 * The GUI class. Represents the view. A window showing the board, prev moves, indacation on player turns, info message, three buttons.
 */

public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JFrame frame;
	private JLabel messageLabel;
	private JButton connectButton, newGameButton, disconnectButton;
	private GamePanel gameGridPanel;
	private ConnectionWindow connect;
	
	/**
	 * The constructor
	 * 
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);
		
		JPanel panel = new JPanel();
		frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		frame.setTitle("Gomoku");
		frame.setVisible(true);
		frame.setResizable(true);
		
		messageLabel = new JLabel("Welcome to Gomoku"); //Starttexten fˆr alla buttons samt messageLabel
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New Game");
		disconnectButton = new JButton("Disconnect");

		if(gamestate.currentState == gamestate.NOT_STARTED) { //Om spelet ej ‰r startat sÂ ska
			newGameButton.setEnabled(false);					   //knapparna ej vara tillg‰ngliga
			disconnectButton.setEnabled(false);
		}
		
		connectButton.addActionListener(new ActionListener() { //Skapa ett nytt connectionWindow
			public void actionPerformed(ActionEvent arg0) {	   //och fˆrsˆk connecta till andra spelaren
				String input = arg0.getActionCommand();
				if(input.equals("Connect")) {
					connect = new ConnectionWindow(client);
					messageLabel.setText("Attempt To Connect");
				}
			}
		});
		
		
		newGameButton.addActionListener(new ActionListener() { //Starta ett nytt spel
			public void actionPerformed(ActionEvent e) {
				String buttonInput = e.getActionCommand();
				if(buttonInput.equals("New Game")) {
					gamestate.newGame();
					client.sendNewGameMessage();
				}
			}
		});		
		
		disconnectButton.addActionListener(new ActionListener() { //Disconnecta frÂn spelet
			public void actionPerformed(ActionEvent e) {
				String buttonInput = e.getActionCommand();
				if(buttonInput.equals("Disconnect")) {
					gamestate.disconnect();
				}
			}
		});

		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		gameGridPanel.setVisible(true);
		
		MouseAdapter mouseListener = new MouseAdapter(){ //H‰mtar x,y position fˆr musklickningar
			public void mouseClicked(MouseEvent e){
				try {
					int[] pos = gameGridPanel.getGridPosition(e.getX(), e.getY());
					gamestate.move(pos[0], pos[1]);

				}catch (ArrayIndexOutOfBoundsException a) {
				}
			}
		};
		
		
		
		gameGridPanel.addMouseListener(mouseListener);
		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(connectButton);
		buttonBox.add(newGameButton);
		buttonBox.add(disconnectButton);
		
		Box messageBox = Box.createHorizontalBox();
		messageBox.add(messageLabel);
		
		Box mainBox = Box.createVerticalBox();
		mainBox.add(gameGridPanel);
		mainBox.add(buttonBox);
		mainBox.add(messageBox);
		panel.add(mainBox);
		
		frame.setContentPane(panel);
	
	}
	
	
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
		
	}
	
}