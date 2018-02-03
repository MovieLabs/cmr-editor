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

import org.jdom2.Element;

import com.movielabs.cmr.client.rspec.SpecificationElement.ENVIRONMENT;
import com.movielabs.cmr.client.widgets.rspec.RatingsOrgPanel;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class Organization extends SpecificationElement implements RSpecLeaf {

	public static enum ORGTYPE {
		Gov, Trade, Consumer, Religious, other, not_specified
	};

	public static List<ORGTYPE> orgTypeList = new ArrayList<ORGTYPE>(
			EnumSet.allOf(ORGTYPE.class));
	


	private String organizationID = ""; 
	private String displayName = "-t.b.d.-";
	private String sortName = "";
	private String contactInfo = "";
	private String url = "";
	private ORGTYPE orgType = ORGTYPE.not_specified;
	private RatingsOrgPanel uiWidget;

	/**
	 * @param child
	 */
	public Organization(Element xOrg) {
		initFromXml(xOrg);
		uiWidget = new RatingsOrgPanel(this);
	}

	/**
	 * 
	 */
	public Organization() {
		// TODO Auto-generated constructor stub
		uiWidget = new RatingsOrgPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.callc.movielab.ratings.client.rspec.RSpecLeaf#resyncUi()
	 */
	@Override
	public void resyncUi() {
		// TODO Auto-generated method stub
		//
	}

	/**
	 * @param xROrg
	 */
	private void initFromXml(Element xmlROrg) {
		// displayName is required
		Element xDName = xmlROrg.getChild("DisplayName", mdNSpace);
		displayName = xDName.getText();
		// everything else is optional
		organizationID = xmlROrg.getAttributeValue("organizationID", "");
//		idType = xmlROrg.getAttributeValue("idType", "");
		Element xSName = xmlROrg.getChild("SortName", mdNSpace);
		if (xSName != null) {
			sortName = xSName.getText();
		}
		Element xCInfo = xmlROrg.getChild("ContactString",
				SpecificationElement.mdcrNSpace);
		if (xCInfo != null) {
			contactInfo = xCInfo.getText();
		}
		Element xURL = xmlROrg.getChild("URL", mdcrNSpace);
		if (xURL != null) {
			url = xURL.getText();
		}
		xURL = xmlROrg.getChild("OrgType", mdcrNSpace);
		if (xURL != null) {
			String oTypeName = xURL.getText();
			orgType = Enum.valueOf(ORGTYPE.class, oTypeName);
		}
	}

	public Element asXml() {
		uiWidget.updateDao();
		Element rOrgRoot = new Element("RatingsOrg", mdcrNSpace);
		Element xDName = new Element("DisplayName", mdNSpace);
		xDName.setText(displayName);
		rOrgRoot.addContent(xDName);
		// optional attributes..
		if (!organizationID.isEmpty()) {
			rOrgRoot.setAttribute("organizationID",  organizationID);
		}
//		if (!idType.isEmpty()) {
//			rOrgRoot.setAttribute("idType", idType);
//		}
		// optional child Elements..
		Element xEl;
		if (!sortName.isEmpty()) {
			xEl = new Element("SortName", mdNSpace);
			xEl.setText(sortName);
			rOrgRoot.addContent(xEl);
		}
		if (!contactInfo.isEmpty()) {
			xEl = new Element("ContactString", mdcrNSpace);
			xEl.setText(contactInfo);
			rOrgRoot.addContent(xEl);
		}
		if (!url.isEmpty()) {
			xEl = new Element("URL", mdcrNSpace);
			xEl.setText(url);
			rOrgRoot.addContent(xEl);
		}
		xEl = new Element("OrgType", mdcrNSpace);
		xEl.setText(orgType.toString());
		rOrgRoot.addContent(xEl);
		return rOrgRoot;
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
	 * @return the organizationID
	 */
	public String getOrganizationID() {
		return organizationID;
	}

	/**
	 * @param organizationID
	 *            the organizationID to set
	 */
	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	/**
	 * @return the orgType
	 */
	public ORGTYPE getOrgType() {
		return orgType;
	}

	/**
	 * @param orgType the orgType to set
	 */
	public void setOrgType(ORGTYPE orgType) {
		this.orgType = orgType;
	}

//	/**
//	 * @return the idType
//	 */
//	public String getIdType() {
//		return idType;
//	}
//
//	/**
//	 * @param idType
//	 *            the idType to set
//	 */
//	public void setIdType(String idType) {
//		this.idType = idType;
//	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the sortName
	 */
	public String getSortName() {
		return sortName;
	}

	/**
	 * @param sortName
	 *            the sortName to set
	 */
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/**
	 * @return the contactInfo
	 */
	public String getContactInfo() {
		return contactInfo;
	}

	/**
	 * @param contactInfo
	 *            the contactInfo to set
	 */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
