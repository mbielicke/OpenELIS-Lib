package org.openelis.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openelis.util.XMLUtil;

public class TestXMLUtil {

    @Test
    public void testCreateNew() {
        try{
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><test/>", XMLUtil.toString(XMLUtil.createNew("test")));
        }catch(Exception e) {
            fail(e.getMessage());
        }
    }

}
