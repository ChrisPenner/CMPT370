package src.views;

import java.awt.Graphics;
import javax.swing.JPanel;

import src.models.Cell;

public class HexGridDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	int length;
	int sideLength;
	int diameter;
	Cell[][] cells;
	
	public HexGridDisplay(int length, int sideLength, Cell[][] cells) {
		this.cells = cells;
		this.sideLength = sideLength;
		this.length = length;
		setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }
	
	private void drawBoard(Graphics g) {
		diameter = sideLength * 2 - 1;
		
		int spacing = (int) ((length / diameter) / 1.3f);
		int vertOffset = 0;
		for(int x = 0; x < diameter; x++) {
			if(x % 2 == 0) {
				vertOffset = 0;
			} else {
				vertOffset = spacing/2;
			}
			for(int y = 0; y < diameter; y++) {
				
				if(cells[x][y] != null && cells[x][y].isValid()) {
					int [] xPoint = new int[6];
					int [] yPoint = new int[6];
					for(int i=0; i<6; i++) {
						xPoint[i] = spacing + (int) (x*spacing + (spacing/1.5f)*Math.cos(i*2*Math.PI/6));
						yPoint[i] = spacing + vertOffset + (int) (y*spacing + (spacing/1.8f)*Math.sin(i*2*Math.PI/6));
					}
					g.drawPolygon(xPoint, yPoint, 6);
				}
			}
		}
	}
}