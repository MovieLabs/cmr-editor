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

import javax.swing.JPanel;

import com.movielabs.cmr.client.RatingsEditor;
import com.movielabs.cmr.client.resources.GuiSettings;
import com.movielabs.cmr.client.rspec.AdoptiveRegion;
import com.movielabs.cmr.client.rspec.SpecificationElement.ENVIRONMENT;
import com.movielabs.cmr.client.rspec.SpecificationElement.MEDIA;
import com.movielabs.cmr.client.util.CountryCode;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.border.EtchedBorder;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import javax.swing.JCheckBox;

/**
 * 
 * @author L. J. Levin, created Aug 26, 2013
 * 
 */
public class UsagePanel extends JPanel {

	private static final String propertyPrefix = "UsagePanel.";
	private AdoptiveRegion dao;
	private JLabel panelLabel;
	private JPanel panel;
	private JLabel lblRegion;
	private JLabel lblSubregion;
	private JLabel lblMedia;
	private JLabel lblEnvironment;
	private List<JComboBox<String>> regionList = new ArrayList<JComboBox<String>>();
	private List<JTextField> subRegionList = new ArrayList<JTextField>();
	private List<String> currentSubRegId = new ArrayList<String>();
	private JPanel envPanel;
	private JPanel mediaPanel;
	private RatingsEditor editor;
	private JPanel subRegPanel;
	private JTextField srCodeField;

	/**
	 * Create the panel.
	 */
	public UsagePanel(AdoptiveRegion dao) {
		editor = RatingsEditor.getEditor();
		setBackground(GuiSettings.backgroundPanel);
		this.dao = dao;
		setLayout(new BorderLayout(0, 0));
		add(getPanelLabel(), BorderLayout.NORTH);
		add(getPanel(), BorderLayout.CENTER);

	}

