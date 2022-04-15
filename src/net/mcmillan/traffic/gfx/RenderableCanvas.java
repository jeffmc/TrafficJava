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

import net.mcmillan.traffic.event.Event;
import net.mcmillan.traffic.event.EventQueue;
import net.mcmillan.traffic.event.EventQueueWindow;
import net.mcmillan.traffic.math.IVec2;

public class RenderableCanvas {

	private Canvas canvas;
	public Canvas getAWTCanvas() { return canvas; }
	
	private BufferStrategy bufferStrategy = null;
	
	private IVec2 size = IVec2.make();
	
	public int getWidth() { return size.x(); }
	public int getHeight() { return size.y(); }

	private EventQueue eventq;
	
	private EventQueueWindow eqw;
	
	
	public RenderableCanvas() {
		canvas = new Canvas();

		eventq = new EventQueue();
		eqw = new EventQueueWindow(eventq);
		
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
			public void keyTyped(KeyEvent e) { 
				eventq.push(new Event( 0x00));
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				eventq.push(new Event( 0x01));
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				eventq.push(new Event( 0x02));
			}
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				eventq.push(new Event( 0x10));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				eventq.push(new Event( 0x11));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				eventq.push(new Event( 0x12));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				eventq.push(new Event( 0x13));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				eventq.push(new Event( 0x14));
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				eventq.push(new Event( 0x15));
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				eventq.push(new Event( 0x16));
			}
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				eventq.push(new Event( 0x17));
			}
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
