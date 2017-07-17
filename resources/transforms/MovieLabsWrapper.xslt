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
				<link href="{$rsrcBase}../../style/movielab2.css" rel="stylesheet" type="text/css"/> 
				<link href="{$rsrcBase}styles/CMRatings.css" rel="stylesheet" type="text/css"/> 
				<link href="{$rsrcBase}../../style/drop-down-menu.css" rel="stylesheet"	type="text/css" />
				<link href="{$rsrcBase}styles/hint.css" rel="stylesheet" type="text/css" />
				<script src="{$rsrcBase}scripts/displaySupport.js" type="text/javascript"></script>
	</xsl:template>
	<xsl:template name="topPanel">   
							<table width="800" align="center" height="130" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="200" valign="top">
										<a href="{$rsrcBase}../index.html">
											<img src="{$rsrcBase}../../images/logo_movielabs.jpg" alt="MovieLab" width="200" height="130" border="0"/>
										</a>
									</td>
									<td width="600" background="{$rsrcBase}../../images/home_banner.jpg">
										<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
											<tbody>
												<tr>
													<td class="header">
														<div align="right">
															<a href="{$rsrcBase}../../AboutUs/contact.html">
																<span class="linkwhite">Contact Us</span>
															</a> | <a href="{$rsrcBase}../../termsofuse.html">
																<span class="linkwhite">Terms of Use</span>
															</a>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td>
													<img src="{$rsrcBase}../../images/bannertext_ratings.gif" alt="Common Metadata" width="600" height="100" class="ctrlIcon"/>
												</td>
											</tr>
										</table>
									</td>
								</tr> 
								<tr><td></td>
								<td><div id="menu-navigation">
								<ul>
									<li>
										<a href="{$menuBase}../../Mission/index.html" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Mission','','../../../../images/nav_mission_RO.gif',1)">
											<img src="{$rsrcBase}../../images/nav_mission.gif" alt="Mission" name="Mission" width="160" height="25" border="0" id="Mission" />
										</a>
									</li>
									<li>
										<a href="{$menuBase}../../Focus/index.html" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Areas of Focus','','../../../../images/nav_areasoffocus_RO.gif',1)">
											<img src="{$rsrcBase}../../images/nav_areasoffocus.gif" alt="Areas of Focus" name="Areas of Focus" width="160" height="25" border="0" id="Areas of Focus" />
										</a>
									</li>
									<li>
										<a href="{$menuBase}../../initiatives/index.html" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Initiatives','','../../../../images/nav_initiatives_RO.gif',1)">
											<img src="{$rsrcBase}../../images/nav_initiatives.gif" alt="Initiatives" name="Initiatives" width="160" height="25" border="0" id="Initiatives" />
										</a>
										<ul>
											<li>
												<a href="{$menuBase}../../initiatives/index.html">Representative Initiatives</a>
											</li>
											<li>
												<a href="{$menuBase}index.html">Metadata</a>
											</li>
											<li>
												<a href="{$menuBase}../../tdl/index.html">TDL</a>
											</li>
											<li>
												<a href="http://www.eidr.org">EIDR</a>
											</li>
											<li>
												<a href="{$menuBase}../../education/index.html">Education</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="{$menuBase}../../Partners/index.html" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Partners','','../../../../images/nav_partners_RO.gif',1)">
											<img src="{$rsrcBase}../../images/nav_partners.gif" alt="Partners" name="Partners" width="160" height="25" border="0" id="Partners" />
										</a>
									</li>
									<li>
										<a href="{$menuBase}../../AboutUs/index.html" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('About Us','','../../../../images/nav_aboutus_RO.gif',1)">
											<img src="{$rsrcBase}../../images/nav_aboutus.gif" alt="About Us" name="About Us" width="160" height="25" border="0" id="About Us" />
										</a>
										<ul>
											<li>
												<a href="{$menuBase}../../AboutUs/team.html">Team</a>
											</li>
											<li>
												<a href="{$menuBase}../../AboutUs/contact.html">Contact Us</a>
											</li>
											<li>
												<a href="{$menuBase}../../AboutUs/press.html">Press</a>
											</li>
											<li>
												<a href="{$menuBase}../../AboutUs/career.html">Career</a>
											</li>
										</ul>
									</li>
								</ul>
							</div></td></tr>
							</table>
							<!--  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
							 
	</xsl:template>
	<xsl:template name="leftSide">
										<ul>
											<li>
												<a href="{$menuBase}index.html">Home</a>
											</li>
											<li>
												<a href="{$menuBase}md/">Common Metadata</a>
											</li>
											<li>
												<a href="{$menuBase}ratings/"> Ratings</a>
											</li>
											<li>
												<a href="{$menuBase}mec/">Media Entertainment Core</a>
											</li>
											<li>
												<a href="{$menuBase}avails/">Avails</a>
											</li>
											<li>
												<a href="{$menuBase}extras/">Extras</a>
											</li>
											<li>
												<a href="{$menuBase}../../crmd/">Content Recognition Metadata</a>
											</li>
										</ul> 
	</xsl:template>
	<xsl:template name="stdFooter">
							<div align="center">
								<hr width="100%" size="1"/>
								<p>
									<a href="{$rsrcBase}../../AboutUs/contact.html">Contact Us</a> | <a href="{$rsrcBase}../../proposal.html">Submit
                        a Technical Proposal </a> | <a href="{$rsrcBase}../../Challenge/index.html">Technology
                        Open Challenge</a> | <a href="{$rsrcBase}../../AboutUs/press.html">Press</a> | <a href="{$rsrcBase}../../termsofuse.html">Terms
                        of Use</a> | <a href="{$rsrcBase}../../privacy.html">Privacy Policy</a>
									<br/>  2014 Motion Picture Laboratories, Inc. All rights reserved.<br/>
									<br/>
									<br/>
								</p>
							</div> 
	</xsl:template>
</xsl:stylesheet>
