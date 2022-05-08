package net.mcmillan.traffic.debug;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class HighwayTable {

	private TableModel nullModel = new AbstractTableModel() {
		@Override public Object getValueAt(int rowIndex, int columnIndex) { return null; }
		@Override public int getRowCount() { return 0; }
		@Override public int getColumnCount() { return 0; }
	};
	
	private JTable table = new JTable();
	public JTable getTable() { return table; }
	private JScrollPane scrollPane;
	public JScrollPane getComponent() { return scrollPane; }
	
	private TrafficSimulation sim;
	
	public HighwayTable() {
		table = new JTable();
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	}
	
	public void setSimulation(TrafficSimulation s) {
		sim = s;
		if (sim != null) {
			table.setModel(new HighwayTableModel(sim.highway));
		} else {
			table.setModel(nullModel);
		}
	}
	
}
