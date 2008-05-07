package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import org.openelis.gwt.common.IForm;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;
/**
 * ScreenForm extends Screen to include functionality for integrating 
 * the ButtonPanel widget and default logic for standard forms that accept
 * input and also perform queries
 * @author tschmidt
 *
 */
public class ScreenForm extends Screen implements FormInt {
	
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
    public Label message;
    
    public ScreenWindow window;
    
    //this is used to internationalize the status bar messages
    //private ConstantsWithLookup constants = null;


	public ScreenForm() {
        super();
    }
    
    public ScreenForm(String xml){
        super(xml);
    }
    
    /**
     * This method will do the default behavior for afterSubmit service calls for
     * a ScreenForm.  If the extending class needs to alter this method make sure to call
     * super.afterSubmit(method,success) from there.
     */
    public void afterSubmit(String method, boolean success) {
        if (method.equals("draw")) {
            //bpanel.setForm(this);
            bpanel.setState(FormInt.DISPLAY);
            enable(false);
            bpanel.setButtonState("update",AppButton.DISABLED);
            bpanel.setButtonState("delete", AppButton.DISABLED);
            if(window != null){
                window.setVisible(true);
                RootPanel.get().removeStyleName("ScreenLoad");
            }
        }
        if (method.equals("commit") ||
            method.equals("commit-add") ||
            method.equals("commit-update")) {
            if(success){
                //  doReset();
                enable(false);
                bpanel.setState(FormInt.DISPLAY);
                //  bpanel.enable("u",false);
                if(method.equals("commit-update")) {
                	if(constants != null)
                		message.setText(constants.getString("updatingComplete"));
                	else
                		message.setText("Updating...Complete");
                }
                if(method.equals("commit-add")) {
                	if(constants != null)
                		message.setText(constants.getString("addingComplete"));
                	else
                		message.setText("Adding...Complete");
                }
            }else{
                if(method.equals("commit-update")) {
                	if(constants != null)
                		message.setText(constants.getString("updateFailed"));
                	else
                		message.setText("Update Failed. Make corrections and try again or Abort.");
                }
                if(method.equals("commit-add")) {
                	if(constants != null)
                		message.setText(constants.getString("addingFailed"));
                	else
                		message.setText("Adding Failed. Make corrections and try again or Abort");
                }
            }
        }
        if (method.equals("fetch")) {
            enable(false);
            bpanel.setState(FormInt.DISPLAY);
            bpanel.setButtonState("update",AppButton.UNPRESSED);
            bpanel.setButtonState("delete", AppButton.UNPRESSED);
        }
        if ((method.equals("update") || method.equals("add")) && success) {
            enable(true);
            if(method.equals("update")){
            	if(constants != null)
            		message.setText(constants.getString("updateFieldsPressCommit"));
            	else
            		message.setText("Update fields then, press Commit");
            	
                bpanel.setState(FormInt.UPDATE);
            }
        }
        if (method.equals("update") && !success){
            bpanel.setState(FormInt.DISPLAY);
        }
    }
    
