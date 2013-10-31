package cs6456.project.event;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class GlobalEventDispatcher implements EventDispatcher {
	JTabbedPane pane = null;

	public GlobalEventDispatcher(JTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public boolean dispatchEvent(EventObject event) {
		Component component = pane.getSelectedComponent();
		System.err.println("Dispatching " + event.getClass().getSimpleName());
		if (component != null) {
			if ( component instanceof JScrollPane ) {
				component = ((JScrollPane) component).getViewport().getView();
			}

			if (component instanceof EventDispatcher) {
				System.err.println("... to " + component.getClass().getSimpleName());
				return ((EventDispatcher) component).dispatchEvent(event);
			}
		}
		return false;
	}
}
