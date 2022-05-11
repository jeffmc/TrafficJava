package net.mcmillan.traffic.debug.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer implements TableCellRenderer {

	JColorLabel jcl = new JColorLabel(null);
	
	public Component getTableCellRendererComponent(JTable table, Object v,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
		Color col = (Color) v;
		jcl.c = col;
		return jcl;
	}
}
