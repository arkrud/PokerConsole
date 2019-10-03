package com.arkrud.pokerconsole.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.FormatException;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

/**
 * Static methods used to work with .ini configuration files.<br>
 *
 */
public class INIFilesFactory {
	// Add boolean INI item to section
	public static void addINIFileItemToSection(File iniFile, String section, String itemName, Object itemValue) {
		System.out.println(itemValue);
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.addItem(itemName);
		iniSection.getItem(itemName).setValue(itemValue);
		writeINI(iniFile, ini);
	}

	// Add new section to INI configuration file
	public static void addINIFileSection(File iniFile, String sectionName, HashMap<String, String> sectionKeys) {
		IniFile ini = readINI(iniFile);
		ini.addSection(sectionName);
		IniSection awsaccountSection = ini.getSection(sectionName);
		Set<String> keys = sectionKeys.keySet();
		for (String key : keys) {
			awsaccountSection.addItem(key);
			awsaccountSection.getItem(key).setValue(sectionKeys.get(key));
		}
		writeINI(iniFile, ini);
	}

	public static Collection<IniItem> getAllItemsFromSection(File iniFile, String iniSectionName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(iniSectionName);
		return iniSection.getItems();
	}

	// Retrieve Applications Tree info from INI configuration file
	public static ArrayList<ArrayList<Object>> getAppTreesConfigInfo(File iniFile) {
		ArrayList<ArrayList<Object>> appsInfo = new ArrayList<ArrayList<Object>>();
		IniFile ini = readINI(iniFile);
		Iterator<IniSection> sections = ini.getSections().iterator();
		while (sections.hasNext()) {
			IniSection theSection = sections.next();
			if (theSection.getName().equals("Applications")) {
				Iterator<IniItem> params = theSection.getItems().iterator();
				while (params.hasNext()) {
					ArrayList<Object> appInfo = new ArrayList<Object>();
					IniItem item = params.next();
					appInfo.add(item.getName());
					appInfo.add(Boolean.valueOf(item.getValue()));
					appsInfo.add(appInfo);
				}
			}
		}
		return appsInfo;
	}

	/**
	 * Get item value from INI. <br>
	 *
	 * @param iniFile INI File object
	 * @param iniSectionName INI file section name
	 * @param itemName INI file item name
	 * @return value of the parameter in INI file
	 */
	public static String getItemValueFromINI(File iniFile, String iniSectionName, String itemName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(iniSectionName);
		return iniSection.getItem(itemName).getValue();
	}

	/**
	 * Check if section exist in INI. <br>
	 *
	 * @param iniFile INI File object
	 * @param sectionName INI file section name
	 *
	 * @return <code>true</code> is specific section present in INI file <code>false</code> if section is not present in INI file
	 */
	public static boolean hasINIFileSection(File iniFile, String sectionName) {
		IniFile ini = readINI(iniFile);
		if (ini.getSection(sectionName) == null) {
			return false;
		}
		return true;
	}

	// Check if item is present in INI file section
	public static boolean hasItemInSection(File iniFile, String section, String itemName) {
		boolean itemExist = false;
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		if (iniSection.hasItem(itemName)) {
			itemExist = true;
		}
		return itemExist;
	}

	/**
	 * Read INI file. <br>
	 *
	 * @param file INI File object
	 * @return <code>IniFile</code> object
	 */
	public static IniFile readINI(File file) {
		IniFile ini = new BasicIniFile();
		IniFileReader reader = new IniFileReader(ini, file);
		try {
			reader.read();
		} catch (FormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ini;
	}

	// Remove section from INI file
	public static void removeINIFileSection(File iniFile, String sectionName) {
		IniFile ini = readINI(iniFile);
		ini.removeSection(sectionName);
		writeINI(iniFile, ini);
	}

	public static void removeINIFileItems(File iniFile, String section, String[] itemNames) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		int x = 0;
		while (x < itemNames.length) {
			iniSection.removeItem(itemNames[x]);
			x++;
		}
		writeINI(iniFile, ini);
	}

	public static void removeINIItemsWithValues(File iniFile, String section, String[] itemValues) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		Iterator<IniItem> it = iniSection.getItems().iterator();
		while (it.hasNext()) {
			IniItem iniItem = (IniItem) it.next();
			int x = 0;
			while (x < itemValues.length) {
				if (iniItem.getValue().equals(itemValues[x])) {
					iniSection.removeItem(iniItem);
				}
				x++;
			}
		}
		writeINI(iniFile, ini);
	}

	public static void removeINIFileItemsByPrefix(File iniFile, String section, String prefix) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		Iterator<String> it = iniSection.getItemNames().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (name.startsWith(prefix)) {
				iniSection.removeItem(name);
			}
		}
		writeINI(iniFile, ini);
	}

	public static void removeINIFileItemsByPatternInItemValues(File iniFile, String section, String pattern) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		Iterator<IniItem> it = iniSection.getItems().iterator();
		while (it.hasNext()) {
			IniItem item = it.next();
			String value = item.getValue();
			if (value.contains(pattern)) {
				iniSection.removeItem(item.getName());
			}
		}
		writeINI(iniFile, ini);
	}

	// Update INI file items in section
	public static void updateINIFileItemName(File iniFile, String section, String newItemName, String itemName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.removeItem(itemName);
		IniItem item = new IniItem(newItemName);
		item.setValue(true);
		iniSection.addItem(item);
		writeINI(iniFile, ini);
	}

	// Update INI file items in section
	public static void updateINIFileItems(File iniFile, String section, String newItemValue, String itemName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.getItem(itemName).setValue(newItemValue);
		writeINI(iniFile, ini);
	}

	// Write to INI
	private static void writeINI(File iniFile, IniFile ini) {
		IniFileWriter writer = new IniFileWriter(ini, iniFile);
		try {
			writer.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Boolean> getTreesData() {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		Iterator<IniItem> it = INIFilesFactory.getAllItemsFromSection(UtilMethodsFactory.getConsoleConfig(), "Applications").iterator();
		while (it.hasNext()) {
			IniItem iniItem = (IniItem) it.next();
			if (iniItem.getValue().equals("true") || iniItem.getValue().equals("false")) {
				map.put(iniItem.getName(), Boolean.parseBoolean(iniItem.getValue()));
			}
		}
		return map;
	}
	
	public static HashMap<String, HashMap<String, String>> getItemValuesFromINI(File iniFile) {
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		IniFile ini = readINI(iniFile);
		Iterator<IniSection> its = ini.getSections().iterator();
		while (its.hasNext()) {
			IniSection section = (IniSection) its.next();
			Iterator<IniItem> iti = section.getItems().iterator();
			HashMap<String, String> itemsMap = new HashMap<String, String>();
			while (iti.hasNext()) {
				IniItem iniItem = (IniItem) iti.next();
				itemsMap.put(iniItem.getName(), iniItem.getValue());
			}
			data.put(section.getName(), itemsMap);
		}
		return data;
	}
}
