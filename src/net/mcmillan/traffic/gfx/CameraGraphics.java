package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.awt.Graphics;

public class CameraGraphics {

	private Camera cam = new Camera();
	public int camX() { return cam.x; }
	public int camY() { return cam.y; }
	private Graphics g;
	
	// Basic
	public CameraGraphics() {
		this(null);
	}
	public CameraGraphics(Graphics g) {
		this.g = g;
	}
	public void setCamera(Camera c) {
		this.cam = c;
	}
	public void setGraphics(Graphics g) {
		this.g = g;
	}
	
	// Color
	public void setColor(Color c) {
		g.setColor(c);
	}
	
	// Rect
	public void drawRect(int x, int y, int w, int h) {
//		g.drawRect((x-cam.x)*cam.z,(y-cam.y)*cam.z,w*cam.z,h*cam.z);
		g.drawRect(x-cam.x,y-cam.y,w,h);
	}
	public void drawOverlayRect(int x, int y, int w, int h) {
		g.drawRect(x,y,w,h);
	}
	public void fillRect(int x, int y, int w, int h) {
//		g.fillRect((x-cam.x)*cam.z,(y-cam.y)*cam.z,w*cam.z,h*cam.z);
		g.fillRect(x-cam.x,y-cam.y,w,h);
	}
	public void fillOverlayRect(int x, int y, int w, int h) {
		g.fillRect(x,y,w,h);
	}

	// Text
	public void drawString(String str, int x, int y) { // Not zoomed
		g.drawString(str, x-cam.x,y-cam.y);
	}
	public void drawOverlayString(String str, int x, int y) { // Overlays are non-transformed
		g.drawString(str, x,y);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) { // Draw line offset
		g.drawLine(x1-cam.x, y1-cam.y, x2-cam.x, y2-cam.y);
	}
	
}
