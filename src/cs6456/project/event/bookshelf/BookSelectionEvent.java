package cs6456.project.event.bookshelf;

import java.util.EventObject;

public class BookSelectionEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookSelectionEvent(Object source) {
		super(source);
	}
}
