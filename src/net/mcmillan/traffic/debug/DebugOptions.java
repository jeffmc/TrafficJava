package net.mcmillan.traffic.debug;

import java.util.HashMap;

public class DebugOptions {

	
	public static final String DRAW_BRAKING_GRAPH = "Draw Braking Graph";
//			DRAW_QUADTREE = "Draw Quadtree";
	public static final String[] OPTIONS = new String[] { 
//			DRAW_QUADTREE,
			DRAW_BRAKING_GRAPH,
		};
	
	private HashMap<String, Boolean> options = new HashMap<>();
	
	public DebugOptions() {
		// Defaults
//		options.put(DRAW_QUADTREE, true);
		options.put(DRAW_BRAKING_GRAPH, false);
	}
	
	public void set(String key, boolean v) {
		options.put(key, Boolean.valueOf(v));
	}
	
	public boolean get(String key) {
		Boolean b = options.get(key);
		if (b == null) throw new IllegalArgumentException("Invalid debug option: '" + key + "'");
		return b;
	}

}
