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

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.movielabs.cmr.client.resources.GuiSettings;

import java.awt.SystemColor;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class HeaderPanel extends JPanel {
	public static final String imgLoc = "/com/callc/movielab/ratings/client/images/";
	private EditorMenuBar menuBar;
	private JLabel logoLabel;
	private JLabel bannerLabel;
	private JPanel menuPanel;

	/**
	 * Create the panel.
	 */
	public HeaderPanel() { 
		setBackground(GuiSettings.backgroundHdrPanel);
		setLayout(new BorderLayout(0, 0));
		add(gelLogoLabel(), BorderLayout.WEST);
		add(gelBannerLabel(), BorderLayout.CENTER);
		add(getMenuPanel(), BorderLayout.SOUTH); 

	}

	/**
	 * @return
	 */
	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			menuPanel = new JPanel(); 
			menuPanel.setBorder(new LineBorder(SystemColor.activeCaption, 2));
			menuPanel.setLayout(new BorderLayout(0, 0));
			menuPanel.add(getMenuBar(), BorderLayout.WEST);
		}
		return menuPanel;
	}

	private JLabel gelLogoLabel() {
		if (logoLabel == null) {
			logoLabel = new JLabel(); 
			Icon ico = new ImageIcon(this.getClass().getResource(imgLoc+"logo_movielabs.jpg"));
			logoLabel.setIcon(ico);
		}
		return logoLabel;
	}

	private JLabel gelBannerLabel() {
		if (bannerLabel == null) {
			bannerLabel = new JLabel(); 
			Icon ico = new ImageIcon(this.getClass().getResource(imgLoc+"home_banner.jpg"));
//			Icon ico2 = new ImageIcon(img);
			bannerLabel.setIcon(ico);
		}
		return bannerLabel;
	}

	public EditorMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new EditorMenuBar();
		}
		return menuBar;
	}
}
