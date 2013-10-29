package cs6456.project.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import cs6456.project.cv.CameraEventDispatcher;
import cs6456.project.cv.CameraInputThread;

public class GesturesFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GesturesFrame() {
		CameraEventDispatcher dispatcher = new CameraEventDispatcher();
		final CameraInputThread cameraThread = new CameraInputThread(dispatcher, 10);

		cameraThread.start();

		GesturesTabbedPane tabbedPane = new GesturesTabbedPane();
		dispatcher.addImageFrameReadListener(tabbedPane.getCameraPanel());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				cameraThread.close();
				super.windowClosing(e);
			}
		});
		setSize(450, 450);
		setContentPane(tabbedPane);
		setVisible(true);
	}
}
