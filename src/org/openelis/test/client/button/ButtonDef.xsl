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
    <screen name="Button">
      <HorizontalPanel style="WhiteContentPanel">
        <CollapsePanel key="collapsePanel" style="noButtons">
          <VerticalPanel>
            <TablePanel style="Form">
              <row>
                <text style="Prompt">Icon:</text>
                <textbox key="icon"/>
              </row>
              <row>
                <text style="Prompt">Text:</text>
                <textbox key="text"/>
              </row>
              <row>
                <text style="Prompt">Wrap:</text>
                <check key="wrap"/>
              </row>
              <row>
                <text style="Prompt">Enabled:</text>
                <check key="enabled"/>
              </row>
              <row>
                <text style="Prompt">Pressed:</text>
                <check key="pressed"/>
              </row>
              <row>
                <text style="Prompt">Locked:</text>
                <check key="locked"/>
              </row>
              <row>
                <text style="Prompt">Toggles:</text>
                <check key="toggles"/>
              </row>
              <row>
                <text style="Prompt">CSS Class:</text>
                <textbox key="css"/>
              </row>
            </TablePanel>
          </VerticalPanel>
        </CollapsePanel>
        <AbsolutePanel>
          <TablePanel style="Form">
            <row>
              <text style="Prompt">Dummy</text>
              <textbox key="dummy1" tab="test,dummy2" enabled="true"/>
            </row>
            <row>
              <text style="Prompt">Test:</text>
              <button key="test" icon="nothing" text="Button" tab="dummy2,dummy1"/>
            </row>
            <row>
              <text style="Prompt">Dummy</text>
              <textbox key="dummy2" tab="dummy1,test" enabled="true"/>
            </row>
          </TablePanel>
        </AbsolutePanel>
       </HorizontalPanel>
    </screen>
  </xsl:template>
 
</xsl:stylesheet>
  