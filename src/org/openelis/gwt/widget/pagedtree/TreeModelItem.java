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
