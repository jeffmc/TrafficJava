package net.mcmillan.traffic.simulation;

import java.util.ArrayList;

import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class Highway {
	
	public ArrayList<Vehicle> vehicles;
	public IVec2 size;

	private QuadtreeNode quadtree;
	public QuadtreeNode getQuadtreeRoot() { return quadtree; }
	
	public Highway() {
		vehicles = new ArrayList<>();
		size = IVec2.make(1024, 256);;
		quadtree = QuadtreeNode.randomize(IVec2.make(size.x(), size.y()), 8);
	}
	
	public void addCar() {
		Vehicle v = new Vehicle(IVec2.make((int)(-Math.random()*50)-50, (int)(Math.random()*500)), IVec2.make(30, 20));
		vehicles.add(v);
		quadtree.testAndAdd(v);
	}
	
	public void update(long delta) {
		for (Vehicle v : vehicles) {
			v.tick(delta);
		}
		
		vehicles.removeIf((v) -> v.transform.x() > size.x());
	}
	
}
