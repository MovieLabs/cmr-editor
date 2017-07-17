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

import javax.swing.JComponent;

import org.jdom2.Element;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.util.LanguageCode;
import com.callc.movielab.ratings.client.widgets.rspec.RatingDefPanel;

/**
 * 
 * @author L. J. Levin, created Sep 1, 2013
 * 
 */
public class RatingDescriptor extends SpecificationElement implements RSpecLeaf {

	private String languageCode;
	private String label = "t.b.d";
	private String definition = "";
	private String explanation = "";
	private Rating rating;
	private RatingsEditor mainApp;
	private RatingDefPanel uiWidget;

	public RatingDescriptor(Rating rating, Element xmlEl) {
		super();
		this.rating = rating;
		ratingSystem = rating.getRatingSystem();
		mainApp = RatingsEditor.getEditor();
		if (xmlEl != null) {
			initFromXml(xmlEl);
		} else {
			throw new UnsupportedOperationException();
		}
		uiWidget = new RatingDefPanel(this);
	}

	/**
	 * @param rating2
	 * @param lang
	 */
	public RatingDescriptor(Rating rating, String lang) {
		super();
		this.rating = rating;
		ratingSystem = rating.getRatingSystem();
		mainApp = RatingsEditor.getEditor();
		languageCode = lang;
		uiWidget = new RatingDefPanel(this);
	}

	/**
	 * @param xmlEl
	 */
	private void initFromXml(Element descriptorEl) {
		languageCode = descriptorEl.getAttributeValue("language", "");
		setLabel(descriptorEl.getChildText("Label", mdcrNSpace));

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
	}

	/**
	 * @return
	 */
	public Element asXml() {
		uiWidget.updateDao();
		Element root = new Element("Descriptor", mdcrNSpace);
		// language stored in attribute as code
		root.setAttribute("language", languageCode);
		Element xEl = new Element("Label", mdcrNSpace);
		xEl.setText(label);
		root.addContent(xEl);
		xEl = new Element("Definition", mdcrNSpace);
		xEl.setText(definition);
		root.addContent(xEl);
		if (!explanation.isEmpty()) {
			xEl = new Element("Explanation", mdcrNSpace); 
			try {
				Element innnerHtml = parseInnerHtml( explanation ); 
				xEl.addContent(innnerHtml);
			} catch ( Exception e) { 
				e.printStackTrace();
				System.out.print("\n\n"+explanation+"\n\n\n");
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
	public JComponent getUiWidget() {
		return uiWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		if (uiWidget != null) {
			uiWidget.syncWithDao();
		}
	}

	/**
	 * @return the ID
	 */
	public String toString() {
		String language = LanguageCode.getNameForCode(getLanguageCode());
		return language;
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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

}
