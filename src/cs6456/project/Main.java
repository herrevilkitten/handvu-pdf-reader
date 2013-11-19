package cs6456.project;

import javax.swing.SwingUtilities;

import cs6456.project.ui.GesturesFrame;

public class Main {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new GesturesFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
