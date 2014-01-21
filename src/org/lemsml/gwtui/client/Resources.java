package org.lemsml.gwtui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE =  GWT.create(Resources.class);

	@Source("index.css") 
	@CssResource.NotStrict
	public CssResource css();

 
	/*
	@Source("config.xml")
	public TextResource initialConfiguration();
	
	@Source("manual.pdf")
	public DataResource ownersManual();
	*/
}
 
