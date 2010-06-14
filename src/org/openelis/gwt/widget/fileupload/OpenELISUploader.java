/*
* Copyright 2009 Manuel Carrasco Moñino. (manuel_carrasco at users.sourceforge.net) 
* http://code.google.com/p/gwtupload
* 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/
package org.openelis.gwt.widget.fileupload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.HasJsData;
import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.IsUpdateable;
import gwtupload.client.UpdateTimer;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
* <p>
* Uploader panel.
* </p>
*         
* @author Manolo Carrasco Moñino
* 
*         <h3>Features</h3>
*         <ul>
*         <li>Renders a form with an input file for sending the file, and a hidden iframe where is received the server response</li>  
*         <li>The user can add more elements to the form</li>
*         <li>It asks the server for the upload progress continuously until the submit process has finished.</li>
*         <li>It expects xml responses instead of gwt-rpc, so the server part can be implemented in any language</li>
*         <li>It uses a progress interface so it is easy to use customized progress bars</li>
*         <li>By default it renders a basic progress bar</li>
*         <li>It can be configured to automatic submit after the user has selected the file</li>
*         <li>It uses a queue that avoid submit more than a file at the same time</li>
*         </ul>
* 
*         <h3>CSS Style Rules</h3>
*         <ul>
*         <li>.GWTUpld { Uploader container }</li>
*         <li>.GWTUpld .upld-input { style for the FileInput element }</li>
*         <li>.GWTUpld .upld-status { style for the IUploadStatus element }</li>
*         <li>.GWTUpld .upld-button { style for submit button if present }</li>
*         </ul>
*/
public class OpenELISUploader extends Composite implements IsUpdateable, IUploader, HasJsData {
 
 /**
  * FormPanel's add method only can be called once
  * This class override the add method to allow multiple additions
  * to a flowPanel.
  */
 public class FormFlowPanel extends FormPanel {
   FlowPanel formElements = new FlowPanel();
   public FormFlowPanel() {
     super.add(formElements);
     formElements.setStyleName("upld-form-elements");
   }
   public void add(Widget w) {
     formElements.add(w);
   }
 }
 public static final int DEFAULT_FILEINPUT_SIZE = 40;
 public static final String DEFAULT_SERVLET_PATH = "servlet.gupld";
 public static final String PARAMETER_FILENAME = "filename";
 
 public static final String PARAMETER_SHOW = "show";
 protected  static final String STYLE_BUTTON = "upld-button";
 protected  static final String STYLE_INPUT = "upld-input";
 protected  static final String STYLE_MAIN = "GWTUpld";
 protected  static final String STYLE_STATUS = "upld-status";
 protected static final int DEFAULT_AJAX_TIMEOUT = 10000;
 
 protected static final int DEFAULT_AUTOUPLOAD_DELAY = 600;
 protected static final int DEFAULT_TIME_MAX_WITHOUT_RESPONSE = 60000;
 
 protected static final int DEFAULT_UPDATE_INTERVAL = 500;
 protected static HashSet<String> fileDone = new HashSet<String>();
 protected static Vector<String> fileQueue = new Vector<String>(); 
 protected static int statusInterval = DEFAULT_UPDATE_INTERVAL;
 protected static final String TAG_CANCELED = "canceled";
 
 protected static final String TAG_CURRENT_BYTES = "currentBytes";
 protected static final String TAG_FINISHED = "finished";

 protected static final String TAG_PERCENT = "percent";
 protected static final String TAG_TOTAL_BYTES = "totalBytes";

 protected static final String TAG_WAIT = "wait";
 protected static int uploadTimeout = DEFAULT_TIME_MAX_WITHOUT_RESPONSE;
 
 /**
  * Configure the frequency to send status requests to the server.
  */
 public static void setStatusInterval(int statusInterval) {
   OpenELISUploader.statusInterval = statusInterval;
 }
 
 /**
  * Configure the maximal time without a valid response from the server.
  * When this period is reached, the upload process is cancelled.
  */
 public static void setUploadTimeout(int uploadTimeout) {
   OpenELISUploader.uploadTimeout = uploadTimeout;
 }
 
 protected static long now() {
   return (new Date()).getTime();
 }
 protected  UploaderConstants i18nStrs = GWT.create(UploaderConstants.class);
 protected  HorizontalPanel uploaderPanel;
 
