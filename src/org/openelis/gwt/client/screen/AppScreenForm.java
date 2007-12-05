package org.openelis.gwt.client.screen;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.openelis.gwt.client.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.client.widget.ButtonPanel;
import org.openelis.gwt.client.widget.FormInt;
import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.IForm;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;

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
    private DataSet key;
    public ScreenWindow window;
    public ConstantsWithLookup constants = (ConstantsWithLookup)ScreenBase.getWidgetMap().get("AppConstants");
    public static AppScreenFormServiceIntAsync formService;
    //this is used to internationalize the status bar messages
    //private ConstantsWithLookup constants = null;

    public AppScreenForm(AppScreenFormServiceIntAsync service) {
        super(formService);
        formService = service;
    }
    
    public AppScreenForm() {
        super();
        modelWidget.addChangeListener(this);
    }
    
    public void afterDraw(boolean sucess) {
        bpanel.setState(FormInt.DISPLAY);
        enable(false);
        bpanel.enable("u",false);
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
       bpanel.enable("ud",true);
       if(!success)
            key = null;
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query(int state) {
        bpanel.setState(FormInt.QUERY);
        if(constants != null)
            message.setText(constants.getString("enterFieldsToQuery"));
        else
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
            if(constants != null)
                message.setText(constants.getString("updateFieldsPressCommit"));
            else
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
        formService.delete(key, (FormRPC)forms.get("display"), new AsyncCallback() {
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key, rpc);
               load();
               afterDelete(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDelete(false);
           }
        });
        
    }
    
    public void afterDelete(boolean success){
        
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
                if(constants != null)
                    message.setText(constants.getString("updating"));
                else
                    message.setText("Updating...");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                if(constants != null)
                    message.setText(constants.getString("correctErrors"));
                else
                    message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.ADD) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                if(constants != null)
                    message.setText(constants.getString("adding"));
                else
                    message.setText("Adding...");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                if(constants != null)
                    message.setText(constants.getString("correctErrors"));
                else
                    message.setText("Please correct the errors indicated, then press Commit");
            }
        }
        if (state == FormInt.QUERY) {            
            if(constants != null)
                message.setText(constants.getString("querying"));
            else
                message.setText("Querying...");
            commitQuery(rpc);
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
            if(constants != null)
                message.setText(constants.getString("updatingComplete"));
            else
                message.setText("Updating...Complete");
        }else{
            if(constants != null)
                message.setText(constants.getString("updateFailed"));
            else
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
            if(constants != null)
                message.setText(constants.getString("addingComplete"));
            else
                message.setText("Adding...Complete");
        }else{
            if(constants != null)
                message.setText(constants.getString("addingFailed"));
            else
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
    
    public void getPage() {
        formService.commitQuery(null, modelWidget.getModel(), new AsyncCallback() {
            public void onSuccess(Object result){
                modelWidget.setModel((DataModel)result);
            }
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage());
            }
         });
    }
    
    public void afterCommitQuery(boolean success) {
        if(success){
            bpanel.setState(FormInt.DISPLAY);
            if(constants != null)
                message.setText(constants.getString("queryingComplete"));
            else
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
           /* formService.abort(rpc, key, new AsyncCallback() {
               public void onSuccess(Object result){
                   rpc = (FormRPC)result;
                   forms.put(rpc.key, rpc);
                   load();
               }
               public void onFailure(Throwable caught){
                   Window.alert(caught.getMessage());
               }
            });*/
            enable(false);
            if(constants != null)
            	message.setText(constants.getString("updateAborted"));
            else
            	message.setText("Update aborted");
        }
        if (state == FormInt.ADD) {
            doReset();
            clearErrors();
            enable(false);
            if(constants != null)
                message.setText(constants.getString("addAborted"));
            else
                message.setText("Add aborted");
        }
        if (state == FormInt.QUERY) {
            setForm(false);
            load((FormRPC)forms.get("display"));
            enable(false);
            if(constants != null)
                message.setText(constants.getString("queryAborted"));
            else
                message.setText("Query aborted");
        }
        bpanel.setState(FormInt.DISPLAY);
        bpanel.enable("ud",false);
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
                key = modelWidget.getSelected();
                fetch();
            }
            if(modelWidget.event == DataModelWidget.GETPAGE)
                getPage();
        }
    }
}