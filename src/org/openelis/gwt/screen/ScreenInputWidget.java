package org.openelis.gwt.screen;

import org.openelis.gwt.widget.MenuLabel;
import org.openelis.gwt.widget.table.TableWidget;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenInputWidget extends ScreenWidget implements FocusListener, MouseListener {
    
    protected ScreenInputWidget queryWidget;
    protected Widget displayWidget;
    protected boolean queryMode;
    protected HorizontalPanel hp;
    protected FocusPanel errorImg = new FocusPanel();
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    
    public ScreenInputWidget() {

    }
    
    public ScreenInputWidget(Node node){
        super(node);
    }
    
    public void setQueryWidget(ScreenInputWidget qWid){
        queryWidget = qWid;
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
            errorImg.setStyleName("ErrorPanelHidden");
            errorImg.addMouseListener(this);
            hp.add(errorImg);
        }
        if(hp.getWidgetCount() > 1){
            hp.remove(0);
        }
        hp.insert(widget,0);
        setWidget(hp);
    }

    public void onFocus(Widget sender) {
        
    }

    public void onLostFocus(Widget sender) {
        submit(screen.rpc.getField(key));
        screen.rpc.getField(key).clearErrors();
        screen.rpc.getField(key).validate();
        if(!screen.rpc.getField(key).isValid())
            drawError();
        else{
            errorImg.setStyleName("ErrorPanelHidden");
        }
    }
    
    public void clearError() {
    	errorImg.setStyleName("ErrorPanelHidden");
        errorPanel.clear();
    }
    
    public void drawError() {
        String[] errors = screen.rpc.getField(key).getErrors();
        errorPanel.clear();
        for (int i = 0; i < errors.length; i++) {
            String error = errors[i];
            errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
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
                pop.setStyleName("ErrorPopup");
            }
            pop.setWidget(errorPanel);
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
