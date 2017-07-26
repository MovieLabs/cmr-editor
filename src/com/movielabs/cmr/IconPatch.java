/**
 * Created: Jul 18, 2017
 */
package com.movielabs.cmr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class IconPatch {

	public static void main(String[] args) {
		File startDir = new File("/home/anna/Desktop/start");
		String endDir = "/home/anna/Desktop/end";

		try {
			getFiles(startDir, endDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getFiles(File startDir, String endDir) throws IOException {
		File[] files = startDir.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				if (file.getName().toLowerCase().endsWith(".xml")) {
					List<String> lst = new ArrayList<String>();
					lst.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTFfjCGPH1WcZz9dwONGcx7fwWD_rxim6fbA-ILfArh_usDr5wGHg");
					lst.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQhynTmHDi8cwk2_d4_HaRBh-60tGIXppcny7xSEfTRzr1sWFT");
					loadFiles(endDir, lst);
				}
			}
		}
	}

	private static void loadFiles(String dir, List<String> urls) {
		for (int i = 0; i < urls.size(); i++) {
			try {
				URL website = new URL(urls.get(i));
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dir + "/file"+ i + ".jpeg");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
