package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.awt.Graphics;

public class CameraGraphics {

	private Camera cam = new Camera();
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
		g.drawRect(x+cam.x,y+cam.y,w,h);
	}
	public void drawOverlayRect(int x, int y, int w, int h) {
		g.drawRect(x,y,w,h);
	}
	public void fillRect(int x, int y, int w, int h) {
		g.fillRect(x+cam.x,y+cam.y,w,h);
	}
	public void fillOverlayRect(int x, int y, int w, int h) {
		g.fillRect(x,y,w,h);
	}

	// Text
	public void drawString(String str, int x, int y) { // Overlays are non-transformed
		g.drawString(str, x+cam.x,y+cam.y);
	}
	public void drawOverlayString(String str, int x, int y) { // Overlays are non-transformed
		g.drawString(str, x,y);
	}
	
}
