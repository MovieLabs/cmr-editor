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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import org.jdom2.Element;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.widgets.rspec.CriteriaPanel;
import com.callc.movielab.ratings.client.widgets.rspec.ReasonPanel;

/**
 * 
 * @author L. J. Levin, created Nov 14, 2013
 * 
 */
public class Criteria extends SpecificationElement implements RSpecLeaf {

	private String definition = "";
	private String explanation = "";
	private RatingsEditor mainApp;
	private CriteriaPanel uiWidget; 
	private String uri;
	private Rating rating;
	private ReasonDescriptor reason;

	public Criteria(RatingSystem ratingSystem, ReasonDescriptor reason,
			Element xmlEl) {
		super();
		this.ratingSystem = ratingSystem;
		this.reason = reason;
		mainApp = RatingsEditor.getEditor();
		if (xmlEl != null) {
			initFromXml(xmlEl);
		}
	}

	/**
	 * @param rating2
	 * @param lang
	 */
	public Criteria(RatingSystem ratingSystem, ReasonDescriptor reason,
			Rating rating) {
		super();
		this.ratingSystem = ratingSystem;
		this.reason = reason;
		this.rating = rating;
		mainApp = RatingsEditor.getEditor();
	}

	/**
	 * @param xmlEl
	 */
	private void initFromXml(Element criteriaEl) {
		String idCode = criteriaEl.getAttributeValue("ratingID", "");
		rating = ratingSystem.getRating(idCode);
		if (rating == null) {
			// throw exception..
			throw new IllegalArgumentException(
					"Unable to locate Rating with ID='" + idCode + "'");
		}
		Element childEl = criteriaEl.getChild("Definition", mdcrNSpace);
		if (childEl != null) {
			definition = childEl.getText();
		}
		Element explanationEl = criteriaEl.getChild("Explanation", mdcrNSpace);
		if (explanationEl != null) {
			explanation = loadInnerHtml(explanationEl);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#asXml()
	 */
	public Element asXml() {
		getUiWidget().updateDao();
		Element root = new Element("Criteria", mdcrNSpace);
		root.setAttribute("ratingID", rating.getRatingID());
		Element xEl;
		if (!definition.isEmpty()) {
			xEl = new Element("Definition", mdcrNSpace);
			xEl.setText(definition);
			root.addContent(xEl);
		}
		if (!explanation.isEmpty()) {
			xEl = new Element("Explanation", mdcrNSpace); 
			try {
				Element innnerHtml = parseInnerHtml( explanation ); 
				xEl.addContent(innnerHtml);
			} catch ( Exception e) { 
				e.printStackTrace();
				xEl.setText(explanation);
			} 
			root.addContent(xEl);
		}
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#getUiWidget()
	 */
	@Override
	public CriteriaPanel getUiWidget() {
		if (uiWidget == null) {
			uiWidget = new CriteriaPanel(this);
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
	}

	/**
	 * @return the ID
	 */
	public String toString() {
		return reason.toString() + "->" + rating.toString();
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
		uri = ratingSystem.getUri() + UriSep + "Criteria" + UriSep
				+ makeSafeForURI(this.toString());
		getUiWidget();
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
	 * @return
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @return
	 */
	public ReasonDescriptor getReason() {
		return reason;
	}
}
