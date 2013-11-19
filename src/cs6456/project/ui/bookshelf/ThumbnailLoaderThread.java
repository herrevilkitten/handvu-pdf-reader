package cs6456.project.ui.bookshelf;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

/*
 * This class is used to load the thumbnails for the PDFs (defined as an image of the first page)
 * It runs in its own thread so that it doesn't interfere with the Swing UI thread
 */
public class ThumbnailLoaderThread extends Thread {
	
	JPanel bookshelfPanel;
	
	public ThumbnailLoaderThread(JPanel bookshelfPanel) {
		super("Thumbnail Loader");
		setDaemon(true);
		
		this.bookshelfPanel = bookshelfPanel;
	}
	
	public void run() {
		/*
		 * Go through each component in the bookshelfPanel
		 */
		for ( Component c : bookshelfPanel.getComponents() ) {
			/*
			 * If it's not a PdfButtonPanel, move on
			 */
			if ( !(c instanceof PdfButtonPanel) ) {
				continue;
			}
			PdfButtonPanel panel = (PdfButtonPanel) c;
			String filename = panel.getFilename();

			/*
			 * Create a new PDF document for each file 
			 */
			Document pdf = new Document();
			try {
				pdf.setFile("bookshelf/" + filename);
				/*
				 * Get the dimensions of the document and then calculate the width:height ratio
				 */
				PDimension dimensions = pdf.getPageDimension(0, 0.0f);
				float ratio = dimensions.getWidth() / dimensions.getHeight();
				
				/*
				 * Use the ratio to determine the button size, given a maximum width of 200 pixels
				 */
				float newWidth = 200 * ratio;
				
				/*
				 * Create a new thumbnail of the first page using the IcePDF API, then rescale it 
				 */
				Image thumbnail = pdf.getPageImage(0, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, 0.0f, 1).getScaledInstance((int) newWidth, 200, Image.SCALE_DEFAULT);
				
				/*
				 * Assign the thumbnail to the button as an icon
				 */
				panel.getButton().setIcon(new ImageIcon(thumbnail));
				
				/*
				 * It would be more efficient to repaint everything at the end, but it looks really neat to see
				 * each button turn into a thumbnail...
				 */
				bookshelfPanel.repaint();
			} catch (PDFException e) {
				e.printStackTrace();
			} catch (PDFSecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pdf.dispose();
			}
		}
	}
}
