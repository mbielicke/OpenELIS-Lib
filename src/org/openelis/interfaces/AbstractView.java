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
/*
 * Created on May 3, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.interfaces;

import org.openelis.util.Datetime;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractView implements IView {
    private static Category log = Category.getInstance(AbstractView.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IView#displayPage(edu.uiowa.uhl.inmsp.interfaces.IRequest,
     *      edu.uiowa.uhl.inmsp.interfaces.AbstractForm)
     */
    protected String getString(Object obj) {
        if (obj == null)
            return "";
        if (obj instanceof Datetime)
            return ((Datetime)obj).toString();
        if (obj instanceof String)
            return ((String)obj);
        if (obj instanceof Integer)
            return ((Integer)obj).toString();
        if (obj instanceof Double)
            return ((Double)obj).toString();
        return "";
    }

    protected Document getNewDoc(String root) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return docBuilder.getDOMImplementation().createDocument(null,
                                                                root,
                                                                null);
    }

    protected String transXML(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance()
                                                        .newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
