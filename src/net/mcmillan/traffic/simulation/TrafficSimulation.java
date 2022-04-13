package net.mcmillan.traffic.simulation;

public class TrafficSimulation {

	private boolean running = false;
	
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
		if (ticks > 300) throw new CollisionException();
	}
	
	public void flagCollision(CollisionException e) {
		// TODO: Impl
	}
	
}
