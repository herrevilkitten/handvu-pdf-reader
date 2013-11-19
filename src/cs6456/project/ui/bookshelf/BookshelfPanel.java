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
	final static private int BUTTON_SIZE = 96;

	public BookshelfPanel(SwingController controller, final GesturesTabbedPane tabbedPane, final UiState uiState) {
		this.scrolling = new ScrollingBookshelfPanel(controller, tabbedPane);
		this.tabbedPane = tabbedPane;
		this.uiState = uiState;
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		final JScrollPane viewport = new JScrollPane(scrolling);
		viewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
		layout.putConstraint(SpringLayout.WEST, upButton, BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, upButton, -BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, upButton, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, upButton, BUTTON_SIZE, SpringLayout.NORTH, this);

		// Attach the Down button to the panel
		layout.putConstraint(SpringLayout.WEST, downButton, BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, downButton, -BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, downButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, downButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);

		// Attach the Reader button to the panel
		layout.putConstraint(SpringLayout.NORTH, readerButton, BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, readerButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, readerButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, readerButton, -BUTTON_SIZE, SpringLayout.EAST, this);
		
		// Attach the Lock button to the panel
		layout.putConstraint(SpringLayout.NORTH, lockButton, 500 - (BUTTON_SIZE / 2), SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, lockButton, -BUTTON_SIZE, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, lockButton, BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, lockButton, 0, SpringLayout.WEST, this);

		// Attach the bookshelf to the buttons
		layout.putConstraint(SpringLayout.NORTH, viewport, 0, SpringLayout.SOUTH, upButton);
		layout.putConstraint(SpringLayout.SOUTH, viewport, 0, SpringLayout.NORTH, downButton);
		layout.putConstraint(SpringLayout.EAST, viewport, 0, SpringLayout.WEST, readerButton);
		layout.putConstraint(SpringLayout.WEST, viewport, BUTTON_SIZE, SpringLayout.WEST, this);

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
		
		readerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		
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
