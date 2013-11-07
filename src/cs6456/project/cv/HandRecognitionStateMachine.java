package cs6456.project.cv;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public class HandRecognitionStateMachine implements ImageFrameReadListener {
	enum HrState {
		INIT
	};
	
	HrState state = HrState.INIT;

	@Override
	public void imageFrameRead(ImageFrameReadEvent event) {
		Mat image = event.getSource();
		Size size = image.size();
		
		Mat src = new Mat(size, 8);
	}
}
