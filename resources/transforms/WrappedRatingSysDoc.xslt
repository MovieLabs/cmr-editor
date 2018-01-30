<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.1"
	xmlns:md="http://www.movielabs.com/schema/md/v2.1/md" xmlns:saxon="http://saxon.sf.net/">
	<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<xsl:include href="MovieLabsWrapper2017.xslt" />
	<xsl:variable name="tipHPCA">
		Indicates if the Rating is applicable to usage in an at-home Access
		Control system. This is intended as a hint for implementors and
		is not
		intended as a requirement.
	</xsl:variable>
	<xsl:variable name="tipOrdinal">
		Provides ranking indicator for internal use by Access Control
		mechanisms.
	</xsl:variable>
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<!-- InstanceBeginEditable name="doctitle" -->
				<xsl:for-each select="//mdcr:RatingSystem">
					<title>
						<xsl:value-of select="mdcr:RatingSystemID/mdcr:System" />
						Ratings
					</title>
				</xsl:for-each> 
				<xsl:comment>
					Converted using XSL version
					<xsl:value-of select="system-property('xsl:version')" />
				</xsl:comment>
				<xsl:call-template name="stdHeader" />

			</head>
			<body onload="resizeThisFrame()">
				<xsl:comment>
					generated using transform 'WrappedRatingSysDoc.xslt' (2017a)
				</xsl:comment>
				<xsl:call-template name="topPanel" /> 
				

  <div class="hero" style="background: url(http://movielabs.com/images/interior-hero-bg.jpg); background-size: cover; background-position: center center;">
    <div class="page-center">
      <!-- TemplateBeginEditable name="Title" -->
		<h1><a href="./Summary.html">COMMON METADATA RATINGS</a></h1>
		<hr style=" width: 200px;"/>
		<xsl:for-each select="//mdcr:RatingSystem">
				<h1>
					<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/mdcr:RegionName" />
					-
					<xsl:value-of select="mdcr:RatingSystemID/mdcr:System" /> 
				</h1>
		</xsl:for-each>
      <!-- TemplateEndEditable -->
    </div>
  </div>
				<table class="main-content" width="800" border="0" align="center" cellpadding="0" cellspacing="0">  
				  <tr>
				    <td valign="top">
				        <table width="100%" border="0" cellspacing="0" cellpadding="10">
				          <tr>
 				              <td width="75%" valign="top">
				<xsl:call-template name="contentSection" /> 
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
	<xsl:template name="contentSection">
		<!-- ******************************************* -->
		<!-- ****** BEGIN Dynamic Content *** -->
		<!-- ******************************************* -->
		<xsl:for-each select="//mdcr:RatingSystem">
			<xsl:variable name="hdrDivID">
				<xsl:if test="mdcr:RatingSystemID/@deprecated">
					<xsl:if test="fn:matches(mdcr:RatingSystemID/@deprecated,'true')">deprecatedRatingSys</xsl:if>
					<xsl:if test="not(fn:matches(mdcr:RatingSystemID/@deprecated,'true'))">activeRatingSys</xsl:if>
				</xsl:if>
				<xsl:if test="not(mdcr:RatingSystemID/@deprecated)">activeRatingSys</xsl:if>
			</xsl:variable> 
