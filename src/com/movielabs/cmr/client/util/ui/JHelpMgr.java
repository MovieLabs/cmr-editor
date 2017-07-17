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
package com.movielabs.cmr.client.util.ui;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.WindowPresentation;
import javax.help.CSH.DisplayHelpFromSource;

public class JHelpMgr {
	private static HashMap<String, HelpBroker> jHelpSets = new HashMap<String, HelpBroker>();
	private static Class<?> rsrcBaseClass;
	private static String helpRsrcPath;

	public static void showJavaHelp(String topicArea, ActionEvent event) {
		/*
		 * first see if we have already created a display window for the
		 * requested help-set (i.e., topic)
		 */
		HelpBroker hb = (HelpBroker) jHelpSets.get(topicArea);
		if (hb != null) {
			if (hb instanceof DefaultHelpBroker) {
				DefaultHelpBroker dhb = (DefaultHelpBroker) hb;
				WindowPresentation wb = dhb.getWindowPresentation();
				wb.setDisplayed(true);
				return;
			}
		}
		HelpSet hs;
		String targetHelpSet = getTopicPath(topicArea);
		URL target = locateJHelp(targetHelpSet);
		if (target != null) {
			try {
				hs = new HelpSet(null, target);
			} catch (HelpSetException e) {
				System.out.println("JHelpMgr: HelpSetException for path "
						+ targetHelpSet);
				return;
			}
		} else {
			System.out.println("JHelpMgr: HelpSet " + targetHelpSet + " not found");
			return;
		}
		hb = hs.createHelpBroker();
		DisplayHelpFromSource displayHelper = new CSH.DisplayHelpFromSource(hb);
		displayHelper.actionPerformed(event);
		/*
		 * 'register' the help set. That way if user requests it again we can
		 * open-up (i.e., display) the original instance which they probably
		 * have minimized and/or forgotten about.
		 */
		jHelpSets.put(topicArea, hb);
	}

	private static URL locateJHelp(String topic) {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		if (loader != null) {
			/*
			 * NOTE: when loading resources from jars it is very VERY important
			 * NOT to have the leading slash! I learned this slowly and
			 * painfully :(
			 */
			// test first to see if the slash is actually there
			String testLoc = topic.replaceFirst("^/", "");
			System.out.println("JHelpMgr: Looking for " + topic);
			URL target = loader.getResource(testLoc);
			if (target == null) {
				System.out.println("JHelpMgr: resource " + topic
						+ " not found, still loooking");
			}
			return target;
		}
		return null;
	}

	/**
	 * Initializes the resource-lookup fields for JHelp Sets. The
	 * <tt>baseClass</tt> will be used as a resource loader. The
	 * <tt>rsrcPathExt</tt> is a path <i>relative to</i> the base class.
	 * <p>
	 * Example:
	 * <ul>
	 * <li>The base class is <tt>com.callc.someApp.FooBar.java</tt></li>
	 * <li>The helpset directory is <tt>com/callc/someApp/rsrc/help/en/</tt></li>
	 * <li>Then the <tt>rsrcPathExt</tt> is <tt>rsrc/help/en</tt> (note the lack
	 * of '/' at the begining and end)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param baseClass
	 * @param rsrcPathExt
	 */
	public static void initializeHelpTools(Class<?> baseClass,
			String rsrcPathExt) {
		rsrcBaseClass = baseClass;
		helpRsrcPath = rsrcBaseClass.getPackage().getName()
				.replaceAll("\\.", "/");
		helpRsrcPath = "/" + helpRsrcPath + "/" + rsrcPathExt + "/";
	}

	/**
	 * Determines the path to the helpdoc directory for the specified topic
	 * area.
	 * 
	 * @param topicArea
	 * @return
	 */
	private static String getTopicPath(String topicArea) {
		// 1st strip off the prefix (if present)
		String rsrcPath = helpRsrcPath + topicArea + "/" +   "helpset.hs"; 
		return rsrcPath;
	}
}
