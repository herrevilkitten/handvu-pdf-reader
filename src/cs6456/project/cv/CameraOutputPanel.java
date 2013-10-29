package cs6456.project.cv;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cs6456.project.util.CVUtil;

public class CameraOutputPanel extends JPanel implements ImageFrameReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BufferedImage background = null;

	public CameraOutputPanel() {
		this.setSize(400, 400);
	}

	@Override
	public void imageFrameRead(ImageFrameReadEvent event) {
		BufferedImage image = CVUtil.matToBufferedImage(event.getSource());
		this.background = image;
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		if ( this.background != null ) {
			g.drawImage(this.background, 10, 10, background.getWidth(), background.getHeight(), this);
		}
	}
}
