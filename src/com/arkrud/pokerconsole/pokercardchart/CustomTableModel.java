package com.arkrud.pokerconsole.pokercardchart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.arkrud.pokerconsole.Util.INIFilesFactory;
import com.arkrud.pokerconsole.Util.UtilMethodsFactory;

public class CustomTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
	private HashMap<String, HashMap<String, String>> iniData = new HashMap<String, HashMap<String, String>>();
	private ArrayList<String> columns = new ArrayList<String>();
	private String[][] pokerHands = { { "AA", "AKs", "AQs", "AJs", "ATs", "A9s", "A8s", "A7s", "A6s", "A5s", "A4s", "A3s", "A2s" }, { "AKo", "KK", "KQs", "KJs", "KTs", "K9s", "K8s", "K7s", "K6s", "K5s", "K4s", "K3s", "K2s" },
			{ "AQo", "KQo", "QQ", "QJs", "QTs", "Q9s", "Q8s", "Q7s", "Q6s", "Q5s", "Q4s", "Q3s", "Q2s" }, { "AJo", "KJO", "QJO", "JJ", "JTs", "J9s", "J8s", "J7s", "J6s", "J5s", "J4s", "J3s", "J2s" },
			{ "ATo", "KTo", "QTO", "JTo", "TT", "T9s", "T8s", "T7s", "T6s", "T5s", "T4s", "T3s", "T2s" }, { "A9o", "K9o", "Q9o", "J9o", "T9o", "99", "98s", "97s", "96s", "95s", "94s", "93s", "92s" },
			{ "A8o", "K8o", "Q8o", "J8o", "T8o", "98o", "88", "87s", "86s", "85s", "84s", "83s", "82s" }, { "A7o", "K7o", "Q7o", "J7o", "T7o", "97o", "87o", "77", "76s", "75s", "74s", "73s", "72s" },
			{ "A6o", "K6o", "Q6o", "J6o", "T6o", "96o", "86o", "76o", "66", "65s", "64s", "63s", "62s" }, { "A5o", "5Ko", "Q5o", "J5o", "T5o", "95o", "85o", "75o", "65o", "55", "54s", "53s", "52s" },
			{ "A4o", "K4o", "Q4o", "J4o", "T4o", "94o", "84o", "74o", "64o", "54o", "44", "43s", "42s" }, { "A3o", "K3o", "Q3o", "J3o", "T3o", "93o", "83o", "73o", "63o", "53o", "43o", "33", "32s" },
			{ "A2o", "K2o", "Q2o", "J2o", "T2o", "92o", "82o", "72o", "62o", "52o", "42o", "32o", "22" } };
	private HashMap<String, HashMap<String, String>> colorsMap;
	private boolean useINI = true;

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			return data.get(rowIndex).get(columnIndex);
		} catch (Exception e) {
			return "Undefined";
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		int x = 0;
		while (x < data.size()) {
			data.get(row).set(col, value);
			x++;
		}
	}

	public void generateTableHeaders() {
		ArrayList<String> columnHeaderData = new ArrayList<String>();
		int n = 0;
		while (n < 13) {
			columnHeaderData.add("");
			n++;
		}
		Iterator<String> it = columnHeaderData.iterator();
		while (it.hasNext()) {
			String st = it.next();
			columns.add(st);
		}
	}

	public void generateTableData(String imagePath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(UtilMethodsFactory.getConfigPath() + imagePath));
		} catch (IOException ex) {
			// handle exception...
		}
		File inifile = new File(UtilMethodsFactory.getConfigPath() + imagePath.substring(0, imagePath.length() - 3) + "ini");
		if (inifile.exists()) {
			colorsMap = INIFilesFactory.getItemValuesFromINI(inifile);
		} else {
			useINI = false;
		}
		int[] steps = { 30, 30, 30, 28, 30, 27, 27, 30, 28, 29, 28, 30, 28 };
		int xstart = -25;
		int y = 0;
		while (y < steps.length) {
			int x = 0;
			int ystart = -25;
			xstart = xstart + steps[y];
			ArrayList<Object> objects = new ArrayList<Object>();
			while (x < 13) {
				String cellText = pokerHands[y][x];
				ystart = ystart + steps[x];
				java.awt.Color color = null;
				if (useINI) {
					HashMap<String, String> itemMap = colorsMap.get(pokerHands[y][x]);
					int alpha = Integer.parseInt(itemMap.get("AlphaRGB"));
					int red = Integer.parseInt(itemMap.get("RedRGB"));
					int green = Integer.parseInt(itemMap.get("GreenRGB"));
					int blue = Integer.parseInt(itemMap.get("BlueRGB"));
					color = new java.awt.Color(red, green, blue, alpha);
				} else {
					HashMap<String, String> iniCellDta = new HashMap<String, String>();
					int clr = image.getRGB(ystart, xstart);
					int alpha = (clr >> 24) & 255;
					int red = (clr & 0x00ff0000) >> 16;
					int green = (clr & 0x0000ff00) >> 8;
					int blue = clr & 0x000000ff;
					color = new java.awt.Color(red, green, blue, alpha);
					iniCellDta.put("AlphaRGB", Integer.toString(alpha));
					iniCellDta.put("RedRGB", Integer.toString(red));
					iniCellDta.put("GreenRGB", Integer.toString(green));
					iniCellDta.put("BlueRGB", Integer.toString(blue));
					iniData.put(pokerHands[y][x], iniCellDta);
				}
				JTextField cell = new JTextField(cellText);
				cell.setBackground(color);
				objects.add(cell);
				x++;
			}
			data.add(objects);
			y++;
		}
	}

	public void adjustColumnPreferredWidths(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int col = 0; col < table.getColumnCount(); col++) {
			TableColumn column = columnModel.getColumn(col);
			column.setPreferredWidth(30);
		}
	}

	public void generateChartINIFile(String filePath) {
		File inifile = new File(UtilMethodsFactory.getConfigPath() + filePath);
		UtilMethodsFactory.createChartINIFile(inifile);
		for (Map.Entry<String, HashMap<String, String>> entry : iniData.entrySet())
			INIFilesFactory.addINIFileSection(inifile, entry.getKey(), entry.getValue());
	}
}