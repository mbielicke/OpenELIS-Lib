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
    <screen name="Table">
      <HorizontalPanel style="WhiteContentPanel">
        <CollapsePanel key="collapsePanel" style="noButtons">
          <VerticalPanel>
            <TablePanel style="Form">
              <row>
                <text style="Prompt">Rows:</text>
                <textbox key="rows" field="Integer"/>
              </row>
              <row>
                <text style="Prompt">Row Height:</text>
                <textbox key="rowHeight" field="Integer"/>
              </row>
              <row>
                <text style="Prompt">Width:</text>
                <textbox key="width" field="Integer"/>
              </row>
              <row>
                <text style="Prompt">Enabled:</text>
                <check key="enabled"/>
              </row>
              <row>
                <text style="Prompt">MultiSelect:</text>
                <check key="multiSelect"/>
              </row>
              <row>
                <text style="Prompt">Query Mode:</text>
                <check key="query"/>
              </row>
              <row>
                <text style="Prompt">Has Header:</text>
                <check key="hasHeader"/>
              </row>
              <row>
                <text style="Prompt">Fix Scroll:</text>
                <check key="fixScroll"/>
              </row>
              <row>
                <text style="Prompt">VScroll:</text>
                <dropdown key="vscroll" width="100"/>
              </row>
              <row>
                <text style="Prompt">HScroll:</text>
                <dropdown key="hscroll" width="100"/>
              </row>
              <row>
                <widget colspan="2">
                  <table key="columns" rows="5" vscroll="ALWAYS" hscroll="ALWAYS">
                    <col header="Label" width="75">
                      <textbox/>
                    </col>
                    <col header="Width" width="75">
                      <textbox field="Integer"/>
                    </col>
                    <col header="Widget" width="100">
                      <dropdown width="100"/>
                    </col>
                    <col header="Filterable" width="30">
                      <check/>
                    </col>
                    <col header="Sortable" width="30">
                      <check/>
                    </col>
                    <col header="Resizable" width="30">
                      <check/>
                    </col>
                    <col header="Required" width="30">
                      <check/>
                    </col>
                    <col header="Enabled" width="30">
                      <check/>
                    </col>
                  </table>
                </widget>
              </row>
              <row>
                <widget colspan="2">
                  <HorizontalPanel>
                    <button style="Button" key="set" text="Set"/>
                    <button style="Button" key="add" text="Add"/>
                    <button style="Button" key="remove" text="Remove"/>
                  </HorizontalPanel>
                </widget>
              </row>
            </TablePanel>
          </VerticalPanel>
        </CollapsePanel>
        <AbsolutePanel>
          <TablePanel style="Form">
            <row>
              <table key="test" rows="10" style="ScreenTableWithSides">
                <col header="Col 1" width="100"/>
              </table>
            </row>
            <row>
              <HorizontalPanel>
                <button key="addRow" style="Button" text="Add Row"/>
                <button key="removeRow" style="Button" text="Remove Row"/>
              </HorizontalPanel>
            </row>
          </TablePanel>
        </AbsolutePanel>
      </HorizontalPanel>
    </screen>
  </xsl:template>
 
</xsl:stylesheet>          