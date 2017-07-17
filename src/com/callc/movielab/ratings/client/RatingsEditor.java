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
package com.callc.movielab.ratings.client;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeNode;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.JDOMParseException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.awt.BorderLayout;

import com.callc.movielab.ratings.BatchXSLT;
import com.callc.movielab.ratings.client.resources.GuiSettings;
import com.callc.movielab.ratings.client.rspec.RSpecLeaf;
import com.callc.movielab.ratings.client.rspec.RatingSystem;
import com.callc.movielab.ratings.client.rspec.SpecificationElement;
import com.callc.movielab.ratings.client.widgets.HeaderPanel;
import com.callc.movielab.ratings.client.widgets.ProgressDisplay;
import com.callc.movielab.ratings.client.widgets.RSpecNavPanel;
import com.critarch.util.logging.TimeStamp;
import com.critarch.util.ui.FileChooserDialog;
import com.critarch.util.ui.JHelpMgr;
import com.critarch.util.ui.PSplitPane;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author L. J. Levin, created Aug 23, 2013
 * 
 */
public class RatingsEditor {

	public static enum XF_TASKTYPE {
		READ, WRITE, VALIDATE
	}

	private static final int MAX_RECENT = 8;

	private JFrame frmCommonMetadataRatings;
	private HeaderPanel headerPanel;
	private PSplitPane mainPanel;

	private File userPropFile;

	private Properties properties;
	private XmlFileTask task;
	public ProgressDisplay progressDialog;
	private BlockingGlassPane glass;
	private RSpecNavPanel navTree;
	private RatingSystem ratingSystem;
	private Properties language;
	private Element clipboardXml;
	private boolean newFile;
	public File currentFile;
	protected static RatingsEditor window;
	protected List<String> recentFileList = new ArrayList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new RatingsEditor();
					window.frmCommonMetadataRatings.setVisible(true);
					window.mainPanel.setDividerLocation(0.15);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @return the window
	 */
	public static RatingsEditor getEditor() {
		return window;
	}

