package net.mcmillan.traffic.debug.table;

import java.awt.Color;

import javax.swing.table.AbstractTableModel;

import net.mcmillan.traffic.simulation.Highway;
import net.mcmillan.traffic.simulation.Vehicle;

public class HighwayTableModel extends AbstractTableModel implements HighwayDataListener { // TODO: Make better model
	
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

	private static final String[] COLUMNS = new String[] { "Color", "Speed", "Max Speed", "Power", "Braking", "X", "Y" }; // TODO: lane
    public String getColumnName(int c) { return COLUMNS[c]; }
	@Override
    public Class<?> getColumnClass(int c) {
		switch (c) {
		case 0: return Color.class; // Color
		case 1: return double.class; // Speed
		case 2: return double.class; // Max
		case 3: return double.class; // Power
		case 4: return double.class; // Braking
		case 5: return double.class; // X
		case 6: return double.class; // Y
		default: throw new IllegalArgumentException("Invalid column index: " + c);
		}
    }
	@Override
	public Object getValueAt(int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		switch (c) {
		case 0: return v.color;
		case 1: return v.speed;
		case 2: return v.topSpeed;
		case 3: return v.power;
		case 4: return v.brake;
		case 5: return v.transform.x();
		case 6: return v.transform.y();
		default: throw new IllegalArgumentException("Invalid column index: " + c);
		}
	}

    @Override
    public boolean isCellEditable(int r, int c) {
		switch (c) {
		case 0: return false;
		case 1: return true;
		case 2: return true;
		case 3: return true;
		case 4: return true;
		case 5: return true;
		case 6: return true;
		default: throw new IllegalArgumentException("Invalid column index: " + c);
		}
    }

    @Override
    public void setValueAt(Object o, int r, int c) {
    	Vehicle v = hw.vehicles.get(r);
    	switch (c) {
		case 0: throw new IllegalArgumentException("Can't edit color (yet)");
		case 1: v.speed = (double) o;
		case 2: v.topSpeed = (double) o;
		case 3: v.power = (double) o;
		case 4: v.brake = (double) o;
		case 5: v.transform.x((double) o);
		case 6: v.transform.y((double) o);
		default: throw new IllegalArgumentException("Invalid column index: " + c);
    	}
    }
    
	@Override
	public void vehicleAdded(int idx) {
		fireTableRowsInserted(idx, idx);
	}
	
	@Override
	public void vehicleRemoved(int idx) {
		fireTableRowsInserted(idx, idx);
	}

	@Override
	public void refreshDataAndStructure() {
		fireTableStructureChanged();
	}

	@Override
	public void refreshData() {
		fireTableDataChanged();
	}
	
}
