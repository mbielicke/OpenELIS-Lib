package org.openelis.gwt.client.widget.pagedtree;

import java.io.Serializable;
import java.util.ArrayList;


public class TreeModel implements Serializable {
    
    private static final long serialVersionUID = -5412348933917626639L;
    public boolean paged;
    public int totalPages;
    public int itemsPerPage;    
    public int pageIndex;
    public int totalItems;
    public int shown;
    public boolean showIndex;
    public boolean autoAdd;
    
    /**
     * @gwt.typeArgs <java.lang.Boolean>
     */
    //private ArrayList hasDummyChild = new ArrayList();
    
    /**
     * @gwt.typeArgs <org.openelis.gwt.client.widget.pagedtree.TreeModelItem>
     */
     
    private ArrayList items = new ArrayList();
  
    //private ScreenTree tree = null;

    
    public TreeModelItem getItem(int index){
       return (TreeModelItem)items.get(index);
    } 
    
    public void addItem(TreeModelItem item){
        items.add(item);        
    }

    public int numItems(){
        return items.size();
    }
    
    public void deleteItem(int index){
        items.remove(index);
    }
    
    public void reset(){
        items = new ArrayList();
        totalItems = 0;
        shown = 0;        
    }

    
    
   
      
 }
