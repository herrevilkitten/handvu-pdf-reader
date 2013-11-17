package cs6456.project.ui.pdf;

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
import cs6456.project.ui.GesturesTabbedPane;

public class PdfReaderPanel extends JPanel implements cs6456.project.event.EventDispatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static private int BUTTON_SIZE = 64;

	SwingController controller;
	SwingViewBuilder factory;
	JPanel viewerComponentPanel;
	GesturesTabbedPane tabbedPane;

	int bookmarkPage = 0;
	int currentPage = 0;

	public PdfReaderPanel(GesturesTabbedPane tabbedPane) {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		this.tabbedPane = tabbedPane;

		// build a component controller
		controller = new SwingController();

		factory = new GesturesPdfViewBuilder(controller);

		viewerComponentPanel = factory.buildViewerPanel();

		final JButton upButton = new JButton("Previous Page");
		final JButton downButton = new JButton("Next Page");
		final JButton bookshelfButton = new JButton("");
		bookshelfButton.setIcon(new RotatedIcon(new TextIcon(bookshelfButton, "Bookshelf"), Rotate.UP));
		final JButton setBookmarkButton = new JButton("");
		setBookmarkButton.setIcon(new RotatedIcon(new TextIcon(setBookmarkButton, "Bookmark"), Rotate.DOWN));
		final JButton gotoBookmarkButton = new JButton("");
		gotoBookmarkButton.setIcon(new RotatedIcon(new TextIcon(gotoBookmarkButton, "Goto"), Rotate.DOWN));

		add(viewerComponentPanel);
		add(upButton);
		add(downButton);
		add(bookshelfButton);
		add(setBookmarkButton);
		add(gotoBookmarkButton);

		// Attach the Up button to the panel
		layout.putConstraint(SpringLayout.WEST, upButton, BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, upButton, -BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, upButton, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, upButton, BUTTON_SIZE, SpringLayout.NORTH, this);

		// Attach the Down button to the panel
		layout.putConstraint(SpringLayout.WEST, downButton, BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, downButton, -BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, downButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, downButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);

		// Attach the Bookshelf button to the panel
		layout.putConstraint(SpringLayout.NORTH, bookshelfButton, BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, bookshelfButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, bookshelfButton, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, bookshelfButton, BUTTON_SIZE, SpringLayout.WEST, this);

		// Attach the Set Bookmark button to the panel
		layout.putConstraint(SpringLayout.NORTH, setBookmarkButton, BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, setBookmarkButton, -(500 - BUTTON_SIZE), SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, setBookmarkButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, setBookmarkButton, -BUTTON_SIZE, SpringLayout.EAST, this);

		// Attach the Goto Bookmark button to the panel
		layout.putConstraint(SpringLayout.SOUTH, gotoBookmarkButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, gotoBookmarkButton, (500 - BUTTON_SIZE), SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, gotoBookmarkButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, gotoBookmarkButton, -BUTTON_SIZE, SpringLayout.EAST, this);

		// Attach the PDF viewer to the panel
		layout.putConstraint(SpringLayout.NORTH, viewerComponentPanel, 0, SpringLayout.SOUTH, upButton);
		layout.putConstraint(SpringLayout.SOUTH, viewerComponentPanel, 0, SpringLayout.NORTH, downButton);
		layout.putConstraint(SpringLayout.WEST, viewerComponentPanel, 0, SpringLayout.EAST, bookshelfButton);
		layout.putConstraint(SpringLayout.EAST, viewerComponentPanel, 0, SpringLayout.WEST, setBookmarkButton);

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(-1);
			}
		});

		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(1);
			}
		});

		bookshelfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToBookshelf();
			}
		});

		setBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBookmark();
			}
		});

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
		 * Translate the low-level events (keyboard, gesture) into high level PDF
		 * events
		 */
		if (event instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent) event;
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_PAGE_DOWN:
				hlEvent = new PageChangeEvent(event, 1);
				break;
			case KeyEvent.VK_PAGE_UP:
				hlEvent = new PageChangeEvent(event, -1);
				break;
			case KeyEvent.VK_END:
				hlEvent = new PageChangeEvent(event, controller.getDocument().getNumberOfPages()
						- controller.getCurrentPageNumber());
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

		if (hlEvent == null) {
			return false;
		}

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
		controller.goToDeltaPage(delta);
		this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
	}

	protected void goToBookshelf() {
		tabbedPane.setSelectedIndex(0);
	}

	protected void setBookmark() {
		this.bookmarkPage = controller.getCurrentPageNumber();
		this.tabbedPane.getStatusLabel().setBookmarkPage(controller.getCurrentPageNumber() + 1);
	}

	protected void goToBookmark() {
		controller.goToDeltaPage(this.bookmarkPage - controller.getCurrentPageNumber());
		this.tabbedPane.getStatusLabel().setCurrentPage(controller.getCurrentPageNumber() + 1);
	}
}
