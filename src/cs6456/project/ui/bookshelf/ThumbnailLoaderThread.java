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

public class ThumbnailLoaderThread extends Thread {
	
	JPanel bookshelfPanel;
	
	public ThumbnailLoaderThread(JPanel bookshelfPanel) {
		super("Thumbnail Loader");
		setDaemon(true);
		
		this.bookshelfPanel = bookshelfPanel;
	}
	
	public void run() {
		for ( Component c : bookshelfPanel.getComponents() ) {
			if ( !(c instanceof PdfButtonPanel) ) {
				continue;
			}
			PdfButtonPanel panel = (PdfButtonPanel) c;
			String filename = panel.getFilename();

			System.err.println("Loading thumbnail for bookshelf/" + filename);
			Document pdf = new Document();
			try {
				pdf.setFile("bookshelf/" + filename);
				PDimension dimensions = pdf.getPageDimension(0, 0.0f);
				float ratio = dimensions.getWidth() / dimensions.getHeight();
				float newWidth = 200 * ratio;
				Image thumbnail = pdf.getPageImage(0, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, 0.0f, 1).getScaledInstance((int) newWidth, 200, Image.SCALE_DEFAULT);
				panel.getButton().setIcon(new ImageIcon(thumbnail));
				bookshelfPanel.repaint();
				pdf.dispose();
			} catch (PDFException e) {
				e.printStackTrace();
			} catch (PDFSecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
