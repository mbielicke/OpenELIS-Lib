/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.xml.transform.stream.StreamResult;

import org.openelis.util.SessionManager;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServiceUtils {

    public static String props;

    public static String getXML(String url) throws Exception {
        try {
            Document doc = XMLUtil.createNew("doc");
            return getXML(url, doc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String getGeneratorXML(InputStream xsl, String props, String language) throws Exception {
        Document doc = XMLUtil.createNew("doc");
        String loc = "en";
        Element root = doc.getDocumentElement();
        if ( !language.equals("default"))
            loc = language;
        Element locale = doc.createElement("locale");
        locale.appendChild(doc.createTextNode(loc));
        root.appendChild(locale);
        Element propsEL = doc.createElement("props");
        propsEL.appendChild(doc.createTextNode(props));
        root.appendChild(propsEL);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(bytes);
        XMLUtil.transformXML(doc, xsl, result);
        return new String(bytes.toByteArray(), "UTF-8");
    }

    public static String getXML(String url, Document doc) throws Exception {
        try {
            String loc = "en";
            if (SessionManager.getSession().getAttribute("locale") != null)
                loc = (String)SessionManager.getSession().getAttribute("locale");
            Element root = doc.getDocumentElement();
            Element locale = doc.createElement("locale");
            locale.appendChild(doc.createTextNode(loc));
            root.appendChild(locale);
            Element propsEL = doc.createElement("props");
            propsEL.appendChild(doc.createTextNode(props));
            root.appendChild(propsEL);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bytes);
            XMLUtil.transformXML(doc, new File(url), result);
            return new String(bytes.toByteArray(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String getXML(InputStream is, Document doc) throws Exception {
        try {
            String loc = "en";
            if (SessionManager.getSession().getAttribute("locale") != null)
                loc = (String)SessionManager.getSession().getAttribute("locale");
            Element root = doc.getDocumentElement();
            Element locale = doc.createElement("locale");
            locale.appendChild(doc.createTextNode(loc));
            root.appendChild(locale);
            Element propsEL = doc.createElement("props");
            propsEL.appendChild(doc.createTextNode(props));
            root.appendChild(propsEL);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bytes);
            XMLUtil.transformXML(doc, is, result);
            return new String(bytes.toByteArray(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
