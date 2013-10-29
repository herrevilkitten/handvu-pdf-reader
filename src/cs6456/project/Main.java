package cs6456.project;

import javax.swing.JFrame;

import cs6456.project.cv.CameraEventDispatcher;
import cs6456.project.cv.CameraInputThread;
import cs6456.project.cv.CameraOutputPanel;



public class Main {

	public static void main(String[] args) throws Exception {

    System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary("opencv_java246_x64");
    
    CameraEventDispatcher dispatcher = new CameraEventDispatcher();
    CameraInputThread cameraThread = new CameraInputThread(dispatcher, 10);
    
    cameraThread.start();
    CameraOutputPanel panel = new CameraOutputPanel();
    dispatcher.addImageFrameReadListener(panel);
    
    JFrame frame = new JFrame("Camera Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(450, 450);
    frame.setContentPane(panel);
    frame.setVisible(true);
    
	/*
    // Get a file from the command line to open
		String filePath = "samples/16-Bit Adventures.pdf";

		// build a component controller
		SwingController controller = new SwingController();

		SwingViewBuilder factory = new GesturesPdfViewBuilder(controller);

		final JPanel viewerComponentPanel = factory.buildViewerPanel();

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		JFrame applicationFrame = new JFrame();
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.getContentPane().add(viewerComponentPanel);

		// Now that the GUI is all in place, we can try opening a PDF
		controller.openDocument(filePath);

		viewerComponentPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.err.println("Requesting focus for " + viewerComponentPanel);
				viewerComponentPanel.requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		viewerComponentPanel.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.err.println(e.getKeyCode() + " " + KeyEvent.getKeyText(e.getKeyCode()));
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		// show the component
		applicationFrame.pack();
		applicationFrame.setVisible(true);
		*/
	}
}
