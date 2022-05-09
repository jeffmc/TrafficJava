package net.mcmillan.traffic.debug;

import java.util.HashMap;

public class DebugOptions {

	
	public static final String DRAW_QUADTREE = "Draw Quadtree";
	public static final String[] OPTIONS = new String[] { DRAW_QUADTREE };
	
	private HashMap<String, Boolean> options = new HashMap<>();
	
	public DebugOptions() {
		// Defaults
		options.put(DRAW_QUADTREE, true);
	}
	
	public void set(String key, boolean v) {
		options.put(key, Boolean.valueOf(v));
	}
	
	public boolean get(String key) {
		return options.get(key);
	}

}
