package cs6456.project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cs6456.project.cv.HandVuEvent;
import cs6456.project.cv.HandVuEventListener;
import cs6456.project.ui.bookshelf.PdfButton;
import cs6456.project.ui.bookshelf.PdfButtonPanel;
import cs6456.project.ui.bookshelf.ScrollingBookshelfPanel;
import cs6456.project.util.ImageUtils;

public class GesturesGlassPane extends JPanel implements HandVuEventListener, UiStateObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * How many milliseconds for a particular action
	 */

	final static private int ACTION_COUNT = 2500;
	final static Icon UNLOCK_ICON = ImageUtils.getIcon("/glyphicons_204_unlock.png");
	final static Icon UNLOCKED_ICON = ImageUtils.getIcon("/glyphicons_204_unlock_inverted.png");
	final BufferedImage HAND_IMAGE = ImageUtils.getBufferedImage("/00-Pointer-Hand-icon-64.png");

	private JFrame frame;
	private JButton hoverButton = null;
	private long hoverTime = 0;
	private int hoverArc = 0;
	private boolean enabled = true;
	private boolean tracking = true;
	private String posture = null;
	private UiState uiState;
	private JButton unlockLeftButton = null;
	private JButton unlockRightButton = null;
	private JLabel popupLabel = null;
	private int unlockCount = 0;

	public GesturesGlassPane(JFrame frame, final UiState uiState) throws IOException {
		super();

		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		this.frame = frame;
		this.uiState = uiState;

		/*
		 * Create the left "unlock button".
		 * It has the UNLOCK_ICON
		 * It is invisible
		 * It is named "unlock" -- this is used later
		 * Its action listener will check its icon state and update
		 * the unlock counter.
		 * If unlockCounter == 2, then reset everything and unlock the page
		 */
		unlockLeftButton = new JButton();
		unlockLeftButton.setIcon(UNLOCK_ICON);
		unlockLeftButton.setVisible(false);
		unlockLeftButton.setName("unlock");
		unlockLeftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unlockLeftButton.getIcon() == UNLOCK_ICON) {
					unlockLeftButton.setIcon(UNLOCKED_ICON);
					unlockCount++;
				}
				if (unlockCount == 2) {
					unlockCount = 0;
					uiState.setLocked(false);
					unlockLeftButton.setIcon(UNLOCK_ICON);
					unlockRightButton.setIcon(UNLOCK_ICON);
				}
			}
		});

		/*
		 * Create the right "unlock button".
		 * It has the UNLOCK_ICON
		 * It is invisible
		 * It is named "unlock" -- this is used later
		 * Its action listener will check its icon state and update
		 * the unlock counter.
		 * If unlockCounter == 2, then reset everything and unlock the page
		 */
		unlockRightButton = new JButton();
		unlockRightButton.setIcon(UNLOCK_ICON);
		unlockRightButton.setVisible(false);
		unlockRightButton.setName("unlock");
		unlockRightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unlockRightButton.getIcon() == UNLOCK_ICON) {
					unlockRightButton.setIcon(UNLOCKED_ICON);
					unlockCount++;
				}
				if (unlockCount == 2) {
					unlockCount = 0;
					uiState.setLocked(false);
					unlockLeftButton.setIcon(UNLOCK_ICON);
					unlockRightButton.setIcon(UNLOCK_ICON);
				}
			}
		});

		/*
		 * The popup label displays short, informative messages.
		 * By using a TimerTask (defined later), the messages last for
		 * 10 seconds before disappearing.
		 */
		popupLabel = new JLabel("");
		popupLabel.setHorizontalAlignment(SwingConstants.CENTER);
		popupLabel.setBackground(Color.red);

		add(unlockLeftButton);
		add(unlockRightButton);
		add(popupLabel);

		// Lay out the left unlock button
		layout.putConstraint(SpringLayout.NORTH, unlockLeftButton, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, unlockLeftButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, unlockLeftButton, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, unlockLeftButton, 0, SpringLayout.WEST, this);

		// Lay out the right unlock button
		layout.putConstraint(SpringLayout.NORTH, unlockRightButton, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, unlockRightButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, unlockRightButton, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.EAST, unlockRightButton, 0, SpringLayout.EAST, this);

		// Lay out the popup label
		layout.putConstraint(SpringLayout.NORTH, popupLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, popupLabel, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, popupLabel, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, popupLabel, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);

		uiState.addObserver(this);

		setOpaque(false);
	}

	int handX = 0;
	int handY = 20;
	int pointerX = handX + 30;
	int pointerY = handY + 2;

	@Override
	public void handVuEvent(HandVuEvent event) {
		int width = this.getWidth();
		int height = this.getHeight();

		/*
		 * HandVu returns a value of 0..1 for the X and Y coordinates. These are
		 * relative values, where (0,0) is the upper-left (right) corner of the
		 * camera and (1,1) is the lower-right (left) corner of the camera. These
		 * values are the same, no matter the camera's resolution. Naturally, higher
		 * resolution means more granularity
		 */
		double xRatio = ((float) (int) (event.getX() * 100)) / 100;
		double yRatio = ((float) (int) (event.getY() * 100)) / 100;

		/*
		 * Convert the relative coordinates to absolute coordinates inside of the
		 * window. Because the left-right is reversed, we need to subtract in order
		 * to find the appropriate coordinates. This is using (0,0) as the
		 * upper-left corner.
		 */
		handX = (width - (int) ((double) width * xRatio)) - 30;
		handY = ((int) ((double) height * yRatio)) - 2;

		/*
		 * The index finger of the pointer is to the right and slightly down 
		 * from the upper-left corner
		 */
		pointerX = handX + 30;
		pointerY = handY + 2;

		tracking = event.isTracking();
		posture = event.getPosture();

		/*
		 * If the glass pane is not enabled or we are not tracking,
		 * then return
		 */
		if (!enabled || !tracking) {
			hoverTime = 0;
			hoverButton = null;
			hoverArc = 0;
			return;
		}

		/*
		 * Convert the glass pane point to a point within the frame.
		 * In actuality, this doesn't change the value, since the glass pane is the size of the frame.
		 */
		Point p = SwingUtilities.convertPoint(this, pointerX, pointerY, frame.getContentPane());
		if (p != null) {
			/*
			 * Find the component that is directly underneath the pointer
			 */
			Component component = findComponentUnderGlassPaneAt(p, frame);
			int index = 0;
			if (component instanceof JButton) {
				/*
				 * It is a button. What is its name?
				 */
				String name = component.getName();
				if (name == null) {
					name = "";
				}
				/*
				 * When the UI is locked, then only the "unlock" ubuttons will be active.
				 */
				if (uiState.isLocked() && !name.equalsIgnoreCase("unlock")) {
					hoverTime = 0;
					hoverButton = null;
					hoverArc = 0;
					return;
				}

				if (component instanceof PdfButton) {
					/*
					 * When hovering over a PdfButton, then update the selected book element
					 */
					PdfButtonPanel panel = findPdfButtonPanel(component);
					index = panel.getIndex();
					((ScrollingBookshelfPanel) panel.getParent()).changeCurrentButton(index, false);
					panel.getParent().repaint();
				}
				/*
				 * A "victory" posture means "click"
				 */
				if (posture != null && posture.toLowerCase().contains("victory")) {
					((JButton) component).doClick();
				} else {
					/*
					 * Keep track of how long we are hovering over a particular button
					 */
					if (hoverButton == component) {
						if (hoverTime == 0) {
							hoverTime = System.currentTimeMillis();
						} else if (System.currentTimeMillis() >= hoverTime + ACTION_COUNT) {
							/*
							 * We've hovered for the required amount of time, so invoke its "click"
							 */
							hoverTime = System.currentTimeMillis();
							hoverButton.doClick();
						} else {
							/*
							 * Calculate how far around the "progress arc" should go.  This is simply:
							 * timeElapsed / timeMax * 360
							 */
							long timeDiff = System.currentTimeMillis() - hoverTime;
							hoverArc = (int) (((double) timeDiff / ACTION_COUNT) * 360);
						}
					} else {
						hoverButton = (JButton) component;
						hoverTime = 0;
						hoverArc = 0;
					}
				}
			} else {
				hoverTime = 0;
				hoverButton = null;
				hoverArc = 0;
			}
		} else {
			hoverTime = 0;
			hoverButton = null;
			hoverArc = 0;
		}

		repaint();
	}

	/*
	 * Taken from:
	 * http://stackoverflow.com/questions/2733896/identifying-swing-component-at-a-particular-screen-coordinate-and-manually-dis
	 * 
	 * Modified slightly
	 */
	public Component findComponentUnderGlassPaneAt(Point p, Component top) {
		Component c = null;

		/*
		 * If the UI is locked, then look on the glass pane and not on the root element
		 */
		if (uiState.isLocked()) {
			c = ((RootPaneContainer) top).getGlassPane().getComponentAt(p);
		} else {
			if (top.isShowing()) {
				if (top instanceof RootPaneContainer)
					c = ((RootPaneContainer) top).getLayeredPane().findComponentAt(p);
				else
					c = ((Container) top).findComponentAt(p);
			}
		}

		return c;
	}

	/*
	 * Find the PDfButtonPanel that is a parent (somewhere) of the given component
	 */
	private PdfButtonPanel findPdfButtonPanel(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof PdfButtonPanel) {
			return (PdfButtonPanel) component;
		}
		return findPdfButtonPanel(component.getParent());
	}

	@Override
	public void paint(Graphics g) {
		/*
		 * Do it this way to make sure the hand is drawn on top of the controls
		 */
		paintChildren(g);
		paintComponent(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// Antialiasing makes it look a little better
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.blue);
		/*
		 * Draw the hand image
		 */
		g.drawImage(HAND_IMAGE, handX, handY, null);
		/*
		 * If hoverArc > 0, then we are hovering over something.  Draw a small circle to be used
		 * as a progress arc.
		 */
		if (hoverArc > 0) {
			g.drawArc(pointerX - 16, pointerY - 16, 32, 32, 0, hoverArc);
		}
	}

	/*
	 * Toggle the visibility of the glass pane
	 */
	public void toggleEnabled() {
		this.enabled = !this.enabled;
		setVisible(this.enabled);
	}

	/*
	 * Show the "unlock buttons"
	 */
	void showUnlockButtons() {
		unlockLeftButton.setVisible(true);
		unlockRightButton.setVisible(true);

		/*
		 * We need to perform this little trick with the tabbed pane in order to get the
		 * buttons to appear. I don't know why, but repainting and invalidating components did nothing,
		 * but switching tabs worked.
		 */
		GesturesTabbedPane pane = (GesturesTabbedPane) frame.getContentPane().getComponents()[0];
		int currentTab = pane.getSelectedIndex();
		pane.setSelectedIndex(currentTab == 0 ? 1 : 0);
		pane.setSelectedIndex(currentTab);
	}

	void hideUnlockButtons() {
		unlockLeftButton.setVisible(false);
		unlockRightButton.setVisible(false);

		/*
		 * Same as showing the buttons
		 */
		GesturesTabbedPane pane = (GesturesTabbedPane) frame.getContentPane().getComponents()[0];
		int currentTab = pane.getSelectedIndex();
		pane.setSelectedIndex(currentTab == 0 ? 1 : 0);
		pane.setSelectedIndex(currentTab);
	}

	public void showPopup(String message) {
		/*
		 * Display the popup message
		 */
		popupLabel.setText(message);
		popupLabel.setVisible(true);

		/*
		 * Create a timer task to hide the popup after 10 seconds
		 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				popupLabel.setText("");
				popupLabel.setVisible(false);
			}
		}, 10000);
	}

	@Override
	public void uiStateChange(UiState state) {
		if (state.isLocked()) {
			showPopup("Interface locked");
			showUnlockButtons();
		} else {
			showPopup("Interface Unlocked");
			hideUnlockButtons();
		}
	}
}
