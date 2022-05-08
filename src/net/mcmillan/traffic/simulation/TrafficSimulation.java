package net.mcmillan.traffic.simulation;

import net.mcmillan.traffic.event.Event;
import net.mcmillan.traffic.event.EventQueue;
import net.mcmillan.traffic.gfx.Camera;
import net.mcmillan.traffic.math.ITransform2D;
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
	
	public IVec2 mstart = IVec2.make(), mnow = IVec2.make(), msize = IVec2.make(), morigin = IVec2.make();
	
	private Camera cam = new Camera();
	public Camera getCamera() { return cam; }
	
	public Highway highway = new Highway();
	public QuadtreeNode getQuadtreeRoot() { return highway.getQuadtreeRoot(); }
	
	public boolean isRunning() { return running; }
	public boolean isPaused() { return paused; }
	
	public void start() {
		if (running) throw new IllegalStateException("Can't start an already active simulation!");
		running = true;
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
	
	private int cox, coy, msx, msy;
	
	public void pollEvents() {
		eventq.unload();
		while (!eventq.unloadedEmpty()) {
			Event e = eventq.pop();
			switch (e.code) {
			case Event.MOUSE_PRESSED:
				switch (e.button()) {
				case Event.BUTTON1:
					dragMode = SELECT_MODE;
					setMouseNowRelativeToCam(e);
					mstart.set(mnow);
					break;
				case Event.BUTTON2:
					dragMode = CAM_MODE;
					cox = cam.x;
					coy = cam.y;
					msx = e.x();
					msy = e.y();
					break;
				}
				break;
			case Event.MOUSE_RELEASED:
				dragMode = -1;
				break;
			case Event.MOUSE_DRAGGED:
				switch (dragMode) {
				case SELECT_MODE:
					setMouseNowRelativeToCam(e);
					break;
				case CAM_MODE:
					cam.x = cox + msx - e.x();
					cam.y = coy + msy - e.y();
					break;
				}
				break;
			case Event.MOUSE_WHEEL_MOVED:
				break;
			}
		}
		msize.set(mstart).sub(mnow).abs();
		morigin.set(mstart).min(mnow);
	}
	
	private void setMouseNowRelativeToCam(Event e) { // Convert from screen -> world coords
		mnow.set(e.x()+cam.x, e.y()+cam.y);
	}
	
	public void update(long delta) {
		highway.update(delta);
	}
	
	public ITransform2D getSelectionTransform() {
		return new ITransform2D(morigin, msize);
	}

	// TODO: Implement flagging collisions.

}
