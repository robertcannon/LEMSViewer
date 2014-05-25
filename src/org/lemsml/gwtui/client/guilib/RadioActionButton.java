package org.lemsml.gwtui.client.guilib;

import org.lemsml.gwtui.client.event.Bundle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

 

// don't want that ugly inner class business for buttons.
// This just hides the handler stuff and gets us a clickAction call in the 
// main class saying which button it was 

public class RadioActionButton implements ClickHandler {

 	
	UserAction action;
	CommandHandler handler; 
	
	FlowPanel panel; 
	
	Bundle payload = null;

	
	boolean useButtons = false;
	
	
	 
	public RadioActionButton(String gnm, UserAction ua, CommandHandler ch, String lbl, boolean bon) {
		action = ua;
		handler = ch;
		
		panel = new FlowPanel();
		RadioButton rb = new RadioButton(gnm, lbl);
		panel.add(rb);

		if (bon) {
			rb.setValue(true);
		}
		rb.addClickHandler(this);
	}

	 
	  
	public Widget getWidget() {
	 
		return panel;
	}

	 

	public void onClick(ClickEvent event) {
		if (handler != null) {
			handler.doCommand(action, panel, payload);
			
		} 
	}

	  

	  
	
	
}
