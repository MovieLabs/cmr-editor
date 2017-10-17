/**
 * Copyright (c) 2013 Motion Picture Laboratories, Inc.
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

import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import com.movielabs.cmr.client.rspec.SpecificationElement;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Handle patching and updating of existing XML to conform with schema changes
 * 
 * @author L. J. Levin, created Oct 17, 2013
 * 
 */
public class BatchPatchTool {
	private static String saxTFac = "net.sf.saxon.TransformerFactoryImpl";
	public static final String fileSuffix = "_Ratings";
	// private File xsltDirF;
	private String srcXmlDirLocation;
	private String destXmlDirLocation;

	public static void main(String[] args) {
		BatchPatchTool tool = new BatchPatchTool("./resources/xml", null, null);
		tool.process();
	}

	/**
	 * @param srcXmlDir
	 * @param destXmlDir
	 *            (optional)
	 * @param rsrcDir
	 */
	public BatchPatchTool(String srcXmlDir, String destXmlDir, String rsrcDir) {
		String xsltDir = rsrcDir + File.separator + "transforms";
		// xsltDirF = new File(xsltDir);
		// if (!xsltDirF.exists()) {
		// throw new RuntimeException(
		// "Transform directory not found. Specified as '" + xsltDir
		// + "'");
		// }
		srcXmlDirLocation = srcXmlDir;
		File srcXmlDirFile = new File(srcXmlDirLocation);
		if (!srcXmlDirFile.exists()) {
			srcXmlDirFile.mkdirs();
		}
		if (destXmlDir == null) {
			destXmlDir = srcXmlDir + "/patched";
		}
		destXmlDirLocation = destXmlDir;
		File destXmlDirFile = new File(destXmlDirLocation);
		if (!destXmlDirFile.exists()) {
			destXmlDirFile.mkdirs();
		}
		try {
			String srxXmlPath = srcXmlDirFile.getCanonicalPath();
			String destXmlLocation = destXmlDirFile.getCanonicalPath();
			boolean copyXml = !(srxXmlPath.equals(destXmlLocation));
			String msg;
			if (copyXml) {
				msg = "XML will be copied to deployment location \n"
						+ destXmlLocation;
			} else {
				msg = "Source directory for XML is also designated deployment location.\n No need to copy XML";
			}
			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"IO Excption while trying to access XML directories ");
		}
		/*
		 * The XSLT has been developed and tested using the Saxon library.
		 * Results when using xalan are not guaranteed. Issue (a/o 2013-June) is
		 * level of support for XSLT 2 features.
		 */
		System.setProperty("javax.xml.transform.TransformerFactory", saxTFac);
	}

	public boolean process() {
		int identifier = 0;
		/*
		 * check to see if xmlSource is a directory. If so, process all XML
		 * files and generated an index in XML format. When we are done, the XML
		 * and a supporting XSLT will be used to generate HTML.
		 */
		File _input = new File(srcXmlDirLocation);
		if (_input.isDirectory()) {
			transformFileSet(_input, identifier);
		}
		return true;
	}

	/**
	 * @param _input
	 * @param identifier
	 */
	private void transformFileSet(File _input, int identifier) {
		File[] inputFiles = _input.listFiles();
		int fileCount = inputFiles.length;
		for (int i = 0; i < fileCount; i++) {
			File aFile = (File) inputFiles[i];
			String message = aFile.getName();
			if (aFile.isFile() && (aFile.getAbsolutePath().endsWith("xml"))) {
				try {
					Element nextRSyst = getAsXml(aFile);
					/*
					 * make sure its a Rating System and not some other XML that
					 * happens to be in this directory
					 */
					if (nextRSyst.getName().equals("RatingSystem")) {
						insertPatch(aFile, nextRSyst);
					}
				} catch (Exception e) {
					System.out.println("Error processing file " + message
							+ "\n");
					e.printStackTrace();
				}
			} else {
				System.out
						.println("Skipping " + message + " (not an XML file)");
			}
		}
	}

	public static Element getAsXml(File inputFile) throws JDOMException, IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				inputFile), "UTF-8");
		XMLReaderJDOMFactory readerFactory = new XMLReaderSAX2Factory(false);
		SAXBuilder builder = new SAXBuilder(readerFactory);
		Document validatedDoc;
		validatedDoc = builder.build(isr);
		Element rootEl = validatedDoc.getRootElement();
		return rootEl;
	}

	/**
	 * @param aFile
	 * @param nextRSyst
	 */
	private boolean insertPatch(File xmlFile, Element rSystemRoot) {
		boolean changed = false;
		// .......... START of changes
		// changed = mod1(rSystemRoot);
		// changed = mod2(rSystemRoot);
		// changed = mod3(rSystemRoot);
		// changed = mod5(rSystemRoot);
		// changed = mod6(rSystemRoot);
		// changed = mod7(rSystemRoot);
		// changed = mod8(rSystemRoot, xmlFile.getName());
		// changed = mod9(rSystemRoot, xmlFile.getName());
		// changed = mod10(rSystemRoot, xmlFile.getName());
		// changed = mod11(rSystemRoot, xmlFile.getName());
		changed = mod12(rSystemRoot, xmlFile.getName());
		// .......... end of changes
		if (changed) {
			// write file to designated location
			String name = xmlFile.getName();
			Format myFormat = Format.getPrettyFormat();
			XMLOutputter outputter = new XMLOutputter(myFormat);
			FileOutputStream fo = null;
			File movedFile = new File(destXmlDirLocation, name);
			try {
				fo = new FileOutputStream(movedFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			try {
				outputter.output(rSystemRoot, fo);
				fo.flush();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;
	}

	private boolean mod12(Element rSystemRoot, String fileName) {
		List<Element> mediaEList = rSystemRoot.getChildren("Media",
				SpecificationElement.mdcrNSpace);
		Object[] mediaTargets = mediaEList.toArray();
		for (int i = 0; i < mediaTargets.length; i++) {
			Element target = (Element) mediaTargets[i];
			rSystemRoot.removeContent(target);
		}
		List<Element> envEList = rSystemRoot.getChildren("Environment",
				SpecificationElement.mdcrNSpace);
		Object[] envTargets = envEList.toArray();
		for (int i = 0; i < envTargets.length; i++) {
			Element target = (Element) envTargets[i];
			rSystemRoot.removeContent(target);
		}
		// Currently under RatingSystemID
		Element rSysEl = rSystemRoot.getChild("RatingSystemID",
				SpecificationElement.mdcrNSpace);
		int regionIndex = rSystemRoot.indexOf(rSysEl) + 1;
		List<Element> regionList = rSysEl.getChildren("Region",
				SpecificationElement.mdcrNSpace);
		// now clone and move...
		for (int i = 0; i < regionList.size(); i++) {
			Element nextARegionEl = regionList.get(i);
			/*
			 * First region gets special treatment as we need to clone it as an
			 * AdoptiveRegion. All other get detached
			 */
			Element regionClone = null;
			if (i == 0) {
				regionClone = nextARegionEl.clone();
			} else {
				regionClone = nextARegionEl;
				regionClone.detach();
			}
			// rename and move
			regionClone.setName("AdoptiveRegion");
			rSystemRoot.addContent(regionIndex++, regionClone);
			Element usageEl = new Element("Usage",
					SpecificationElement.mdcrNSpace);
			regionClone.addContent(usageEl);
			for (int j = 0; j < mediaTargets.length; j++) {
				Element target = (Element) mediaTargets[j];
				Element clone = target.clone();
				usageEl.addContent(clone);
			} 
			for (int j = 0; j < envTargets.length; j++) {
				Element target = (Element) envTargets[j];
				Element clone = target.clone();
				usageEl.addContent(clone);
			}
		}
		return true;
	}

	private boolean mod11(Element rSystemRoot, String fileName) {
		Element orgEL = rSystemRoot.getChild("RatingsOrg",
				SpecificationElement.mdcrNSpace);
		Element infoEL = orgEL.getChild("ContactInfo",
				SpecificationElement.mdcrNSpace);
		if (infoEL != null) {
			infoEL.setName("ContactString");
		}
		return true;
	}

	private boolean mod10(Element rSystemRoot, String fileName) {
		List<Element> childList = rSystemRoot.getChildren("Media",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < childList.size(); i++) {
			Element mediaEL = childList.get(i);
			String mediaValue = mediaEL.getTextNormalize();
			if (mediaValue.equals("Games")) {
				mediaEL.setText("Game");
				return true;
			}
		}
		return false;
	}

	/**
	 * Recompute ordinal for any 'adult' rating as per Schema Def Sec 3.5.1
	 * "Use of Ordinal Attribute"
	 * 
	 * @param rSystemRoot
	 */
	private boolean mod9(Element rSystemRoot, String fileName) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			/* Need the current Ordinal value as well as the MinAge */
			Element ordinalEL = ratingEL.getChild("Value",
					SpecificationElement.mdcrNSpace);
			int minAge = getAgeAsInt(ratingEL, "MinAge");
			int minAgeRec = getAgeAsInt(ratingEL, "MinRecAge");
			if (minAge >= 18) {
				String previous = ordinalEL.getAttributeValue("ordinal");
				ordinalEL.setAttribute("ordinal", "80");
				String id = ordinalEL.getText();
				System.out.println(fileName + "-" + id + ": set to 80, was "
						+ previous);
			} else if ((minAge <= 0) && (minAgeRec <= 0)) {
				String previous = ordinalEL.getAttributeValue("ordinal");
				if (previous.equals("1")) {
					ordinalEL.setAttribute("ordinal", "0");
					String id = ordinalEL.getText();
					System.out.println(fileName + "-" + id + ": set to 0, was "
							+ previous);
				}

			}
		}
		return true;
	}

	private int getAgeAsInt(Element ratingEL, String property) {
		String ageS = ratingEL.getChildText(property,
				SpecificationElement.mdcrNSpace);
		if (ageS == null || ageS.isEmpty()) {
			return -1;
		}
		return Integer.parseInt(ageS);

	}

	/**
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod8(Element rSystemRoot, String fileName) {
		/*
		 * 1st re-map the Reason and create a HashMap of RatingReason elements
		 * from the Criteria. Then go thru the Ratings and replace the
		 * ApplicableReasons elements.
		 */
		HashMap<String, Element> criteriaMap = new HashMap<String, Element>();
		List<Element> childList = rSystemRoot.getChildren("Reason",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < childList.size(); i++) {
			Element reasonEL = childList.get(i);
			String langCode = reasonEL.getAttributeValue("language");
			reasonEL.removeAttribute("language");
			String reasonID = reasonEL.getAttributeValue("reasonID");
			Element valueEl = new Element("Value",
					SpecificationElement.mdcrNSpace);
			valueEl.setText(reasonID);
			reasonEL.addContent(0, valueEl);
			// next WAS optional but now required. It has also been moved
			Element defEl = reasonEL.getChild("Definition",
					SpecificationElement.mdcrNSpace);
			if (defEl == null) {
				defEl = new Element("Definition",
						SpecificationElement.mdcrNSpace);
				defEl.setText("n.a");
			} else {
				defEl.detach();
			}
			// next is optional
			Element explainEl = reasonEL.getChild("Explanation",
					SpecificationElement.mdcrNSpace);
			// now create the new GeneralDescriptor
			Element gdEl = new Element("GeneralDescriptor",
					SpecificationElement.mdcrNSpace);
			reasonEL.addContent(1, gdEl);
			Element gdLabelEl = new Element("Label",
					SpecificationElement.mdcrNSpace);
			gdLabelEl.setText(reasonID);
			gdEl.addContent(gdLabelEl);
			gdEl.addContent(defEl);
			if (explainEl != null) {
				explainEl.detach();
				gdEl.addContent(explainEl);
			}
			// next is an error to be corrected
			Element badEl = reasonEL.getChild("Label",
					SpecificationElement.mdcrNSpace);
			if (badEl != null) {
				badEl.detach();
			}
			/*
			 * Next step is to convert the CRITERIA to RatingReason/Descriptor
			 * elements
			 */
			List<Element> criteriaList = reasonEL.getChildren("Criteria",
					SpecificationElement.mdcrNSpace);
			// need to deal with 'live list' issue
			Element[] working = new Element[criteriaList.size()];
			working = criteriaList.toArray(working);
			for (int j = 0; j < working.length; j++) {
				Element nextCriteria = working[j];
				Element criteriaDescEl = new Element("Descriptor",
						SpecificationElement.mdcrNSpace);
				/*
				 * the language, Label, and Definition are the same as the
				 * 'generic'. Only the (optional) Explanation is unique.
				 */
				criteriaDescEl.setAttribute("language", langCode);
				criteriaDescEl.addContent(gdLabelEl.clone());
				criteriaDescEl.addContent(defEl.clone());
				Element cxEl = nextCriteria.getChild("Explanation",
						SpecificationElement.mdcrNSpace);
				if (cxEl != null) {
					cxEl.detach();
					criteriaDescEl.addContent(cxEl);
				}
				// create the parent RatingReason
				Element rrEl = new Element("RatingReason",
						SpecificationElement.mdcrNSpace);
				/*
				 * the reasonID, Value, and (optional) LinkToLogo are the same
				 * as the 'generic'.
				 */
				rrEl.setAttribute("reasonID", reasonID);
				rrEl.addContent(valueEl.clone());
				rrEl.addContent(criteriaDescEl);
				Element l2lEl = reasonEL.getChild("LinkToLogo",
						SpecificationElement.mdcrNSpace);
				if (l2lEl != null) {
					rrEl.addContent(l2lEl.clone());
				}
				/*
				 * finish up by adding new element to hashmap and detaching old
				 * element
				 */
				nextCriteria.detach();
				String key = nextCriteria.getAttributeValue("ratingID") + "<->"
						+ reasonID;
				criteriaMap.put(key, rrEl);
			}
		}
		/*
		 * END of Step 1. Now go thru the Ratings and replace the
		 * ApplicableReasons elements.
		 */

		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			// if old schema....
			String ratingID = ratingEL.getChildText("Value",
					SpecificationElement.mdcrNSpace);
			// if new schema....
			// String ratingID = ratingEL.getAttributeValue("ratingID");
			List<Element> arList = ratingEL.getChildren("ApplicableReason",
					SpecificationElement.mdcrNSpace);
			// need to deal with 'live list' issue
			Element[] working = new Element[arList.size()];
			working = arList.toArray(working);
			for (int j = 0; j < working.length; j++) {
				Element arEL = working[j];
				String reasonID = arEL.getAttributeValue("id");
				String key = ratingID + "<->" + reasonID;
				Element rrEL = criteriaMap.get(key);
				arEL.detach();
				try {
					ratingEL.addContent(rrEL);
				} catch (Exception e) {
					System.out.println("WARNING: Can not match Criteria key '"
							+ key + "' in " + fileName);
					// create a minimal default RatingReason
					rrEL = new Element("RatingReason",
							SpecificationElement.mdcrNSpace);
					/*
					 * the reasonID, Value, and (optional) LinkToLogo are the
					 * same as the 'generic'.
					 */
					rrEL.setAttribute("reasonID", reasonID);
					Element valueEl = new Element("Value",
							SpecificationElement.mdcrNSpace);
					valueEl.setText(reasonID);
					rrEL.addContent(valueEl);
					ratingEL.addContent(rrEL);
				}
			}
		}
		return true;
	}

	private boolean mod7(Element rSystemRoot) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			List<Element> descList = ratingEL.getChildren("Descriptor",
					SpecificationElement.mdcrNSpace);
			for (int j = 0; j < descList.size(); j++) {
				Element descriptorEL = descList.get(j);
				Element defUrlEl = descriptorEL.getChild("DefinitionURL",
						SpecificationElement.mdcrNSpace);
				if (defUrlEl != null) {
					defUrlEl.detach();
				}
			}
		}
		return true;
	}

	/**
	 * the '<tt>lang</tt>' attribute renamed to '<tt>language</tt>'
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod6(Element rSystemRoot) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			List<Element> descList = ratingEL.getChildren("Descriptor",
					SpecificationElement.mdcrNSpace);
			for (int j = 0; j < descList.size(); j++) {
				Element descriptorEL = descList.get(j);
				String languageCode = descriptorEL
						.getAttributeValue("lang", "");
				descriptorEL.setAttribute("language", languageCode);
				descriptorEL.removeAttribute("lang");
			}
		}
		List<Element> reasonList = rSystemRoot.getChildren("Reason",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < reasonList.size(); i++) {
			Element reasonEL = reasonList.get(i);
			String languageCode = reasonEL.getAttributeValue("lang", "");
			reasonEL.setAttribute("language", languageCode);
			reasonEL.removeAttribute("lang");
		}
		return true;
	}

	private boolean mod5(Element rSystemRoot) {
		List<Element> childList = rSystemRoot.getChildren("Reason",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < childList.size(); i++) {
			Element reasonEL = childList.get(i);
			Element labelEl = reasonEL.getChild("Label",
					SpecificationElement.mdcrNSpace);
			String value = labelEl.getText();
			reasonEL.setAttribute("reasonID", value);
		}
		return true;
	}

	/**
	 * Restructuring of the 'long' text into 2 parts: Definition & Explanation.
	 * The 1st is limited to 1 sentence.
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod4(Element rSystemRoot) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			List<Element> descList = ratingEL.getChildren("Descriptor",
					SpecificationElement.mdcrNSpace);
			for (int j = 0; j < descList.size(); j++) {
				Element descriptorEL = descList.get(j);
				Element defEl = descriptorEL.getChild("Definition",
						SpecificationElement.mdcrNSpace);
				if (defEl != null) {
					String curDesc = defEl.getTextNormalize();
					if (!curDesc.equalsIgnoreCase("n.a.")) {
						String[] parts = curDesc.split("\\.", 2);
						if (parts.length > 1) {
							defEl.setText(parts[0] + ".");
							Element expEl = new Element("Explanation",
									SpecificationElement.mdcrNSpace);
							expEl.setText(parts[1]);
							descriptorEL.addContent(expEl);
						}
					}
				}
			}

		}
		return true;
	}

	/**
	 * Add in the "HPCApplicable" property for each Rating
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod3(Element rSystemRoot) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			Element hpcFlagEl = new Element("HPCApplicable",
					SpecificationElement.mdcrNSpace);

			Element depEl = ratingEL.getChild("Deprecated",
					SpecificationElement.mdcrNSpace);
			boolean deprecated = false;
			if (depEl != null) {
				deprecated = depEl.getText().equalsIgnoreCase("true");
			}
			if (deprecated) {
				hpcFlagEl.setText("false");
			} else {
				hpcFlagEl.setText("true");
			}
			// insert BEFORE 1st Descriptor
			List<Element> descList = ratingEL.getChildren("Descriptor",
					SpecificationElement.mdcrNSpace);
			int pos = ratingEL.indexOf(descList.get(0));
			ratingEL.addContent(pos, hpcFlagEl);
		}
		return true;
	}

	/**
	 * Adds 'ordinal' attribute along with an estimated value based on the age
	 * attributes of the rating.
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod2(Element rSystemRoot) {
		List<Element> ratingList = rSystemRoot.getChildren("Rating",
				SpecificationElement.mdcrNSpace);
		int previousScore = -1;
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			Element valueEL = ratingEL.getChild("Value",
					SpecificationElement.mdcrNSpace);
			valueEL.removeAttribute("ordinal", SpecificationElement.mdcrNSpace);
			/*
			 * Gen a 'score' using the ages. Order of merge is to use the MinAge
			 * if present, else use MinRecAge. The MinAgeSupervised is ignored.
			 * To indicate that a MinAge is more restrictive than a MinRecAge,
			 * the values are multiplied by 10, than 5 is added if MinAge is
			 * base for the score.
			 */
			int score = 0;
			String minAgeS = ratingEL.getChildText("MinAge",
					SpecificationElement.mdcrNSpace);
			if (minAgeS == null || minAgeS.isEmpty()) {
				minAgeS = "0";
			}
			int v1 = Integer.parseInt(minAgeS);
			String minRecAgeS = ratingEL.getChildText("MinRecAge",
					SpecificationElement.mdcrNSpace);
			if (minRecAgeS == null || minRecAgeS.isEmpty()) {
				minRecAgeS = "0";
			}
			int v2 = Integer.parseInt(minRecAgeS);
			if (v1 > 0) {
				score = 5 + (10 * v1);
			} else {
				score = (10 * v2);
			}
			// ensure we have an increasing value
			if (score == previousScore) {
				score = previousScore + 1;
			}
			if (score == 0
					&& (ratingEL == ratingList.get(ratingList.size() - 1))) {
				// last element. Score=0 means BANNED or EXEMPT
				score = 300;
			}
			valueEL.setAttribute("ordinal", Integer.toString(score));
			previousScore = score;
		}
		return true;
	}

	/**
	 * @param rSystemRoot
	 * @return
	 */
	private boolean mod1(Element rSystemRoot) {
		Element rSysIDEL = rSystemRoot.getChild("RatingSystemID",
				SpecificationElement.mdcrNSpace);
		Element regionEL = rSysIDEL.getChild("Region",
				SpecificationElement.mdcrNSpace);
		String cCode = regionEL.getChildText("country",
				SpecificationElement.mdNSpace);
		return true;
	}
}