<!-- 			<hr id="blockDivider" /> -->
<!-- 			<div id="{$hdrDivID}"> -->
<!-- 				<h2> -->
<!-- 					<xsl:value-of select="mdcr:RatingSystemID/mdcr:System" /> -->
<!-- 					( -->
<!-- 					<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/mdcr:RegionName" /> -->
<!-- 					) -->
<!-- 				</h2> -->
<!-- 			</div> -->
			<p class="subtitle">
				Version
				<xsl:value-of select="mdcr:RatingSystemID/@version" />
			</p>
			URI:
			<tt>
				<xsl:value-of select="mdcr:URI" />
			</tt>
			<xsl:if test="./mdcr:LastChecked">
				<br />
				Last verified:
				<xsl:value-of select="./mdcr:LastChecked" />
			</xsl:if>
			<br />
			<hr id="blockDivider" />
			<p class="subtitle">Organization:</p>
			<ul>
				<li>
					Name:
					<xsl:value-of select="./mdcr:RatingsOrg/md:DisplayName" />
				</li>
				<!-- <li> Region: <xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/mdcr:RegionName" 
					/> <xsl:if test="mdcr:RatingSystemID/mdcr:Region/mdcr:SubRegion"> - <xsl:value-of 
					select="mdcr:RatingSystemID/mdcr:Region/mdcr:SubRegion" /> </xsl:if> </li> -->
				<li>
					Type of Organization:
					<xsl:value-of select="./mdcr:RatingsOrg/mdcr:OrgType" />
				</li>
				<xsl:if test="./mdcr:RatingsOrg/mdcr:ContactString">
					<li>
						Contact Information:
						<br />
						<xsl:value-of select="./mdcr:RatingsOrg/mdcr:ContactString" />
					</li>
				</xsl:if>
				<xsl:if test="not(./mdcr:RatingsOrg/mdcr:ContactString)">
					<li>Contact information not available</li>
				</xsl:if>
				<xsl:if test="./mdcr:RatingsOrg/mdcr:URL">
					<xsl:variable name="website">
						<xsl:value-of select="./mdcr:RatingsOrg/mdcr:URL" />
					</xsl:variable>
					<li>
						Web Site:
						<a href="{$website}">
							<xsl:value-of select="./mdcr:RatingsOrg/mdcr:URL" />
						</a>
					</li>
				</xsl:if>
			</ul>
			<hr id="blockDivider" />
			<p class="subtitle">Scope:</p>
			<p>
				This rating system has been adopted in these regions for the
				specified usage:
			</p>
			<table id="regionUsageTable">
				<thead align="center">
					<th>Region</th>
					<th>Distribution media</th>
					<th>Viewing Environments</th>
				</thead>
				<xsl:for-each select="mdcr:AdoptiveRegion">
					<tr>
						<td valign="top">
							<xsl:value-of select="./mdcr:RegionName" />
							<xsl:if test="./mdcr:SubRegion">
								-
								<xsl:value-of select="./mdcr:SubRegion" />
							</xsl:if>
						</td>
						<xsl:for-each select="./mdcr:Usage">
							<td align="center">
								<xsl:if test="./mdcr:Media">
									-
									<xsl:for-each select="./mdcr:Media">
										<xsl:value-of select="." />
										-
									</xsl:for-each>
								</xsl:if>
								<xsl:if test="not(./mdcr:Media)">
									all
								</xsl:if>
							</td>
							<td align="center">
								<xsl:if test="./mdcr:Environment">
									-
									<xsl:for-each select="./mdcr:Environment">
										<xsl:value-of select="." />
										-
									</xsl:for-each>
								</xsl:if>
								<xsl:if test="not(./mdcr:Environment)">
									all
								</xsl:if>
							</td>
						</xsl:for-each>

					</tr>
				</xsl:for-each>
			</table>

			<hr id="blockDivider" />
			<p class="subtitle">Notes:</p>
			<xsl:choose>
				<xsl:when test="$hdrDivID ='deprecatedRatingSys'">
					This is a
					<b>DEPRECATED</b>
					Rating System
				</xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
			<xsl:if test="./mdcr:Notes">
				<xsl:value-of select="./mdcr:Notes" />
			</xsl:if>
			<hr id="blockDivider" />
			<p class="subtitle">Ratings:</p>
			<xsl:variable name="filePrefix">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region[1]/md:country" />
			</xsl:variable>
			<xsl:variable name="fileId">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:System" />
			</xsl:variable>
			<p>
				<a href="../xml/{$filePrefix}_{$fileId}_Ratings.xml" target="_blank">
					<span class="hint--info" data-hint="Open XML file in new browser tab"> XML formatted file
					</span>
				</a>
			</p>
			<div id="ratingsDetails">
				<!-- +++++++++++++++++++++++++++++ -->
				<xsl:apply-templates select="mdcr:Rating">
					<!-- <xsl:sort select="mdcr:Value/@ordinal"/> broken (sorts alphabetically 
						instead of numerically ) -->
				</xsl:apply-templates>
			</div> <!-- END of ratingDetails -->
			<xsl:if test="count(./mdcr:Reason) &gt; 0">
				<hr id="blockDivider" />
				<p id="ratingSectionHdr">
					<a name="reason_list"></a>
					Reasons and Content Descriptors:
				</p>
				<div id="reasonDetails">
					<xsl:for-each select="./mdcr:Reason">
						<xsl:variable name="anchor">
							<xsl:value-of select="./@reasonID" />
						</xsl:variable>
						<div id="reasonSection">
							<p id="ratingHdr">
								<a name="reason_{$anchor}">
									<xsl:value-of select="./@reasonID" />
								</a>
							</p>
						</div>
						<xsl:if test="./mdcr:LinkToLogo">
							<xsl:for-each select="./mdcr:LinkToLogo">
								<p id="reasonIcon">
									<xsl:variable name="logoUrl" select="." />
									<img id="reasonIcon" alt="" src="{$logoUrl}" />
								</p>
							</xsl:for-each>
						</xsl:if>
						<xsl:if test="./mdcr:Definition">
							<p id="ratingSectionHdr">
								<xsl:value-of select="./mdcr:Definition" />
							</p>
						</xsl:if>
						<div id="reasonEx">
							<xsl:if test="./mdcr:Explanation">
								<xsl:value-of select=" ./mdcr:Explanation"
									disable-output-escaping="yes" />
							</xsl:if>
						</div>
						<xsl:for-each select="./mdcr:Criteria">

							<xsl:variable name="anchor2">
								<xsl:value-of select="./@ratingID" />
							</xsl:variable>
							<a name="reason_{$anchor}_{$anchor2}"></a>
							<p id="ratingSectionHdr">
								<u>
									<xsl:value-of select="./@ratingID" />
								</u>
								:
								<xsl:value-of select="./mdcr:Definition" />
							</p>
							<div id="explanatory">
								<xsl:if test="./mdcr:Explanation">
									<xsl:copy-of select="./mdcr:Explanation" />
								</xsl:if>
								<xsl:if test="not(./mdcr:Explanation)">
									<i>Not provided</i>
								</xsl:if>
							</div>
						</xsl:for-each>
					</xsl:for-each>
				</div>
			</xsl:if>
		</xsl:for-each>
		<!-- ******************************************* -->
		<!-- ****** END Dynamic Content *** -->
		<!-- ******************************************* -->
	</xsl:template>
	<xsl:template match="mdcr:Rating">
		<xsl:variable name="dead">
			<xsl:if test="./mdcr:Deprecated">
				<xsl:if test="fn:matches(./mdcr:Deprecated,'true')">(DEPRECATED)</xsl:if>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="bg-color-1">
			<xsl:if test="./mdcr:Deprecated">
				<xsl:if test="fn:matches(./mdcr:Deprecated,'true')">deprecatedRating</xsl:if>
				<xsl:if test="not(fn:matches(./mdcr:Deprecated,'true'))">activeRating</xsl:if>
			</xsl:if>
			<xsl:if test="not(./mdcr:Deprecated)">activeRating</xsl:if>
		</xsl:variable>
		<xsl:variable name="rValue">
			<xsl:value-of select="./mdcr:Value" />
		</xsl:variable>

		<!-- #################################################### -->
		<div id="{$bg-color-1}">
			<p id="ratingHdr">
				<a name="{$rValue}"></a>
				<xsl:for-each select="./mdcr:Descriptor">
					<u>
						<b>
							<xsl:value-of select="$rValue" />
						</b>
						<xsl:if test="not($rValue = ./mdcr:Label)">
							:
							<xsl:value-of select="./mdcr:Label" />
						</xsl:if>
					</u>
					<br />
				</xsl:for-each>
			</p>
		</div>
		<table id="ratingSymbolTable">
			<tbody>
				<tr>
					<xsl:if test="./mdcr:LinkToLogo">
						<xsl:for-each select="./mdcr:LinkToLogo">
							<td align="center" style="width:170px;height:60px;">
								<xsl:variable name="logoUrl" select="." />
								<img alt="" src="{$logoUrl}" />
							</td>
						</xsl:for-each>
					</xsl:if>
					<xsl:if test="not(./mdcr:LinkToLogo)">
						<td align="center" style="width:170px;height:60px;">
							<font face="arial" size="20">
								<xsl:value-of select="./mdcr:Value" />
							</font>
						</td>
					</xsl:if>
				</tr>
			</tbody>
		</table>
		<p id="ratingSectionHdr">Age Restrictions</p>
		<table id="ratingAgeTable">
			<tr>
				<td>Min Recommend Age:</td>
				<td align="center">
					<xsl:if test="./mdcr:MinRecAge">
						<xsl:if test="fn:number(./mdcr:MinRecAge) eq 0">
							N.A.
						</xsl:if>
						<xsl:if test="fn:number(./mdcr:MinRecAge) ne 0">
							<xsl:value-of select="./mdcr:MinRecAge" />
						</xsl:if>
					</xsl:if>
					<xsl:if test="not(./mdcr:MinRecAge)">
						N.A.
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>Min Allowed Age (Supervised):</td>
				<td align="center">
					<xsl:if test="./mdcr:MinAgeSupervised">
						<xsl:if test="fn:number(./mdcr:MinAgeSupervised) eq 0">
							N.A.
						</xsl:if>
						<xsl:if test="fn:number(./mdcr:MinAgeSupervised) ne 0">
							<xsl:value-of select="./mdcr:MinAgeSupervised" />
						</xsl:if>
					</xsl:if>
					<xsl:if test="not(./mdcr:MinAgeSupervised)">
						N.A.
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>Min Allowed Age (Unsupervised):</td>
				<td align="center">
					<xsl:if test="./mdcr:MinAge">
						<xsl:if test="fn:number(./mdcr:MinAge) eq 0">
							N.A.
						</xsl:if>
						<xsl:if test="fn:number(./mdcr:MinAge) ne 0">
							<xsl:value-of select="./mdcr:MinAge" />
						</xsl:if>
					</xsl:if>
					<xsl:if test="not(./mdcr:MinAge)">
						N.A.
					</xsl:if>
				</td>
			</tr>
		</table>

		<p id="ratingSectionHdr">Attributes</p>
		<table id="ratingAttribTable">
			<tr>
				<td>
					<span class="hint--info" data-hint="{$tipHPCA}">HAC Usage:</span>
				</td>
				<td>
					<xsl:if test="(./mdcr:HPCApplicable = 'true')">
						Y
					</xsl:if>
					<xsl:if test="not(./mdcr:HPCApplicable = 'true')">
						N
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>
					<span class="hint--info" data-hint="{$tipOrdinal}">HAC Ordinal:</span>
				</td>
				<td>
					<xsl:value-of select="./mdcr:Value/@ordinal" />
				</td>
			</tr>
		</table>
		<xsl:if test="./mdcr:Media or ./mdcr:Environment">
			<p id="ratingSectionHdr">Scope</p>
			<div id="explanatory">
				<xsl:if test="./mdcr:Media">
					This rating is applicable to the following types of
					distribution
					media:
					<ul>
						<xsl:for-each select="./mdcr:Media">
							<li>
								<xsl:value-of select="." />
							</li>
						</xsl:for-each>
					</ul>
				</xsl:if>
				<xsl:if test="not(./mdcr:Media)">
					This rating is applicable all types of distribution media
				</xsl:if>
				<xsl:if test="./mdcr:Environment">
					when viewed in the following environments:
					<ul>
						<xsl:for-each select="./mdcr:Environment">
							<li>
								<xsl:value-of select="." />
							</li>
						</xsl:for-each>
					</ul>
				</xsl:if>
				<xsl:if test="not(./mdcr:Environment)">
					regardless of viewing environment.
				</xsl:if>
			</div>
		</xsl:if>
		<p id="ratingSectionHdr">Definition</p>
		<xsl:for-each select="./mdcr:Descriptor">
			<p id="subSectionHdr">
				<u>
					<b>
						<xsl:value-of select="$rValue" />
					</b>
					<xsl:if test="not($rValue = ./mdcr:Label)">
						:
						<xsl:value-of select="./mdcr:Label" />
					</xsl:if>
				</u>
				<xsl:if test="./mdcr:Definition">
					<div id="explanatory">
						<xsl:value-of select="./mdcr:Definition" />
					</div>
				</xsl:if>
			</p>
		</xsl:for-each>
		<!-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ -->
		<xsl:if test="./mdcr:Override">
			<p id="ratingSectionHdr">Scope of Usage:</p>
			<p>
				The applicability of this rating has been restricted as follows:
			</p>
			<table id="regionUsageTable">
				<thead align="center">
					<th>Region</th>
					<th>Distribution media</th>
					<th>Viewing Environments</th>
				</thead>
				<xsl:for-each select="./mdcr:Override">
					<tr>
						<td valign="top">
							<xsl:value-of select="./mdcr:RegionName" />
							<xsl:if test="./mdcr:SubRegion">
								-
								<xsl:value-of select="./mdcr:SubRegion" />
							</xsl:if>
						</td>
						<xsl:for-each select="./mdcr:Usage">
							<td align="center">
								<xsl:if test="./mdcr:Media">
									-
									<xsl:for-each select="./mdcr:Media">
										<xsl:value-of select="." />
										-
									</xsl:for-each>
								</xsl:if>
								<xsl:if test="not(./mdcr:Media)">
									all
								</xsl:if>
							</td>
							<td align="center">
								<xsl:if test="./mdcr:Environment">
									-
									<xsl:for-each select="./mdcr:Environment">
										<xsl:value-of select="." />
										-
									</xsl:for-each>
								</xsl:if>
								<xsl:if test="not(./mdcr:Environment)">
									all
								</xsl:if>
							</td>
						</xsl:for-each>

					</tr>
				</xsl:for-each>
			</table>

		</xsl:if>
		<!-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ -->
		<p id="ratingSectionHdr">Reasons and Content Descriptors</p>
		<table id="ratingCriteriaTable">
			<xsl:for-each select="./mdcr:ApplicableReason ">
				<xsl:variable name="criteriaAnchor">
					<xsl:value-of select="./@id" />
				</xsl:variable>
				<xsl:variable name="criteriaDefPath">
					/mdcr:RatingSystem/mdcr:Reason[@reasonID='
					<xsl:value-of select="./@id" />
					']/mdcr:Criteria[@ratingID='
					<xsl:value-of select="$rValue" />
					']/mdcr:Definition
				</xsl:variable>
				<xsl:variable name="criteriaDef">
					<xsl:value-of select="saxon:evaluate($criteriaDefPath)" />
				</xsl:variable>
				<tr>
					<td>
						<a href="#reason_{$criteriaAnchor}_{$rValue}">
							<xsl:value-of select="./@id" />
						</a>
					</td>
					<td>
						<xsl:value-of select="saxon:evaluate($criteriaDefPath)" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<p id="ratingSectionHdr">Explanation</p>
		<xsl:for-each select="./mdcr:Descriptor">
			<div id="explanatory">
				<xsl:if test="./mdcr:Explanation">
					<xsl:copy-of select="./mdcr:Explanation" />
				</xsl:if>
				<xsl:if test="not(./mdcr:Explanation)">
					<i>Not provided</i>
				</xsl:if>
			</div>
			<xsl:if test="./mdcr:DefinitionURL">
				<xsl:variable name="anchor">
					<xsl:value-of select="./mdcr:DefinitionURL" />
				</xsl:variable>
				<hr />
				Def URL=
				<a href="{$anchor}">
					<xsl:value-of select="./mdcr:DefinitionURL" />
				</a>
			</xsl:if>
			<!-- #################################################### -->
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
