/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
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