 protected final Timer automaticUploadTimer = new Timer() {
   boolean firstTime = true;
   public void run() {
     if (isTheFirstInQueue()) {
       this.cancel();
       statusWidget.setStatus(IUploadStatus.Status.SUBMITING);
       
       // For security reasons, most browsers don't submit files if fileInput is hidden or has a size of 0,
       // so, before sending the form, it is necessary to show the fileInput,
       // finally, onSubmit handler will hide the fileInput again
       setFileInputSize(1);
       fileInput.getWidget().setSize("1px", "1px");
       fileInput.setVisible(true);
       uploadForm.submit();
     }
     if (firstTime) {
       addToQueue();
       firstTime = false;
       fileInput.setVisible(false);
       statusWidget.setVisible(true);
     }
   }
 };
 
 protected boolean autoSubmit = false;
 protected boolean avoidRepeatedFiles = false;
 protected String basename = null;
 protected IUploadStatus.UploadCancelHandler cancelHandler = new IUploadStatus.UploadCancelHandler() {
   public void onCancel() {
     cancel();
   }
 };
 protected boolean cancelled = false;
 protected IFileInput fileInput;
 protected String fileInputPrefix = "GWTU";
 protected FileInputType fileInputType;
 protected boolean finished = false;
 
 protected boolean hasSession = false;

 protected long lastData = now();
 protected final RequestCallback onCancelReceivedCallback = new RequestCallback() {
   public void onError(Request request, Throwable exception) {
     GWT.log("onCancelReceivedCallback onError: " , exception);
     statusWidget.setStatus(IUploadStatus.Status.CANCELED);
   }
   public void onResponseReceived(Request request, Response response) {
     if (getStatus() == Status.CANCELING) {
       updateStatusTimer.scheduleRepeating(3000);
     }
   }
 };
 protected Vector<IUploader.OnChangeUploaderHandler> onChangeHandlers = new Vector<IUploader.OnChangeUploaderHandler>();
 
 protected final RequestCallback onDeleteFileCallback = new RequestCallback() {
   public void onError(Request request, Throwable exception) {
     statusWidget.setStatus(Status.DELETED);
     GWT.log("onCancelReceivedCallback onError: ", exception);
   }

   public void onResponseReceived(Request request, Response response) {
     statusWidget.setStatus(Status.DELETED);
     fileDone.remove(getFileName());
   }
 };
 protected final ChangeHandler onFileInputChanged = new ChangeHandler() {
   public void onChange(ChangeEvent event) {
     basename = Utils.basename(getFileName());
     statusWidget.setFileName(basename);
     if (autoSubmit && validateExtension(basename) && basename.length() > 0) {
       if (avoidRepeatedFiles && fileDone.contains(fileInput.getFilename())) {
         return;
       } else {
         automaticUploadTimer.scheduleRepeating(DEFAULT_AUTOUPLOAD_DELAY);
       }
     }
     onChangeInput();
   }
 };
 protected Vector<IUploader.OnFinishUploaderHandler> onFinishHandlers = new Vector<IUploader.OnFinishUploaderHandler>();
 
 protected final RequestCallback onSessionReceivedCallback = new RequestCallback() {
   public void onError(Request request, Throwable exception) {
     String message = removeHtmlTags(exception.getMessage());
     cancelUpload(i18nStrs.uploaderServerUnavailable() + getServletPath() + "\n\n" + message);
   }
   public void onResponseReceived(Request request, Response response) {
     hasSession = true;
     uploadForm.submit();
   }
 };
 
 protected Vector<IUploader.OnStartUploaderHandler> onStartHandlers = new Vector<IUploader.OnStartUploaderHandler>();
 
 protected Vector<IUploader.OnStatusChangedHandler> onStatusChangeHandlers = new Vector<IUploader.OnStatusChangedHandler>();
 
 /**
  * Handler called when the status request response comes back.
  * 
  * In case of success it parses the xml document received and updates the progress widget
  * In case of a non timeout error, it stops the status repeater and notifies the user with the exception.
  */
 protected final RequestCallback onStatusReceivedCallback = new RequestCallback() {
   public void onError(Request request, Throwable exception) {
     waitingForResponse = false;
     if (exception instanceof RequestTimeoutException) {
       GWT.log("GWTUpload: onStatusReceivedCallback timeout error, asking the server again.", null);
     } else {
       GWT.log("GWTUpload: onStatusReceivedCallback error: " + exception.getMessage(), exception);
       updateStatusTimer.finish();
       String message = removeHtmlTags(exception.getMessage());
       message += "\n" + exception.getClass().getName();
       message += "\n" + exception.toString();
       statusWidget.setError(i18nStrs.uploaderServerUnavailable() + getServletPath() + "\n\n" + message);
     }
   }

   public void onResponseReceived(Request request, Response response) {
     
     waitingForResponse = false;
     if (finished == true && !uploading) {
       return;
     }
     
     parseAjaxResponse(response.getText());
   }

 };

