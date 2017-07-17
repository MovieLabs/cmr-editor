/*
 * Created on Aug 26, 2008
 * Copyright Motion Picture Laboratories, Inc. 2008
 * 
 * HISTORY:
 * 
 */
package com.movielabs.cmr.client.util.ui;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileChooserDialog {
	public static final String propPrefix = "fileChooser.";
	protected static Map<String, String> defaultDirMap = new HashMap<String, String>();

	/**
	 * Simple wrapper around a <tt>JFileChooser</tt> that handles housekeeping
	 * chores such as keeping track of default directories. If
	 * <tt>directoryPath</tt> is specified then that will be used as the
	 * starting point for file selection. If it is <tt>null</tt> then the
	 * <tt>key</tt> is used to look up the last directory associated with the
	 * specified key value. If one does not exist then a <tt>null</tt> value is
	 * passed to <tt>JFileChooser</tt> as the starting point (i.e., the user's
	 * home directory is to be used as the starting point).
	 * 
	 * @param promptText
	 * @param directoryPath
	 * @param filter
	 * @param key
	 * @return
	 */
	public static synchronized String getFilePath(String promptText,
			String directoryPath, FileFilter filter, Object key,
			Component parent) {
		String myPath = handleReq(promptText, directoryPath, filter, key,
				parent, false);
		return myPath;
	}

	public static synchronized String getDirPath(String promptText,
			String directoryPath, FileFilter filter, Object key,
			Component parent) {
		String myPath = handleReq(promptText, directoryPath, filter, key,
				parent, true);
		return myPath;
	}

	private static synchronized String handleReq(String promptText,
			String directoryPath, FileFilter filter, Object key,
			Component parent, boolean getDir) {
		String myPath = null;
		if (directoryPath == null) {
			directoryPath = defaultDirMap.get(propPrefix + key);
			if (directoryPath == null) {
				directoryPath = ".";
			}
		}
		JFileChooser myChooser = new JFileChooser(directoryPath);
		if (getDir) {
			myChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {
			myChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (filter != null) {
				myChooser.setFileFilter(filter);
			}
			File test = new File(directoryPath);
			/*
			 * The idea here is to show as already selected a default file name
			 * if a 'save-as' is being done. The problem is that doing a check
			 * for 'test.isFile()' will not work as a non-existent file would
			 * return FALSE. Hence the check for 'isDIrectory' instead.
			 */
			if (!test.isDirectory()) {
				myChooser.setSelectedFile(test);
			}
		}
		Action details = myChooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		myChooser.setDialogTitle(promptText);
		int retval = myChooser.showDialog(parent, "OK");
		if (retval == JFileChooser.APPROVE_OPTION) {
			File myFile = myChooser.getSelectedFile();
			myPath = myFile.getPath();
			String newDefaultDir = myChooser.getCurrentDirectory().getPath();
			defaultDirMap.put(propPrefix + key, newDefaultDir);
		}
		return myPath;
	}

	/**
	 * @return Returns the defaultDirMap.
	 */
	public static Map<String, String>  getDefaultDirMap() {
		return defaultDirMap;
	}

	/**
	 * Set the HashMap containing default directories. Typically this would be
	 * the same an application's <tt>Properties</tt> instance in which some of
	 * the properties begin with the appropriate prefix (i.e., "
	 * <tt>fileChooser.</tt>")
	 * 
	 * @param defaultDirMap
	 *            The defaultDirMap to set.
	 */
	public static synchronized void setDefaultDirMap(Map defaultDirMap) {
		FileChooserDialog.defaultDirMap = defaultDirMap;
	}
}
