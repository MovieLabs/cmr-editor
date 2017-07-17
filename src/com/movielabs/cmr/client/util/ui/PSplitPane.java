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
package com.movielabs.cmr.client.util.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JSplitPane;

/**
 * Extension to basic Swing <tt>JSplitPane</tt> that provides a work-around hack
 * for a 'feature' that hinders setting the divider location when initializing
 * or when changing the top/bottom/left/right <tt>Components</tt>
 * 
 * @author L. J. Levin
 * 
 */
public class PSplitPane extends JSplitPane {
	public PSplitPane() {
		setOneTouchExpandable(true);
	}

	private boolean isPainted;
	private boolean hasProportionalLocation;
	private double proportionalLocation;
	private int absLocation;
	private boolean hasAbsLocation;

	/*
	 * From the javadoc for setDividerLocation(double):
	 * 
	 * This method is implemented in terms of setDividerLocation(int).
	 * 
	 * This method immediately changes the size of the receiver based on its
	 * current size. If the receiver is not correctly realized and on screen,
	 * this method will have no effect (new divider location will become
	 * (current size * proportionalLocation) which is 0).
	 * 
	 * So, as you can see the JSplitPane MUST be visible invoking this method
	 * otherwise it will not have the desired effect.
	 * 
	 * The following methods are a modified version of a hack taken from a jGuru
	 * faq at http://www.jguru.com/faq/view.jsp?EID=27191
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JSplitPane#setDividerLocation(double)
	 */
	public void setDividerLocation(int location) {
		if (!isPainted) {
			hasProportionalLocation = false;
			hasAbsLocation = true;
			this.absLocation = location;
		} else
			super.setDividerLocation(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JSplitPane#setDividerLocation(double)
	 */
	public void setDividerLocation(final double proportion) {
		// if (!isPainted) {
		// hasProportionalLocation = true;
		// hasAbsLocation = false;
		// this.proportionalLocation = proportion;
		// } else
		// super.setDividerLocation(proportion);
		if (isShowing()) {
			if (getWidth() > 0 && getHeight() > 0) {
				super.setDividerLocation(proportion);
			} else {
				final JSplitPane target = this;
				addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent ce) {
						removeComponentListener(this);
						target.setDividerLocation(proportion);
					}
				});
			}
		} else {
			final JSplitPane target = this;
			addHierarchyListener(new HierarchyListener() {
				@Override
				public void hierarchyChanged(HierarchyEvent e) {
					if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0
							&& isShowing()) {
						removeHierarchyListener(this);
						target.setDividerLocation(proportion);
					}
				}
			});
		}
	}

	public void paint(Graphics g) {
		if (!isPainted) {
			if (hasProportionalLocation) {
				super.setDividerLocation(proportionalLocation);
			} else if (hasAbsLocation) {
				super.setDividerLocation(absLocation);
			}
			isPainted = true;
			hasProportionalLocation = false;
			hasAbsLocation = false;
		}
		super.paint(g);
	}

	public void setRightComponent(Component comp) {
		int curLoc = getDividerLocation();
		super.setRightComponent(comp);
		if (curLoc >= 0) {
			setDividerLocation(curLoc);
		}
	}

	public void setLeftComponent(Component comp) {
		int curLoc = getDividerLocation();
		super.setLeftComponent(comp);
		if (curLoc >= 0) {
			setDividerLocation(curLoc);
		}
	}

	public void setTopComponent(Component comp) {
		int curLoc = getDividerLocation();
		super.setTopComponent(comp);
		if (curLoc >= 0) {
			setDividerLocation(curLoc);
		}
	}

	public void setBottomComponent(Component comp) {
		int curLoc = getDividerLocation();
		super.setBottomComponent(comp);
		if (curLoc >= 0) {
			setDividerLocation(curLoc);
		}
	}
}
