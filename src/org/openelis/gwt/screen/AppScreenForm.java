package org.openelis.gwt.screen;

//import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.IForm;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
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
public class AppScreenForm extends AppScreen implements FormInt, ChangeListener, SourcesChangeEvents {
    
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
    public AppScreenFormServiceIntAsync formService;
    public int state = FormInt.DEFAULT;
    protected ChangeListenerCollection changeListeners;

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
        bpanel.addChangeListener(this);
        addChangeListener(bpanel);
        changeState(FormInt.DEFAULT);
        enable(false);
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
       changeState(FormInt.DISPLAY);
       if(!success)
            key = null;
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query() {
        doReset();
        state = FormInt.QUERY;
        window.setStatus("Enter fields to query by then press Commit","");
        setForm(true);
        rpc = (FormRPC)forms.get("query");
        doReset();
        enable(true);
        changeState(FormInt.QUERY);
        
    }

    /**
     * This stub can be overridden to handle the default behavior for when the
     * Next button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void next() {
        // TODO Auto-generated method stub
        modelWidget.next();
        
    }
    
    /**
     * This stub can be overridden to handle the default behavior for when the
     * Previous button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void prev() {
        // TODO Auto-generated method stub
        modelWidget.previous();
    }

    /** 
     * This method provides the default behavior for a form when the Add button on a 
     * ButtonPanel is clicked.  It is called from the ButtonPanel Widget.  If overridden 
     * in the extending class be sure to call super.add(state).
     */
    public void add() {
        doReset();
        enable(true);
        changeState(FormInt.ADD);
        window.setStatus("Enter information in the fields, then press Commit","");
    }

    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void up() {
        window.setStatus("Locking record for Update...","spinnerIcon");
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
            window.setStatus("Update fields then, press Commit","");
            changeState(FormInt.UPDATE);
        }else{
            window.setStatus("", "");
            changeState(FormInt.DISPLAY);
        }
    }

    /** 
     * This stub can be overridden by the extending class to handle the behavior 
     * for when the Delete button on a ButtonPanel is clicked.  It is called from 
     * the ButtonPanel widget.
     */
    public void delete() {
    	//strikethru all the input widgets
    	strikeThru(true);
    	
    	//set the state to delete
        changeState(FormInt.DELETE);

    	
        //set the message to delete
        window.setStatus("Pressing commit will delete the current record from the database","");           
    }
    
    public void commitDelete(){
        window.setStatus("Deleting...","spinnerIcon");
    	formService.commitDelete(key, (FormRPC)forms.get("display"), new AsyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key, rpc);
                load();
                afterCommitDelete(true);
            }
            public void onFailure(Throwable caught){
            	if(caught instanceof RPCDeleteException)
            		window.setStatus(caught.getMessage(),"ErrorPanel");
                else
            		Window.alert(caught.getMessage());
                afterCommitDelete(false);
            }
         });
    }
    
    public void afterCommitDelete(boolean success){
    	if(success){
            getPage(false,"Delete...Complete");        
    		strikeThru(false);
            changeState(FormInt.DEFAULT);
        }
    }

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit() {
        super.doSubmit();
        if (state == FormInt.UPDATE) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                window.setStatus("Updating..","spinnerIcon");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                window.setStatus("Please correct the errors indicated, then press Commit","ErrorPanel");
            }
        }
        if (state == FormInt.ADD) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                window.setStatus("Adding...","spinnerIcon");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                window.setStatus("Please correct the errors indicated, then press Commit","ErrorPanel");
            }
        }
        if (state == FormInt.QUERY) {            
            commitQuery(rpc);
        }
        if(state == FormInt.DELETE){
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
            changeState(FormInt.DISPLAY);
            window.setStatus("Updating...Complete","");
        }else{
            window.setStatus("Update Failed. Make corrections and try again or Abort.","ErrorPanel");
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
            changeState(FormInt.DISPLAY);
            window.setStatus("Adding...Complete","");
        }else{
            window.setStatus("Adding Failed. Make corrections and try again or Abort","ErrorPanel");
        }
    }
    
    public void commitQuery(FormRPC rpcQuery) {
        window.setStatus("Querying...","spinnerIcon");
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
    	if(modelWidget.getPage() < 0){
    		window.setStatus("You have reached the first page of your query results","");
    		modelWidget.getModel().setPage(0);
    		modelWidget.getModel().select(modelWidget.getSelectedIndex()+1);
    	}else{
            window.setStatus("Querying...","spinnerIcon");
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
    	                window.setStatus("Querying...Complete","");
    	            }else{
    	                message.setText(messageText);
    	            }
    	        }
                        
    	        public void onFailure(Throwable caught){
    	            
    	            if(caught instanceof LastPageException){
    	                modelWidget.getModel().setPage(modelWidget.getPage()-1);
    	                if(modelWidget.getSelectedIndex() == modelWidget.getModel().size()){
    	                    modelWidget.getModel().select(modelWidget.getSelectedIndex()-1);
    	                    window.setStatus("You are at the end of your query results","ErrorPanel");             		   
    	                }else
    	                    window.setStatus(caught.getMessage(),"ErrorPanel");
    	            }else
    	                Window.alert(caught.getMessage());
    	        }
    	        });
        }
    }
    
    public void afterCommitQuery(boolean success) {
        if(success){
        	///
        	doReset();
        	setForm(false);
            load((FormRPC)forms.get("display"));
            enable(false);
            if(modelWidget.getSelectedIndex() > -1)
            	changeState(FormInt.DISPLAY);
            else
            	changeState(FormInt.DEFAULT);
            window.setStatus("Querying...Complete","");
        }else{
            window.setStatus("Querying Failed. Make corrections and try again or Abort","ErrorPanel");
        }
    }
    /**
     * This method provides the default behavior for a form when the Abort button 
     * on a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void abort() {
        if (state == FormInt.UPDATE) {
            rpc.operation = IForm.CANCEL;
            clearErrors();
            doReset();
            formService.abort(key, (FormRPC)forms.get("display"), new AsyncCallback() {
               public void onSuccess(Object result){
                   rpc = (FormRPC)result;
                   forms.put(rpc.key, rpc);
                   load();
                   afterAbort(true);
               }
               public void onFailure(Throwable caught){
                   Window.alert(caught.getMessage());
                   afterAbort(false);
               }
            });
            enable(false);
            changeState(FormInt.DISPLAY);
            window.setStatus("Update aborted","");
        }
        if (state == FormInt.ADD) {
            doReset();
            clearErrors();
            enable(false);
            window.setStatus("Add aborted","");
            changeState(FormInt.DEFAULT);
        }
        if (state == FormInt.QUERY) {
            setForm(false);
            load((FormRPC)forms.get("display"));
            enable(false);
            window.setStatus("Query aborted","");
            //FIXME we need to see if there is any data selected
            if(modelWidget.getSelectedIndex() > -1)
              	changeState(FormInt.DISPLAY);
            else
              	changeState(FormInt.DEFAULT);
        }
        if(state == FormInt.DELETE){
        	strikeThru(false);
        	window.setStatus("Delete aborted","");
        	changeState(FormInt.DISPLAY);
        }
    }

    public void afterAbort(boolean success) {
        
    }
    /** 
     * This method provides the default logic for a form when the Reload button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void reload() {
        fetch();
    }
    
    public void select() {
        // TODO Auto-generated method stub
        
    }
    /**
     * This method is called when the ScreenForm is displayed as part of a ScreenWindow widget.  
     * It will return true if the button panel is one of the three update modes and false when 
     * it is any of the display only field.
     */
    public boolean hasChanges() {
        // TODO Auto-generated method stub
        if(state == FormInt.ADD ||
           state == FormInt.QUERY ||
           state == FormInt.UPDATE){
                window.setStatus("You must Commit or Abort changes first","ErrorPanel");
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
    	//clear the message, the action method can set it to something else
    	if(message != null)
    		window.setStatus("","");
    	
        if(sender == modelWidget){
            if(modelWidget.event == DataModelWidget.SELECTION){
                key = (DataSet)((DataSet)modelWidget.getSelected()).getInstance();
                fetch();
            }
            if(modelWidget.event == DataModelWidget.GETPAGE)
                getPage(true, null);
        }
        if(sender == bpanel){
            if (bpanel.buttonClicked.action.equals("query")) {
                query();
            }
            else if (bpanel.buttonClicked.action.equals("next")) {
                next();
            }
            else if (bpanel.buttonClicked.action.equals("prev")) {
                prev();
            }
            else if (bpanel.buttonClicked.action.equals("add")) {
                add();
            }
            else if (bpanel.buttonClicked.action.equals("update")) {
                up();
            }
            else if (bpanel.buttonClicked.action.equals("delete")) {
                delete();
            }
            else if (bpanel.buttonClicked.action.equals("commit")) {
                commit();
            }
            else if (bpanel.buttonClicked.action.equals("abort")) {
                abort();
            }
            else if (bpanel.buttonClicked.action.equals("reload")) {
                reload();
            }
            else if (bpanel.buttonClicked.action.equals("select")) {
                select();
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null){
            changeListeners.remove(listener);
        }       
    }
    
    public void changeState(int state){
        this.state = state;
        changeListeners.fireChange(this);
    }
}