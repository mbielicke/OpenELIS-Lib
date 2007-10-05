package org.openelis.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class XMLUtil {
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
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(source, result);
        return result.getWriter().toString();
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
                                                                                TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(new StreamSource(xsl));
        DOMSource document = new DOMSource(doc);
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
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource ss = new StreamSource(schemaURI);
            Schema schema = sf.newSchema(ss);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setSchema(schema);
            spf.setNamespaceAware(true);
            javax.xml.parsers.SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xml.getBytes()), handler);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
