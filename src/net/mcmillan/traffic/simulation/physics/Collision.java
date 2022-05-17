package net.mcmillan.traffic.simulation.physics;

import net.mcmillan.traffic.math.DVec2;
import net.mcmillan.traffic.simulation.TrafficSimulation;
import net.mcmillan.traffic.simulation.Vehicle;

public class Collision {
	
	public TrafficSimulation sim;
	public Vehicle a,b;
	public DVec2 pos;
	
	public Collision(TrafficSimulation s, Vehicle a, Vehicle b) {
		this.sim = s;
		this.a = a;
		this.b = b;
		pos = a.transform.pos.copy().add(b.transform.pos).div(2);
	}
	
}
