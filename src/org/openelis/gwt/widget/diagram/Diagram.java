package org.openelis.gwt.widget.diagram;

import java.util.HashMap;

import javax.swing.tree.TreeNode;

import org.openelis.gwt.screen.ScreenWindow;
import org.openelis.gwt.widget.IconContainer;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.tree.TreeDataItem;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Diagram extends Composite implements MouseListener,ClickListener,ClickHandler,DoubleClickHandler,SourcesCommandEvents{
    
    public enum Action {HOVER,LEAVE,CLICK};
    
    public GWTCanvas canvas = new GWTCanvas();
    public AbsolutePanel panel = new AbsolutePanel();
    public ScrollPanel scroll = new ScrollPanel();
    public TreeDataModel model;
    public HashMap<TreeDataItem,TreeNode> mapping = new HashMap<TreeDataItem,TreeNode>();
    public ScreenWindow window;
    public CommandListenerCollection commandListeners;
    private TreeNode editNode;
    public TreeNode selectNode;
    
    public PickupDragController dragController = new PickupDragController(panel,true) {
      @Override
      public void dragMove() {
        super.dragMove();
        TreeNode node = (TreeNode)this.context.draggable;
        node.item.x = node.getAbsoluteLeft() - panel.getAbsoluteLeft();//this.context.mouseX;
        node.item.y = node.getAbsoluteTop() - panel.getAbsoluteTop();//this.context.mouseY;
        canvas.clear();
        canvas.beginPath();
        drawGraph(model.get(0));
        canvas.closePath();
        canvas.setLineWidth(0.5);
        canvas.stroke();
      }  
      @Override
        public void dragStart() {
            super.dragStart();
            commandListeners.fireCommand(Action.LEAVE, this.context.draggable);
        }
    };
    public AbsolutePositionDropController dropController = new AbsolutePositionDropController(panel);
    
    public class TreeNode extends IconContainer implements HasDoubleClickHandlers {
        
        public TreeDataItem item;
        public boolean showing;
        public int rowback = -1;
        
        public TreeNode(String style, Diagram diagram,TreeDataItem item){
            super(style);
            addMouseListener(diagram);
            addClickListener(diagram);
            enable(true);
            this.item = item;
            this.setStyleName("TreeNode");           
        }

        public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
            // TODO Auto-generated method stub
            return addDomHandler(handler, DoubleClickEvent.getType());
        }
        
    }
    
    public Diagram() {
        initWidget(scroll);
        panel.setSize("1000px", "1000px");
        panel.add(canvas);
        scroll.setWidget(panel);
        scroll.setSize("600px", "300px");
        panel.setWidgetPosition(canvas, 0, 0);
        canvas.setSize("1000px", "1000px");
        //canvas.addClickListener(this);
    }
    
    public void setModel(TreeDataModel model) {
        this.model = model;
    }
    
    public void draw(TreeDataModel model) {
        setModel(model);
        draw();
    }
    
    public void draw() {
        for(TreeNode node : mapping.values()){
            panel.remove(node);
        }
        canvas.clear();
        canvas.setLineWidth(1);
        canvas.setCoordSize(1000, 1000);
        drawNodes(model.get(0));
        DeferredCommand.addCommand(new Command(){
            public void execute() {
                canvas.beginPath();
                drawGraph(model.get(0));
                canvas.closePath();
                canvas.setLineWidth(0.5);
                canvas.stroke();
            } 
        });
    }
    
    public void drawNodes(TreeDataItem item) {
        for(int i = 0; i < item.getItems().size(); i++){
            drawNodes(item.getItem(i));
        }
        if(item.hasChildren()) {
            item.y = item.getItem(item.getItems().size()/2).y;
            if(item.getItems().size()%2 == 0)
                item.y -= 35/2;
        }else if(item.childIndex > 0){
            if(item.getPreviousSibling().hasChildren()){
                item.y = item.getPreviousSibling().getLastChild().y+35;
            }else{
                item.y = item.getPreviousSibling().y+35;
            }
        }else if(item.parent != null && item.parent.getPreviousSibling() != null && item.parent.getPreviousSibling().hasChildren()){
            item.y = item.parent.getPreviousSibling().getLastChild().y+35;
        }else if(item.parent != null && item.parent.getPreviousSibling() != null){
            item.y = item.parent.getPreviousSibling().y+35;
            item.y -= item.parent.getItems().size()/2 * 35 /2;
        }else
            item.y = 5;
        item.x = item.depth*200+5;
        TreeNode node = new TreeNode("",this,item);
        node.setWidth("100px");
        dragController.makeDraggable(node);
        node.add(new Label((String)item.cells[0].getValue()));
        node.addDoubleClickHandler(this);
        panel.add(node,item.x,item.y);
        mapping.put(item,node);
        dragController.makeDraggable(node);
    }
    
    public void drawGraph(TreeDataItem item) {    
        if(item.getItems().size() > 0){
            for(TreeDataItem child : item.getItems()){
                TreeNode itemNode = mapping.get(item);
                TreeNode childNode = mapping.get(child);
                //if(itemNode.showing){
                //    canvas.moveTo(item.x+table.view.getOffsetWidth(),item.y+itemNode.getOffsetHeight()+18+(((Integer)child.cells[1].getValue()).intValue()*18)+9);
                //}else
                canvas.moveTo(item.x+(itemNode.getOffsetWidth()/2),item.y+(itemNode.getOffsetHeight()/2));
                int cX, cY;
                if(item.y+(itemNode.getOffsetHeight()/2) < child.y-40){
                    cX = child.x+(childNode.getOffsetWidth()/2);
                    cY = child.y;
                }else if(item.y+(itemNode.getOffsetHeight()/2) > child.y+childNode.getOffsetHeight()+40){
                    cX = child.x+(childNode.getOffsetWidth()/2);
                    cY = child.y+childNode.getOffsetHeight();
                }else if(item.x+(itemNode.getOffsetWidth()/2) > child.x+childNode.getOffsetWidth()+40){
                    cX = child.x +childNode.getOffsetWidth();
                    cY = child.y +childNode.getOffsetHeight()/2;
                }else{
                    cX = child.x;
                    cY = child.y + childNode.getOffsetHeight()/2;
                }
                
                cX = child.x;
                cY = child.y + childNode.getOffsetHeight()/2;
                if(Math.abs(item.x+(itemNode.getOffsetWidth()/2) - child.x) < 40){
                    cX = child.x+(childNode.getOffsetWidth()/2);
                    if(item.y+(itemNode.getOffsetHeight()/2) > child.y+childNode.getOffsetHeight())
                        cY = child.y + childNode.getOffsetHeight();
                }
                
                double rise = (item.y+itemNode.getOffsetHeight()/2) - cY;
                double run  = cX - (item.x); 
                if(run < 0.0)
                    cX = child.x + childNode.getOffsetWidth();
                if((run != 0.0 && (rise/run) > 1.5) || (run == 0.0 && rise < 0)){
                    cX = child.x + childNode.getOffsetWidth()/2;
                    if(run < 0.0)
                        cY = child.y;
                    else
                        cY = child.y + childNode.getOffsetHeight();
                }else if((run != 0.0 && (rise/run) < -1.5) || (run == 0.0 && rise > 0)){
                    cX = child.x +childNode.getOffsetWidth()/2;
                    if(run < 0.0)
                        cY = child.y + childNode.getOffsetHeight();
                    else
                        cY = child.y;
                }
                canvas.lineTo(cX,cY);
                canvas.saveContext();
                canvas.translate(cX,cY);
                rise = (item.y+itemNode.getOffsetHeight()/2) - cY;
                run  = cX - (item.x+itemNode.getOffsetWidth()/2); 
                double rotation = 0.0; 
                if(run == 0.0) {
                    rotation = Math.PI;
                }else{
                    rotation = Math.atan(rise/run);
                }
                if(run < 0)
                    rotation += Math.PI;
                canvas.rotate(rotation*-1);
                canvas.moveTo(0,0);
                canvas.lineTo(-7,-3);
                canvas.moveTo(0,0);
                canvas.lineTo(-7,3);
                canvas.restoreContext();
                if(child.getItems().size() > 0)
                    drawGraph(child);
            }
        }
        
    }

    public void onMouseDown(Widget sender, int x, int y) {
        sender.addStyleName("Pressed");
        
    }
    
    public void onMouseEnter(Widget sender) {
        sender.addStyleName("NodeHover");
        commandListeners.fireCommand(Action.HOVER, sender);        
    }

    public void onMouseLeave(Widget sender) {
        sender.removeStyleName("NodeHover");
        commandListeners.fireCommand(Action.LEAVE, sender);
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        sender.removeStyleName("Pressed");
        
    }
    

    public void onClick(Widget sender) {
        if(editNode != null && sender != editNode)
            saveEditNode();
        if(selectNode != null && sender != selectNode){
            selectNode.removeStyleName("NodeSelected");
            selectNode = null;
        }
        if(selectNode == null && sender instanceof TreeNode){
            selectNode = (TreeNode)sender;
            selectNode.addStyleName("NodeSelected");
            commandListeners.fireCommand(Action.CLICK, sender);
        }
    }

    public void addCommandListener(CommandListener listener) {
       if(commandListeners == null){
           commandListeners = new CommandListenerCollection();
       }
       commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
       if(commandListeners != null) {
          commandListeners.remove(listener);
       }
        
    }

    public void onClick(ClickEvent event) {
        
    }

    public void onDoubleClick(DoubleClickEvent event) {
        if(selectNode != null)
            selectNode.removeStyleName("NodeSelected");
        editNode = (TreeNode)event.getSource();
        TextBox box = new TextBox();
        box.setWidth(editNode.getOffsetWidth()+"px");
        box.setValue((String)editNode.item.cells[0].getValue());
     //   editNode.remove(0);
        editNode.add(box);
        box.setFocus(true);
        selectNode = editNode;
        selectNode.addStyleName("NodeSelected");
    }
    
    private void saveEditNode() {
        //editNode.item.cells[0].setValue(((TextBox)editNode.getWidget(0)).getValue());
        //editNode.remove(0);
        Label label = new Label((String)editNode.item.cells[0].getValue());
        editNode.add(label);
        selectNode.removeStyleName("NodeSelected");
        editNode = null;
        selectNode = null;
    }
    
    
    

}
