package org.openelis.gwt.common.data;

import java.io.Serializable;

public class ModelObject implements DataObject, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected DataModel value;

	public ModelObject(){
		
	}
	
	public ModelObject(DataModel val){
		setValue(val);
	}
	
	public Object getInstance() {
		ModelObject clone = new ModelObject();
        clone.setValue(value);
        return clone;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = (DataModel)value;
	}

}
