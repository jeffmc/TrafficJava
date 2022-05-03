package net.mcmillan.traffic;

import net.mcmillan.traffic.gfx.AppWindow;
import net.mcmillan.traffic.gfx.ControlPanel;
import net.mcmillan.traffic.gfx.TrafficRenderer;
import net.mcmillan.traffic.simulation.TrafficSimulation;

public class AppManager {

	private TrafficSimulation sim;
	private TrafficRenderer renderer;
	private AppWindow window;
	private ControlPanel controlPanel;
	
	private static AppManager instance;
	public static AppManager getSingleton() { return instance; }
	
	private boolean running = false;
	
	public static void main(String[] args) {
		instance = new AppManager();
		instance.setup();
		instance.loop();
		instance.exit();
	}

	private void setup() {
		window = new AppWindow();
		controlPanel = new ControlPanel();
		
		sim = new TrafficSimulation();
		
		renderer = new TrafficRenderer();
		renderer.setScene(sim);
		renderer.setTarget(window.getCanvas());
		
		controlPanel.setSimulation(sim);
		
//		renderer.addMonitor(() -> "Stalls: " + sim.getEventQueue().getStalls());
	}

	private void loop() {
		running = true;
		sim.start();
		long lastTime = 0;
		while (running) {
			long time = System.nanoTime();
			long delta = time - lastTime;
			final long nspf = 1000000000 / 60; // 1/60 of a second in nanoseconds
			if (delta >= nspf) {
				lastTime = System.nanoTime();
				step(delta);
			}
		}
	}
	
	private void step(long delta) { // Delta is given in nanoseconds
//		Timestep ts = new Timestep(delta); TODO: Might implement timestep
		if (sim.isRunning()) {
			sim.tick(delta);
		}
		
		renderer.draw(delta);
	}
	
	public void stop() {
		running = false;
	}
	
	public void exit() {
		System.exit(0);
	}
}
