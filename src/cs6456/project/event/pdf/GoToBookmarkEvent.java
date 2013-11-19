package cs6456.project.event.pdf;

import java.util.EventObject;

/*
 * Indicates the user is going to a previously set bookmark
 */
public class GoToBookmarkEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int delta;

	public GoToBookmarkEvent(Object source) {
		super(source);
	}
}
