package org.lemsml.gwtui.client;
 

import org.lemsml.jlems.core.logging.MessageHandler;
import org.lemsml.jlems.core.logging.MessageType;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class BrowserMessageHandler implements MessageHandler {

	
	FlowPanel destPanel;
	
	String wkText = "";
	
	
	public BrowserMessageHandler(FlowPanel fp) {
		destPanel = fp;
	}

	
	public void clear() {
		destPanel.clear();
		 
	}


	@Override
	public void msg(MessageType type, String txt) {
	
	
	String fmsg = " (" + type.name() + ") " + txt;
		
	if (type == MessageType.ERROR || 
		type == MessageType.COREERROR || 
		type == MessageType.FATAL) {

		showText(fmsg);
		
	} else if (type == MessageType.WARNING) {
		
		showText(fmsg);
		
	} else {
		showText(fmsg);
	}
	 
}

	

public void msg(final String txt) {
	msg(MessageType.LOG, txt);
}

	
	
	private void showText(String fmsg) {
		Label lbl = new Label(fmsg);
		destPanel.add(lbl);
	}



	
}
