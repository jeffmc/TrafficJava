package net.mcmillan.traffic.debug.table;

import javax.swing.table.TableCellRenderer;

import net.mcmillan.traffic.simulation.Vehicle;

public interface HighwayTableColumn<T> {
	
	public String getColumnName();
	public Class<?> getColumnClass();
	public T getValueAt(Vehicle v);
	public boolean isCellEditable(int row);
	public void setValueAt(Vehicle v, Object d);
	public TableCellRenderer getCustomCellRenderer();
	
}
