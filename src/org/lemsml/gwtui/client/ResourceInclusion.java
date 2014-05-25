package org.lemsml.gwtui.client;

public class ResourceInclusion implements ModelFileUser {

	ResourceInclusionLoader parent;
	int offset;
	String fileName;
	String myText = "";
	
	
	public ResourceInclusion(ResourceInclusionLoader ril, int inext, String fnm) {
		parent = ril;
		offset = inext;
		fileName = fnm;
	}

	public String getFilename() {
		 return fileName;
	}
	
	
	public void startLoad() {
		ResourceInclusionLoader loader = parent.newRelativeLoader(fileName, this);
		loader.startLoad();
	}


	@Override
	public void gotFile(String stxt) {
		myText = stxt;
		parent.inclusionReady();
	}

	
	public String getText() {
		return myText;
	}
	
	 


	public int getOffset() {
		return offset;
	}
	
	
	
	
}
