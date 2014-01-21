package org.lemsml.gwtui.client;
 

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
 
public class LEMSViewer implements EntryPoint {
	 
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// proxy to talk to the server
	private final LemsServiceAsync lemsService = GWT.create(LemsService.class);

	Resources resources; 
	
	
	public void onModuleLoad() {
		conslog("Loading viewer....");
		
	 	
		resources = GWT.create(Resources.class);

		//customImages = GWT.create(CustomImages.class);
		resources.css().ensureInjected();
		
		
		RootPanel mainContainer = RootPanel.get("testmain");
		
		// CheckRunner cr = new CheckRunner(this);
		// cr.displayIn(mainContainer);
		
		JLEMSRunner jlr = new JLEMSRunner(this);
		jlr.buildPage();
	 
	 
	}	

	 
	
	private native void conslog(String str) /*-{
		console.log(str);
	 }-*/;
	
	
 
	
	public void fetchModel(String fnm, BrowserMessageHandler bmh, ModelFileUser mfu) {
		final BrowserMessageHandler mh = bmh;
		final ModelFileUser mfUser = mfu;
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				reportFailure(mh, caught, "Could not get file");
			}
			
			public void onSuccess(String stxt) {
				mfUser.gotFile(stxt);
			}
		};
			
		GWT.log("About to fetch " + fnm);
		lemsService.fetchModel(fnm, callback);
	}
	

	private void reportFailure(BrowserMessageHandler bmh, Throwable caught, String str) {
		// TODO Auto-generated method stub
		bmh.msg("Server operation failed: " + str + " ");
	}



	public void getExampleFiles( BrowserMessageHandler bmh, ModelFileUser mfu) {
		final BrowserMessageHandler mh = bmh;
		final ModelFileUser mfUser = mfu;
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
				reportFailure(mh, caught, "Could not get file list");
			}
			
			public void onSuccess(String[] stxt) {
				mfUser.gotFileList(stxt);
			}
		};
		
		lemsService.fetchModelList(callback);
		
	}
}
