package net.mcmillan.traffic.debug.table;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class HighwayTable implements HighwaySelectionListener {

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
			table.setDefaultRenderer(Color.class, new ColorCellRenderer());
			HighwayTableModel model = new HighwayTableModel(sim.highway);
			table.setModel(model);
			sim.highway.addSelectionListener(this);
			ListSelectionModel selModel = table.getSelectionModel();
			selModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					sim.highway.selectIndices(selModel.getSelectedIndices()); // TODO: Fix this code, make reactionary rather than rebuilding array every tick from scratch.
				}
			});
		} else {
			table.setModel(nullModel);
		}
	}

	@Override
	public void selectionChanged(int[] selected) {
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.clearSelection();
		if (selected.length < 2) {
			if (selected.length == 1) {
				selModel.setSelectionInterval(selected[0], selected[0]);
			}
			return;
		}
		for (int i = 0; i < selected.length; i++) {
			selModel.addSelectionInterval(selected[i], selected[i]); // TODO: Group consecutive intervals and add as groups.
		}
	}
	
}
