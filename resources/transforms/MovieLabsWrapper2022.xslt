<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.1" xmlns:md="http://www.movielabs.com/schema/md/v2.1/md">
	
	<xsl:variable name="rsrcBase">../../</xsl:variable>
	<xsl:variable name="menuBase">../../../</xsl:variable>
	<xsl:variable name="disclaimer">DISCLAIMER: Although care has been taken to ensure the accuracy, completeness and reliability of the information provided, 
	we are not responsible if information that we make available on this site is not accurate, complete or current. The material on this site is 
	provided for general information only, and any reliance upon the material on this site will be at your own risk. We reserve the right to 
	modify the contents of the site at any time, but we have no obligation to update any information on this site. You agree that it is your 
	responsibility to monitor changes to the site.</xsl:variable>
	<xsl:template name="addGoogleTracking">
				<xsl:variable name="trackid">UA-139301931-1</xsl:variable>
				<!-- Global site tag (gtag.js) - Google Analytics -->
				<script async="async" src="https://www.googletagmanager.com/gtag/js?id=UA-{$trackid}"><xsl:text> </xsl:text></script>
				<script>
				  <xsl:text disable-output-escaping="yes">
				  window.dataLayer = window.dataLayer || [];
				  function gtag(){dataLayer.push(arguments);}
				  gtag('js', new Date());
				  
				  gtag('config', '</xsl:text><xsl:value-of select="$trackid"/><xsl:text disable-output-escaping="yes">');</xsl:text>
				</script>
	</xsl:template>
	<xsl:template name="stdHeader">
				<meta http-equiv="Content-Type" content="text/html;  charset=utf-8"/>
				<link rel="Shortcut Icon" href="/images/ml_icon.ico"/> 
				<script src="https://use.typekit.net/rjt6miv.js"> </script>
				<script>try{Typekit.load({ async: false });}catch(e){}</script>
				<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"> </script> 
				<link xmlns="" href="../../styles/CMRatings.css" rel="stylesheet" type="text/css"/>
				<style type="text/css"></style>
				<link href="../../styles/movielab-2022.css" rel="stylesheet" type="text/css" />
				<link href="../../styles/menu_2022.css" rel="stylesheet" type="text/css" />
				<!-- Global site tag (gtag.js) - Google Analytics -->
				<script async="async" src="https://www.googletagmanager.com/gtag/js?id=UA-139301931-1"></script>
				<script>
				  window.dataLayer = window.dataLayer || [];
				  function gtag(){dataLayer.push(arguments);}
				  gtag('js', new Date());
				  gtag('config', 'UA-139301931-1');
				</script>
	</xsl:template>
	<xsl:template name="topPanel">

<div class="site-header hero-new bg-black">
  <div class="site-header-main">
    <div class="page-center">
      <nav>
        <a id="logo-new" href="/md"></a>
        <a class="header-button" href="/">More about Movielabs</a>
      </nav>
    </div>
  </div>
</div>

							<!--  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
							 
	</xsl:template>
	
	<xsl:template name = 'breadcrumb'>
<div class="breadcrumb">
	<div class="page-center">
	  <div class="breadcrumbs" itemscope="" itemtype="http://schema.org/BreadcrumbList">
		  <span itemprop="itemListElement" itemscope="" itemtype="http://schema.org/ListItem"><a href="https://movielabs.com/" itemprop="item" class="home"><span itemprop="name">Home</span></a><meta itemprop="position" content="1"></meta></span> 
		  <!-- InstanceBeginEditable name="Breadcrumb" -->
		  <span class="sep">›</span><span itemprop="itemListElement" itemscope="" itemtype="http://schema.org/ListItem"><a href="https://movielabs.com/md" itemprop="item" class="home"><span itemprop="name">MDDF</span></a><meta itemprop="position" content="2"></meta></span> 
		  <span class="sep">›</span> <span class="current">Ratings</span> <span class="sep">›</span><xsl:for-each select="//mdcr:RatingSystem"/>
		  <!-- InstanceEndEditable -->
		</div>
	</div>
</div>
	</xsl:template>
	<xsl:template name="stdFooter">
<div class="md-footer-link">Learn more about Motion Picture Laboratories, Inc at <a href="/">movielabs.com</a></div>
<footer id="colophon" class="md-footer bg-black ta-center" role="contentinfo">
  <div class="footer-cc">@ 2021 Movielabs. All rights reserved</div>
</footer>
<script>
$(document).ready(function(){
  //$('.hero h1').text( $(document).find("title").text().split('-')[0] );

  $('#menu-toggle').click(function(){
    if ( $('.site-header').hasClass('toggled-on') ){
      $('.site-header').removeClass('toggled-on');
      $('.site-header-menu').removeClass('toggled-on');
      $('.primary-menu').removeClass('toggled-on');
    }
    else{
      $('.site-header').addClass('toggled-on');
      $('.site-header-menu').addClass('toggled-on');
      $('.primary-menu').addClass('toggled-on');
    }
  });

  $('.dropdown-toggle').click(function(){
    if ( $(this).hasClass('toggled-on') ){
      $(this).removeClass('toggled-on');
      $(this).siblings('.sub-menu').removeClass('toggled-on');
    }
    else{
      $(this).addClass('toggled-on');
      $(this).siblings('.sub-menu').addClass('toggled-on');
    }
  });

});
</script> 
<script>
function isTouchDevice(){
    return typeof window.ontouchstart !== 'undefined';
}
jQuery(document).ready(function(){
	if ( isTouchDevice() == true ){
        jQuery('.site-header').addClass('touch-device');
		console.log('touch device');
	}
	else{
		console.log('not touch device');
	}
});
</script> 
<script>
function isTouchDevice(){
    return typeof window.ontouchstart !== 'undefined';
}
jQuery(document).ready(function(){
	if ( isTouchDevice() == true ){
        jQuery('.site-header').addClass('touch-device');
		console.log('touch device');
	}
	else{
		console.log('not touch device');
	}
});
</script>
	</xsl:template>
</xsl:stylesheet>