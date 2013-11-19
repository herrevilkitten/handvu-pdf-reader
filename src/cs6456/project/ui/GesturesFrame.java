package cs6456.project.ui;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;

import cs6456.project.cv.HandVuEventDispatcher;
import cs6456.project.cv.HandVuInputThread;
import cs6456.project.event.GlobalEventDispatcher;

/*
 * GesturesFrame is the top-level frame for the application.
 */
public class GesturesFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StatusLabel statusLabel = new StatusLabel();

	HandVuEventDispatcher dispatcher = new HandVuEventDispatcher();

	public GesturesFrame() throws IOException {
		setLayout(new BorderLayout());

		/*
		 * Start the HandVuInputThread, which will connect to the HandVu server and
		 * listen to it.
		 */
		final HandVuInputThread handVuInputThread = new HandVuInputThread(7045, this.dispatcher);
		handVuInputThread.start();

		/*
		 * UiState provides a global state and is observable
		 */
		final UiState uiState = new UiState();

		/*
		 * The main UI element is a JTabbedPane that holds the bookshelf and the
		 * reading pane
		 */
		GesturesTabbedPane tabbedPane = new GesturesTabbedPane(statusLabel, uiState);

		/*
		 * The default close event is to exit
		 */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 * Set the size of the application to 1000 x 1000
		 */
		setSize(1000, 1000);

		/*
		 * Put the tabbed pane in the center
		 */
		add(tabbedPane, BorderLayout.CENTER);

		/*
		 * Put the status label on the bottom of the page
		 */
		add(statusLabel, BorderLayout.SOUTH);

		/*
		 * Create a new GlobalEventDispatcher
		 */
		final GlobalEventDispatcher globalEventDispatcher = new GlobalEventDispatcher(tabbedPane);

		/*
		 * Create the GesturesGlassPane and attach it to the frame. The glass pane
		 * also listens to dispatched events
		 */
		final GesturesGlassPane glassPane = new GesturesGlassPane(this, uiState);
		this.dispatcher.addListener(glassPane);
		setGlassPane(glassPane);
		glassPane.setVisible(true);

		/*
		 * Set up the global key manager. This lets us override any existing key
		 * listeners, allowing us to send commands directly to UI controls.
		 */
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_T) {
						/*
						 * The "T" key will toggle the glass pane on and off.
						 */
						glassPane.toggleEnabled();
					} else if (e.getKeyCode() == KeyEvent.VK_L) {
						/*
						 * The "L" key will lock the UI
						 */
						uiState.setLocked(true);
					} else {
						return globalEventDispatcher.dispatchEvent(e);
					}
				}
				return false;
			}
		});
		setVisible(true);
	}
}
