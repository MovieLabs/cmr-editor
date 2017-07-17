/**
 * 
 * Copyright Critical Architectures, LLC 2014
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

import java.awt.Component;
import org.jdom2.Element;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.widgets.rspec.UsagePanel;

/**
 * Combo of a geopolitical Region with the Usage data.
 * 
 * @author L. J. Levin, created Mar 19, 2014
 * 
 */
public class AdoptiveRegion extends Region implements RSpecLeaf {
	private Usage usage;
	private RatingsEditor mainApp;
	private UsagePanel uiWidget;
	private boolean defaultAllSelected;

	public AdoptiveRegion() {
		super();
		mainApp = RatingsEditor.getEditor();
		uiWidget = new UsagePanel(this);

	}

	public AdoptiveRegion(Element xmlEl) {
		this(xmlEl, true); 
	}

	/**
	 * @param xml
	 * @param b
	 */
	public AdoptiveRegion(Element xmlEl, boolean defaultAll) {
		super();
		mainApp = RatingsEditor.getEditor();
		defaultAllSelected = defaultAll;
		usage = new Usage(defaultAllSelected);
		if (xmlEl != null) {
			initFromXml(xmlEl);
		}
		uiWidget = new UsagePanel(this);
	}

	/**
	 * @param element
	 */
	public void initFromXml(Element xmlEl) {
		super.initFromXml(xmlEl);
		Element usageEl = xmlEl.getChild("Usage", mdcrNSpace);
		usage.initFromXml(usageEl);
		uiWidget = new UsagePanel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#asXml()
	 */
	@Override
	public Element asXml() {
		return asXml(false);
	}

	/**
	 * If <tt>asClone==true</tt> any ID fields are altered (e.g.,
	 * <tt>xEl.setText("dup-" + id);</tt>. Currently, however, there is no
	 * unique identifier for the AdoptiveRegion so the <tt>asClone</tt> flag has
	 * no effect.
	 * 
	 * @param asClone
	 * @return
	 */
	public Element asXml(boolean asClone) {
		getUiWidget(); // make sure it exists
		uiWidget.updateDao();
		Element arEl = super.asXml(asClone);
		arEl.setName("AdoptiveRegion");
		arEl.addContent(usage.asXml());
		return (arEl);

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
	 * @param isoCode
	 *            the isoCode to set
	 */
	public void setIsoCode(String isoCode) {
		super.setIsoCode(isoCode);
		resyncUi();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		if (mainApp != null) {
			mainApp.hasChanged(this);
		}
	}

	/**
	 * @return the usage
	 */
	public Usage getUsage() {
		return usage;
	}

}
