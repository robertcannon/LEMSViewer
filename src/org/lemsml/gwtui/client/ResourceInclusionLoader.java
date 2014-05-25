package org.lemsml.gwtui.client;

import java.util.ArrayList;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;

public class ResourceInclusionLoader implements ModelFileUser {

	LEMSViewer lemsServer;
	BrowserMessageHandler logger;
	String mainURL;
	ModelFileUser mfUser;
	 	
	String mainText;
	HashSet<String> requestSet;
	
	
	ArrayList<ResourceInclusion> inclusions = new ArrayList<ResourceInclusion>();
	int nLoaded = 0;
	
	
	public ResourceInclusionLoader(LEMSViewer lv, BrowserMessageHandler bmh, String stxt, ModelFileUser mfu) {
		lemsServer = lv;
		logger = bmh;
		mainURL = stxt;
		mfUser = mfu;
		requestSet = new HashSet<String>();
	}
	
	
	public ResourceInclusionLoader(String stxt, ModelFileUser mfu) {
		mainURL = stxt;
		mfUser = mfu;
	}
	
	
	public ResourceInclusionLoader newRelativeLoader(String fnm, ModelFileUser mfu) {
		requestSet.add(fnm);
		
		String url = siblingURL(fnm);
		
		ResourceInclusionLoader ret = new ResourceInclusionLoader(url, mfu);
		ret.lemsServer = lemsServer;
		ret.logger = logger;
		ret.requestSet = requestSet;
		return ret;
	}
	
	
	
	private String siblingURL(String fnm) {
		int ils = mainURL.lastIndexOf("/");
		String ret = mainURL.substring(0, ils) + "/" + fnm;
		return ret;
	}
	
	
	
	public void startLoad() {
		logger.msg("Requesting " + mainURL);
		lemsServer.loadURL(mainURL, this);
	}

	@Override
	public void gotFile(String stxt) {
		  int inext = 0;
		  mainText = stxt;
		  
		  while (true) {
			  inext = stxt.indexOf("<Include ", inext);
			  if (inext > 0) {
				  int icb = stxt.indexOf("/>", inext);
				  String part = stxt.substring(inext + 8, icb);
				  String fnm = extractFilename(part);
				  ResourceInclusion ri = new ResourceInclusion(this, inext, fnm);
				  inclusions.add(ri);
				  
				  logger.msg("Need an include file from " + inext + " to " + icb + " for " + fnm); 
				   
				  inext = icb;
			  } else {
				  break;
			  }
		  }
		  
		  if (inclusions.size() == 0) {
			  // no inclusions, we're ready now
			  mfUser.gotFile(stxt);
		  } else {
			  
			  for (ResourceInclusion ri : inclusions) {
				  if (requestSet.contains(ri.getFilename())) {
					  // no need to load it 
					  logger.msg("NOT loading " + ri.getFilename() + " - already requested");
					  inclusionReady();
					  
				  } else {
					  ri.startLoad();
				  }
			  }
		  }
	}
	
	public void inclusionReady() {
		nLoaded += 1;
		logger.msg("Got an inclusion " + nLoaded);
		if (nLoaded == inclusions.size()) {
			finalizeInclusions();
		}
	}
	
	
	private void finalizeInclusions() {
		for (int i = inclusions.size() - 1; i >= 0; i--) {
			ResourceInclusion ri = inclusions.get(i);
			
			int ioff = ri.getOffset();
			int iend = mainText.indexOf("/>", ioff);
			logger.msg("Replacing section " + ioff + " to " + iend);
			mainText = mainText.substring(0, ioff) + extractInnerText(ri.getText()) + mainText.substring(iend + 2, mainText.length());
		}
		
		logger.msg("All inclusions inserted");
		mfUser.gotFile(mainText);
	}
	
	
	private String extractInnerText(String src) {
		String ret = src.trim();
		int ia = ret.indexOf("<Lems>");
		if (ia >= 0) {
			ret = ret.substring(ia+6, ret.length());
		}
		if (ret.endsWith("</Lems>")) {
			ret = ret.substring(0, ret.length() - 7);
		}
		return ret;
 	}
	
	
	private String extractFilename(String src) {
		String s = src.trim();
		String[] bits = s.split("=");
		String ret = null;
		if (bits.length > 1) {
			String qfnm = bits[1].trim();
			ret = qfnm.substring(1, qfnm.length() - 1);
			
		} else {
			E.warning("Cant extract file name from " + src);
		}
		return ret;
	}
	

	 
	
}
