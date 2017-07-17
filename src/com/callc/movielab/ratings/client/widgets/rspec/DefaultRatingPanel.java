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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import com.callc.movielab.ratings.client.resources.GuiSettings;
import com.callc.movielab.ratings.client.rspec.Rating;
import com.callc.movielab.ratings.client.rspec.RatingDescriptor;
import com.callc.movielab.ratings.client.rspec.RatingSystem;
import com.callc.movielab.ratings.client.rspec.ReasonDescriptor;
import com.callc.movielab.ratings.client.rspec.SpecificationElement.ENVIRONMENT;
import com.callc.movielab.ratings.client.rspec.SpecificationElement.MEDIA;
import com.critarch.util.ui.JOutlookBar1;

/**
 * 
 * @author L. J. Levin, created Aug 30, 2013
 * 
 */
public class DefaultRatingPanel extends AbstractSpecElementPanel implements
		RatingPanel {

	private Rating dao;
	private JOutlookBar1 definitionOBar;
	private int defCount;
	private JLabel lblEnvironment;
	private JPanel mediaPanel;
	private JPanel envPanel;
	private JLabel lblMedia;

	/**
	 * @param rating
	 */
	public DefaultRatingPanel(Rating rating) {
		super();
		this.dao = rating;
		propertyPrefix = "RatingPanel.";
		setDataFieldCount(13);
		add(getPanelLabel(), BorderLayout.NORTH);
		add(getMainPanel(), BorderLayout.CENTER);
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
			// rows for data entry....
			int rowIdx = 0;
			// Row 0: ID
			addRow(0, rowIdx++, 1, ROW_TYPE.TEXT);
			// Row 1: URI is auto-generated
			addRow(1, rowIdx++, 1, ROW_TYPE.TEXT);
			dataField[1].setEnabled(false);
			// Row 3: minimum recommended age...
			addRow(3, rowIdx++, 0, 3, ROW_TYPE.AGE);
			// Row 4: minimum accompanied age...
			addRow(4, rowIdx++, 0, 1, ROW_TYPE.AGE);
			// Row 5: minimum age
			addRow(5, rowIdx++, 0, 1, ROW_TYPE.AGE);
			// Row 6: deprecated
			addRow(6, rowIdx++, 0, 1, ROW_TYPE.BOOL);
			// Row 7: Ordinal ranking
			addRow(9, rowIdx++, 0, 1, ROW_TYPE.ORDINAL);
			// Row 8: Home Parental Control usage
			addRow(10, rowIdx++, 0, 1, ROW_TYPE.BOOL);
			// Row 2: URL of logo or icon
			addRow(2, rowIdx++, 1, ROW_TYPE.TEXT);
			addRow(11, rowIdx++, 1, ROW_TYPE.TEXT);
//			addRow(12, rowIdx++, 0, 1, ROW_TYPE.ENUM);
			// ++++++++++++++++++++++++++++++++++++++
			// addMediaPanel(3,3);
			// addEnvPanel(4,3);

			// ++++++++++++++++++++++++++++++++++++++
			definitionOBar = addAccordianPanel(rowIdx);
			// addFillerPanel(6);
			syncWithDao();
			definitionOBar.setVisibleBar(defCount - 1);
		}
		return mainPanel;
	}

	/**
	 * Any subclass using ROW_TYPE 'ENUM' needs to override this method.
	 * 
	 * @param id
	 * @return
	 */
	protected JComponent getListModel(int id) {
		switch (id) {
		//case 12:
		case 1200:
			// Reasons applicable to a given Rating
			List<String> labels = new ArrayList<String>();
			List<String> tips = new ArrayList<String>();
			RatingSystem rSys = (RatingSystem) dao.getParent().getParent();
			List<ReasonDescriptor> reasonList = rSys.getAllReasons();
			for (int i = 0; i < reasonList.size(); i++) {
				ReasonDescriptor reason = reasonList.get(i);
				labels.add(reason.getReasonID());
				tips.add(reason.getDefinition());
			}
			return buildEnumPanel(labels, tips);
		default:
			throw new UnsupportedOperationException();
		}
	}
 
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.RatingPanel#syncWithDao()
	 */
	@Override
	public void syncWithDao() {
		((JTextComponent) dataField[0]).setText(dao.getRatingID());
		((JTextComponent) dataField[1]).setText(dao.getUri());
		((JTextComponent) dataField[2]).setText(dao.getLogoUrl(0));
		((JTextComponent) dataField[11]).setText(dao.getLogoUrl(1));
		// Field 3: minimum unaccompanied age...
		JComboBox cb = (JComboBox) dataField[3];
		cb.setSelectedIndex(dao.getMinRecAge());
		// Field 4: minimum accompanied age...
		cb = (JComboBox) dataField[4];
		cb.setSelectedIndex(dao.getMinAgeSupervised());
		// Field 5: minimum unaccompanied age...
		cb = (JComboBox) dataField[5];
		cb.setSelectedIndex(dao.getMinAge());
		// Field 6: deprecated...
		cb = (JComboBox) dataField[6];
		if (dao.isDeprecated()) {
			cb.setSelectedIndex(0);
		} else {
			cb.setSelectedIndex(1);
		}
		// Field 9 (currently in Row 7): ordinal...
		cb = (JComboBox) dataField[9];
		cb.setSelectedIndex(dao.getOrdinal());
		// Field 10 (row 8): HPC Usage...
		cb = (JComboBox) dataField[10];
		if (dao.isHpcApplicable()) {
			cb.setSelectedIndex(0);
		} else {
			cb.setSelectedIndex(1);
		}
		List<RatingDescriptor> dList = dao.getDescriptorList();
		defCount = dList.size();
		for (int i = 0; i < dList.size(); i++) {
			RatingDescriptor next = dList.get(i);
			JOutlookBar1.BarInfo bar = definitionOBar.addBar(next.toString(),
					next.getUiWidget());
			bar.getButton().setBackground(GuiSettings.backgroundPanelLabel);
		}
		// Associated REASONs...
//		JPanel enumPanel = (JPanel) dataField[12];
//		int cBoxCnt = enumPanel.getComponentCount();
//		for (int i = 0; i < cBoxCnt; i++) {
//			JCheckBox checkBox = (JCheckBox) enumPanel.getComponent(i);
//			checkBox.setSelected(dao.isApplicableReason(checkBox.getText()));
//		} 
//		setReasonCBoxEnabled(false);
	}

