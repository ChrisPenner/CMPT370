package views;

import java.awt.Graphics;
import javax.swing.JPanel;

import models.Cell;

public class HexGridDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	int boundLength;
	int sideLength;
	int diameter;
	Cell[][] cells;
	
	public HexGridDisplay(int boundLength, int sideLength, Cell[][] cells) {
		this.cells = cells;
		this.sideLength = sideLength;
		this.boundLength = boundLength;
		setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }
	
	private void drawBoard(Graphics g) {
//		this.cells = cells;
		diameter = sideLength * 2 - 1;
		
		int spacing = (int) ((boundLength / diameter) / 1.3f);
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
						yPoint[i] = spacing + vertOffset + (int) (y*spacing + (spacing/1.7f)*Math.sin(i*2*Math.PI/6));
					}
					if(cells[x][y].getOccupants() != null) {
						int count = cells[x][y].getOccupants().size();
						if(count > 0) {
							String s = "" + count;
//							String s = "(" + x + ", " + y + ")";
							g.drawString("" + s, spacing + x*spacing - (getFontMetrics(getFont()).stringWidth(s) / 2),
										spacing + vertOffset + y*spacing + (getFontMetrics(getFont()).getHeight() / 4));
						}
					}
					g.drawPolygon(xPoint, yPoint, 6);
				}
			}
		}
	}
}
