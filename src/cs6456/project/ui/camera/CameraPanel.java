package cs6456.project.ui.camera;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import cs6456.project.cv.ImageFrameReadEvent;
import cs6456.project.cv.ImageFrameReadListener;
import cs6456.project.util.CVUtil;

public class CameraPanel extends JPanel implements ImageFrameReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BufferedImage background = null;

	public CameraPanel() {
		this.setSize(400, 400);
		this.setDoubleBuffered(true);
	}

	@Override
	public void imageFrameRead(ImageFrameReadEvent event) {
		Mat frame = event.getSource();
		Mat converted = new Mat();
		frame.convertTo(converted, Imgproc.COLOR_RGB2HSV);
		BufferedImage image = CVUtil.matToBufferedImage(frame);
		this.background = image;
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if ( this.background != null ) {
			g.drawImage(this.background, 10, 10, background.getWidth(), background.getHeight(), this);
		}
	}

}
