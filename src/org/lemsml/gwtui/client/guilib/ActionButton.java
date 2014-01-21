package org.lemsml.gwtui.client.guilib;

import org.lemsml.gwtui.client.customimages.ButtonMap;
import org.lemsml.gwtui.client.event.Bundle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
 
 

// don't want that ugly inner class business for buttons.
// This just hides the handler stuff and gets us a clickAction call in the 
// main class saying which button it was 

public class ActionButton implements ClickHandler {

	static ButtonMap buttonMap;
	
	UserAction action;
	CommandHandler handler; 
	
	ImgTextButton imgButton;
	
	Button button;
 	
	Bundle payload = null;

	
	boolean useButtons = true;
	
	public ActionButton(UserAction ua, CommandHandler ch) {
		this(ua, ch, null, null);
	}
	
	public ActionButton(UserAction ua, CommandHandler ch, String lbl) {
		this(ua, ch, lbl, null);
	}
	
	public ActionButton(UserAction ua, CommandHandler ch, Bundle b) {
		this(ua, ch, null, b);
	}
		
	public ActionButton(UserAction ua, CommandHandler ch, String lbl, Bundle b) {
		boolean isImg = false;
		payload = b;
		
		if (useButtons && buttonMap != null) {
			ImageResource ir = buttonMap.getImage(ua.getAction());
			if (ir != null) {
				
				// Image img = new Image(ir);
				String rlbl = (lbl != null ? lbl : ua.label);

				imgButton = new ImgTextButton(ir, rlbl);

				 
				imgButton.addClickHandler(this);
				 
				isImg = true;
			}
		}
		if (!isImg) {
			button = new Button(ua.label);
			button.addClickHandler(this);
		}	
		action = ua;
		handler = ch;
	}

 
	
	 
	
	
	public Widget getWidget() {
		Widget ret = null;
		if (imgButton != null) {
			ret = imgButton;
			
		} else if (button != null) {
			ret = button;
		}
		return ret;
	}

	 

	public void onClick(ClickEvent event) {
		if (handler != null) {
			Widget w = null;
			if (button != null) {
				 w = button;
			} else if (imgButton != null) {
				w = imgButton;
			}
			handler.doCommand(action, w, payload);
			
		} 
	}

	public void addStyleName(String str) {
		if (button != null) {
			button.addStyleName(str);
		}
		if (imgButton != null) {
			imgButton.addStyleName(str);
		}
	}

	public void setEnabled(boolean b) {
		if (button != null) {
			button.setEnabled(b);
		} 
		if (imgButton != null) {
			imgButton.setEnabled(b);
		}
	}

	public void setText(String str) {
		 if (button != null) {
			 button.setText(str);
		 }
		 if (imgButton != null) {
			 imgButton.setText(str);
		 }
	}

	public void setVisible(boolean b) {
		if (button != null) {
			button.setVisible(b);
		}
		if (imgButton != null) {
			imgButton.setVisible(b);
		}
	}

	public static void setButtonMap(ButtonMap bm) {
		buttonMap = bm;
	}

	 

	public void setAble(boolean b) {
		setEnabled(b);
	}

	 
	
	
}
