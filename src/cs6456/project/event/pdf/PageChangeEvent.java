package cs6456.project.event.pdf;

import java.util.EventObject;

/*
 * Indicates the user is changing to a different page in the PDF
 */
public class PageChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int delta;

	public PageChangeEvent(Object source, int delta) {
		super(source);

		this.delta = delta;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}
}
