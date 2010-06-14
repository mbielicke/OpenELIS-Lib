package org.openelis.gwt.widget.fileupload;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IFileInput;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

public class FTPMultiUploader extends MultiUploader {
	
	public FTPMultiUploader(IFileInput fileInput) {
		super(new BaseUploadStatus(), fileInput);
	}
	
	protected IUploader getUploaderInstance() {
		return new FTPUploader();
	}

}
