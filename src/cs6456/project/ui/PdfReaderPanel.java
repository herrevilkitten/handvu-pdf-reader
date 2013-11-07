package cs6456.project.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import cs6456.project.event.pdf.GoToBookmarkEvent;
import cs6456.project.event.pdf.GoToBookshelfEvent;
import cs6456.project.event.pdf.PageChangeEvent;
import cs6456.project.event.pdf.SetBookmarkEvent;
import cs6456.project.pdf.GesturesPdfViewBuilder;

public class PdfReaderPanel extends JPanel implements cs6456.project.event.EventDispatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SwingController controller;
	SwingViewBuilder factory;
	JPanel viewerComponentPanel;
	GesturesTabbedPane tabbedPane;
	
	int bookmarkPage = 0;
	int currentPage = 0;

	public PdfReaderPanel(GesturesTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		
		// build a component controller
		controller = new SwingController();

		factory = new GesturesPdfViewBuilder(controller);

		viewerComponentPanel = factory.buildViewerPanel();

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		viewerComponentPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.err.println("Requesting focus for " + viewerComponentPanel);
				viewerComponentPanel.requestFocus();
			}
		});

		add(viewerComponentPanel);
	}

	public SwingController getController() {
		return controller;
	}
	
	public void openDocument(String document) {
		controller.openDocument(document);
		this.bookmarkPage = 0;
		this.currentPage = 0;
		this.tabbedPane.getStatusLabel().setDocument(document);
		this.tabbedPane.getStatusLabel().setCurrentPage(1);
		this.tabbedPane.getStatusLabel().setLastPage(controller.getDocument().getNumberOfPages());
	}

	@Override
	public boolean dispatchEvent(EventObject event) {
		System.err.println(getClass().getSimpleName() + " handling " + event);
		EventObject hlEvent = null;
		/*
		 * Translate the low-level events (keyboard, gesture) into high level PDF events
		 */
		if ( event instanceof KeyEvent ) {
			KeyEvent ke = (KeyEvent) event;
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_PAGE_DOWN:
				hlEvent = new PageChangeEvent(event, 1);
				break;
			case KeyEvent.VK_PAGE_UP:
				hlEvent = new PageChangeEvent(event, -1);
				break;
			case KeyEvent.VK_END:
				hlEvent = new PageChangeEvent(event, controller.getDocument().getNumberOfPages() - controller.getCurrentPageNumber());
				break;
			case KeyEvent.VK_HOME:
				hlEvent = new PageChangeEvent(event, -controller.getCurrentPageNumber());
				break;
			case KeyEvent.VK_BACK_SPACE:
				hlEvent = new GoToBookshelfEvent(event);
				break;
			case KeyEvent.VK_SPACE:
				hlEvent = new GoToBookmarkEvent(event);
				break;
			case KeyEvent.VK_B:
				hlEvent = new SetBookmarkEvent(event);
				break;
			}
		}
		
		if ( hlEvent == null ) {
			return false;
		}
		
		if ( hlEvent instanceof PageChangeEvent ) {
			controller.goToDeltaPage(((PageChangeEvent) hlEvent).getDelta());
			this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
		} else if ( hlEvent instanceof GoToBookshelfEvent ) {
			tabbedPane.setSelectedIndex(0);
		} else if ( hlEvent instanceof SetBookmarkEvent ) {
			this.bookmarkPage = controller.getCurrentPageNumber();
			this.tabbedPane.getStatusLabel().setBookmarkPage(controller.getCurrentPageNumber() + 1);
		} else if ( hlEvent instanceof GoToBookmarkEvent ) {
			controller.goToDeltaPage(this.bookmarkPage - controller.getCurrentPageNumber());
			this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
		}

		return true;
	}
}
