package cs6456.project.ui.pdf;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/*
 * Extend SwingViewBuilder
 * This class returns "null" for a number of methods, in order to prevent these UI elements from
 * being rendered.
 */
public class GesturesPdfViewBuilder extends SwingViewBuilder {

	public GesturesPdfViewBuilder(SwingController c) {
		super(c);
	}

	@Override
	public JToolBar buildCompleteToolBar(boolean embeddableComponent) {
		return null;
	}

	@Override
	public JPanel buildStatusPanel() {
		return null;
	}
	
}
