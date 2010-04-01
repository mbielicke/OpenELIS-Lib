package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class FileUpload extends Composite {
	
	private com.google.gwt.user.client.ui.FileUpload fu = new com.google.gwt.user.client.ui.FileUpload();
	private FormPanel fp = new FormPanel();
	
	public FileUpload() {
		initWidget(fp);
		fp.add(fu);
		fu.setName("fu");
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setMethod(FormPanel.METHOD_POST);
      
	}
	
	public void setAction(String url) {
		fp.setAction(url);
	}
	
	public void submit() {
		fp.submit();
	}
	
	public void addSubmitHandler(SubmitHandler handler) {
		fp.addSubmitHandler(handler);
	}
	
	public void addSubmitCompleteHandler(SubmitCompleteHandler handler) {
		fp.addSubmitCompleteHandler(handler);
	}

}
