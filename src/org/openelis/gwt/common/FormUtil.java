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
