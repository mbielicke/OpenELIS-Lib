<!--
Exhibit A - UIRF Open-source Based Public Software License.
  
The contents of this file are subject to the UIRF Open-source Based
Public Software License(the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at
openelis.uhl.uiowa.edu
  
Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.
  
The Original Code is OpenELIS code.
  
The Initial Developer of the Original Code is The University of Iowa.
Portions created by The University of Iowa are Copyright 2006-2008. All
Rights Reserved.
  
Contributor(s): ______________________________________.
  
Alternatively, the contents of this file marked
"Separately-Licensed" may be used under the terms of a UIRF Software
license ("UIRF Software License"), in which case the provisions of a
UIRF Software License are applicable instead of those above. 
  -->

<xsl:stylesheet
  version="1.0"
  extension-element-prefixes="resource"
  xmlns:locale="xalan://java.util.Locale"
  xmlns:resource="xalan://org.openelis.util.UTFResource"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xsi:noNamespaceSchemaLocation="http://openelis.uhl.uiowa.edu/schema/ScreenSchema.xsd"
  xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://openelis.uhl.uiowa.edu/schema/XSLTSchema.xsd"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:service="xalan://org.openelis.gwt.server.ServiceUtils"
  xmlns:so="xalan://org.openelis.gwt.common.ModulePermission">

  <xsl:template match="doc">
    <screen name="Calendar">
      <HorizontalPanel style="WhiteContentPanel">
        <CollapsePanel key="collapsePanel" style="noButtons">
          <VerticalPanel>
            <TablePanel style="Form">
              <row>
                <text style="Prompt">Begin:</text>
                <dropdown key="begin" field="Integer" width="100"/>
              </row>
              <row>
                <text style="Prompt">End:</text>
                <dropdown key="end" field="Integer" width="100"/>
              </row>
              <row>
                <text style="Prompt">Enabled:</text>
                <check key="enabled"/>
              </row>
              <row>
                <text style="Prompt">Alignment:</text>
                <dropdown key="alignment" width="75"/>
              </row>
              <row>
                <text style="Prompt">Query Mode:</text>
                <check key="query"/>
              </row>
              <row>
                <text style="Prompt">Mask:</text>
                <textbox key="mask"/>
              </row>
              <row>
                <text style="Prompt">Pattern:</text>
                <textbox key="pattern"/>
              </row>
              <row>
                <text style="Prompt">Required:</text>
                <check key="required"/>
              </row>
              <row>
                <text style="Prompt">Value:</text>
                <textbox key="value"/>
              </row>
            </TablePanel>
          </VerticalPanel>
        </CollapsePanel>
        <AbsolutePanel>
          <TablePanel style="Form">
            <row>
              <text style="Prompt">Test:</text>
              <calendar begin="0" end="2" key="test"/>
            </row>
          </TablePanel>
        </AbsolutePanel>
       </HorizontalPanel>
    </screen>
  </xsl:template>
 
</xsl:stylesheet>
  