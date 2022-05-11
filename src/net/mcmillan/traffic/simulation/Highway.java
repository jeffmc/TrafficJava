package net.mcmillan.traffic.simulation;

import java.util.ArrayList;
import java.util.Arrays;

import net.mcmillan.traffic.debug.table.HighwayDataListener;
import net.mcmillan.traffic.debug.table.HighwaySelectionListener;
import net.mcmillan.traffic.math.DVec2;
import net.mcmillan.traffic.math.ITransform2D;
import net.mcmillan.traffic.math.IVec2;

public class Highway {
	
//	public int id = (int)(Math.random()*Integer.MAX_VALUE); // For debugging purposes
	
	public ArrayList<Vehicle> vehicles = new ArrayList<>(), selectedVehicles = new ArrayList<>();
	public IVec2 size;

//	private QuadtreeNode quadtree;
//	public QuadtreeNode getQuadtreeRoot() { return quadtree; }
	
	private ArrayList<HighwayDataListener> dataListeners = new ArrayList<>();
	private ArrayList<HighwaySelectionListener> selectionListeners = new ArrayList<>();
	
	public Highway() {
		size = IVec2.make(512, 256); // powers of 2 for the quadtree
//		quadtree = QuadtreeNode.root(size, 6);
	}
	
	public void addCar() {
		Vehicle v = new Vehicle(
				DVec2.make(
					Math.round(Math.random()/2*size.x()),
					Math.round(Math.random()*size.y())), 
				DVec2.make(30, 20));
		vehicles.add(v);
		for (HighwayDataListener l : dataListeners) {
			l.vehicleAdded(vehicles.size()-1);
		}
//		if (quadtree.testAndAdd(v)) {
//			System.out.println("[Highway] Added to qt!");
//		}
	}
	
	public void selectIndices(int[] indices) { // TODO: Make more efficient selection system that doesn't reset every single tick
		selectedVehicles.clear();
		for (Vehicle v : vehicles) v.setSelected(false);
		for (int idx : indices) {
			Vehicle v = vehicles.get(idx);
			v.setSelected(true);
			selectedVehicles.add(v);
		}
	}
	
	public void selectRect(ITransform2D mrect) {
		selectedVehicles.clear();
		int[] selected = new int[vehicles.size()];
		int j=0;
		for (int i=0;i<vehicles.size();i++) {
			Vehicle v = vehicles.get(i);
			if (v.transform.intersects(mrect)) {
				v.setSelected(true);
				selectedVehicles.add(v);
				selected[j++] = i;
			} else {
				v.setSelected(false);
			}
		}
		int[] trimmed = Arrays.copyOf(selected, j);
		for (HighwaySelectionListener l : selectionListeners)
			l.selectionChanged(trimmed);
	}
	
	public void update(long delta) {
		for (Vehicle v : vehicles) {
			v.tick(delta);
		}
		
		if (vehicles.removeIf((v) -> v.transform.x() > size.x())) {
			for (HighwayDataListener l : dataListeners)
				l.refreshDataAndStructure();
		} else {
			for (HighwayDataListener l : dataListeners)
				l.refreshData();
		}
		
	}
	
	public void addDataListener(HighwayDataListener l) { dataListeners.add(l); }
	public boolean removeDataListener(HighwayDataListener l) { return dataListeners.remove(l); }
	
	public void addSelectionListener(HighwaySelectionListener l) { selectionListeners.add(l); }
	public boolean removeSelectionListener(HighwaySelectionListener l) { return selectionListeners.remove(l); }
	
}
