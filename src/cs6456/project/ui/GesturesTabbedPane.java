package cs6456.project.ui;

import javax.swing.JScrollPane;
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
		pdfReaderPanel = new PdfReaderPanel();
		bookshelfPanel = new BookshelfPanel(pdfReaderPanel.getController());
		cameraPanel = new CameraPanel();
		
		this.addTab("Shelf", new JScrollPane(bookshelfPanel));
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
