package cs6456.project.ui;

import java.util.ArrayList;
import java.util.List;

public class UiState {
	final static public int BUTTON_SIZE = 96;

	List<UiStateObserver> observers = new ArrayList<UiStateObserver>();

	boolean locked = false;
	String message = "";

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
		observe();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
