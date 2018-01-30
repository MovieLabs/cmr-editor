<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" 
	xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.1" 
	xmlns:md="http://www.movielabs.com/schema/md/v2.1/md">
	<xsl:output method="html" encoding="UTF-8" indent="yes"/>
	<xsl:include href="MovieLabsWrapper2017.xslt" />
	<xsl:param name="mLabVersion"/>
		<xsl:template match="/mdcr:RatingSystemSet">
		<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.1" xmlns:md="http://www.movielabs.com/schema/md/v2.1/md">
			<head>
				<title>Ratings Metadata – MovieLabs</title>
				<xsl:comment>
				generated using transform 'WrappedSummary2017.xslt'
				</xsl:comment>	
				 <xsl:call-template name="stdHeader" />
				 <style type="text/css">
				 	tr.summary{font-size: 11px;}
				 	ul.summaryList {list-style:none; padding:1;}
				 	td.summaryAutoCell {width:1px;white-space:nowrap;}
				 	p.summaryTitle {font-size: 12px; text-decoration: underline;}
				 </style>				 
			</head>
			<body>
				<xsl:call-template name="topPanel" /> 
				  <div class="hero" style="background: url(http://movielabs.com/images/interior-hero-bg.jpg); background-size: cover; background-position: center center;">
    <div class="page-center">
      <!-- TemplateBeginEditable name="Title" -->
		<h1><a href="./Summary.html">COMMON METADATA RATINGS</a></h1>
      <!-- TemplateEndEditable -->
    </div>
  </div>
				<table width="800" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="10">
								<tr>
									<td>
										<p class="title">INTRODUCTION</p>
										<p>This document contains enumeration values and usage constraints for the inclusion of Content Ratings in metadata. </p>
										<p>This document contains information useful when used in conjunction with MovieLabs Common Metadata (TR-META-CM) and 
										derived specifications such as DEG/EMA Metadata and UltraViolet Content Metadata.</p>
										<p><xsl:value-of select="$disclaimer"/></p>
										<p class="subtitle">References</p>
										<ul>
											<li>[CM] TR-META-CM, Common Metadata, www.movielabs.com/md/md</li>
											<li>[CEA766] ANSI/CEA-766-C, U.S. and Canadian Rating Region Tables (RRT) and Content Advisory Descriptors for Transport of Content Advisory Information Using ATSC Program and System Information Protocol (PSIP). April 2008.</li>
										</ul>
										<p>References to specific ratings systems and bodies are found in the text.</p>
										<p class="subtitle">Versioning </p>
										<p>This is <b>Version <xsl:value-of select="$mLabVersion"/></b>.</p>
										<p>This document will be versioned when updated. It is recommended that the latest version be used. However, specifications may reference a specific version if necessary.</p>
										<p class="subtitle">Contact</p>
										<p>All questions, comments, and feedback regarding either this document or specific ratings systems should be submitted to <a href="mailto:ratings@movielabs.com">ratings@movielabs.com</a></p>
										<p class="title">CONTENT RATING ENCODING</p>
										<div id="listing-section">
										<table  id="summaryTable" border="1" cellpadding="4" cellspacing="0" summary="">
											<thead align="center"  >
												<th>Region</th>
												<th>Type</th>
												<th>Environ</th>
												<th>System</th>
												<th>Ratings</th>
												<th>Reasons</th>
												<th>Notes &amp; References</th>
											</thead>   
											<xsl:apply-templates select="//mdcr:AdoptiveRegion"> 
 												 <xsl:sort select="mdcr:RegionName"/>
											</xsl:apply-templates>  
										</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<xsl:call-template name="stdFooter" /> 
			</body>
		</html>
	</xsl:template>
	<xsl:template match="mdcr:AdoptiveRegion">
	<!-- make sure this is NOT an AdoptiveRegion that is part of the <Override> of a specific <Rating> --> 
		<xsl:if test="../../mdcr:RatingSystem">
			<!-- <p><xsl:value-of select="./mdcr:RegionName"/> in <xsl:value-of select="../mdcr:RatingSystemID/mdcr:System"/> 
				</p> -->
			<xsl:variable name="subregion">
				<xsl:if test="./mdcr:SubRegion">
					<br />
					(
					<xsl:value-of select="./mdcr:SubRegion" />
					)
				</xsl:if>
				<xsl:if test="not(./mdcr:SubRegion)">
				</xsl:if>
			</xsl:variable>
			<xsl:apply-templates select="..">
				<xsl:with-param name="aoi" select="./mdcr:RegionName" />
				<xsl:with-param name="subaoi" select="$subregion" />
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template> 
	<xsl:template match="mdcr:RatingSystem">
		<xsl:param name="aoi"/>
		<xsl:param name="subaoi"/>
		<xsl:variable name="rName">
		<xsl:value-of select="$aoi"/>
		</xsl:variable>
		<xsl:variable name="filePrefix">
			<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region[1]/md:country"/>
		</xsl:variable>
		<xsl:variable name="fileId">
		<xsl:value-of select="./mdcr:RatingSystemID/mdcr:System"/>
		</xsl:variable>
		<xsl:variable name="hdrDivID">
			<xsl:if test="./mdcr:RatingSystemID/@deprecated">
				<xsl:if test="fn:matches(./mdcr:RatingSystemID/@deprecated,'true')">deprecatedRatingSys</xsl:if>
				<xsl:if test="not(fn:matches(./mdcr:RatingSystemID/@deprecated,'true'))">activeRatingSys</xsl:if>
			</xsl:if>
			<xsl:if test="not(./mdcr:RatingSystemID/@deprecated)">activeRatingSys</xsl:if>
		</xsl:variable>
		<xsl:variable name="divTitle">
		<xsl:choose>
			<xsl:when test="$hdrDivID ='deprecatedRatingSys'">DEPRECATED Rating System</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
		</xsl:variable>
		<div title="{$divTitle}">
		<tr align="left" valign="top" class="summary" id="{$hdrDivID}">
			<td class="summaryKeyCell">
				<xsl:value-of select="$aoi"/><xsl:value-of select="$subaoi"/>
			</td>
			<td class="summaryOther">
				<xsl:if test="./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Media"> 
					<ul class="summaryList">
						<xsl:for-each select="./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Media">
							<li>
								<xsl:value-of select="."/>
							</li>
						</xsl:for-each>
					</ul>
				</xsl:if>
				<xsl:if test="not(./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Media)">
												 all										 
											</xsl:if>
			</td>
			<td class="summaryOther">
				<xsl:if test="./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Environment">
					<ul class="summaryList">
						<xsl:for-each select="./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Environment">
							<li>
								<xsl:value-of select="."/>
							</li>
						</xsl:for-each>
					</ul>
				</xsl:if>
				<xsl:if test="not(./mdcr:AdoptiveRegion[mdcr:RegionName=$rName]/mdcr:Usage/mdcr:Environment)">
												 all											 
											</xsl:if>
			</td>
			<td class="summaryCell">
					 <a href="./{$filePrefix}_{$fileId}_Ratings.html"><xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/></a> 
			</td>
			<td class="summaryOther summaryAutoCell">
				<ul class="summaryList">
					<xsl:for-each select="./mdcr:Rating">
		<xsl:variable name="anchor">
			<xsl:value-of select="./@ratingID"/>
		</xsl:variable>
						<li><a href="./{$filePrefix}_{$fileId}_Ratings.html#{$anchor}">
							<xsl:value-of select="./@ratingID"/></a>
						</li>
					</xsl:for-each>
				</ul>
			</td>
			<td class="summaryOther summaryAutoCell">
				<ul class="summaryList">
					<xsl:for-each select="./mdcr:Reason">
						<xsl:variable name="anchor">
							<xsl:value-of select="./@reasonID"/>
						</xsl:variable>
						<li><a href="./{$filePrefix}_{$fileId}_Ratings.html#reason_{$anchor}">
							<xsl:value-of select="./@reasonID"/></a>
						</li>
					</xsl:for-each>
				</ul>
				</td>
			<td class="summaryOther">
			<ul>
			<xsl:if test="$hdrDivID ='deprecatedRatingSys'">
			<li>This Rating System has been tagged as <b>DEPRECATED</b></li>
			</xsl:if>
			<li>Web Site:
				<xsl:if test="./mdcr:RatingsOrg/mdcr:URL">
					<xsl:variable name="orgURL" select="./mdcr:RatingsOrg/mdcr:URL"/>
					<a href="{$orgURL}">
						<xsl:value-of select="./mdcr:RatingsOrg/mdcr:URL"/>
					</a>
				</xsl:if></li>
			<li>URI: <xsl:value-of select="./mdcr:URI"/></li>
			<xsl:if test="./mdcr:LastChecked">
			<li>Last verified: 
						<xsl:value-of select="./mdcr:LastChecked"/> </li>
				</xsl:if>
			</ul>
			<xsl:if test="./mdcr:Notes">
				<p class="summaryTitle">Notes:</p>
				<div class="summaryNotes">
					<xsl:value-of select="./mdcr:Notes" />
				</div>
			</xsl:if>
			</td>
		</tr>   
		</div> 
	</xsl:template>
</xsl:stylesheet>
