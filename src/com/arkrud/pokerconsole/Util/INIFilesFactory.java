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

// TODO: Auto-generated Javadoc
/**
 * Static methods used to work with .ini configuration files.<br>
 *
 */
public class INIFilesFactory {
	
	/**
	 * Adds the INI file item to section.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param itemName the item name
	 * @param itemValue the item value
	 */
	// Add boolean INI item to section
	public static void addINIFileItemToSection(File iniFile, String section, String itemName, Object itemValue) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.addItem(itemName);
		iniSection.getItem(itemName).setValue(itemValue);
		writeINI(iniFile, ini);
	}

	/**
	 * Adds the INI file section.
	 *
	 * @param iniFile the ini file
	 * @param sectionName the section name
	 * @param sectionKeys the section keys
	 */
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

	/**
	 * Gets the app trees config info.
	 *
	 * @param iniFile the ini file
	 * @return the app trees config info
	 */
	// Retrieve Applications Tree info from INI configuration file
	public static ArrayList<String> getAppTreesConfigInfo(File iniFile) {
		ArrayList<String> appsInfo = new ArrayList<String>();
		IniFile ini = readINI(iniFile);
		Iterator<IniSection> sections = ini.getSections().iterator();
		while (sections.hasNext()) {
			IniSection theSection = sections.next();
			if (theSection.getName().equals("Solutions")) {
				Iterator<IniItem> params = theSection.getItems().iterator();
				while (params.hasNext()) {
					//ArrayList<Object> appInfo = new ArrayList<Object>();
					IniItem item = params.next();
					//appInfo.add(item.getName());
					//appInfo.add(Boolean.valueOf(item.getValue()));
					appsInfo.add(item.getName());
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
	 * Gets the item values from INI.
	 *
	 * @param iniFile the ini file
	 * @return the item values from INI
	 */
	public static HashMap<String, HashMap<String, String>> getItemValuesFromINI(File iniFile) {
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		IniFile ini = readINI(iniFile);
		Iterator<IniSection> its = ini.getSections().iterator();
		while (its.hasNext()) {
			IniSection section = its.next();
			Iterator<IniItem> iti = section.getItems().iterator();
			HashMap<String, String> itemsMap = new HashMap<String, String>();
			while (iti.hasNext()) {
				IniItem iniItem = iti.next();
				itemsMap.put(iniItem.getName(), iniItem.getValue());
			}
			data.put(section.getName(), itemsMap);
		}
		return data;
	}

	/**
	 * Gets the ini item names from section.
	 *
	 * @param iniFile the ini file
	 * @param secionName the secion name
	 * @return the ini item names from section
	 */
	public static String[] getIniItemNamesFromSection(File iniFile, String secionName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(secionName);
		iniSection.getItemNames();
		String[] itemNames = iniSection.getItemNames().toArray(new String[iniSection.getItemNames().size()]);
		return itemNames;
	}

	/**
	 * Gets the solution copy selection item name.
	 *
	 * @param iniFile the ini file
	 * @param solutionName the solution name
	 * @return the solution copy selection item name
	 */
	public static String getSolutionCopySelectionItemName(File iniFile, String solutionName) {
		String solutionCopySelectionItemName = "";
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection("Solutions");
		Iterator<IniItem> params = iniSection.getItems().iterator();
		while (params.hasNext()) {
			IniItem item = params.next();
			if (item.getName().contains(solutionName)) {
				if (!item.getValue().equals("true")) {
					solutionCopySelectionItemName = item.getName();
					break;
				}
			}
		}
		return solutionCopySelectionItemName;
	}

	/**
	 * Gets the trees data.
	 *
	 * @return the trees data
	 */
	public static HashMap<String, Boolean> getTreesData() {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		Iterator<IniItem> it = INIFilesFactory.getAllItemsFromSection(UtilMethodsFactory.getConsoleConfig(), "Solutions").iterator();
		while (it.hasNext()) {
			IniItem iniItem = it.next();
			if (iniItem.getValue().equals("true") || iniItem.getValue().equals("false")) {
				map.put(iniItem.getName(), Boolean.parseBoolean(iniItem.getValue()));
			}
		}
		return map;
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

	/**
	 * Checks for item in section.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param itemName the item name
	 * @return true, if successful
	 */
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

	/**
	 * Removes the INI file item.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param itemName the item name
	 */
	public static void removeINIFileItem(File iniFile, String section, String itemName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.removeItem(itemName);
		writeINI(iniFile, ini);
	}

	/**
	 * Removes the INI file items.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param itemNames the item names
	 */
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

	/**
	 * Removes the INI file items with pattern.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param pattern the pattern
	 */
	public static void removeINIFileItemsWithPattern(File iniFile, String section, String pattern) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		Iterator<String> it = iniSection.getItemNames().iterator();
		while (it.hasNext()) {
			String itemName = it.next();
			if (itemName.contains(pattern)) {
			iniSection.removeItem(itemName);
			}
		}
		writeINI(iniFile, ini);
	}

	/**
	 * Removes the INI file section.
	 *
	 * @param iniFile the ini file
	 * @param sectionName the section name
	 */
	// Remove section from INI file
	public static void removeINIFileSection(File iniFile, String sectionName) {
		IniFile ini = readINI(iniFile);
		ini.removeSection(sectionName);
		writeINI(iniFile, ini);
	}

	/**
	 * Update INI file item name.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param newItemName the new item name
	 * @param itemName the item name
	 */
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

	/**
	 * Update INI file item.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param newItemValue the new item value
	 * @param itemName the item name
	 */
	// Update INI file items in section
	public static void updateINIFileItem(File iniFile, String section, String newItemValue, String itemName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		iniSection.getItem(itemName).setValue(newItemValue);
		writeINI(iniFile, ini);
	}

	/**
	 * Update INI file items.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param newItemValue the new item value
	 * @param itemNames the item names
	 */
	// Update INI file items in section
	public static void updateINIFileItems(File iniFile, String section, String newItemValue, String[] itemNames) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(section);
		int x = 0;
		while (x < itemNames.length) {
			iniSection.getItem(itemNames[x]).setValue(newItemValue);
			x++;
		}
		writeINI(iniFile, ini);
	}

	/**
	 * Update all INI file items in section.
	 *
	 * @param iniFile the ini file
	 * @param section the section
	 * @param newItemsValue the new items value
	 */
	// Update INI file items in section
		public static void updateAllINIFileItemsInSection(File iniFile, String section, String newItemsValue) {
			IniFile ini = readINI(iniFile);
			IniSection iniSection = ini.getSection(section);
			Iterator<IniItem> it = iniSection.getItems().iterator();
			while (it.hasNext()) {
				IniItem iniItem = it.next();
				iniItem.setValue(newItemsValue);
			}
			writeINI(iniFile, ini);
		}

	/**
	 * Gets the all items from section.
	 *
	 * @param iniFile the ini file
	 * @param iniSectionName the ini section name
	 * @return the all items from section
	 */
	private static Collection<IniItem> getAllItemsFromSection(File iniFile, String iniSectionName) {
		IniFile ini = readINI(iniFile);
		IniSection iniSection = ini.getSection(iniSectionName);
		return iniSection.getItems();
	}

	/**
	 * Write INI.
	 *
	 * @param iniFile the ini file
	 * @param ini the ini
	 */
	// Write to INI
	private static void writeINI(File iniFile, IniFile ini) {
		IniFileWriter writer = new IniFileWriter(ini, iniFile);
		try {
			writer.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
