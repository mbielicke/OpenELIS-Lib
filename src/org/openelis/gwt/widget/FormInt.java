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
package org.openelis.gwt.widget;

/**
 * This interface is used by Screens that want to use a ButtonPanel 
 * to control the operations and state of the form.
 * @author tschmidt
 *
 */
public interface FormInt {
    
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE}

    public void query();

    public void next();

    public void prev();

    public void add();

    public void update();

    public void delete();

    public void commit();

    public void abort();
    
    public void reload();
    
    public void select();
    
    public boolean hasChanges();
    
}
