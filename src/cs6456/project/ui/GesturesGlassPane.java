package cs6456.project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cs6456.project.cv.HandVuEvent;
import cs6456.project.cv.HandVuEventListener;
import cs6456.project.event.EventDispatcher;
import cs6456.project.ui.bookshelf.PdfButtonPanel;
import cs6456.project.util.ImageUtils;

public class GesturesGlassPane extends JPanel implements HandVuEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage handImage = ImageUtils.getBufferedImage("/00-Pointer-Hand-icon-64.png");
	private EventDispatcher eventDispatcher;
	private JFrame frame;

	public GesturesGlassPane(EventDispatcher eventDispatcher, JFrame frame) throws IOException {
		super();

		this.eventDispatcher = eventDispatcher;
		this.frame = frame;

		setOpaque(false);
	}

	int handX;
	int handY;

	@Override
	public void handVuEvent(HandVuEvent event) {
		int width = this.getWidth();
		int height = this.getHeight();

		double xRatio = event.getX();
		double yRatio = event.getY();
		String posture = event.getPosture();

		int xPosition = width - (int) ((double) width * xRatio) + 30;
		int yPosition = (int) ((double) height * yRatio) + 2;

		handX = xPosition;
		handY = yPosition;

		System.err.println("---------------------");
		System.err.println("x: " + xRatio + " => " + xPosition + " of " + width);
		System.err.println("y: " + yRatio + " => " + yPosition + " of " + height);
		System.err.println("p: " + posture);

		Point p = SwingUtilities.convertPoint(this, xPosition, yPosition, frame.getContentPane());
		if (p != null) {
			Component component = SwingUtilities.getDeepestComponentAt(frame.getContentPane(),
					(int) p.getX(), (int) p.getY());
			int index = 0;
			PdfButtonPanel panel = findPdfButtonPanel(component);
			if ( panel != null ) {
				index = panel.getIndex();
				((BookshelfPanel) panel.getParent()).changeCurrentButton(index, false);
				panel.getParent().repaint();
				
				if ( posture != null && posture.toLowerCase().contains("victory") ) {
					panel.getButton().doClick();
				}
			}
		}

		repaint();
	}
	
	private PdfButtonPanel findPdfButtonPanel(Component component) {
		if ( component == null ) {
			return null;
		}
		if ( component instanceof PdfButtonPanel ) {
			return (PdfButtonPanel) component;
		}
		return findPdfButtonPanel(component.getParent());
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.blue);
		g.drawImage(handImage, handX, handY, null);
	}
}
