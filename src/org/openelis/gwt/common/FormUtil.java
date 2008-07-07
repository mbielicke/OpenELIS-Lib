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
* Copyright (C) OpenELIS.  All Rights Reserved.
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
    public static void create(FormRPC form, String xml) {
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
