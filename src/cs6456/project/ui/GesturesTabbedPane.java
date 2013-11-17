package cs6456.project.ui;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import cs6456.project.ui.bookshelf.BookshelfPanel;
import cs6456.project.ui.camera.CameraPanel;
import cs6456.project.ui.pdf.PdfReaderPanel;

public class GesturesTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BookshelfPanel bookshelfPanel;
	PdfReaderPanel pdfReaderPanel;
	CameraPanel cameraPanel;
	StatusLabel statusLabel;

	public GesturesTabbedPane(StatusLabel statusLabel) {
		this.statusLabel = statusLabel;
		
		pdfReaderPanel = new PdfReaderPanel(this);
		bookshelfPanel = new BookshelfPanel(pdfReaderPanel.getController(), this);
//		cameraPanel = new CameraPanel();
		
		this.addTab("Shelf", new JScrollPane(bookshelfPanel));
		this.addTab("Reader", pdfReaderPanel);
//		this.addTab("Camera", cameraPanel);
	}

	public BookshelfPanel getBookshelfPanel() {
		return bookshelfPanel;
	}

	public PdfReaderPanel getPdfReaderPanel() {
		return pdfReaderPanel;
	}

	public CameraPanel getCameraPanel() {
		return cameraPanel;
	}

	public StatusLabel getStatusLabel() {
		return statusLabel;
	}
}
