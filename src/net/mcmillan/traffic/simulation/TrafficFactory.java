package net.mcmillan.traffic.simulation;

public abstract class TrafficFactory {

	public static TrafficSimulation generate() {
		return new TrafficSimulation(); // TODO: Impl random generation
	}
	
}
