package net.mcmillan.traffic.simulation;

import java.awt.Color;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.DTransform2D;
import net.mcmillan.traffic.math.DVec2;

public class Vehicle {
	public static final Color BRAKING_COLOR = new Color(255, 0, 0), 
			NEUTRAL_COLOR = new Color(0,0,255),
			ACCEL_COLOR = new Color(0,255,0),
			SELECTED_COLOR = new Color(255,255,0);
	
	public Color forceColor = NEUTRAL_COLOR;
	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	public DTransform2D transform;
	public double speed = 2, topSpeed = 4, power = 0.05, brake = 0.03;
	
	private boolean selected = false;
	public void setSelected(boolean b) { selected = b; }
	public boolean isSelected() { return selected; }
	
	public Vehicle() {
		this(DVec2.make(10,10), DVec2.make(42,16));
	}
	
	public Vehicle(DVec2 pos, DVec2 size) {
		transform = new DTransform2D(pos, size);
	}
	
	public void tick(long delta) {
		transform.pos.add((int)speed, 0);
	}
	
	public void draw(CameraGraphics g) {
		g.setColor(color);
		g.fillRect(transform);
		g.setColor(forceColor);
		g.drawRect(transform);
		if (selected) {
			g.setColor(SELECTED_COLOR);
			g.drawRect(transform.copy().offset(3));
		}
	}
	
}
