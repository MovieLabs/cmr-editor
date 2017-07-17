<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.0/mdcr" xmlns:md="http://www.movielabs.com/schema/md/v2.0/md">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/RatingSpecs">
		<RatingSystemCatalog>
			<!-- InstanceBegin template="/Templates/MovieLab_template.dwt" codeOutsideHTMLIsLocked="false" -->
			<ByRegion>
					<xsl:apply-templates select="document(RFile/@xmlFile)//mdcr:RatingSystem">
						<xsl:with-param name="mode">Region</xsl:with-param>
						<xsl:sort select="mdcr:RatingSystemID/mdcr:Region[1]/md:country"/>
					</xsl:apply-templates>			
			</ByRegion>
			<ByName>
					<xsl:apply-templates select="document(RFile/@xmlFile)//mdcr:RatingSystem">
						<xsl:with-param name="mode">Name</xsl:with-param>
						<xsl:sort select="mdcr:RatingSystemID/mdcr:System"/>
					</xsl:apply-templates>
			</ByName> 
		</RatingSystemCatalog>
	</xsl:template>
	<xsl:template match="mdcr:RatingSystem">
		<xsl:param name="mode"/>
		<xsl:variable name="ptr">
			<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/>
		</xsl:variable>
		<RatingSys  value="{$ptr}">
			<xsl:if test="$mode='Name'">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/> (<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/md:country"/>)
			</xsl:if>
			<xsl:if test="$mode = 'Region'">
				<xsl:value-of select="mdcr:RatingSystemID/mdcr:Region/md:country"/> (<xsl:value-of select="mdcr:RatingSystemID/mdcr:System"/>)
			</xsl:if>
		</RatingSys>
	</xsl:template>
</xsl:stylesheet>
