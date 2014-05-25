package org.lemsml.gwtui.client.guilib;

import org.lemsml.gwtui.client.event.Bundle;
import org.lemsml.gwtui.client.images.CoreImages;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
 

public abstract class BaseDialog extends DialogBox implements ResponseListener {	
	
	private HorizontalPanel captionPanel = new HorizontalPanel();

	protected VerticalPanel mainPanel;
	private VerticalPanel contentPanel;

	HorizontalPanel buttonPanel;
	
	UserAction uaApply = new UserAction("OK", Action.APPLY);
	UserAction uaCancel = new UserAction("Cancel", Action.CANCEL);
	
 
	ActionButton bCancel = null;
	ActionButton bApply = null;

	private Label bclose;  

	
	DialogResizer dialogResizer;
	
 
	
	
	public BaseDialog() {
		super();
	}
 
	public BaseDialog(String title, String scancel) {
		this(title, scancel, null);
	}

	
	public BaseDialog(String title, String scancel, String sapply) {
		super();
 
		buildPanels(scancel, sapply);

		
		
		
		add(mainPanel);

		setHTML(title);
		setPopupPosition(200, 200);
	}

	
	public void showCentered() {
		center();
	}
	
	
	public void setOKAble(boolean b) {
		bApply.setEnabled(b);
	}
	
	
  
	
	public final void showGlassCentered() {
		setGlassEnabled(true);
		center();
	}
	
	
	
	
	public final void showDialog(Widget wrel) {
		if (wrel != null) {
			showRelativeTo(wrel);
		} else {
			center();
		}
	}
	
	
	public void setDialogTitle(String ttl) {
		setHTML(ttl);
	}
	
	protected void buildPanels(String scancel, String sapply) {
		mainPanel = new VerticalPanel();
		
		FlowPanel fptop = new FlowPanel();
		mainPanel.add(fptop);
		
		bclose = new Label("X");
		bclose.addStyleName("dialogcloser");
		add(bclose);
		bclose.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				closeDialog();
			}
	
		});
		fptop.add(bclose);
		
		
		contentPanel = new VerticalPanel();
		
		mainPanel.add(contentPanel);

		setModal(false);

	    buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		buttonPanel.addStyleName("dialogbuttons");

		mainPanel.add(buttonPanel);
		mainPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		
		CommandHandler ch = new CommandHandler() {
			public void doCommand(UserAction ma, Widget w, Bundle payload) {
				ownCommand(ma, w, payload);
			}
		};
		
		if (scancel != null) {	
			uaCancel.setLabel(scancel);
			bCancel = new ActionButton(uaCancel, ch, scancel);
			buttonPanel.add(bCancel.getWidget());
		}

		if (sapply != null) {
			bApply = new ActionButton(uaApply, ch, sapply);
			buttonPanel.add(bApply.getWidget());
		}
	}

	public void setApplyButtonText(String txt) {
		bApply.setText(txt);
	}

	public void closeDialog() {
		hide();
	}

	public void bodyAdd(Panel p) {
		contentPanel.add(p);
	}

	public void bodyClear() {
		contentPanel.clear();
	}

	/*
	@Override
	public void setHTML(String html) {
		mySetCaption(html);
	}

	@Override
	public void setHTML(SafeHtml html) {
		mySetCaption(html.asString());
	}
*/

	public Widget getTitleWidget() {
		return captionPanel;
	}
	
	
	private void mySetCaption(String txt) {
		captionPanel.clear();
		captionPanel.setWidth("100%");
		captionPanel.add(new Label(txt));
		captionPanel.add(bclose);
		captionPanel.setCellHorizontalAlignment(bclose, HasHorizontalAlignment.ALIGN_RIGHT);

		 
		captionPanel.setCellWidth(bclose, "4%");
		captionPanel.addStyleName("Caption");
		

		// Get the cell element that holds the caption
		Element td = getCellElement(0, 1);

		// Remove the old caption
		td.setInnerHTML("");
		
		while (td.getChildCount() > 0) {
			td.removeChild(td.getFirstChild());
		}
		
		// append our horizontal panel
		td.appendChild(captionPanel.getElement());
	}

	
	
	// @Override
	public void onBrowserEvent(Event event) {
		try {
		EventTarget target = event.getEventTarget();

		if (Element.is(target) && bclose.getElement().isOrHasChild(Element.as(target))) {

			int iev = event.getTypeInt();
			if (iev == Event.ONMOUSEUP || iev == Event.ONCLICK) {
				closeDialog();

			}
		}
		} catch (Exception ex) {
			Window.alert("Exception on browser event " + event);
		}
		super.onBrowserEvent(event);
	}
	
	
	

	public void ownCommand(UserAction ua, Widget w, Bundle payload) {
		if (ua == uaCancel) {
			doCancel();
			
		} else if (ua == uaApply) {
			doApply(w);
		}
	}

	public abstract void doApply(Widget w);

	public void localCancel() {
		
	}
	
	public void doCancel() {
		localCancel();
		hide();
	}

	
	public void setResizable(UIObject pan, int w0, int h0) {
		CoreImages ci = CoreImages.INSTANCE;
		Image ibe = new Image(ci.boxExtender());
		buttonPanel.add(ibe);
		dialogResizer = new DialogResizer(this, ibe, pan, w0, h0);
	}
	
 
	
	
	
	public void resized() {
		
	}


	@Override
	public void gotSaveResponse(SaveResponse sr) {
		if (sr.isOK()) {
			closeDialog();
		} else {
			Window.alert(sr.message);
		}
	}
	
	
}
