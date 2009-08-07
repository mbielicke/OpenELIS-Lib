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
package org.openelis.gwt.common;


import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.common.data.TableField;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Deprecated
public abstract class Form<EntityKey> extends AbstractField<String> implements FieldType,RPC {

    private static final long serialVersionUID = 1L;
    
    public transient Node node;
    public String xml;
    public EntityKey entityKey;
    
    public enum Status {valid,invalid}
    public Status status;
    public boolean load;

    public Form() {
    }
    
    public Form(String key) {
        this.key = key;
    }
    
    public Form(Node node) {
        this.node = node;
        createFields(node);
    }
    
    public void createFields(Node node){
        this.node = node;
        HashMap<String,Node> nodeMap = createNodeMap(node);
        for (AbstractField field : getFields()){
            field.setAttributes(nodeMap.get(field.key));
            
            if("table".equals(nodeMap.get(field.key).getNodeName()) &&
                nodeMap.get(field.key).hasChildNodes()){
                NodeList fieldList = nodeMap.get(field.key).getChildNodes();
                
                TableDataRow<? extends Object> row = null;
                if(node.getAttributes().getNamedItem("class") != null){
                    String rowClass = node.getAttributes().getNamedItem("class").getNodeValue();
                    row = (TableDataRow<? extends Object>)ClassFactory.forName(rowClass);
                    
                }else
                    row = new TableDataRow<Integer>(fieldList.getLength());
                List<FieldType> cells = row.getCells();
                for (int i = 0; i < fieldList.getLength(); i++) {
                    if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if(cells.size() > i && cells.get(i) != null) {
                            ((AbstractField)cells.get(i)).setAttributes(fieldList.item(i));
                        }else{
                            AbstractField cellField = (ScreenBase.createField(fieldList.item(i)));
                            cells.set(i,(FieldType)cellField);
                        }
                    }
                }
                ((TableField)field).defaultRow = row;
            }
        }
        key = node.getAttributes().getNamedItem("key").getNodeValue();
        if(node.getAttributes().getNamedItem("load") != null){
            if(node.getAttributes().getNamedItem("load").getNodeValue().equals("true"))
                load = true;
        }
    }
    
    public abstract AbstractField[] getFields();
    
    public void setAttributes(Node node) {
        this.node = node;
        createFields(node);
    }
    
    public void addError(String err) {
        if(errors == null)
            errors = new ArrayList<String>();
    	errors.add(err);
    }
    
    public void validate() {
        // TODO Auto-generated method stub
        boolean valid = true;
        for (AbstractField field  : getFields()) {
            field.clearErrors();
            field.validate();
            if (!field.isValid()) {
                status = Status.invalid;
            }
        }
        if (status == Status.invalid)
            return;
        status = Status.valid;
        return;
    }

    public void removeErrors() {
        status = Status.valid;
        errors = null;
        for (AbstractField field : getFields()) {
            field.clearErrors();
        }
    }
    
    public HashMap<String,Node> createNodeMap(Node node) {
        HashMap<String,Node> nmap = new HashMap<String,Node>();
        NodeList fields = node.getChildNodes();
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i).getNodeType() == Node.ELEMENT_NODE){
                String key = fields.item(i).getAttributes().getNamedItem("key").getNodeValue();
                nmap.put(key, fields.item(i));
            }
        }
        return nmap;
    }
}
