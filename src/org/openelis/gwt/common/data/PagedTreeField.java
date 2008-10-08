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

import org.openelis.gwt.widget.pagedtree.TreeModel;

import com.google.gwt.xml.client.Node;

public class PagedTreeField extends AbstractField {


	private static final long serialVersionUID = 1L;

	    private TreeModel value;
        
        public static final String TAG_NAME = "rpc-tree";
        
        public PagedTreeField() {
            
        }
        
        public PagedTreeField(Node node){
            setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        }
	    
	    public void validate() {
	        // TODO Auto-generated method stu
	    }

	    public boolean isInRange() {
	        // TODO Auto-generated method stub
	        return true;
	    }

	    public void setValue(Object val) {
	        // TODO Auto-generated method stub
	        if(val == null)
	            value = null;
	        else
	            value = (TreeModel)val;
	    }

	    public Object getValue() {
	        // TODO Auto-generated method stub
	        return value;
	    }

	    public PagedTreeField getInstance() {
	        // TODO Auto-generated method stub
	        return null;
	    }

	    public PagedTreeField getInstance(Node node) {
	        return new PagedTreeField(node);
	    }
	    

}
