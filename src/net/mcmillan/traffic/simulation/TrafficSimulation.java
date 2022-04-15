package net.mcmillan.traffic.simulation;

import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class TrafficSimulation {

	private boolean running = false;
	
	public QuadtreeNode quadtree;
		
	public TrafficSimulation() {
		quadtree = QuadtreeNode.randomize(IVec2.make(1024, 1024), 8);
	}
	
	public boolean isRunning() { return running; }
	
	public void start() {
		if (running) throw new IllegalStateException("Can't start an already active simulation!");
		running = true;
	}
	
	public void stop() {
		if (!running) throw new IllegalStateException("Can't stop inactive simulation!");
		running = false;
	}
	
	private long ticks = 0;
	
	public void tick() throws CollisionException {
		if (!running) throw new IllegalStateException("Can't tick inactive simulation!");
		ticks++;
//		if (ticks > 300) throw new CollisionException();
	}
	
	public void flagCollision(CollisionException e) {
		// TODO: Impl
	}
	
}
