package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenInputWidget extends ScreenWidget {
    
    protected ScreenWidget queryWidget;
    protected Widget displayWidget;
    protected boolean queryMode;
    
    public ScreenInputWidget() {
        
    }
    
    public ScreenInputWidget(Node node){
        super(node);
    }
    
    public void setQueryWidget(ScreenWidget qWid){
        queryWidget = qWid;
    }
    
    public void setForm(boolean mode){
        this.queryMode = mode;
        if(queryMode){
            if(queryWidget != null){
                if(displayWidget == null)
                    displayWidget = getWidget();
                initWidget(queryWidget.getWidget());
            }else{
                queryMode = false;
            }
        }else{
            if(queryWidget != null)
                queryWidget.initWidget(getWidget());
            if(displayWidget != null)
                initWidget(displayWidget);
        }
    }
    

}
