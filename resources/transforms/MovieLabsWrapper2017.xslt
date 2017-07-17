<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdcr="http://www.movielabs.com/schema/mdcr/v1.1" xmlns:md="http://www.movielabs.com/schema/md/v2.1/md">
	
	<xsl:variable name="rsrcBase">../../</xsl:variable>
	<xsl:variable name="menuBase">../../../</xsl:variable>
	<xsl:variable name="disclaimer">DISCLAIMER: Although care has been taken to ensure the accuracy, completeness and reliability of the information provided, 
	we are not responsible if information that we make available on this site is not accurate, complete or current. The material on this site is 
	provided for general information only, and any reliance upon the material on this site will be at your own risk. We reserve the right to 
	modify the contents of the site at any time, but we have no obligation to update any information on this site. You agree that it is your 
	responsibility to monitor changes to the site.</xsl:variable>
	
	<xsl:template name="stdHeader">
				<meta http-equiv="Content-Type" content="text/html;  charset=utf-8"/>
				<link rel="Shortcut Icon" href="/images/ml_icon.ico"/> 
				<script src="https://use.typekit.net/rjt6miv.js"></script>
				<script>try{Typekit.load({ async: false });}catch(e){}</script>
				<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
				<link href="../../../../style/movielab-2017.css" rel="stylesheet" type="text/css" />
	</xsl:template>
	<xsl:template name="topPanel">   
  <div class="site-header">
    <div class="site-header-main">
      <div class="page-center">

        <div class="site-branding">
    		  <p class="site-title"><a href="http://movielabs.com" rel="home">Movie Labs</a></p>
    		</div>

        <button id="menu-toggle" class="menu-toggle">Menu</button>

        <div id="site-header-menu" class="site-header-menu">
        	<nav id="site-navigation" class="main-nav" role="navigation" aria-label="Primary Menu">
        			<div class="menu-main-container">
                <ul id="menu-main" class="primary-menu">
                  <li id="menu-item-24" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-has-children menu-item-24" aria-haspopup="true"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/">Who We Are</a><button class="dropdown-toggle" aria-expanded="false"><span class="screen-reader-text">expand child menu</span></button>
                    <ul class="sub-menu">
                    	<li id="menu-item-26" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-26"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/team/">Team</a></li>
                    	<li id="menu-item-212" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-212"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/how-we-run">How We Run</a></li>
                    	<li id="menu-item-211" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-211"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/partners/">Partners</a></li>
                    	<li id="menu-item-27" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-27"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/careers/">Careers</a></li>
                    </ul>
                  </li>
                  <li id="menu-item-28" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-28"><a href="http://dev.saltedstone.com/clients/movieLabs/what-we-do/">What We Do</a></li>
                  <li id="menu-item-271" class="menu-item menu-item-type-post_type menu-item-object-page current-page-ancestor current-menu-ancestor current-menu-parent current-page-parent current_page_parent current_page_ancestor menu-item-has-children menu-item-271" aria-haspopup="true"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/">Solutions &amp; Specifications</a><button class="dropdown-toggle" aria-expanded="false"><span class="screen-reader-text">expand child menu</span></button>
                  <ul class="sub-menu">
                  	<li id="menu-item-223" class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-141 current_page_item menu-item-223"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/movielabs-digital-distribution-framework-mddf/">MovieLabs Digital Distribution Framework (MDDF)</a></li>
                  	<li id="menu-item-229" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-229"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/ultra-hd-and-hdr/">Ultra HD and HDR</a></li>
                  	<li id="menu-item-232" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-232"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/enhanced-content-protection-ecp/">Enhanced Content Protection (ECP)</a></li>
                  	<li id="menu-item-235" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-235"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/digital-extras/">Digital extras</a></li>
                  	<li id="menu-item-241" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-241"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/common-metadata/">Common Metadata</a></li>
                  	<li id="menu-item-244" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-244"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/ema-avails/">EMA Avails</a></li>
                  	<li id="menu-item-247" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-247"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/media-manifest/">Media Manifest</a></li>
                  	<li id="menu-item-251" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-251"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/media-manifest-core/">Media Manifest Core</a></li>
                  	<li id="menu-item-254" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-254"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/media-entertainment-core-metadata/">Media Entertainment Core Metadata</a></li>
                  	<li id="menu-item-257" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-257"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/ratings/">Ratings</a></li>
                  	<li id="menu-item-260" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-260"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/automated-content-notification-system/">Automated Content Notification System</a></li>
                  </ul>
                </li>
                <li id="menu-item-30" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-30"><a href="http://dev.saltedstone.com/clients/movieLabs/outreach/">Outreach</a></li>
                <li id="menu-item-31" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-31"><a href="http://dev.saltedstone.com/clients/movieLabs/news/">News</a></li>
                <li id="menu-item-32" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-32"><a href="http://dev.saltedstone.com/clients/movieLabs/contact-us/">Contact Us</a></li>
              </ul>
            </div>
          </nav><!-- .main-navigation -->
        </div>

      </div>
    </div>
  </div>
							<!--  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
							 
	</xsl:template>
	<xsl:template name="stdFooter">
<footer id="colophon" class="site-footer bg-blue ta-center" role="contentinfo">
  <div class="page-center">

      <span class="footer-logo"><a href="http://dev.saltedstone.com/clients/movieLabs/" rel="home">Movie Labs</a></span>

      <nav class="footer-nav" role="navigation" aria-label="Footer Menu">
        <div class="menu-footer-container">
          <ul id="menu-footer" class="footer-menu">
            <li id="menu-item-33" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-33"><a href="http://dev.saltedstone.com/clients/movieLabs/who-we-are/">Who We Are</a></li>
            <li id="menu-item-34" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-34"><a href="http://dev.saltedstone.com/clients/movieLabs/what-we-do/">What We Do</a></li>
            <li id="menu-item-35" class="menu-item menu-item-type-post_type menu-item-object-page current-page-ancestor menu-item-35"><a href="http://dev.saltedstone.com/clients/movieLabs/solutions-specifications/">Solutions &amp; Specifications</a></li>
            <li id="menu-item-36" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-36"><a href="http://dev.saltedstone.com/clients/movieLabs/outreach/">Outreach</a></li>
            <li id="menu-item-37" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-37"><a href="http://dev.saltedstone.com/clients/movieLabs/news/">News</a></li>
            <li id="menu-item-38" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-38"><a href="http://dev.saltedstone.com/clients/movieLabs/contact-us/">Contact Us</a></li>
          </ul>
        </div>
      </nav>
      <!-- .footer-nav -->

      <aside id="footer-2" class="footer-widget widget-area" role="complementary">
        <section id="text-2" class="widget widget_text">
          <div class="textwidget">
            <p class="copyrights">© 2017 Motion Picture Laboratories, Inc. All rights reserved.</p>
          </div>
  		  </section>

        <section id="nav_menu-2" class="widget widget_nav_menu">
          <div class="menu-terms-policy-container">
            <ul id="menu-terms-policy" class="menu">
              <li id="menu-item-43" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-43">
                <a href="http://dev.saltedstone.com/clients/movieLabs/terms-of-use/">Terms of Use</a>
              </li>
              <li id="menu-item-44" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-44">
                <a href="http://dev.saltedstone.com/clients/movieLabs/privacy-policy/">Privacy Policy</a>
              </li>
            </ul>
          </div>
        </section>
      </aside>
      <!-- .footer-widget .widget-area -->

  </div>
</footer>
	</xsl:template>
</xsl:stylesheet>
