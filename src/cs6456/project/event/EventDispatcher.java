package cs6456.project.event;

import java.util.EventObject;

public interface EventDispatcher {
	public boolean dispatchEvent(EventObject event);
}
