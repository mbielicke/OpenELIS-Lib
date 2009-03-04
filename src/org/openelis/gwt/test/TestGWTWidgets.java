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
package org.openelis.gwt.test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenText;

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
