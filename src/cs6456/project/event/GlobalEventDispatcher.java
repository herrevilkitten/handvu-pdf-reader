package cs6456.project.event;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/*
 * GlobalEventDispatcher determines which tab will accept the dispatched events and passes the event along
 */
public class GlobalEventDispatcher implements EventDispatcher {
	JTabbedPane pane = null;

	public GlobalEventDispatcher(JTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public boolean dispatchEvent(EventObject event) {
		Component component = pane.getSelectedComponent();
		if (component != null) {
			if ( component instanceof JScrollPane ) {
				component = ((JScrollPane) component).getViewport().getView();
			}

			if (component instanceof EventDispatcher) {
				return ((EventDispatcher) component).dispatchEvent(event);
			}
		}
		return false;
	}
}
