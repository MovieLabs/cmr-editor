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

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;
import org.jdom2.util.IteratorIterable;

import com.movielabs.cmr.client.CsvExporter;
import com.movielabs.cmr.client.rspec.SpecificationElement;
import com.movielabs.cmr.client.util.TimeStamp;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Generates the final 'published' forms of all Rating Systems. This consists of
 * the following functions:
 * <ul>
 * <li>Handle conversion of each Rating System spec from XML into HTML.</li>
 * <li>Handle conversion of each Rating System from the <i>internal</i> XML
 * schema to the latest version of the
 * <tt>Common Metadata Content Rating Structure</tt> schema (see
 * <a href='http://movielabs.com/md/ratings/doc.html'>http://movielabs.com/md/
 * ratings/doc.html</a>)</li>
 * <li>generates HTML with aggregate summary of all ratings systems</li>
 * </ul>
 * 
 * @author L. J. Levin, created July 23, 2013
 * 
 */
public class BatchXSLT {
	private static String saxTFac = "net.sf.saxon.TransformerFactoryImpl";
	public static final String fileSuffix = "_Ratings";
	public static final String schemaFile = "mdcr-v1.1.xsd";
	private File xsltDirF;
	private Element allSystemsForXml;
	private File destHtmlDir;
	private File destXmlDirFile;
	private boolean copyXml;
	private String srcXmlDirLocation;
	private String destXmlLocation;
	private boolean clearDestDir = true;
	private boolean patchXml = false;
	private String rsrcDir;
	private String rootDstDir;
	private String disclaimer;
	private String releaseVersion;
	private File baseReleaseDir;

	/**
	 * @param srcXmlDir
	 * @param rootDstDir
	 * @param rsrcDir
	 */
	public BatchXSLT(String srcXmlDir, String rootDstDir, String rsrcDir) {
		this.rootDstDir = rootDstDir;
		baseReleaseDir = new File(rootDstDir);
		releaseVersion = baseReleaseDir.getName();
		this.rsrcDir = rsrcDir;
		String xsltDir = rsrcDir + File.separator + "transforms";
		xsltDirF = new File(xsltDir);
		if (!xsltDirF.exists()) {
			throw new RuntimeException("Transform directory not found. Specified as '" + xsltDir + "'");
		}
		srcXmlDirLocation = srcXmlDir;
		File srcXmlDirFile = new File(srcXmlDirLocation);
		if (!srcXmlDirFile.exists()) {
			srcXmlDirFile.mkdirs();
		}
		destXmlDirFile = new File(rootDstDir + "/xml");
		if (!destXmlDirFile.exists()) {
			destXmlDirFile.mkdirs();
		}
		try {
			String srxXmlPath = srcXmlDirFile.getCanonicalPath();
			destXmlLocation = destXmlDirFile.getCanonicalPath();
			copyXml = !(srxXmlPath.equals(destXmlLocation));
			String msg;
			if (copyXml) {
				msg = "XML will be copied to deployment location \n" + destXmlLocation;
			} else {
				msg = "Source directory for XML is also designated deployment location.\n No need to copy XML";
			}
			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO Excption while trying to access XML directories ");
		}
		if (clearDestDir && copyXml) {
			clearAll(destXmlDirFile);
		}

		destHtmlDir = new File(rootDstDir + "/html");
		if (!destHtmlDir.exists()) {
			destHtmlDir.mkdirs();
		} else {
			if (clearDestDir) {
				clearAll(destHtmlDir);
			}
		}
		/*
		 * The XSLT has been developed and tested using the Saxon library.
		 * Results when using xalan are not guaranteed. Issue (a/o 2013-June) is
		 * level of support for XSLT 2 features.
		 */
		System.setProperty("javax.xml.transform.TransformerFactory", saxTFac);
		disclaimer = "DISCLAIMER: Although care has been taken to ensure the accuracy, completeness and reliability of the information provided, "
				+ "we are not responsible if information that we make available on this site is not accurate, complete or current. The material "
				+ "on this site is provided for general information only, and any reliance upon the material on this site will be at your own "
				+ "risk. We reserve the right to modify the contents of the site at any time, but we have no obligation to update any information "
				+ "on this site. You agree that it is your 	responsibility to monitor changes to the site.";
	}

