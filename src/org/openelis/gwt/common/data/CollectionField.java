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
package org.openelis.gwt.common.data;


import com.google.gwt.xml.client.Node;

import java.util.ArrayList;

/**
 * A CollectionField wraps an ArrayList<Data> to be used in communication between
 * a client and the server.  It extends AbstractField and can be used in the FormRPC.  
 * @author tschmidt
 *
 */

@Deprecated public class CollectionField  extends AbstractField<ArrayList<DataObject>>  {

    private static final long serialVersionUID = 1L;
    private ArrayList<DataObject> coll = new ArrayList<DataObject>();
    private String type = "";
    public static final String TAG_NAME = "rpc-collection";

    /**
     * Default constructor
     *
     */
    public CollectionField() {
        
    }
    
    /**
     * Contstructor that accepts a XML definition for this field to set it's
     * member fields and value.
     * @param node
     */
    public CollectionField(Node node){
        setAttributes(node);
    }
    
    public void setAttributes(Node node){
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        /*
        if (node.getAttributes().getNamedItem("type") != null)
            setType(node.getAttributes()
                               .getNamedItem("type")
                               .getNodeValue());
        */                       
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
    }
    
    /**
     * This method will be called when the screen is validated before it is 
     * submitted to the server.
     */
    public void validate() {
        valid = true;
        if (required) {
            if (coll.size() == 0) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        for(DataObject data : coll) {
            if(data instanceof AbstractField){
                ((AbstractField)data).validate();
                if(!((AbstractField)data).valid)
                    valid = false;
            }
        }
        
    }

    /**
     * This method is hard coded to true for a CollectionField
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    
    /**
     * This method will add the item passed to the end of this
     * fields current list
     * @param item
     */
    public void addItem(DataObject item) {
        coll.add(item);
    }

    /**
     * This method creates a new object and sets it's member fields
     * and values to the calling object
     */
    public Object clone() {
        CollectionField obj = new CollectionField();
        obj.setRequired(required);
       // obj.setType(type);
        obj.setValue(value);
        obj.setKey(key);
        return obj;
    }
    
    /* I don't think this used anymore
    public CollectionField getInstance(Node node) {
        return new CollectionField(node);
    }
    */


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    /**
     * This method will return a comma seprated list of all values in 
     * the current list
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < coll.size(); i++){
            if(i > 0)
                sb.append(",");
            sb.append(((StringObject)coll.get(i)).getValue());
        }
        return sb.toString();
    }
}
