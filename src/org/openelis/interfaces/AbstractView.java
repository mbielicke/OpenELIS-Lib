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
