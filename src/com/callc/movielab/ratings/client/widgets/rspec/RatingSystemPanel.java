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
package com.callc.movielab.ratings.client.widgets.rspec;

import javax.swing.JPanel;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.resources.GuiSettings;
import com.callc.movielab.ratings.client.rspec.RatingSystem;
import com.callc.movielab.ratings.client.util.CountryCode;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * @author L. J. Levin, created Aug 26, 2013
 * 
 */
public class RatingSystemPanel extends JPanel {

	private static final String propertyPrefix = "RatingSystemPanel.";
	private RatingSystem dao;
	private JLabel panelLabel;
	private JPanel panel;
	private JLabel lblSysID;
	private JLabel lblSysURI;
	private JTextField txtFieldSysID;
	private JTextField txtFieldUri;
	private JLabel lblRegion;
	private JLabel lblSubregion;
	private JLabel lblNotes;
	private List<JComboBox<String>> regionList = new ArrayList<JComboBox<String>>();
	private List<JTextField> subRegionList = new ArrayList<JTextField>();
	private List<String> currentSubRegId = new ArrayList<String>();
	private String currentSysId = "";
	private JPanel orgPanel;
	private JTextField versionField;
	private JComboBox deprecatedComboBox;
	private RatingsEditor editor;
	private JTextArea notesField;

	/**
	 * Create the panel.
	 */
	public RatingSystemPanel(RatingSystem dao) {
		editor = RatingsEditor.getEditor();
		setBackground(GuiSettings.backgroundPanel);
		this.dao = dao;
		setLayout(new BorderLayout(0, 0));
		add(getPanelLabel(), BorderLayout.NORTH);
		add(getPanel(), BorderLayout.CENTER);

	}

