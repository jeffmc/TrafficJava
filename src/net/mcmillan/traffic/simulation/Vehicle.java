package net.mcmillan.traffic.simulation;

import java.awt.Color;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.DTransform2D;
import net.mcmillan.traffic.math.DVec2;

public class Vehicle {
	public static final Color BRAKING_COLOR = new Color(255, 0, 0), 
			NEUTRAL_COLOR = new Color(0,0,255),
			ACCEL_COLOR = new Color(0,255,0),
			SELECTED_COLOR = new Color(255,255,0),
			DEBUG_COLOR = new Color(0,255,255),
			FRONT_DISTANCE_COLOR = new Color(255,0,0);
	
	public Color forceColor = NEUTRAL_COLOR;
	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	public DTransform2D transform;
	
	// Vehicle Data
	public double acceleration = 0, speed = 0, // velocity 
			topSpeed = 4, power = 0.01, brake = 0.03, // attributes
			cachedStoppingDistance = -1, cachedFrontDistance = -1;
	
	public boolean debugMode = false;
	
	private boolean selected = false;
	public void setSelected(boolean b) { selected = b; }
	public boolean isSelected() { return selected; }
	
	public Vehicle() {
		this(DVec2.make(10,10), DVec2.make(42,16));
	}
	
	public Vehicle(DVec2 pos, DVec2 size) {
		transform = new DTransform2D(pos, size);
	}
	
	private long lastPretick = -1;
	public void pretick(long ticks, Vehicle f, int highwayLength) {
		if (ticks == lastPretick) return;
		lastPretick = ticks;
		
		// Determine target speed
		if (f == this) { // Hit max speed if alone in lane
			acceleration = power;
			cachedFrontDistance = -1;
			return;
		}
		
		// Find front distance
		double dist = f.transform.x() - this.transform.rx();
		if (dist < 0) dist += highwayLength;
		cachedFrontDistance = dist;
		
		// Find front brake difference
		double brakeDiff = f.cachedStoppingDistance - this.cachedFrontDistance;
		if (brakeDiff > 0) {
			acceleration = power;
		} else if (closeEqual(brakeDiff, 0, 0.1)) {
			acceleration = 0;
		} else {
			acceleration = brake;
		}
	}
	
	public boolean closeEqual(double a, double b, double margin) {
		return Math.abs(a - b) <= margin;
	}
	
	public void tick() {
		acceleration = Math.max(-brake, Math.min(power, acceleration));
		if (acceleration > 0) {
			forceColor = ACCEL_COLOR;
		} else if (acceleration < 0) {
			forceColor = BRAKING_COLOR;
		} else {
			forceColor = NEUTRAL_COLOR;
		}
		speed += acceleration;
		speed = Math.max(0, Math.min(speed, topSpeed));
		cachedStoppingDistance = brakingFunc(brakingTime());
	}
	
	public void posttick() {
		transform.pos.add(speed, 0);
	}
	
	public void draw(CameraGraphics g, boolean drawForces, boolean drawBraking, boolean drawFrontDistance, int xOffset) {
		DTransform2D transform = this.transform.copy().move(xOffset,0);
		g.setColor(color);
		g.fillRect(transform);
		if (drawForces||debugMode) {
			g.setColor(forceColor);
			g.drawRect(transform);
		}
		if ((drawBraking||debugMode)&&cachedStoppingDistance > 0) {
			double x = transform.rx();
			int y = (int)(transform.cy());
			int sx = (int)(x+cachedStoppingDistance);
			final int HALF_HEIGHT = 8;
			g.setColor(forceColor);
			g.drawLine((int)x, y, sx, y);
			g.drawLine(sx, y-HALF_HEIGHT, sx, y+HALF_HEIGHT);
		}
		if ((drawFrontDistance||debugMode)&&cachedFrontDistance > 0) {
			g.setColor(FRONT_DISTANCE_COLOR);
			int rx = (int)transform.rx();
			int y = (int)transform.cy()+5;
			g.drawLine(rx, y, (int) (rx + cachedFrontDistance), y);
		}
		if (selected) {
			g.setColor(SELECTED_COLOR);
			g.drawRect(transform.copy().offset(3));
		}
		if (debugMode) {
			g.setColor(DEBUG_COLOR);
			g.drawRect(transform.copy().offset(-3));
		}
	}
	
	public double brakingTime() {
		return speed / brake;
	}

	public double brakingFunc(double t) {
		return (-brake/2)*t*t + (speed+brake/2)*t;
	}
	
	public double stoppingDistance() {
		return brakingFunc(brakingTime());
	}
}
