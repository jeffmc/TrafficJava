package net.mcmillan.traffic.debug.table;

import javax.swing.table.AbstractTableModel;

import net.mcmillan.traffic.simulation.Highway;
import net.mcmillan.traffic.simulation.Vehicle;

public class HighwayTableModel extends AbstractTableModel implements HighwayDataListener { // TODO: Make better model
	
	private Highway hw;
	private HighwayTableColumn<?>[] cols;
	
	public HighwayTableModel(Highway h, HighwayTableColumn<?>[] cols) { 
		hw = h;
		this.cols = cols;
		hw.addDataListener(this);
	}
	public void dispose() {
		hw.removeDataListener(this);
		hw = null;
	}

	// Table Properties
	@Override public int getRowCount() { return hw.vehicles.size(); }
	@Override public int getColumnCount() { return cols.length; }
	
	// Column Properties
	@Override public String getColumnName(int c) { return cols[c].getColumnName(); }
	@Override public Class<?> getColumnClass(int c) { return cols[c].getColumnClass(); }
    @Override public boolean isCellEditable(int r, int c) { return cols[c].isCellEditable(r); }
    
    // Getters and Setters
	@Override
	public Object getValueAt(int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		return cols[c].getValueAt(v);
	}

    @Override
    public void setValueAt(Object o, int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		cols[c].setValueAt(v, o);
    }
    
    // Highway Data Listener
	@Override public void vehicleAdded(int idx) { fireTableRowsInserted(idx, idx); }
	@Override public void vehicleRemoved(int idx) { fireTableRowsInserted(idx, idx); }
	@Override public void refreshDataAndStructure() { fireTableStructureChanged(); }
	@Override public void refreshData() { fireTableDataChanged(); }
	
}
