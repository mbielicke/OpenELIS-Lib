package org.openelis.gwt.test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.client.screen.ScreenBase;
import org.openelis.gwt.client.screen.ScreenText;

public class TestGWTWidgets extends GWTTestCase {
    
    public String getModuleName() {
        return "org.openelis.gwt.TestWidgets";
    }
    
    public void testScreenText() {
        String nodeString = "<textbox key=\"test\" width=\"100px\"/>";    
        ScreenBase screen = new ScreenBase();
        assertNotNull(new ScreenText((Node)XMLParser.parse(nodeString).getDocumentElement(),screen));
        
    }

}
