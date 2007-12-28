package org.openelis.gwt.common.data;

import org.openelis.gwt.client.widget.pagedtree.TreeModel;

import com.google.gwt.xml.client.Node;

public class PagedTreeField extends AbstractField {


	private static final long serialVersionUID = 1L;
		/**
	     * @gwt.typeArgs <org.openelis.gwt.common.data.TableModel>
	     */
	    private TreeModel value;
	    
	    public boolean isValid() {
	        // TODO Auto-generated method stub
	        return true;
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

	    public Object getInstance() {
	        // TODO Auto-generated method stub
	        return null;
	    }

	    public Object getInstance(Node field) {
	        PagedTreeField pagedTree = new PagedTreeField();
	        pagedTree.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
	        return pagedTree;
	    }
	    

}