    /**
     * This method provides default behavior for afterQuery method for a form.
     * If the extending class needs to add behavior to this method be sure to 
     * call super.afterQuery(result,success) from the extending method.
     */
    public void afterQuery(Object result, boolean success){
        if(success){
            enable(false);
            bpanel.setState(FormInt.DISPLAY);
            doReset();
            if(constants != null)
            	message.setText(constants.getString("queryingComplete"));
            else
            	message.setText("Querying...Complete");
            
            bpanel.setButtonState("update",AppButton.DISABLED);
            bpanel.setButtonState("delete", AppButton.DISABLED);
        }
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query(int state) {
        doReset();
        enable(true);
        bpanel.setState(FormInt.QUERY);
        
        if(constants != null)
        	message.setText(constants.getString("enterFieldsToQuery"));
        else
        	message.setText("Enter fields to query by then press Commit");
    }

    /**
     * This stub can be overridden to handle the default behavior for when the
     * Next button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void next(int state) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * This stub can be overridden to handle the default behavior for when the
     * Previous button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void prev(int state) {
        // TODO Auto-generated method stub
        
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
        
        if(constants != null)
        	message.setText(constants.getString("enterInformationPressCommit"));
        else
        	message.setText("Enter information in the fields, then press Commit");
    }

    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void up(int state) {
        rpc.operation = IForm.DISPLAY_UPDATE;
        callService("update");        
        
    }

    /** 
     * This stub can be overridden by the extending class to handle the behavior 
     * for when the Delete button on a ButtonPanel is clicked.  It is called from 
     * the ButtonPanel widget.
     */
    public void delete(int state) {
        // TODO Auto-generated method stub
        
    }

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit(int state) {
        if (state == FormInt.UPDATE) {
            super.doSubmit();
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
            	if(constants != null)
            		message.setText(constants.getString("updating"));
            	else
            		message.setText("Updating...");
            	
                clearErrors();
                callService("commit-update");
            } else {
                drawErrors();
                if(constants != null)
                	message.setText(constants.getString("correctErrors"));
                else
                	message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.ADD) {
            super.doSubmit();
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
            	if(constants != null)
            		message.setText(constants.getString("adding"));
            	else
            		message.setText("Adding...");
                clearErrors();
                callService("commit-add");
            } else {
                drawErrors();
                if(constants != null)
                	message.setText(constants.getString("correctErrors"));
                else
                	message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.QUERY) {
            super.doSubmit();
            if(constants != null)
            	message.setText(constants.getString("querying"));
            else
            	message.setText("Querying...");
            
            callService("query");
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
            if(constants != null)
            	message.setText(constants.getString("updateAborted"));
            else
            	message.setText("Update aborted");
            
            callService("fetch");
        }
        if (state == FormInt.ADD) {
            doReset();
            clearErrors();
            load();
            enable(false);
            
            if(constants != null)
            	message.setText(constants.getString("addAborted"));
            else
            	message.setText("Add aborted");
        }
        if (state == FormInt.QUERY) {
            doReset();
            ((DeckPanel)getWidget("formDeck")).showWidget(0);
            enable(false);
            
            if(constants != null)
            	message.setText(constants.getString("queryAborted"));
            else
            	message.setText("Query aborted");
        }
        bpanel.setState(FormInt.DISPLAY);
        bpanel.setButtonState("update",AppButton.DISABLED);
        bpanel.setButtonState("delete", AppButton.DISABLED);
    }

    /** 
     * This method provides the default logic for a form when the Reload button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void reload(int state) {
        rpc.operation = IForm.DISPLAY;
        callService("fetch");
        
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
        if(bpanel.getState() == FormInt.ADD ||
           bpanel.getState() == FormInt.QUERY ||
           bpanel.getState() == FormInt.UPDATE){
        	if(constants != null)
        		message.setText(constants.getString("mustCommitOrAbort"));
        	else 
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

    public void select(int state) {
        // TODO Auto-generated method stub
        
    }

    public void option(String action, int state) {
        // TODO Auto-generated method stub
        
    }

    public void abort() {
        // TODO Auto-generated method stub
        
    }

    public void add() {
        // TODO Auto-generated method stub
        
    }

    public void commit() {
        // TODO Auto-generated method stub
        
    }

    public void delete() {
        // TODO Auto-generated method stub
        
    }

    public void next() {
        // TODO Auto-generated method stub
        
    }

    public void prev() {
        // TODO Auto-generated method stub
        
    }

    public void query() {
        // TODO Auto-generated method stub
        
    }

    public void reload() {
        // TODO Auto-generated method stub
        
    }

    public void select() {
        // TODO Auto-generated method stub
        
    }

    public void update() {
        // TODO Auto-generated method stub
        
    }
}