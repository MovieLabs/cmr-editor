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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import org.jdom2.Element;

import com.movielabs.cmr.client.RatingsEditor;
import com.movielabs.cmr.client.resources.GuiSettings;
import com.movielabs.cmr.client.rspec.*;

import com.movielabs.cmr.client.widgets.rspec.*;

/**
 * 
 * @author L. J. Levin, created Aug 24, 2013
 * 
 */
public class RSpecNavPanel extends JPanel implements TreeModelListener,
		TreeSelectionListener, MouseListener {
	protected RatingSystem rootRatingSysNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	protected LanguageSelectDialog langDialog = new LanguageSelectDialog();

	/**
	 * @param rSys
	 */
	public RSpecNavPanel(RatingSystem rSys) {
		setBackground(GuiSettings.backgroundPanel);
		rootRatingSysNode = rSys;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(GuiSettings.backgroundPanel);
		add(scrollPane, BorderLayout.CENTER);
		// ...
		treeModel = new DefaultTreeModel(rootRatingSysNode);
		tree = new JTree(treeModel);
		tree.setBackground(GuiSettings.backgroundPanel);
		tree.setEditable(false);
		// tree.setLargeModel(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		TreePath path1 = new TreePath(rootRatingSysNode.getPath());
		tree.setSelectionPath(path1);
		scrollPane.getViewport().setView(tree);
		treeModel.addTreeModelListener(this);
		tree.addTreeSelectionListener(this);
		tree.addMouseListener(this);
		DefaultTreeCellRenderer defaultRenderer = (DefaultTreeCellRenderer) tree
				.getCellRenderer();
		defaultRenderer
				.setBackgroundNonSelectionColor(GuiSettings.backgroundPanel);
		defaultRenderer
				.setBackgroundSelectionColor(GuiSettings.backgroundPanelLabel);
	}

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildPopup(DefaultMutableTreeNode node) {
		JPopupMenu popup = new JPopupMenu();
		if (node instanceof RatingsBin) {
			return buildRatingsBinPopup(node);
		} else if (node instanceof ReasonsBin) {
			return buildReasonsBinPopup(node);
		} else if (node instanceof ReasonDescriptor) {
			return buildReasonDescPopup(node);
		} else if (node instanceof Rating) {
			return buildRatingPopup(node);
		} else if (node instanceof Criteria) {
			return buildCriteriaPopup(node);
		}else if (node instanceof UsageBin) {
			return buildUsageBinPopup(node);
		} else if (node instanceof AdoptiveRegion) {
			return buildUsagePopup(node);
		} 
		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildUsageBinPopup(DefaultMutableTreeNode node) {
		final DefaultMutableTreeNode bin = node;
		JPopupMenu popup = new JPopupMenu();
		popup.setLabel(rootRatingSysNode.toString());
		JMenuItem addMI = new JMenuItem("add new Region of Usage");
		addMI.setToolTipText("appends a new usage to end of list");
		popup.add(addMI);
		addMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode child = rootRatingSysNode
						.addUsage(null);
				treeModel.insertNodeInto(child, bin, bin.getChildCount() - 1);
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		return popup;
	}
	
	

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildUsagePopup(DefaultMutableTreeNode node) {
		final RatingsEditor editor = RatingsEditor.getEditor();
		/*
		 * some items are enabled based on what, if anything, is on the
		 * clipboard
		 */
		final Element xml = editor.getClipboardXml();
		String xmlType = null;
		if (xml != null) {
			xmlType = xml.getName();
		}
		final AdoptiveRegion regionOfUseage = (AdoptiveRegion) node;
		JPopupMenu popup = new JPopupMenu();
		popup.setLabel(node.toString());

		JMenuItem copyMI = new JMenuItem("copy");
		popup.add(copyMI);
		copyMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(regionOfUseage.asXml(true));
			}
		});

		JMenuItem pasteMI = new JMenuItem("paste");
		popup.add(pasteMI);
		pasteMI.setToolTipText("paste clipbaord contents BEFORE selected node");
		pasteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) regionOfUseage
						.getParent();
				int index = bin.getIndex(regionOfUseage);
				DefaultMutableTreeNode child = rootRatingSysNode.insertUsage(xml, index);
				treeModel.insertNodeInto(child, bin, index);
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		pasteMI.setEnabled(xmlType != null
				&& xmlType.equalsIgnoreCase("AdoptiveRegion"));

		JMenuItem dupMI = new JMenuItem("duplicate");
		popup.add(dupMI);
		dupMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(regionOfUseage.asXml(true));
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) regionOfUseage
						.getParent();
				int index = bin.getIndex(regionOfUseage);
				DefaultMutableTreeNode child = rootRatingSysNode.insertUsage(
						xml, index);
				treeModel.insertNodeInto(child, bin, index); 
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});

		JMenuItem deleteMI = new JMenuItem("delete");
		popup.add(deleteMI);
		deleteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: add confirm dialog
				rootRatingSysNode.delete(regionOfUseage);
				treeModel.removeNodeFromParent(regionOfUseage);
			}
		}); 

		return popup;
	}
	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildRatingsBinPopup(DefaultMutableTreeNode node) {
		final DefaultMutableTreeNode bin = node;
		JPopupMenu popup = new JPopupMenu();
		popup.setLabel(rootRatingSysNode.toString());
		JMenuItem addRatingMI = new JMenuItem("add new rating");
		addRatingMI.setToolTipText("appends a new Rating to end of list");
		popup.add(addRatingMI);
		addRatingMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode child = rootRatingSysNode
						.addRating(null);
				treeModel.insertNodeInto(child, bin, bin.getChildCount() - 1);
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		return popup;
	}

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildRatingPopup(DefaultMutableTreeNode node) {
		final RatingsEditor editor = RatingsEditor.getEditor();
		/*
		 * some items are enabled based on what, in anything, is on the
		 * clipboard
		 */
		final Element xml = editor.getClipboardXml();
		String xmlType = null;
		if (xml != null) {
			xmlType = xml.getName();
		}
		final Rating rating = (Rating) node;
		JPopupMenu popup = new JPopupMenu();
		popup.setLabel(node.toString());

		JMenuItem copyMI = new JMenuItem("copy");
		popup.add(copyMI);
		copyMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(rating.asXml(true));
			}
		});

		JMenuItem pasteMI = new JMenuItem("paste");
		popup.add(pasteMI);
		pasteMI.setToolTipText("paste clipbaord contents BEFORE selected node");
		pasteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) rating
						.getParent();
				int index = bin.getIndex(rating);
				DefaultMutableTreeNode child = rootRatingSysNode.insertRating(
						xml, index);
				treeModel.insertNodeInto(child, bin, index);
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		pasteMI.setEnabled(xmlType != null
				&& xmlType.equalsIgnoreCase("Rating"));

		JMenuItem dupMI = new JMenuItem("duplicate");
		popup.add(dupMI);
		dupMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(rating.asXml(true));
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) rating
						.getParent();
				int index = bin.getIndex(rating);
				DefaultMutableTreeNode child = rootRatingSysNode.insertRating(
						xml, index);
				treeModel.insertNodeInto(child, bin, index); 
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});

		JMenuItem deleteMI = new JMenuItem("delete");
		popup.add(deleteMI);
		deleteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: add confirm dialog
				rootRatingSysNode.delete(rating);
				treeModel.removeNodeFromParent(rating);
			}
		});
		// ....
		popup.add(new JSeparator());
		JMenuItem addDescMI = new JMenuItem("add Descriptor");
		popup.add(addDescMI);
		addDescMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				langDialog.setLocationRelativeTo(tree);
				langDialog.setVisible(true);
				if (langDialog.isConfirmed()) {
					String lang = langDialog.getSelectedLangCode();
					DefaultMutableTreeNode child = rating.addDesc(lang);
					/*
					 * if the individual Descriptor nodes are to appear in the
					 * JTree than uncomment the next couple lines
					 */
					// treeModel.insertNodeInto(child, rating,
					// rating.getChildCount() - 1);
					// tree.scrollPathToVisible(new
					// TreePath(child.getPath()));
				}
			}
		});

		JMenu ddMenu = new JMenu("delete Descriptor...");
		popup.add(ddMenu);
		List<RatingDescriptor> dList = rating.getDescriptorList();
		for (int i = 0; i < dList.size(); i++) {
			final RatingDescriptor rDesc = dList.get(i);
			JMenuItem deleteDescMI = new JMenuItem(rDesc.toString());
			ddMenu.add(deleteDescMI);
			deleteDescMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// TODO: add confirm dialog
					rating.delete(rDesc);
					/*
					 * if the individual Descriptor nodes are to appear in the
					 * JTree than uncomment the next line
					 */
					// treeModel.removeNodeFromParent(rDesc);

				}
			});
		}
		 
		popup.add(new JSeparator());  
		JMenuItem addMI = new JMenuItem("add Usage Override");
		addMI.setToolTipText("add restriction of usage");
		popup.add(addMI);
