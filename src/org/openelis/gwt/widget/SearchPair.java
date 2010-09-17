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
package org.openelis.gwt.widget;

/**
 * This Class is used to provide a comparable search and sort object for Dropdown Models 
 *
 */
public class SearchPair implements Comparable<SearchPair>{

    /*
     * Index of the entry into the model of Dropdown
     */
    public int    modelIndex;
    /*
     * Display string of the entry in the Dropdown model.
     */
    public String display;

    /**
     * Constructor the takes the index of the entry and the display of the entry
     * @param index
     * @param display
     */
    public SearchPair(int index, String display) {
        this.modelIndex = index;
        this.display = display;
    }

    /**
     * Method compares the display of the entry with display of the entry passed
     */
    public int compareTo(SearchPair o) {
        return display.compareTo(o.display);
    }
}
