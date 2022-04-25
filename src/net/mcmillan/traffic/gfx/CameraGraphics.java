package net.mcmillan.traffic.gfx;

import java.awt.Color;
import java.awt.Graphics;

import net.mcmillan.traffic.math.Mat3x2;

public class CameraGraphics {

	private Graphics g;
	private Mat3x2 matrix = new Mat3x2().translate(10, 0).scale(0.5);
	
	public CameraGraphics() {
		this(null);
	}
	
	public CameraGraphics(Graphics g) {
		this.g = g;
	}
	
	public void setGraphics(Graphics g) {
		this.g = g;
	}
	
	public void setColor(Color c) {
		g.setColor(c);
	}
	
	public void drawRect(int x, int y, int w, int h) {
		double[] ps = matrix.multiply(x, y);
		double[] sz = matrix.scaled(w,h);
		g.drawRect((int)ps[0], (int)ps[1], (int)sz[0], (int)sz[1]);
	}
	public void drawOverlayRect(int x, int y, int w, int h) {
		g.drawRect(x,y,w,h);
	}
	public void fillRect(int x, int y, int w, int h) {
		double[] ps = matrix.multiply(x, y);
		double[] sz = matrix.scaled(w,h);
		g.fillRect((int)ps[0], (int)ps[1], (int)sz[0], (int)sz[1]);
	}
	public void fillOverlayRect(int x, int y, int w, int h) {
		g.fillRect(x,y,w,h);
	}
	
	public void drawOverlayString(String str, int x, int y) { // Overlays are non-transformed
		g.drawString(str, x, y);
	}
	
}
