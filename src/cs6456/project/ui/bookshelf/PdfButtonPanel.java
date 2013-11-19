package cs6456.project.ui.bookshelf;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs6456.project.ui.GesturesTabbedPane;
import cs6456.project.util.ImageUtils;

public class PdfButtonPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * A PdfButtonPanel is
	 * - A button with an icon
	 * - A label (the filename)
	 */
	String filename;
	PdfButton button;
	JLabel label;
	GesturesTabbedPane tabbedPane;
	int index;

	public PdfButtonPanel(final String filename, GesturesTabbedPane tabbedPane, int index) {
		this.setLayout(new BorderLayout());
		this.filename = filename;
		this.button = new PdfButton("", index);
		this.tabbedPane = tabbedPane;
		this.index = index;

		/*
		 * The default icon is a rescaled "Adobe Acrobat" icon
		 */
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
		/*
		 * When the button is "clicked", either with mouse, keyboard, or hover:
		 * - Open the PDF document in the Reader tab
		 * - Set the currently selected button to this button
		 * - Repaint
		 * - Switch tabs to the Reader tab
		 */
		tabbedPane.getPdfReaderPanel().openDocument("bookshelf/" + filename);
		((ScrollingBookshelfPanel) getParent()).setCurrentButton(this);
		repaint();
		tabbedPane.setSelectedIndex(1);
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

	public int getIndex() {
		return index;
	}
	
}
