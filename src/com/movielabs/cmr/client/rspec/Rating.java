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
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdom2.Element;

import com.movielabs.cmr.client.RatingsEditor;
import com.movielabs.cmr.client.util.LocalizedText;
import com.movielabs.cmr.client.widgets.rspec.DefaultRatingPanel;
import com.movielabs.cmr.client.widgets.rspec.RatingPanel;
import com.movielabs.cmr.client.widgets.rspec.UsageBin;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class Rating extends SpecificationElement implements RSpecLeaf {

	String ratingID = "t.b.d.";
	String uri = "";
	List<LocalizedText> labels = new ArrayList<LocalizedText>();
	private RatingsEditor mainApp;
	private RatingPanel uiWidget;;
	private boolean autoGenUri;
	private int ordinal = 0;
	private int minRecAge = 0;
	private int minAge = 0;
	private int minAgeSupervised = 0;
	private List<String> logoUrl = new ArrayList<String>();
	private boolean hpcApplicable = true;
	private boolean deprecated = false;
	private HashSet<String> applicableReasons = new HashSet<String>();
	private List<RatingDescriptor> descriptorList = new ArrayList();
	private List<AdoptiveRegion> usageRegions = new ArrayList<AdoptiveRegion>();
	private DefaultMutableTreeNode usageNode = new UsageBin();

	// private EnumSet<MEDIA> targetMedia = EnumSet.noneOf(MEDIA.class);
	// private EnumSet<ENVIRONMENT> targetEnviron = EnumSet
	// .noneOf(ENVIRONMENT.class);

	/**
	 * @param ratingSystem
	 * @param xmlEl
	 */
	public Rating(RatingSystem ratingSystem, Element xmlEl) {
		super();
		this.ratingSystem = ratingSystem;
		autoGenUri = ratingSystem.isAutoGenUri();
		mainApp = RatingsEditor.getEditor();
		if (xmlEl != null) {
			initFromXml(xmlEl);
		}
		// uiWidget = new RatingPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		genUri();
		if (uiWidget != null) {
			// uiWidget.syncWithDao();
		}
	}

	/**
	 * @param xmlEl
	 */
	private void initFromXml(Element ratingEl) {
		Element valueEl = ratingEl.getChild("Value", mdcrNSpace);
		setRatingID(valueEl.getText());
		String ordAsString = valueEl.getAttributeValue("ordinal", "0");
		try {
			int ordvalue = Integer.parseInt(ordAsString);
			setOrdinal(ordvalue);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		setUri(ratingEl.getChildText("URI", mdcrNSpace));

		// everything else is optional
		List<Element> logoList = ratingEl.getChildren("LinkToLogo", mdcrNSpace);
		if (!logoList.isEmpty()) {
			for (int i = 0; i < logoList.size(); i++) {
				logoUrl.add(logoList.get(i).getText());
			}
		}
		Element childEl = ratingEl.getChild("MinRecAge", mdcrNSpace);
		minRecAge = extractInt(childEl);
		childEl = ratingEl.getChild("MinAge", mdcrNSpace);
		minAge = extractInt(childEl);
		childEl = ratingEl.getChild("MinAgeSupervised", mdcrNSpace);
		minAgeSupervised = extractInt(childEl);
		childEl = ratingEl.getChild("HPCApplicable", mdcrNSpace);
		if (childEl != null) {
			hpcApplicable = childEl.getText().equalsIgnoreCase("true");
		}
		childEl = ratingEl.getChild("Deprecated", mdcrNSpace);
		if (childEl != null) {
			deprecated = childEl.getText().equalsIgnoreCase("true");
		}
		initScopeFromXml(ratingEl);
		/*
		 * now instantiate language-specific descriptions
		 */
		List<Element> descEl = ratingEl.getChildren("Descriptor", mdcrNSpace);
		for (int i = 0; i < descEl.size(); i++) {
			addDesc(descEl.get(i));
		}
		/*
		 * Which Reasons (a.k.a. 'content descriptors' are applicable for usage
		 * with this rating?
		 */
		// List<Element> reasonEl = ratingEl.getChildren("ApplicableReason",
		// mdcrNSpace);
		// for (int i = 0; i < reasonEl.size(); i++) {
		// String id = reasonEl.get(i).getAttributeValue("id");
		// if (id != null && !id.isEmpty()) {
		// applicableReasons.add(id);
		// }
		// }
	}

	protected void initScopeFromXml(Element xmlEl) {
		// NEW schema.....
		List<Element> regionOfUsageList = xmlEl.getChildren("Override",
				mdcrNSpace);
		for (int i = 0; i < regionOfUsageList.size(); i++) {
			addUsageOverride(regionOfUsageList.get(i));
		} 
	}

	/**
	 * @param element
	 */
	public AdoptiveRegion addUsageOverride(Element xml) {
		return insertUsageOverride(xml, usageRegions.size());
	}

	/**
	 * @param element
	 * @return
	 */
	public AdoptiveRegion insertUsageOverride(Element xml, int index) {
		AdoptiveRegion nextReg = new AdoptiveRegion(xml, false);
		usageRegions.add(index, nextReg);
		usageNode.add(nextReg);
		TreeNode parent = usageNode.getParent();
		if (parent == null) {
			// Hook up the JTree support...
			this.add(usageNode);
		}
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
		return nextReg;
	}


	/**
	 * @param rating
	 */
	public void deleteUsageOverride(AdoptiveRegion usage) {
		usageRegions.remove(usage);
		if(usageRegions.isEmpty()){
			this.remove(usageNode);
		}
	}
	/**
	 * @param object
	 * @return
	 */
	public RatingDescriptor addDesc(Element xmlObject) {
		RatingDescriptor rDesc = new RatingDescriptor(this, xmlObject);
		descriptorList.add(rDesc);
		// uncomment next line if you want to add to JTree as well...
		// this.add(rDesc);

		return rDesc;
	}

	/**
	 * @param lang
	 * @return
	 */
	public RatingDescriptor addDesc(String lang) {
		RatingDescriptor rDesc = new RatingDescriptor(this, lang);
		descriptorList.add(rDesc);
		// uncomment next line if you want to add to JTree as well...
		// this.add(rDesc);
		if (uiWidget != null) {
			uiWidget.syncWithDao();
		}
		return rDesc;
	}

	/**
	 * @param rDesc
	 */
	public void delete(RatingDescriptor rDesc) {
		descriptorList.remove(rDesc);
		if (uiWidget != null) {
			uiWidget.removeDescriptor(rDesc);
		}
	}

	public Element asXml() {
		return asXml(false);
	}

	/**
	 * If <tt>asClone==true</tt> any ID fields are altered (e.g.,
	 * <tt>xEl.setText("dup-" + ratingID);</tt>
	 * 
	 * @param asClone
	 * @return
	 */
	public Element asXml(boolean asClone) {
		getUiWidget(); // make sure it exists
		uiWidget.updateDao();
		Element ratingRoot = new Element("Rating", mdcrNSpace);
		Element xEl = new Element("Value", mdcrNSpace);
		if (asClone) {
			xEl.setText("dup-" + ratingID);
		} else {
			xEl.setText(ratingID);
		}
		xEl.setAttribute("ordinal", Integer.toString(getOrdinal()));
		ratingRoot.addContent(xEl);
		// optional...
		genUri();
		if (!uri.isEmpty()) {
			xEl = new Element("URI", mdcrNSpace);
			xEl.setText(uri);
			ratingRoot.addContent(xEl);
		}
		// optional...
		if (!logoUrl.isEmpty()) {
			for (int i = 0; i < logoUrl.size(); i++) {
				xEl = new Element("LinkToLogo", mdcrNSpace);
				String urlString = logoUrl.get(i);
				if (urlString != null && !urlString.isEmpty()) {
					xEl.setText(logoUrl.get(i));
					ratingRoot.addContent(xEl);
				}
			}
		}
		// optional...
		if (minRecAge > -1) {
			xEl = new Element("MinRecAge", mdcrNSpace);
			xEl.setText(Integer.toString(minRecAge));
			ratingRoot.addContent(xEl);
		}
		// optional...
		if (minAge > -1) {
			xEl = new Element("MinAge", mdcrNSpace);
			xEl.setText(Integer.toString(minAge));
			ratingRoot.addContent(xEl);
		}
		// optional...
		if (minAgeSupervised > -1) {
			xEl = new Element("MinAgeSupervised", mdcrNSpace);
			xEl.setText(Integer.toString(minAgeSupervised));
			ratingRoot.addContent(xEl);
		}
		if (deprecated) {
			xEl = new Element("Deprecated", mdcrNSpace);
			xEl.setText("true");
			ratingRoot.addContent(xEl);
		}
		// NEW............

		addScopeAsXml(ratingRoot);
		/*
		 * XSD specifies this as a REQUIRED element that appears immediately
		 * before the 1st Descriptor.
		 */
		xEl = new Element("HPCApplicable", mdcrNSpace);
		if (hpcApplicable) {
			xEl.setText("true");
		} else {
			xEl.setText("false");
		}
		ratingRoot.addContent(xEl);
		/*
		 * next step is adding all the Descriptors
		 */
		for (int i = 0; i < descriptorList.size(); i++) {
			RatingDescriptor rDesc = descriptorList.get(i);
			ratingRoot.addContent(rDesc.asXml());
		}
		/*
		 * Which Reasons (a.k.a. 'content descriptors' are applicable for usage
		 * with this rating?
		 */
		List<String> arList = new ArrayList<String>();
		arList.addAll(applicableReasons);
		for (int i = 0; i < arList.size(); i++) {
			xEl = new Element("ApplicableReason", mdcrNSpace);
			xEl.setAttribute("id", arList.get(i));
			ratingRoot.addContent(xEl);
		}
		return ratingRoot;
	}

	/**
	 * @param localRoot
	 */
	protected void addScopeAsXml(Element localRoot) {
		for (int i = 0; i < usageRegions.size(); i++) {
			Element regionEl = usageRegions.get(i).asXml();
			if (regionEl != null) {
				regionEl.setName("Override");
				localRoot.addContent(regionEl);
			}
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#getUiWidget()
	 */
	@Override
	public Component getUiWidget() {
		if (uiWidget == null) {
			uiWidget = new DefaultRatingPanel(this);
		}
		return (Component) uiWidget;
	}

	/**
	 * @return the ID
	 */
	public String toString() {
		return ratingID;
	}

	/**
	 * @return the ratingID
	 */
	public String getRatingID() {
		return ratingID;
	}

	/**
	 * @param ratingID
	 *            the ratingID to set
	 */
	public void setRatingID(String ratingID) {
		if (this.ratingID.equals(ratingID)) {
			return;
		}
		this.ratingID = ratingID;
		genUri();
		resyncUi();
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}

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
		uri = ratingSystem.getUri() + UriSep + makeSafeForURI(ratingID);
		if (uiWidget != null) {
			uiWidget.syncUri();
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
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal
	 *            the ordinal to set
	 */
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return the minRecAge
	 */
	public int getMinRecAge() {
		return minRecAge;
	}

	/**
	 * @param minRecAge
	 *            the minRecAge to set
	 */
	public void setMinRecAge(int minRecAge) {
		this.minRecAge = minRecAge;
	}

	/**
	 * @return the minAge
	 */
	public int getMinAge() {
		return minAge;
	}

	/**
	 * @param minAge
	 *            the minAge to set
	 */
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	/**
	 * @return the minAgeSupervised
	 */
	public int getMinAgeSupervised() {
		return minAgeSupervised;
	}

	/**
	 * @param minAgeSupervised
	 *            the minAgeSupervised to set
	 */
	public void setMinAgeSupervised(int minAgeSupervised) {
		this.minAgeSupervised = minAgeSupervised;
	}

	/**
	 * @return the deprecated
	 */
	public boolean isDeprecated() {
		return deprecated;
	}

	/**
	 * @param deprecated
	 *            the boolean value to set
	 */
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	/**
	 * Indicates if the Rating is applicable to usage in a home Parental Control
	 * system. This is intended as a hint for systems such as UltraViolet and is
	 * not intended as a requirement regarding the usage of the Rating in a HPC
	 * implementation.
	 * 
	 * @return the hpcApplicable
	 */
	public boolean isHpcApplicable() {
		return hpcApplicable;
	}

	/**
	 * @param hpcApplicable
	 *            the boolean value to set
	 */
	public void setHpcApplicable(boolean hpcApplicable) {
		this.hpcApplicable = hpcApplicable;
	}

	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl(int i) {
		if (i < logoUrl.size()) {
			return logoUrl.get(i);
		} else {
			return "";
		}
	}

	/**
	 * @param logoUrl
	 *            the logoUrl to set
	 */
	public void setLogoUrl(String url, int index) {
		if (index < logoUrl.size()) {
			this.logoUrl.set(index, url);
		} else {
			this.logoUrl.add(index, url);
		}

	}

	// /**
	// * @return the restriction
	// */
	// public boolean isRestriction() {
	// return restriction;
	// }
	//
	// /**
	// * @param restriction
	// * the restriction to set
	// */
	// public void setRestriction(boolean restriction) {
	// this.restriction = restriction;
	// }

	/**
	 * @return the labels
	 */
	public List<LocalizedText> getLabels() {
		return labels;
	}

	/**
	 * @return the descriptorList
	 */
	public List<RatingDescriptor> getDescriptorList() {
		return descriptorList;
	}


	/**
	 * Compares ranking (i.e., <tt>ordinal</tt> of other Rating to current
	 * instance. The value returned is the amount by which the <i>other</i>
	 * Rating's ordinal exceeds this Rating's ordinal value.
	 * 
	 * @param other
	 * @return
	 */
	int compare(Rating other) {
		int delta = other.getOrdinal() - this.getOrdinal();
		return delta;
	}

	/**
	 * @param reasonList
	 */
	public void reasonSetChange(List<ReasonDescriptor> reasonList) {
		((RatingPanel) getUiWidget()).resetReasonPanel();
	}

	public boolean isApplicableReason(String id) {
		return applicableReasons.contains(id);
	}

	/**
	 * @param text
	 * @param selected
	 */
	public void setApplicableReason(String id, boolean selected) {
		if (selected) {
			applicableReasons.add(id);
		} else {
			applicableReasons.remove(id);
		}
	}

	/**
	 * @param reasonDescriptor
	 * @param b
	 */
	public void linkTo(ReasonDescriptor reason, boolean appliesTo) {
		setApplicableReason(reason.getReasonID(), appliesTo);
	}

	/**
	 * @return
	 */
	public RatingSystem getRatingSystem() {
		return ratingSystem;
	}

}
