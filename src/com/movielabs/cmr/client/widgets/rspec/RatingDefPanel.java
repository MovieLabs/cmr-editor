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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import com.movielabs.cmr.client.resources.GuiSettings;
import com.movielabs.cmr.client.rspec.RatingDescriptor;
import com.movielabs.cmr.client.util.LanguageCode;

/**
 * 
 * @author L. J. Levin, created Sep 1, 2013
 * 
 */
public class RatingDefPanel extends AbstractSpecElementPanel {

	private RatingDescriptor dao;

	/**
	 * @param ratingDescriptor
	 */
	public RatingDefPanel(RatingDescriptor ratingDescriptor) {
		super();
		this.dao = ratingDescriptor;
		propertyPrefix = "RatingDesc.";
		setDataFieldCount(4);
		/*
		 * When using the JOutlookBar, a panel label is not displayed.
		 */
		// add(getPanelLabel(), BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(getMainPanel());
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		// add(getMainPanel(), BorderLayout.CENTER);
	}

	/**
	 * @return
	 */
	private Component getMainPanel() {
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
			// rows for data entry....
			addRow(0, 0, 1, ROW_TYPE.TEXT); // the 'long' Label
			addRow(1, 1, 1, ROW_TYPE.TEXT); // the Definition
			addRow(3, 2, 10, ROW_TYPE.TEXT);    // the Explanation
//			addRow(2, 3, 1, ROW_TYPE.TEXT); //DefURL
			addFillerPanel(3);
			syncWithDao();
		}
		return mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.SpecElementPanel#syncWithDao
	 * ()
	 */
	@Override
	public void syncWithDao() {
		((JTextComponent) dataField[0]).setText(dao.getLabel());
//		String language = LanguageCode.getNameForCode(dao.getLanguageCode());
//		((JComboBox) langSelector[0]).setSelectedItem(language);
		((JTextComponent) dataField[1]).setText(dao.getDefinition());
//		((JTextComponent) dataField[2]).setText(dao.getDefUrl());
		((JTextComponent) dataField[3]).setText(dao.getExplanation());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.SpecElementPanel#updateDao
	 * (int)
	 */
	@Override
	protected void updateDao(int rowId) {
		switch (rowId) {
		case 0:
			dao.setLabel(((JTextComponent) dataField[rowId]).getText());
			break;
		case 1:
			dao.setDefinition(((JTextComponent) dataField[rowId]).getText());
			break;
		case 2:
//			dao.setDefUrl(((JTextComponent) dataField[rowId]).getText());
			break;
		case 3:
			dao.setExplanation(((JTextComponent) dataField[rowId]).getText());
			break;
		default:
			break;

		}
	}

}
