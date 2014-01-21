package org.lemsml.gwtui.client;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class ReportingSplitLayoutPanel extends SplitLayoutPanel {

	SplitHandler handler;
	
	public ReportingSplitLayoutPanel(SplitHandler sh) {
		super();
		handler = sh;
	}
	
	
	@Override
	public void onResize() {
		super.onResize();
		handler.splitMoved();
	}
	
}
