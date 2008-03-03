package org.openelis.test;

import static org.junit.Assert.*;

import org.openelis.util.XMLUtil;

import junit.framework.TestCase;

public class TestXMLUtil extends TestCase {

    public void testCreateNew() {
        try{
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><test/>", XMLUtil.toString(XMLUtil.createNew("test")));
        }catch(Exception e) {
            fail(e.getMessage());
        }
    }

}
