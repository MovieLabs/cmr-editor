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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author L. J. Levin
 * 
 */
public class TimeStamp {

	public static Date asDate() {
		GregorianCalendar cal = new GregorianCalendar(
				TimeZone.getTimeZone("GMT"));
		return (cal.getTime());
	}

	/**
	 * Returns the specifed date and time as String. The format is compatible
	 * with SQL TIMESTAMP and DATETIME formats (i.e.,
	 * <tt>yyyy-MM-dd kk:mm:ss</tt>).
	 * 
	 * @return
	 */
	public static String asString(Date dt) {
		// Remember: SimpleDateFormat is NOT thread-safe so we need per-thread
		// instance
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return (sdf.format(dt));
	}

	/**
	 * Returns the current GMT date and time as String. The format is compatible
	 * with SQL TIMESTAMP and DATETIME formats (i.e.,
	 * <tt>yyyy-MM-dd kk:mm:ss</tt>).
	 * 
	 * @return
	 */
	public static String asString() {
		GregorianCalendar cal = new GregorianCalendar(
				TimeZone.getTimeZone("GMT"));
		// Remember: SimpleDateFormat is NOT thread-safe so we need per-thread
		// instance
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return (sdf.format(cal.getTime()));
	}

	/**
	 * @return returns current date formated as specified for
	 *         xs:date 
	 */
	public static String asXsDate() { 
		return asXsDate(new Date());
	}

	/**
	 * @return returns date  formated as specified for xs:date 
	 */
	public static String asXsDate(Date dt) {
		SimpleDateFormat ISO8601UTC = new SimpleDateFormat(
				"yyyy-MM-dd");
		ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		String now = ISO8601UTC.format(dt);
		return now;
	}

	/**
	 * @return returns current date and time formated as specified for
	 *         xs:dateTime
	 */
	public static String asXsDateTime() { 
		return asXsDateTime(new Date());
	}

	/**
	 * Converts a String in xs:date  format to a Date instance. Any parsing
	 * error results in a <tt>null</tt> return value.
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Date fromXsDate (String timestamp) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date tStampDate = null;
		try {
			tStampDate = df.parse(timestamp);
		} catch (ParseException e) {
		}
		return tStampDate;
	}
	/**
	 * @return returns date and time formated as specified for xs:dateTime
	 */
	public static String asXsDateTime(Date dt) {
		SimpleDateFormat ISO8601UTC = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		String now = ISO8601UTC.format(dt);
		return now;
	}
	/**
	 * Converts a String in xs:dateTime format to a Date instance. Any parsing
	 * error results in a <tt>null</tt> return value.
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Date fromXsDateTime(String timestamp) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date tStampDate = null;
		try {
			tStampDate = df.parse(timestamp);
		} catch (ParseException e) {
		}
		return tStampDate;
	}
}
