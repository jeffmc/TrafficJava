package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.awt.Graphics;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class TrafficRenderer {

	private TrafficSimulation scene;
	private RenderableCanvas target;
	
	public void setScene(TrafficSimulation sim) {
		scene = sim;
	}
	
	public void setTarget(RenderableCanvas canvas) { // Change type to generic
		target = canvas; // Add listener
	}
	
	public void draw() {
		Graphics g = target.getGraphics();
		// TODO: Make real draw
		g.setColor(Color.black);
		g.fillRect(0, 0, target.getWidth(), target.getHeight());
		
		drawTargetDimensions(g);
		
		target.showBuffer();
	}
	
	private void drawTargetDimensions(Graphics g) {
		g.setColor(Color.white);
		g.drawString("[" + target.getWidth() + ", " + target.getHeight() + "]", 2, 12);
	}
	
}
