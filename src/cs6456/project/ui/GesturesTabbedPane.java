package cs6456.project.ui;

import javax.swing.JTabbedPane;

public class GesturesTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BookshelfPanel bookshelfPanel;
	PdfReaderPanel pdfReaderPanel;
	CameraPanel cameraPanel;

	public GesturesTabbedPane() {
		bookshelfPanel = new BookshelfPanel();
		pdfReaderPanel = new PdfReaderPanel();
		cameraPanel = new CameraPanel();
		
		this.addTab("Shelf", bookshelfPanel);
		this.addTab("Book", pdfReaderPanel);
		this.addTab("Camera", cameraPanel);
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
}
