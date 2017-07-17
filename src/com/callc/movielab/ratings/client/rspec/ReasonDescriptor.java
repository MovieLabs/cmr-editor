/**
 * 
 * Copyright Critical Architectures, LLC 2013
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
package com.callc.movielab.ratings.client.rspec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.widgets.rspec.ReasonPanel;

/**
 * 
 * @author L. J. Levin, created Nov 4, 2013
 * 
 */
public class ReasonDescriptor extends SpecificationElement implements RSpecLeaf {

	private String languageCode;
	private String definition = "";
	private String explanation = "";
	private List<String> logoUrl = new ArrayList<String>();
	private List<JCheckBox> ratingCBoxList = new ArrayList();
	private RatingsEditor mainApp;
	private ReasonPanel uiWidget; 
	private String reasonID = "t.b.d";
	private boolean autoGenUri = true;
	private String uri;
	private Map<Rating, Criteria> applicationMap = new HashMap<Rating, Criteria>();

	public ReasonDescriptor(RatingSystem ratingSystem, Element xmlEl) {
		super();
		this.ratingSystem = ratingSystem;
		mainApp = RatingsEditor.getEditor();
		if (xmlEl != null) {
			initFromXml(xmlEl);
		}
	}

	/**
	 * @param rating2
	 * @param lang
	 */
	public ReasonDescriptor(RatingSystem ratingSystem, String lang) {
		super();
		this.ratingSystem = ratingSystem;
		mainApp = RatingsEditor.getEditor();
		languageCode = lang;
	}

	/**
	 * @param xmlEl
	 */
	private void initFromXml(Element descriptorEl) {
		languageCode = descriptorEl.getAttributeValue("language", "");
		String idCode = descriptorEl.getAttributeValue("reasonID", "");
		setReasonID(idCode);

		// should be either/or for next two
		Element childEl = descriptorEl.getChild("Definition", mdcrNSpace);
		if (childEl != null) {
			definition = childEl.getText();
			Element explanationEl = descriptorEl.getChild("Explanation",
					mdcrNSpace);
			if (explanationEl != null) {
				explanation = loadInnerHtml(explanationEl); 
			}
		}
		// icons and symbols are optional
		List<Element> logoList = descriptorEl.getChildren("LinkToLogo",
				mdcrNSpace);
		if (!logoList.isEmpty()) {
			for (int i = 0; i < logoList.size(); i++) {
				logoUrl.add(logoList.get(i).getText());
			}
		}
		// Application to Ratings are optional but expected
		List<Element> criteriaList = descriptorEl.getChildren("Criteria",
				mdcrNSpace);
		if (!criteriaList.isEmpty()) {
			for (int i = 0; i < criteriaList.size(); i++) {
				Element applyCriteriaEl = criteriaList.get(i);
				Criteria applyCriteria = new Criteria(ratingSystem, this,
						applyCriteriaEl);
				add(applyCriteria);
				Rating rating = applyCriteria.getRating();
				applicationMap.put(rating, applyCriteria); 
				rating.linkTo(this, true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#asXml()
	 */
	public Element asXml() {
		return asXml(false);
	}

	/**
	 * Returns XML representation suitable for persisting or creating a shallow
	 * clone. If <tt>asClone == TRUE</tt> the <tt>reasonID</tt> (which is the
	 * field used as the UID for this class) will be modified by the insertion
	 * of the prefix <tt>dup-</tt>.
	 * 
	 * @param asClone
	 * @return
	 */
	public Element asXml(boolean asClone) {
		getUiWidget().updateDao();
		Element root = new Element("Reason", mdcrNSpace);
		// language stored in attribute as code
		root.setAttribute("language", "en");
		if (asClone) {
			root.setAttribute("reasonID", "dup-" + getReasonID());
		} else {
			root.setAttribute("reasonID", getReasonID());
		}
		Element xEl = new Element("Label", mdcrNSpace);
		root.addContent(xEl);
		if (!definition.isEmpty()) {
			xEl = new Element("Definition", mdcrNSpace);
			xEl.setText(definition);
			root.addContent(xEl);
		}
		if (!explanation.isEmpty()) {
			xEl = new Element("Explanation", mdcrNSpace);
			try {
				Element innnerHtml = parseInnerHtml(explanation); 
				xEl.addContent(innnerHtml);
			} catch ( Exception e) { 
				e.printStackTrace();
				xEl.setText(explanation);
			} 
			root.addContent(xEl);
		}
		// optional...
		if (!logoUrl.isEmpty()) {
			for (int i = 0; i < logoUrl.size(); i++) {
				xEl = new Element("LinkToLogo", mdcrNSpace);
				String urlString = logoUrl.get(i);
				if (urlString != null && !urlString.isEmpty()) {
					xEl.setText(logoUrl.get(i));
					root.addContent(xEl);
				}
			}
		}
		// add any Application Criteria (in order of Ratings)
		List<Rating> ratList = ratingSystem.getAllRatings();
		for (int i = 0; i < ratList.size(); i++) {
			Rating nextRating = ratList.get(i);
			Criteria nextCriteria = applicationMap.get(nextRating);
			if (nextCriteria != null) {
				Element cEl = nextCriteria.asXml();
				root.addContent(cEl);
			}
		}

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#getUiWidget()
	 */
	@Override
	public ReasonPanel getUiWidget() {
		if (uiWidget == null) {
			uiWidget = new ReasonPanel(this);
		}
		return uiWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		getUiWidget();
		if (uiWidget != null) {
			uiWidget.syncWithDao();
		}
		for (int i = 0; i < ratingCBoxList.size(); i++) {
			JCheckBox next = ratingCBoxList.get(i);
			next.setText(reasonID);
		}
	}

	/**
	 * @return the ID
	 */
	public String toString() {
		return reasonID;
	}

	/**
	 * @return the languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @param languageCode
	 *            the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition
	 *            the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * 
	 */
	private void genUri() {
		if (autoGenUri) {
			uri = ratingSystem.getUri() + UriSep+"Reason"+UriSep + makeSafeForURI(reasonID);
			getUiWidget();
			if (uiWidget != null) {
				uiWidget.syncUri();
			}
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
	 * @return the reasonID
	 */
	public String getReasonID() {
		return reasonID;
	}

	/**
	 * @param reasonID
	 *            the reasonID to set
	 */
	public void setReasonID(String reasonID) {
		if (this.reasonID.equals(reasonID)) {
			return;
		}
		this.reasonID = reasonID;
		genUri();
		resyncUi();
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
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

	/**
	 * @param addedCBox
	 */
	public void linkWithRating(JCheckBox ratingCBox) {
		ratingCBoxList.add(ratingCBox);
	}

	/**
	 * @param cb
	 */
	public void dropRating(JCheckBox ratingCBox) {
		ratingCBoxList.remove(ratingCBox);
	}

	/**
	 * @param nextRating
	 * @return
	 */
	public Criteria applyTo(Rating rating) {
		Criteria applicationOf = new Criteria(ratingSystem, this, rating);
		this.add(applicationOf);
		applicationMap.put(rating, applicationOf);
		rating.linkTo(this, true);
		return applicationOf;
	}

	public boolean isAppliedTo(Rating rating) {
		return applicationMap.containsKey(rating);
	}

	/**
	 * @param target
	 */
	public void dropApplication(Criteria target) {
		Rating rating = target.getRating();
		applicationMap.remove(rating);
		rating.linkTo(this, false);
	}
}
