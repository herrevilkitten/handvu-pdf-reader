package cs6456.project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import cs6456.project.pdf.GesturesPdfViewBuilder;


public class Main {

	public static void main(String[] args) throws Exception {

    System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary("opencv_java246_x64");

    VideoCapture camera = new VideoCapture(0);
    Thread.sleep(1000);
    camera.open(0); //Useless
    if(!camera.isOpened()){
        System.out.println("Camera Error");
    }
    else{
        System.out.println("Camera OK?");
    }

    Mat frame = new Mat();

    //camera.grab();
    //System.out.println("Frame Grabbed");
    //camera.retrieve(frame);
    //System.out.println("Frame Decoded");

    camera.read(frame);
    System.out.println("Frame Obtained");

    /* No difference
    camera.release();
    */

    System.out.println("Captured Frame Width " + frame.width());

    Highgui.imwrite("camera.jpg", frame);
    System.out.println("OK");
    
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
		
	}
}
