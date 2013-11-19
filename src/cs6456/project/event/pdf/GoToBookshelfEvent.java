package cs6456.project.event.pdf;

import java.util.EventObject;

/*
 * Indicates that the user desires to return to the bookshelf view
 */
public class GoToBookshelfEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int delta;

	public GoToBookshelfEvent(Object source) {
		super(source);
	}
}
