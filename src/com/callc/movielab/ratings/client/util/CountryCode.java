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
package com.callc.movielab.ratings.client.util;

import java.io.*;
import java.util.*;

/**
 * 
 * @author L. J. Levin, created Aug 30, 2013
 * 
 */
public class CountryCode {
	private static final String ISO3166_FILE = "./resources/ISO3166.properties";
	private static HashMap<String, String> country2code = new HashMap<String, String>();
	private static String[] sorted4display;
	private static Properties code2country;

	static {
		/*
		 * The file stores the properties as code=country (e.g., 'CA=CANADA').
		 */
		code2country = new Properties();
		File isoFile = new File(ISO3166_FILE);
		try {
			code2country.load(new FileReader(isoFile));
			// sort by country name for UI ComboBox
			Collection countries = code2country.values();
			sorted4display = new String[countries.size()];
			sorted4display = (String[]) countries.toArray(sorted4display);
			Arrays.sort(sorted4display, String.CASE_INSENSITIVE_ORDER);
			// now for reverse lookup..
			ArrayList<String> keys = new ArrayList<String>(
					code2country.stringPropertyNames());
			for (int i = 0; i < keys.size(); i++) {
				String nextKey = keys.get(i);
				String nextValue = code2country.getProperty(nextKey);
				/* reverse the key and value */
				country2code.put(nextValue, nextKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] getInDisplayOrder() {
		return sorted4display;
	}

	/**
	 * @param country
	 * @return
	 */
	public static String getCodeForName(String country) {
		return country2code.get(country);
	}

	/**
	 * @param countryCode
	 * @return
	 */
	public static String getNameForCode(String countryCode) {
		String lc = countryCode.toUpperCase();
		return (String) code2country.get(lc);
	}
}
