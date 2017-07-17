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
package com.movielabs.cmr.client.widgets.rspec;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import com.movielabs.cmr.client.RatingsEditor;
import com.movielabs.cmr.client.resources.GuiSettings;
import com.movielabs.cmr.client.rspec.Organization;
import com.movielabs.cmr.client.rspec.Organization.ORGTYPE;

/**
 * 
 * @author L. J. Levin, created Aug 28, 2013
 * 
 */
public class RatingsOrgPanel extends JPanel {

	private JLabel panelLabel;
	private RatingsEditor editor;
	private JPanel mainPanel;
	private HashMap<String, JTextComponent> knownFields = new HashMap<String, JTextComponent>();
	private String[] currentText = new String[5];
	private JTextComponent[] textField = new JTextComponent[5];
	private Organization dao;
	private JComboBox orgTypeCBox;

	/**
	 * @param organization
	 */
	public RatingsOrgPanel(Organization dao) {
		setBackground(GuiSettings.backgroundPanel);
		this.dao = dao;
		editor = RatingsEditor.getEditor();
		setLayout(new BorderLayout(0, 10));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(getPanelLabel(), BorderLayout.NORTH);
		add(getMainPanel(), BorderLayout.CENTER);
	}

	/**
	 * @return
	 */
	private Component getPanelLabel() {
		if (panelLabel == null) {
			panelLabel = new JLabel(editor.getLanguage().getProperty(
					"RatingOrgPanel.panel-label", "Org"));
			panelLabel.setBackground(GuiSettings.backgroundPanelLabel);
			panelLabel.setOpaque(true);
			panelLabel.setFont(new Font("Arial", panelLabel.getFont()
					.getStyle(), panelLabel.getFont().getSize() + 2));
			panelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return panelLabel;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setBackground(GuiSettings.backgroundPanel);
			GridBagLayout gbl_mainPanel = new GridBagLayout();
			gbl_mainPanel.columnWidths = new int[] { 0, 0, 0, 0 };
			gbl_mainPanel.rowHeights = new int[] { 0, 0 };
			gbl_mainPanel.columnWeights = new double[] { 0.0, 0.0, 1.0,
					Double.MIN_VALUE };
			gbl_mainPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			mainPanel.setLayout(gbl_mainPanel);
			/*
			 * rows for data entry....
			 */
			
			// Display Name
			addRow(0, 1);
			currentText[0] = "";
			
			// Organization ID
			addRow(1, 1);
			currentText[1] = "";
			
			// OrgType
//			addRow(2, 1);
//			currentText[2] = "";
			addOrgType(2);
			
			// ContactInfo
			addRow(3, 3);
			currentText[3] = "";
			
			// URL or org's web site
			addRow(4, 1);
			currentText[4] = "";
			// add filler panel
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridwidth = GridBagConstraints.REMAINDER;
			gbc_panel_2.gridx = 0;
			gbc_panel_2.gridy = 5;
			gbc_panel_2.weighty = 1.0;
			JPanel filler = new JPanel();
			filler.setBackground(GuiSettings.backgroundPanel);
			mainPanel.add(filler, gbc_panel_2);
		}
		return mainPanel;
	}

	/**
	 * @param i
	 */
	private void addOrgType(int row) {
		int col = 0;
		// 1st the label...
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = col;
		gbc_label.gridy = row;
		JLabel fieldLabel = buildLabel(row); 
		mainPanel.add(fieldLabel, gbc_label);
		// now the combo box...
		GridBagConstraints gbc_field = new GridBagConstraints();
		gbc_field.weightx = 1.0;
		gbc_field.insets = new Insets(0, 0, 5, 0);
		gbc_field.gridx = 2 + col;
		gbc_field.gridy = row;
		gbc_field.anchor = GridBagConstraints.LINE_START;
		String[] fieldOpt = new String[Organization.orgTypeList.size()];
		for(int i=0; i < fieldOpt.length;i++){
			fieldOpt[i] = Organization.orgTypeList.get(i).toString();
		} 
		orgTypeCBox = new JComboBox(fieldOpt);
		orgTypeCBox.setSelectedItem(dao.getOrgType().toString());
		mainPanel.add(orgTypeCBox, gbc_field);
	}

