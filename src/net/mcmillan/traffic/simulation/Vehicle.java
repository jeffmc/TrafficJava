package net.mcmillan.traffic.simulation;

import java.awt.Color;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.IVec2;

public class Vehicle {

	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	public IVec2 pos, size;
	
	public Vehicle() {
		this(IVec2.make(10,10), IVec2.make(60,40));
	}
	
	public Vehicle(IVec2 pos, IVec2 size) {
		this.pos = pos;
		this.size = size;
	}
	
	public void draw(CameraGraphics g) {
		pos.add(1, 0);
		g.setColor(color);
		g.fillRect(pos.x(), pos.y(), size.x(), size.y());
	}
	
}
