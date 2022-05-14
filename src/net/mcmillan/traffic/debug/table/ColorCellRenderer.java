package net.mcmillan.traffic.debug.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer implements TableCellRenderer {

	JLabel label = new JLabel();
	
	public ColorCellRenderer() {
		label.setOpaque(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object v,
            boolean isSelected, boolean hasFocus, int row, int column) {
		// Set color and tooltip
		Color col = (Color) v;
		label.setBackground(col);
        label.setToolTipText("RGB: " + col.getRed() + ", "
                + col.getGreen() + ", "
                + col.getBlue());

        // Focus border
        Border b = null;
        if (hasFocus) {
        	float[] hsb = new float[3];
        	Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), hsb);
//    		b = BorderFactory.createLineBorder(hsb[2]>0.5?col.darker():col.brighter(), 1);
    		b = BorderFactory.createLineBorder(hsb[2]>0.5?Color.BLACK:Color.WHITE, 1);
        }
        label.setBorder(b);
		return label;
	}
}