	/**
	 * Create the application.
	 */
	RatingsEditor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JHelpMgr.initializeHelpTools(this.getClass(), "resources/help/en");
		String NAME_SETTINGSFILE = ".movielab.r-edit.ini";
		String PATH_USERSETTINGS = null;
		try {
			PATH_USERSETTINGS = System.getProperty("user.home")
					+ File.separator + NAME_SETTINGSFILE;
		} catch (SecurityException e) {
			// ignore
		}
		properties = new Properties();
		userPropFile = new File(PATH_USERSETTINGS);
		if (userPropFile.canRead()) {
			try {
				properties.load(new FileReader(userPropFile));
				FileChooserDialog.setDefaultDirMap(properties);
				/* also re-load the list of recently accessed files */

				for (int i = 0; i < MAX_RECENT; i++) {
					String key = "recentFile." + i;
					String value = properties.getProperty(key);
					if (value != null) {
						recentFileList.add(value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * Language specific properties
		 */
		language = new Properties();
		// Only ENGLISH for now
		// TODO: select language based on user-specific properties
		// File langFile = new File("./resources/language/english.properties");
		// try {
		// language.load(new FileReader(langFile));
		// } catch ( Exception e) {
		// e.printStackTrace();
		// }
		String langBase = "/com/callc/movielab/ratings/client/resources/language/";
		String langRsrc = langBase + "english.properties";
		InputStream in = this.getClass().getResourceAsStream(langRsrc);
		try {
			language.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		frmCommonMetadataRatings = new JFrame();
		frmCommonMetadataRatings.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				shutDown();
			}
		});
		frmCommonMetadataRatings
				.setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage(
								"E:\\Eclipse\\Workspace\\MovieLab Server\\WebContent\\images\\logo_movielabs.jpg"));
		frmCommonMetadataRatings.setTitle("Common Metadata Ratings Editor");
		frmCommonMetadataRatings.setBounds(100, 100, 1000, 700);
		frmCommonMetadataRatings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCommonMetadataRatings.getContentPane().add(getTopPanel(),
				BorderLayout.NORTH);
		frmCommonMetadataRatings.getContentPane().add(getMainPanel(),
				BorderLayout.CENTER);
		glass = new BlockingGlassPane();
		frmCommonMetadataRatings.setGlassPane(glass);
		// now update the UI
		headerPanel.getMenuBar().setFileRecentMenuItems(recentFileList);
	}

	/**
	 * @return
	 */
	public JFrame getAppFrame() {
		return frmCommonMetadataRatings;
	}

	/**
	 * @return
	 */
	private Component getTopPanel() {
		if (headerPanel == null) {
			headerPanel = new HeaderPanel();
		}
		return headerPanel;
	}

	/**
	 * @return
	 */
	private JSplitPane getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new PSplitPane();
			mainPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mainPanel.setLeftComponent(getPlaceHolderPanel());
			mainPanel.setRightComponent(getPlaceHolderPanel());
		}
		return mainPanel;
	}

	/**
	 * @return
	 */
	private JPanel getPlaceHolderPanel() {
		JPanel blank = new JPanel();
		blank.setBackground(GuiSettings.backgroundHdrPanel);
		return blank;
	}

	/**
	 * 
	 */
	public void setEditingPanel() {
		getMainPanel();
		int curLoc = mainPanel.getDividerLocation();
		TreeNode tNode = navTree.getSelectedNode();
		if (tNode instanceof RSpecLeaf) {
			((RSpecLeaf) tNode).resyncUi();
			Component ew = navTree.getSelectedWidget();
			mainPanel.setRightComponent(ew);
			mainPanel.setDividerLocation(curLoc);
		}
	}

	/**
	 * Create a new RatingSystem specification from scratch.
	 */
	public void createNewRSys() {
		ratingSystem = new RatingSystem();
		navTree = new RSpecNavPanel(ratingSystem);
		getMainPanel().setLeftComponent(navTree);
		setEditingPanel();
		headerPanel.getMenuBar().fileLoaded(true);
		setCurrentFile(null);
		newFile = true;
	}

	/**
	 * @param path2xml
	 */
	public void open(String path2xml) {
		if (path2xml != null) {
			newFile = false;
			File xmlFile = new File(path2xml);
			if (xmlFile.canRead()) {
				task = new XmlFileTask(xmlFile, ratingSystem, XF_TASKTYPE.READ,
						true);
				task.execute();
			}
		}
	}

	/**
	 * @param path2xml
	 */
	public void save() {
		if (currentFile != null) {
			writeAsXml(currentFile, ratingSystem);
		} else {
			saveAs(null);
		}
	}

	/**
	 * @param path2xml
	 */
	public void saveAs(String path2xml) {
		if (path2xml == null) {
			String dirPath = (String) FileChooserDialog.getDefaultDirMap().get(
					"fileChooser.xml");
			String fileName = ratingSystem.getCountry(0) + "_"
					+ ratingSystem.getName() + "_Ratings.xml";
			path2xml = dirPath + "/" + fileName;
			FileFilter filter = new FileNameExtensionFilter(
					"Ratings Spec file", "xml");
			path2xml = FileChooserDialog.getFilePath("Save as", path2xml,
					filter, "xml", RatingsEditor.getEditor().getAppFrame());
			if (path2xml == null) {
				// user cancelled
				return;
			}
		}
		File xmlFile = new File(path2xml);
		if (xmlFile.canWrite()) {
			int n = JOptionPane.showConfirmDialog(frmCommonMetadataRatings,
					"Overwrite and replace existing specification?",
					"Replace Confirmation", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION) {
				return;
			}
		} else {
			// new file
			try {
				xmlFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		/*
		 * Now we can actually persist the file
		 */
		writeAsXml(xmlFile, ratingSystem);

	}

	protected void writeAsXml(File xmlFile, RatingSystem ratingSys) {
		task = new XmlFileTask(xmlFile, ratingSystem, XF_TASKTYPE.WRITE, true);
		task.execute();
	}

	/**
	 * 
	 */
	public void validate() {
		task = new XmlFileTask(null, ratingSystem, XF_TASKTYPE.VALIDATE, true);
		task.execute();
	}

	/**
	 * @param path2html
	 * 
	 */
	public void genHtml(String htmlDirPath) {
		String xmlDirPath = (String) FileChooserDialog.getDefaultDirMap().get(
				"fileChooser.xml");
		/* IFF path is null then ask user */
		if (xmlDirPath == null || xmlDirPath.isEmpty()) {
			xmlDirPath = FileChooserDialog.getDirPath(
					"Directory with XML-formated Rating Specs", null, null,
					"xml", getAppFrame());
			if (xmlDirPath == null || xmlDirPath.isEmpty()) {
				// Cancelled by user
				return;
			} else {
				// save for next access
				Map dpm = FileChooserDialog.getDefaultDirMap();
				dpm.put("fileChooser.xml", xmlDirPath);
			}
		}
		File parent = new File(htmlDirPath);
		String version = parent.getName();
		final JOptionPane optionPane = new JOptionPane(
				"The data set will be tagged as Version '" + version + "'.\n"
						+ "Do you wish to continue?",
				JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optionPane
				.createDialog(headerPanel, "Confirm Version");
		dialog.setVisible(true);
		int value = ((Integer)optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION) {
			// ................................................
			BatchXSLT htmlGenerator = new BatchXSLT(xmlDirPath, htmlDirPath,
					"./resources");
			boolean status = htmlGenerator.process();
		}
	}

	public Element getClipboardXml() {
		return clipboardXml;
	}

	public void setClipboardXml(Element xml) {
		clipboardXml = xml;
	}

	/**
	 * @param ratingSystem
	 */
	public void hasChanged(SpecificationElement node) {
		if (navTree != null) {
			navTree.hasChanged(node);
		}
	}

	/**
	 * @param xmlFile
	 */
	public void setCurrentFile(File xmlFile) {
		currentFile = xmlFile;
		if (xmlFile != null) {
			/* keep track of recently accessed files */
			String filePath;
			try {
				filePath = xmlFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			if (recentFileList.isEmpty()) {
				recentFileList.add(filePath);
			} else {
				// if already there, remove it
				recentFileList.remove(filePath);
				// set as most recent
				recentFileList.add(0, filePath);
				// trim to max# remembered
				int last = recentFileList.size();
				if (last > MAX_RECENT) {
					for (int j = last - 1; j >= MAX_RECENT; j--) {
						recentFileList.remove(last - 1);
					}
				}
			}
			// now update the UI
			headerPanel.getMenuBar().setFileRecentMenuItems(recentFileList);
		}
	}

	/**
	 * Save properties and open files, then shut down and exit.
	 */
	public void shutDown() {
		// save what user hass been accessing
		for (int i = 0; i < recentFileList.size(); i++) {
			/* remember the list contains the full path */
			String path2xml = recentFileList.get(i);
			properties.setProperty("recentFile." + i, path2xml);
		}
		try {
			properties.store(new FileWriter(userPropFile),
					"Movie Labs Ratings Editor");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * @return the language
	 */
	public Properties getLanguage() {
		return language;
	}

	/**
	 * @param b
	 */
	void showWorkInProgress(boolean modal) {
		frmCommonMetadataRatings.setCursor(Cursor
				.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressDialog = new ProgressDisplay(
				"Rating System Load & Validation in progress...");
		progressDialog.setLocationRelativeTo(frmCommonMetadataRatings);
		progressDialog.setAlwaysOnTop(modal);
		progressDialog.setVisible(true);
		glass.setVisible(modal);
	}

	/**
	 * @param b
	 */
	void endWorkInProgress() {
		frmCommonMetadataRatings.setCursor(Cursor
				.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		progressDialog.setVisible(false);
		progressDialog.dispose();
		headerPanel.getMenuBar().setEnabled(true);
		Toolkit.getDefaultToolkit().beep();
		glass.setVisible(false);
	}

	/**
	 * Component used to block all user interactions while work is in progress.
	 * 
	 * @author L. J. Levin, created Aug 26, 2013
	 * 
	 */
	class BlockingGlassPane extends JPanel {

		public BlockingGlassPane() {

			setOpaque(false);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
		}

	}

	/**
	 * SwingWorker used to perform tasks relating to reading, writing, or
	 * validating of Ratings Specification. Tasks take place on background
	 * thread. All user interactions may or may not be blocked depending on the
	 * flags.
	 * 
	 * @author L. J. Levin, created Aug 28, 2013
	 * 
	 */
	class XmlFileTask extends SwingWorker<Void, Void> {
		private File xmlFile;
		private XF_TASKTYPE tasking;
		private boolean modal;
		private RatingSystem taskRatingSys;

		/**
		 * @param xmlFile
		 *            file to read or write
		 * @param rSys
		 *            ignored for READ
		 * 
		 * @param tasking
		 * @param modal
		 */
		public XmlFileTask(File xmlFile, RatingSystem rSys,
				XF_TASKTYPE tasking, boolean modal) {
			this.xmlFile = xmlFile;
			this.taskRatingSys = rSys;
			this.tasking = tasking;
			this.modal = modal;
		}

		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			switch (tasking) {
			case READ:
				return executeRead();
			case WRITE:
				return executeWrite();
			case VALIDATE:
				return executeValidate();
			}
			return null;
		}

		/**
		 * @return
		 */
		private Void executeValidate() {
			try {
				showWorkInProgress(modal);
				File tempFile = getTempFile();
				if (tempFile == null) {
					return null;
				}
				Element rootEl = taskRatingSys.asXml();
				if (rootEl != null) {
					Document xmlDoc = new Document(rootEl);
					Format myFormat = Format.getRawFormat();
					XMLOutputter outputter = new XMLOutputter(myFormat);
					FileWriter fw = new FileWriter(tempFile);
					outputter.output(xmlDoc, fw);
					fw.close();
					// now read back in validate mode
					RatingSystem rSys = RatingSystem
							.loadFromXml(tempFile, true);
					tempFile.delete();
					taskRatingSys.setLastValidated(TimeStamp.asDate());
					String msgID = "Dialog-msg-v1";
					String msg = language.getProperty(msgID,
							"Increment version?");
					String[] options = { "Yes", "No" };
					int choice = JOptionPane.showOptionDialog(headerPanel, msg,
							"Validated", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					if (choice == 0) {
						taskRatingSys.incrementVersion();
					}
				}
			} catch (Exception e) {
				if (e instanceof JDOMParseException) {
					taskRatingSys.setLastValidated(null);
					String details = e.getCause().getMessage();
					JOptionPane.showMessageDialog(headerPanel, details,
							"XML Parse error", JOptionPane.ERROR_MESSAGE);
				} else {
					// TODO: pop-up dialog and status-bar update
					e.printStackTrace();
				}
			} finally {
				// re-enable user interactions
				endWorkInProgress();
			}
			return null;
		}

		/**
		 * @return
		 */
		private File getTempFile() {
			try {
				File tempDir = new File("./resources/temp");
				tempDir.mkdirs();
				File tFile = File.createTempFile("validate_", ".xml", tempDir);
				return tFile;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * @return
		 */
		private Void executeWrite() {
			Date previousSave = taskRatingSys.getLastSaved();
			try {
				showWorkInProgress(modal);
				taskRatingSys.setLastSaved(TimeStamp.asDate());
				Element rootEl = taskRatingSys.asXml();
				if (rootEl != null) {
					Document xmlDoc = new Document(rootEl);
					Format myFormat = Format.getPrettyFormat();
					XMLOutputter outputter = new XMLOutputter(myFormat);
					OutputStreamWriter osw = new OutputStreamWriter(
							new FileOutputStream(xmlFile), "UTF-8");
					outputter.output(xmlDoc, osw);
					osw.close();
					setCurrentFile(xmlFile);
				}
			} catch (Exception e) {
				// restore previous save date
				taskRatingSys.setLastSaved(previousSave);
				// TODO: pop-up dialog and status-bar update
				e.printStackTrace();
			} finally {
				// re-enable user interactions
				endWorkInProgress();
			}
			return null;
		}

		/**
		 * @return
		 */
		private Void executeRead() {
			try {
				showWorkInProgress(modal);
				RatingSystem rSys = RatingSystem.loadFromXml(xmlFile, false);
				// begin cut-over
				ratingSystem = null;
				navTree = new RSpecNavPanel(rSys);
				getMainPanel().setLeftComponent(navTree);
				setEditingPanel();
				headerPanel.getMenuBar().fileLoaded(true);
				// finalize cut-over
				ratingSystem = rSys;
				setCurrentFile(xmlFile);
			} catch (Exception e) {
				headerPanel.getMenuBar().fileLoaded(false);
				// TODO: pop-up dialog and status-bar update
				e.printStackTrace();
			} finally {
				// re-enable user interactions
				endWorkInProgress();
			}
			return null;
		}

		/*
		 * Executed in event dispatch thread
		 */
		public void done() {
			System.out.println("XmlTask DONE");
		}
	}

}
