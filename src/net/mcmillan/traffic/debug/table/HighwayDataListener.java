package net.mcmillan.traffic.debug.table;

public interface HighwayDataListener {

	public void vehicleAdded(int idx);
	public void vehicleRemoved(int idx);
	
	public void refreshDataAndStructure(); //fireTableStructureChanged: Invalidate entire table, both data and structure
	public void refreshData();
	
}
