package net.mcmillan.traffic.debug.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class SignedDoubleRenderer implements TableCellRenderer {

	private static Color BRAKING = new Color(224,0,0),
			ACCELERATING = new Color(0,224,0);
	
	private JLabel label = new JLabel("NULL");
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object v, 
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		double n = (double) v;
		label.setFont(table.getFont());
		label.setText(Double.toString(n));
		label.setOpaque(isSelected);
		label.setBackground(isSelected?table.getSelectionBackground():null); // null is okay, because opaque is false
		label.setForeground(n > 0 ? ACCELERATING:(n < 0 ? BRAKING:table.getForeground()));
        
		// Focus border
        Border b = null;
        if (hasFocus) {
    		b = BorderFactory.createLineBorder(Color.BLACK, 1); // TODO: Fine tune this to make it seamless with default text cell renderer.
        }
        label.setBorder(b);
        
		return label;
	}

}
