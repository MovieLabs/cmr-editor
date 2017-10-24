/**
 * 
 * Copyright Motion Picture Laboratories, Inc. 2013
 * All Right Reserved
 *
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.movielabs.cmr.client.rspec;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.input.sax.XMLReaderXSDFactory;

import com.movielabs.cmr.client.RatingsEditor;
//import com.movielabs.cmr.client.util.CountryCode;
import com.movielabs.cmr.client.widgets.rspec.RatingSystemPanel;
import com.movielabs.cmr.client.widgets.rspec.RatingsBin;
import com.movielabs.cmr.client.widgets.rspec.ReasonsBin;
import com.movielabs.cmr.client.widgets.rspec.UsageBin;
import com.movielabs.cmr.client.util.TimeStamp;

/**
 * Handles display and persistence of System Properties
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class RatingSystem extends SpecificationElement implements RSpecLeaf {
	private RatingsEditor mainApp;
	private RatingSystemPanel uiWidget;
	private boolean autoGenUri = false;

	// .... start of properties...
	private String name = "";
	private int version = 0;
	private boolean isValid = false;
	private Date lastChecked = null;
	private Date lastSaved;
	private Date lastValidated;
	private Date lastHtmlGen;
	private boolean deprecated = false;

	private String uri;
	private Region adminRegion;
	private List<AdoptiveRegion> usageRegions = new ArrayList<AdoptiveRegion>();
	private Organization org;
	private DefaultMutableTreeNode usageNode = new UsageBin();
	// private List<AdoptiveRegion> usageList = new ArrayList<AdoptiveRegion>();
	private DefaultMutableTreeNode ratingNode = new RatingsBin();
	private List<Rating> ratingList = new ArrayList<Rating>();
	private DefaultMutableTreeNode reasonNode = new ReasonsBin();
	private List<ReasonDescriptor> reasonList = new ArrayList<ReasonDescriptor>();
	private ArrayList<String> reportedProblems;
	private String notesAndComments;

	/**
	 * @param xmlFile
	 * @param validate
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static RatingSystem loadFromXml(File xmlFile, boolean validate) throws Exception {
		File xsdfile = new File("./resources/" + schemaFile);
		InputStreamReader isr = new InputStreamReader(new FileInputStream(xmlFile), "UTF-8");
		XMLReaderJDOMFactory readerFactory;
		if (validate) {
			readerFactory = new XMLReaderXSDFactory(xsdfile);
		} else {
			readerFactory = new XMLReaderSAX2Factory(false);
		}
		SAXBuilder builder = new SAXBuilder(readerFactory);
		Document validatedDoc;
		validatedDoc = builder.build(isr);
		Element rootEl = validatedDoc.getRootElement();
		return new RatingSystem(rootEl);
	}

	public RatingSystem() {
		super();
		mainApp = RatingsEditor.getEditor();
		adminRegion = new Region();
		setName("-t.b.d.-");
		org = new Organization();
		uiWidget = new RatingSystemPanel(this);
		// Hook up the JTree support...
		this.add(usageNode);
		this.add(ratingNode);
		this.add(reasonNode);
	}

	public RatingSystem(Element xmlEl) {
		super();
		mainApp = RatingsEditor.getEditor();
		// Hook up the JTree support...
		this.add(usageNode);
		this.add(ratingNode);
		this.add(reasonNode);
		// now load XML
		initFromXml(xmlEl);
		uiWidget = new RatingSystemPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		if (uiWidget != null) {
			// uiWidget.
		}
	}

	/**
	 * @param xmlEl
	 */
	private void initFromXml(Element xmlEl) {
		Element rSysIdEl = xmlEl.getChild("RatingSystemID", mdcrNSpace);
		List<Element> regionList = rSysIdEl.getChildren("Region", mdcrNSpace);
		for (int i = 0; i < regionList.size(); i++) {
			// NEW schema will restrict this to a single Region
			adminRegion = new Region();
			adminRegion.initFromXml(regionList.get(i));
		}
		/*
		 * Set name AFTER initializing the adminregion, otherwise the URI
		 * generation triggered by setName will fail
		 */
		setName(rSysIdEl.getChildText("System", mdcrNSpace));
		// NEW schema.....
		List<Element> regionOfUsageList = xmlEl.getChildren("AdoptiveRegion", mdcrNSpace);
		for (int i = 0; i < regionOfUsageList.size(); i++) {
			addUsage(regionOfUsageList.get(i));
		}
		/*
		 * Has it been verified against latest data available from the Rating
		 * Org responsible for this classification system?
		 */
		Element lastCheckEl = xmlEl.getChild("LastChecked", mdcrNSpace);
		if (lastCheckEl != null) {
			String lcText = lastCheckEl.getText();
			lastChecked = TimeStamp.fromXsDate(lcText);
		}

		Element systemUriEl = xmlEl.getChild("URI", mdcrNSpace);
		if (systemUriEl != null) {
			uri = systemUriEl.getText();
		}

		Element notesEl = xmlEl.getChild("Notes", mdcrNSpace);
		// if (Notes != null) {
		// notesAndComments = Notes.getText();
		// }else{
		// notesAndComments = "";
		// }
		if (notesEl != null) {
			notesAndComments = loadInnerHtml(notesEl);
		}

		// initScopeFromXml(xmlEl);
		org = new Organization(xmlEl.getChild("RatingsOrg", mdcrNSpace));
		/*
		 * now instantiate individual Rating elements
		 */
		List<Element> ratingEl = xmlEl.getChildren("Rating", mdcrNSpace);
		sorter: for (int i = 0; i < ratingEl.size(); i++) {
			Rating rating = new Rating(this, ratingEl.get(i));
			/*
			 * crude but effective sorting that works since # of ratings is
			 * small.
			 */
			if (ratingList.isEmpty()) {
				ratingList.add(rating);
			} else {
				// place in sorted order
				for (int j = ratingList.size() - 1; j >= 0; j--) {
					Rating other = ratingList.get(j);
					if (rating.compare(other) < 0) {
						ratingList.add(j + 1, rating);
						continue sorter;
					} else if (rating.compare(other) == 0) {
						/* place a DEPRECATED rating AFTER an active one */
						if (rating.isDeprecated()) {
							ratingList.add(j + 1, rating);
						} else {
							ratingList.add(j, rating);
						}
						continue sorter;
					} else if (j == 0) {
						ratingList.add(j, rating);
					}
				}
			}
		}

		for (int i = 0; i < ratingList.size(); i++) {
			ratingNode.add(ratingList.get(i));
		}
		/*
		 * Now do the 'Reason' elements. This is much simpler that the Ratings
		 * since they have no order and there is therefore no need to sort.
		 */
		List<Element> reasonEl = xmlEl.getChildren("Reason", mdcrNSpace);
		for (int i = 0; i < reasonEl.size(); i++) {
			addReason(reasonEl.get(i));
		}

		/*
		 * add version control attributes
		 */
		String temp = rSysIdEl.getAttributeValue("version", "0");
		version = Integer.parseInt(temp);
		temp = rSysIdEl.getAttributeValue("deprecated", "false");
		deprecated = temp.equalsIgnoreCase("true");

		temp = xmlEl.getAttributeValue("lastSave", "");
		lastSaved = TimeStamp.fromXsDateTime(temp);

		temp = xmlEl.getAttributeValue("lastValidated", "");
		lastValidated = TimeStamp.fromXsDateTime(temp);

		temp = xmlEl.getAttributeValue("lastHtmlGen", "");
		lastHtmlGen = TimeStamp.fromXsDateTime(temp);
	}

	/**
	 * @return
	 */
	public Element asXml() {
		clearErrors();
		uiWidget.updateDao();
		Element ratingSysRoot = new Element("RatingSystem", mdcrNSpace);
		ratingSysRoot.addNamespaceDeclaration(mdNSpace);
		ratingSysRoot.addNamespaceDeclaration(xsiNSpace);
		ratingSysRoot.setAttribute("schemaLocation", schemaLoc, xsiNSpace);
		// RatingSystemID:
		Element rSysIdEl = new Element("RatingSystemID", mdcrNSpace);
		ratingSysRoot.addContent(rSysIdEl);
		rSysIdEl.addContent(adminRegion.asXml());
		Element sysEl = new Element("System", mdcrNSpace);
		sysEl.setText(name);
		rSysIdEl.addContent(sysEl);

		for (int i = 0; i < usageRegions.size(); i++) {
			Element regionEl = usageRegions.get(i).asXml();
			if (regionEl != null) {
				ratingSysRoot.addContent(regionEl);
			}
		}

		if (lastChecked != null) {
			Element lcEl = new Element("LastChecked", mdcrNSpace);
			lcEl.setText(TimeStamp.asXsDate(lastChecked));
			ratingSysRoot.addContent(lcEl);
		}

		/*
		 * URI..
		 */
		// make sure its up-to-date
		genUri();
		Element uriEl = new Element("URI", mdcrNSpace);
		uriEl.setText(uri);
		ratingSysRoot.addContent(uriEl);

		/*
		 * Add notes and comments. The editor saves files in a slightly modified
		 * schema, then exports the XML in conformance with the official one.
		 * Than means we can persist info that doesn't get passed to UltraViolet
		 * and similar consumers of the XML. Any comments, notes, or
		 * documentation that is strictly for human consumption would still
		 * available via the HTML pages. Thus we use an XML element here that is
		 * NOT in the export XML.
		 */
		if (notesAndComments != null && !notesAndComments.isEmpty()) {
			Element noteEl = new Element("Notes", mdcrNSpace);
			try {
				Element innnerHtml = parseInnerHtml(notesAndComments);
				noteEl.addContent(innnerHtml);
			} catch (Exception e) {
				e.printStackTrace();
				noteEl.setText(notesAndComments);
			}
			ratingSysRoot.addContent(noteEl);
		}

		/*
		 * Ratings Org
		 */
		ratingSysRoot.addContent(org.asXml());
		/*
		 * now add individual Rating elements
		 */
		for (int i = 0; i < ratingList.size(); i++) {
			Rating rating = ratingList.get(i);
			ratingSysRoot.addContent(rating.asXml());
		}
		/*
		 * now add individual Reason elements
		 */
		for (int i = 0; i < reasonList.size(); i++) {
			ReasonDescriptor reason = reasonList.get(i);
			ratingSysRoot.addContent(reason.asXml());
		}
		/*
		 * add attributes
		 */
		rSysIdEl.setAttribute("version", Integer.toString(version));
		if (deprecated) {
			rSysIdEl.setAttribute("deprecated", "true");
		}
		if (lastValidated != null) {
			ratingSysRoot.setAttribute("lastValidated", TimeStamp.asXsDateTime(lastValidated));
		}
		if (lastHtmlGen != null) {
			ratingSysRoot.setAttribute("lastHtmlGen", TimeStamp.asXsDateTime(lastHtmlGen));
		}
		if (lastSaved != null) {
			ratingSysRoot.setAttribute("lastSave", TimeStamp.asXsDateTime(lastSaved));
		}
		// ....... done
		if (confirmOperation()) {
			return ratingSysRoot;
		} else {
			return null;
		}
	}

	/**
	 * 
	 */
	private void clearErrors() {
		reportedProblems = new ArrayList<String>();
	}

	/**
	 * @param specElement
	 * @param details
	 */
	public void reportException(SpecificationElement specElement, String details) {
		reportedProblems.add(specElement.toString() + ":: " + details);
	}

	/**
	 * @return
	 */
	private boolean confirmOperation() {
		if (reportedProblems.isEmpty()) {
			return true;
		}
		// check with user..
		String msg = reportedProblems.size() + " errors found. Continue saving?";
		for (int i = 0; i < reportedProblems.size(); i++) {
			msg = msg + "\n" + reportedProblems.get(i);
		}
		int n = JOptionPane.showConfirmDialog(uiWidget.getParent(), msg, "Confirm SAVE", JOptionPane.YES_NO_OPTION);
		return (n == 0);
	}

	/**
	 * 
	 */
	public void incrementVersion() {
		version++;
		genUri();
		if (uiWidget != null) {
			uiWidget.getVersionField().setText(Integer.toString(version));
		}
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return Integer.toString(version);
	}

	/**
	 * @return
	 * 
	 */
	public Rating addRating(Element xml) {
		return insertRating(xml, ratingList.size());
	}

	/**
	 * @return
	 * 
	 */
	public Rating insertRating(Element xml, int index) {
		Rating rating = new Rating(this, xml);
		ratingList.add(index, rating);
		ratingNode.add(rating);
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
		return rating;
	}

	/**
	 * @param rating
	 */
	public void delete(Rating rating) {
		ratingList.remove(rating);
	}

	/**
	 * @return
	 */
	public List<Rating> getAllRatings() {
		return ratingList;
	}

	/**
	 * Locates and returns a <tt>Rating</tt> using the <tt>ratingID</tt> value.
	 * If a matching <tt>Rating</tt> is not found a <tt>null</tt> value will be
	 * returned.
	 * 
	 * @param id
	 * @return
	 */
	public Rating getRating(String id) {
		for (int i = 0; i < ratingList.size(); i++) {
			Rating next = ratingList.get(i);
			if (next.getRatingID().equals(id)) {
				return next;
			}
		}
		return null;
	}

	/**
	 * @param element
	 */
	public AdoptiveRegion addUsage(Element xml) {
		return insertUsage(xml, usageRegions.size());
	}

	/**
	 * @param element
	 * @return
	 */
	public AdoptiveRegion insertUsage(Element xml, int index) {
		AdoptiveRegion nextReg = new AdoptiveRegion(xml);
		usageRegions.add(index, nextReg);
		usageNode.add(nextReg);
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
		return nextReg;
	}

	/**
	 * @param rating
	 */
	public void delete(AdoptiveRegion usage) {
		usageRegions.remove(usage);
	}

	/**
	 * @return
	 * 
	 */
	public ReasonDescriptor addReason(Element xml) {
		return insertReason(xml, reasonList.size());
	}

	/**
	 * @return
	 * 
	 */
	public ReasonDescriptor insertReason(Element xml, int index) {
		ReasonDescriptor reason = new ReasonDescriptor(this, xml);
		reasonList.add(index, reason);
		reasonNode.add(reason);
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
		handleReasonSetChange();
		return reason;
	}

	/**
	 * @param reason
	 */
	public void deleteReason(ReasonDescriptor reason) {
		reasonList.remove(reason);
		handleReasonSetChange();
	}

	/**
	 * 
	 */
	private void handleReasonSetChange() {
		// notify all Rating elements
		for (int i = 0; i < ratingList.size(); i++) {
			ratingList.get(i).reasonSetChange(reasonList);
		}
	}

	/**
	 * @return
	 */
	public List<ReasonDescriptor> getAllReasons() {
		return reasonList;
	}

	public String toString() {
		return name;
	}

	/**
	 * 
	 */
	private void genUri() {
		if (autoGenUri) {
			generateURI();
		}
	}

	public void generateURI() {
		uri = baseUrl + adminRegion.getIsoCode() + UriSep + makeSafeForURI(name) + UriSep
				+ makeSafeForURI(getVersion());
		if (uiWidget != null) {
			uiWidget.getTextFieldUri().setText(getUri());
		}

		for (int i = 0; i < ratingList.size(); i++) {
			Rating rating = ratingList.get(i);
			rating.generateURI();
		}

		/*
		 * now add individual Reason elements
		 */
		for (int i = 0; i < reasonList.size(); i++) {
			ReasonDescriptor reason = reasonList.get(i);
			reason.generateURI();
		}
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param childText
	 */
	public void setName(String systemId) {
		name = systemId;
		genUri();
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#getUiWidget()
	 */
	@Override
	public Component getUiWidget() {
		return uiWidget;
	}

	/**
	 * @return ISO3166 2-character code
	 */
	public String getCountry(int index) {
		// if (index < regions.size()) {
		// return regions.get(index).getIsoCode();
		// } else {
		return getAdminRegion();
		// }
	}

	public String getAdminRegion() {
		return adminRegion.getIsoCode();
	}

	/**
	 * @param isoCode
	 */
	public void setCountry(String isoCode, int index) {
		// if (index < regions.size()) {
		// regions.get(index).setIsoCode(isoCode);
		// } else {
		// Region addedRegion = new Region();
		// addedRegion.setIsoCode(isoCode);
		// regions.add(addedRegion);
		// }
		// genUri();
		throw new UnsupportedOperationException();
	}

	public void setAdminRegion(String isoCode) {
		adminRegion.setIsoCode(isoCode);
		genUri();
	}

	/**
	 * @param subRegIdent
	 */
	public void setSubRegion(String subRegIdent, int index) {
		// if (index < regions.size()) {
		// regions.get(index).setSubRegion(subRegIdent);
		// genUri();
		// }
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 */
	public String getSubRegion(int index) {
		// if (index < regions.size()) {
		// return regions.get(index).getSubRegion();
		// } else {
		return null;
		// }
	}

	/**
	 * @return
	 * 
	 */
	public Organization getOrganization() {
		return org;
	}

	/**
	 * @return the autoGenUri
	 */
	public boolean isAutoGenUri() {
		return autoGenUri;
	}

	/**
	 * @param autoGenUri
	 *            the autoGenUri to set
	 */
	public void setAutoGenUri(boolean autoGenUri) {
		this.autoGenUri = autoGenUri;
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 *            the isValid to set
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the lastSaved
	 */
	public Date getLastSaved() {
		return lastSaved;
	}

	/**
	 * @param lastSaved
	 *            the lastSaved to set
	 */
	public void setLastSaved(Date lastSaved) {
		this.lastSaved = lastSaved;
	}

	/**
	 * @return the lastValidated
	 */
	public Date getLastValidated() {
		return lastValidated;
	}

	/**
	 * @param lastValidated
	 *            the lastValidated to set
	 */
	public void setLastValidated(Date lastValidated) {
		this.lastValidated = lastValidated;
		setValid(lastValidated != null);
	}

	/**
	 * @return the lastChecked
	 */
	public Date getLastChecked() {
		return lastChecked;
	}

	/**
	 * @param lastValidated
	 *            the lastValidated to set
	 */
	public void setLastChecked(Date lastChecked) {
		this.lastChecked = lastChecked;
	}

	/**
	 * @return the lastHtmlGen
	 */
	public Date getLastHtmlGen() {
		return lastHtmlGen;
	}

	/**
	 * @param lastHtmlGen
	 *            the lastHtmlGen to set
	 */
	public void setLastHtmlGen(Date lastHtmlGen) {
		this.lastHtmlGen = lastHtmlGen;
	}

	/**
	 * @return the deprecated
	 */
	public boolean isDeprecated() {
		return deprecated;
	}

	/**
	 * @param deprecated
	 *            the deprecated to set
	 */
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setNotes(String notes) {
		this.notesAndComments = notes;
	}

	public String getNotes() {
		return notesAndComments;
	}
}
