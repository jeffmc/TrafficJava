package net.mcmillan.traffic.physics;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Random;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.DTransform2D;
import net.mcmillan.traffic.math.ITransform2D;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.simulation.Vehicle;

public class QuadtreeNode {

	private QuadtreeNode parent;
	private QuadtreeNode[] children;
	private ITransform2D transform;
	
	private int depth, maxDepth;
	
	public IVec2 size() { return transform.size; }
	public IVec2 position() { return transform.pos; }
	
	public static final Color debugGreen = new Color(0,200,0);
	public Color color = debugGreen; // new Color((int)(Math.random() * Integer.MAX_VALUE));
	
	public LinkedHashSet<Vehicle> linkedVehicles; // null if no children contained or if not leaf
	
	public static QuadtreeNode root(IVec2 size, int maxDepth) {
		return new QuadtreeNode(null, size, IVec2.make(), 0, maxDepth);
	}
	
	public static QuadtreeNode randomize(IVec2 size, int maxDepth) {
		QuadtreeNode root = new QuadtreeNode(null, size, IVec2.make(), 0, maxDepth);
		root.randomize();
		return root;
	}
	
	public void randomize() {
		if (!isLeaf()) merge();
		QuadtreeNode[] queue = new QuadtreeNode[]{ this };
		int d = 0, l = queue.length, nl = 0;
		Random r = new Random(/*1231234*/); // Defined seed for consistency in TESTING
		while (d < maxDepth) {
			QuadtreeNode[] next = new QuadtreeNode[queue.length*4];
			for (int i=0;i<l;i++) {
				if (r.nextInt(maxDepth) > d) {
					QuadtreeNode n = queue[i];
					n.split();
					for (QuadtreeNode c : n.children)
						next[nl++] = c;
				}
			}
			d++;
			queue = next;
			l = nl;
			nl = 0;
		}
	}
	
	public void draw(CameraGraphics cg) {
		if (children != null) {
			for (QuadtreeNode n : children)
				n.draw(cg);
		} else { 
			cg.setColor(color);
			cg.drawRect(transform.x(), transform.y(), transform.w(), transform.h());
		}
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public boolean isLeaf() {
		return children == null;
	}
	
	public boolean isBranch() {
		return parent != null && children != null;
	}
	
	private QuadtreeNode(QuadtreeNode parent, IVec2 size, IVec2 position, int depth, int maxDepth) {
		this.depth = depth;
		this.maxDepth = maxDepth;
		this.parent = parent;
		this.transform = new ITransform2D(position, size);
	}
	
	public boolean testAndAdd(Vehicle v) {
		boolean added = false;
		if (DTransform2D.intersects(v.transform, this.transform)) {
			if (this.isLeaf()) {
				linkVehicle(v);
				added |= true;
			} else {
				for (QuadtreeNode n : children) {
					added |= n.testAndAdd(v);
				}
			}
		}
		return added;
	}
	
	public void linkVehicle(Vehicle v) {
		if (!isLeaf()) throw new IllegalStateException("Can't link to non-leaf!");
		if (linkedVehicles == null) linkedVehicles = new LinkedHashSet<>();
		linkedVehicles.add(v);
		if (linkedVehicles.size() > 1 && depth < maxDepth) this.split();
	}
	
	public void split() {
//		System.out.println("[Quadtree] Split!");
		if (!isLeaf()) throw new IllegalStateException("Already split!");
		if (depth >= maxDepth) System.err.println("[Quadtree] Splitting beyond MAX: " + depth);
		IVec2 hsz = transform.size.copy().div(2); // Half-size
		IVec2 pos = transform.pos; // Position
		children = new QuadtreeNode[4];
		int nextDepth = depth + 1;
		children[0] = new QuadtreeNode(this, hsz, pos.copy(),                 nextDepth, maxDepth);
		children[1] = new QuadtreeNode(this, hsz, pos.copy().add(hsz.x(), 0), nextDepth, maxDepth);
		children[2] = new QuadtreeNode(this, hsz, pos.copy().add(0, hsz.y()), nextDepth, maxDepth);
		children[3] = new QuadtreeNode(this, hsz, pos.copy().add(hsz),        nextDepth, maxDepth);
		for (QuadtreeNode n : children) {
			if (n.linkedVehicles != null)
				for (Vehicle v : linkedVehicles)
					n.testAndAdd(v);
		}
		linkedVehicles = null;
	}
	
	public void maxSplit() {
		if (depth < maxDepth) {
			if (isLeaf()) split();
			for (QuadtreeNode n : children) n.maxSplit();
		}
	}
	
	public void attemptMerge() {
		try {
			merge();
		} catch (IllegalStateException e) {}
	}
	
	public void merge() { // difference between add and link is that add asserts that link is actually physically intersects.
		if (isLeaf()) throw new IllegalStateException("Can't merge a leaf!");
		for (QuadtreeNode n : children)
			if (!n.isLeaf()) n.merge();
		linkedVehicles = new LinkedHashSet<Vehicle>();
		for (QuadtreeNode n : children) {
			if (n.linkedVehicles != null)
				for (Vehicle v : n.linkedVehicles)
					testAndAdd(v);
			n.parent = null;
			n.linkedVehicles = null;
		}
		children = null;
		if (linkedVehicles.size() < 1) linkedVehicles = null;
	}
}
