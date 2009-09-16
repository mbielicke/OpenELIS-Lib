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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xalan"
                xmlns:cal="xalan://java.util.Calendar"
                xmlns:calUtils="xalan://org.openelis.util.CalendarUtils"
                xmlns:resource="xalan://org.openelis.util.UTFResource"
                xmlns:locale="xalan://java.util.Locale"
                extension-element-prefixes="resource" 
                version="1.0">

  <xalan:component prefix="resource">
    <xalan:script lang="javaclass" src="xalan://org.openelis.util.UTFResource"/>
  </xalan:component>
  <xalan:component prefix="locale">
    <xalan:script lang="javaclass" src="xalan://java.util.Locale"/>
  </xalan:component>

  <xsl:variable name="month"><xsl:value-of select="doc/month"/></xsl:variable>
  <xsl:variable name="year"><xsl:value-of select="doc/year"/></xsl:variable>
  <xsl:variable name="yearCell"><xsl:value-of select="doc/yearCell"/></xsl:variable>


  <xsl:template match="doc"> 
     <xsl:variable name="language">
      <xsl:value-of select="locale"/>
    </xsl:variable>
    <xsl:variable name="constants" select="resource:getBundle('org.openelis.gwt.server.CalendarConstants',locale:new(string($language)))"/>
  <screen name="MonthYear" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	  <VerticalPanel width="100%" style="CalendarWidget">
	    <HorizontalPanel style="Calendar" width="100%">
	      <TablePanel spacing="0" padding="0" width="100%">
	        <row>
	         <icon key="month0" style="MYCell">
                <label key="month0Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth0')}"/>
             </icon>
             <icon key="month6" style="MYCell">
                <label key="month6Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth6')}"/>
             </icon>
	      </row>
	      <row>
	         <icon key="month1" style="MYCell">
                <label key="month1Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth1')}"/>
             </icon>
	         <icon key="month7" style="MYCell">
                <label key="month7Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth7')}"/>
             </icon>             	      
	      </row>	     
	      <row>
	         <icon key="month2" style="MYCell">
                <label key="month2Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth2')}"/>
             </icon>	      
	         <icon key="month8" style="MYCell">
                <label key="month8Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth8')}"/>
             </icon>             
	      </row>	     
	      <row>
	         <icon key="month3" style="MYCell">
                <label key="month3Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth3')}"/>
             </icon>	      
              <icon key="month9" style="MYCell">
                <label key="month9Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth9')}"/>
             </icon>
	      </row>	     
	      <row>
	         <icon key="month4" style="MYCell">
                <label key="month4Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth4')}"/>
             </icon>	      
	         <icon key="month10" style="MYCell">
                <label key="month10Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth10')}"/>
             </icon>             
	      </row>	     
	      <row>
	         <icon key="month5" style="MYCell">
                <label key="month5Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth5')}"/>
             </icon>
	         <icon key="month11" style="MYCell">
                <label key="month11Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth11')}"/>
             </icon>             	      
	      </row>	          
	    </TablePanel>
	    <TablePanel spacing="0" padding="0" width="100%">
	      <row>
	        <widget>
    	        <icon key="prevDecade" style="prevNavIndex"/>
    	    </widget>
    	    <widget>
		        <icon key="nextDecade" style="nextNavIndex"/>
		    </widget>
	      </row>	      
	      <row>
	        <icon key="year0" style="MyCell">
	          <label key="year0Text" wordwrap="true"/>
	        </icon> 
	        <icon key="year5" style="MyCell">
	          <label key="year5Text" wordwrap="true"/>
	        </icon> 
	      </row>	 
	      <row>
	      	<icon key="year1" style="MyCell">
	          <label key="year1Text" wordwrap="true"/>
	        </icon> 
	        <icon key="year6" style="MyCell">
	          <label key="year6Text" wordwrap="true"/>
	        </icon> 	        
	      </row>		  
	      <row>
	        <icon key="year2" style="MyCell">
	          <label key="year2Text" wordwrap="true"/>
	        </icon> 	      
	        <icon key="year7" style="MyCell">
	          <label key="year7Text" wordwrap="true"/>
	        </icon> 	        
	      </row>		  
	      <row>
	        <icon key="year3" style="MyCell">
	          <label key="year3Text" wordwrap="true"/>
	        </icon> 	      
	        <icon key="year8" style="MyCell">
	          <label key="year8Text" wordwrap="true"/>
	        </icon> 	        
	      </row>		  
	      <row>
	        <icon key="year4" style="MyCell">
	          <label key="year4Text" wordwrap="true"/>
	        </icon> 	      
	        <icon key="year9" style="MyCell">
	          <label key="year9Text" wordwrap="true"/>
	        </icon> 	        
	      </row>		                         
	    </TablePanel>
	    </HorizontalPanel>
	    <AbsolutePanel layout="absolute" style="SelectBar" halign="center">
	    <HorizontalPanel layout="horizontal" >
	        <appButton action="ok" key="ok">
	            <text>OK</text>
	        </appButton>
            <appButton action="cancel" key="cancel" align="left">
	            <text>CANCEL</text>
            </appButton>
	    </HorizontalPanel>
	   </AbsolutePanel>
	  </VerticalPanel>
  </screen>
  
  </xsl:template>
  
</xsl:stylesheet>