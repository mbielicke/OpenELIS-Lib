/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */

package org.openelis.gwt.common;

import java.io.Serializable;

/**
 * This class represents the structure of an option list entry
 */

public class OptionListItem implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String key, label;

    /**
     * A no-argument constructor is needed for serialization to work
     */
    protected OptionListItem() {
        
    }
    
    /**
     * Creates an item with a key and label suitable for being displayed in a
     * dropdown
     */
    public OptionListItem(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /**
     * Creates an item by parsing a key and label using "|" as a delimiter
     */
    public OptionListItem(String keyAndLabel) {
        int i;

        i = keyAndLabel.indexOf("|");
        if (i != -1) {
            this.key = keyAndLabel.substring(0, i);
            this.label = keyAndLabel.substring(i + 1);
        } else {
            this.key = keyAndLabel;
            this.label = keyAndLabel;
        }
    }

    /**
     * Returns the key for the item
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the label for the item
     */
    public String getLabel() {
        return label;
    }

    public String toString() {
        return key + "|" + label;
    }
}
