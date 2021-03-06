package cs6456.project.ui.bookshelf;

import java.awt.Component;
import java.awt.Dimension;
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
import cs6456.project.ui.GesturesTabbedPane;
import cs6456.project.ui.UiState;

/*
 * This class loads and keeps track of the Bookshelf contents, as well as handling the viewport state
 * (via Scrollable)
 */
public class ScrollingBookshelfPanel extends JPanel implements EventDispatcher, Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int currentButton = 0;
	SwingController controller;
	GesturesTabbedPane tabbedPane;
	
	public ScrollingBookshelfPanel(SwingController controller, GesturesTabbedPane tabbedPane) {
		this.controller = controller;
		this.tabbedPane = tabbedPane;
		
		/*
		 * We can display 4 documents horizontally, and unlimited vertically
		 */
		setLayout(new GridLayout(0, UiState.BOOKSHELF_SHELF_SIZE));

		/*
		 * Open the "bookshelf" directory and scan it for files ending with ".pdf"
		 */
		File bookshelfDir = new File("bookshelf");

		if (bookshelfDir.exists()) {
			int index = 0;
			for (File file : bookshelfDir.listFiles()) {
				if (!file.isFile()) {
					continue;
				}
				String filename = file.getName();

				if (!filename.toLowerCase().endsWith(".pdf")) {
					continue;
				}

				/*
				 * Create a button for each file and add the button to the panel 
				 */
				PdfButtonPanel buttonPanel = new PdfButtonPanel(filename, tabbedPane, index++);
				add(buttonPanel);
			}
		}

		/*
		 * Create and start the ThumbnailLoaderThread
		 */
		ThumbnailLoaderThread thread = new ThumbnailLoaderThread(this);
		thread.start();
	}
	
	/*
	 * Set the current button to the given PdfButtonPanel
	 */
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
		EventObject hlEvent = null;
		/*
		 * Translate the low-level events (keyboard, gesture) into high level PDF events
		 */
		if ( event instanceof KeyEvent ) {
			KeyEvent ke = (KeyEvent) event;
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				/*
				 * Move left one book
				 */
				hlEvent = new BookshelfChangeEvent(event, -1);
				break;
			case KeyEvent.VK_RIGHT:
				/*
				 * Move right one book
				 */
				hlEvent = new BookshelfChangeEvent(event, 1);
				break;
			case KeyEvent.VK_DOWN:
				/*
				 * Move down one book/right four books
				 */
				hlEvent = new BookshelfChangeEvent(event, UiState.BOOKSHELF_SHELF_SIZE);
				break;
			case KeyEvent.VK_UP:
				/*
				 * Move up one book/left four books
				 */
				hlEvent = new BookshelfChangeEvent(event, -UiState.BOOKSHELF_SHELF_SIZE);
				break;
			case KeyEvent.VK_ENTER:
				/*
				 * Select the book
				 */
				hlEvent = new BookSelectionEvent(event);
				break;
			}
		}
		
		/*
		 * If we have not matched a high-level event, then return
		 */
		if ( hlEvent == null ) {
			return false;
		}
		
		if ( hlEvent instanceof BookshelfChangeEvent ) {
			/*
			 * If it is a BookshelfChangeEvent, then change the currently selected button
			 * and repaint
			 */
			BookshelfChangeEvent bce = (BookshelfChangeEvent) hlEvent;
			changeCurrentButton(bce.getAmount());
			repaint();
		} else if ( hlEvent instanceof BookSelectionEvent ) {
			/*
			 * Open the document by "clicking" on the button
			 */
			getCurrentButtonPanel().getButton().doClick();
		}
		return true;
	}
	
	public PdfButtonPanel getCurrentButtonPanel() {
		Component[] components = getComponents();
		return (PdfButtonPanel) components[getCurrentButton()];
	}
	
	public int getCurrentButton() {
		return Math.max(Math.min(currentButton, (getComponents()).length - 1), 0);
	}
	
	/*
	 * Change the currently selected button by the amount given.
	 * 
	 * Scroll to make sure the selected button is visible.
	 */
	public int changeCurrentButton(int delta) {
		currentButton = Math.max(Math.min(currentButton + delta, (getComponents()).length - 1), 0);
		this.scrollRectToVisible(getCurrentButtonPanel().getBounds());
		return currentButton;
	}

	/*
	 * Change the currently selected button to either the absolute value or by
	 * the amount given.
	 * 
	 * Scroll to make sure the selected button is visible.
	 */
	public int changeCurrentButton(int amount, boolean delta) {
		if ( delta ) {
			return changeCurrentButton(amount);
		}
		currentButton = Math.max(Math.min(amount, (getComponents()).length - 1), 0);
		this.scrollRectToVisible(getCurrentButtonPanel().getBounds());
		return currentButton;
	}
	
	/*
	 * These methods implement the Scrollable interface, so the bookshelf panel acts
	 * as a viewport.
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
