package org.lemsml.gwtui.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.lemsml.gwtui.client.LemsService;
import org.lemsml.gwtui.shared.comm.FileFetchException;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.io.util.FileUtil;
 


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LemsServiceImpl extends RemoteServiceServlet implements
		LemsService {

	File froot = new File("/home/padraig/jLEMSDev/examples");
    
    //File froot = new File("/home/padraig/lemspaper/tidyExamples/");
	
	public String serverInfo(String input) throws IllegalArgumentException {
	 	String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
 
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

 

	@Override
	public String fetchModel(String fnm) throws FileFetchException {
		File fm = new File(froot, fnm);
		
		E.info("Reading file " + fm.getAbsolutePath());
		
		String stxt = "";
		
		try {
			FileInclusionReader fir = new FileInclusionReader(fm);
			stxt = fir.read();
		
		} catch (ContentError ce) {
			E.info("Content error - " + ce);
			throw new FileFetchException("Cant read file " + fnm + " " + ce);
		}
		 
		
		return stxt;
		 
	}



	@Override
	public String[] fetchModelList() {
		ArrayList<String> wk = new ArrayList<String>();
		File[] fl = froot.listFiles();
		for (File f : fl) {
			if (f.getName().endsWith(".xml")) {
				try {
					String s = FileUtil.readStringFromFile(f);
					if (s.indexOf("<Simulation") > 0) {
						wk.add(f.getName());
					}
				} catch (IOException ex) {
					E.error("Couldn't read " + f + " " + ex);
				}
			}
		}
		Collections.sort(wk);
		String[] ret = wk.toArray(new String[wk.size()]);
		return ret;
	}
}