//	protected void setReasonCBoxEnabled(boolean flag){ 
//		JPanel enumPanel = (JPanel) dataField[12];
//		int cBoxCnt = enumPanel.getComponentCount();
//		for (int i = 0; i < cBoxCnt; i++) {
//			JCheckBox checkBox = (JCheckBox) enumPanel.getComponent(i);
//			checkBox.setEnabled(flag);
//		}
//	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.RatingPanel#removeDescriptor
	 * (com.callc.movielab.ratings.client.rspec.RatingDescriptor)
	 */
	@Override
	public void removeDescriptor(RatingDescriptor rDesc) {
		definitionOBar.removeBar(rDesc.toString());
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
			dao.setRatingID(((JTextComponent) dataField[rowId]).getText());
			break;
		case 1:
			dao.setUri(((JTextComponent) dataField[rowId]).getText());
			break;
		case 2:
			dao.setLogoUrl(((JTextComponent) dataField[rowId]).getText(), 0);
			break;
		case 11:
			dao.setLogoUrl(((JTextComponent) dataField[rowId]).getText(), 1);
			break;
		case 3:
			// Row 3: minimum unaccompanied age...
			JComboBox cb = (JComboBox) dataField[3];
			int value = cb.getSelectedIndex();
			dao.setMinRecAge(value);
			break;
		case 4:
			// Row 4: minimum accompanied age...
			cb = (JComboBox) dataField[4];
			value = cb.getSelectedIndex();
			dao.setMinAgeSupervised(value);
			break;
		case 5:
			// Row 5: Legal Requirement
			cb = (JComboBox) dataField[5];
			value = cb.getSelectedIndex();
			dao.setMinAge(value);
			break;
		case 6:
			// Row 6: Deprecated rating
			cb = (JComboBox) dataField[6];
			dao.setDeprecated(cb.getSelectedIndex() == 0);
			break;
		case 9:
			// Row 7: Ordinal (aka ranking, severity, etc)...
			cb = (JComboBox) dataField[9];
			value = cb.getSelectedIndex();
			dao.setOrdinal(value);
			break;
		case 10:
			// Row 8: Home Control usage
			cb = (JComboBox) dataField[10];
			dao.setHpcApplicable(cb.getSelectedIndex() == 0);
			break;
		case 12:
			// Associated REASONs...
//			JPanel enumPanel = (JPanel) dataField[12];
//			int cBoxCnt = enumPanel.getComponentCount();
//			for (int i = 0; i < cBoxCnt; i++) {
//				JCheckBox checkBox = (JCheckBox) enumPanel.getComponent(i);
//				dao.setApplicableReason(checkBox.getText(),
//						checkBox.isSelected());
//			}
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.RatingPanel#resetReasonPanel
	 * (java.util.List)
	 */
	@Override
	public void resetReasonPanel() {
		boolean usingOldWay = false;
		if(!usingOldWay){
			return;
		}
		JPanel enumPanel = (JPanel) dataField[12];
		int cBoxCnt = enumPanel.getComponentCount();
		RatingSystem rSys = (RatingSystem) dao.getParent().getParent();
		List<ReasonDescriptor> reasonList = rSys.getAllReasons();
		// which list has more?
		/*
		 * WARNING: this algorithm assumes only 1 change is processed at a time.
		 */
		if (cBoxCnt > reasonList.size()) {
			// something was deleted
			for (int i = 0; i < reasonList.size(); i++) {
				ReasonDescriptor reason = reasonList.get(i);
				JCheckBox cb = (JCheckBox) enumPanel.getComponent(i);
				// does it match ith entry in reasonList?
				if (!reason.getReasonID().equalsIgnoreCase(cb.getText())) {
					// delete the cBox
					enumPanel.remove(cb);
					dao.setApplicableReason(cb.getText(), false);
					return;
				}
			}
			/*
			 * If we got to this point it means the last reason was deleted
			 */
			JCheckBox cb = (JCheckBox) enumPanel.getComponent(cBoxCnt - 1);
			enumPanel.remove(cb);
			dao.setApplicableReason(cb.getText(), false);
			return;

		} else if (cBoxCnt < reasonList.size()) {
			// something was added (somewhere)
			for (int i = 0; i < reasonList.size(); i++) {
				ReasonDescriptor reason = reasonList.get(i);
				if (i == cBoxCnt) {
					// at the end
					JCheckBox addedCBox = new JCheckBox(reason.getReasonID());
					String tTip = reason.getDefinition();
					if (tTip != null && !tTip.isEmpty()) {
						addedCBox.setToolTipText(tTip);
					} 
					addedCBox.setSelected(dao.isApplicableReason(addedCBox.getText()));
					enumPanel.add(addedCBox);
					reason.linkWithRating(addedCBox);
					return;
				}
				JCheckBox cb = (JCheckBox) enumPanel.getComponent(i);
				// does it match ith entry in reasonList?
				if (!reason.getReasonID().equalsIgnoreCase(cb.getText())) {
					JCheckBox addedCBox = new JCheckBox(reason.getReasonID());
					String tTip = reason.getDefinition();
					if (tTip != null && !tTip.isEmpty()) {
						addedCBox.setToolTipText(tTip);
					}
					addedCBox.setSelected(dao.isApplicableReason(addedCBox.getText()));
					enumPanel.add(addedCBox, i);
					reason.linkWithRating(addedCBox);
					return;
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.callc.movielab.ratings.client.widgets.rspec.RatingPanel#syncUri()
	 */
	@Override
	public void syncUri() {
		((JTextComponent) dataField[1]).setText(dao.getUri());
	}

	private JLabel getLblMedia() {
		if (lblMedia == null) {
			lblMedia = new JLabel("Media");
		}
		return lblMedia;
	}
 

	private JLabel getLblEnvironment() {
		if (lblEnvironment == null) {
			lblEnvironment = new JLabel("Environment");
		}
		return lblEnvironment;
	}
 

}
