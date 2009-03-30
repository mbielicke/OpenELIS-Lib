/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.ResultsTable;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableModel;
import org.openelis.gwt.widget.table.TableWidget;

import java.util.ArrayList;
import java.util.Vector;

public class ScreenResultsTable extends ScreenInputWidget {
    public ResultsTable results;
    public ScreenTableWidget tableWidget;
    public static final String TAG_NAME = "resultsTable";
    public Vector<String> dropTargets;
    public boolean dropInited;
    public boolean queryable;
    
    public ScreenResultsTable() {
    }

    public ScreenResultsTable(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            results = (ResultsTable)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            results = new ResultsTable();
        NodeList children = node.getChildNodes();
        Node bpanel = null;
        Node table = null;
        Node query = null;
        for(int i = 0; i < children.getLength(); i++){
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if(children.item(i).getNodeName().equals("buttonPanel"))
                    bpanel = children.item(i);
                else if(children.item(i).getNodeName().equals("table"))
                    table = children.item(i);
                else if(children.item(i).getNodeName().equals("query"))
                    query = children.item(i);
            }
        }
        if(table != null){
            tableWidget = new ScreenTableWidget(table,screen);
            results.setTable((TableWidget)tableWidget.getWidget());
        }
        if(query != null){
            NodeList inputList = query.getChildNodes();
            Node input = null;
            for (int m = 0; m < inputList.getLength(); m++) {
                if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                    input = inputList.item(m);
                    m = 100;
                }
            }
            Widget queryWid = ScreenBase.createWidget(input, screen);
            setQueryWidget((ScreenInputWidget)queryWid);
        }
        if(node.getAttributes().getNamedItem("showNavPanel") != null) {
            if(node.getAttributes().getNamedItem("showNavPanel").getNodeValue().equals("false")){
                results.showNavPanel = false;
            }
        }
        if(node.getAttributes().getNamedItem("query") != null){
            if(node.getAttributes().getNamedItem("query").getNodeValue().equals("true")){
                queryable = true;
            }
        }
        displayWidget = results;

        if(bpanel != null)
            results.setButtonPanel(ScreenWidget.loadWidget(bpanel, screen));
        
        initWidget(results);        
        setDefaults(node, screen);
        
    }
    
    public void load(AbstractField field) {
        if(!queryMode){
            tableWidget.load(field);
         }else {
            queryWidget.load(field);
         }
    }

    public void submit(AbstractField field) {
        tableWidget.submit(field);
    }
   
    public void enable(boolean enabled){
        tableWidget.enable(enabled);
    }
    
    public void setForm(State state) {
        if(queryable){
            TableManager man = ((TableModel)tableWidget.table.model).manager;
            tableWidget.setForm(state);
            tableWidget.table.model.setManager(man);
        }
    }
    
    public void submitQuery(ArrayList<AbstractField> qList) {
        if(queryable)
            tableWidget.submitQuery(qList);
    }
   

}
