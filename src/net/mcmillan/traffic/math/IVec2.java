package net.mcmillan.traffic.math;

public class IVec2 { // TODO: Add read-only vecs for sending through events
	public static final int VECSIZE = 2;
	
	private int[] n;
	public int x() { return n[0]; }
	public int y() { return n[1]; }
	public void x(int nx) { n[0] = nx; }
	public void y(int ny) { n[1] = ny; }
	
	private IVec2(int[] t) { n = t; }
	private IVec2(int x, int y) { n = new int[] {x, y}; }
	
	public static IVec2 make() { return new IVec2(0,0); }
	public static IVec2 make(int x, int y) { return new IVec2(x,y); }
	public static IVec2 copy(IVec2 src) { return new IVec2(src.n.clone()); }
	public static IVec2 ref(int[] raw) { return new IVec2(raw); }
	
	public IVec2 add(IVec2 a) {
		for (int i=0;i<VECSIZE;i++)
			n[i] += a.n[i];
		return this;
	}
	public IVec2 sub(IVec2 a) {
		for (int i=0;i<VECSIZE;i++)
			n[i] -= a.n[i];
		return this;
	}
}
