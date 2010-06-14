package org.openelis.gwt.widget.fileupload;

import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;

import org.openelis.gwt.common.FtpRPC;
import org.openelis.gwt.event.FTPStartUploadEvent;
import org.openelis.gwt.event.FTPStartUploadHandler;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

  public class FTPUploader extends OpenELISUploader implements FTPStartUploadHandler {
	  
	  protected String URL;
	  protected String service;
	  final ScreenService uploadservice = new ScreenService("controller?service=org.openelis.gwt.server.UploadServlet");
	  
	  protected final Timer FTPUploadTimer = new Timer() {
		   boolean firstTime = true;
		   public void run() {
		     if (isTheFirstInQueue()) {
		       this.cancel();
		       statusWidget.setStatus(IUploadStatus.Status.SUBMITING);
		       uploadFTP();
		     
		       
		     }
		     if (firstTime) {
		       addToQueue();
		       firstTime = false;
		       fileInput.setVisible(false);
		       statusWidget.setVisible(true);
		     }
		   }
		 };
		 
	@SuppressWarnings("unchecked")
	protected void uploadFTP() {
		
		final FtpRPC rpc = new FtpRPC();
		rpc.path = URL;
		rpc.service = ((FTPFileUpload)fileInput).getService();
		
			uploadservice.call("startFTPLoad",rpc, new AsyncCallback() {
				public void onSuccess(Object result) {
					
				}
				
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					finished = true;
					statusWidget.setError(caught.getMessage());
				}
			});

		
		  uploading = true;
	       finished = false;

           statusWidget.setStatus(IUploadStatus.Status.INPROGRESS);
	       new Timer() {
	           public void run() {
	        	 try {
	        		 parseAjaxResponse(uploadservice.callString("getFTPStatus",URL));
	        		 if(finished ){
	        			 this.cancel();
	        		 }
	        	 }catch(Exception e) {
	        		 e.printStackTrace();
	        	 }
	             lastData = now();
	           }
	         }.scheduleRepeating(200);
		
	}

	@Override
	public void setFileInput(IFileInput input) {
		super.setFileInput(input);
		if(input instanceof FTPFileUpload){
			((FTPFileUpload)input).addFTPStartUploadHandler(this);
		}
		autoSubmit = true;
	}
	
	public void onFTPStart(FTPStartUploadEvent event) {
		  URL = event.getURL();
		  basename = Utils.basename(event.getURL());
	      statusWidget.setFileName(basename);
	      if (autoSubmit && validateExtension(basename) && basename.length() > 0) {
	        if (avoidRepeatedFiles && fileDone.contains(fileInput.getFilename())) {
	          return;
	        } else {
	          FTPUploadTimer.scheduleRepeating(DEFAULT_AUTOUPLOAD_DELAY);
	        }
	      }
	} 
	
	@Override
	public String getFileName() {
		String name =  super.getFileName();
		if(name == null || name.equals(""))
			return basename;
		return name;
	}


  }