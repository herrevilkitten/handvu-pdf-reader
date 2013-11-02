package cs6456.project.event.bookshelf;

import java.util.EventObject;

public class BookshelfChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int amount = 0;

	public BookshelfChangeEvent(Object source, int amount) {
		super(source);
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
