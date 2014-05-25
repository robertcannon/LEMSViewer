package org.lemsml.gwtui.client;
 

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
 
public class LEMSViewer implements EntryPoint {
	 
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// proxy to talk to the server
	private final LemsServiceAsync lemsService = null;

	
	// lemsService = GWT.create(LemsService.class);

	
	public static final int SERVLET = 1;
	public static final int PHP = 2;
	
	public int serverMode = PHP;
	
	
	Resources resources; 
	
	BrowserMessageHandler logger = null;
	
	
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
	 
	 
		String sm = getInitialModelFromURL();
		if (sm != null) {
			jlr.displayLoadAndRun(sm);
		}
		
	}	

	
	private String getInitialModelFromURL() {
		String s = Window.Location.getParameter("u");
		return s;
	}
	
	
	 
	
	private native void conslog(String str) /*-{
		console.log(str);
	 }-*/;
	
	
	public void setLogger(BrowserMessageHandler bmh) {
		logger = bmh;
	}
	
	
	public void fetchModel(String fnm, ModelFileUser mfu) {
		if (serverMode == SERVLET) {
			servletFetchModel(fnm, mfu);
		} else {
			phpFetchModel("file", fnm, mfu);
		}
	}
	

 
	
	public void loadURL(String surl, ModelFileUser mfu) {
		phpFetchModel("url", surl, mfu);
	}


	
 

	

	private void phpFetchModel(String src, String fnm, ModelFileUser mfu) {
 		final ModelFileUser mfUser = mfu;
		String url = ""; 
		if (src.equals("url")) {
			url += "getexampleurl.php?url=" + URL.encode(fnm);
			
		} else if (src.equals("file")) {
			url += "getexamplefile.php?file=" + URL.encode(fnm);
			
		} else {
			 reportFailure(null, "ERROR. unrecognised data source");
			 return;
		}
	    
				
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

	        try {
	          Request request = builder.sendRequest(null, new RequestCallback() {
	            public void onError(Request request, Throwable exception) {
	                reportFailure(null, "ERROR. Could not connect to server.");
	            }

	            public void onResponseReceived(Request request, Response response) {
	              if (200 == response.getStatusCode()) {
	                  // call the method in main class to process the response
	            	  String txt = response.getText();
	            		mfUser.gotFile(txt);
	                
	              } else {
	                 reportFailure(null, "ERROR. Errorcode: " + response.getStatusCode());
	              }
	            }
	          });

	        } catch (RequestException e) {
	                reportFailure(null, "ERROR. No connection to server");
	        }
	}
	 
	
	

	public void servletFetchModel(String fnm, ModelFileUser mfu) {
 		final ModelFileUser mfUser = mfu;
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				reportFailure(caught, "Could not get file");
			}
			
			public void onSuccess(String stxt) {
				mfUser.gotFile(stxt);
			}
		};
			
		GWT.log("About to fetch " + fnm);
		lemsService.fetchModel(fnm, callback);
	}
	

	private void reportFailure(Throwable caught, String str) {
 		logger.msg("Server operation failed: " + str + " ");
	}


 
}
