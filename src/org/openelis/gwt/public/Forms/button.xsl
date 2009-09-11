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
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xalan="http://xml.apache.org/xalan" 
xmlns:resource="xalan://org.openelis.util.UTFResource" 
xmlns:locale="xalan://java.util.Locale" 
version="1.0">

<xalan:component prefix="resource">
	<xalan:script lang="javaclass" src="xalan://org.openelis.util.UTFResource"/>
</xalan:component>
  
<xalan:component prefix="locale">
	<xalan:script lang="javaclass" src="xalan://java.util.Locale"/>
</xalan:component>
  
<xsl:variable name="language"><xsl:value-of select="doc/locale"/></xsl:variable>
<xsl:variable name="props"><xsl:value-of select="doc/props"/></xsl:variable>
<xsl:variable name="constants" select="resource:getBundle(string($props),locale:new(string($language)))"/>
    
<!-- query button template -->
<xsl:template name="queryButton">
	<appButton key="query" action="query" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="query" shortcut="ctrl+q">	
		<HorizontalPanel>
		  <AbsolutePanel style="QueryButtonImage" width="20px" height="20px"/>
          <text><xsl:value-of select='resource:getString($constants,"query")'/></text>
		</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- previous button template -->
<xsl:template name="previousButton">
	<appButton key="previous" action="previous" style="ButtonPanelButton" enabledStates="display" lockedStates="" shortcut="ctrl+p">
		<HorizontalPanel>
	    	<AbsolutePanel style="PreviousButtonImage" width="20px" height="20px"/>
           	<text><xsl:value-of select='resource:getString($constants,"previous")'/></text>
	 	</HorizontalPanel>
	</appButton>	
</xsl:template>

<!-- next button template -->
<xsl:template name="nextButton">
	<appButton key="next" action="next" style="ButtonPanelButton" enabledStates="display" lockedStates="" shortcut="ctrl+n">
		 <HorizontalPanel>
	  		<AbsolutePanel style="NextButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"next")'/></text>
	 	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- update button template -->
<xsl:template name="updateButton">
	<appButton key="update" action="update" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="update" shortcut="ctrl+u">
		<HorizontalPanel>
	    	<AbsolutePanel style="UpdateButtonImage" width="20px" height="20px"/>
         	<text><xsl:value-of select='resource:getString($constants,"update")'/></text>
	 	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- add button template -->
<xsl:template name="addButton">
	<appButton key="add" action="add" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="add" shortcut="ctrl+a">
		<HorizontalPanel>
	    	<AbsolutePanel style="AddButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"add")'/></text>
	 	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- delete button template -->
<xsl:template name="deleteButton">
	<appButton key="delete" action="delete" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="delete" shortcut="ctrl+d">
		<HorizontalPanel>
	     	<AbsolutePanel style="DeleteButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"delete")'/></text>
	 	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- commit button template -->
<xsl:template name="commitButton">
    <appButton key="commit" action="commit" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="" shortcut="ctrl+m">
		<HorizontalPanel>
	    	<AbsolutePanel style="CommitButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"commit")'/></text>
	  	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- process button template -->
<xsl:template name="processButton">
    <appButton key="process" action="add" style="ButtonPanelButton" enabledStates="display" lockedStates="add" shortcut="ctrl+a">
		<HorizontalPanel>
	    	<AbsolutePanel style="CommitButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"process")'/></text>
	  	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- abort button template -->
<xsl:template name="abortButton">
	<appButton key="abort" action="abort" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="" shortcut="ctrl+o">
		<HorizontalPanel>
	    	<AbsolutePanel style="AbortButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"abort")'/></text>
	  	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- options button template -->
<xsl:template name="optionsButton">
	<menuPanel key="optionsMenu" layout="vertical" style="topBarItemHolder">
	    <menuItem>
	        <menuDisplay>
		    	<appButton action="option" style="ButtonPanelButton">
					<HorizontalPanel>
		        		<text><xsl:value-of select='resource:getString($constants,"options")'/></text>
			    		<AbsolutePanel style="OptionsButtonImage" width="20px" height="20px"/>
				  	</HorizontalPanel>
				</appButton>
			</menuDisplay>
			<menuPanel style="topMenuContainer" layout="vertical" position="below">
			    <xsl:call-template name="menuItem">
				    <xsl:with-param name="key">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="label">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="enabled">true</xsl:with-param>
				      <xsl:with-param name="enabledStates">display</xsl:with-param>
				      <xsl:with-param name="class">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="args"></xsl:with-param>
				    </xsl:call-template>
				  </menuPanel>
		    </menuItem>
	</menuPanel>
</xsl:template>

<!-- popup transfer button template -->
<xsl:template name="popupTransferButton">
<appButton key="popupTransfer" action="commit" style="Button" enabledStates="default" lockedStates="">
		<HorizontalPanel>
	    	<AbsolutePanel style="CommitButtonImage" width="20px" height="20px"/>
        	<text><xsl:value-of select='resource:getString($constants,"transfer")'/></text>
	  	</HorizontalPanel>
	</appButton>
</xsl:template>

<!-- buttonpanel divider template -->
<xsl:template name="buttonPanelDivider">
	<AbsolutePanel style="ButtonDivider"/>
</xsl:template>

<!-- A to Z buttons template -->
<xsl:template name="aToZButton">
<xsl:param name="keyParam" />
<xsl:param name="queryParam" />
	<appButton key="{string($keyParam)}" action="query:{string($queryParam)}" toggle="true" alwaysEnabled="true" enabledStates="default,display" lockedStates="add,query,update,delete" style="smallButton">
       	<text><xsl:value-of select="string($keyParam)"/></text>
 	</appButton>
</xsl:template>

<!-- Menu item template -->
 <xsl:template name="menuItem">
  	<xsl:param name="key"/>
    <xsl:param name="label"/>
    <xsl:param name="class"/>
    <xsl:param name="args"/>
    <xsl:param name="enabled"/>
    <xsl:param name="enabledStates"/>
    <xsl:variable name="descrip"><xsl:value-of select="$label"/>Description</xsl:variable>
  	<menuItem key="{$key}" style="TopMenuRowContainer" enabled="{$enabled}"  
	          hover="Hover"
	          icon="{$label}Icon"
	   		  label="{resource:getString($constants,$label)}"
	          description="{resource:getString($constants,$descrip)}" 
	          class="{$class}"
			  args="{$args}"
			  enabledStates="{$enabledStates}"
	          onClick="this"/>
  </xsl:template>
</xsl:stylesheet>
