package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.OptionItem;
import org.openelis.gwt.widget.OptionList;

import java.util.Iterator;

/**
 * An OpptionList that implements CellWidget so that it can be used in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableOption extends TableCellInputWidget {
	
	private OptionList editor;
	private Label display;
    private ChangeListener listener;
	
    public boolean loadFromModel;
    public String loadFromHidden = null;
    private boolean multi;
    private int visible = 1;
    public OptionField fromHidden;
    private boolean enabled;

    public TableOption() {
        sinkEvents(Event.KEYEVENTS);
    }

    public TableCellWidget getNewInstance() {
        TableOption to = new TableOption();
        to.multi = multi;
        to.fromHidden = fromHidden;
        to.visible = visible;
        to.loadFromHidden = loadFromHidden;
        to.loadFromModel = loadFromModel;
        to.editor = editor;
        to.listener = listener;
        to.enabled = enabled;
        return to;
    }

    public Widget getInstance(Node node) {
        TableOption to = new TableOption();
        if (node.getAttributes().getNamedItem("fromModel") != null) {
            to.loadFromModel = true;
        } else if (node.getAttributes().getNamedItem("fromHidden") != null) {
            to.loadFromHidden = node.getAttributes()
                                    .getNamedItem("fromHidden")
                                    .getNodeValue();
        } else {
        	to.editor = new OptionList();
            to.editor.addChangeListener(listener);
            NodeList items = ((Element)node).getElementsByTagName("item");
            for (int j = 0; j < items.getLength(); j++) {
                to.editor.addItem(items.item(j)
                                .getAttributes()
                                .getNamedItem("value")
                                .getNodeValue(), (items.item(j).getFirstChild() == null ? " " : items.item(j).getFirstChild().getNodeValue()));
            }
        }
        if (node.getAttributes().getNamedItem("multi") != null){
            if(node.getAttributes().getNamedItem("multi").getNodeValue().equals("true")){
            	multi = true;
            	if(to.editor != null)
            		to.editor.setMultipleSelect(true);
            }
        }
        if (node.getAttributes().getNamedItem("size") != null){
        	visible = Integer.parseInt(node.getAttributes().getNamedItem("size").getNodeValue());
        	if(to.editor != null)
        		to.editor.setVisibleItemCount(visible);
        }
        return to;
    }

	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
		}
		if(field instanceof OptionField){
			display.setText(((OptionField)field).getDisplay());
            display.setTitle(field.getTip());
		}else{
			if(loadFromHidden != null){
				fromHidden.setValue(field.getValue());
				display.setText(fromHidden.getDisplay());
                display.setTitle(fromHidden.getTip());
			}else{
				editor.setValue(field.getValue());
				display.setText(editor.getItemText(editor.getSelectedIndex()));
                display.setTitle(field.getTip());
			}
		}
		setWidget(display);
	}

	public void setEditor() {
        if(!enabled)
            return;
		if(editor == null){
			editor = new OptionList();
            editor.addChangeListener(listener);
			editor.setMultipleSelect(multi);
			editor.setVisibleItemCount(visible);
			if(loadFromHidden != null){
		        Iterator fieldIt = fromHidden.getOptions().iterator();
		        while (fieldIt.hasNext()) {
		            OptionItem item = (OptionItem)fieldIt.next();
		            editor.addItem(item.akey, item.display);
		        }
			}
		}
		if(loadFromModel){
			editor.clear();
	        Iterator fieldIt = ((OptionField)field).getOptions().iterator();
	        while (fieldIt.hasNext()) {
	            OptionItem item = (OptionItem)fieldIt.next();
	            editor.addItem(item.akey, item.display);
	        }
		}
		editor.setValue(field.getValue());
        setTitle(field.getTip());
		setWidget(editor);
	}

	public void saveValue() {
        if(!enabled)
            return;
        if (editor.isMultipleSelect()){
            for (int i = 0; i < editor.getItemCount(); i++) {
                if (editor.isItemSelected(i))
                    ((OptionField)field).addValue(editor.getValue(i));
            }
        }else
            field.setValue(editor.getValue());
        display.setTitle(field.getTip());
        super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
	}
    
    public void setListener(ChangeListener listener){
       this.listener = listener;
    }

    public void enable(boolean enabled) {
      this.enabled = enabled;
        
    }
}
