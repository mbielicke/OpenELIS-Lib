package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;

import org.openelis.gwt.client.widget.ButtonPanel;
import org.openelis.gwt.client.widget.FormInt;
import org.openelis.gwt.common.IForm;
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
    public ButtonPanel bpanel;
    /**
     * Reference to the Label that will display the Form Messages to
     * the User.  If this Screen is set in a ScreenWindow this 
     * field will be set by that class.  If not, then the Extending class
     * must set this field after the Screen is drawn.
     */
    public Label message;
    
    public ScreenWindow window;
    
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
            bpanel.setForm(this);
            bpanel.setState(FormInt.DISPLAY);
            enable(false);
            bpanel.enable("u",false);
        }
        if (method.equals("commit") ||
            method.equals("commit-add") ||
            method.equals("commit-update")) {
            if(success){
                //  doReset();
                enable(false);
                bpanel.setState(FormInt.DISPLAY);
                //  bpanel.enable("u",false);
                if(method.equals("commit-update"))
                    message.setText("Updating...Complete");
                if(method.equals("commit-add"))
                    message.setText("Adding...Complete");
            }else{
                if(method.equals("commit-update"))
                    message.setText("Update Failed. Make corrections and try again or Abort.");
                if(method.equals("commit-add"))
                    message.setText("Adding Failed. Make corrections and try again or Abort");
            }
        }
        if (method.equals("fetch")) {
            enable(false);
            bpanel.setState(FormInt.DISPLAY);
            bpanel.enable("u",true);
        }
        if ((method.equals("update") || method.equals("add")) && success) {
            enable(true);
            if(method.equals("update")){
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
            message.setText("Querying...Complete");
            bpanel.enable("u",false);
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
                message.setText("Updating...");
                clearErrors();
                callService("commit-update");
            } else {
                drawErrors();
                message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.ADD) {
            super.doSubmit();
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                message.setText("Adding...");
                clearErrors();
                callService("commit-add");
            } else {
                drawErrors();
                message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.QUERY) {
            super.doSubmit();
            message.setText("Querying....");
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
            message.setText("Update aborted");
            callService("fetch");
        }
        if (state == FormInt.ADD) {
            doReset();
            clearErrors();
            load();
            enable(false);
            message.setText("Add aborted");
        }
        if (state == FormInt.QUERY) {
            doReset();
            ((DeckPanel)getWidget("formDeck")).showWidget(0);
            enable(false);
            message.setText("Query aborted");
        }
        bpanel.setState(FormInt.DISPLAY);
        bpanel.enable("u",false);
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
        if(bpanel.state == FormInt.ADD ||
           bpanel.state == FormInt.QUERY ||
           bpanel.state == FormInt.UPDATE){
            message.setText("You must Commit or Abort changes first");
            return true;
        }
        return false;
    }

}
