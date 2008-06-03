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