	private JLabel getPanelLabel() {
		if (panelLabel == null) {
			String text = editor.getLanguage().getProperty(
					propertyPrefix + "panel-label", "Properties");
			panelLabel = new JLabel(text);
			panelLabel.setFont(new Font("Arial", panelLabel.getFont()
					.getStyle(), panelLabel.getFont().getSize() + 2));
			panelLabel.setHorizontalAlignment(SwingConstants.CENTER);
			panelLabel.setBackground(GuiSettings.backgroundPanelLabel);
			panelLabel.setOpaque(true);

		}
		return panelLabel;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBackground(GuiSettings.backgroundPanel);
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			/*
			 * Defining a 12 column grid
			 */
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 160, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.2, 0.0, 0.0,
					0.2, 0.0, 0.0, 0.2, 0.0, 0.0, 0, 0, 0, 0, Double.MIN_VALUE };
			/*
			 * Defining a 7 rows with last weighted to act as a filler...
			 */
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 1.0 };
			panel.setLayout(gbl_panel);

			// ............ ROW 1 ......................
			/*
			 * System ID....
			 */
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 0;
			panel.add(getLblNewLabel_1(), gbc_lblNewLabel_1);
			GridBagConstraints gbc_txtTbd = new GridBagConstraints();
			gbc_txtTbd.gridwidth = 1;
			gbc_txtTbd.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTbd.anchor = GridBagConstraints.NORTHWEST;
			// gbc_txtTbd.weightx = 0.6;
			gbc_txtTbd.insets = new Insets(0, 0, 5, 5);
			gbc_txtTbd.gridx = 2;
			gbc_txtTbd.gridy = 0;
			panel.add(getTxtFieldSystemID(), gbc_txtTbd);

			// ................
			// VERSION....
			/* ---------------------------
			GridBagConstraints gbc_vLabel = new GridBagConstraints();
			gbc_vLabel.insets = new Insets(0, 20, 0, 5);
			gbc_vLabel.gridx = 4;
			gbc_vLabel.gridy = 0;
			gbc_vLabel.anchor = GridBagConstraints.NORTHEAST;
			// gbc_vLabel.weightx = 0.3;
			JLabel label = setLabel(5);
			panel.add(label, gbc_vLabel);
			GridBagConstraints gbc_vField = new GridBagConstraints();
			gbc_vField.insets = new Insets(0, 10, 5, 0);
			gbc_vField.gridx = 5; // 6;
			gbc_vField.gridy = 0;
			gbc_vField.gridwidth = 1;
			gbc_vField.weightx = 0.3;
			gbc_vField.anchor = GridBagConstraints.NORTHWEST;
			gbc_vField.fill = GridBagConstraints.HORIZONTAL;
			panel.add(getVersionField(), gbc_vField);
	--------------------  */
			// ................
			// DEPRECATED:

			GridBagConstraints gbcDepLabel = new GridBagConstraints();
			gbcDepLabel.insets = new Insets(0, 20, 0, 5);
			gbcDepLabel.gridx = 6;
			gbcDepLabel.gridy = 0;
			gbcDepLabel.anchor = GridBagConstraints.NORTHEAST;
			panel.add(setLabel(6), gbcDepLabel);
			GridBagConstraints gbcDepWidget = new GridBagConstraints();
			gbcDepWidget.insets = new Insets(0, 10, 5, 0);
			gbcDepWidget.gridx = 7;
			gbcDepWidget.gridy = 0;
			gbcDepWidget.gridwidth = 1;
			gbcDepWidget.weightx = 0.0;
			gbcDepWidget.anchor = GridBagConstraints.NORTHWEST;
			panel.add(getDepWidget(), gbcDepWidget);

			// ............ ROW 2 ......................
			// URI
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 0;
			gbc_lblNewLabel_2.gridy = 1;
			panel.add(getLblNewLabel_2(), gbc_lblNewLabel_2);
			GridBagConstraints gbc_uriField = new GridBagConstraints();
			gbc_uriField.gridwidth = 4;
			gbc_uriField.insets = new Insets(0, 0, 5, 5);
			gbc_uriField.fill = GridBagConstraints.HORIZONTAL;
			gbc_uriField.gridx = 2;
			gbc_uriField.gridy = 1;
			panel.add(getTextFieldUri(), gbc_uriField);

			// VERSION....
			GridBagConstraints gbc_vLabel = new GridBagConstraints();
			gbc_vLabel.insets = new Insets(0, 20, 0, 5);
			gbc_vLabel.gridx = 6;
			gbc_vLabel.gridy = 1;
			gbc_vLabel.anchor = GridBagConstraints.NORTHEAST;
			// gbc_vLabel.weightx = 0.3;
			JLabel label = setLabel(5);
			panel.add(label, gbc_vLabel);
			GridBagConstraints gbc_vField = new GridBagConstraints();
			gbc_vField.insets = new Insets(0, 10, 5, 0);
			gbc_vField.gridx = 7; // 6;
			gbc_vField.gridy = 1;
			gbc_vField.gridwidth = 1;
			gbc_vField.weightx = 0.3;
			gbc_vField.anchor = GridBagConstraints.NORTHWEST;
			gbc_vField.fill = GridBagConstraints.HORIZONTAL;
			panel.add(getVersionField(), gbc_vField);
			
			// ............ ROW 3 ......................
			// Region
			GridBagConstraints gbc_lblRegion = new GridBagConstraints();
			gbc_lblRegion.insets = new Insets(0, 0, 5, 5);
			gbc_lblRegion.gridx = 0;
			gbc_lblRegion.gridy = 2;
			panel.add(getLblRegion(), gbc_lblRegion);
			// Region[0]
			int regionCellWidth = 3;
			int rCnt = 0;
			GridBagConstraints gbcRegion01 = new GridBagConstraints();
			gbcRegion01.insets = new Insets(0, 0, 5, 5);
			// gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbcRegion01.gridy = 2;
			gbcRegion01.gridx = 2 + (regionCellWidth * rCnt);
			gbcRegion01.gridwidth = regionCellWidth;
			panel.add(getRegionComboBox(), gbcRegion01);

			// ............ ROW 4 ......................
			// and the sub-region too....
			GridBagConstraints gbc_subRegionField = new GridBagConstraints();
			gbc_subRegionField.insets = new Insets(0, 0, 5, 5);
			gbc_subRegionField.gridy = 3;
			gbc_subRegionField.gridx = 2 + (regionCellWidth * rCnt);
			gbc_subRegionField.gridwidth = regionCellWidth;
			gbc_subRegionField.fill = GridBagConstraints.HORIZONTAL;
			panel.add(getSubRegionField(rCnt), gbc_subRegionField);

			// .............
			// SubRegion LABEL
			GridBagConstraints gbc_lblSubregion = new GridBagConstraints();
			gbc_lblSubregion.insets = new Insets(0, 0, 5, 5);
			gbc_lblSubregion.gridx = 0;
			gbc_lblSubregion.gridy = 3;
			panel.add(getLblSubregion(), gbc_lblSubregion);

			// ............ ROW 5 ......................
			// text area for Notes and Comments:
			// LABEL
			GridBagConstraints gbc_lblNotes = new GridBagConstraints();
			gbc_lblNotes.insets = new Insets(0, 0, 5, 5);
			gbc_lblNotes.gridx = 0;
			gbc_lblNotes.gridy = 4;
			panel.add(getLblNotes(), gbc_lblNotes);
			// text area
			GridBagConstraints gbc_noteTxt = new GridBagConstraints();
			gbc_noteTxt.weightx = 1.0;
			gbc_noteTxt.weighty = 0.5;
			gbc_noteTxt.insets = new Insets(0, 0, 5, 0);
			gbc_noteTxt.fill = GridBagConstraints.BOTH;
			gbc_noteTxt.gridx = 2;
			gbc_noteTxt.gridy = 4;
			int rowHeight = 3;
			gbc_noteTxt.gridheight = rowHeight; 
			panel.add(getNotesField(), gbc_noteTxt); 

			// ............ ROW 6 ......................
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridwidth = GridBagConstraints.REMAINDER;
			gbc_panel_2.gridx = 0;
			gbc_panel_2.gridy = 8;
			panel.add(getOrganizationPanel(), gbc_panel_2);
		}
		return panel;
	}

	private JTextField getTxtFieldSystemID() {
		if (txtFieldSysID == null) {
			txtFieldSysID = new JTextField();
			/*
			 * add listeners to detect changes in text field. Any typing is
			 * considered 'completed' when either the user presses 'Enter' or
			 * moves the mouse out of the text field.
			 */
			txtFieldSysID.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent arg0) {
					updateSysId();
				}

			});
			txtFieldSysID.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					int kCode = arg0.getKeyCode();
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						updateSysId();
					}
				}
			});
			txtFieldSysID
					.setToolTipText("Enter name of rating system (required)");
			currentSysId = dao.getName();
			txtFieldSysID.setText(currentSysId);
			txtFieldSysID.setColumns(10);
		}
		return txtFieldSysID;
	}

	/**
	 * @return
	 */
	public JTextField getVersionField() {
		if (versionField == null) {
			versionField = new JTextField();
			versionField.setText(dao.getVersion());
			// KLUDGE...
			/*
			 * version needs to be a montonicly increasing integer. Since
			 * migration to Eclipse LUNA the Window Builder is fubar so I have
			 * simply made this an editable field. Note original mechanism is
			 * that Valdation will let you increase the version but that is
			 * broken due to schema issues.
			 */
			versionField.setEditable(true);
		}
		return versionField;
	}

	public JTextArea getNotesField() {
		if (notesField == null) {
			notesField = new JTextArea(3, 120);
			notesField.setLineWrap(true);
			notesField.setWrapStyleWord(true);
			notesField.setBorder(new LineBorder(new Color(100, 100, 230), 1)); 
			String[] text = getLocalizedText(6);
			if (text[1] != null) {
				notesField.setToolTipText(text[1]);
			}
			notesField.setText(dao.getNotes());
		}
		return notesField;
	}

	/**
	 * @return
	 */
	private Component getDepWidget() {
		if (deprecatedComboBox == null) {
			deprecatedComboBox = new JComboBox(AbstractSpecElementPanel.boolOpt);
			if (dao.isDeprecated()) {
				deprecatedComboBox.setSelectedItem("TRUE");
			} else {
				deprecatedComboBox.setSelectedItem("FALSE");
			}
		}
		return deprecatedComboBox;
	}

	private void updateSysId() {
		String newest = txtFieldSysID.getText();
		if (currentSysId.equals(newest)) {
			return;
		} else {
			currentSysId = newest;
			dao.setName(currentSysId);
			/*
			 * name changes cause URI to be changed...
			 */
			txtFieldUri.setText(dao.getUri());
		}

	}

	public JTextField getTextFieldUri() {
		if (txtFieldUri == null) {
			txtFieldUri = new JTextField();
			// is user allowed to manually enter this field?
			txtFieldUri.setEditable(!dao.isAutoGenUri());
			txtFieldUri.setText(dao.getUri());
			txtFieldUri.setColumns(10);
		}
		return txtFieldUri;
	}

	private JComboBox getRegionComboBox() {
		JComboBox<String> targetedCB = null;
		try {
			targetedCB = regionList.get(0);
		} catch (Exception e) {
		}
		if (targetedCB == null) {
			targetedCB = new JComboBox(CountryCode.getInDisplayOrder());
			regionList.add(0, targetedCB);
			final int cbIdx = 0;
			targetedCB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JComboBox<String> targetedCB = regionList.get(cbIdx);
					String fullName = (String) targetedCB.getSelectedItem();
					if (fullName != null) {
						String isoCode = CountryCode.getCodeForName(fullName);
						if (isoCode != null) {
							dao.setAdminRegion(isoCode);
							/*
							 * region changes cause URI to be changed...
							 */
							txtFieldUri.setText(dao.getUri());
						}
					}
				}
			});
			String curIsoCode = dao.getAdminRegion();
			if (curIsoCode != null) {
				String country = CountryCode.getNameForCode(curIsoCode);
				targetedCB.setSelectedItem(country);
			}
		}
		return targetedCB;
	}

	public JTextField getSubRegionField(int index) {
		JTextField target = null;
		try {
			target = subRegionList.get(index);
		} catch (Exception e) {
		}
		if (target == null) {
			target = new JTextField();
			subRegionList.add(null);// make sure there is room
			subRegionList.add(index, target);
			currentSubRegId.add(index, "");
			target.setToolTipText("State, province, or canton (OPTIONAL)");
			target.setColumns(10);
			String srName = dao.getSubRegion(index);
			if (srName != null) {
				target.setText(dao.getSubRegion(index));
			}
			/*
			 * add listeners to detect changes in text field. Any typing is
			 * considered 'completed' when either the user presses 'Enter' or
			 * moves the mouse out of the text field.
			 */
			final int cbIdx = index;
			target.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent arg0) {
					updateSubRegion(cbIdx);
				}

			});
			target.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					int kCode = arg0.getKeyCode();
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						updateSubRegion(cbIdx);
					}
				}
			});
		}
		return target;
	}

	private void updateSubRegion(int index) {
		JTextField subRegionField = subRegionList.get(index);
		String newest = subRegionField.getText();
		if (currentSubRegId.get(index).equals(newest)) {
			return;
		} else {
			currentSubRegId.set(index, newest);
			dao.setSubRegion(newest, index);
			/*
			 * changes may cause URI to be changed...
			 */
			txtFieldUri.setText(dao.getUri());
		}

	}

	private JPanel getOrganizationPanel() {
		if (orgPanel == null) {
			dao.getOrganization().getUiWidget();
			orgPanel = (JPanel) dao.getOrganization().getUiWidget();
		}
		return orgPanel;
	}

	private JLabel getLblNewLabel_1() {
		if (lblSysID == null) {
			lblSysID = setLabel(1);
		}
		return lblSysID;
	}

	private JLabel getLblNewLabel_2() {
		if (lblSysURI == null) {
			lblSysURI = setLabel(2);
		}
		return lblSysURI;
	}

	private JLabel getLblRegion() {
		if (lblRegion == null) {
			lblRegion = setLabel(3);
		}
		return lblRegion;
	}

	private JLabel getLblSubregion() {
		if (lblSubregion == null) {
			lblSubregion = setLabel(4);
		}
		return lblSubregion;
	}

	private JLabel getLblNotes() {
		if (lblNotes == null) {
			lblNotes = setLabel(7);
		}
		return lblNotes;
	}

	protected JLabel setLabel(int id) {
		String[] text = getLocalizedText(id);
		JLabel label = new JLabel(text[0]);
		if (text[1] != null && !text[1].isEmpty()) {
			label.setToolTipText(text[1]);
		}
		return label;
	}

	protected String[] getLocalizedText(int fieldID) {
		String[] text = new String[2];
		String labelID = "row-label-" + fieldID;
		text[0] = editor.getLanguage().getProperty(propertyPrefix + labelID,
				labelID);
		String tipID = "row-tip-" + fieldID;
		text[1] = editor.getLanguage()
				.getProperty(propertyPrefix + tipID, null);
		return text;
	}

	/**
	 * 
	 */
	public void updateDao() {
		String depSel = (String) deprecatedComboBox.getSelectedItem();
		dao.setDeprecated(depSel.equalsIgnoreCase("TRUE"));
		// version kludge
		int verValue = Integer.parseInt(getVersionField().getText());
		dao.setVersion(verValue);
		
		String notes = getNotesField().getText();
		dao.setNotes(notes);
	}
}
