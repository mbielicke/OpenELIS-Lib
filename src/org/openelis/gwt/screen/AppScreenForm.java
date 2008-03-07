package org.openelis.gwt.screen;

//import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.IForm;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;

import java.util.Iterator;
/**
 * ScreenForm extends Screen to include functionality for integrating 
 * the ButtonPanel widget and default logic for standard forms that accept
 * input and also perform queries
 * @author tschmidt
 *
 */
public class AppScreenForm extends AppScreen implements FormInt, ChangeListener {
    
    /**
     * Reference to the ButtonPanel that is defined on this
     * Screen.  This needs to be set by the Extending class 
     * after the screen is drawn.
     */
    public ButtonPanel bpanel = null;
    /**
     * Reference to the Label that will display the Form Messages to
     * the User.  If this Screen is set in a ScreenWindow this 
     * field will be set by that class.  If not, then the Extending class
     * must set this field after the Screen is drawn.
     */
    public Label message = new Label();
    
    public DataModelWidget modelWidget = new DataModelWidget();
    protected DataSet key;
    public ScreenWindow window;
    //public ConstantsWithLookup constants = (ConstantsWithLookup)ScreenBase.getWidgetMap().get("AppConstants");
    public AppScreenFormServiceIntAsync formService;
    //this is used to internationalize the status bar messages
    //private ConstantsWithLookup constants = null;

    public AppScreenForm(AppScreenFormServiceIntAsync service) {
        super(service);
        formService = service;
    }
    
    public AppScreenForm() {
        super();
        modelWidget.addChangeListener(this);
    }
    
    public void afterDraw(boolean sucess) {
        super.afterDraw(sucess);
        bpanel.setForm(this);
        bpanel.setState(FormInt.DISPLAY);
        enable(false);
        bpanel.setButtonState("update",AppButton.DISABLED);
        if(window != null){
            window.setVisible(true);
            RootPanel.get().removeStyleName("ScreenLoad");
        }
    }
    
