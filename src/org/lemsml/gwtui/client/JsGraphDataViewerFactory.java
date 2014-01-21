package org.lemsml.gwtui.client;

import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;

import com.google.gwt.user.client.ui.FlowPanel;

public class JsGraphDataViewerFactory extends DataViewerFactory {

	FlowPanel container;
	
	public JsGraphDataViewerFactory(FlowPanel ctr) {
		container = ctr;
	}
	
	
	@Override
	public DataViewer newDataViewer(String s) {
		FlowPanel gpan = new FlowPanel();
		gpan.setHeight("350px");
		container.add(gpan);
		JsGraphDataViewer ret = new JsGraphDataViewer(gpan, s);
		return ret;
	}
	
}
