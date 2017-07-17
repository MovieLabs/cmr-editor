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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * Creates a pop-up JDialog with a standardized corporate look-n-feel. This
 * class is typically extended by an application to content specific to that
 * app.
 * <p>
 * The JDialog will have:
 * <ul>
 * <li>a header panel with the application's name and logo,</li>
 * <li>a footer panel with the Critical Architectures corporate logo, and</li>
 * <li>a tab panel in the middle with distinct tabs for presenting information
 * about various aspects of the app (e.g., copyright, build/version, licensing,
 * etc)</li>
 * </ul>
 * </p>
 * 
 * @author L. J. Levin
 */
public class AboutDialog extends JDialog {
	private JPanel jContentPane = null;
	private JPanel buttonPanel;
	private JTabbedPane tabbedPane;
	private HashMap tabPanes = new HashMap();
	private HashMap tabEntries = new HashMap();
	private static String callcLogoPath = "/com/critarch/util/ui/Logo_smallest.JPG";

	/**
	 * Create dialog using the default Critical Architectures logo
	 * 
	 * @param title
	 * @param subtitle
	 */
	public AboutDialog(String title, String subtitle) {
		this(title, subtitle, callcLogoPath);
	}

	/**
	 * Create dialog using the provided logo
	 * 
	 * @param title
	 * @param subtitle
	 * @param appLogoPath
	 */
	public AboutDialog(String title, String subtitle, String appLogoPath) {
		super();
		setTitle(title);
		// get the icon with the logo for the application
		ImageIcon appLogo = new ImageIcon(getClass().getResource(appLogoPath));
		initialize();
		// Add the header panel
		if (subtitle != null || appLogo != null) {
			DialogHeaderPanel header = new DialogHeaderPanel(appLogo, title,
					subtitle);
			getContentPane().add(header, BorderLayout.NORTH);
		}
		// Adds the button panel
		buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(16, 8, 8, 8)));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		// Adds OK button to close window
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton, BorderLayout.EAST);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		// add the icon with the corporate logo
		ImageIcon callcLogo = new ImageIcon(getClass().getResource(
				callcLogoPath));
		JLabel logoLabel = new JLabel(callcLogo);
		buttonPanel.add(logoLabel, BorderLayout.WEST);
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 480);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
			tabbedPane = new JTabbedPane();
			jContentPane.add(tabbedPane, BorderLayout.CENTER);
		}
		return jContentPane;
	}

	public void addTab(String name) {
		String initText = "<html>t.b.d. ::" + name + "</html>";
		JLabel tabContents = new JLabel(initText);
		tabContents
				.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		tabContents.setVerticalAlignment(SwingConstants.TOP);
		tabbedPane.add(name, tabContents);
		tabPanes.put(name, tabContents);
		tabEntries.put(name, new ArrayList());
	}

	/**
	 * @param name
	 * @param string
	 * @param string2
	 */
	protected void addVersionEntry(String name, String component,
			String rsrcPrefix) {
		ResourceBundle configRsrcs = PropertyResourceBundle
				.getBundle(rsrcPrefix + ".build");
		if (configRsrcs != null) {
			String ver = configRsrcs.getString("version");
			String date = configRsrcs.getString("buildDate");
			addEntry("Build/Version", "   <b>" + component + "</b>:   " + ver
					+ ", " + date);
		}

	}

	public void addEntry(String name, String text) {
		ArrayList entries = (ArrayList) tabEntries.get(name);
		entries.add(text);
		// now reset contents shown in the pane
		String htmlText = "<html><ul>";
		if (!entries.isEmpty()) {
			for (int i = 0; i < entries.size(); i++) {
				String next = (String) entries.get(i);
				htmlText = htmlText + "<li>" + next + "<br></li>";
			}
		}
		htmlText = htmlText + "</ul></html>";
		setTabText(name, htmlText);
	}

	protected void setTabText(String name, String htmlText) {
		JLabel tabContents = (JLabel) tabPanes.get(name);
		tabContents.setText(htmlText);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
