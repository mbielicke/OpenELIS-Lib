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
package org.openelis.gwt.test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.screen.deprecated.ScreenBase;
import org.openelis.gwt.screen.deprecated.ScreenText;

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
