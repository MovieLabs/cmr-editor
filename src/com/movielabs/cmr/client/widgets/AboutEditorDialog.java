/**
 * 
 * Copyright Motion Picture Laboratories, Inc. 2014
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

import com.movielabs.cmr.client.util.ui.AboutDialog;

/**
 * 
 * @author L. J. Levin, created Jan 17, 2014
 * 
 */
public class AboutEditorDialog extends AboutDialog {

    protected static String COPYRIGHT_MLABS = "<html><br>� 2014 Motion Picture Laboratories, Inc. All rights reserved.<br>http://www.MovieLabs.com/</html>";
    private static String appLogoPath = "/com/movielabs/cmr/client/images/logo_movielabs.jpg";

	/**
	 * @param title
	 * @param subtitle
	 * @param appLogoPath
	 */
	public AboutEditorDialog(  ) {
		super( "Common Metadata Ratings Editor","", appLogoPath);
		// Adds the build/version tab
		addVersion();
        createContent();
	}

    protected void createContent() { 
        // Adds the copyright tab
        addCopyright();
    } 
    private void addCopyright() {
        String name = "Copyright";
        addTab(name);
        addEntry(name, COPYRIGHT_MLABS); 
    }
    

	private void addVersion() {
		String name = "Build/Version";
		addTab(name);
		addVersionEntry(name, "Editor", "com.callc.movielab");  
	}

}
