package com.arkrud.pokerconsole.pokercardchart;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomTableCellRenderer.
 */
public class CustomTableCellRenderer extends JLabel implements TableCellRenderer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new custom table cell renderer.
	 */
	public CustomTableCellRenderer() {
		// MUST do this for background to show up
		setOpaque(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object columnObject, boolean isSelected, boolean hasFocus, int row, int column) {
		Font font = this.getFont();
		setFont(font.deriveFont(Font.BOLD));
		setHorizontalAlignment(JLabel.CENTER);
		setText(((JTextField) columnObject).getText());
		if (table.isRowSelected(row) && table.isColumnSelected(column)) {
			setBackground(((CustomTable) table).getCurrentSelectionColor());
			((JTextField) table.getModel().getValueAt(row, column)).setBackground(((CustomTable) table).getCurrentSelectionColor());
		} else {
			Color c = ((JTextField) table.getModel().getValueAt(row, column)).getBackground();
			if (c.getRed() > 236 && c.getGreen() > 236 && c.getBlue() > 236 && c.getRed() + c.getGreen() + c.getBlue() < 750) {
				setFont(font.deriveFont(Font.ITALIC));
				setBorder(javax.swing.BorderFactory.createEmptyBorder());
			} else {
				setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
			}
			setBackground(c);
		}
		return this;
	}
}
