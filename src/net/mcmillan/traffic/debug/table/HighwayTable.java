package net.mcmillan.traffic.debug.table;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class HighwayTable implements HighwaySelectionListener {

	// Column Definition
	private static final HighwayTableColumn<?>[] COLUMNS = new HighwayTableColumn[] {
			new DefaultHighwayTableColumn<Color>("Color", Color.class, true, (v) -> v.color, (v,o) -> v.color = o),
			new DefaultHighwayTableColumn<Double>("Speed", double.class, true, (v) -> v.speed, (v,o) -> v.speed = o),
			new DefaultHighwayTableColumn<Double>("Acceleration", double.class, true, (v) -> v.acceleration, (v,o) -> v.acceleration = o,
					new SignedDoubleRenderer()),
			new DefaultHighwayTableColumn<Double>("Top Speed", double.class, true, (v) -> v.topSpeed, (v,o) -> v.topSpeed = o),
			new DefaultHighwayTableColumn<Double>("Power", double.class, true, (v) -> v.speed, (v,o) -> v.speed = o),
			new DefaultHighwayTableColumn<Double>("Brake", double.class, true, (v) -> v.brake, (v,o) -> v.brake = o),
			new DefaultHighwayTableColumn<Double>("X", double.class, true, (v) -> v.transform.x(), (v,o) -> v.transform.x(o)),
			new DefaultHighwayTableColumn<Double>("Y", double.class, true, (v) -> v.transform.y(), (v,o) -> v.transform.y(o)),
	};
	
	// Null model if sim hasn't been assigned
	private TableModel nullModel = new AbstractTableModel() {
		@Override public Object getValueAt(int rowIndex, int columnIndex) { return null; }
		@Override public int getRowCount() { return 0; }
		@Override public int getColumnCount() { return 0; }
	};
	
	// Components
	private JTable table = new JTable();
	public JTable getTable() { return table; }
	private JScrollPane scrollPane;
	public JScrollPane getComponent() { return scrollPane; }
	
	// Target
	private TrafficSimulation sim;
	
	public HighwayTable() {
		table = new JTable();
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	}
	
	public void setSimulation(TrafficSimulation s) {
		sim = s;
		if (sim != null) {
			// Assign model to table
			HighwayTableModel model = new HighwayTableModel(sim.highway, COLUMNS);
			table.setModel(model);

			// Cell Rendering
			table.setDefaultRenderer(Color.class, new ColorCellRenderer());
			TableColumnModel cm = table.getColumnModel();
			for (int i=0;i<COLUMNS.length;i++) {
				TableCellRenderer r = COLUMNS[i].getCustomCellRenderer();
				if (r != null) cm.getColumn(i).setCellRenderer(r);;
			}
			
			// Data and Selection Listeners
			sim.highway.addSelectionListener(this);
			ListSelectionModel selModel = table.getSelectionModel();
			// TODO: Fix this code, make reactionary rather than rebuilding array every tick from scratch
			selModel.addListSelectionListener(
				(e) -> sim.highway.selectIndices(selModel.getSelectedIndices())); 
			
		} else {
			table.setModel(nullModel);
		}
	}

	@Override
	public void selectionChanged(int[] selected) {
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.clearSelection();
		for (int i = 0; i < selected.length; i++) {
			selModel.addSelectionInterval(selected[i], selected[i]); // TODO: Group consecutive intervals and add as groups.
		}
	}
	
}
