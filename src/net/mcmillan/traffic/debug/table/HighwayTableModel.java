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

	// Column Definition
	private static final HighwayTableColumn<?>[] COLUMNS = new HighwayTableColumn[] {
			new DefaultHighwayTableColumn<Color>("Color", Color.class, true, (v) -> v.color, (v,o) -> v.color = o),
			new DefaultHighwayTableColumn<Double>("Speed", double.class, true, (v) -> v.speed, (v,o) -> v.speed = o),
			new DefaultHighwayTableColumn<Double>("Top Speed", double.class, true, (v) -> v.topSpeed, (v,o) -> v.topSpeed = o),
			new DefaultHighwayTableColumn<Double>("Power", double.class, true, (v) -> v.speed, (v,o) -> v.speed = o),
			new DefaultHighwayTableColumn<Double>("Brake", double.class, true, (v) -> v.brake, (v,o) -> v.brake = o),
			new DefaultHighwayTableColumn<Double>("X", double.class, true, (v) -> v.transform.x(), (v,o) -> v.transform.x(o)),
			new DefaultHighwayTableColumn<Double>("Y", double.class, true, (v) -> v.transform.y(), (v,o) -> v.transform.y(o)),
	};

	// Table Properties
	@Override public int getRowCount() { return hw.vehicles.size(); }
	@Override public int getColumnCount() { return COLUMNS.length; }
	
	// Column Properties
	@Override public String getColumnName(int c) { return COLUMNS[c].getColumnName(); }
	@Override public Class<?> getColumnClass(int c) { return COLUMNS[c].getColumnClass(); }
    @Override public boolean isCellEditable(int r, int c) { return COLUMNS[c].isCellEditable(r); }

    // Getters and Setters
	@Override
	public Object getValueAt(int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		return COLUMNS[c].getValueAt(v);
	}

    @Override
    public void setValueAt(Object o, int r, int c) {
		Vehicle v = hw.vehicles.get(r);
		COLUMNS[c].setValueAt(v, o);
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
