package net.mcmillan.traffic.simulation;

import net.mcmillan.traffic.event.Event;
import net.mcmillan.traffic.event.EventQueue;
import net.mcmillan.traffic.gfx.Camera;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.physics.QuadtreeNode;

public class TrafficSimulation {

	// State
	private boolean running = false;
	private boolean paused = false;
	private boolean stepOnce = false;
	public void pause() { paused = true; }
	public void play() { paused = false; }
	public void step() { stepOnce = true; }
	
	private EventQueue eventq = new EventQueue();
	public EventQueue getEventQueue() { return eventq; }
	
	public IVec2 mstart = IVec2.make(), mnow = IVec2.make(), msize = IVec2.make(), mtl = IVec2.make();
	
	private Camera cam = new Camera();
	public Camera getCamera() { return cam; }
	
	public Highway highway;
	public QuadtreeNode getQuadtreeRoot() { return highway.getQuadtreeRoot(); }
	
	public TrafficSimulation() {
		highway = new Highway();
	}
	
	public boolean isRunning() { return running; }
	public boolean isPaused() { return paused; }
	
	public void start() {
		if (running) throw new IllegalStateException("Can't start an already active simulation!");
		running = true;
		highway = new Highway();
		for (int i=0;i<5;i++) {
			addCar();
		}
	}
	
	public void addCar() {
		highway.addCar();
	}
	
	public void stop() {
		if (!running) throw new IllegalStateException("Can't stop inactive simulation!");
		running = false;
		highway = null;
	}
	
	private long ticks = 0;
	public long ticks() { return ticks; }
	
	public void tick(long delta) {
		if (!running) throw new IllegalStateException("Can't tick inactive simulation!");
		pollEvents(); // Run this before any update code.
		if ((!paused) || stepOnce) {
			update(delta);
			ticks++;
			stepOnce = false;
		}
	}

	private int dragMode = -1;
	private static final int SELECT_MODE = 1;
	private static final int CAM_MODE = 0;
	
	private int cox, coy, csx, csy;
	
	public void pollEvents() {
		eventq.unload();
		while (!eventq.unloadedEmpty()) {
			Event e = eventq.pop();
			switch (e.code) {
			case Event.MOUSE_PRESSED:
				switch (e.button()) {
				case Event.BUTTON1:
					dragMode = SELECT_MODE;
//					mstart.set((e.x()-cam.x)/cam.z, (e.y()-cam.y)/cam.z);
					mstart.set(e.x()-cam.x, e.y()-cam.y);
					mnow.set(mstart);
					break;
				case Event.BUTTON2:
					dragMode = CAM_MODE;
					cox = cam.x;
					coy = cam.y;
					csx = e.x();
					csy = e.y();
					break;
				}
				break;
			case Event.MOUSE_RELEASED:
				dragMode = -1;
//				switch (e.button()) {
//				case Event.BUTTON2:
//					System.out.println(e.x() + ", " + e.y());
//					break;
//				}
				break;
			case Event.MOUSE_DRAGGED:
				switch (dragMode) {
				case SELECT_MODE:
//					mnow.set((e.x()-cam.x)/cam.z, (e.y()-cam.y)/cam.z);
					mnow.set(e.x()-cam.x, e.y()-cam.y);
					break;
				case CAM_MODE:
					cam.x = cox - csx + e.x();
					cam.y = coy - csy + e.y();
					break;
				}
				break;
			case Event.MOUSE_WHEEL_MOVED:
				break;
			}
		}
		msize.set(mstart).sub(mnow).abs();
		mtl.set(mstart).min(mnow);
	}
	
	public void update(long delta) {
		highway.update(delta);
	}
	
	public IVec2[] getMouseRect() {
		return new IVec2[] { mtl, msize };
	}

	// TODO: Implement flagging collisions.

}