    public void fetch(){
        formService.fetch(key, (FormRPC)forms.get("display"), new AsyncCallback(){
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key, rpc);
               load();
               afterFetch(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterFetch(false);
           }
        });
    }
    
    public void afterFetch(boolean success){
       enable(false);
       bpanel.setState(FormInt.DISPLAY);
       bpanel.setButtonState("update",AppButton.UNPRESSED);
       bpanel.setButtonState("delete", AppButton.UNPRESSED);
       if(!success)
            key = null;
       
       //if it is the first page with the first selected. dont enable the previous button
       if(modelWidget.getModel().getPage() == 0 && modelWidget.getModel().getSelectedIndex() == 0) {
    	   bpanel.setButtonState("next", AppButton.UNPRESSED);
    	   bpanel.setButtonState("prev",AppButton.DISABLED);
       }else{
    	   bpanel.setButtonState("next", AppButton.UNPRESSED);
           bpanel.setButtonState("prev",AppButton.UNPRESSED);
       }
       
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query(int state) {
        bpanel.setState(FormInt.QUERY);
       // if(constants != null)
        //    message.setText(constants.getString("enterFieldsToQuery"));
        //else
            message.setText("Enter fields to query by then press Commit");
        setForm(true);
        rpc = (FormRPC)forms.get("query");
        doReset();
        enable(true);
        
    }

    /**
     * This stub can be overridden to handle the default behavior for when the
     * Next button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void next(int state) {
        // TODO Auto-generated method stub
        modelWidget.next();
        
    }
    
    /**
     * This stub can be overridden to handle the default behavior for when the
     * Previous button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void prev(int state) {
        // TODO Auto-generated method stub
        modelWidget.previous();
    }

    /** 
     * This method provides the default behavior for a form when the Add button on a 
     * ButtonPanel is clicked.  It is called from the ButtonPanel Widget.  If overridden 
     * in the extending class be sure to call super.add(state).
     */
    public void add(int state) {
        doReset();
        enable(true);
        bpanel.setState(FormInt.ADD);
        //if(constants != null)
        //    message.setText(constants.getString("enterInformationPressCommit"));
        //else
            message.setText("Enter information in the fields, then press Commit");
    }

    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void up(int state) {
        formService.fetchForUpdate(key, (FormRPC)forms.get("display"), new AsyncCallback() {
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key, rpc);
               load();
               afterUpdate(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterUpdate(false);
           }
        });
    }
    
    public void afterUpdate(boolean success){
        if(success){
            enable(true);
           // if(constants != null)
           //     message.setText(constants.getString("updateFieldsPressCommit"));
           // else
                message.setText("Update fields then, press Commit");            
            bpanel.setState(FormInt.UPDATE);
        }else
            bpanel.setState(FormInt.DISPLAY);
    }

    /** 
     * This stub can be overridden by the extending class to handle the behavior 
     * for when the Delete button on a ButtonPanel is clicked.  It is called from 
     * the ButtonPanel widget.
     */
    public void delete(int state) {
    	//strikethru all the input widgets
    	strikeThru(true);
    	
    	//set the state to delete
    	bpanel.setState(FormInt.DELETE);
    	
    	//set the message to delete
    	//if(constants != null)
        //    message.setText(constants.getString("deleteMessage"));
       // else
        	message.setText("Pressing commit will delete the current record from the database");           
    }
    
    public void commitDelete(){
    	formService.commitDelete(key, (FormRPC)forms.get("display"), new AsyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key, rpc);
                load();
                afterCommitDelete(true);
            }
            public void onFailure(Throwable caught){
            	if(caught instanceof RPCDeleteException)
            		message.setText(caught.getMessage());
                else
            		Window.alert(caught.getMessage());
                afterCommitDelete(false);
            }
         });
    }
    
    public void afterCommitDelete(boolean success){
    	if(success){
    	//	if(constants != null)
    	//		getPage(false,constants.getString("deleteComplete"));
        //    else
            	getPage(false,"Delete...Complete");        

    		strikeThru(false);
    		bpanel.setState(FormInt.DISPLAY);   
        }
    }

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit(int state) {
        super.doSubmit();
        if (state == FormInt.UPDATE) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
          //      if(constants != null)
          //          message.setText(constants.getString("updating"));
          //      else
                    message.setText("Updating...");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
          //      if(constants != null)
          //          message.setText(constants.getString("correctErrors"));
          //      else
                    message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.ADD) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
          //      if(constants != null)
          //          message.setText(constants.getString("adding"));
          //      else
                    message.setText("Adding...");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
         //       if(constants != null)
         //           message.setText(constants.getString("correctErrors"));
         //       else
                    message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.QUERY) {            
         //   if(constants != null)
         //       message.setText(constants.getString("querying"));
         //   else
                message.setText("Querying...");
            commitQuery(rpc);
        }
        if(state == FormInt.DELETE){
        //	 if(constants != null)
        //         message.setText(constants.getString("deleting"));
        //     else
            	 message.setText("Deleting...");
           
        	 commitDelete();
        }
        
    }
    
    public void commitUpdate() {
        formService.commitUpdate(rpc, (FormRPC)forms.get("display"),new AsyncCallback() {
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key, rpc);
               load();
               afterCommitUpdate(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterCommitUpdate(false);
           }
        });
    }
    
    public void afterCommitUpdate(boolean success) {
        if(success){
            enable(false);
            bpanel.setState(FormInt.DISPLAY);
       //     if(constants != null)
       //         message.setText(constants.getString("updatingComplete"));
       //     else
                message.setText("Updating...Complete");
        }else{
      //      if(constants != null)
      //          message.setText(constants.getString("updateFailed"));
      //      else
                message.setText("Update Failed. Make corrections and try again or Abort.");
        }
    }
    
    public void commitAdd() {
        formService.commitAdd(rpc, (FormRPC)forms.get("display"), new AsyncCallback() {
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key,rpc);
               load();
               afterCommitAdd(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterCommitAdd(false);
           }
        });
    }
    
    public void afterCommitAdd(boolean success) {
        if(success){
            enable(false);
            bpanel.setState(FormInt.DISPLAY);
      //      if(constants != null)
      //          message.setText(constants.getString("addingComplete"));
      //      else
                message.setText("Adding...Complete");
        }else{
      //      if(constants != null)
      //          message.setText(constants.getString("addingFailed"));
      //      else
                message.setText("Adding Failed. Make corrections and try again or Abort");
        }
    }
    
    public void commitQuery(FormRPC rpcQuery) {
        formService.commitQuery(rpcQuery, modelWidget.getModel(), new AsyncCallback() {
           public void onSuccess(Object result){
               modelWidget.setModel((DataModel)result);
               afterCommitQuery(true);
               modelWidget.select(0);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterCommitQuery(false);
           }
        });
    }
    
    public void getPage(final boolean selectItem, final String messageText) {
        formService.commitQuery(null, modelWidget.getModel(), new AsyncCallback() {
            public void onSuccess(Object result){
                modelWidget.setModel((DataModel)result);
                if(selectItem){
	                if(modelWidget.getModel().isSelectLast())
	                	modelWidget.select(modelWidget.getModel().size()-1);
	                else
	                	modelWidget.select(0);
                }        
                
                if(messageText == null){
	    //            if(constants != null)
	    //                message.setText(constants.getString("queryingComplete"));
	    //            else
	                    message.setText("Querying...Complete");
                }else{
                	message.setText(messageText);
                }
            }
            
            public void onFailure(Throwable caught){
                
                if(caught instanceof LastPageException){
             	   modelWidget.getModel().setPage(modelWidget.getPage()-1);
             	   if(modelWidget.getSelectedIndex() == modelWidget.getModel().size()){
             		   modelWidget.getModel().select(modelWidget.getSelectedIndex()-1);
             		   
        //     		  if(constants != null)
        //                  message.setText(constants.getString("endingQueryException"));
        //              else
                          message.setText("You are at the end of your query results");             		   
             	   }else
             		   message.setText(caught.getMessage());
                }else
                	Window.alert(caught.getMessage());
            }
         });
    }
    
    public void afterCommitQuery(boolean success) {
        if(success){
        	///
        	doReset();
        	setForm(false);
            load((FormRPC)forms.get("display"));
            enable(false);
            bpanel.setState(FormInt.DISPLAY);

      //      if(constants != null)
      //          message.setText(constants.getString("queryingComplete"));
      //      else
                message.setText("Querying...Complete");
            
        }
    }
    /**
     * This method provides the default behavior for a form when the Abort button 
     * on a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void abort(int state) {
        if (state == FormInt.UPDATE) {
            rpc.operation = IForm.CANCEL;
            clearErrors();
            formService.abort(key, (FormRPC)forms.get("display"), new AsyncCallback() {
               public void onSuccess(Object result){
                   rpc = (FormRPC)result;
                   forms.put(rpc.key, rpc);
                   load();
               }
               public void onFailure(Throwable caught){
                   Window.alert(caught.getMessage());
               }
            });
            enable(false);
     //       if(constants != null)
     //       	message.setText(constants.getString("updateAborted"));
     //       else
            	message.setText("Update aborted");
        }
        if (state == FormInt.ADD) {
            doReset();
            clearErrors();
            enable(false);
     //       if(constants != null)
     //           message.setText(constants.getString("addAborted"));
     //       else
                message.setText("Add aborted");
        }
        if (state == FormInt.QUERY) {
            setForm(false);
            load((FormRPC)forms.get("display"));
            enable(false);
    //        if(constants != null)
    //            message.setText(constants.getString("queryAborted"));
    //        else
                message.setText("Query aborted");
        }
        if(state == FormInt.DELETE){
        	strikeThru(false);
        	
   //     	if(constants != null)
   //             message.setText(constants.getString("deleteAborted"));
   //         else
        	message.setText("Delete aborted");
        }
        bpanel.setState(FormInt.DISPLAY);
        //bpanel.setButtonState("update",AppButton.DISABLED);
        //bpanel.setButtonState("delete", AppButton.DISABLED);
    }

    /** 
     * This method provides the default logic for a form when the Reload button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void reload(int state) {
        fetch();
    }
    
    public void select(int state) {
        // TODO Auto-generated method stub
        
    }
    /**
     * This method is called when the ScreenForm is displayed as part of a ScreenWindow widget.  
     * It will return true if the button panel is one of the three update modes and false when 
     * it is any of the display only field.
     */
    public boolean hasChanges() {
        // TODO Auto-generated method stub
        if(bpanel == null)
            return false;
        if(bpanel.state == FormInt.ADD ||
           bpanel.state == FormInt.QUERY ||
           bpanel.state == FormInt.UPDATE){
   //         if(constants != null)
   //             message.setText(constants.getString("mustCommitOrAbort"));
   //         else 
                message.setText("You must Commit or Abort changes first");
            return true;
        }
        return false;
    }
    
    public void onDetach() {
        if(!keep){
            bpanel = null;
            message = null;      
            window = null;
        }
        super.onDetach();
    }
    
    public void setForm(boolean mode){
        Iterator widIt = widgets.values().iterator();
        while(widIt.hasNext()){
            ScreenWidget wid = (ScreenWidget)widIt.next();
            if(wid instanceof ScreenInputWidget){
                ((ScreenInputWidget)wid).setForm(mode);
            }
        }
        
    }

    public void onChange(Widget sender) {
        if(sender == modelWidget){
            if(modelWidget.event == DataModelWidget.SELECTION){
                key = (DataSet)((DataSet)modelWidget.getSelected()).getInstance();
                fetch();
            }
            if(modelWidget.event == DataModelWidget.GETPAGE)
                getPage(true, null);
        }
    }

    public void option(String action, int state) {
        // TODO Auto-generated method stub
        
    }
}