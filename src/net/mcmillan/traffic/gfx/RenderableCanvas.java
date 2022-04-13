package net.mcmillan.traffic.gfx;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

import net.mcmillan.traffic.math.IVec2;

public class RenderableCanvas {

	private Canvas canvas;
	public Canvas getAWTCanvas() { return canvas; }
	
	private BufferStrategy bufferStrategy = null;
	
	private IVec2 size = IVec2.make();
	
	public int getWidth() { return size.x(); }
	public int getHeight() { return size.y(); }
	
	public RenderableCanvas() {
		canvas = new Canvas();
		
		setupCanvas(new Dimension(1152, 648));
		updateSizeVec();
		
		canvas.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				updateSizeVec(); // TODO: Add listener model
			}
		});
		
		setupEventPolling();
	}

	private void setupEventPolling() { // TODO: Aggregate events into common queue
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) { }
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {}
		});
	}
	
	public void makeBuffers() {
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
	}

	private void setupCanvas(Dimension size) {
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
	}
	
	private void updateSizeVec() {
		size.x(canvas.getWidth());
		size.y(canvas.getHeight());
	}
	
	public Graphics getGraphics() {
		Graphics gg = bufferStrategy.getDrawGraphics();
		return gg.create();
	}

//	public void renderToCanvas() {
//		Graphics g = getGraphics();
////		Renderer.drawFrame(g); TODO: Impl
//		g.dispose();
//	}
	public void showBuffer() {
		bufferStrategy.show();
	}
}
