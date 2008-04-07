package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.TableModel;
import org.openelis.gwt.screen.ScreenBase;

import java.util.ArrayList;

public class TableMultiple extends SimplePanel implements TableCellWidget {
    
    ArrayList cells = new ArrayList();
    public int active;
    AbstractField field;
    public static final String TAG_NAME = "table-multiple";
    
    public TableMultiple() {
        
    }
    
    public void clear() {
        for(int i = 0; i < cells.size(); i++){
            ((TableCellWidget)cells.get(i)).clear();
        }
    }

    public void enable(boolean enabled) {
       for(int i = 0; i < cells.size(); i++){
           ((TableCellWidget)cells.get(i)).enable(enabled);
       }
        
    }

    public TableMultiple(Node node) {
        NodeList editors = node.getChildNodes();
        for (int i = 0; i < editors.getLength(); i++) {
            if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                cells.add(ScreenBase.createCellWidget(editors.item(i)));
            }
        }
    }

    public TableCellWidget getNewInstance() {
        TableMultiple mult = new TableMultiple();
        for(int i = 0; i < cells.size(); i++){
            mult.cells.add(((TableCellWidget)cells.get(i)).getNewInstance());
        }
        mult.active = active;
        return mult;
    }

    public void saveValue() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).saveValue();
    }

    public void setDisplay() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).setDisplay();
        setWidget((Widget)cells.get(active));
        
    }

    public void setEditor() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).setEditor();
        setWidget((Widget)cells.get(active));
        
    }

    public void setField(AbstractField field) {
       this.field = field;
        
    }
    
    public void initCells(TableModel model){
        for(int i = 0; i < cells.size(); i++){
            if(cells.get(i) instanceof TableOption){
                if(((TableOption)cells.get(i)).loadFromHidden != null){
                    ((TableOption)cells.get(i)).fromHidden = (OptionField)model.hidden.get(((TableOption)cells.get(i)).loadFromHidden);
                }
            }
        }
    }
    
    public Widget getWidget() {
        return ((SimplePanel)super.getWidget()).getWidget();
    }
    
    public void initCells(TableController controller){
        for(int i = 0; i < cells.size(); i++){
            if(cells.get(i) instanceof TableOption){
                ((TableOption)cells.get(i)).setListener(controller);
            }
        }
    }

    public void setCellWidth(int width) {
        for(int i = 0; i < cells.size(); i++){
            ((TableCellWidget)cells.get(i)).setCellWidth(width);
        }
        
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return null;
    }

}
