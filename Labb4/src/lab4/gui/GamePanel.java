package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 */

public class GamePanel extends JPanel implements Observer{

	public final int UNIT_SIZE = 20;
	private GameGrid grid;
	
	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 */
	public GamePanel(GameGrid grid){
		this.grid = grid;
		grid.addObserver(this);
		Dimension d = new Dimension(grid.getSize()*UNIT_SIZE, grid.getSize()*UNIT_SIZE);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}

	/**
	 * Returns a grid position given pixel coordinates
	 * of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y){
		int [] arr = {x/UNIT_SIZE,y/UNIT_SIZE};
		return arr; 
	}
	
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (int i = 0; i < grid.getSize(); i++) { // Y-led
			for (int k = 0; k < grid.getSize(); k++) {// X-led
					Rect(k, i, Color.BLACK, g);
				if (grid.getLocation(k, i) == GameGrid.ME) {
					Circle(k, i, Color.RED, g);
				}
				if (grid.getLocation(k, i) == GameGrid.OTHER) {
					Cross(k, i, Color.BLUE, g);
				}
			}
		}
		this.repaint();
	}
	
	public void Rect(int x, int y, Color color, Graphics g) {
		g.setColor(color);
		g.drawRect(x*UNIT_SIZE, y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
	}
	
	public void Circle(int x, int y, Color color, Graphics g) { //KAN KOMMA ATT BEH÷VA ƒNDRA STORLEKAR
		g.setColor(color);
		g.drawOval(x*UNIT_SIZE, y*UNIT_SIZE, UNIT_SIZE-2, UNIT_SIZE-2);
	}	
	public void Cross(int x, int y, Color color, Graphics g) { //KAN KOMMA ATT BEH÷VA ƒNDRA STORLEKAR
		g.setColor(color);
		g.drawLine(x*UNIT_SIZE, y*UNIT_SIZE, x*UNIT_SIZE, y*UNIT_SIZE);
		g.drawLine(x*UNIT_SIZE, y*UNIT_SIZE + y, x*UNIT_SIZE + x, y*UNIT_SIZE);	
	}	
		
	}
	
