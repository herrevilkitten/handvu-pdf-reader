package cs6456.project.pdf;

import javax.swing.JToolBar;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class GesturesPdfViewBuilder extends SwingViewBuilder {

	public GesturesPdfViewBuilder(SwingController c) {
		super(c);
	}

	@Override
	public JToolBar buildCompleteToolBar(boolean embeddableComponent) {
		return super.buildCompleteToolBar(embeddableComponent);
//		return null;
	}
	
	

}
