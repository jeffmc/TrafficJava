package net.mcmillan.traffic.physics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import net.mcmillan.traffic.gfx.CameraGraphics;
import net.mcmillan.traffic.math.IVec2;

public class QuadtreeNode {

	private QuadtreeNode parent;
	private QuadtreeNode[] children;
	private IVec2 size, position;
	
	public Color color = new Color((int)(Math.random() * Integer.MAX_VALUE));
	
//	public LinkedHashSet<Vehicle> linkedVehicles; // null unless leaf
//	public LinkedHashSet<Road> linkedRoads; // null unless leaf
	
	public static QuadtreeNode root(IVec2 size) {
		return new QuadtreeNode(null, size, IVec2.make());
	}
	
	public static QuadtreeNode randomize(IVec2 size, int maxDepth) {
		QuadtreeNode root = new QuadtreeNode(null, size, IVec2.make());
		ArrayList<QuadtreeNode> level = new ArrayList<>();
		level.add(root);
		int depth = 0;
		Random r = new Random();
		while (depth < maxDepth) {
			ArrayList<QuadtreeNode> nextLevel = new ArrayList<>();
			for (QuadtreeNode n : level) {
				if (r.nextInt(maxDepth) > depth) {
					n.split();
					for (QuadtreeNode c : n.children)
						nextLevel.add(c);
				}
			}
			depth++;
			level = nextLevel;
		}
		return root;
	}
	
	public void draw(CameraGraphics cg, IVec2[] mr) {
		if (children != null) {
			for (QuadtreeNode n : children)
				n.draw(cg, mr);
		} else { 
			cg.setColor(color);
			if (IVec2.rectIntersect(position, size, mr[0], mr[1])) {
				cg.fillRect(position.x(), position.y(), size.x(), size.y());
			} else {
				cg.drawRect(position.x(), position.y(), size.x(), size.y());
			}
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
	
	private QuadtreeNode(QuadtreeNode parent, IVec2 size, IVec2 position) {
		this.parent = parent;
		this.size = size;
		this.position = position;
	}
	
	public void split() {
		IVec2 hsize = size.copy().div(2);
		children = new QuadtreeNode[4];
		children[0] = new QuadtreeNode(this, hsize, position.copy());
		children[1] = new QuadtreeNode(this, hsize, position.copy().add(hsize.x(), 0));
		children[2] = new QuadtreeNode(this, hsize, position.copy().add(0, hsize.y()));
		children[3] = new QuadtreeNode(this, hsize, position.copy().add(hsize));
//		for (QuadTreeNode n : children) {
//			for (Vehicle v : linkedVehicles)
//				if (n.intersects(v)) n.addVehicle(v); // don't have these add functions check intersection
//			for (Road r : linkedRoads)
//				if (n.intersects(r)) n.addRoad(r);
//		}
//		linkedVehicles = null;
//		linkedRoads = null;
	}
	
	void merge() { // difference between add and link is that add asserts that link is actually physically interesects.
//		linkedVehicles = new LinkedHashSet<Vehicle>;
//		linkedRoads = new LinkedHashSet<Road>;
//		for (QuadTreeNode n : children) {
//			this.linkVehicle(n.linkedVehicles);
//			this.linkRoad(n.linkedRoads);
//			n.parent = null;
//			n.linkedVehicles = null;
//			n.linkedRoads = null;
//		}
		children = null;
	}
}
