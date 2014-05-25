package org.lemsml.gwtui.client.guilib;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;

public class DialogResizer implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {
	
	Image ctrlImage;
	
	UIObject panel;
 	
	int wdown;
	int hdown;
	
	int xdown;
	int ydown;
	
	HandlerRegistration  moveReg;
	HandlerRegistration  upReg;
	
	BaseDialog dlg;
	
	public DialogResizer(BaseDialog ad, Image img, UIObject pan, int w0, int h0) {
		dlg = ad;
		ctrlImage = img;
		panel = pan;
	
		panel.setWidth(w0 + "px");
		panel.setHeight(h0 + "px");
	
		ctrlImage.addMouseDownHandler(this);
	}

 
	
	
	
	@Override
	public void onMouseDown(MouseDownEvent event) {
		xdown = event.getClientX();
		ydown = event.getClientY();
		
		wdown = getWidth();
		hdown = getHeight();
		
		event.preventDefault();
		
		RootPanel rp = RootPanel.get();
		moveReg = rp.addDomHandler(this, MouseMoveEvent.getType());
		upReg = rp.addDomHandler(this, MouseUpEvent.getType());
	}


	private int getWidth() {
		int ret = 0;
		if (panel != null) {
			ret = panel.getOffsetWidth();
		}  
		return ret;
	}
	
	private int getHeight() {
		int ret = 0;
		if (panel != null) {
			ret = panel.getOffsetHeight();
		} 
		return ret;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		 moveReg.removeHandler();
		 upReg.removeHandler();
		 
		 dlg.resized();
	}



	@Override
	public void onMouseMove(MouseMoveEvent event) {
		 int xcur = event.getClientX();
		 int ycur = event.getClientY();
		 applyDims(wdown + xcur - xdown, hdown + ycur - ydown);
	}
	
	
	private void applyDims(int w, int h) {
		int wa = (w < 50 ? 50 : w);
		int ha = (h < 50 ? 50 : h);
		if (panel != null) {
			panel.setWidth(wa + "px");
			panel.setHeight(ha + "px");
		} 
	}
	
}
