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
		
		frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		frame.setTitle("Gomoku");
		frame.setVisible(true);
		frame.setResizable(false);
		
		messageLabel = new JLabel("Welcome to Gomoku"); //Starttexten för alla buttons samt messageLabel
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New Game");
		disconnectButton = new JButton("Disconnect");

		if(gamestate.getcurrentState() == gamestate.NOT_STARTED) { //Om spelet ej är startat så ska
			newGameButton.setEnabled(false);					   //knapparna ej vara tillgängliga
			disconnectButton.setEnabled(false);
		}
		
		connectButton.addActionListener(new ActionListener() { //Skapa ett nytt connectionWindow
			public void actionPerformed(ActionEvent arg0) {	   //och försök connecta till andra spelaren
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
					messageLabel.setText("New Game Started");
				}
			}
		});		
		
		disconnectButton.addActionListener(new ActionListener() { //Disconnecta från spelet
			public void actionPerformed(ActionEvent e) {
				String buttonInput = e.getActionCommand();
				if(buttonInput.equals("Disconnect")) {
					gamestate.disconnect();
					messageLabel.setText("Disconnected From Game");
				}
			}
		});

		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		gameGridPanel.setVisible(true);
		
		MouseAdapter mouseListener = new MouseAdapter(){ //Hämtar x,y position för musklickningar
			public void mouseClicked(MouseEvent e){
				gamestate.move(gameGridPanel.getGridPosition(e.getX(), e.getY())[0],
						(gameGridPanel.getGridPosition(e.getX(), e.getY()))[1]);
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
		frame.add(mainBox);
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
