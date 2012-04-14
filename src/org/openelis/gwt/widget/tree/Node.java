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
package org.openelis.gwt.widget.tree;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.Row;

public class Node extends Row {
    
    protected ArrayList<Node> children;
    protected Node parent;
    protected boolean isOpen,loaded = true;
    protected String type;
    protected Integer key;
    
    public Node(int columns) {
        super(columns);
    }
    
    public Node(Object... objects) {
    	super(objects);
    }
    
    public Node() {
        super();
    }
    
    public void setKey(Integer key) {
    	this.key = key;
    }
    
    public Integer getKey() {
    	return key;
    }
    
    public Node setType(String type) {
        this.type = type;
        return this;
    }
    
    public String getType() {
        return type;
    }
    
    public void add(Node node) {
        add(node,-1);
    }
    
    public void add(Node node, int index) {
        if(children == null) {
            children = new ArrayList<Node>();
            loaded = true;
        }
        children.add(index < 0 ? children.size() : index, node);
        node.setParent(this);
    }
    
    public ArrayList<Node> children() {
        return children;
    }
    
    public boolean hasChildren() {
        return children != null && children.size() > 0;
    }
    
    public Node getChildAt(int index) {
        return children.get(index);
    }
    
    public int getChildCount() {
        return children != null ? children.size() : 0;
    }
    
    public Node getFirstChild() {
        return children.get(0);
    }
    
    public int getIndex(Node node) {
        return children.indexOf(node);
    }
    
    public Node getLastChild() {
        return children.get(children.size()-1);
    }
    
    public int getLevel() {
        int level = 0;
        Node up = this;
        
        while(!up.isRoot()) {
            level++;
            up = up.getParent();
        }
        
        return level;
    }
    
    public Node getParent() {
        return parent;
    }
        
    public Node getRoot() {
        Node root;
        
        root = parent;
        while(!root.isRoot())
            root = root.getParent();
        
        return root;
    }
    
    public boolean isRoot() {
        return parent == null;
    }
    
    public boolean isLeaf() {
        return !hasChildren() && loaded;
    }
    
    public boolean isNodeAncestor(Node node) {
        
        if(node == this || parent == node)
            return true;
        
        if(!isRoot()) 
            return parent.isNodeAncestor(node);
        
        return false;
        
    }
    
    public boolean isNodeDescendent(Node node) {
        
        if(node == this || isNodeChild(node))
            return true;
        
        if(hasChildren()) { 
            for(Node child : children) {
                if(child.isNodeDescendent(node))
                    return true;
            }
        }   
        
        return false;
        
    }
    
    public boolean isNodeChild(Node node) {
        if(children != null)
            return children.contains(node);
        return false;
    }
    
    public void remove(int index) {
        children.remove(index);
    }
    
    public void remove(Node node) {
        children.remove(node);
    }
    
    public void removeAllChildren() {
        children = null;
    }
    
    public void removeFromParent() {
        if(parent != null) {
            parent.remove(this);
            parent = null;
        }
    }
    
    public void setParent(Node node) {
        parent = node;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void setOpen(boolean open) {
        isOpen = open;
    }
    
   public void setDeferLoadingUntilExpand(boolean defer){
        loaded = !defer;
    }
    
    public boolean isLoaded(){
        return loaded;
    }
    
    public String getImage() {
        return null;
    }
}
