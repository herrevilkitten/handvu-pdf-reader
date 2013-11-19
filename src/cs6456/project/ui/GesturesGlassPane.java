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

	private BufferedImage handImage = ImageUtils.getBufferedImage("/00-Pointer-Hand-icon-64.png");
	private JFrame frame;
	private JButton hoverButton = null;
	private long hoverTime = 0;
	private int hoverArc = 0;
	private boolean enabled = true;
	private boolean tracking = true;
	private String posture = null;
	UiState uiState;
	private JButton unlockLeft = null;
	private JButton unlockRight = null;
	private JLabel popup = null;

	/*
	 * How many milliseconds for a particular action
	 */

	final static private int ACTION_COUNT = 2500;

	Icon unlockIcon = ImageUtils.getIcon("/glyphicons_204_unlock.png");
	Icon unlockedIcon = ImageUtils.getIcon("/glyphicons_204_unlock_inverted.png");
	int unlockCount = 0;

	public GesturesGlassPane(JFrame frame, final UiState uiState) throws IOException {
		super();

		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		this.frame = frame;
		this.uiState = uiState;

		unlockLeft = new JButton();
		unlockLeft.setIcon(unlockIcon);
		unlockLeft.setVisible(false);
		unlockLeft.setName("unlock");
		unlockLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unlockLeft.getIcon() == unlockIcon) {
					unlockLeft.setIcon(unlockedIcon);
					unlockCount++;
				}
				if (unlockCount == 2) {
					unlockCount = 0;
					uiState.setLocked(false);
					unlockLeft.setIcon(unlockIcon);
					unlockRight.setIcon(unlockIcon);
				}
			}
		});

		unlockRight = new JButton();
		unlockRight.setIcon(unlockIcon);
		unlockRight.setVisible(false);
		unlockRight.setName("unlock");
		unlockRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unlockRight.getIcon() == unlockIcon) {
					unlockRight.setIcon(unlockedIcon);
					unlockCount++;
				}
				if (unlockCount == 2) {
					unlockCount = 0;
					uiState.setLocked(false);
					unlockLeft.setIcon(unlockIcon);
					unlockRight.setIcon(unlockIcon);
				}
			}
		});
		
		popup = new JLabel("");
		popup.setHorizontalAlignment(SwingConstants.CENTER);
		popup.setBackground(Color.red);

		add(unlockLeft);
		add(unlockRight);
		add(popup);

		layout.putConstraint(SpringLayout.NORTH, unlockLeft, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, unlockLeft, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, unlockLeft, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, unlockLeft, 0, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, unlockRight, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, unlockRight, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, unlockRight, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.EAST, unlockRight, 0, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, popup, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, popup, UiState.BUTTON_SIZE, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, popup, UiState.BUTTON_SIZE, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, popup, -UiState.BUTTON_SIZE, SpringLayout.EAST, this);
		
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

		pointerX = handX + 30;
		pointerY = handY + 2;

		tracking = event.isTracking();
		posture = event.getPosture();

		if (!enabled || !tracking) {
			hoverTime = 0;
			hoverButton = null;
			hoverArc = 0;
			return;
		}

		Point p = SwingUtilities.convertPoint(this, pointerX, pointerY, frame.getContentPane());
		if (p != null) {
			Component component = findComponentUnderGlassPaneAt(p, frame);
			int index = 0;
			if (component instanceof JButton) {
				String name = component.getName();
				if (name == null) {
					name = "";
				}
				if (uiState.isLocked() && !name.equalsIgnoreCase("unlock")) {
					hoverTime = 0;
					hoverButton = null;
					hoverArc = 0;
					return;
				}
				if (component instanceof PdfButton) {
					PdfButtonPanel panel = findPdfButtonPanel(component);
					index = panel.getIndex();
					((ScrollingBookshelfPanel) panel.getParent()).changeCurrentButton(index, false);
					panel.getParent().repaint();
				}
				if (posture != null && posture.toLowerCase().contains("victory")) {
					((JButton) component).doClick();
				} else {
					if (hoverButton == component) {
						if (hoverTime == 0) {
							hoverTime = System.currentTimeMillis();
						} else if (System.currentTimeMillis() >= hoverTime + ACTION_COUNT) {
							hoverTime = System.currentTimeMillis();
							hoverButton.doClick();
						} else {
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

	public Component findComponentUnderGlassPaneAt(Point p, Component top) {
		Component c = null;

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
		paintChildren(g);
		paintComponent(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.blue);
		g.drawImage(handImage, handX, handY, null);
		if (hoverArc > 0) {
			g.drawArc(pointerX - 16, pointerY - 16, 32, 32, 0, hoverArc);
		}
	}

	public void toggleEnabled() {
		this.enabled = !this.enabled;
		setVisible(this.enabled);
	}

	void showUnlockButtons() {
		unlockLeft.setVisible(true);
		unlockRight.setVisible(true);

		GesturesTabbedPane pane = (GesturesTabbedPane) frame.getContentPane().getComponents()[0];
		int currentTab = pane.getSelectedIndex();
		pane.setSelectedIndex(currentTab == 0 ? 1 : 0);
		pane.setSelectedIndex(currentTab);
	}

	void hideUnlockButtons() {
		unlockLeft.setVisible(false);
		unlockRight.setVisible(false);

		GesturesTabbedPane pane = (GesturesTabbedPane) frame.getContentPane().getComponents()[0];
		int currentTab = pane.getSelectedIndex();
		pane.setSelectedIndex(currentTab == 0 ? 1 : 0);
		pane.setSelectedIndex(currentTab);
	}
	
	public void showPopup(String message) {
		popup.setText(message);
		popup.setVisible(true);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				popup.setText("");
				popup.setVisible(false);
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
		
		if ( !uiState.getMessage().isEmpty() ) {
		}
	}
}
