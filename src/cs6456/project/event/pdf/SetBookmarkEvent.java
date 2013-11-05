package cs6456.project.event.pdf;

import java.util.EventObject;

public class SetBookmarkEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int delta;

	public SetBookmarkEvent(Object source) {
		super(source);
	}
}
