package cs6456.project.ui;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import cs6456.project.ui.bookshelf.BookshelfPanel;
import cs6456.project.ui.pdf.PdfReaderPanel;

public class GesturesTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BookshelfPanel bookshelfPanel;
	PdfReaderPanel pdfReaderPanel;
	StatusLabel statusLabel;
	UiState uiState;

	public GesturesTabbedPane(StatusLabel statusLabel, UiState uiState) {
		this.statusLabel = statusLabel;
		
		pdfReaderPanel = new PdfReaderPanel(this, uiState);
		bookshelfPanel = new BookshelfPanel(pdfReaderPanel.getController(), this, uiState);
		this.uiState = uiState;
		
		this.addTab("Shelf", new JScrollPane(bookshelfPanel));
		this.addTab("Reader", pdfReaderPanel);
	}

	public BookshelfPanel getBookshelfPanel() {
		return bookshelfPanel;
	}

	public PdfReaderPanel getPdfReaderPanel() {
		return pdfReaderPanel;
	}

	public StatusLabel getStatusLabel() {
		return statusLabel;
	}
}
