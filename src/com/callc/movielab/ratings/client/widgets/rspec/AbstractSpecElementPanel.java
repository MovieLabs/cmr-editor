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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import com.callc.movielab.ratings.client.RatingsEditor;
import com.callc.movielab.ratings.client.resources.GuiSettings;
import com.callc.movielab.ratings.client.util.LanguageCode;
import com.critarch.util.ui.JOutlookBar1;

/**
 * 
 * @author L. J. Levin, created Aug 30, 2013
 * 
 */
public abstract class AbstractSpecElementPanel extends JPanel implements SpecElementPanel {
	protected static enum ROW_TYPE {
		TEXT, LANG_TEXT, BOOL, AGE, ORDINAL, ENUM
	};

	public static String[] boolOpt = { "TRUE", "FALSE" };
	public static int ordinalRange = 101;
	protected static String[] ordinalOpt;
	public static int maxAgeRange = 30;
	protected static String[] ageOpt;
	protected static String[] languages = LanguageCode.getInDisplayOrder();
	protected RatingsEditor editor;
	protected JPanel mainPanel;
	protected String propertyPrefix;
	protected HashMap<String, JTextComponent> knownFields = new HashMap<String, JTextComponent>();

	protected ROW_TYPE[] rowType;
	protected String[] currentText;
	protected JComponent[] dataField;
	protected JLabel[] labels;
	protected JComponent[] langSelector;

	protected JLabel panelLabel;

	static {
		ageOpt = new String[maxAgeRange];
		for (int i = 0; i < maxAgeRange; i++) {
			ageOpt[i] = Integer.toString(i);
		}
		ordinalOpt = new String[ordinalRange];
		for (int i = 0; i < ordinalRange; i++) {
			ordinalOpt[i] = Integer.toString(i);
		}
	}

	/**
	 * Create the panel.
	 */
	public AbstractSpecElementPanel() {
		editor = RatingsEditor.getEditor();
		setLayout(new BorderLayout(0, 10));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBackground(GuiSettings.backgroundPanel);

	}

	/**
	 * @param rowcount
	 */
	protected void setDataFieldCount(int rc) {
		currentText = new String[rc];
		dataField = new JComponent[rc];
		labels = new JLabel[rc];
		langSelector = new JComponent[rc];
		rowType = new ROW_TYPE[rc];
	}

