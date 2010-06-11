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

  <xsl:template match="doc"> 
     <xsl:variable name="language">
      <xsl:value-of select="locale"/>
    </xsl:variable>
    <xsl:variable name="constants" select="resource:getBundle('org.openelis.gwt.server.CalendarConstants',locale:new(string($language)))"/>
  <screen name="MonthYear" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	  <VerticalPanel style="CalendarWidget">
	    <HorizontalPanel style="Calendar" width="100%">
	      <AbsolutePanel>
	      <TablePanel spacing="0" padding="0" width="100%">
	        <row>
	         <button key="month0" style="Button" toggle="true">
                <label key="month0Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth0')}"/>
             </button>
             <button key="month6" style="Button" toggle="true">
                <label key="month6Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth6')}"/>
             </button>
	      </row>
	      <row>
	         <button key="month1" style="Button" toggle="true">
                <label key="month1Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth1')}"/>
             </button>
	         <button key="month7" style="Button" toggle="true">
                <label key="month7Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth7')}"/>
             </button>             	      
	      </row>	     
	      <row>
	         <button key="month2" style="Button" toggle="true">
                <label key="month2Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth2')}"/>
             </button>	      
	         <button key="month8" style="Button" toggle="true">
                <label key="month8Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth8')}"/>
             </button>             
	      </row>	     
	      <row>
	         <button key="month3" style="Button" toggle="true">
                <label key="month3Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth3')}"/>
             </button>	      
              <button key="month9" style="Button" toggle="true">
                <label key="month9Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth9')}"/>
             </button>
	      </row>	     
	      <row>
	         <button key="month4" style="Button" toggle="true">
                <label key="month4Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth4')}"/>
             </button>	      
	         <button key="month10" style="Button" toggle="true">
                <label key="month10Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth10')}"/>
             </button>             
	      </row>	     
	      <row>
	         <button key="month5" style="Button" toggle="true">
                <label key="month5Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth5')}"/>
             </button>
	         <button key="month11" style="Button" toggle="true">
                <label key="month11Text" wordwrap="true" text="{resource:getString($constants,'abrvMonth11')}"/>
             </button>             	      
	      </row>	          
	    </TablePanel>
	    </AbsolutePanel>
	    <AbsolutePanel style="Divider">
	    </AbsolutePanel>
	    <AbsolutePanel>
	    <TablePanel spacing="0" padding="0" width="100%">
	      <row>
	        <widget>
    	        <button key="prevDecade" style="Button">
    	           <AbsolutePanel style="prevNavIndex"/>
    	        </button>
    	    </widget>
    	    <widget>
		        <button key="nextDecade" style="Button"> 
		           <AbsolutePanel style="nextNavIndex"/>
		        </button>
		    </widget>
	      </row>	      
	      <row>
	        <button key="year0" style="Button" toggle="true">
	          <label key="year0Text" wordwrap="true" text="2010"/>
	        </button> 
	        <button key="year5" style="Button" toggle="true">
	          <label key="year5Text" wordwrap="true" text="2015"/>
	        </button> 
	      </row>	 
	      <row>
	      	<button key="year1" style="Button"  toggle="true">
	          <label key="year1Text" wordwrap="true" text="2011"/>
	        </button> 
	        <button key="year6" style="Button"  toggle="true">
	          <label key="year6Text" wordwrap="true" text="2016"/>
	        </button> 	        
	      </row>		  
	      <row>
	        <button key="year2" style="Button" toggle="true">
	          <label key="year2Text" wordwrap="true" text="2012"/>
	        </button> 	      
	        <button key="year7" style="Button" toggle="true">
	          <label key="year7Text" wordwrap="true" text="2017"/>
	        </button> 	        
	      </row>		  
	      <row>
	        <button key="year3" style="Button" toggle="true">
	          <label key="year3Text" wordwrap="true" text="2013"/>
	        </button> 	      
	        <button key="year8" style="Button"  toggle="true">
	          <label key="year8Text" wordwrap="true" text="2018"/>
	        </button> 	        
	      </row>		  
	      <row>
	        <button key="year4" style="Button" toggle="true">
	          <label key="year4Text" wordwrap="true" text="2014"/>
	        </button> 	      
	        <button key="year9" style="Button" toggle="true">
	          <label key="year9Text" wordwrap="true" text="2019"/>
	        </button> 	        
	      </row>		                         
	    </TablePanel>
	    </AbsolutePanel>
	    </HorizontalPanel>
	    <AbsolutePanel layout="absolute" style="SelectBar" halign="center">
	    <HorizontalPanel layout="horizontal" >
	        <button action="ok" key="ok" style="Button">
	            <text>OK</text>
	        </button>
            <button action="cancel" key="cancel" align="left" style="Button">
	            <text>CANCEL</text>
            </button>
	    </HorizontalPanel>
	   </AbsolutePanel>
	  </VerticalPanel>
  </screen>
  
  </xsl:template>
  
</xsl:stylesheet>
