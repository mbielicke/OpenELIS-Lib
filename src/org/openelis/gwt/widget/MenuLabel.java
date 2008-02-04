package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
/**
 * MenuLabel is a widget that displays text with an image in front of it.
 * The default image is a red bullet, but this can be overridden to be any
 * image.
 * @author tschmidt
 *
 */
public class MenuLabel extends Composite {

    private HorizontalPanel hp = new HorizontalPanel();
    private Image imagePanel = new Image("Images/navBullet.gif");
    private Label label = new Label();

    public MenuLabel() {
        initWidget(hp);
        hp.add(imagePanel);
        hp.add(label);
    }

    /** 
     * Pass an url for an image to this constructor to replace the default 
     * red bullet.
     * @param text
     * @param image
     */
    public MenuLabel(String text, String image) {
        initWidget(hp);
        if(image != null)
            imagePanel.setUrl(image);
        label.setText(text);
        hp.add(imagePanel);
        hp.add(label);
    }


    public void setImage(String image){
        imagePanel.setUrl(image);
    }
    
    public void setText(String text) {
        label.setText(text);
    }
    
    public void addClickListener(ClickListener listener){
        label.addClickListener(listener);
    }
    
    public Label getLabel(){
        return label;
    }

}
