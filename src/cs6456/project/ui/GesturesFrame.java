package cs6456.project.ui;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cs6456.project.cv.CameraEventDispatcher;
import cs6456.project.cv.CameraInputThread;
import cs6456.project.cv.HandRecognitionStateMachine;
import cs6456.project.cv.HandVuInputThread;
import cs6456.project.event.GlobalEventDispatcher;

public class GesturesFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StatusLabel statusLabel = new StatusLabel();
	JLabel gestureLabel = new JLabel("Gesture: ");
	
	public GesturesFrame() {
		setLayout(new BorderLayout());
		CameraEventDispatcher dispatcher = new CameraEventDispatcher();
		final CameraInputThread cameraThread = new CameraInputThread(dispatcher, 60);

		//cameraThread.start();
		
		final HandVuInputThread handVuInputThread = new HandVuInputThread(7045);
		handVuInputThread.start();
		
		HandRecognitionStateMachine hrsm = new HandRecognitionStateMachine();

		GesturesTabbedPane tabbedPane = new GesturesTabbedPane(statusLabel);
		dispatcher.addImageFrameReadListener(tabbedPane.getCameraPanel());
		dispatcher.addImageFrameReadListener(hrsm);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				cameraThread.close();
				super.windowClosing(e);
			}
		});
		setSize(750, 1000);
		add(gestureLabel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
		add(statusLabel, BorderLayout.SOUTH);
		setVisible(true);
		
		final GlobalEventDispatcher globalEventDispatcher = new GlobalEventDispatcher(tabbedPane);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
				new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if ( e.getID() == KeyEvent.KEY_PRESSED ) {
							System.err.println("Global key event: " + e.getKeyCode() + " "
									+ KeyEvent.getKeyText(e.getKeyCode()));
							return globalEventDispatcher.dispatchEvent(e);							
						}
						return false;
					}
				});

	}
}
