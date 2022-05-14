package net.mcmillan.traffic.debug.table;

import net.mcmillan.traffic.simulation.Vehicle;

public interface HighwayTableColumn<T> {
	
	public String getColumnName();
	public Class<?> getColumnClass();
	public T getValueAt(Vehicle v);
	public boolean isCellEditable(int row);
	public void setValueAt(Vehicle v, Object d);
	
}
