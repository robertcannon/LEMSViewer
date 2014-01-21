package org.lemsml.gwtui.shared.comm;

import java.io.Serializable;

public class FileFetchException extends Exception implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public String myMessage;
	
	
	public FileFetchException() {
		
	}
	
	public FileFetchException(String s, Exception ex) {
		super(s, ex);
		myMessage = s;
	}
	
	public FileFetchException(String s) {
		super(s);
		myMessage = s;
	}

}
