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
package org.openelis.gwt.common;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;

public class FormUtil {
    public static void create(Form form, String xml) {
        try {
            Document formDoc = XMLParser.parse(xml);
            NodeList rpcList = formDoc.getDocumentElement()
                                      .getElementsByTagName("rpc");
            Element rpc = (Element)rpcList.item(0);
            NodeList fieldList = rpc.getChildNodes();
            for (int i = 0; i < fieldList.getLength(); i++) {
                if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    AbstractField field = ScreenBase.createField(fieldList.item(i));
                    form.getFieldMap().put((String)field.getKey(), field);
                }
            }
        } catch (Exception e) {
            Window.alert("FormUtil: " + e.getMessage());
        }
    }

}
