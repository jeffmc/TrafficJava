package net.mcmillan.traffic;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

public class RenderableCanvas {

	private static final int DEFAULT_CANVAS_SIZE = 768;
	private Canvas canvas;
	public Canvas getAWTCanvas() { return canvas; }
	
	private BufferStrategy bufferStrategy;
	
	public RenderableCanvas() {

		canvas = new Canvas();
		
		
		setupCanvas(new Dimension(DEFAULT_CANVAS_SIZE,DEFAULT_CANVAS_SIZE));
		canvas.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
//				Renderer.setViewport(canvas.getWidth(), canvas.getHeight()); TODO: Add listener model
			}
		});
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
	}

	private void setupCanvas(Dimension size) {
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
	}
	
	public Graphics getGraphics() {
		Graphics gg = bufferStrategy.getDrawGraphics();
		return gg.create();
	}

	public void renderToCanvas() {
		Graphics g = getGraphics();
//		Renderer.drawFrame(g); TODO: Impl
		g.dispose();
	}
	public void postUpdate() {
		renderToCanvas();
		
		bufferStrategy.show();
	}
}
