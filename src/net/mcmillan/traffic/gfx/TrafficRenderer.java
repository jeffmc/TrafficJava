package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.util.ArrayList;

import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.simulation.TrafficSimulation;
import net.mcmillan.traffic.simulation.Vehicle;

public class TrafficRenderer {

	private TrafficSimulation scene;
	private RenderableCanvas target;
	private CameraGraphics cameraGfx = new CameraGraphics();
	
	private ArrayList<Monitorable> monitors = new ArrayList<>();
	
	private long delta = 0;
	
	public TrafficRenderer() {
		addMonitor(() -> "[" + target.getWidth() + ", " + target.getHeight() + "]");
		addMonitor(() -> "Ticks: " + scene.ticks());
		addMonitor(() -> "Delta: " + delta);
	}
	
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
	
	public void draw(long d) {
		cameraGfx.setGraphics(target.getGraphics());
		delta = d;
		intl_draw(cameraGfx, delta);
		target.showBuffer();
	}
	
	private void intl_draw(CameraGraphics cg, long delta) {
		drawBackground(cg);

		// Draw quadtree!
		IVec2[] mr = scene.getMouseRect();
//		long start = System.currentTimeMillis();
//		scene.getQuadtreeRoot().draw(cg);
//		System.out.println("Drawing quadtree took " + (System.currentTimeMillis() - start) + "ms");

		// Draw mouse
		cg.setColor(Color.white);
		cg.drawRect(mr[0].x(), mr[0].y(), mr[1].x(), mr[1].y());
		
		if (scene != null) 
			drawScene(cg, scene);
		
		drawTargetDimensions(cg, delta);
	}
	
	private static final Color background = new Color(0,0,0), gridLines = new Color(45,45,45);
	private static final int gridSize = 25;
	private void drawBackground(CameraGraphics cg) {
		int w = target.getWidth(), h = target.getHeight();
		int lbound = cg.camX(), rbound = lbound + w, tbound = cg.camY(), bbound = tbound + h;
		// Background
		cg.setColor(background);
		cg.fillOverlayRect(0, 0, w, h);
		cg.setColor(gridLines);
		for (int x=(lbound/gridSize)*gridSize;x<rbound;x+=gridSize) {
			cg.drawLine(x,tbound,x,bbound);
		}
		for (int y=(tbound/gridSize)*gridSize;y<bbound;y+=gridSize) {
			cg.drawLine(lbound,y,rbound,y);
		}
	}
	
	private void drawScene(CameraGraphics cg, TrafficSimulation sim) {
		for (Vehicle v : scene.highway.vehicles) v.draw(cg);
	}
	
	private void drawTargetDimensions(CameraGraphics cg, long delta) {
		cg.setColor(Color.white);
		int x = 2, y = 1, yf = 12;
		for (Monitorable m : monitors) 
			cg.drawOverlayString(m.get(), x, y++*yf);
	}
	
	public void addMonitor(Monitorable m) {
		monitors.add(m);
	}
	public boolean removeMonitor(Monitorable m) {
		return monitors.remove(m);
	}
	
	public interface Monitorable {
		public String get();
	}
	
}
