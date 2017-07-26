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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconPatch {

	public static void main(String[] args) {
		String baseDir = "/home/anna/Desktop/";
		// File startDir = new File("/home/anna/Desktop/start");
		// String endDir = "/home/anna/Desktop/end";
		File startDir = new File(baseDir + "xml");
		String endDir = baseDir + "imageCache2";
		try {
			getFiles(startDir, endDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Identify all remote URLs pointing to logo images and return a <tt>Map</tt> with and the assigned file name and the URL.
	 * 
	 * @param XML file
	 * @return
	 */
	private static Map<String, String> identifyImageFiles(File file) {
		Map<String, String> urlList = new HashMap<String, String>();
		urlList.put("ImageONE.jpg",
				"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTFfjCGPH1WcZz9dwONGcx7fwWD_rxim6fbA-ILfArh_usDr5wGHg");
		urlList.put("ImageTWO.jpg",
				"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQhynTmHDi8cwk2_d4_HaRBh-60tGIXppcny7xSEfTRzr1sWFT");
		// TODO Auto-generated method stub

		return urlList;
	}

	private static void loadFiles(String dir, List<String> urls) {
		for (int i = 0; i < urls.size(); i++) {
			try {
				URL website = new URL(urls.get(i));
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dir + "/file" + i + ".jpeg");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
