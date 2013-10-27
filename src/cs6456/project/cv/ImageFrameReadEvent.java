package cs6456.project.cv;

import java.util.EventObject;

import org.opencv.core.Mat;

public class ImageFrameReadEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageFrameReadEvent(Mat source) {
		super(source);
	}
	
	public Mat getSource() {
		return (Mat) this.source;
	}
}
