<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.0/mdcr" xmlns:md="http://www.movielabs.com/schema/md/v2.0/md">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/RatingSpecs">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<!-- InstanceBegin template="/Templates/MovieLab_template.dwt" codeOutsideHTMLIsLocked="false" -->
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<script type="text/JavaScript">
function showSystem(id)
{ 
  var node = document.getElementById( id );   
    sysName = node.options[node.selectedIndex].value;
    if(sysName != ""){
		if(id == "countryCode"){
		  var nodeOther = document.getElementById( "sysCode" );  
		  nodeOther.selectedIndex=0;
		  }else{
		  var nodeOther = document.getElementById( "countryCode" );  
		  nodeOther.selectedIndex=0;
		  }			
		//window.alert(sysName);  //for debugging only
		window.parent.postMessage("show:"+sysName, "*"); 
	}
} 
</script>
			</head>
			<body>
				<table border="0" cellpadding="0" cellspacing="0" summary="">
					<tr>
						<td>by Region:</td>
						<td>by Name</td>
					</tr>
					<tr>
					<td>
				<select name="Region" id="countryCode" class="country_select" onchange="showSystem('countryCode')">
					<option value="">
                                --- 
                              </option>
					<xsl:apply-templates select="document(RFile/@xmlFile)//mdcr:RatingSystem/mdcr:RatingSystemID/mdcr:Region">
						<xsl:with-param name="mode">Region</xsl:with-param>
						<xsl:sort select="mdcr:RegionName"/>
					</xsl:apply-templates>
				</select></td>
				<td><select name="System" id="sysCode" class="country_select" onchange="showSystem('sysCode')">
					<option value="">
                                --- 
                              </option>
					<xsl:apply-templates select="document(RFile/@xmlFile)//mdcr:RatingSystem">
						<xsl:with-param name="mode">Name</xsl:with-param>
						<xsl:sort select="mdcr:RatingSystemID/mdcr:System"/>
					</xsl:apply-templates>
				</select></td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="mdcr:Region">
		<xsl:param name="mode"/>
		<xsl:variable name="ptr">
			<xsl:value-of select="./../mdcr:System"/>
		</xsl:variable>
		<xsl:variable name="prefix">
			<xsl:value-of select="./../mdcr:Region[1]/md:country"/>
		</xsl:variable>
		<option value="{$prefix}_{$ptr}"> 
				<xsl:value-of select="./mdcr:RegionName"/> (<xsl:value-of select="./../mdcr:System"/>) 
		</option>
	</xsl:template>
	<xsl:template match="mdcr:RatingSystem">
		<xsl:param name="mode"/>
		<xsl:variable name="ptr">
			<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/>
		</xsl:variable>
		<xsl:variable name="prefix">
			<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region[1]/md:country"/>
		</xsl:variable>
		<option value="{$prefix}_{$ptr}">
			<xsl:if test="$mode='Name'">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/> (<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/mdcr:RegionName"/>)
			</xsl:if>
			<xsl:if test="$mode = 'Region'">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/mdcr:RegionName"/> (<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/>)
			</xsl:if>
		</option>
	</xsl:template>
</xsl:stylesheet>
