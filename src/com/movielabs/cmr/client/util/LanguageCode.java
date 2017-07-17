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
package com.movielabs.cmr.client.util;

import java.io.*;
import java.util.*;

/**
 * 
 * @author L. J. Levin, created Aug 30, 2013
 * 
 */
public class LanguageCode {
	private static final String LANG_FILE = "./resources/ISO2Lang.properties";
	private static HashMap<String, String> code2language = new HashMap<String, String>();
	private static String[] sorted4display;
	private static Properties lang2code;

	static {
		/*
		 * The file stores the properties as language=code' (e.g.,
		 * 'Italian=IT').
		 */
		int count = 0;
		lang2code = new Properties();
		File isoFile = new File(LANG_FILE);
		try {
			lang2code.load(new FileReader(isoFile));
			Collection countryNames = lang2code.keySet();
			sorted4display = new String[countryNames.size()];
			sorted4display = (String[]) countryNames.toArray(sorted4display);
			Arrays.sort(sorted4display, String.CASE_INSENSITIVE_ORDER);
			// now for reverse lookup..
			ArrayList<String> keys = new ArrayList<String>(
					lang2code.stringPropertyNames());
			for (int i = 0; i < keys.size(); i++) {
				String nextKey = keys.get(i);
				String nextValue = lang2code.getProperty(nextKey);
				/* reverse the key and value */
				code2language.put(nextValue, nextKey);
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.out.println("loaded " + count + " language codes");
		}
	}

	public static String[] getInDisplayOrder() {
		return sorted4display;
	}

	/**
	 * @param languageCode
	 * @return
	 */
	public static String getCodeForName(String language) {
		return (String) lang2code.get(language);
	}

	/**
	 * @param languageCode
	 * @return
	 */
	public static String getNameForCode(String languageCode) {
		String lc = languageCode.toUpperCase();
		return code2language.get(lc);
	}
}
