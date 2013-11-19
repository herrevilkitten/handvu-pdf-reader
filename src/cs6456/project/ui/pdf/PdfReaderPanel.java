package cs6456.project.ui.pdf;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import camick.swing.icon.RotatedIcon;
import camick.swing.icon.RotatedIcon.Rotate;
import camick.swing.icon.TextIcon;
import cs6456.project.event.pdf.GoToBookmarkEvent;
import cs6456.project.event.pdf.GoToBookshelfEvent;
import cs6456.project.event.pdf.PageChangeEvent;
import cs6456.project.event.pdf.SetBookmarkEvent;
import cs6456.project.ui.GesturesGlassPane;
import cs6456.project.ui.GesturesTabbedPane;
import cs6456.project.ui.StatusLabel;
import cs6456.project.ui.UiState;

public class PdfReaderPanel extends JPanel implements cs6456.project.event.EventDispatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SwingController controller;
	SwingViewBuilder factory;
	JPanel viewerComponentPanel;
	GesturesTabbedPane tabbedPane;
	UiState uiState;

	int bookmarkPage = 0;
	int currentPage = 0;

	public PdfReaderPanel(GesturesTabbedPane tabbedPane, final UiState uiState) {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		this.tabbedPane = tabbedPane;
		this.uiState = uiState;

		/*
		 * These are part of the IcePDF API for creating the Swing interface
		 */
		controller = new SwingController();
		factory = new GesturesPdfViewBuilder(controller);
		viewerComponentPanel = factory.buildViewerPanel();
		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		/*
		 * Create each hover button
		 */
		final JButton upButton = new JButton("Previous Page");
		final JButton downButton = new JButton("Next Page");
		final JButton bookshelfButton = new JButton("");
		bookshelfButton.setIcon(new RotatedIcon(new TextIcon(bookshelfButton, "Bookshelf"), Rotate.UP));
		final JButton setBookmarkButton = new JButton("");
		setBookmarkButton.setIcon(new RotatedIcon(new TextIcon(setBookmarkButton, "Set Bookmark"), Rotate.DOWN));
		final JButton gotoBookmarkButton = new JButton("");
		gotoBookmarkButton.setIcon(new RotatedIcon(new TextIcon(gotoBookmarkButton, "Goto Bookmark"), Rotate.DOWN));
		final JButton lockButton = new JButton("");
		lockButton.setIcon(new RotatedIcon(new TextIcon(lockButton, "Lock")));

		/*
		 * Add the components to the panel
		 */
		add(viewerComponentPanel);
		add(upButton);
		add(downButton);
		add(bookshelfButton);
		add(setBookmarkButton);
		add(gotoBookmarkButton);
		add(lockButton);

		// Attach the Up button to the panel
		layout.putConstraint(SpringLayout.WEST, upButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, upButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, upButton, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, upButton, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);

		// Attach the Down button to the panel
		layout.putConstraint(SpringLayout.WEST, downButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, downButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, downButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, downButton, -UiState.BUTTON_SIZE, SpringLayout.SOUTH, this);

		// Attach the Bookshelf button to the panel
		layout.putConstraint(SpringLayout.NORTH, bookshelfButton, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, bookshelfButton, -(500 - UiState.BUTTON_SIZE / 2), SpringLayout.SOUTH,
				this);
		layout.putConstraint(SpringLayout.WEST, bookshelfButton, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, bookshelfButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);

		// Attach the Set Bookmark button to the panel
		layout.putConstraint(SpringLayout.NORTH, setBookmarkButton, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, setBookmarkButton, -(500 - UiState.BUTTON_SIZE / 2), SpringLayout.SOUTH,
				this);
		layout.putConstraint(SpringLayout.EAST, setBookmarkButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, setBookmarkButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);

		// Attach the Goto Bookmark button to the panel
		layout.putConstraint(SpringLayout.SOUTH, gotoBookmarkButton, -UiState.BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, gotoBookmarkButton, (500 - UiState.BUTTON_SIZE / 2), SpringLayout.NORTH,
				this);
		layout.putConstraint(SpringLayout.EAST, gotoBookmarkButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, gotoBookmarkButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);

		// Attach the PDF viewer to the panel
		layout.putConstraint(SpringLayout.NORTH, viewerComponentPanel, 0, SpringLayout.SOUTH, upButton);
		layout.putConstraint(SpringLayout.SOUTH, viewerComponentPanel, 0, SpringLayout.NORTH, downButton);
		layout.putConstraint(SpringLayout.WEST, viewerComponentPanel, 0, SpringLayout.EAST, bookshelfButton);
		layout.putConstraint(SpringLayout.EAST, viewerComponentPanel, 0, SpringLayout.WEST, setBookmarkButton);

		// Attach the Lock button to the panel
		layout.putConstraint(SpringLayout.NORTH, lockButton, 500 - (UiState.BUTTON_SIZE / 2), SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lockButton, -UiState.BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, lockButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lockButton, 0, SpringLayout.WEST, this);

		/*
		 * Change the UI state to locked
		 */
		lockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uiState.setLocked(true);
			}
		});

		/*
		 * Go back one page
		 */
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(-1);
			}
		});

		/*
		 * Go forward one page
		 */
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(1);
			}
		});

		/*
		 * Go to the bookshelf
		 */
		bookshelfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToBookshelf();
			}
		});

		/*
		 * Set a bookmark
		 */
		setBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBookmark();
			}
		});

		/*
		 * Go to the previously set bookmark
		 */
		gotoBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToBookmark();
			}
		});

	}

	public SwingController getController() {
		return controller;
	}

	/*
	 * Open a new document and update the status label to reflect this
	 */
	public void openDocument(String document) {
		controller.openDocument(document);
		this.bookmarkPage = 0;
		this.currentPage = 0;
		StatusLabel statusLabel = this.tabbedPane.getStatusLabel();
		statusLabel.setDocument(document);
		statusLabel.setCurrentPage(1);
		statusLabel.setBookmarkPage(1);
		statusLabel.setLastPage(controller.getDocument().getNumberOfPages());
		statusLabel.repaint();
	}

	@Override
	public boolean dispatchEvent(EventObject event) {
		EventObject hlEvent = null;
		/*
		 * Translate the low-level events (keyboard, gesture) into high level PDF
		 * events
		 */
		if (event instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent) event;
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_PAGE_DOWN:
				/*
				 * Page down
				 */
				hlEvent = new PageChangeEvent(event, 1);
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_PAGE_UP:
				/*
				 * Page up
				 */
				hlEvent = new PageChangeEvent(event, -1);
				break;
			case KeyEvent.VK_END:
				/*
				 * Go to last Page
				 */
				hlEvent = new PageChangeEvent(event, controller.getDocument().getNumberOfPages()
						- controller.getCurrentPageNumber());
				break;
			case KeyEvent.VK_HOME:
				/*
				 * Go to first page
				 */
				hlEvent = new PageChangeEvent(event, -controller.getCurrentPageNumber());
				break;
			case KeyEvent.VK_BACK_SPACE:
				/*
				 * Return to bookshelf
				 */
				hlEvent = new GoToBookshelfEvent(event);
				break;
			case KeyEvent.VK_SPACE:
				/*
				 * Go to a previously selected bookmark
				 */
				hlEvent = new GoToBookmarkEvent(event);
				break;
			case KeyEvent.VK_B:
				/*
				 * Set a bookmark
				 */
				hlEvent = new SetBookmarkEvent(event);
				break;
			}
		}

		if (hlEvent == null) {
			return false;
		}

		/*
		 * Invoke the high level event
		 */
		if (hlEvent instanceof PageChangeEvent) {
			changePage(((PageChangeEvent) hlEvent).getDelta());
		} else if (hlEvent instanceof GoToBookshelfEvent) {
			goToBookshelf();
		} else if (hlEvent instanceof SetBookmarkEvent) {
			setBookmark();
		} else if (hlEvent instanceof GoToBookmarkEvent) {
			goToBookmark();
		}

		return true;
	}

	protected void changePage(int delta) {
		/*
		 * Change the page using the controller and then update the status label
		 */
		controller.goToDeltaPage(delta);
		this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
	}

	protected void goToBookshelf() {
		/*
		 * Switch to the first tab
		 */
		tabbedPane.setSelectedIndex(0);
	}

	protected void setBookmark() {
		/*
		 * Set a bookmark and then update the label
		 * Use the glass pane to create a popup
		 */
		this.bookmarkPage = controller.getCurrentPageNumber();
		this.tabbedPane.getStatusLabel().setBookmarkPage(controller.getCurrentPageNumber() + 1);
		Component glassPane = this.getRootPane().getGlassPane();
		if (glassPane instanceof GesturesGlassPane) {
			((GesturesGlassPane) glassPane).showPopup("Bookmark set to page " + (controller.getCurrentPageNumber() + 1));
		}
	}

	protected void goToBookmark() {
		/*
		 * Switch pages to the bookmark page and then update the label
		 */
		controller.goToDeltaPage(this.bookmarkPage - controller.getCurrentPageNumber());
		this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
	}
}
