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
				<link href="../../styles/movielab-2020.css" rel="stylesheet" type="text/css" />
				<link href="../../styles/menu_2020.css" rel="stylesheet" type="text/css" />
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

<div class="site-header">
  <div class="site-header-main">
    <div class="page-center">
      <nav>
        <div id="logo"><a href="/" rel="home"></a></div>
        <label for="drop" class="toggle burger"><img src="../images/burger.png"></img></label>
        <input type="checkbox" id="drop" />
        <ul class="menu">
          <li class="firstLevel"> 
            <!-- First Tier Drop Down -->
            <label for="drop-0" class="toggle">WHO WE ARE</label>
            <a href="/who-we-are/">WHO WE ARE</a>
            <input type="checkbox" id="drop-0"/>
            <ul>
              <li><a href="/who-we-are/team/">Team</a></li>
              <li><a href="/who-we-are/how-we-run/">How We Run</a></li>
              <li><a href="/who-we-are/partners/">Partners</a></li>
              <li><a href="/who-we-are/careers/">Careers</a></li>
              <li><a class="noBorder" href="/who-we-are/contact-us/">Contact Us</a></li>
            </ul>
          </li>
          <li class="firstLevel"> 
            <!-- First Tier Drop Down -->
            <label for="drop-1" class="toggle">WHAT WE DO</label>
            <a href="/what-we-do-2/">WHAT WE DO</a>
            <input type="checkbox" id="drop-1"/>
            <ul>
              <li><a href="/production-technology/">Production Technology</a></li>
              <li><a href="/solutions-specifications/">Content Protection</a></li>
              <li><a href="/md/">Distribution Supply Chain</a></li>
              <li><a href="/solutions-specifications/">Next Generation Formats</a></li>
              <li><a href="/solutions-specifications/">Data &amp; Business Intelligence</a></li>
              <li><a class="noBorder" href="/outreach/">Future Technology</a></li>
            </ul>
          </li>
          <li class="firstLevel"> 
            <!-- First Tier Drop Down -->
            <label for="drop-2" class="toggle">SPECIFICATIONS &amp; RESOURCES</label>
            <a href="/specifications-and-resources/">SPECIFICATIONS &amp; RESOURCES</a>
            <input type="checkbox" id="drop-2"/>
            <ul>
              <li> 
                <!-- Second Tier Drop Down -->
                <label for="drop-21" class="toggle">Production Technology Resources</label>
                <a href="/production-technology/">Production Technology Resources</a>
                <input type="checkbox" id="drop-21"/>
                <ul>
                  <li><a href="/production-technology/#Envisioning production in 2030 - white paper">Envisioning Production in 2030</a></li>
                  <li><a href="/production-technology/#SECURING THE 2030 VISION - WHITE PAPER">Securing The 2030 Vision</a></li>
				  <li><a href="/production-technology/sdw/">Software-Defined Workflows</a></li>
                  <li><a href="/production-technology/sdw/vfx/">VFX Naming and Workflows</a></li>
                  <li><a class="noBorder" href="/production-technology#Workflow%20use%20cases%20for%20resumption%20of%20film%20and%20TV%20production">Use Cases for Production Resumption</a></li>
                </ul>
              </li>
              <li> 
                <!-- Second Tier Drop Down -->
                <label for="drop-22" class="toggle">Content Protection</label>
                <a href="/specifications-and-resources/#Content%20Protection">Content Protection</a>
                <input type="checkbox" id="drop-22"/>
                <ul>
                  <li><a class="noBorder" href="/solutions-specifications/enhanced-content-protection-ecp/">Enhanced Content Protection</a></li>
                </ul>
              </li>
              <li> 
                <!-- Second Tier Drop Down -->
                <label for="drop-23" class="toggle">Distribution Supply Chain</label>
                <a href="/specifications-and-resources/#Distribution%20Supply%20Chain">Distribution Supply Chain</a>
                <input type="checkbox" id="drop-23"/>
                <ul>
                  <li><a href="/md/">Movielabs Digital Distibution Framework (MDDF)</a></li>
                  <li><a href="/md/md/">Common Metadata</a></li>
                  <li><a href="/md/mec/">Media Entertainment Core Metadata</a></li>
                  <li><a href="/md/avails/">EMA Avails and Title List</a></li>
                  <li><a href="/md/manifest/">Media Manifest</a></li>
                  <li><a href="/md/mmc/">Media Manifest Core</a></li>
                  <li><a href="/md/delivery">Delivery</a></li>
                  <li><a href="/md/qcvocabulary">QC Vocabulary</a></li>
                  <li><a class="noBorder" href="/md/ratings/">Ratings</a></li>
                </ul>
              </li>
              <li> 
                <!-- Second Tier Drop Down -->
                <label for="drop-24" class="toggle">Next Generation Formats</label>
                <a href="/specifications-and-resources/#Next-Generation%20Formats">Next Generation Formats</a>
                <input type="checkbox" id="drop-24"/>
                <ul>
                  <li><a href="/next-generation-platforms/ultra-hd-and-hdr/">Ultra HD &amp; HDR</a></li>
                  <li><a class="noBorder" href="/cpe/">Digital Extras/CPE</a></li>
                </ul>
              </li>
              <li> 
                <!-- Second Tier Drop Down -->
                <label for="drop-25" class="toggle">Data &amp; Business Intelligence</label>
                <a class="noBorder" href="/specifications-and-resources/#Data%20&amp;%20Business%20Intelligence">Data &amp; Business Intelligence</a>
                <input type="checkbox" id="drop-25"/>
                <ul>
                  <li><a href="/creative-works-ontology/">Creative Works Ontology</a></li>
                  <li><a class="noBorder" href="/news/movielabs-publishes-talent-id-white-paper/">Talent ID</a></li>
                </ul>
              </li>
            </ul>
          </li>
          <li class="firstLevel-s"><a href="/outreach/">INNOVATION</a></li>
          <li class="firstLevel-s"><a href="/news/">NEWS</a></li>
          <li class="firstLevel-s"><a href="/blog/">2030 BLOG</a></li>
        </ul>
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

<footer id="colophon" class="md-footer bg-blue ta-center" role="contentinfo">
  <div class="footer-logo"><a href="/" rel="home">Movie Labs</a></div>
  <div class="footer-menu" role="navigation" aria-label="Footer Menu">
    <ul>
      <li id="menu-item-33" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-33"><a href="/who-we-are/">Who We Are</a></li>
      <li id="menu-item-34" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-34"><a href="/what-we-do/">What We Do</a></li>
      <li id="menu-item-35" class="menu-item menu-item-type-post_type menu-item-object-page current-page-ancestor menu-item-35"><a href="/solutions-specifications/">Specifications &amp; Resources</a></li>
      <li id="menu-item-36" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-36"><a href="/outreach/">Innovation</a></li>
      <li id="menu-item-37" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-37"><a href="/news/">News</a></li>
      <li id="menu-item-38" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-38"><a href="/blog/">2030 Blog</a></li>
    </ul>
  </div>
  <div>
    <hr class="footer-ruler"/>
  </div>
  <aside id="footer-2" class="footer-terms" role="complementary">
    <ul id="menu-terms-policy" class="">
      <li id="menu-item-43" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-43"> <a href="/terms-of-use/">Terms of Use</a> </li>
      <li id="menu-item-44" class=""> <a href="/privacy-policy/">Privacy Policy</a> </li>
    </ul>
  </aside>
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