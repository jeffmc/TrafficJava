package net.mcmillan.traffic.simulation;

import java.util.ArrayList;

import net.mcmillan.traffic.debug.HighwayDataListener;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class Highway {
	
//	public int id = (int)(Math.random()*Integer.MAX_VALUE); // For debugging purposes
	
	public ArrayList<Vehicle> vehicles;
	public IVec2 size;

	private QuadtreeNode quadtree;
	public QuadtreeNode getQuadtreeRoot() { return quadtree; }
	
	private ArrayList<HighwayDataListener> dataListeners;
	
	public Highway() {
		vehicles = new ArrayList<>();
		dataListeners = new ArrayList<>();
		size = IVec2.make(512, 256); // powers of 2 for the quadtree
		quadtree = QuadtreeNode.root(size, 6);
	}
	
	public void addCar() {
		Vehicle v = new Vehicle(IVec2.make((int)(Math.random()/2*size.x()), (int)(Math.random()*size.y())), IVec2.make(30, 20));
		vehicles.add(v);
		for (HighwayDataListener l : dataListeners) {
			l.vehicleAdded(vehicles.size()-1);
		}
		if (quadtree.testAndAdd(v)) {
			System.out.println("[Highway] Added to qt!");
		}
	}
	
	public void update(long delta) {
		for (Vehicle v : vehicles) {
			v.tick(delta);
		}
		
		if (vehicles.removeIf((v) -> v.transform.x() > size.x())) {
			for (HighwayDataListener l : dataListeners)
				l.refreshEverything();
		};
	}
	
	public void addDataListener(HighwayDataListener l) {
		dataListeners.add(l);
//		System.out.println("[Highway" + this.id + "] Added data listener: " + dataListeners.size());
	}
	
	public boolean removeDataListener(HighwayDataListener l) {
		return dataListeners.remove(l);
	}
	
}