	/**
	 * @param row
	 * @return
	 */
	private JLabel buildLabel(int fieldId) {
		String labelID = "row-label-" + fieldId;
		String labelText = editor.getLanguage().getProperty(
				"RatingOrgPanel." + labelID, labelID);
		return new JLabel(labelText);
	}

	private void addRow(int row, int rowHeight) {
		String labelID = "row-label-" + row;
		String labelText = editor.getLanguage().getProperty(
				"RatingOrgPanel." + labelID, labelID);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = row;
		JLabel rowLabel = new JLabel(labelText);
		mainPanel.add(rowLabel, gbc_label);

		GridBagConstraints gbc_field = new GridBagConstraints();
		gbc_field.weightx = 1.0;
		gbc_field.insets = new Insets(0, 0, 5, 0);
		gbc_field.fill = GridBagConstraints.HORIZONTAL;
		gbc_field.gridx = 2;
		gbc_field.gridy = row;
		JTextComponent textField = getTxtFieldSystemID(labelID, row, rowHeight);
		mainPanel.add(textField, gbc_field);
		String tipID = "row-tip-" + row;
		String tipText = editor.getLanguage().getProperty(
				"RatingOrgPanel." + tipID, null);
		if (tipText != null) {
			rowLabel.setToolTipText(tipText);
			textField.setToolTipText(tipText);
		}
	}

	private JTextComponent getTxtFieldSystemID(String labelID, int row,
			int rowHeight) {
		JTextComponent tField = knownFields.get(labelID);
		if (tField == null) {
			if (rowHeight > 1) {
				tField = new JTextArea(rowHeight, 80);
				tField.setBorder(new LineBorder(new Color(100, 100, 230), 1));
			} else {
				tField = new JTextField();
			}
			knownFields.put(labelID, tField);
			textField[row] = tField;
			switch (row) {
			case 0:
				tField.setText(dao.getDisplayName());
				break;
			case 1:
				tField.setText(dao.getOrganizationID());
				break;
			case 2: 
				orgTypeCBox.setSelectedItem(dao.getOrgType().toString());
				return null;
			case 3:
				tField.setText(dao.getContactInfo());
				break;
			case 4:
				tField.setText(dao.getUrl());
				break;
			}
			/*
			 * add listeners to detect changes in text field. Any typing is
			 * considered 'completed' when either the user presses 'Enter' or
			 * moves the mouse out of the text field.
			 */
			final int evtSrcId = row;
			tField.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent evt) {
					updateText(evtSrcId, evt);
				}

			});
			tField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent evt) {
					evt.getKeyCode();
					if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
						updateText(evtSrcId, evt);
					}
				}
			});
		}
		return tField;
	}

	/**
	 * @param evtSrcId
	 */
	protected void updateText(int evtSrcId, InputEvent evt) {
		JTextComponent tField = (JTextComponent) evt.getSource();
		String newest = tField.getText();
		if (currentText[evtSrcId].equals(newest)) {
			return;
		}
		currentText[evtSrcId] = newest;
		updateDao(evtSrcId);
	}

	public void updateDao() {
		for (int i = 0; i < textField.length; i++) {
			updateDao(i);
		}
	}

	/**
	 * @param evtSrcId
	 */
	private void updateDao(int evtSrcId) {
		switch (evtSrcId) {
		case 0:
			dao.setDisplayName(textField[evtSrcId].getText());
			break;
		case 1:
			dao.setOrganizationID(textField[evtSrcId].getText());
			break;
		case 2:
			String orgTypeName = (String) orgTypeCBox.getSelectedItem();
			ORGTYPE selectedOT = Enum.valueOf(ORGTYPE.class, orgTypeName);
			dao.setOrgType(selectedOT);
			break;
		case 3:
			dao.setContactInfo(textField[evtSrcId].getText());
			break;
		case 4:
			dao.setUrl(textField[evtSrcId].getText());
			break;
		}
	}
}
