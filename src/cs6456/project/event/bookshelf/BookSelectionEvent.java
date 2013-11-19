package cs6456.project.event.bookshelf;

import java.util.EventObject;

/*
 * This event indicates that the user has selected a book to open
 */
public class BookSelectionEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookSelectionEvent(Object source) {
		super(source);
	}
}
