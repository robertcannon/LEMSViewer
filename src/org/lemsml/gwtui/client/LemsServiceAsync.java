package org.lemsml.gwtui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
 
public interface LemsServiceAsync {
	void serverInfo(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void fetchModel(String fnm, AsyncCallback<String> callback);

	void fetchModelList(AsyncCallback<String[]> callback);
}
