package cs6456.project.ui;

import java.util.ArrayList;
import java.util.List;

public class UiState {
	// The button size for all of the border gesture controls
	final static public int BUTTON_SIZE = 96;
	
	// The number of documents to display per row in the bookshelf
	final static public int BOOKSHELF_SHELF_SIZE = 4;

	List<UiStateObserver> observers = new ArrayList<UiStateObserver>();

	// is the UI locked?
	boolean locked = false;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
		observe();
	}

	public void addObserver(UiStateObserver observer) {
		this.observers.add(observer);
	}

	void observe() {
		for (UiStateObserver observer : observers) {
			observer.uiStateChange(this);
		}
	}
}
