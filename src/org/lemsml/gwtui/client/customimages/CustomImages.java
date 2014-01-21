package org.lemsml.gwtui.client.customimages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface CustomImages extends ClientBundle {
	public static final CustomImages INSTANCE =  GWT.create(CustomImages.class);

	@Source("logo.png")
	public ImageResource logo();

	@Source("Login.png")
	public ImageResource login();
	
	@Source("Logout.png")
	public ImageResource logout();

	@Source("Back.png")
	public ImageResource back();
	
	@Source("NewProject.png")
	public ImageResource add();
	
	@Source("ExcelExport.png")
	public ImageResource excelExport();
		
	@Source("Cancel.png")
	public ImageResource cancel();
	
	@Source("Comment.png")
	public ImageResource comment();
	
	@Source("Download.png")
	public ImageResource download();
	
	@Source("Edit.png")
	public ImageResource edit();
	
	@Source("Feedback.png")
	public ImageResource feedback();
	
	@Source("Film.png")
	public ImageResource film();
	
	@Source("Filter.png")
	public ImageResource filter();
	
	@Source("Define.png")
	public ImageResource define();
	
	@Source("Go.png")
	public ImageResource go();
	
	@Source("Help.png")
	public ImageResource help();
	
	@Source("Next.png")
	public ImageResource next();
	
	@Source("OK.png")
	public ImageResource ok();
	
	@Source("Previous.png")
	public ImageResource previous();
	
	@Source("Save.png")
	public ImageResource save();
	
	@Source("Submit.png")
	public ImageResource submit();
	
	@Source("Upload.png")
	public ImageResource upload();
	
	@Source("View.png")
	public ImageResource view();
	
	@Source("Review.png")
	public ImageResource review();

	@Source("PDF.png")
	public ImageResource pdfIcon();
	
 
}
