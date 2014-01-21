package org.lemsml.gwtui.client;

import org.lemsml.gwtui.shared.comm.FileFetchException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface LemsService extends RemoteService {

	String serverInfo(String name) throws IllegalArgumentException;

	String fetchModel(String fnm) throws FileFetchException;

	String[] fetchModelList();
}
