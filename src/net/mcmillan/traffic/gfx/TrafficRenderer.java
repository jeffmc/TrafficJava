package net.mcmillan.traffic.gfx;

import java.awt.Color;

import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.simulation.TrafficSimulation;
import net.mcmillan.traffic.simulation.Vehicle;

public class TrafficRenderer {

	private TrafficSimulation scene;
	private RenderableCanvas target;
	private CameraGraphics cameraGfx = new CameraGraphics();
	
	public void setScene(TrafficSimulation sim) {
		scene = sim;
		cameraGfx.setCamera(scene.getCamera());
		if (scene != null && target != null) {
			target.setEventQueue(scene.getEventQueue());
		}
	}
	
	public void setTarget(RenderableCanvas canvas) {
		target = canvas; // TODO: Add listener for canvas size change
		if (scene != null && target != null) {
			target.setEventQueue(scene.getEventQueue());
		}
	}
	
	public void setCamera(Camera c) {
		cameraGfx.setCamera(c);
	}
	
	public void draw(long delta) {
		cameraGfx.setGraphics(target.getGraphics());
		intl_draw(cameraGfx, delta);
		target.showBuffer();
	}
	
	private void intl_draw(CameraGraphics cg, long delta) {
		// Background
		cg.setColor(Color.black);
		cg.fillOverlayRect(0, 0, target.getWidth(), target.getHeight());

		// Draw quadtree!
		IVec2[] mr = scene.getMouseRect();
		scene.getQuadtreeRoot().draw(cg, mr);

		// Draw mouse
		cg.setColor(Color.white);
		cg.drawRect(mr[0].x(), mr[0].y(), mr[1].x(), mr[1].y());
		
		if (scene != null) 
			for (Vehicle v : scene.vehicles) v.draw(cg);
		
		drawTargetDimensions(cg, delta);
	}
	
	private void drawTargetDimensions(CameraGraphics cg, long delta) {
		cg.setColor(Color.white);
		cg.drawOverlayString("[" + target.getWidth() + ", " + target.getHeight() + "]", 2, 12);
		cg.drawOverlayString("Ticks: " + scene.ticks(), 2, 24);
		cg.drawOverlayString("Delta: " + delta, 2, 36);
	}
	
}
