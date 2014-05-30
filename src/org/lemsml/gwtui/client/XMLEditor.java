package org.lemsml.gwtui.client;

import org.lemsml.jlems.core.logging.E;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class XMLEditor {

	FlowPanel contentPanel;
	ScrollPanel vsp;
	
	HTML contentLabel;
	
	
	public XMLEditor() {
		vsp = new ScrollPanel();
		
		contentPanel = new FlowPanel();
		contentPanel.addStyleName("xmleditor");
		contentLabel = new HTML("");
		contentLabel.addStyleName("xmlcontent");
		contentLabel.getElement().setAttribute("contenteditable", "true");
		
		contentPanel.add(contentLabel);
		vsp.add(contentPanel);
	}
	
	public Panel getPanel() {
		return vsp;
	}

	public void setHeight(int h) {
		vsp.setHeight(h + "px");
	}

	public void setText(String stxt) {
		contentLabel.setText(stxt);
		contentLabel.addStyleName("bordered");
		 
	}

	public String getText() {
		String ret = contentLabel.getText();
		return ret;
	}
	
}
