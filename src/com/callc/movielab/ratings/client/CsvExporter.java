/**
 * Created Dec 18, 2014
 * Copyright Critical Architectures LLC, 2014
 * All Rights Reserved
 * http://www.criticalArchitectures.com/
 */
package com.callc.movielab.ratings.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.jdom2.*;

import com.callc.movielab.ratings.client.rspec.SpecificationElement;

/**
 * @author larry
 *
 */
public class CsvExporter {
	private static final String SEP = ", ";
	static Namespace mdcr = SpecificationElement.mdcrNSpace;
	static Namespace md = SpecificationElement.mdNSpace;
	private static FileOutputStream fos;

	public static boolean export(Element root, File destFile)
			throws FileNotFoundException {
		fos = new FileOutputStream(destFile);
		List<Element> rSysList = root.getChildren("RatingSystem", mdcr);
		for (int i = 0; i < rSysList.size(); i++) {
			Element nextSysEl = rSysList.get(i);
			addSystem(nextSysEl);
		}
		try {
			fos.close();
			fos = null;

		} catch (IOException e) {
			e.printStackTrace();

		}
		return true;
	}

	/**
	 * @param nextSysEl
	 */
	private static void addSystem(Element rSysEl) {
		Element rsIdEl = rSysEl.getChild("RatingSystemID", mdcr);
		String sysName = rsIdEl.getChildTextNormalize("System", mdcr);
		if (sysName.equalsIgnoreCase("PEGI")) {
			return;
		}
		List<String> regionList = new ArrayList<String>();
		List<Element> regionElList = rSysEl.getChildren("AdoptiveRegion", mdcr);
		for (int i = 0; i < regionElList.size(); i++) {
			Element nextARegEl = regionElList.get(i);
			String regionName = nextARegEl.getChildTextNormalize("RegionName",
					mdcr);
			String regionCode = nextARegEl.getChildTextNormalize("country", md);
			String entry = regionName + SEP + regionCode + SEP + sysName + SEP;
			regionList.add(entry);
		}
		List<Element> ratingElList = rSysEl.getChildren("Rating", mdcr);
		for (int i = 0; i < ratingElList.size(); i++) {
			Element nextRatEl = ratingElList.get(i);
			String id = nextRatEl.getAttributeValue("ratingID");
			Element nextDescEl = nextRatEl.getChild("Descriptor", mdcr);
			String label = nextDescEl.getChildTextNormalize("Label", mdcr);
			// String def = nextDescEl
			// .getChildTextNormalize("Definition", mdcr);
			for (int j = 0; j < regionList.size(); j++) {
				String entry = regionList.get(j) + id + SEP + label + "\n";
				// System.out.println(entry);
				try {
					fos.write(entry.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
