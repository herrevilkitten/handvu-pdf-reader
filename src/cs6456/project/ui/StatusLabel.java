package cs6456.project.ui;

import javax.swing.JLabel;

public class StatusLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String document;
	int currentPage = 1;
	int lastPage = 1;
	int bookmarkPage = 1;
	
	public StatusLabel() {
		updateLabel();
	}

	private void updateLabel() {
		StringBuilder label = new StringBuilder();

		label.append("Status: ");
		if (document != null && !document.isEmpty()) {
			label.append(String.format("%s, page %d of %d, bookmark page %d", document, currentPage,
					lastPage, bookmarkPage));
		} else {
			label.append("No document opened");
		}

		this.setText(label.toString());
		repaint();
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
		updateLabel();
	}

	public void setDocument(String document) {
		this.document = document;
		updateLabel();
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		updateLabel();
	}

	public void setBookmarkPage(int bookmarkPage) {
		this.bookmarkPage = bookmarkPage;
		updateLabel();
	}
}
