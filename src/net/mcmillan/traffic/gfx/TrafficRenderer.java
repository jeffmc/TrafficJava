package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.awt.Graphics;

import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.simulation.TrafficSimulation;

public class TrafficRenderer {

	private TrafficSimulation scene;
	private RenderableCanvas target;
	
	public void setScene(TrafficSimulation sim) {
		scene = sim;
		if (scene != null && target != null) {
			target.setEventQueue(scene.getEventQueue());
		}
	}
	
	public void setTarget(RenderableCanvas canvas) { // Change type to generic
		target = canvas; // Add listener
		if (scene != null && target != null) {
			target.setEventQueue(scene.getEventQueue());
		}
	}
	
	public void draw(long delta) {
		Graphics g = target.getGraphics();
		
		// Background
		g.setColor(Color.black);
		g.fillRect(0, 0, target.getWidth(), target.getHeight());

		IVec2[] mr = scene.getMouseRect();
		// Draw quadtree!
		scene.getQuadtreeRoot().draw(g, mr);

		// Draw mouse
		g.setColor(Color.white);
		g.drawRect(mr[0].x(), mr[0].y(), mr[1].x(), mr[1].y());
		System.out.println(mr[0].x() + ", " + mr[0].y() + ", " +mr[1].x() + ", " +mr[1].y());
		
		drawTargetDimensions(g, delta);
		target.showBuffer();
	}
	
	private void drawTargetDimensions(Graphics g, long delta) {
		g.setColor(Color.white);
		g.drawString("[" + target.getWidth() + ", " + target.getHeight() + "]", 2, 12);
		g.drawString("Ticks: " + scene.ticks(), 2, 24);
		g.drawString("Delta: " + delta, 2, 36);
	}
	
}
