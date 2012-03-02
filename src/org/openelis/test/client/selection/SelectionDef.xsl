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
    <screen name="Selection">
      <HorizontalPanel style="WhiteContentPanel">
        <CollapsePanel key="collapsePanel" style="noButtons">
          <VerticalPanel>
            <TablePanel style="Form">
              <row>
                <text style="Prompt">Visible Items:</text>
                <textbox key="visibleItems" field="Integer"/>
              </row>
              <row>
                <text style="Prompt">Max Display Options:</text>
                <textbox key="maxDisplay" field="Integer"/>
              </row>
              <row>
                <text style="Prompt">Field:</text>
                <select key="field" width="75"/>
              </row>
              <row>
                <text style="Prompt">Enabled:</text>
                <check key="enabled"/>
              </row>
              <row>
                <text style="Prompt">Query Mode:</text>
                <HorizontalPanel>
                  <check key="query"/>
                  <button key="getQuery" icon="nothing" text="Query"/>
                </HorizontalPanel>
              </row>
              <row>
                <text style="Prompt">Required:</text>
                <check key="required"/>
              </row>
              <row>
                <text style="Prompt">MultiSelect:</text>
                <check key="multi"/>
              </row>
              <row>
                <text style="Prompt">CSS Class:</text>
                <textbox key="css"/>
              </row>
              <row>
                <text style="Prompt">Value:</text>
                <HorizontalPanel>
                  <textbox key="value"/>
                  <button key="setValue" text="Set" icon="nothing"/>
                </HorizontalPanel>
              </row>
              <row>
                <widget colspan="2">
                  <table rows="5" vscroll="ALWAYS" hscroll="ALWAYS" key="model">
                    <col header="Key" width="75">
                      <textbox required="true"/>
                    </col>
                    <col header="Display" width="100">
                      <textbox required="true"/>
                    </col>
                    <col header="Enabled" width="75">
                      <check/>
                    </col>
                  </table>
                </widget>
              </row>
              <row>
                <widget colspan="2" halign="center">
                  <HorizontalPanel>
                    <button key="setModel" style="Button" text="Set"/>
                    <button key="addRow" style="Button" text="Add"/>
                    <button key="removeRow" style="Button" text="Remove"/>
                  </HorizontalPanel>
                </widget>
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
              <dropselect key="test" width="100" tab="dummy2,dummy1"/>
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
  