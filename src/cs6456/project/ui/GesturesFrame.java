package cs6456.project.ui;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cs6456.project.cv.HandVuEventDispatcher;
import cs6456.project.cv.HandVuInputThread;
import cs6456.project.event.GlobalEventDispatcher;

public class GesturesFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StatusLabel statusLabel = new StatusLabel();
	JLabel gestureLabel = new JLabel("Gesture: ");

	HandVuEventDispatcher dispatcher = new HandVuEventDispatcher();

	public GesturesFrame() throws IOException {
		setLayout(new BorderLayout());
		final HandVuInputThread handVuInputThread = new HandVuInputThread(7045, this.dispatcher);
		handVuInputThread.start();

		GesturesTabbedPane tabbedPane = new GesturesTabbedPane(statusLabel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
		setSize(1000, 1000);
		add(tabbedPane, BorderLayout.CENTER);
		add(statusLabel, BorderLayout.SOUTH);

		final GlobalEventDispatcher globalEventDispatcher = new GlobalEventDispatcher(tabbedPane);

		final GesturesGlassPane glassPane = new GesturesGlassPane(this);
		this.dispatcher.addListener(glassPane);

		setGlassPane(glassPane);
		glassPane.setVisible(true);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
				new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							if (e.getKeyCode() == KeyEvent.VK_T) {
								glassPane.toggleEnabled();
							} else {
								System.err.println("Global key event: " + e.getKeyCode() + " "
										+ KeyEvent.getKeyText(e.getKeyCode()));
								return globalEventDispatcher.dispatchEvent(e);
							}
						}
						return false;
					}
				});

		// pack();
		setVisible(true);
	}

}
