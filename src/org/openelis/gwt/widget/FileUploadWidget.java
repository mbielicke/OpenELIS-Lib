package org.openelis.gwt.widget;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FileUploadWidget extends Composite implements HasField<ArrayList<String>> {
	
	protected ArrayList<String> files;
	protected FTPMultiUploader defaultUploader; 
	protected boolean enabled;
	protected HorizontalPanel hp = new HorizontalPanel();
	protected TextBox text = new TextBox();
	protected AppButton load = new AppButton();
	protected String service;
	
	
	public FileUploadWidget(final String service) {
		this.service = service;
		defaultUploader = new FTPMultiUploader(new FTPFileUpload(service));
		VerticalPanel vp = new VerticalPanel();
		initWidget(vp);
		vp.add(defaultUploader);
		defaultUploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {
			public void onFinish(IUploader uploader) {
				if(files == null)
					files = new ArrayList<String>();
				files.add(uploader.getFileName());
			}
		});
	}

	public void addException(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<ArrayList<String>> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkValue() {
		// TODO Auto-generated method stub
	}

	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		//if(enabled){
			//setWidget(defaultUploader);
		//}else
			//setWidget(new Label("Not Enabled"));
	}

	public ArrayList<Exception> getExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field<ArrayList<String>> getField() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getFieldValue() {
		return files;
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		// TODO Auto-generated method stub
		
	}

	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public void setField(Field<ArrayList<String>> field) {
		// TODO Auto-generated method stub
		
	}

	public void setFieldValue(ArrayList<String> value) {
		// TODO Auto-generated method stub
		
	}

	public void setQueryMode(boolean query) {
		// TODO Auto-generated method stub
		
	}
	
	

}
