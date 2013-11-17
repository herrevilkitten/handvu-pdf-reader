package cs6456.project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import cs6456.project.cv.HandVuEvent;
import cs6456.project.cv.HandVuEventListener;
import cs6456.project.ui.bookshelf.PdfButton;
import cs6456.project.ui.bookshelf.PdfButtonPanel;
import cs6456.project.ui.bookshelf.ScrollingBookshelfPanel;
import cs6456.project.util.ImageUtils;

public class GesturesGlassPane extends JPanel implements HandVuEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage handImage = ImageUtils.getBufferedImage("/00-Pointer-Hand-icon-64.png");
	private JFrame frame;
	private JButton hoverButton = null;
	private long hoverTime = 0;
	private int hoverArc = 0;
	private boolean enabled = true;

	final static private int ACTION_COUNT = 4000;

	public GesturesGlassPane(JFrame frame) throws IOException {
		super();

		this.frame = frame;

		setOpaque(false);
	}

	int handX = 0;
	int handY = 20;
	int pointerX = handX + 30;
	int pointerY = handY + 2;

	@Override
	public void handVuEvent(HandVuEvent event) {
		int width = this.getWidth() / 2;
		int height = this.getHeight() / 2;

		double xRatio = ((float) (int) (event.getX() * 100)) / 100;
		double yRatio = ((float) (int) (event.getY() * 100)) / 100;
		String posture = event.getPosture();

		handX = (width - (int) ((double) width * xRatio)) * 2 - 30;
		handY = ((int) ((double) height * yRatio)) * 2 - 2;

		pointerX = handX + 30;
		pointerY = handY + 2;

		if (!enabled || !event.isTracking()) {
			hoverTime = 0;
			hoverButton = null;
			hoverArc = 0;
			return;
		}
		Point p = SwingUtilities.convertPoint(this, pointerX, pointerY, frame.getContentPane());
		if (p != null) {
			Component component = SwingUtilities
					.getDeepestComponentAt(frame.getContentPane(), (int) p.getX(), (int) p.getY());
			component = findComponentUnderGlassPaneAt(p, frame);
			int index = 0;
			if (component instanceof JButton) {
				if (component instanceof PdfButton) {
					PdfButtonPanel panel = findPdfButtonPanel(component);
					index = panel.getIndex();
					((ScrollingBookshelfPanel) panel.getParent()).changeCurrentButton(index, false);
					panel.getParent().repaint();
				}
				if (posture != null && posture.toLowerCase().contains("victory")) {
					((JButton) component).doClick();
				} else {
					if (hoverButton == component) {
						if (hoverTime == 0) {
							hoverTime = System.currentTimeMillis();
						} else if (System.currentTimeMillis() >= hoverTime + ACTION_COUNT) {
							hoverTime = System.currentTimeMillis();
							hoverButton.doClick();
						} else {
							long timeDiff = System.currentTimeMillis() - hoverTime;
							hoverArc = (int) (((double) timeDiff / ACTION_COUNT) * 360);
						}
					} else {
						hoverButton = (JButton) component;
						hoverTime = 0;
						hoverArc = 0;
					}
				}
			} else {
				hoverTime = 0;
				hoverButton = null;
				hoverArc = 0;
			}
		} else {
			hoverTime = 0;
			hoverButton = null;
			hoverArc = 0;			
		}
	
		repaint();
	}

	public Component findComponentUnderGlassPaneAt(Point p, Component top) {
		Component c = null;

		if (top.isShowing()) {
			if (top instanceof RootPaneContainer)
				c = ((RootPaneContainer) top).getLayeredPane().findComponentAt(p);
			else
				c = ((Container) top).findComponentAt(p);
		}

		return c;
	}

	private PdfButtonPanel findPdfButtonPanel(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof PdfButtonPanel) {
			return (PdfButtonPanel) component;
		}
		return findPdfButtonPanel(component.getParent());
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.blue);
		g.drawImage(handImage, handX, handY, null);
		if (hoverArc > 0) {
			g.drawArc(pointerX - 16, pointerY - 16, 32, 32, 0, hoverArc);
		}
	}

	public void toggleEnabled() {
		this.enabled = !this.enabled;
		setVisible(this.enabled);
	}
}
