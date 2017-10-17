/**
 * Copyright (c) 2018 Motion Picture Laboratories, Inc.
 *
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.movielabs.cmr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * Special 'one time only' tool to modify all XML to use only locally cached
 * icon files.
 *
 */
public class IconPatch {

	public static void main(String[] args) {
		String baseDir = "../cmr-ratings/";
		File startDir = new File(baseDir + "xml");
		String endDir = baseDir + "temp";
		try {
			getFiles(startDir, endDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void getFiles(File startDir, String endDir) throws IOException {
		File[] allXmlFiles = startDir.listFiles();
		for (File xmlFile : allXmlFiles) {
			if (!xmlFile.isDirectory()) {
				if (xmlFile.getName().toLowerCase().endsWith(".xml")) {
					Map<String, String> lst = identifyImageFiles(xmlFile);
					loadFiles(endDir, lst);
				}
			}
		}
	}

	/**
	 * Identify all remote URLs pointing to logo images and return a
	 * <tt>Map</tt> with and the assigned file name and the URL.
	 * 
	 * @param XML
	 *            inputFile
	 * @return
	 */
	private static Map<String, String> identifyImageFiles(File inputFile) {
		Map<String, String> urlList = new HashMap<String, String>();
		// Step 1: Read XML file
		Element rootEl;
		try {
			rootEl = BatchPatchTool.getAsXml(inputFile);
		} catch (Exception e) {
			e.printStackTrace();
			return urlList;
		}

		// Step2: we need the UID
		Namespace mdcrNSpace = Namespace.getNamespace("mdcr", "http://www.movielabs.com/schema/mdcr/v1.1");
		Namespace mdNSpace = Namespace.getNamespace("md", "http://www.movielabs.com/schema/md/v2.1/md");
		Element rSysIdEl = rootEl.getChild("RatingSystemID", mdcrNSpace);
		String sysName = rSysIdEl.getChildText("System", mdcrNSpace);
		String isoCode = rSysIdEl.getChild("Region", mdcrNSpace).getChildText("country", mdNSpace);
		String uid = isoCode + "_" + sysName;
		// System.out.println("Processing = "+uid);

		// Step 3: Find all 'LinkToLogo' Elements
		XPathFactory xpfac = XPathFactory.instance();
		String xpath = "./mdcr:Rating/mdcr:LinkToLogo";
		XPathExpression<Element> xpExpression = xpfac.compile(xpath, Filters.element(), null, mdcrNSpace);
		List<Element> l2lElList = xpExpression.evaluate(rootEl);

		/*
		 * Step 4: Ignore anything already in local cache but the remote stuff
		 * we add to the map
		 * 
		 */
		boolean changed = false;
		for (Element nextEl : l2lElList) {
			String url = nextEl.getText();
			// System.out.println(" checking = "+url);
			if (url.startsWith("http")) {
				int pos = url.lastIndexOf("/");
				String iconFileName = url.substring(pos + 1);
				// System.out.println(" found icon file = " + iconFileName);
				String name = uid + "_" + iconFileName;
				urlList.put(name, url);
				changed = true;
				String newUrl = "./imageCache/" + name;
				nextEl.setText(newUrl);
			}
		}
		/* Last: if we changed the XML we need to write the file back to disk */

		if (changed) {
			// write file to designated location
			String name = inputFile.getName();
			Format myFormat = Format.getPrettyFormat();
			XMLOutputter outputter = new XMLOutputter(myFormat);
			FileOutputStream fo = null;
			File movedFile = new File("../cmr-ratings/temp", name);
			try {
				fo = new FileOutputStream(movedFile);
				outputter.output(rootEl, fo);
				fo.flush();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return urlList;
	}

	private static void loadFiles(String dir, Map<String, String> urls) {
		Set<String> keySet = urls.keySet();
		String[] keyList = keySet.toArray(new String[keySet.size()]);
		for (int i = 0; i < keyList.length; i++) {
			String name = keyList[i];
			String link = urls.get(name);
			try {
				URL website = new URL(link);
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dir + "/" + name);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
			} catch (Exception e) {
				System.out.println("Exception:  URL=" + link);
			}
		}

	}

}
