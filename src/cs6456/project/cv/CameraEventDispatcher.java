package cs6456.project.cv;

import java.util.ArrayList;
import java.util.List;

public class CameraEventDispatcher {
	List<ImageFrameReadListener> imageFrameReadListeners = new ArrayList<ImageFrameReadListener>();
	
	public void addImageFrameReadListener(ImageFrameReadListener listener) {
		this.imageFrameReadListeners.add(listener);
	}
	
	public void removeImageFrameReadListener(ImageFrameReadListener listener) {
		this.imageFrameReadListeners.remove(listener);
	}

	public void triggerImageFrameReadEvent(ImageFrameReadEvent event) {
		for ( ImageFrameReadListener listener : imageFrameReadListeners ) {
			listener.imageFrameRead(event);			
		}
	}
	
}
