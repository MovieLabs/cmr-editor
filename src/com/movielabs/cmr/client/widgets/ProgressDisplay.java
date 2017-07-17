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

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

import com.movielabs.cmr.client.resources.GuiSettings;

/**
 * 
 * @author L. J. Levin, created Aug 26, 2013
 * 
 */
public class ProgressDisplay extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ProgressDisplay dialog = new ProgressDisplay("FooBar in progress...");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void activate(){
		SwingUtilities.invokeLater(new Runnable() {
        public void run() {
    		setVisible(true);
        }
    });
		
	}

	/**
	 * Create the dialog.
	 */
	public ProgressDisplay(String title) {
		this.setBounds(100, 100, 325, 75);
		this.setTitle(title);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(GuiSettings.backgroundHdrPanel); 
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			contentPanel.add(progressBar);
		}
	}

}
