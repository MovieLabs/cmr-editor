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

import org.jdom2.Element;

import com.callc.movielab.ratings.client.util.CountryCode;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class Region extends SpecificationElement {
	public static final String nullIndicator = "-t.b.d.-";
	private String isoCode = nullIndicator;
	private String subRegionName = "";
	private String subRegionCode = "";

	/**
	 * @param regionEl
	 */
	public void initFromXml(Element regionEl) {
		subRegionCode = regionEl.getChildText("countryRegion", mdNSpace);
		if (subRegionCode != null && !subRegionCode.isEmpty()) {
			subRegionName = regionEl.getChildText("SubRegion", mdcrNSpace);
			if (subRegionName == null) {
				subRegionName = "";
			}
			isoCode = subRegionCode.substring(0, 2);
		} else {
			isoCode = regionEl.getChildText("country", mdNSpace);
		}
	}

	public Element asXml() {
		return asXml(false);
	}

	/**
	 * @return
	 */
	public Element asXml(boolean asClone) {
		boolean asSubRegion = false;
		if (isoCode == null || isoCode.isEmpty() || isoCode.equalsIgnoreCase(nullIndicator)) {
			return null;
		}
		// Is there a sub-region?
		if (subRegionCode == null || subRegionCode.isEmpty() || subRegionCode.equalsIgnoreCase(nullIndicator)) {
			asSubRegion = false;
		} else {
			asSubRegion = true;
		}
		Element xRegion = new Element("Region", mdcrNSpace);
		if (asSubRegion) {
			// State, province, or canton (i.e., a sub-region)
			Element xSubRegCode = new Element("countryRegion", mdNSpace);
			xSubRegCode.setText(subRegionCode);
			xRegion.addContent(xSubRegCode);
			Element xSubRegion = new Element("SubRegion", mdcrNSpace);
			xSubRegion.setText(subRegionName);
			xRegion.addContent(xSubRegion);

		} else {
			// its a country
			Element xCountry = new Element("country", mdNSpace);
			xCountry.setText(isoCode);
			xRegion.addContent(xCountry);
		}
		Element xRegionName = new Element("RegionName", mdcrNSpace);
		xRegionName.setText(CountryCode.getNameForCode(isoCode));
		xRegion.addContent(xRegionName);
		return xRegion;
	}

	/**
	 * @return the isoCode
	 */
	public String getIsoCode() {
		return isoCode;
	}

	/**
	 * @return the subRegion
	 */
	public String getSubRegionName() {
		return subRegionName;
	}

	/**
	 * @return the subRegionCode
	 */
	public String getSubRegionCode() {
		return subRegionCode;
	}

	/**
	 * @param subRegionName
	 *            the subRegion to set
	 */
	public void setSubRegion(String subRegionName, String subRegionCode) {
		this.subRegionName = subRegionName;
		this.subRegionCode = subRegionCode;
	}

	/**
	 * @param isoCode
	 *            the isoCode to set
	 */
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String toString() {
		if (subRegionCode != null && !subRegionCode.isEmpty()) {
			return subRegionName;
		} else {
			return (CountryCode.getNameForCode(isoCode));
		}
	}
}
