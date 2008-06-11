package org.openelis.gwt.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.MenuLabel;
import org.openelis.gwt.widget.table.TableWidget;

public class ScreenInputWidget extends ScreenWidget implements FocusListener, MouseListener {
    
    protected ScreenInputWidget queryWidget;
    protected Widget displayWidget;
    protected boolean queryMode;
    protected HorizontalPanel hp;
    protected FocusPanel errorImg = new FocusPanel();
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    protected boolean showError = true;
    
    public ScreenInputWidget() {

    }
    
    public ScreenInputWidget(Node node){
        super(node);
        if(node.getAttributes().getNamedItem("showError") != null){
            if(node.getAttributes().getNamedItem("showError").getNodeValue().equals("false"))
                showError = false;
        }
            
    }
    
    public void setQueryWidget(ScreenInputWidget qWid){
        queryWidget = qWid;
        queryWidget.key = key;
    }
    
    public void setForm(boolean mode){
        this.queryMode = mode;
        if(queryMode){
            if(queryWidget != null){
            	if(queryWidget instanceof ScreenTableWidget){
                    ((TableWidget)((ScreenTableWidget)queryWidget).getWidget()).controller.model.reset();
                    ((TableWidget)((ScreenTableWidget)queryWidget).getWidget()).controller.model.addRow(null);
                    ((TableWidget)((ScreenTableWidget)queryWidget).getWidget()).controller.reset();
                    ((TableWidget)((ScreenTableWidget)queryWidget).getWidget()).controller.enabled(true);
            	}
                initWidget(queryWidget.displayWidget);
            }else{
                queryMode = false;
            }
        }else{
           initWidget(displayWidget);
        }
    }
    
    public void initWidget(Widget widget){
        if(hp == null){
            hp = new HorizontalPanel();
            if(showError){
                errorImg.setStyleName("ErrorPanelHidden");
                errorImg.addMouseListener(this);
                hp.add(errorImg);
            }
        }
        if(hp.getWidgetCount() > 1 || (hp.getWidgetCount() == 1 && !showError)){
            hp.remove(0);
        }
        hp.insert(widget,0);
        setWidget(hp);
    }

    public void onFocus(final Widget sender) {
        if(sender instanceof TextBoxBase){
            //((TextBoxBase)sender).setSelectionRange(0, 0);
            DeferredCommand.addCommand(new Command(){
                public void execute() {
                   //((TextBoxBase)sender).setSelectionRange(0,0);
                   ((TextBoxBase)sender).setCursorPos(0);
                   ((TextBoxBase)sender).selectAll();
                }
            });
        }
    }

    public void onLostFocus(Widget sender) {
        if(!showError)
            return;
    	String tempKey = key;
    	
        submit(screen.rpc.getField(tempKey));
        screen.rpc.getField(tempKey).clearErrors();
        screen.rpc.getField(tempKey).validate();
        if(!screen.rpc.getField(tempKey).isValid())
            drawError();
        else{
            errorImg.setStyleName("ErrorPanelHidden");
            if(pop != null){
                pop.hide();
            }
        }
        if(sender instanceof TextBoxBase){
            ((TextBoxBase)sender).setSelectionRange(0, 0);
        }
    }
    
    public void clearError() {
        if(!showError)
            return;
        if(pop != null){
            pop.hide();
        }
    	errorImg.setStyleName("ErrorPanelHidden");
        errorPanel.clear();
    }
    
    public void drawError() {
        if(!showError)
            return;
    	String[] errors;
        errors = screen.rpc.getField(key).getErrors();
        
        errorPanel.clear();
        for (int i = 0; i < errors.length; i++) {
            String error = errors[i];
            MenuLabel errorLabel = new MenuLabel(error,"Images/bullet_red.png");
            errorLabel.setStyleName("errorPopupLabel");
            //errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
            errorPanel.add(errorLabel);
        }
        if(errors.length == 0){
            errorImg.setStyleName("ErrorPanelHidden");
        }else{
            errorImg.setStyleName("ErrorPanel");
        }
    }
    
    public void drawBusyIcon(){
        errorImg.setStyleName("BusyPanel");
    }
    
    public void clearBusyIcon(){
    	errorImg.setStyleName("ErrorPanelHidden");
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        if(errorImg.getStyleName().equals("ErrorPanel")){
            if(pop == null){
                pop = new PopupPanel();
                //pop.setStyleName("ErrorPopup");
            }
            ScreenWindow win = new ScreenWindow(pop,"","","",false);
            win.setStyleName("ErrorWindow");
            win.setContent(errorPanel);
            win.setVisible(true);
            pop.setWidget(win);
            pop.setPopupPosition(sender.getAbsoluteLeft()+16, sender.getAbsoluteTop());
            pop.show();
        }
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        if(errorImg.getStyleName().equals("ErrorPanel")){
            if(pop != null){
                pop.hide();
            }
        }
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public Widget getWidget() {
        return hp.getWidget(0);
    }
    

}
