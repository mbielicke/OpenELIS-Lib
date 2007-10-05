package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.FormTable;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.TableModel;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTable extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "table";
	/**
	 * Widget wrapped by this class
	 */
    private FormTable table;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTable() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * 
     * 
     * @param node
     * @param screen
     */	
    public ScreenTable(Node node, Screen screen) {
        super(node);
        if (screen.constants != null) {
            table = new FormTable(node, screen.constants);
        } else{
            table = new FormTable(node);
        }
        initWidget(table);
        table.setStyleName("ScreenTable");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenTable(node, screen);
    }

    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        if (field.getValue() != null)
            table.controller.setModel((TableModel)field.getValue());
        else{
            table.controller.model.reset();
            table.controller.reset();
            field.setValue(table.controller.model);
        }
    }

    public void submit(AbstractField field) {
        // TODO Auto-generated method stub
        field.setValue(table.controller.model);
    }

    public Widget getWidget() {
        return table;
    }

}
