package net.mcmillan.traffic.debug;

public interface HighwayDataListener {

	public void vehicleAdded(int idx);
	public void vehicleRemoved(int idx);
	
	public void refreshEverything(); //fireTableStructureChanged: Invalidate entire table, both data and structure
	
}
