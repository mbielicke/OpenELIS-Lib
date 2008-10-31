package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TreeDataItem;

public class TreeDragHandler implements DragListener, DropListener {
    
    private TreeWidget controller;
    
    public TreeDragHandler(TreeWidget controller) {
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
        if(sender instanceof TreeRow){
            if(controller.model.canDrop(source, ((TreeRow)sender).modelIndex)){
                controller.model.drop(source, ((TreeRow)sender).modelIndex);
                controller.model.refresh();
            }
        }else {
            controller.model.drop(source);
        }
        
    }

    Timer scroll;
    Timer open;
    
    public void onDropEnter(Widget sender, final Widget source) {
        sender.addStyleName("Highlighted");
        final int rowIndex = ((TreeRow)sender).index;
        final int modelIndex = ((TreeRow)sender).modelIndex;
        final TreeDataItem row = ((TreeRow)sender).item;
        if((rowIndex == 0 && modelIndex > 0) || 
           (rowIndex == 9 && modelIndex < controller.model.rows.size() -1)){
            if(rowIndex == 9){
                if(modelIndex < controller.model.rows.size() -1){
                    controller.view.setScrollPosition(controller.view.scrollBar.getScrollPosition()+10);
                    scroll = new Timer() {
                        public void run() {
                            onDropEnter(controller.renderer.getRows().get(9),source);
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
        if(row.getItems().size() > 0 && !row.open){
            if(open != null)
                open.cancel();
            open = new Timer() {
                public void run() {
                    if(controller.model.canToggle(modelIndex))
                        controller.model.toggle(modelIndex);
                }
            };
            open.schedule(1000);
        }
    }

    public void onDropExit(Widget sender, Widget source) {
        sender.removeStyleName("Highlighted");
        if(scroll != null) {
            scroll.cancel();
            scroll = null;
        }   
        if(open != null) {
            open.cancel();
            open = null;
        }
    }

    public void onDropOver(Widget sender, Widget source) {

    }

}
