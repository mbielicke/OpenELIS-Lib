<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xalan="http://xml.apache.org/xalan" 
xmlns:resource="xalan://org.openelis.server.constants.UTFResource" 
xmlns:locale="xalan://java.util.Locale" 
extension-element-prefixes="resource" 
version="1.0">

<xalan:component prefix="resource">
	<xalan:script lang="javaclass" src="xalan://org.openelis.server.constants.UTFResource"/>
</xalan:component>
  
<xalan:component prefix="locale">
	<xalan:script lang="javaclass" src="xalan://java.util.Locale"/>
</xalan:component>
  
<xsl:variable name="language"><xsl:value-of select="locale"/></xsl:variable>
<xsl:variable name="constants" select="resource:getBundle('org.openelis.modules.main.server.constants.OpenELISConstants',locale:new(string($language)))"/>
    
<!-- query button template -->
<xsl:template name="queryButton">
	<appButton action="query" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="query">	
		<panel xsi:type="Panel" layout="horizontal">
		<panel xsi:type="Absolute" layout="absolute" style="QueryButtonImage"/>
	     	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"query")'/></text>
			</widget>
		</panel>
	</appButton>
</xsl:template>

<!-- previous button template -->
<xsl:template name="previousButton">
	<appButton action="prev" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="PreviousButtonImage"/>
	        <widget>
	           	<text><xsl:value-of select='resource:getString($constants,"previous")'/></text>
	        </widget>
	 	</panel>
	</appButton>	
</xsl:template>

<!-- next button template -->
<xsl:template name="nextButton">
	<appButton action="next" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="">
		 <panel xsi:type="Panel" layout="horizontal">
	  		<panel xsi:type="Absolute" layout="absolute" style="NextButtonImage"/>
	     	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"next")'/></text>
	     	</widget>
	 	</panel>
	</appButton>
</xsl:template>

<!-- update button template -->
<xsl:template name="updateButton">
	<appButton action="update" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="update">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="UpdateButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"update")'/></text>
	       	</widget>
	 	</panel>
	</appButton>
</xsl:template>

<!-- add button template -->
<xsl:template name="addButton">
	<appButton action="add" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="add">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AddButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"add")'/></text>
	        </widget>
	 	</panel>
	</appButton>
</xsl:template>

<!-- delete button template -->
<xsl:template name="deleteButton">
	<appButton action="delete" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="delete">
		<panel xsi:type="Panel" layout="horizontal">
	     	<panel xsi:type="Absolute" layout="absolute" style="DeleteButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"delete")'/></text>
	      	</widget>
	 	</panel>
	</appButton>
</xsl:template>

<!-- commit button template -->
<xsl:template name="commitButton">
<appButton action="commit" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="CommitButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"commit")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- abort button template -->
<xsl:template name="abortButton">
	<appButton action="abort" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AbortButtonImage"/>
	    	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"abort")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- popup commit button template -->
<xsl:template name="popupCommitButton">
<appButton action="commit" style="Button" enabledStates="default" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="CommitButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"commit")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- popup abort button template -->
<xsl:template name="popupAbortButton">
	<appButton action="abort" style="Button" enabledStates="default" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AbortButtonImage"/>
	    	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"abort")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- buttonpanel divider template -->
<xsl:template name="buttonPanelDivider">
	<panel xsi:type="Absolute" layout="absolute" style="ButtonDivider"/>
</xsl:template>

<!-- A to Z buttons template -->
<xsl:template name="aToZButton">
<xsl:param name="buttonsParam" />
 <widget>
	<appButton key="{string($buttonsParam)}" action="query:{string($buttonsParam)}" toggle="true" alwaysEnabled="true" style="smallButton" onclick="this">
    	<widget>
        	<text><xsl:value-of select="string($buttonsParam)"/></text>
     	</widget>
 	</appButton>
</widget>
</xsl:template>
</xsl:stylesheet>