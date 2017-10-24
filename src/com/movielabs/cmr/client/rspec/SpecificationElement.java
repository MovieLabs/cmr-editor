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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 * 
 * @author L. J. Levin, created Aug 30, 2013
 * 
 */
public abstract class SpecificationElement extends DefaultMutableTreeNode {
	public static final String UriSep = "/";

	public static final String MD_VER = "2.1";

	/**
	 * Ratings Specification Schema version being used
	 */
	public static final String MDCR_VER = "1.1";

	// TODO: These should load from XSD.....

	public static enum MEDIA {
		Film, Trailer, DVD, Music, Game, TV, Ad, other
	};

	public static enum ENVIRONMENT {
		Home, Theater, Broadcast, Retail, App, other
	};

	protected static List<MEDIA> mediaList = new ArrayList<MEDIA>(EnumSet.allOf(MEDIA.class));
	protected static List<ENVIRONMENT> envList = new ArrayList<ENVIRONMENT>(EnumSet.allOf(ENVIRONMENT.class));

	public static Namespace xsiNSpace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	public static Namespace mdNSpace = Namespace.getNamespace("md",
			"http://www.movielabs.com/schema/md/v" + MD_VER + "/md");
	public static final String mdcrFull = "http://www.movielabs.com/schema/mdcr/v" + MDCR_VER;
	public static Namespace mdcrNSpace = Namespace.getNamespace("mdcr", mdcrFull);
	protected static String schemaFile = "mdcr-v" + MDCR_VER + ".xsd";
	public static String mdcrUrl = mdcrFull + "/" + schemaFile;
	public static String baseUrl = "http://www.movielabs.com/md/ratings/";
	public static final String schemaLoc = mdcrFull + " " + mdcrUrl;

	protected RatingSystem ratingSystem;

	protected int extractInt(Element xml) {
		if (xml != null) {
			String value = xml.getText();
			if (!value.isEmpty()) {
				try {
					return Integer.parseInt(xml.getText());
				} catch (NumberFormatException e) {
				}
			}
		}
		return -1;

	}

	protected boolean extractBool(Element xml) {
		if (xml != null) {
			String value = xml.getText();
			if (!value.isEmpty()) {
				return value.equalsIgnoreCase("true");
			}
		}
		return false;
	}

	protected String makeSafeForURI(String text) {
		String regex = "[^a-zA-Z0-9 \\-_\\.\\(\\)\\+]";
		String temp = text.replaceAll(regex, "_");
		return temp;
	}

	protected String loadInnerHtml(Element containerEl) {
		Element divEl = containerEl.getChild("div");
		if (divEl == null) {
			// its the old-style simple text w/o html
			return containerEl.getText();
		}
		XMLOutputter xmOut = new XMLOutputter();
		Document doc = new Document(divEl.detach());
		String raw = xmOut.outputString(doc);
		if (raw == null || raw.isEmpty()) {
			return "";
		}
		if (raw.contains("<div class=\"userHtml\">")) {
			String[] temp1 = raw.split("<div class=\"userHtml\">");
			String[] temp2 = temp1[1].split("</div");
			return (temp2[0]);
		} else {
			return raw;
		}

	}

	protected Element parseInnerHtml(String text) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader("<div class='userHtml'>" + text + "</div>");
		Document doc = null;
		Element root = null;
		try {
			doc = builder.build(in);
		} catch (JDOMParseException e) {
			String details = e.getMessage();
			if (ratingSystem != null) {
				ratingSystem.reportException(this, details);
			} else {
				System.out.println(details);
			}
		}
		root = doc.getRootElement();
		root.detach();
		return root;
	}
}
