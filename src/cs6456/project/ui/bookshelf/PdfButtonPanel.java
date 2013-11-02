package cs6456.project.ui.bookshelf;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;

import cs6456.project.ui.BookshelfPanel;
import cs6456.project.ui.PdfButton;
import cs6456.project.util.ImageUtils;

public class PdfButtonPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String filename;
	PdfButton button;
	JLabel label;
	SwingController controller;

	public PdfButtonPanel(final String filename, final SwingController controller) {
		this.setLayout(new BorderLayout());
		this.filename = filename;
		this.button = new PdfButton("");
		this.controller = controller;

		Image adobeImage = ImageUtils.getImage("/Adobe_Acrobat_Icon.jpg");
		Icon icon = new ImageIcon(adobeImage.getScaledInstance(200, 200, Image.SCALE_DEFAULT));

		button.addActionListener(this);
		button.setIcon(icon);
		button.setToolTipText(filename);
		add(button, BorderLayout.CENTER);

		this.label = new JLabel(filename);
		add(label, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.openDocument("bookshelf/" + filename);
		((BookshelfPanel) getParent()).setCurrentButton(this);
		repaint();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public PdfButton getButton() {
		return button;
	}

	public void setButton(PdfButton button) {
		this.button = button;
	}

	public SwingController getController() {
		return controller;
	}

	public void setController(SwingController controller) {
		this.controller = controller;
	}

}
