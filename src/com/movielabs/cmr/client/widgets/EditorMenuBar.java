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
package com.movielabs.cmr.client.widgets;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.movielabs.cmr.client.RatingsEditor;
import com.movielabs.cmr.client.util.ui.FileChooserDialog;
import com.movielabs.cmr.client.util.ui.JHelpMgr;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class EditorMenuBar extends JMenuBar {

	private JMenu fileMenu;
	private JMenuItem newFileMI;
	private JMenuItem openFileMI;
	private JMenuItem loadFileMI;
	private JMenuItem saveFileMI;
	private JMenuItem saveAsFileMI;
	private JMenuItem exitMI;
	private JMenu fileRecentMenu;
	private JMenu helpMenu;
	private JMenuItem aboutMI;
	private JMenuItem helpDocMI;
	private RatingsEditor editor;
	// protected String currentFilePath;
	private JMenuItem validateMI;
	private JMenu toolMenu;
	private JMenuItem genHtmlMI;
	private JMenuItem uploadMI;
	private JMenu htmlMenu;
	private JMenuItem showHtmlIdxMI;
	private JMenuItem showHtmlSummaryMI;
	private JMenuItem genUriMI;

	public EditorMenuBar() {
		this.editor = editor;
		initialize();
	}

	/**
	 * 
	 */
	protected void initialize() {
		add(getFileMenu());
		add(getToolMenu());
		add(getHelpMenu());
	}

	public JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
			fileMenu.add(getFileNewMenuItem());
			fileMenu.add(getFileOpenMenuItem());
			fileMenu.add(getFileLoadMenuItem());
			fileMenu.add(getFileSaveMenuItem());
			fileMenu.add(getFileSaveAsMenuItem());
			fileMenu.add(new JSeparator());
			fileMenu.add(getFileRecentMenu());
			fileMenu.add(new JSeparator());
			fileMenu.add(getExitAsMenuItem());
		}
		return fileMenu;
	}

	public JMenuItem getFileNewMenuItem() {
		if (newFileMI == null) {
			newFileMI = new JMenuItem("New");
			newFileMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RatingsEditor.getEditor().createNewRSys();
					// currentFilePath = null;
				}
			});
		}
		return newFileMI;
	}

	public JMenuItem getFileOpenMenuItem() {
		if (openFileMI == null) {
			openFileMI = new JMenuItem("Open");
			openFileMI.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					FileFilter filter = new FileNameExtensionFilter("Ratings Spec file", "xml");
					String path2xml = FileChooserDialog.getFilePath("Open Ratings Spec for Edits", null, filter, "xml",
							RatingsEditor.getEditor().getAppFrame());
					if (path2xml != null) {
						// currentFilePath = path2xml;
						RatingsEditor.getEditor().open(path2xml);
					}
				}
			});
		}
		return openFileMI;
	}

	/**
	 * Download file from remote server
	 * 
	 * @return
	 */
	public JMenuItem getFileLoadMenuItem() {
		if (loadFileMI == null) {
			loadFileMI = new JMenuItem("Load");
			loadFileMI.setToolTipText("Download file from remote server");
			loadFileMI.setEnabled(false);
			loadFileMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
		}
		return loadFileMI;
	}

	public JMenuItem getFileSaveMenuItem() {
		if (saveFileMI == null) {
			saveFileMI = new JMenuItem("Save");
			saveFileMI.setEnabled(false);
			saveFileMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RatingsEditor.getEditor().save();
				}
			});
		}
		return saveFileMI;
	}

	public JMenuItem getFileSaveAsMenuItem() {
		if (saveAsFileMI == null) {
			saveAsFileMI = new JMenuItem("Save As");
			saveAsFileMI.setEnabled(false);
			saveAsFileMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					doSaveAs();
				}
			});
		}
		return saveAsFileMI;
	}

	/**
	 * 
	 */
	protected void doSaveAs() {
		RatingsEditor.getEditor().saveAs(null);
	}

	/**
	 * @return
	 */
	public JMenu getFileRecentMenu() {
		if (fileRecentMenu == null) {
			fileRecentMenu = new JMenu("Recent..");
		}
		return fileRecentMenu;
	}

	/**
	 * Resets the Menu Items specifying recently accessed files.
	 * 
	 * @param recentFileList
	 */
	public void setFileRecentMenuItems(List<String> recentFileList) {
		// make sure the Menu has been created
		getFileRecentMenu();
		fileRecentMenu.removeAll();
		for (int i = 0; i < recentFileList.size(); i++) {
			/* remember the list contains the full path */
			final String path2xml = recentFileList.get(i);
			File temp = new File(path2xml);
			String name = temp.getName();
			JMenuItem nextMI = new JMenuItem(name);
			nextMI.setToolTipText(recentFileList.get(i));
			fileRecentMenu.add(nextMI);
			nextMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RatingsEditor.getEditor().open(path2xml);
				}
			});
		}
	}

	public JMenuItem getExitAsMenuItem() {
		if (exitMI == null) {
			exitMI = new JMenuItem("Exit");
			exitMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RatingsEditor.getEditor().shutDown();
				}
			});
		}
		return exitMI;
	}

	private JMenu getToolMenu() {
		if (toolMenu == null) {
			toolMenu = new JMenu("Tools");
			// toolMenu.add(getGenUriMenuItem());
			toolMenu.add(getValidateMenuItem());
			toolMenu.add(getHtmlMenu());
		}
		return toolMenu;
	}

	public JMenuItem getValidateMenuItem() {
		if (validateMI == null) {
			validateMI = new JMenuItem("Validate");
			validateMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RatingsEditor.getEditor().validate();
				}
			});
		}
		return validateMI;
	}

	private JMenu getHtmlMenu() {
		if (htmlMenu == null) {
			htmlMenu = new JMenu("HTML");
			htmlMenu.add(getGenHtmlMI());
			htmlMenu.add(getShowHtmlPageMI());
			htmlMenu.add(getUploadMI());
		}
		return htmlMenu;
	}

	private JMenuItem getGenHtmlMI() {
		if (genHtmlMI == null) {
			genHtmlMI = new JMenuItem("Generate HTML");
			genHtmlMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					doHtmlGen();
				}
			});
		}
		return genHtmlMI;
	}

	/**
	 * 
	 */
	protected void doHtmlGen() {
		String path2html = FileChooserDialog.getDirPath("Root Directory", null, null, "html",
				RatingsEditor.getEditor().getAppFrame());
		if (path2html != null) {
			RatingsEditor.getEditor().genHtml(path2html);
		}

	}

	private JMenuItem getShowHtmlPageMI() {
		if (showHtmlIdxMI == null) {
			showHtmlIdxMI = new JMenuItem("Show in Browser");
			showHtmlIdxMI.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doShowInBrowser();
				}
			});
		}
		return showHtmlIdxMI;
	}

	/**
	 * @param relative
	 * 
	 */
	protected void doShowInBrowser() {
		String path2html = FileChooserDialog.getFilePath("HTML Directory", null, null, "html",
				RatingsEditor.getEditor().getAppFrame());
		if (path2html != null) {
			String completePath = path2html;
			File test = new File(completePath);
			if (test.exists()) {
				String cPath = null;
				try {
					cPath = test.getCanonicalPath();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				String url = "file:///" + cPath;
				url = url.replaceAll("\\\\", "/");
				url = url.replaceAll(" ", "%20");
				URI uri;
				try {
					uri = new URI(url);
					Desktop.getDesktop().browse(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private JMenuItem getUploadMI() {
		if (uploadMI == null) {
			uploadMI = new JMenuItem("Upload HTML");
			uploadMI.setEnabled(false);
		}
		return uploadMI;
	}

	/**
	 * @return
	 */
	public JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu("Help");
			helpMenu.add(getHelpDocMenuItem());
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * @return
	 */
	public JMenuItem getHelpDocMenuItem() {
		if (helpDocMI == null) {
			helpDocMI = new JMenuItem("Help Documentation");
			helpDocMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JHelpMgr.showJavaHelp("CMRatingsEditor", e);
				}
			});
		}
		return helpDocMI;
	}

	/**
	 * @return
	 */
	public JMenuItem getAboutMenuItem() {
		if (aboutMI == null) {
			aboutMI = new JMenuItem("About");
			final Component menuBar = this;
			aboutMI.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AboutEditorDialog dialog = new AboutEditorDialog();
					final Component parent = menuBar.getParent().getParent();
					dialog.setLocation(parent.getLocationOnScreen());
					dialog.setVisible(true);
				}
			});
		}
		return aboutMI;
	}

	/**
	 * Signals if a file is available. This will be used as a signal to enable
	 * or disable various MenuItems
	 * 
	 * @param status
	 */
	public void fileLoaded(boolean status) {
		saveFileMI.setEnabled(status);// currentFilePath!= null &&
										// !currentFilePath.isEmpty());
		saveAsFileMI.setEnabled(status);
	}
}
