package cs6456.project.ui.bookshelf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import org.icepdf.ri.common.SwingController;

import camick.swing.icon.RotatedIcon;
import camick.swing.icon.RotatedIcon.Rotate;
import camick.swing.icon.TextIcon;
import cs6456.project.event.EventDispatcher;
import cs6456.project.ui.GesturesTabbedPane;
import cs6456.project.ui.UiState;

public class BookshelfPanel extends JPanel implements EventDispatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ScrollingBookshelfPanel scrolling;
	GesturesTabbedPane tabbedPane;
	UiState uiState;

	public BookshelfPanel(SwingController controller, final GesturesTabbedPane tabbedPane, final UiState uiState) {
		this.scrolling = new ScrollingBookshelfPanel(controller, tabbedPane);
		this.tabbedPane = tabbedPane;
		this.uiState = uiState;
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		/*
		 * Create a JScrollPane to store the bookshelf ("ScrollingBookshelfPanel")
		 * Make sure the vertical scrollbar is always visible
		 */
		final JScrollPane viewport = new JScrollPane(scrolling);
		viewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		/*
		 * Create the hover buttons for the border
		 */
		final JButton upButton = new JButton("Up");
		final JButton downButton = new JButton("Down");
		final JButton readerButton = new JButton("");
		readerButton.setIcon(new RotatedIcon(new TextIcon(readerButton, "Reader"), Rotate.DOWN));
		final JButton lockButton = new JButton("");
		lockButton.setIcon(new RotatedIcon(new TextIcon(lockButton, "Lock")));

		add(viewport);
		add(upButton);
		add(downButton);
		add(readerButton);
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

		// Attach the Reader button to the panel
		layout.putConstraint(SpringLayout.NORTH, readerButton, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, readerButton, -UiState.BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, readerButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, readerButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		
		// Attach the Lock button to the panel
		layout.putConstraint(SpringLayout.NORTH, lockButton, 500 - (UiState.BUTTON_SIZE / 2), SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lockButton, -UiState.BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, lockButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lockButton, 0, SpringLayout.WEST, this);

		// Attach the bookshelf to the buttons
		layout.putConstraint(SpringLayout.NORTH, viewport, 0, SpringLayout.SOUTH, upButton);
		layout.putConstraint(SpringLayout.SOUTH, viewport, 0, SpringLayout.NORTH, downButton);
		layout.putConstraint(SpringLayout.EAST, viewport, 0, SpringLayout.WEST, readerButton);
		layout.putConstraint(SpringLayout.WEST, viewport, UiState.BUTTON_SIZE, SpringLayout.WEST, this);

		/*
		 * Because there are 4 entries per row, we can add or subtract 4 from the current
		 * index in order to scroll down or up
		 */
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrolling.changeCurrentButton(-4);
			}
		});

		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrolling.changeCurrentButton(4);
			}
		});
		
		/*
		 * Switch to the Reader tab
		 */
		readerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		
		/*
		 * Lock the screen
		 */
		lockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uiState.setLocked(true);
			}
		});
	}

	@Override
	public boolean dispatchEvent(EventObject event) {
		return scrolling.dispatchEvent(event);
	}
}
