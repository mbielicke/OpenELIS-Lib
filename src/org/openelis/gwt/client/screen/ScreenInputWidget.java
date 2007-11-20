package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenInputWidget extends ScreenWidget {
    
    protected ScreenInputWidget queryWidget;
    protected Widget displayWidget;
    protected boolean queryMode;
    
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
                initWidget(queryWidget.displayWidget);
            }else{
                queryMode = false;
            }
        }else{
           initWidget(displayWidget);
        }
    }
    

}
