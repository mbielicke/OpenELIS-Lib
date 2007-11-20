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
public class ScreenTable extends ScreenInputWidget {
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
    public ScreenTable(Node node, ScreenBase screen) {
        super(node);
        table = new FormTable(node);
        initWidget(table);
        displayWidget = table;
        table.setStyleName("ScreenTable");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTable(node, screen);
    }

    public void load(AbstractField field) {
        if(!queryMode){
          //queryWidget.load(field);
        //else{
            
            if (field.getValue() != null)
                table.controller.setModel((TableModel)field.getValue());
            else{
                table.controller.model.reset();
                table.controller.reset();
                field.setValue(table.controller.model);
            }
        }
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(table.controller.model);       
    }

    public Widget getWidget() {
        return table;
    }
    
    public void destroy(){
        table = null;
        super.destroy();
    }
    
    public void setQueryWidget(ScreenInputWidget qWid){
        queryWidget = qWid;
        
    }
    

}