 protected SubmitCompleteHandler onSubmitCompleteHandler = new SubmitCompleteHandler() {
   public void onSubmitComplete(SubmitCompleteEvent event) {
     serverResponse = event.getResults();
   }
 };

 /**
  *  Handler called when the file form is submitted
  *  
  *  If any validation fails, the upload process is canceled.
  *  
  *  If the client hasn't got the session, it asks for a new one 
  *  and the submit process is delayed until the client has got it
  */
 protected SubmitHandler onSubmitFormHandler = new SubmitHandler() {
   public void onSubmit(SubmitEvent event) {

     if (!finished && uploading) {
       uploading = false;
       statusWidget.setStatus(IUploadStatus.Status.CANCELED);
       return;
     }

     if (!autoSubmit && fileQueue.size() > 0) {
       statusWidget.setError(i18nStrs.uploaderActiveUpload());
       event.cancel();
       return;
     }

     if (fileDone.contains(getFileName())) {
       successful = true;
       event.cancel();
       uploadFinished();
       return;
     }

     if (basename == null || !validateExtension(basename)) {
       event.cancel();
       return;
     }

     if (!hasSession) {
       event.cancel();
       try {
         sendAjaxRequestToValidateSession();
       } catch (Exception e) {
           GWT.log("Exception in validateSession", null);
       }
       return;
     }

     addToQueue();

     uploading = true;
     finished = false;

     if (autoSubmit) {
       (new Timer() {
         public void run() {
           statusWidget.setVisible(true);
           fileInput.setVisible(false);
           updateStatusTimer.start();
           statusWidget.setStatus(IUploadStatus.Status.INPROGRESS);
           lastData = now();
         }
       }).schedule(200);
     } else {
       statusWidget.setVisible(true);
       // wait a time before asking the server status
       updateStatusTimer.squeduleStart();
       statusWidget.setStatus(IUploadStatus.Status.INPROGRESS);
       lastData = now();
     }
   }
 };
 
 protected int requestsCounter = 0;

 protected String serverResponse = null;
 
 protected IUploadStatus.UploadStatusChangedHandler statusChangedHandler = new IUploadStatus.UploadStatusChangedHandler() {
   public void onStatusChanged(IUploadStatus statusWiget) {
     for (IUploader.OnStatusChangedHandler handler : onStatusChangeHandlers) {
       handler.onStatusChanged(thisInstance);
     }
   }
 };
 
 protected IUploadStatus statusWidget = new BaseUploadStatus();

 protected boolean successful = false;

 protected OpenELISUploader thisInstance;
 
 protected final UpdateTimer updateStatusTimer = new UpdateTimer(this, statusInterval);

 protected FormPanel uploadForm;

 protected boolean uploading = false;
 
 protected String[] validExtensions = null;
 
 protected String validExtensionsMsg = "";

 protected boolean waitingForResponse = false;

 /**
  * Default constructor.
  * Initialize widget components and layout elements using the 
  * standard file input. 
  */
 public OpenELISUploader() {
   this(FileInputType.BROWSER_INPUT);
 }

 /**
  * Initialize widget components and layout elements using the 
  * standard file input. 
  * 
  * @param automaticUpload 
  *    when true the upload starts as soon as the user selects a file 
  */
 public OpenELISUploader(boolean automaticUpload) {
   this(FileInputType.BROWSER_INPUT, automaticUpload);
 }
 
 /**
  * Initialize widget components and layout elements. 
  * 
  * @param type
  *   file input to use
  */
 public OpenELISUploader(FileInputType type) {
   this(type, null);
 }
 
 /**
  * Initialize widget components and layout elements. 
  * 
  * @param type
  *   file input to use
  * @param automaticUpload
  *   when true the upload starts as soon as the user selects a file 
  */
 public OpenELISUploader(FileInputType type, boolean automaticUpload) {
   this(type);
   setAutoSubmit(automaticUpload);
 }
 
