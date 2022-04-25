package net.mcmillan.traffic.simulation;

import net.mcmillan.traffic.event.Event;
import net.mcmillan.traffic.event.EventQueue;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class TrafficSimulation {

	private boolean running = false;
	
	private QuadtreeNode quadtree;
	
	public QuadtreeNode getQuadtreeRoot() { return quadtree; }
	
	private EventQueue eventq = new EventQueue();
		
	public EventQueue getEventQueue() { return eventq; }
	
	public IVec2 mstart = IVec2.make(), mnow = IVec2.make(), msize = IVec2.make(), mtl = IVec2.make();
	
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
		while (!eventq.isEmpty()) {
			Event e = eventq.pop();
			switch (e.code) {
			case Event.MOUSE_PRESSED:
				switch (e.button()) {
				case Event.BUTTON1:
					mstart.set(e.x(), e.y());
					mnow.set(mstart);
					break;
				case Event.BUTTON2:
					System.out.println(e.x() + ", " + e.y());
					break;
				}
				break;
			case Event.MOUSE_RELEASED:
//				switch (e.button()) {
//				case Event.BUTTON2:
//					System.out.println(e.x() + ", " + e.y());
//					break;
//				}
				break;
			case Event.MOUSE_DRAGGED:
				mnow.set(e.x(), e.y());
				switch (e.button()) {
				case Event.BUTTON2:
					System.out.println(e.x() + ", " + e.y());
					break;
				}
				break;
			}
		}
	}
	
	public void update(long delta) {
		msize.set(mstart).sub(mnow).abs();
		mtl.set(mstart).min(mnow);
	}
	
	public IVec2[] getMouseRect() {
		return new IVec2[] { mtl, msize };
	}

	// TODO: Implement flagging collisions.

}
