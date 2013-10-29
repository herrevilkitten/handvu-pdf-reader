package cs6456.project.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import cs6456.project.pdf.GesturesPdfViewBuilder;

public class PdfReaderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PdfReaderPanel() {
		// Get a file from the command line to open
		String filePath = "samples/05460693.pdf";

		// build a component controller
		SwingController controller = new SwingController();

		SwingViewBuilder factory = new GesturesPdfViewBuilder(controller);

		final JPanel viewerComponentPanel = factory.buildViewerPanel();

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		// Now that the GUI is all in place, we can try opening a PDF
		controller.openDocument(filePath);

		viewerComponentPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.err.println("Requesting focus for " + viewerComponentPanel);
				viewerComponentPanel.requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		viewerComponentPanel.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.err.println(e.getKeyCode() + " " + KeyEvent.getKeyText(e.getKeyCode()));
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		add(viewerComponentPanel);
	}

}
