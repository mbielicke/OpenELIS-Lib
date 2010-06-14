package org.openelis.gwt.widget.fileupload;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FileUploadWidget extends Composite implements HasValue<ArrayList<String>> {
	
	protected ArrayList<String> files;
	protected FTPMultiUploader defaultUploader; 
	protected boolean enabled;
	protected HorizontalPanel hp = new HorizontalPanel();
	protected TextBox text = new TextBox();
	protected Button load = new Button();
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

	public void addException(LocalizedException exception) {
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

	public ArrayList<LocalizedException> getExceptions() {
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


    public ArrayList<String> getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(ArrayList<String> value) {
        // TODO Auto-generated method stub
        
    }

    public void setValue(ArrayList<String> value, boolean fireEvents) {
        // TODO Auto-generated method stub
        
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ArrayList<String>> handler) {
        // TODO Auto-generated method stub
        return null;
    }
	
	

}