 /**
  * Initialize widget components and layout elements. 
  * 
  * @param type
  *   file input to use
  * @param form
  *   An existing form panel to use
  */
 public OpenELISUploader(FileInputType type, FormPanel form) {
   thisInstance = this;
   this.fileInputType = type;
   
   if (form == null) {
     form = new FormFlowPanel();
   }
   uploadForm = form;
   uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
   uploadForm.setMethod(FormPanel.METHOD_POST);
   uploadForm.setAction(DEFAULT_SERVLET_PATH);
   uploadForm.addSubmitHandler(onSubmitFormHandler);
   uploadForm.addSubmitCompleteHandler(onSubmitCompleteHandler);

   uploaderPanel = new HorizontalPanel();
   uploaderPanel.add(uploadForm);
   uploaderPanel.setStyleName(STYLE_MAIN);

   setFileInput(fileInputType.getInstance());
   
   setStatusWidget(statusWidget);

   super.initWidget(uploaderPanel);
 }
 
 /**
  * Adds a widget to formPanel.
  */
 public void add(Widget w) {
   uploadForm.add(w);
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#addOnCancelUploadHandler(gwtupload.client.IUploader.OnCancelUploaderHandler)
  */
 public HandlerRegistration addOnCancelUploadHandler(final OnCancelUploaderHandler handler) {
   assert handler != null;
   return statusWidget.addCancelHandler(new IUploadStatus.UploadCancelHandler() {
     public void onCancel() {
       handler.onCancel(thisInstance);
     }
   });
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#addOnChangeUploadHandler(gwtupload.client.IUploader.OnChangeUploaderHandler)
  */
 public HandlerRegistration addOnChangeUploadHandler(final IUploader.OnChangeUploaderHandler handler) {
   assert handler != null;
   onChangeHandlers.add(handler);
   return new HandlerRegistration() {
     public void removeHandler() {
       onChangeHandlers.remove(handler);
     }
   };
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#addOnFinishUploadHandler(gwtupload.client.IUploader.OnFinishUploaderHandler)
  */
 public HandlerRegistration addOnFinishUploadHandler(final IUploader.OnFinishUploaderHandler handler) {
   assert handler != null;
   onFinishHandlers.add(handler);
   return new HandlerRegistration() {
     public void removeHandler() {
       onFinishHandlers.remove(handler);
     }
   };
 }
 
 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#addOnStartUploadHandler(gwtupload.client.IUploader.OnStartUploaderHandler)
  */
 public HandlerRegistration addOnStartUploadHandler(final IUploader.OnStartUploaderHandler handler) {
   assert handler != null;
   onStartHandlers.add(handler);
   return new HandlerRegistration() {
     public void removeHandler() {
       onStartHandlers.remove(handler);
     }
   };
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#addOnStatusChangedHandler(gwtupload.client.IUploader.OnStatusChangedHandler)
  */
 public HandlerRegistration addOnStatusChangedHandler(final OnStatusChangedHandler handler) {
   assert handler != null;
   onStatusChangeHandlers.add(handler);
   return new HandlerRegistration() {
     public void removeHandler() {
       onStatusChangeHandlers.remove(handler);
     }
   };
 }

 /**
  * Don't send files that have already been uploaded.
  */
 public void avoidRepeatFiles(boolean avoidRepeat) {
   this.avoidRepeatedFiles = avoidRepeat;
 }

 /**
  * Cancel the current upload process.
  */
 public void cancel() {
   if (finished && !uploading) {
     if (successful) {
       try {
         sendAjaxRequestToDeleteUploadedFile();
       } catch (Exception e) {
       }
     } else {
       statusWidget.setStatus(Status.DELETED);
     }
     return;
   }
     
   if (cancelled) {
     return;
   }
   
   cancelled = true;
   automaticUploadTimer.cancel();
   GWT.log("cancelling " +  uploading, null);
   if (uploading) {
     updateStatusTimer.finish();
     try {
       sendAjaxRequestToCancelCurrentUpload();
     } catch (Exception e) {
       GWT.log("Exception cancelling request " + e.getMessage(), e);
     }
     statusWidget.setStatus(IUploadStatus.Status.CANCELING);
   } else {
     uploadFinished();
   }
 }

 /**
  * Remove all widget from the form.
  */
 public void clear() {
   uploadForm.clear();
 }

 /**
  * Returns the link for getting the uploaded file from the server
  * It's useful to display uploaded images or generate links to uploaded files.
  */
 public String fileUrl() {
   return composeURL(PARAMETER_SHOW + "=" + getInputName());
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#getBasename()
  */
 public String getBasename() {
   return Utils.basename(getFileName());
 }

 /**
  * Returns a JavaScriptObject properties with the url of the uploaded file.
  * It's useful in the exported version of the library. 
  * Because native javascript needs it
  */
 public JavaScriptObject getData() {
   return getDataImpl(fileUrl(), getInputName(), getFileName(), getBasename(), getServerResponse(), getStatus().toString());
 }

 public IFileInput getFileInput() {
   return fileInput;
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#getFileName()
  */
 public String getFileName() {
   return fileInput.getFilename();
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#getInputName()
  */
 public String getInputName() {
   return fileInput.getName();
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#getServerResponse()
  */
 public String getServerResponse() {
   return serverResponse;
 }

 /**
  * return the configured server service in the form-panel.
  */
 public String getServletPath() {
   return uploadForm.getAction();
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#getStatus()
  */
 public Status getStatus() {
   return statusWidget.getStatus();
 }

 /**
  * Get the status progress used.
  */
 public IUploadStatus getStatusWidget() {
   return statusWidget;
 }

 /**
  * Returns a iterator of the widgets contained in the form panel.
  */
 public Iterator<Widget> iterator() {
   return uploadForm.iterator();
 }

 /**
  * remove a widget from the form panel.
  */
 public boolean remove(Widget w) {
   return uploadForm.remove(w);
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#reset()
  */
 public void reset() {
   this.uploadForm.reset();
   updateStatusTimer.finish();
   uploading = cancelled = finished = successful = false;
   basename = serverResponse = null;
   fileDone = new HashSet<String>();
 }

 /**
  * Enable/disable automatic submit when the user selects a file.
  * @param b
  */
 public void setAutoSubmit(boolean b) {
   autoSubmit = b;
 }

 public void setFileInput(IFileInput input) {
   if (fileInput != null) {
     fileInput.getWidget().removeFromParent();
   }
   
   fileInput = input;
   fileInput.addChangeHandler(onFileInputChanged);
   fileInput.setText(i18nStrs.uploaderBrowse());
   setFileInputSize(DEFAULT_FILEINPUT_SIZE);
   assignNewNameToFileInput();
   uploadForm.add(fileInput.getWidget());
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#setFileInputPrefix(java.lang.String)
  */
 public void setFileInputPrefix(String prefix) {
   fileInputPrefix = prefix;
   assignNewNameToFileInput();
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#setFileInputSize(int)
  */
 public void setFileInputSize(int length) {
   fileInput.setLength(length);
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#setI18Constants(gwtupload.client.I18nUploadConstants)
  */
 public void setI18Constants(UploaderConstants strs) {
   i18nStrs = strs;
   fileInput.setText(strs.uploaderBrowse());
   statusWidget.setI18Constants(strs);
 }

 /**
  * set the url of the server service that receives the files and informs 
  * about the progress.  
  */
 public void setServletPath(String path) {
   if (path != null) {
     uploadForm.setAction(path);
   }
 }

 /**
  * set the status widget used to display the upload progress.
  */
 public void setStatusWidget(IUploadStatus stat) {
   if (stat == null) {
     return;
   }
   uploaderPanel.remove(statusWidget.getWidget());
   statusWidget = stat;
   if (!stat.getWidget().isAttached()) {
     uploaderPanel.add(statusWidget.getWidget());
   }
   statusWidget.getWidget().addStyleName(STYLE_STATUS);
   statusWidget.setVisible(false);
   statusWidget.addCancelHandler(cancelHandler);
   statusWidget.setStatusChangedHandler(statusChangedHandler);
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#setValidExtensions(java.lang.String[])
  */
 public void setValidExtensions(String[] validExtensions) {
   if (validExtensions == null) {
     this.validExtensions = new String[0];
     return;
   }
   this.validExtensions = new String[validExtensions.length];
   validExtensionsMsg = "";
   for (int i = 0, j = 0; i < validExtensions.length; i++) {
     String ext = validExtensions[i];
     if (ext == null) {
       continue;
     }
     if (ext.charAt(0) != '.') {
       ext = "." + ext;
     }
     if (i > 0) {
       validExtensionsMsg += ", ";
     }
     validExtensionsMsg += ext;

     ext = ext.replaceAll("\\.", "\\\\.");
     ext = ".+" + ext;
     this.validExtensions[j++] = ext.toLowerCase();
   }
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUploader#submit()
  */
 public void submit() {
   this.uploadForm.submit();
 }

 /* (non-Javadoc)
  * @see gwtupload.client.IUpdateable#update()
  */
 public void update() {
   try {
     if (waitingForResponse) {
       return;
     }
     waitingForResponse = true;
     // Using a reusable builder makes IE fail because it caches the response
     // So it's better to change the request path sending an additional random parameter
     RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL("filename=" + fileInput.getName() , "c=" + requestsCounter++));
     reqBuilder.setTimeoutMillis(DEFAULT_AJAX_TIMEOUT);
     reqBuilder.sendRequest("get_status", onStatusReceivedCallback);
   } catch (RequestException e) {
     e.printStackTrace();
   }
 }
 
 /**
  * Method called when the file input has changed. This happens when the 
  * user selects a file.
  * 
  * Override this method if you want to add a customized behavior,
  * but remember to call this in your function
  */
 protected  void onChangeInput() {
   for (IUploader.OnChangeUploaderHandler handler : onChangeHandlers) {
     handler.onChange(this);
   }
 }

 /**
  * Method called when the file upload process has finished,  
  * or the file has been canceled or removed from the queue.
  * Override this method if you want to add a customized behavior,
  * but remember to call this in your function.
  */
 protected  void onFinishUpload() {
   for (IUploader.OnFinishUploaderHandler handler : onFinishHandlers) {
     handler.onFinish(this);
   }
   if (autoSubmit == false) {
     assignNewNameToFileInput();
   }
 }
 
 /**
  * Method called when the file is added to the upload queue.
  * Override this if you want to add a customized behavior,
  * but remember to call this from your method.
  */
 protected  void onStartUpload() {
   for (IUploader.OnStartUploaderHandler handler : onStartHandlers) {
     handler.onStart(this);
   }
 }
 
 /**
  * Adds a file to the upload queue.
  */
 protected void addToQueue() {
   statusWidget.setStatus(IUploadStatus.Status.QUEUED);
   statusWidget.setProgress(0, 0);
   if (!fileQueue.contains(fileInput.getName())) {
     onStartUpload();
     fileQueue.add(fileInput.getName());
   }
 }
 
 /**
  * Change the fileInput name, because the server uses it as an uniq identifier.
  */
 protected void assignNewNameToFileInput() {
   String fileInputName = (fileInputPrefix + "-" + Math.random()).replaceAll("\\.", "");
   fileInput.setName(fileInputName);
 }

 /**
  * Cancel upload process and show an error message to the user.
  */
 protected void cancelUpload(String msg) {
   successful = false;
   uploadFinished();
   statusWidget.setStatus(IUploadStatus.Status.ERROR);
   statusWidget.setError(msg);
 }
 
 protected String composeURL(String... params) {
   String ret = getServletPath();
   String sep = ret.contains("?") ? "&" : "?";
   for (String par : params) { 
     ret += sep + par;
     sep = "&";
   }
   ret += sep + "random=" + Math.random();
   return ret;
 }

 protected native JavaScriptObject getDataImpl(String url, String inputName, String fileName, String baseName, String serverResponse, String status) /*-{
   return {
      url: url,
      name: inputName,
      filename: fileName,
      basename: baseName,
      response: serverResponse,
      status:  status
   };
 }-*/;

 protected boolean isTheFirstInQueue() {
   return fileQueue.size() > 0 && fileQueue.get(0).equals(fileInput.getName());
 }

 protected void parseAjaxResponse(String responseTxt) {
   String error = null;
   Document doc = null;

   try {
     doc = XMLParser.parse(responseTxt);
     error = Utils.getXmlNodeValue(doc, "error");
   } catch (Exception e) {
     if (responseTxt.toLowerCase().matches("error")) {
       error = i18nStrs.uploaderServerError() + "\nAction: " + getServletPath() + "\nException: " + e.getMessage() + responseTxt;
     }
   }
   
   if (error != null) {
     successful = false;
     cancelUpload(error);
     return;
   } else if (Utils.getXmlNodeValue(doc, TAG_WAIT) != null) {
   } else if (Utils.getXmlNodeValue(doc, TAG_CANCELED) != null) {
     successful = false;
     cancelled = true;
     uploadFinished();
     return;
   } else if (Utils.getXmlNodeValue(doc, TAG_FINISHED) != null) {
     successful = true;
     uploadFinished();
     return;
   } else if (Utils.getXmlNodeValue(doc, TAG_PERCENT) != null) {
     lastData = now();
     int transferredKB = Integer.valueOf(Utils.getXmlNodeValue(doc, TAG_CURRENT_BYTES)) / 1024;
     int totalKB = Integer.valueOf(Utils.getXmlNodeValue(doc, TAG_TOTAL_BYTES)) / 1024;
     statusWidget.setProgress(transferredKB, totalKB);
     return;
   } else {
     GWT.log("incorrect response: " + getFileName() + " " + responseTxt, null);
   }
   
   if (now() - lastData >  uploadTimeout) {
     successful = false;
     cancelUpload(i18nStrs.uploaderTimeout());
     try {
       sendAjaxRequestToCancelCurrentUpload();
     } catch (Exception e) {
     }
   }
 }

 /**
  * remove a file from the upload queue.
  */
 protected void removeFromQueue() {
   fileQueue.remove(fileInput.getName());
 }

 protected String removeHtmlTags(String message) {
   return message.replaceAll("<[^>]+>", "");
 }

 protected void sendAjaxRequestToCancelCurrentUpload() throws RequestException {
   RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL("cancel=true"));
   reqBuilder.sendRequest("cancel_upload", onCancelReceivedCallback);
 }

 protected void sendAjaxRequestToDeleteUploadedFile() throws RequestException {
   RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL("remove=" + getInputName()));
   reqBuilder.sendRequest("remove_file", onDeleteFileCallback);
 }

 /**
  * Sends a request to the server in order to get the session cookie,
  * when the response with the session comes, it submits the form.
  * 
  * This is needed because this client application usually is part of 
  * static files, and the server doesn't set the session until dynamic pages
  * are requested.
  * 
  * If we submit the form without a session, the server creates a new
  * one and send a cookie in the response, but the response with the
  * cookie comes to the client at the end of the request, and in the
  * meanwhile the client needs to know the session in order to ask
  * the server for the upload status.
  */
 protected void sendAjaxRequestToValidateSession() throws RequestException {
   // Using a reusable builder makes IE fail
   RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL("new_session=true"));
   reqBuilder.setTimeoutMillis(DEFAULT_AJAX_TIMEOUT);
   reqBuilder.sendRequest("create_session", onSessionReceivedCallback);
 }

 /**
  * Called when the uploader detects that the upload process has finished:
  * - in the case of submit complete.
  * - in the case of error talking with the server.
  */
 protected void uploadFinished() {
   try {
     removeFromQueue();
     finished = true;
     uploading = false;
     updateStatusTimer.finish();

     if (successful) {
       if (avoidRepeatedFiles) {
         if (fileDone.contains(getFileName())) {
           if (autoSubmit) {
             statusWidget.setVisible(false);
             statusWidget.setStatus(IUploadStatus.Status.REPEATED);
           } else {
             successful = false;
             statusWidget.setError(i18nStrs.uploaderAlreadyDone());
           }
         } else {
           fileDone.add(getFileName());
           statusWidget.setStatus(IUploadStatus.Status.SUCCESS);
         }
       } else {
         statusWidget.setStatus(IUploadStatus.Status.SUCCESS);
       }
     } else if (cancelled) {
       statusWidget.setStatus(IUploadStatus.Status.CANCELED);
     } else {
       statusWidget.setStatus(IUploadStatus.Status.ERROR);
     }
     if (!autoSubmit) {
       statusWidget.setVisible(false);
     } else {
       uploaderPanel.remove(uploadForm);
     }
     onFinishUpload();
   } catch (Exception e) {
     Window.alert(e.getMessage());
   }
 }
 
 protected boolean validateExtension(String filename) {
   if (filename == null || filename.length() == 0) {
     return false;
   }
   boolean valid = Utils.validateExtension(validExtensions, filename);
   if (!valid) {
     if (autoSubmit) {
       uploaderPanel.remove(uploadForm);
     }
     statusWidget.setError(i18nStrs.uploaderInvalidExtension() + validExtensionsMsg);
   }
   return valid;
 }

}
