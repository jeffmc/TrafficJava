package net.mcmillan.traffic.physics;

import net.mcmillan.traffic.math.IVec2;

public class QuadtreeNode {

	public QuadtreeNode parent;
	public QuadtreeNode[] children;
	
	public IVec2 size, position;
	
}
