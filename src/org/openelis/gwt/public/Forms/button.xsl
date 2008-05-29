 <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xalan="http://xml.apache.org/xalan" 
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
  
<xsl:variable name="language"><xsl:value-of select="doc/locale"/></xsl:variable>
<xsl:variable name="props"><xsl:value-of select="doc/props"/></xsl:variable>
<xsl:variable name="constants" select="resource:getBundle(string($props),locale:new(string($language)))"/>
    
<!-- query button template -->
<xsl:template name="queryButton">
	<appButton action="query" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="query" shortcut="q">	
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
	<appButton action="prev" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="" shortcut="p">
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
	<appButton action="next" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="" shortcut="n">
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
	<appButton action="update" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="update" shortcut="u">
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
	<appButton action="add" toggle="true" style="ButtonPanelButton" enabledStates="default,display" lockedStates="add" shortcut="a">
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
	<appButton action="delete" toggle="true" style="ButtonPanelButton" enabledStates="display" lockedStates="delete" shortcut="d">
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
<appButton action="commit" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="" shortcut="c">
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
	<appButton action="abort" style="ButtonPanelButton" enabledStates="query,update,add,delete" lockedStates="" shortcut="x">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AbortButtonImage"/>
	    	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"abort")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- options button template -->
<xsl:template name="optionsButton">
	<menuPanel key="optionsMenu" layout="horizontal" xsi:type="Panel" style="topBarItemHolder" spacing="0" padding="0">
		    <menuItem>
		        <menuDisplay>
			    	  <appButton action="" style="ButtonPanelButton">
		<panel xsi:type="Panel" layout="horizontal">
			<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"options")'/></text>
	      	</widget>
	    	<panel xsi:type="Absolute" layout="absolute" style="OptionsButtonImage"/>
	  	</panel>
	</appButton>
				</menuDisplay>
				  <menuPanel style="topMenuContainer" layout="vertical" xsi:type="Panel" position="below">
				    <xsl:call-template name="menuItem">
				    <xsl:with-param name="key">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="label">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="enabled">true</xsl:with-param>
				      <xsl:with-param name="class">duplicateRecord</xsl:with-param>
				      <xsl:with-param name="args"></xsl:with-param>
				    </xsl:call-template>
				  </menuPanel>
		    </menuItem>
	</menuPanel>
</xsl:template>

<!-- popup select button template -->
<xsl:template name="popupSelectButton">
<appButton action="commit" style="Button" enabledStates="default" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="CommitButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"select")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:template>

<!-- popup cancel button template -->
<xsl:template name="popupCancelButton">
	<appButton action="abort" style="Button" enabledStates="default" lockedStates="">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AbortButtonImage"/>
	    	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"cancel")'/></text>
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
<xsl:param name="keyParam" />
<xsl:param name="queryParam" />
 <widget>
	<appButton key="{string($keyParam)}" action="query:{string($queryParam)}" toggle="true" alwaysEnabled="true" style="smallButton">
    	<widget>
        	<text><xsl:value-of select="string($keyParam)"/></text>
     	</widget>
 	</appButton>
</widget>
</xsl:template>

<!-- Menu item template -->
 <xsl:template name="menuItem">
  	<xsl:param name="key"/>
    <xsl:param name="label"/>
    <xsl:param name="class"/>
    <xsl:param name="args"/>
    <xsl:param name="enabled"/>
    <xsl:variable name="descrip"><xsl:value-of select="$label"/>Description</xsl:variable>
  	<menuItem key="{$key}" style="TopMenuRowContainer" enabled="{$enabled}"  
	          hover="Hover"
	          icon="{$label}Icon"
	   		  label="{resource:getString($constants,$label)}"
	          description="{resource:getString($constants,$descrip)}" 
	          class="{$class}"
			  args="{$args}"
	          onClick="this"/>
  </xsl:template>
</xsl:stylesheet>