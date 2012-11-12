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
<!-- 
<xalan:component prefix="resource">
	<xalan:script lang="javaclass" src="xalan://org.openelis.util.UTFResource"/>
</xalan:component>
  
<xalan:component prefix="locale">
	<xalan:script lang="javaclass" src="xalan://java.util.Locale"/>
</xalan:component>
-->
<xsl:variable name="language"><xsl:value-of select="doc/locale"/></xsl:variable>
<xsl:variable name="props"><xsl:value-of select="doc/props"/></xsl:variable>
<xsl:variable name="constants" select="resource:getBundle(string($props),locale:new(string($language)))"/>

<!-- query button template -->
<xsl:template name="queryButton">
	<button css="libRes.buttonPanel" key="query" icon="libRes.buttonPanel.QueryButtonImage" text="{resource:getString($constants,'btn.query')}" action="query" toggle="true" shortcut="ctrl+q"/>	
</xsl:template>

<!-- previous button template -->
<xsl:template name="previousButton">
	<button key="previous" icon="libRes.buttonPanel.PreviousButtonImage" text="{resource:getString($constants,'btn.previous')}" action="previous" css="libRes.buttonPanel" shortcut="ctrl+p"/>
</xsl:template>

<!-- next button template -->
<xsl:template name="nextButton">
	<button key="next" icon="libRes.buttonPanel.NextButtonImage" text="{resource:getString($constants,'btn.next')}" action="next" css="libRes.buttonPanel" shortcut="ctrl+n"/>
</xsl:template>

<!-- update button template -->
<xsl:template name="updateButton">
	<button key="update" icon="libRes.buttonPanel.UpdateButtonImage" text="{resource:getString($constants,'btn.update')}" action="update" toggle="true" css="libRes.buttonPanel" shortcut="ctrl+u"/>
</xsl:template>

<!-- add button template -->
<xsl:template name="addButton">
	<button key="add" icon="libRes.buttonPanel.AddButtonImage" text="{resource:getString($constants,'btn.add')}" action="add" toggle="true" css="libRes.buttonPanel" shortcut="ctrl+a"/>
</xsl:template>

<!-- delete button template -->
<xsl:template name="deleteButton">
	<button key="delete" icon="libRes.buttonPanel.DeleteButtonImage" text="{resource:getString($constants,'btn.delete')}" action="delete" toggle="true" css="libRes.buttonPanel" shortcut="ctrl+d"/>
</xsl:template>

<!-- commit button template -->
<xsl:template name="commitButton">
    <button key="commit" icon="libRes.buttonPanel.CommitButtonImage" text="{resource:getString($constants,'btn.commit')}" action="commit" css="libRes.buttonPanel" shortcut="ctrl+m"/>
</xsl:template>

<!-- process button template -->
<xsl:template name="processButton">
    <button key="process" icon="libRes.buttonPanel.ProcessButtonImage" text="{resource:getString($constants,'btn.process')}" action="add" css="libRes.buttonPanel" shortcut="ctrl+a"/>
</xsl:template>

<!-- abort button template -->
<xsl:template name="abortButton">
	<button key="abort" icon="libRes.buttonPanel.AbortButtonImage" text="{resource:getString($constants,'btn.abort')}" action="abort" css="libRes.buttonPanel" shortcut="ctrl+o"/>
</xsl:template>

<!-- ok button template -->
<xsl:template name="okButton">
	<button key="ok" icon="libRes.buttonPanel.CommitButtonImage" text="{resource:getString($constants,'btn.ok')}" action="ok" css="libRes.buttonPanel"/>
</xsl:template>

<!-- cancel button template -->
<xsl:template name="cancelButton">
	<button key="cancel" icon="libRes.buttonPanel.AbortButtonImage" text="{resource:getString($constants,'btn.cancel')}" action="cancel" css="libRes.buttonPanel"/>
</xsl:template>

<!-- duplicate menu item-->
<xsl:template name="duplicateRecordMenuItem">
	<xsl:call-template name="menuItem">
		<xsl:with-param name="key">duplicateRecord</xsl:with-param>
		<xsl:with-param name="label">btn.duplicateRecord</xsl:with-param>
		<xsl:with-param name="enabled">false</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<!-- history menu item-->
<xsl:template name="historyMenuItem">
	<xsl:call-template name="menuItem">
		<xsl:with-param name="key">history</xsl:with-param>
		<xsl:with-param name="label">history</xsl:with-param>
		<xsl:with-param name="enabled">false</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<!-- popup transfer button template -->
<xsl:template name="popupTransferButton">
   <button key="popupTransfer" icon="libRes.buttonPanel.CommitButtonImage" text="{resource:getString($constants,'btn.transfer')}" action="commit"/>
</xsl:template>

<!-- select button template -->
<xsl:template name="selectButton">
	<button key="select" icon="libRes.buttonPanel.CommitButtonImage" text="{resource:getString($constants,'btn.select')}" action="select" css="libRes.buttonPanel" />
</xsl:template>

<!-- print button template -->
<xsl:template name="printButton">
	<button key="print" icon="libRes.buttonPanel.PrintButtonImage" text="{resource:getString($constants,'btn.print')}" action="print" css="libRes.buttonPanel"/>
</xsl:template>

<!-- buttonpanel divider template -->
<xsl:template name="buttonPanelDivider">
	<AbsolutePanel class="libRes.buttonPanel.ButtonDivider"/>
</xsl:template>

<!-- A to Z buttons template -->
<xsl:template name="aToZButton">
<xsl:param name="keyParam" />
<xsl:param name="queryParam" />
	<button key="{string($keyParam)}" text="{string($keyParam)}" action="{string($queryParam)}" alwaysEnabled="true" style="smallButton"/>
</xsl:template>

<!-- Menu item template -->
 <xsl:template name="menuItem">
  	<xsl:param name="key"/>
    <xsl:param name="label"/>
    <xsl:param name="enabled"/>
    <xsl:variable name="descrip"><xsl:value-of select="$label"/>Description</xsl:variable>
  	<menuItem key="{$key}" style="TopMenuRowContainer" enabled="{$enabled}"  
	          icon="libRes.buttonPanel.{$label}Icon"
	   		  display="{resource:getString($constants,$label)}"
	          description="{resource:getString($constants,$descrip)}"/>
  </xsl:template>
</xsl:stylesheet>
