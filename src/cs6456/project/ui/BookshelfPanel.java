package cs6456.project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.icepdf.ri.common.SwingController;

import cs6456.project.event.EventDispatcher;
import cs6456.project.event.bookshelf.BookSelectionEvent;
import cs6456.project.event.bookshelf.BookshelfChangeEvent;
import cs6456.project.ui.bookshelf.PdfButtonPanel;
import cs6456.project.ui.bookshelf.ThumbnailLoaderThread;

public class BookshelfPanel extends JPanel implements EventDispatcher, Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int currentButton = 0;
	SwingController controller;
	
	public BookshelfPanel(SwingController controller) {
		this.controller = controller;
		
		setLayout(new GridLayout(0, 4));

		File bookshelfDir = new File("bookshelf");

		if (bookshelfDir.exists()) {
			for (File file : bookshelfDir.listFiles()) {
				if (!file.isFile()) {
					continue;
				}
				String filename = file.getName();

				if (!filename.toLowerCase().endsWith(".pdf")) {
					continue;
				}

				PdfButtonPanel buttonPanel = new PdfButtonPanel(filename, controller);
				add(buttonPanel);
			}
		}

		ThumbnailLoaderThread thread = new ThumbnailLoaderThread(this);
		thread.start();
	}
	
	public void setCurrentButton(PdfButtonPanel panel) {
		int index = 0;
		for ( Component c : getComponents() ) {
			if ( c == panel ) {
				currentButton = index;
				repaint();
				break;
			}
			index++;
		}
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
			case KeyEvent.VK_LEFT:
				hlEvent = new BookshelfChangeEvent(event, -1);
				break;
			case KeyEvent.VK_RIGHT:
				hlEvent = new BookshelfChangeEvent(event, 1);
				break;
			case KeyEvent.VK_DOWN:
				hlEvent = new BookshelfChangeEvent(event, 4);
				break;
			case KeyEvent.VK_UP:
				hlEvent = new BookshelfChangeEvent(event, -4);
				break;
			case KeyEvent.VK_ENTER:
				hlEvent = new BookSelectionEvent(event);
				break;
			}
		}
		
		if ( hlEvent == null ) {
			return false;
		}
		
		if ( hlEvent instanceof BookshelfChangeEvent ) {
			BookshelfChangeEvent bce = (BookshelfChangeEvent) hlEvent;
			changeCurrentButton(bce.getAmount());
			repaint();
		} else if ( hlEvent instanceof BookSelectionEvent ) {
			getCurrentButtonPanel().getButton().doClick();
		}
		return true;
	}
	
	public PdfButtonPanel getCurrentButtonPanel() {
		Component[] components = getComponents();
		return (PdfButtonPanel) components[currentButton];
	}
	
	public int getCurrentButton() {
		return currentButton;
	}
	
	public int changeCurrentButton(int delta) {
		currentButton = Math.max(Math.min(currentButton + delta, (getComponents()).length), 0);
		return currentButton;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
