package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataSet;

public class TableDragHandler implements DragListener, DropListener {
    
    private TableWidget<? extends DataSet> controller;
    
    public TableDragHandler(TableWidget<? extends DataSet> controller) {
        this.controller = controller;
    }

    public void onDragDropEnd(Widget sender, Widget target) {
        // TODO Auto-generated method stub
        
    }

    public void onDragEnd(Widget sender, int x, int y) {
        DOM.removeChild(RootPanel.get().getElement(), sender.getParent().getElement());
        
    }

    public void onDragEnter(Widget sender, Widget target) {
        
        
    }

    public void onDragExit(Widget sender, Widget target) {
        
        
    }

    public void onDragMouseMoved(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onDragOver(Widget sender, Widget target) {
        
    }

    public void onDragStart(Widget sender, int x, int y) {
        controller.view.addStyleName("locked");
        
    }

    public void onDrop(Widget sender, Widget source) {
        sender.removeStyleName("Highlighted");
        if(sender instanceof TableRow){
            if(controller.model.canDrop(source, ((TableRow)sender).modelIndex)){
                controller.model.drop(source, ((TableRow)sender).modelIndex);
                controller.model.refresh();
            }
        }else {
            controller.model.drop(source);
        }
        
    }

    Timer scroll;
    
    public void onDropEnter(Widget sender, final Widget source) {
      
        sender.addStyleName("Highlighted");
        if(!(sender instanceof TableRow))
            return;
        final int rowIndex = ((TableRow)sender).index;
        final int modelIndex = ((TableRow)sender).modelIndex;
        if((rowIndex == 0 && modelIndex > 0) || 
           (rowIndex == controller.maxRows -1 && modelIndex < controller.model.shownRows() -1)){
            if(rowIndex == controller.maxRows -1){
                if(modelIndex < controller.model.shownRows() -1){
                    controller.view.setScrollPosition(controller.view.scrollBar.getScrollPosition()+10);
                    scroll = new Timer() {
                        public void run() {
                            onDropEnter(controller.renderer.getRows().get(controller.maxRows -1),source);
                        }
                    };
                    scroll.schedule(150);
                }
            }
            if(rowIndex == 0){
                if(modelIndex > 0){
                    controller.view.setScrollPosition(controller.view.scrollBar.getScrollPosition()-10);
                    scroll = new Timer() {
                        public void run() {
                            onDropEnter(controller.renderer.getRows().get(0),source);
                        }
                    };
                    scroll.schedule(150);
                }
            }
        }
    }

    public void onDropExit(Widget sender, Widget source) {
       
        sender.removeStyleName("Highlighted");
        if(!(sender instanceof TableRow))
            return;
        if(scroll != null) {
            scroll.cancel();
            scroll = null;
        }   
    }

    public void onDropOver(Widget sender, Widget source) {

    }

}
