package org.openelis.gwt.widget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class FileUpload extends Composite {
	
	private FormPanel fp = new FormPanel();
	private ArrayList<com.google.gwt.user.client.ui.FileUpload> files = new ArrayList<com.google.gwt.user.client.ui.FileUpload>();
	private VerticalPanel vp = new VerticalPanel();
	private boolean multiFile;
	
	public FileUpload() {
		initWidget(fp);
		fp.add(vp);
		addFile();
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
	
	public void setMultiFile(boolean multi) {
		multiFile = multi;
	}
	
	public void addFile() {
		if(files.size() == 0 || multiFile) {
			files.add(new com.google.gwt.user.client.ui.FileUpload());
			files.get(files.size()-1).setName("file"+(files.size()-1));
			vp.add(files.get(files.size()-1));
		}
	}

}
