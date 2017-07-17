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
package com.callc.movielab.ratings.client.widgets;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.resources.GuiSettings;
import com.callc.movielab.ratings.client.util.LanguageCode;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;

/**
 * 
 * @author L. J. Levin, created Sep 2, 2013
 * 
 */
public class LanguageSelectDialog extends JDialog {

	protected static String[] languages = LanguageCode.getInDisplayOrder();
	private JPanel mainPanel;
	private JPanel btnPanel;
	private JButton btnOk;
	private JButton btnCancel;
	protected String langPrefix = "LanguageSelectDialog.";
	private Properties uiLang;
	private JComboBox langComboBox;
	private boolean confirmed;

	public LanguageSelectDialog() {
		RatingsEditor editor = RatingsEditor.getEditor();
		uiLang = editor.getLanguage();
		String title = uiLang.getProperty(langPrefix + "title", langPrefix);
		setTitle(title);
		setModal(true);
		setAlwaysOnTop(true);
		getContentPane().add(getMainPanel(), BorderLayout.CENTER);
		getContentPane().add(getBtnPanel(), BorderLayout.SOUTH);
		pack();
	}
	
	public void setVisible(boolean show){
		if(show){
			confirmed = false;
		}
		super.setVisible(show);
	}

	public String getSelectedLangCode() {
		String language = (String) langComboBox.getSelectedItem();
		if (language != null) {
			return LanguageCode.getCodeForName(language);
		} else {
			return null;
		}
	}

	/**
	 * 
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setBackground(GuiSettings.backgroundPanel);
			langComboBox = new JComboBox(languages);
			mainPanel.add(langComboBox);
		}
		return mainPanel;
	}

	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.add(getBtnOk());
			btnPanel.add(getBtnCancel());
		}
		return btnPanel;
	}

	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton(uiLang.getProperty("Dialog.btn-confirm",
					"Confirm"));
			btnOk.addActionListener(new ActionListener(){
 				public void actionPerformed(ActionEvent arg0) {
					confirmed = true;
					setVisible(false);
				} 
			});
		}
		return btnOk;
	}

	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton(uiLang.getProperty("Dialog.btn-cancel",
					"Cancel"));
			btnCancel.addActionListener(new ActionListener(){
 				public void actionPerformed(ActionEvent arg0) {
					confirmed = false;
					setVisible(false);
				} 
			});
		}
		return btnCancel;
	}

	/**
	 * @return the confirmed
	 */
	public boolean isConfirmed() {
		return confirmed;
	}
}