	/**
	 * @return
	 */
	protected Component getPanelLabel() {
		if (panelLabel == null) {
			panelLabel = new JLabel(editor.getLanguage().getProperty(
					propertyPrefix + "panel-label", propertyPrefix));
			panelLabel.setBackground(GuiSettings.backgroundPanelLabel);
			panelLabel.setOpaque(true);
			panelLabel.setFont(new Font("Arial", panelLabel.getFont()
					.getStyle(), panelLabel.getFont().getSize() + 2));
			panelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return panelLabel;
	}

	protected void addRow(int id, int row, int rowHeight, ROW_TYPE rType) {
		addRow(id, row, 0, rowHeight, rType);
	}

	protected void addRow(int id, int row, int colOffset, int rowHeight,
			ROW_TYPE rType) {

		dataField[id] = null;
		langSelector[id] = null;

		String labelID = "row-label-" + id;
		String labelText = editor.getLanguage().getProperty(
				propertyPrefix + labelID, labelID);
		String tipID = "row-tip-" + id;
		String tipText = editor.getLanguage().getProperty(
				propertyPrefix + tipID, null);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = colOffset;
		gbc_label.gridy = row;
		JLabel fieldLabel = new JLabel(labelText);
		if (tipText != null) {
			fieldLabel.setToolTipText(tipText);
		}
		mainPanel.add(fieldLabel, gbc_label);
		labels[id] = fieldLabel;

		GridBagConstraints gbc_field = new GridBagConstraints();
		gbc_field.weightx = 1.0;
		gbc_field.insets = new Insets(0, 0, 5, 0);
		gbc_field.gridx = 2 + colOffset;
		gbc_field.gridy = row;

		rowType[id] = rType;
		JComponent dataEntryWidget = null;
		JComponent uiWidget = null;
		JList listWidget;
		switch (rType) {
		case LANG_TEXT:
			GridBagConstraints gbc_langCB = new GridBagConstraints();
			gbc_langCB.weightx = 1.0;
			gbc_langCB.insets = new Insets(0, 0, 5, 0);
			gbc_langCB.gridx = 5 + colOffset;
			gbc_langCB.gridy = row;
			gbc_langCB.fill = GridBagConstraints.HORIZONTAL;
			gbc_langCB.weightx = 0;
			gbc_langCB.anchor = GridBagConstraints.LINE_END;
			JComboBox langComboBox = new JComboBox(languages);
			langSelector[id] = langComboBox;
			mainPanel.add(langComboBox, gbc_langCB);
			gbc_field.gridwidth = GridBagConstraints.RELATIVE;
			gbc_field.fill = GridBagConstraints.HORIZONTAL;
			dataEntryWidget = buildTextWidget(id, row, rowHeight);
			uiWidget = dataEntryWidget;
			break;
		case TEXT:
			gbc_field.gridwidth = GridBagConstraints.REMAINDER;
			gbc_field.fill = GridBagConstraints.HORIZONTAL;
			dataEntryWidget = buildTextWidget(id, row, rowHeight);
			uiWidget = dataEntryWidget;
			break;
		case BOOL:
			gbc_field.anchor = GridBagConstraints.LINE_START;
			dataEntryWidget = new JComboBox(boolOpt);
			uiWidget = dataEntryWidget;
			break;
		case ENUM:
			gbc_field.anchor = GridBagConstraints.LINE_START;
			dataEntryWidget = getListModel(id);
			uiWidget = dataEntryWidget;
			break;
		case AGE:
			gbc_field.anchor = GridBagConstraints.LINE_START;
			dataEntryWidget = new JComboBox(ageOpt);
			uiWidget = dataEntryWidget;
			break;
		case ORDINAL:
			gbc_field.anchor = GridBagConstraints.LINE_START;
			dataEntryWidget = new JComboBox(ordinalOpt);
			uiWidget = dataEntryWidget;
			break;
		}
		dataField[id] = dataEntryWidget;
		// if (rowHeight > 1 && (rType == ROW_TYPE.TEXT)) {
		// JScrollPane scrollPane = new JScrollPane();
		// scrollPane.setViewportView(dataEntryWidget);
		// scrollPane
		// .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane
		// .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// mainPanel.add(scrollPane, gbc_field);
		// } else {
		mainPanel.add(uiWidget, gbc_field);
		// }
		if (tipText != null) {
			uiWidget.setToolTipText(tipText);
			dataEntryWidget.setToolTipText(tipText);
		}
	}

	/**
	 * Any subclass using ROW_TYPE 'ENUM' needs to override this method.
	 * 
	 * @param id
	 * @return
	 */
	protected JComponent getListModel(int id) {
		throw new UnsupportedOperationException();
	}

	protected JPanel buildEnumPanel(List<String> labels, List<String> tips) {
		JPanel enumPanel = new JPanel();
		enumPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		enumPanel.setLayout(new GridLayout(1, 0, 0, 0));
		for (int eCnt = 0; eCnt < labels.size(); eCnt++) {
			JCheckBox cb = new JCheckBox(labels.get(eCnt));
			enumPanel.add(cb);
			if (tips != null) {
				String toolTip = tips.get(eCnt);
				if (toolTip != null && !toolTip.isEmpty()) {
					cb.setToolTipText(toolTip);
				}
			}
		}
		return enumPanel;
	}

	protected JOutlookBar1 addAccordianPanel(int row) {
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridwidth = GridBagConstraints.REMAINDER;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = row;
		gbc_panel_2.weighty = 1.0;
		JOutlookBar1 ob = new JOutlookBar1();
		ob.setBackground(GuiSettings.backgroundPanel);
		mainPanel.add(ob, gbc_panel_2);
		return ob;
	}

	protected void addFillerPanel(int row) {
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridwidth = GridBagConstraints.REMAINDER;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = row;
		gbc_panel_2.weighty = 1.0;
		JPanel filler = new JPanel();
		filler.setBackground(GuiSettings.backgroundPanel);
		mainPanel.add(filler, gbc_panel_2);
	}

	protected JTextComponent buildTextWidget(final int id, int row,
			int rowHeight) {
		JTextComponent tField = null;
		if (rowHeight > 1) {
			JTextArea tArea = new JTextArea(rowHeight, 80);
			// tArea.setWrapStyleWord(true);
			tArea.setLineWrap(false);
			tField = tArea;
		} else {
			tField = new JTextField();
		}
		if (currentText[id] != null) {
			tField.setText(currentText[id]);
		} else {
			tField.setText("");
		}
		tField.setDisabledTextColor(GuiSettings.disabledTextColor);
		/*
		 * add listeners to detect changes in text field. Any typing is
		 * considered 'completed' when either the user presses 'Enter' or moves
		 * the mouse out of the text field.
		 */
		tField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent evt) {
				updateText(id, evt);
			}
		});
		tField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt) {
				evt.getKeyCode();
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					updateText(id, evt);
				}
			}
		});
		return tField;
	}

	/**
	 * @param evtSrcId
	 */
	protected void updateText(int id, InputEvent evt) {
		JTextComponent tField = (JTextComponent) evt.getSource();
		String newest = tField.getText();
		String previous = currentText[id];
		if (previous != null && previous.equals(newest)) {
			return;
		}
		currentText[id] = newest;
		updateDao(id);
	}

	/* (non-Javadoc)
	 * @see com.callc.movielab.ratings.client.widgets.rspec.SpecElementPanel#updateDao()
	 */
	@Override
	public void updateDao() {
		for (int i = 0; i < dataField.length; i++) {
			if (dataField[i] != null) {
				updateDao(i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.callc.movielab.ratings.client.widgets.rspec.SpecElementPanel#syncWithDao()
	 */
	@Override
	public abstract void syncWithDao();

	protected abstract void updateDao(int id);

}
