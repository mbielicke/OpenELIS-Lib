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
package org.openelis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLUtil {
	
	public static class MyResolver implements URIResolver {

		public Source resolve(String href, String base)
				throws TransformerException {
			if(href.startsWith("IMPORT")) {
				return new StreamSource(new File(System.getProperty("IMPORT")+href.substring(6)));
			}
			if(href.startsWith("OPENELIS"))
				return new StreamSource(new File(System.getProperty("OPENELIS")+href.substring(8)));
			return null;
		}
		
	}
    /**
     * This will return a new, empty Document object with only a root tag
     * matching the parameter sent in.
     * 
     * @param root
     * @return
     * @throws ParserConfigurationException
     */
    public static Document createNew(String root) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        docBuilder = dbf.newDocumentBuilder();
        return docBuilder.getDOMImplementation().createDocument(null,
                                                                root,
                                                                null);
    }

    /**
     * This method is used to convert a Document object into an XML String.
     * 
     * @param doc
     * @return
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static String toString(Document doc) throws TransformerConfigurationException,
                                               TransformerException {
        Transformer transformer = TransformerFactory.newInstance()
                                                    .newTransformer();
        DOMSource source = new DOMSource(doc);
        try {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
        
        return new String(output.toByteArray(),"UTF-8");
        }catch(Exception e){
            return null;
        }
    }

    /**
     * This will transform a Document object using the xsl styleSheet specified
     * by the File. The File Path should be absolute. The output of the
     * transformation will be sent to the stream result passed in as a
     * parameter.
     * 
     * @param doc
     * @param xsl
     * @param result
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static void transformXML(Document doc, File xsl, StreamResult result) throws TransformerConfigurationException,
                                                                                TransformerException, FileNotFoundException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(new StreamSource(xsl));
        DOMSource document = new DOMSource(doc);
        transformer.transform(document, result);
    }
    
    public static void transformXML(Document doc, InputStream xsl, StreamResult result) throws TransformerConfigurationException,
    																			TransformerException, FileNotFoundException {
    	TransformerFactory tf = TransformerFactory.newInstance();
    	tf.setURIResolver(new MyResolver());
    	Transformer transformer = tf.newTransformer(new StreamSource(xsl));
    	transformer.setURIResolver(new MyResolver());
    	DOMSource document = new DOMSource(doc);
    	//transformer.setParameter("importPath","/home/tschmidt/GWT2.0/workspace/OpenELIS-Lib/src/org/openelis/gwt/public/Forms/");
    	transformer.transform(document, result);
    }

    /**
     * This method will return a new Document object representing the XML loaded
     * from the url passed in as a parameter. Usually a file qualified with
     * file:// at the begining of the name.
     * 
     * @param url
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document load(String url) throws ParserConfigurationException,
                                           IOException,
                                           SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(url);
    }

    /**
     * This method will return a new Document object parsed from the XML String
     * passed in as a parameter.
     * 
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    public static Document parse(String xml) throws ParserConfigurationException,
                                            SAXException,
                                            IOException,
                                            Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * This method will validate the the XML String passed in afainst the Schema
     * Location passed in as a parameter. A handler must be passed in to handle
     * any validation errors. This method requires jdk1.5 in order to work.
     * 
     * @param xml
     * @param schemaURI
     * @param handler
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void validateXML(String xml,
                                   String schemaURI,
                                   DefaultHandler handler) throws IOException,
                                                          SAXException,
                                                          ParserConfigurationException {
        
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.XML_NS_URI);
            StreamSource ss = new StreamSource(schemaURI);
            Schema schema = sf.newSchema(ss);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setSchema(schema);
            spf.setNamespaceAware(true);
            javax.xml.parsers.SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xml.getBytes()), handler);
    }
    
}
