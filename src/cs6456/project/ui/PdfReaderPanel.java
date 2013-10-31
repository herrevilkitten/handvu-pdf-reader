package cs6456.project.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import cs6456.project.event.pdf.PageChangeEvent;
import cs6456.project.pdf.GesturesPdfViewBuilder;

public class PdfReaderPanel extends JPanel implements cs6456.project.event.EventDispatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SwingController controller;
	SwingViewBuilder factory;
	JPanel viewerComponentPanel;

	public PdfReaderPanel() {
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
				hlEvent = new PageChangeEvent(event.getSource(), 1);
				break;
			case KeyEvent.VK_PAGE_UP:
				hlEvent = new PageChangeEvent(event.getSource(), -1);
				break;
			}
		}
		
		if ( hlEvent == null ) {
			return false;
		}
		
		if ( hlEvent instanceof PageChangeEvent ) {
			controller.goToDeltaPage(((PageChangeEvent) hlEvent).getDelta());
		}

		return true;
	}
}
