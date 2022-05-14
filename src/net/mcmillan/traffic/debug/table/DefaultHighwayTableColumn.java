package net.mcmillan.traffic.debug.table;

import javax.swing.table.TableCellRenderer;

import net.mcmillan.traffic.simulation.Vehicle;

public class DefaultHighwayTableColumn<T> implements HighwayTableColumn<T> {
	
	public DefaultHighwayTableColumn(String name, Class<T> clazz, boolean editable, VehicleGetter<T> getter, VehicleSetter<T> setter) {
		this(name,clazz,editable,getter,setter,null);
	}
	
	public DefaultHighwayTableColumn(String name, Class<T> clazz, boolean editable, VehicleGetter<T> getter, VehicleSetter<T> setter, TableCellRenderer renderer) {
		this.name = name;
		this.type = clazz;
		this.editable = editable;
		this.getter = getter;
		this.setter = setter;
		this.renderer = renderer;
	}
	
	// Column properties
	public final String name;
	public final Class<T> type;
	public final boolean editable;
	
	@Override public String getColumnName() { return name; }
	@Override public Class<?> getColumnClass() { return type; }
	@Override public boolean isCellEditable(int row) { return editable; }
	
	// Getters and Setters
	public final VehicleGetter<T> getter;
	public final VehicleSetter<T> setter;
	
	@Override public T getValueAt(Vehicle v) { return getter.get(v); }
	public interface VehicleGetter<T> { T get(Vehicle v); }
	@Override public void setValueAt(Vehicle v, Object o) { setter.set(v, type.cast(o)); }
	public interface VehicleSetter<T> { void set(Vehicle v, T o); }
	
	// Custom Cell Renderer
	public final TableCellRenderer renderer;
	@Override public TableCellRenderer getCustomCellRenderer() { return renderer; }
	
}
