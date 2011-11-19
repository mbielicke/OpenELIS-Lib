package org.openelis.gwt.widget.fileupload;


import java.util.Random;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

public class FileLoad extends Composite {
	
	protected FileLoadButton upload;
	protected FormPanel form;
	protected AbsolutePanel panel;
    protected Hidden service,method;
	
	public FileLoad() {
		form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		upload = new FileLoadButton();
		upload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				form.submit();
			}
		});
		upload.setName("file");
		service = new Hidden("service");
		method = new Hidden("method");
		panel = new AbsolutePanel();
		panel.add(upload);
		panel.add(service);
		panel.add(method);
		form.add(panel);
		initWidget(form);
	}
	
	public HandlerRegistration addSubmitHandler(SubmitHandler handler) {
		return form.addSubmitHandler(handler);
	}
	
	public HandlerRegistration addSubmitCompleteHandler(SubmitCompleteHandler handler) {
		return form.addSubmitCompleteHandler(handler);
	}
	
	public void setAction(String action) {
		form.setAction(action);
	}
	
	public void setService(String service) {
		this.service.setValue(service);
	}
	
	public void setMethod(String method) {
		this.method.setValue(method);
	}
	
	public void setWidget(Widget widget) {
		upload.setButton(widget);
	}

}
