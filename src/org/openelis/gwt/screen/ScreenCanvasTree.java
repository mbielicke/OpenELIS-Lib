package org.openelis.gwt.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.canvastree.CanvasTree;

public class ScreenCanvasTree extends ScreenInputWidget {
    
    private CanvasTree tree;
    
    public static final String TAG_NAME = "ctree";
    
    public ScreenCanvasTree() {
        
    }
    
    public ScreenCanvasTree(Node node, ScreenBase screen) {
        super(node);
        tree = new CanvasTree();
        initWidget(tree);
        setDefaults(node,screen);
    }
    
    public ScreenCanvasTree getInstance(Node node, ScreenBase screen) {
        return new ScreenCanvasTree(node,screen);
    }

}