//		addMI.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				DefaultMutableTreeNode child = rootRatingSysNode
//						.addUsage(null);
//				treeModel.insertNodeInto(child, bin, bin.getChildCount() - 1);
//				TreePath targetPath = new TreePath(child.getPath());
//				tree.scrollPathToVisible(targetPath);
//				tree.setSelectionPath(targetPath);
//			}
//		});

		return popup;
	}

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildReasonsBinPopup(DefaultMutableTreeNode node) {
		JPopupMenu popup = new JPopupMenu();
		final DefaultMutableTreeNode bin = node;
		popup.setLabel(rootRatingSysNode.toString());
		JMenuItem addMI = new JMenuItem("add new reason");
		addMI.setToolTipText("appends a new Reason to end of list");
		popup.add(addMI);
		addMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode child = rootRatingSysNode
						.addReason(null);
				int curCnt = bin.getChildCount();
				treeModel.insertNodeInto(child, bin, curCnt - 1); 
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		return popup;
	}

	/**
	 * @param node
	 * @return
	 */
	protected JPopupMenu buildReasonDescPopup(DefaultMutableTreeNode node) {
		final RatingsEditor editor = RatingsEditor.getEditor();
		/*
		 * some items are enabled based on what, in anything, is on the
		 * clipboard
		 */
		final Element xml = editor.getClipboardXml();
		String xmlType = null;
		if (xml != null) {
			xmlType = xml.getName();
		}
		JPopupMenu popup = new JPopupMenu();
		final ReasonDescriptor reasonNode = (ReasonDescriptor) node;
		popup.setLabel(node.toString());

		JMenuItem copyMI = new JMenuItem("copy");
		popup.add(copyMI);
		copyMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(reasonNode.asXml(true));
			}
		});

		JMenuItem pasteMI = new JMenuItem("paste");
		popup.add(pasteMI);
		pasteMI.setToolTipText("paste clipboard contents BEFORE selected node");
		pasteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) reasonNode
						.getParent();
				int index = bin.getIndex(reasonNode);
				DefaultMutableTreeNode child = rootRatingSysNode.insertReason(
						xml, index);
				treeModel.insertNodeInto(child, bin, index); 
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});
		pasteMI.setEnabled(xmlType != null
				&& xmlType.equalsIgnoreCase("Reason"));

		JMenuItem dupMI = new JMenuItem("duplicate");
		popup.add(dupMI);
		dupMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setClipboardXml(reasonNode.asXml(true));
				Element xml = editor.getClipboardXml();
				DefaultMutableTreeNode bin = (DefaultMutableTreeNode) reasonNode
						.getParent();
				int index = bin.getIndex(reasonNode);
				DefaultMutableTreeNode child = rootRatingSysNode.insertReason(
						xml, index);
				treeModel.insertNodeInto(child, bin, index); 
				TreePath targetPath = new TreePath(child.getPath());
				tree.scrollPathToVisible(targetPath);
				tree.setSelectionPath(targetPath);
			}
		});

		JMenuItem deleteMI = new JMenuItem("delete");
		popup.add(deleteMI);
		deleteMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: add confirm dialog
				rootRatingSysNode.deleteReason(reasonNode);
				treeModel.removeNodeFromParent(reasonNode);
			}
		});
		/*
		 * Menu Items to handle applying a Reason to a Rating..
		 */
		popup.add(new JSeparator());
		JMenu addMappingMenu = new JMenu("apply to..");
		popup.add(addMappingMenu);
		List<Rating> ratList = rootRatingSysNode.getAllRatings();
		for (int i = 0; i < ratList.size(); i++) {
			final Rating nextRating = ratList.get(i);
			JMenuItem applyMI = new JMenuItem(nextRating.toString());
			addMappingMenu.add(applyMI);
			if (reasonNode.isAppliedTo(nextRating)) {
				applyMI.setEnabled(false);
			} else {
				applyMI.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { 
						DefaultMutableTreeNode child = reasonNode
								.applyTo(nextRating);
						/*
						 * if the individual child nodes are to appear in the
						 * JTree than uncomment the next couple lines
						 */
						int curCnt = reasonNode.getChildCount();
						treeModel.insertNodeInto(child, reasonNode, curCnt - 1);
						tree.scrollPathToVisible(new TreePath(child.getPath()));
						// }
					}
				});
			}
		}
		return popup;
	}

	/**
	 * @param node
	 * @return
	 */
	private JPopupMenu buildCriteriaPopup(DefaultMutableTreeNode node) {
		final Criteria target = (Criteria) node;
		JPopupMenu popup = new JPopupMenu();
		JMenuItem deleteDescMI = new JMenuItem("delete");
		popup.add(deleteDescMI);
		deleteDescMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: add confirm dialog
				ReasonDescriptor parent = (ReasonDescriptor) target.getParent();
				parent.dropApplication(target);  
				treeModel.removeNodeFromParent(target);
				tree.scrollPathToVisible(new TreePath(parent.getPath()));
			}
		});
		return popup;
	}

	/**
	 * @param ratingSystem
	 */
	public void hasChanged(SpecificationElement node) {
		treeModel.nodeChanged(node);
	}

	/**
	 * @return
	 */
	public TreeNode getSelectedNode() {
		TreeNode selection = (TreeNode) tree.getSelectionPath()
				.getLastPathComponent();
		return selection;
	}

	/**
	 * @return
	 */
	public Component getSelectedWidget() {
		RSpecLeaf selection = (RSpecLeaf) tree.getSelectionPath()
				.getLastPathComponent();
		return selection.getUiWidget();
	}

	/*
	 * START of LISTENER methods...
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event
	 * .TreeModelEvent)
	 */
	@Override
	public void treeNodesChanged(TreeModelEvent evt) {
		// System.out.println("treeNodesChanged");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event
	 * .TreeModelEvent)
	 */
	@Override
	public void treeNodesInserted(TreeModelEvent evt) {
		// System.out.println("treeNodesInserted");
		processStructureChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event
	 * .TreeModelEvent)
	 */
	@Override
	public void treeNodesRemoved(TreeModelEvent evt) {
		// System.out.println("treeNodesRemoved");
		processStructureChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.
	 * event.TreeModelEvent)
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent evt) {
		// System.out.println("treeStructureChanged");
	}

	/**
	 * Currently unused code. An evolutionary dead-end??
	 * 
	 * @param evt
	 */
	private void processStructureChange(TreeModelEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null) {
			// Nothing is selected.
			return;
		}
		// System.out.println("StructureChanged - insert or delete at {"+node.toString()+"}");
		if (node instanceof ReasonsBin || node instanceof ReasonDescriptor) {
			// System.out.println("   change to set of Reasons detected");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null) {
			// Nothing is selected.
			return;
		}
		// System.out.println("Selected " + node.toString());
		/*
		 * display the appropriate construct
		 */
		RatingsEditor.getEditor().setEditingPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			int row = tree.getRowForLocation(evt.getX(), evt.getY());
			TreePath evtPath = tree.getPathForRow(row);
			if (evtPath != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) evtPath
						.getLastPathComponent();
				JPopupMenu popupMenu = buildPopup(node);
				if (popupMenu != null) {
					popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent evt) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent evt) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent evt) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent evt) {
	}
}
