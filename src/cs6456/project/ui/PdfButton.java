package cs6456.project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;

public class PdfButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;

	public PdfButton(String name, int index) {
		super(name);
		this.index = index;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		PdfButton current = ((BookshelfPanel) getParent().getParent()).getCurrentButtonPanel().getButton();
		if (current == this) {
			Rectangle r = getBounds();
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.blue);
			g2.setStroke(new BasicStroke(4));
			g2.draw(r);
		}
	}

	public int getIndex() {
		return index;
	}
	
}
