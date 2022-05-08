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
		size = IVec2.make(1024, 256);;
		quadtree = QuadtreeNode.randomize(IVec2.make(size.x(), size.y()), 8);
	}
	
	public void addCar() {
		Vehicle v = new Vehicle(IVec2.make((int)(-Math.random()*50)-50, (int)(Math.random()*500)), IVec2.make(30, 20));
		vehicles.add(v);
//		System.out.println("[Highway" + this.id + "] Added car: " + vehicles.size() + ", listeneres: " + dataListeners.size());
		for (HighwayDataListener l : dataListeners) {
			l.vehicleAdded(vehicles.size()-1);
//			System.out.println("[Highway" + this.id + "] Triggered listener");
		}
		quadtree.testAndAdd(v);
	}
	
	public void update(long delta) {
		for (Vehicle v : vehicles) {
			v.tick(delta);
		}
		
		vehicles.removeIf((v) -> v.transform.x() > size.x());
	}
	
	public void addDataListener(HighwayDataListener l) {
		dataListeners.add(l);
//		System.out.println("[Highway" + this.id + "] Added data listener: " + dataListeners.size());
	}
	
	public boolean removeDataListener(HighwayDataListener l) {
		return dataListeners.remove(l);
	}
	
}