	/**
	 * @param directory
	 */
	private void clearAll(File directory) {
		File[] inputFiles = directory.listFiles();
		int fileCount = inputFiles.length;
		for (int i = 0; i < fileCount; i++) {
			File aFile = (File) inputFiles[i];
			aFile.delete();
		}
	}

	public boolean process() {
		int identifier = 0;
		/*
		 * check to see if xmlSource is a directory. If so, process all XML
		 * files and generated an index in XML format. When we are done, the XML
		 * and a supporting XSLT will be used to generate HTML.
		 */
		File _input = new File(srcXmlDirLocation);
		if (_input.isFile()) {
			// if it's just a file, transform it...
			try {
				transformFile(_input, "", ++identifier);
			} catch (Exception e) {
				System.out.println("Error processing file");
				e.printStackTrace();
				return false;
			}
		} else if (_input.isDirectory()) {
			transformFileSet(_input, identifier);
		}
		try {
			copyResources();
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeReleaseInfo(releaseVersion, SpecificationElement.MDCR_VER);
		return true;
	}

	/**
	 * @throws Exception
	 * 
	 */
	private void copyResources() throws Exception {
		File destDir = new File(destHtmlDir.getCanonicalPath() + "/imageCache");
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File srcDir = new File(srcXmlDirLocation + "/../imageCache");
		File[] inputFiles = srcDir.listFiles();
		int fileCount = inputFiles.length;
		for (int i = 0; i < fileCount; i++) {
			File srcFile = (File) inputFiles[i];
			if (srcFile.isFile()) {
				File destFile = new File(destDir, srcFile.getName());
				copyFile(srcFile, destFile);
			}
		}
		/*
		 * We also copy the current Schema to the release directory
		 */
		File srcFile = new File(rsrcDir, schemaFile);
		File destFile = new File(rootDstDir, schemaFile);
		copyFile(srcFile, destFile);
	}

	private void copyFile(File srcFile, File destFile) throws Exception {
		FileChannel fileSource = new FileInputStream(srcFile).getChannel();
		FileChannel destination = new FileOutputStream(destFile).getChannel();
		destination.transferFrom(fileSource, 0, fileSource.size());
		if (fileSource != null) {
			fileSource.close();
		}
		if (destination != null) {
			destination.close();
		}
	}

	/**
	 * @param _input
	 * @param identifier
	 */
	private void transformFileSet(File _input, int identifier) {
		/*
		 * this is used to create a 'master' all-encompassing XML file for use
		 * by UltraViolet
		 */
		allSystemsForXml = new Element("RatingSystemSet", SpecificationElement.mdcrNSpace);
		allSystemsForXml.addNamespaceDeclaration(SpecificationElement.mdNSpace);
		allSystemsForXml.addNamespaceDeclaration(SpecificationElement.xsiNSpace);
		allSystemsForXml.setAttribute("schemaLocation", SpecificationElement.schemaLoc, SpecificationElement.xsiNSpace);
		String blurb = "Saved " + TimeStamp.asString() + " as version " + releaseVersion;
		Comment versionInfo = new Comment(blurb);
		allSystemsForXml.addContent(versionInfo);
		Element allSystemsForHtml = allSystemsForXml.clone();
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
						if (copyXml) {
							String destXml = destXmlLocation + "/" + aFile.getName();
							modifyPublishedXml(nextRSyst);
							Element cloned = nextRSyst.clone();
							allSystemsForHtml.addContent(cloned);
							insertMod_05(nextRSyst); // <--- removes 'Notes'
							insertMod_06(nextRSyst); // wrap <div
														// class='userHtml'> in
														// [CDATA]
							outputXml(destXml, nextRSyst);
						}
						/*
						 * now its OK to manipulate XML so we can append to the
						 * master file.
						 */
						nextRSyst.detach();
						/*
						 * The schemaLocation needs to be removed as the
						 * attribute is already set in the root of the master
						 * doc and can only appear once.
						 */
						nextRSyst.removeAttribute("schemaLocation", SpecificationElement.xsiNSpace);

						allSystemsForXml.addContent(nextRSyst);
						/*
						 * NOTE that xslt operates on a 'fresh' copy of original
						 * src XML so any manipulation to the published copy are
						 * not relevant.
						 */
						transformFile(aFile, message, ++identifier);
					}
				} catch (Exception e) {
					System.out.println("Error processing file " + message + "\n");
					e.printStackTrace();
				}
			} else {
				System.out.println("Skipping " + message + " (not an XML file)");
			}
		}
		// use version WITH the 'Notes' when generating HTML
		try {
			// transformIndex();
			genSummary(allSystemsForHtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		File csvFile = new File(baseReleaseDir, "CMR_Ratings_" + releaseVersion + ".csv");
		try {
			CsvExporter.export(allSystemsForXml, csvFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * Last step is output the 'master' XML that has everything in one big
		 * file.
		 */
		File xmlFile = new File(baseReleaseDir, "CMR_Ratings_" + releaseVersion + ".xml");
		Format myFormat = Format.getPrettyFormat();
		XMLOutputter outputter = new XMLOutputter(myFormat);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(xmlFile), "UTF-8");
			// use version WITH OUT the <Notes> element
			outputter.output(new Document(allSystemsForXml), osw);
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void genSummary(Element rootEl) throws JDOMException, TransformerException, IOException {
		Document masterDoc = new Document(rootEl);
		String outputFile = new String(destHtmlDir + "/Summary.html");
		String xsltFile = new String(xsltDirF + "/WrappedSummary_w_Ratings.xslt");
		genSummary(masterDoc, outputFile, xsltFile);
		outputFile = new String(destHtmlDir + "/Summary_without_Ratings.html");
		xsltFile = new String(xsltDirF + "/WrappedSummaryV2.xslt");
		genSummary(masterDoc, outputFile, xsltFile);
	}

	private void genSummary(Document masterDoc, String outputFileName, String xsltFile)
			throws JDOMException, TransformerException, IOException {
		DOMOutputter converter = new DOMOutputter();
		org.w3c.dom.Document domDoc = converter.output(masterDoc);
		FileOutputStream fos = new FileOutputStream(outputFileName);
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFile));
		transformer.setParameter("mLabVersion", releaseVersion);
		transformer.transform(new DOMSource(domDoc), new StreamResult(fos));
		fos.flush();
		fos.close();
	}

	/**
	 * @param outputFileName
	 * @param xmlStuff
	 * @return
	 */
	private boolean outputXml(String outputFileName, Element xmlStuff) {
		Comment disclamerNode = new Comment(disclaimer);
		xmlStuff.addContent(1, disclamerNode);
		Format myFormat = Format.getPrettyFormat();
		XMLOutputter outputter = new XMLOutputter(myFormat);
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			outputter.output(xmlStuff, fo);
			fo.flush();
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Three XSLT transforms are used, all of which should be located in
	 * <tt>${rsrcDir}/transforms</tt>. The transforms are:
	 * <ul>
	 * <li><tt>WrappedRatingSysDoc.xslt</tt> used to convert a single
	 * XML-defined Rating System into an HTML description.</li>
	 * <li><tt>WrappedRatingSysDoc.xslt</tt> used to gen the index page</li>
	 * </ul>
	 * 
	 * @param inputFile
	 * @param message
	 * @param identifier
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void transformFile(File inputFile, String message, int identifier)
			throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException {

		String outputFileName = new String(destHtmlDir + "/" + inputFile.getName());
		if (outputFileName.matches("^.+xml$")) {
			StringBuilder b = new StringBuilder(outputFileName);
			b.replace(outputFileName.lastIndexOf("xml"), outputFileName.lastIndexOf("xml") + 3, "html");
			outputFileName = b.toString();
		} else {
			outputFileName = outputFileName + ".html";
		}

		System.out.println("Transforming XML file: " + inputFile.getPath() + "    " + message);
		/*
		 * Use the static TransformerFactory.newInstance() method to instantiate
		 * a TransformerFactory. The javax.xml.transform.TransformerFactory
		 * system property setting determines the actual class to instantiate --
		 * org.apache.xalan.transformer.TransformerImpl.
		 */
		TransformerFactory tFactory = TransformerFactory.newInstance();
		/*
		 * Use the TransformerFactory to instantiate a Transformer that will
		 * work with the stylesheet you specify. This method call also processes
		 * the stylesheet into a compiled Templates object.
		 */
		String xsltFileName = new String(xsltDirF + "/WrappedRatingSysDoc.xslt");
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFileName));
		transformer.setParameter("identifier", new Integer(identifier));
		/*
		 * Use the Transformer to apply the associated Templates object to an
		 * XML document (foo.xml) and write the output to a file (foo.out).
		 */
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8");
		transformer.transform(new StreamSource(inputFile), new StreamResult(osw));
	}

	private Element getAsXml(File inputFile) throws JDOMException, IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		XMLReaderJDOMFactory readerFactory = new XMLReaderSAX2Factory(false);
		SAXBuilder builder = new SAXBuilder(readerFactory);
		Document validatedDoc;
		validatedDoc = builder.build(isr);
		Element rootEl = validatedDoc.getRootElement();
		return rootEl;
	}

	/**
	 * Generate the <tt>releaseSpecific.js</tt> that is used by the
	 * <tt>index.html</tt> page. This contains release-specific information,
	 * such as version and date, that is displayed on the index page.
	 * 
	 * @param aFile
	 * @param nextRSyst
	 */
	private void writeReleaseInfo(String rVer, String sVer) {
		String content = "";
		content = content + "function getLatestRatingsVersion() {" + "return '" + rVer + "';}\n\n";

		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
		String curDate = sdf.format(cal.getTime());
		content = content + "function getLatestRatingsDate() { " + "return '" + curDate + "';}\n\n";

		content = content + "function getLatestSchemaVersion() { " + "return '" + sVer + "';}\n\n";

		File file = new File(baseReleaseDir.getParent(), "releaseSpecific.js");

		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param nextRSyst
	 */
	private void modifyPublishedXml(Element nextRSyst) {
		insertMod_01(nextRSyst);
		// insertMod_02(nextRSyst);
		insertMod_03(nextRSyst);
		insertMod_04(nextRSyst);
		insertMod_07(nextRSyst);
	}

	/**
	 * <tt>version</tt> and <tt>deprecated</tt> attributes moved from
	 * <tt>RatingSystemID</tt> to <tt>RatingSystem</tt> element
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean insertMod_01(Element rSystemRoot) {
		Element rSysIDEL = rSystemRoot.getChild("RatingSystemID", SpecificationElement.mdcrNSpace);
		rSystemRoot.removeAttribute("lastHtmlGen");
		// move version
		String value = rSysIDEL.getAttributeValue("version", "");
		rSystemRoot.setAttribute("version", value);
		rSysIDEL.removeAttribute("version");
		// move deprecated
		value = rSysIDEL.getAttributeValue("deprecated", "");
		if (value.equalsIgnoreCase("true")) {
			rSystemRoot.setAttribute("deprecated", value);
		}
		rSysIDEL.removeAttribute("deprecated");
		return true;
	}

	/**
	 * <tt>RatingSystemID</tt> is again limited to a single child
	 * <tt>Region</tt>. Multi-region support is now via 1 or more
	 * <tt>AdoptiveRegion</tt> elements
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean insertMod_02(Element rSystemRoot) {
		Element rSysIDEL = rSystemRoot.getChild("RatingSystemID", SpecificationElement.mdcrNSpace);
		List<Element> regionList = rSysIDEL.getChildren("Region", SpecificationElement.mdcrNSpace);
		Element primaryRegionEL = regionList.get(0);
		// clone it
		Element cloned = primaryRegionEL.clone();
		cloned.setName("AdoptiveRegion");
		int index = rSystemRoot.indexOf(rSysIDEL) + 1;
		rSystemRoot.addContent(index, cloned);
		if (regionList.size() > 1) {
			for (int i = 1; i < regionList.size(); i++) {
				Element regionEL = regionList.get(i);
				regionEL.detach();
				regionEL.setName("AdoptiveRegion");
				index++;
				rSystemRoot.addContent(index, regionEL);
			}
		}
		return true;
	}

	/**
	 * changes to <tt>Rating</tt> element:
	 * <ul>
	 * <li><tt>ordinal</tt> is no longer attribute of child element
	 * <tt>Value</tt> but is now an Element itself</li>
	 * <li>child element <tt>Value</tt> replaced by <tt>ratingID</tt> attribute
	 * </li>
	 * </ul>
	 * 
	 * @param rSys
	 */
	private void insertMod_03(Element rSys) {
		List<Element> ratingList = rSys.getChildren("Rating", SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			Element valueEL = ratingEL.getChild("Value", SpecificationElement.mdcrNSpace);
			String id = valueEL.getText();
			String ordinal = valueEL.getAttributeValue("ordinal");
			ratingEL.setAttribute("ratingID", id);
			Element ordinalEl = new Element("Ordinal", SpecificationElement.mdcrNSpace);
			ordinalEl.setText(ordinal);
			ratingEL.setContent(0, ordinalEl);
			valueEL.detach();
		}
	}

	private void insertMod_04(Element rSystemRoot) {
		String defaultLangCode = getDefaultLang(rSystemRoot);
		/*
		 * 1st re-map the Reason and create a HashMap of RatingReason elements
		 * from the Criteria. Then go thru the Ratings and replace the
		 * ApplicableReasons elements.
		 */
		HashMap<String, Element> criteriaMap = new HashMap<String, Element>();
		List<Element> childList = rSystemRoot.getChildren("Reason", SpecificationElement.mdcrNSpace);
		for (int i = 0; i < childList.size(); i++) {
			Element reasonEL = childList.get(i);
			String langCode = reasonEL.getAttributeValue("language");
			reasonEL.removeAttribute("language");
			String reasonID = reasonEL.getAttributeValue("reasonID");
			Element valueEl = new Element("Value", SpecificationElement.mdcrNSpace);
			valueEl.setText(reasonID);
			reasonEL.addContent(0, valueEl);
			// next WAS optional but now required. It has also been moved
			Element defEl = reasonEL.getChild("Definition", SpecificationElement.mdcrNSpace);
			if (defEl == null) {
				defEl = new Element("Definition", SpecificationElement.mdcrNSpace);
				defEl.setText("n.a");
			} else {
				defEl.detach();
			}
			// next is optional
			Element explainEl = reasonEL.getChild("Explanation", SpecificationElement.mdcrNSpace);
			// now create the new GeneralDescriptor
			Element gdEl = new Element("GeneralDescriptor", SpecificationElement.mdcrNSpace);
			reasonEL.addContent(1, gdEl);
			gdEl.setAttribute("language", defaultLangCode);
			Element gdLabelEl = new Element("Label", SpecificationElement.mdcrNSpace);
			gdLabelEl.setText(reasonID);
			gdEl.addContent(gdLabelEl);
			gdEl.addContent(defEl);
			if (explainEl != null) {
				explainEl.detach();
				gdEl.addContent(explainEl);
			}
			// next is an error to be corrected
			Element badEl = reasonEL.getChild("Label", SpecificationElement.mdcrNSpace);
			if (badEl != null) {
				badEl.detach();
			}
			/*
			 * Next step is to convert the CRITERIA to RatingReason/Descriptor
			 * elements
			 */
			List<Element> criteriaList = reasonEL.getChildren("Criteria", SpecificationElement.mdcrNSpace);
			// need to deal with 'live list' issue
			Element[] working = new Element[criteriaList.size()];
			working = criteriaList.toArray(working);
			for (int j = 0; j < working.length; j++) {
				Element nextCriteria = working[j];
				Element criteriaDescEl = new Element("Descriptor", SpecificationElement.mdcrNSpace);
				/*
				 * the language, Label, and Definition are the same as the
				 * 'generic'. Only the (optional) Explanation is unique.
				 */
				criteriaDescEl.setAttribute("language", langCode);
				criteriaDescEl.addContent(gdLabelEl.clone());
				criteriaDescEl.addContent(defEl.clone());
				Element cxEl = nextCriteria.getChild("Explanation", SpecificationElement.mdcrNSpace);
				if (cxEl != null) {
					cxEl.detach();
					criteriaDescEl.addContent(cxEl);
				}
				// create the parent RatingReason
				Element rrEl = new Element("RatingReason", SpecificationElement.mdcrNSpace);
				/*
				 * the reasonID, Value, and (optional) LinkToLogo are the same
				 * as the 'generic'.
				 */
				rrEl.setAttribute("reasonID", reasonID);
				rrEl.addContent(valueEl.clone());
				rrEl.addContent(criteriaDescEl);
				Element l2lEl = reasonEL.getChild("LinkToLogo", SpecificationElement.mdcrNSpace);
				if (l2lEl != null) {
					rrEl.addContent(l2lEl.clone());
				}
				/*
				 * finish up by adding new element to hashmap and detaching old
				 * element
				 */
				nextCriteria.detach();
				String key = nextCriteria.getAttributeValue("ratingID") + "<->" + reasonID;
				criteriaMap.put(key, rrEl);
			}
		}
		/*
		 * END of Step 1. Now go thru the Ratings and replace the
		 * ApplicableReasons elements.
		 */

		List<Element> ratingList = rSystemRoot.getChildren("Rating", SpecificationElement.mdcrNSpace);
		for (int i = 0; i < ratingList.size(); i++) {
			Element ratingEL = ratingList.get(i);
			// if old schema....
			// String ratingID = ratingEL.getChildText("Value",
			// SpecificationElement.mdcrNSpace);
			// if new schema....
			String ratingID = ratingEL.getAttributeValue("ratingID");
			List<Element> arList = ratingEL.getChildren("ApplicableReason", SpecificationElement.mdcrNSpace);
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
					System.out.println("WARNING: Can not match Criteria key '" + key + "' in " + "fileName????");
					// create a minimal default RatingReason
					rrEL = new Element("RatingReason", SpecificationElement.mdcrNSpace);
					/*
					 * the reasonID, Value, and (optional) LinkToLogo are the
					 * same as the 'generic'.
					 */
					rrEL.setAttribute("reasonID", reasonID);
					Element valueEl = new Element("Value", SpecificationElement.mdcrNSpace);
					valueEl.setText(reasonID);
					rrEL.addContent(valueEl);
					ratingEL.addContent(rrEL);
				}
			}
		}
	}

	private boolean insertMod_05(Element rSystemRoot) {
		Element noteEl = rSystemRoot.getChild("Notes", SpecificationElement.mdcrNSpace);
		if (noteEl != null) {
			noteEl.detach();
		}
		return true;
	}

	/**
	 * Schema compliance issue: embedded HTML is allowed for use when generating
	 * the <i>Explanation</i> part of a RatingSystem's HTML page but it is
	 * exported in XML files as [CDATA].
	 * 
	 * @param rSystemRoot
	 * @return
	 */
	private boolean insertMod_06(Element rSystemRoot) {
		XMLOutputter xmlOut = new XMLOutputter();
		Filter<Element> divFilter = Filters.element("div");
		IteratorIterable<Element> targetIterator = rSystemRoot.getDescendants(divFilter);
		ArrayList<Element> divElList = new ArrayList<Element>();
		while (targetIterator.hasNext()) {
			divElList.add(targetIterator.next());
		}
		int cnt = 0;
		for (Element nextDivEl : divElList) {
			String divContent = xmlOut.outputString(nextDivEl);
			Element parent = nextDivEl.getParentElement();
			int loc = parent.indexOf(nextDivEl);
			nextDivEl.detach();
			CDATA cdEl = new CDATA(divContent);
			parent.setContent(loc, cdEl);
			cnt++;
		}
		// System.out.println("Found " + cnt + " <div> elements");
		return true;
	}

	private boolean insertMod_07(Element rSystemRoot) {
		Filter<Element> divFilter = Filters.element("OrgType", SpecificationElement.mdcrNSpace);
		IteratorIterable<Element> targetIterator = rSystemRoot.getDescendants(divFilter);
		/*
		 * Need to convert Iterable to a List in order to avoid a concurrent
		 * modification exception when doing the 'setText()'
		 */
		List<Element> eList = new ArrayList<Element>();
		while (targetIterator.hasNext()) {
			Element nextEl = targetIterator.next();
			eList.add(nextEl);
		}
		for (int i = 0; i < eList.size(); i++) {
			Element nextEl = eList.get(i);
			String value = nextEl.getText();
			if (value.equalsIgnoreCase("not_specified")) {
				nextEl.setText("not specified");
			}
		}
		return true;
	}

	/**
	 * @param rSystemRoot
	 * @return
	 */
	private String getDefaultLang(Element rSystemRoot) {
		Element firstRating = rSystemRoot.getChild("Rating", SpecificationElement.mdcrNSpace);
		String code = firstRating.getChild("Descriptor", SpecificationElement.mdcrNSpace).getAttributeValue("language",
				"en");
		return code;
	}
}
