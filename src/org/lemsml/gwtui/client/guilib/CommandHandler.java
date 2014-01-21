package org.lemsml.gwtui.client.guilib;

import org.lemsml.gwtui.client.event.Bundle;

import com.google.gwt.user.client.ui.Widget;
 

public interface CommandHandler {

	public void doCommand(UserAction ma, Widget w, Bundle payload);
	
}
