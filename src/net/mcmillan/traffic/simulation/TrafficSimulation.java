package net.mcmillan.traffic.simulation;

import net.mcmillan.traffic.event.EventQueue;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class TrafficSimulation {

	private boolean running = false;
	
	private QuadtreeNode quadtree;
	
	public QuadtreeNode getQuadtreeRoot() { return quadtree; }
	
	private EventQueue eventq = new EventQueue();
		
	public EventQueue getEventQueue() { return eventq; }
	
	public TrafficSimulation() {
		quadtree = QuadtreeNode.randomize(IVec2.make(1024, 512), 8);
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
	public long ticks() { return ticks; }
	
	public void tick(long delta) {
		if (!running) throw new IllegalStateException("Can't tick inactive simulation!");
		pollEvents(); // Run this before any update code.
		update(delta);
		ticks++;
	}

	public void pollEvents() {			
		/* TODO: Parse canvas events and pass them into the renderer's camera 
		and/or other tools. */	
	}
	
	public void update(long delta) {
		
	}
	
	public void flagCollision(CollisionException e) { // TODO: Rework from an exception into a normal class.
		// TODO: Impl
	}

}
