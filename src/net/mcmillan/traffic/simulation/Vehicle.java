package net.mcmillan.traffic.simulation;

import java.awt.Color;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.ITransform2D;
import net.mcmillan.traffic.math.IVec2;

public class Vehicle {
	
	public static final Color BRAKING = new Color(255, 0, 0), NEUTRAL = new Color(255,255,0), ACCEL = new Color(0,255,0);
	
	public Color forceColor = NEUTRAL;
	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	public ITransform2D transform;
	public int topSpeed = 4;
	public double power = 0.05, brake = 0.03;
	
	public Vehicle() {
		this(IVec2.make(10,10), IVec2.make(42,16));
	}
	
	public Vehicle(IVec2 pos, IVec2 size) {
		transform = new ITransform2D(pos, size);
	}
	
	public void tick(long delta) {
		transform.pos.add(1, 0);
	}
	
	public void draw(CameraGraphics g) {
		g.setColor(color);
		g.fillRect(transform.x(), transform.y(), transform.w(), transform.h());
		g.setColor(forceColor);
		g.drawRect(transform.x(), transform.y(), transform.w(), transform.h());
	}
	
}
