package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.util.ArrayList;

import net.mcmillan.traffic.math.ITransform2D;
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
		addMonitor(new LabeledMonitorable("Camera", () -> "[" + cameraGfx.camX() + ", " + cameraGfx.camY() + "]")); // Make the visibility of these toggable in Control Panel
		addMonitor(new LabeledMonitorable("Viewport", () -> "[" + target.getWidth() + ", " + target.getHeight() + "]"));
		addMonitor(new LabeledMonitorable("Ticks", () -> Long.toString(scene.ticks())));
		addMonitor(new LabeledMonitorable("Delta", () -> Long.toString(delta)));
		addMonitor(new LabeledMonitorable("Vehicles", () -> Integer.toString(scene.highway.vehicles.size())));
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
		
		if (scene != null) 
			drawScene(cg);

		// Draw mouse selection rect
		if (scene.getDragMode() == TrafficSimulation.DRAG_SELECT_MODE) {
			cg.setColor(Color.white);
			ITransform2D selection = scene.getSelectionTransform();
			cg.drawRect(selection);
		}
		
		drawMonitorables(cg, delta);
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
	
	private void drawScene(CameraGraphics cg) {
//		if (scene.debugOptions.get(DebugOptions.DRAW_QUADTREE)) {
//			scene.getQuadtreeRoot().draw(cg);
//		} else {
		cg.setColor(Color.LIGHT_GRAY);
		cg.drawRect(new ITransform2D(IVec2.make(), scene.highway.size));
//		}
		for (Vehicle v : scene.highway.vehicles) v.draw(cg);
	}
	
	private void drawMonitorables(CameraGraphics cg, long delta) {
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
	
	public class LabeledMonitorable implements Monitorable {

		private String l, s = ": "; // Label + separator
		private Monitorable m; // Data retriever
		
		public LabeledMonitorable(String label, Monitorable mon) {
			l = label; 
			m = mon;
		}
		public LabeledMonitorable(String label, String seperator, Monitorable mon) {
			l = label; 
			s = seperator;
			m = mon;
		}
		
		@Override
		public String get() {
			return l + s + m.get();
		}
		
	}
	
}
