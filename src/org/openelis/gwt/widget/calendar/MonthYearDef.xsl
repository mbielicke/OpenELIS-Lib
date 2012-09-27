
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
  extension-element-prefixes="resource"
  version="1.0"
  xmlns:locale="xalan://java.util.Locale"
  xmlns:resource="xalan://org.openelis.util.UTFResource"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xalan:component prefix="resource">
    <xalan:script lang="javaclass" src="xalan://org.openelis.util.UTFResource" />
  </xalan:component>
  <xalan:component prefix="locale">
    <xalan:script lang="javaclass" src="xalan://java.util.Locale" />
  </xalan:component>
  <xsl:template match="doc">
    <xsl:variable name="language">
      <xsl:value-of select="locale" />
    </xsl:variable>
    <xsl:variable name="constants" select="resource:getBundle('org.openelis.gwt.constants..LibraryConstants',locale:new(string($language)))" />
    <screen name="MonthYear" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <VerticalPanel class="libRes.calendar.CalendarWidget">
        <HorizontalPanel class="libRes.calendar.Calendar" width="100%">
          <AbsolutePanel>
            <TablePanel padding="0" spacing="0" width="100%">
              <row>
                <button key="month0" toggle="true">
                  <label key="month0Text" text="{resource:getString($constants,'cal.abrvMonth0')}" wordwrap="true" />
                </button>
                <button key="month6" toggle="true">
                  <label key="month6Text" text="{resource:getString($constants,'cal.abrvMonth6')}" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="month1" toggle="true">
                  <label key="month1Text" text="{resource:getString($constants,'cal.abrvMonth1')}" wordwrap="true" />
                </button>
                <button key="month7" toggle="true">
                  <label key="month7Text" text="{resource:getString($constants,'cal.abrvMonth7')}" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="month2" toggle="true">
                  <label key="month2Text" text="{resource:getString($constants,'cal.abrvMonth2')}" wordwrap="true" />
                </button>
                <button key="month8" toggle="true">
                  <label key="month8Text" text="{resource:getString($constants,'cal.abrvMonth8')}" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="month3" toggle="true">
                  <label key="month3Text" text="{resource:getString($constants,'cal.abrvMonth3')}" wordwrap="true" />
                </button>
                <button key="month9" toggle="true">
                  <label key="month9Text" text="{resource:getString($constants,'cal.abrvMonth9')}" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="month4" toggle="true">
                  <label key="month4Text" text="{resource:getString($constants,'cal.abrvMonth4')}" wordwrap="true" />
                </button>
                <button key="month10" toggle="true">
                  <label key="month10Text" text="{resource:getString($constants,'cal.abrvMonth10')}" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="month5" toggle="true">
                  <label key="month5Text" text="{resource:getString($constants,'cal.abrvMonth5')}" wordwrap="true" />
                </button>
                <button key="month11" toggle="true">
                  <label key="month11Text" text="{resource:getString($constants,'cal.abrvMonth11')}" wordwrap="true" />
                </button>
              </row>
            </TablePanel>
          </AbsolutePanel>
          <AbsolutePanel class="libRes.calendar.Divider"></AbsolutePanel>
          <AbsolutePanel>
            <TablePanel padding="0" spacing="0" width="100%">
              <row>
                <widget>
                  <button key="prevDecade" icon="libRes.calendar.prevNavIndex"/>
                </widget>
                <widget>
                  <button key="nextDecade" icon="libRes.calendar.nextNavIndex"/>
                </widget>
              </row>
              <row>
                <button key="year0" toggle="true">
                  <label key="year0Text" wordwrap="true" />
                </button>
                <button key="year5" toggle="true">
                  <label key="year5Text" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="year1" toggle="true">
                  <label key="year1Text" wordwrap="true" />
                </button>
                <button key="year6" toggle="true">
                  <label key="year6Text" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="year2" toggle="true">
                  <label key="year2Text" wordwrap="true" />
                </button>
                <button key="year7" toggle="true">
                  <label key="year7Text" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="year3" toggle="true">
                  <label key="year3Text" wordwrap="true" />
                </button>
                <button key="year8" toggle="true">
                  <label key="year8Text" wordwrap="true" />
                </button>
              </row>
              <row>
                <button key="year4" toggle="true">
                  <label key="year4Text" wordwrap="true" />
                </button>
                <button key="year9" toggle="true">
                  <label key="year9Text" wordwrap="true" />
                </button>
              </row>
            </TablePanel>
          </AbsolutePanel>
        </HorizontalPanel>
        <AbsolutePanel halign="center" layout="absolute" class="libRes.calendar.SelectBar">
          <HorizontalPanel layout="horizontal">
            <button action="ok" key="ok" text="OK"/>
            <button action="cancel" align="left" key="cancel" text="CANCEL"/>
          </HorizontalPanel>
        </AbsolutePanel>
      </VerticalPanel>
      <Resource field="libRes" source="org.openelis.gwt.resources.OpenELISResources"/>
    </screen>
  </xsl:template>
</xsl:stylesheet>
