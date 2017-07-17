/**
 * 
 * Copyright Motion Picture Laboratories, Inc. 2014
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

import java.util.EnumSet;
import java.util.List;

import org.jdom2.Element;

/**
 * 
 * @author L. J. Levin, created Mar 19, 2014
 * 
 */
public class Usage extends SpecificationElement {
	private EnumSet<MEDIA> targetMedia = EnumSet.noneOf(MEDIA.class);
	private EnumSet<ENVIRONMENT> targetEnviron = EnumSet
			.noneOf(ENVIRONMENT.class);
	private boolean defaultAllSelected;

	/**
	 * @param defaultAll
	 */
	public Usage(boolean defaultAll) {
		defaultAllSelected = defaultAll;
	}

	/**
	 * @param usageEl
	 */
	public void initFromXml(Element xmlEl) {
		List<Element> mediaEl = xmlEl.getChildren("Media", mdcrNSpace);
		if (mediaEl != null && !mediaEl.isEmpty()) {
			for (int i = 0; i < mediaEl.size(); i++) {
				String next = mediaEl.get(i).getText();
				targetMedia.add(Enum.valueOf(MEDIA.class, next));
			}
		} else {
			if (defaultAllSelected) {
				targetMedia.addAll(mediaList);
			}
		}
		List<Element> envEl = xmlEl.getChildren("Environment", mdcrNSpace);
		if (envEl != null && !envEl.isEmpty()) {
			for (int i = 0; i < envEl.size(); i++) {
				String next = envEl.get(i).getText();
				targetEnviron.add(Enum.valueOf(ENVIRONMENT.class, next));
			}
		} else {
			if (defaultAllSelected) {
				targetEnviron.addAll(envList);
			}
		}
	}

	/** 
	 */
	public Element asXml() {
		Element usageEl = new Element("Usage", mdcrNSpace);
		// Media:
		for (int eCnt = 0; eCnt < mediaList.size(); eCnt++) {
			MEDIA type = mediaList.get(eCnt);
			if (isMediaEnabled(type)) {
				String name = type.toString();
				Element mediaEl = new Element("Media", mdcrNSpace);
				mediaEl.setText(name);
				usageEl.addContent(mediaEl);
			}
		}
		// Env:
		for (int eCnt = 0; eCnt < envList.size(); eCnt++) {
			ENVIRONMENT type = envList.get(eCnt);
			if (isEnvironEnabled(type)) {
				String name = type.toString();
				Element envEl = new Element("Environment", mdcrNSpace);
				envEl.setText(name);
				usageEl.addContent(envEl);
			}
		}
		return usageEl;
	}

	/**
	 * Set or clear the selection of the specified <tt>type</tt>.
	 * 
	 * @param type
	 *            of ENVIRONMENT
	 * @param set
	 *            flag indicates if selection is to be set of cleared.
	 * @return <tt>true</tt> if there has been a change.
	 */
	public boolean setEnvironEnabled(ENVIRONMENT type, boolean set) {
		if (set) {
			return targetEnviron.add(type);
		} else {
			return targetEnviron.remove(type);
		}
	}

	public boolean isEnvironEnabled(ENVIRONMENT type) {
		return targetEnviron.contains(type);
	}

	/**
	 * Set or clear the selection of the specified <tt>type</tt>.
	 * 
	 * @param type
	 *            of MEDIA
	 * @param set
	 *            flag indicates if selection is to be set of cleared.
	 * @return <tt>true</tt> if there has been a change.
	 */
	public boolean setMediaEnabled(MEDIA type, boolean set) {
		if (set) {
			return targetMedia.add(type);
		} else {
			return targetMedia.remove(type);
		}
	}

	public boolean isMediaEnabled(MEDIA type) {
		return targetMedia.contains(type);
	}
}
