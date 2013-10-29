package cs6456.project.cv;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class CameraInputThread extends Thread {
	CameraEventDispatcher dispatcher;
	int rate;
	long sleepTime;
	
	public CameraInputThread(CameraEventDispatcher dispatcher, int rate) {
		super();
		this.setDaemon(true);
		this.dispatcher = dispatcher;
		this.rate = rate;
		this.sleepTime = 1000 / this.rate;
	}

	@Override
	public void run() {
		Mat image = new Mat();
		VideoCapture capture = new VideoCapture(0);

		try {
			if (capture.isOpened()) {
				while (true) {
					capture.read(image);
					if (!image.empty()) {
						ImageFrameReadEvent event = new ImageFrameReadEvent(image);
						dispatcher.triggerImageFrameReadEvent(event);
					} else {
						System.out.println(" --(!) No captured frame -- Break!");
						break;
					}
					Thread.sleep(this.sleepTime);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
