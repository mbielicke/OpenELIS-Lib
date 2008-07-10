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
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.util;

public interface Meta {

	public String[] getColumnList();

	public String getTable();
	
	public String getEntity();
	
	//this boolean tells the querybuilder whether to put the table inside the from statement or not.
	//Jboss will handle this for us if it is mapped in the entity and isnt a collection.
	public boolean includeInFrom();
	
	public boolean hasColumn(String columnName);
}
