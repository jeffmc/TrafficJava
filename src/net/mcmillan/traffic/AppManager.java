package net.mcmillan.traffic;

import net.mcmillan.traffic.gfx.TrafficRenderer;
import net.mcmillan.traffic.gfx.AppWindow;
import net.mcmillan.traffic.simulation.CollisionException;
import net.mcmillan.traffic.simulation.TrafficFactory;
import net.mcmillan.traffic.simulation.TrafficSimulation;

public class AppManager {

	TrafficSimulation sim;
	TrafficRenderer renderer;
	AppWindow window;
	
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
		sim = TrafficFactory.generate();
		window = new AppWindow();
		renderer = new TrafficRenderer();
		renderer.setScene(sim);
		renderer.setTarget(window.getCanvas());
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
//				Timestep ts = new Timestep(delta); TODO: Might implement timestep
				
				if (sim.isRunning()) {
					try {
						sim.tick();
					} catch (CollisionException e) {
						sim.stop();
						sim.flagCollision(e);
						e.printStackTrace();
					}
				}
				
				pollEvents();

				renderer.draw();
			
			}
		}
	}
	
	public void stop() {
		System.err.println("STOP!");
		running = false;
	}
	
	public void exit() {
		System.exit(0);
	}

	public void pollEvents() {			
		/* TODO: Parse canvas events and pass them into the renderer's camera 
		and/or other tools. */
	}
}
