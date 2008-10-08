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
package org.openelis.gwt.widget.pagedtree;

import java.io.Serializable;

public class TreeModelItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8989977247598557600L;
    
    private String text;    
    private String userObject;
    private boolean hasDummyChild;
    private String styleName;
    private String image;
    
    public TreeModelItem(){
        
    }
    
    public TreeModelItem(String text){
       this.text = text;  
    }
    
    public boolean getHasDummyChild() {
        return hasDummyChild;
    }
    public void setHasDummyChild(boolean hasDummyChild) {
        this.hasDummyChild = hasDummyChild;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getUserObject() {
        return userObject;
    }
    public void setUserObject(String userObject) {
        this.userObject = userObject;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    

}
