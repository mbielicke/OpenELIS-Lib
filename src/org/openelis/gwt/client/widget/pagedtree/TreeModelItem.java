package org.openelis.gwt.client.widget.pagedtree;

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
