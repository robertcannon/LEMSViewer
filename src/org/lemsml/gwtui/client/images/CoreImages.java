package org.lemsml.gwtui.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface CoreImages extends ClientBundle {
	public static final CoreImages INSTANCE =  GWT.create(CoreImages.class);
 
	
	@Source("folder-icon.png")
	public ImageResource folderIcon();
	 
	@Source("zip-icon.png")
	public ImageResource zipIcon();
	 
	@Source("boxextender.png")
	public ImageResource boxExtender();

    @Source("rtarr.png")
    public ImageResource rightArrow();
	
    @Source("dnarr.png")
    public ImageResource downArrow();
  
    @Source("menuarrow.png")
    public ImageResource menuArrow();

    @Source("generic-doc.png")
	public ImageResource genericDocIcon();
    
     
    
}
