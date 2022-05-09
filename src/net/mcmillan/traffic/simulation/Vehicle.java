package net.mcmillan.traffic.simulation;

import java.awt.Color;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.ITransform2D;
import net.mcmillan.traffic.math.IVec2;

public class Vehicle {
	public static final Color BRAKING_COLOR = new Color(255, 0, 0), 
			NEUTRAL_COLOR = new Color(0,0,255),
			ACCEL_COLOR = new Color(0,255,0),
			SELECTED_COLOR = new Color(255,255,0);
	
	public Color forceColor = NEUTRAL_COLOR;
	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	public ITransform2D transform;
	public int speed = 2;
	public int topSpeed = 4;
	public double power = 0.05, brake = 0.03;
	
	private boolean selected = false;
	public void setSelected(boolean b) { selected = b; }
	public boolean isSelected() { return selected; }
	
	public Vehicle() {
		this(IVec2.make(10,10), IVec2.make(42,16));
	}
	
	public Vehicle(IVec2 pos, IVec2 size) {
		transform = new ITransform2D(pos, size);
	}
	
	public void tick(long delta) {
		transform.pos.add(speed, 0);
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
