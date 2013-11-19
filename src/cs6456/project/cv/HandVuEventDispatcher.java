package cs6456.project.cv;

import java.util.ArrayList;
import java.util.List;

/*
 * Dispatches HandVu events to any listeners
 */
public class HandVuEventDispatcher {
	List<HandVuEventListener> listeners = new ArrayList<HandVuEventListener>();
	
	public void addListener(HandVuEventListener listener) {
		this.listeners.add(listener);
	}
	
	public void dispatch(HandVuEvent event) {
		for ( HandVuEventListener listener : listeners ) {
			listener.handVuEvent(event);
		}
	}
}