	private JLabel getPanelLabel() {
		if (panelLabel == null) {
			String text = editor.getLanguage().getProperty(propertyPrefix + "panel-label", "Properties");
			panelLabel = new JLabel(text);
			panelLabel.setFont(new Font("Arial", panelLabel.getFont().getStyle(), panelLabel.getFont().getSize() + 2));
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
			gbl_panel.columnWidths = new int[] { 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.2, 0.0, 0.0, 0.2, 0.0, 0.0, 0.2, 0.0, 0.0, 0, 0, 0, 0,
					Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
			panel.setLayout(gbl_panel);

			// .............
			// Region
			GridBagConstraints gbc_lblRegion = new GridBagConstraints();
			gbc_lblRegion.insets = new Insets(0, 0, 5, 5);
			gbc_lblRegion.gridx = 0;
			gbc_lblRegion.gridy = 2;
			panel.add(getLblRegion(), gbc_lblRegion);
			// Region[0]
			int regionCellWidth = 3;
			GridBagConstraints gbcRegion01 = new GridBagConstraints();
			gbcRegion01.insets = new Insets(0, 0, 5, 5);
			// gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbcRegion01.gridy = 2;
			gbcRegion01.gridx = 2;
			gbcRegion01.gridwidth = regionCellWidth;
			panel.add(getRegionComboBox(), gbcRegion01);
			// and the sub-region too....
			GridBagConstraints gbc_subRegionField = new GridBagConstraints();
			gbc_subRegionField.insets = new Insets(0, 0, 5, 5);
			gbc_subRegionField.gridy = 3;
			gbc_subRegionField.gridx = 2;
			gbc_subRegionField.gridwidth = regionCellWidth;
			gbc_subRegionField.fill = GridBagConstraints.HORIZONTAL;
			// panel.add(getSubRegionField(), gbc_subRegionField);
			panel.add(getSubRegionPanel(), gbc_subRegionField);

			// .............
			// SubRegion LABEL
			GridBagConstraints gbc_lblSubregion = new GridBagConstraints();
			gbc_lblSubregion.insets = new Insets(0, 0, 5, 5);
			gbc_lblSubregion.gridx = 0;
			gbc_lblSubregion.gridy = 3;
			panel.add(getLblSubregion(), gbc_lblSubregion);

			// ...................
			// media...
			GridBagConstraints gbc_lblMedia = new GridBagConstraints();
			gbc_lblMedia.insets = new Insets(0, 0, 5, 5);
			gbc_lblMedia.gridx = 0;
			gbc_lblMedia.gridy = 4;
			panel.add(getLblMedia(), gbc_lblMedia);

			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
			gbc_panel_1.insets = new Insets(0, 0, 5, 5);
			gbc_panel_1.gridx = 2;
			gbc_panel_1.gridy = 4;
			gbc_panel_1.gridwidth = 8;
			gbc_panel_1.weightx = 1.0;
			gbc_panel_1.fill = GridBagConstraints.REMAINDER;
			panel.add(getMediaPanel(), gbc_panel_1);
			setUpMediaPanel();
			// ...................
			// Environment....
			GridBagConstraints gbc_lblEnvironment = new GridBagConstraints();
			gbc_lblEnvironment.insets = new Insets(0, 0, 5, 5);
			gbc_lblEnvironment.gridx = 0;
			gbc_lblEnvironment.gridy = 5;
			panel.add(getLblEnvironment(), gbc_lblEnvironment);

			GridBagConstraints gbc_envPanel = new GridBagConstraints();
			gbc_envPanel.insets = new Insets(0, 0, 5, 5);
			gbc_envPanel.anchor = GridBagConstraints.NORTHWEST;
			// gbc_envPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_envPanel.gridx = 2;
			gbc_envPanel.gridy = 5;
			gbc_envPanel.gridwidth = 8;
			gbc_envPanel.fill = GridBagConstraints.REMAINDER;
			panel.add(getEnvPanel(), gbc_envPanel);
			// ...................

		}
		return panel;
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
							dao.setIsoCode(isoCode);
						}
					}
				}
			});
			String curIsoCode = dao.getIsoCode();
			if (curIsoCode != null) {
				String country = CountryCode.getNameForCode(curIsoCode);
				targetedCB.setSelectedItem(country);
			}
		}
		return targetedCB;
	}

	private JPanel getSubRegionPanel() {
		if (subRegPanel == null) {
			subRegPanel = new JPanel();
			subRegPanel.setBackground(GuiSettings.backgroundPanel);
			subRegPanel.add(getSubRegionField());
			subRegPanel.add(new JLabel("-")); 
			subRegPanel.add(getSubRegCodeField());
		}
		return subRegPanel;
	}

	private JTextField getSubRegCodeField() {
		if (srCodeField == null) {
			srCodeField = new JTextField();
			srCodeField.setColumns(6);
			srCodeField.setToolTipText("ISO 3166-2");

			String srCode = dao.getSubRegionCode();
			if (srCode != null) {
				srCodeField.setText(srCode);
			}
			/*
			 * add listeners to detect changes in text field. Any typing is
			 * considered 'completed' when either the user presses 'Enter' or
			 * moves the mouse out of the text field.
			 */
			final int cbIdx = 0;
			srCodeField.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent arg0) {
					updateSubRegion(cbIdx);
				}

			});
			srCodeField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					int kCode = arg0.getKeyCode();
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						updateSubRegion(cbIdx);
					}
				}
			});
		}
		return srCodeField;
	}

	public JTextField getSubRegionField() {
		JTextField target = null;
		try {
			target = subRegionList.get(0);
		} catch (Exception e) {
		}
		if (target == null) {
			target = new JTextField();
			subRegionList.add(null);// make sure there is room
			subRegionList.add(0, target);
			currentSubRegId.add(0, "");
			target.setToolTipText("State, province, or canton (OPTIONAL)");
			target.setColumns(10);
			String srName = dao.getSubRegionName();
			if (srName != null) {
				target.setText(srName);
			}
			/*
			 * add listeners to detect changes in text field. Any typing is
			 * considered 'completed' when either the user presses 'Enter' or
			 * moves the mouse out of the text field.
			 */
			final int cbIdx = 0;
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
		String newest = subRegionField.getText() + srCodeField.getText();
		if (currentSubRegId.get(index).equals(newest)) {
			return;
		} else {
			currentSubRegId.set(index, newest);
			dao.setSubRegion(subRegionField.getText(), srCodeField.getText());
		}

	}

	private JPanel getMediaPanel() {
		if (mediaPanel == null) {
			mediaPanel = new JPanel();
			mediaPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			mediaPanel.setLayout(new GridLayout(1, 0, 0, 0));
		}
		return mediaPanel;
	}

	private void setUpMediaPanel() {
		List<MEDIA> envList = new ArrayList<MEDIA>(EnumSet.allOf(MEDIA.class));
		for (int eCnt = 0; eCnt < envList.size(); eCnt++) {
			final MEDIA type = envList.get(eCnt);
			String name = type.toString();
			JCheckBox cb = new JCheckBox(name);
			mediaPanel.add(cb);
			cb.setSelected(dao.getUsage().isMediaEnabled(type));
			cb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent evt) {
					boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
					dao.getUsage().setMediaEnabled(type, selected);
				}
			});
		}

	}

	private JPanel getEnvPanel() {
		if (envPanel == null) {
			envPanel = new JPanel();
			envPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			envPanel.setLayout(new GridLayout(1, 0, 0, 0));
			List<ENVIRONMENT> envList = new ArrayList<ENVIRONMENT>(EnumSet.allOf(ENVIRONMENT.class));
			for (int eCnt = 0; eCnt < envList.size(); eCnt++) {
				final ENVIRONMENT type = envList.get(eCnt);
				String name = type.toString();
				JCheckBox cb = new JCheckBox(name);
				envPanel.add(cb);
				cb.setSelected(dao.getUsage().isEnvironEnabled(type));
				cb.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent evt) {
						boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
						dao.getUsage().setEnvironEnabled(type, selected);
					}
				});
			}
		}
		return envPanel;
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

	private JLabel getLblMedia() {
		if (lblMedia == null) {
			lblMedia = setLabel(7);
		}
		return lblMedia;
	}

	private JLabel getLblEnvironment() {
		if (lblEnvironment == null) {
			lblEnvironment = setLabel(8);
		}
		return lblEnvironment;
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
		text[0] = editor.getLanguage().getProperty(propertyPrefix + labelID, labelID);
		String tipID = "row-tip-" + fieldID;
		text[1] = editor.getLanguage().getProperty(propertyPrefix + tipID, null);
		return text;
	}

	/**
	 * 
	 */
	public void updateDao() {
		// String depSel = (String) deprecatedComboBox.getSelectedItem();
		// dao.setDeprecated(depSel.equalsIgnoreCase("TRUE"));
	}
}
