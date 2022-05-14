package net.mcmillan.traffic.debug.table;

import net.mcmillan.traffic.simulation.Vehicle;

public class DefaultHighwayTableColumn<T> implements HighwayTableColumn<T> {

	public final String name;
	public final Class<T> type;
	public final boolean editable;
	public final VehicleGetter<T> getter;
	public final VehicleSetter<T> setter;
	
	public DefaultHighwayTableColumn(String name, Class<T> clazz, boolean editable, VehicleGetter<T> getter, VehicleSetter<T> setter) {
		this.name = name;
		this.type = clazz;
		this.editable = editable;
		this.getter = getter;
		this.setter = setter;
	}
	
	// Column properties
	@Override public String getColumnName() { return name; }
	@Override public Class<?> getColumnClass() { return type; }
	@Override public boolean isCellEditable(int row) { return editable; }
	
	// Getters and Setters
	@Override public T getValueAt(Vehicle v) { return getter.get(v); }
	public interface VehicleGetter<T> { T get(Vehicle v); }
	@Override public void setValueAt(Vehicle v, Object o) { setter.set(v, type.cast(o)); }
	public interface VehicleSetter<T> { void set(Vehicle v, T o); }
}
