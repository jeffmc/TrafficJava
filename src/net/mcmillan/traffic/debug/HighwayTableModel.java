package net.mcmillan.traffic.debug;

import javax.swing.table.AbstractTableModel;

import net.mcmillan.traffic.simulation.Highway;
import net.mcmillan.traffic.simulation.Vehicle;

public class HighwayTableModel extends AbstractTableModel implements HighwayDataListener {
	
	private Highway hw;
	public HighwayTableModel(Highway h) { 
		hw = h;
		hw.addDataListener(this);
	}
	public void dispose() {
		hw.removeDataListener(this);
		hw = null;
	}
	@Override public int getRowCount() { return hw.vehicles.size(); }
	@Override public int getColumnCount() { return COLUMNS.length; }

	private static final String[] COLUMNS = new String[] { "Color", "Speed", "Max Speed", "Power", "Braking" }; // TODO: x, y, lane
    public String getColumnName(int c) { return COLUMNS[c]; }
	@Override
    public Class<?> getColumnClass(int c) {
		switch (c) {
		case 0: return String.class;
		case 1: return int.class;
		case 2: return int.class;
		case 3: return double.class;
		case 4: return double.class;
		default: throw new IllegalArgumentException("Invalid column index: " + c);
		}
    }
	@Override
	public Object getValueAt(int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		switch (c) {
		case 0: return v.color.toString();
		case 1: return v.speed;
		case 2: return v.topSpeed;
		case 3: return v.power;
		case 4: return v.brake;
		default: throw new IllegalArgumentException("Invalid column index: " + c);
		}
	}
	
	@Override
	public void vehicleAdded(int idx) {
		fireTableRowsInserted(idx, idx);
	}
}
