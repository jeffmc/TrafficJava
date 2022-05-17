package net.mcmillan.traffic.debug;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugOptions {

	public static final String 
			DRAW_BRAKING_GRAPH = "Draw Braking Graph (TODO)",
			DRAW_GRIDLINES = "Draw Gridlines",
			DRAW_FORCE_INDICATORS = "Draw Force Indicators",
			DRAW_LANE_BOUNDING_BOXES = "Draw Lane Bounding Boxes",
			DRAW_ROLLOVER = "Draw Rollover";
	
	public static final String[] OPTIONS = new String[] {
			DRAW_BRAKING_GRAPH,
			DRAW_GRIDLINES,
			DRAW_FORCE_INDICATORS,
			DRAW_LANE_BOUNDING_BOXES,
			DRAW_ROLLOVER
		};
	
	private HashMap<String, Boolean> options = new HashMap<>();
	
	public DebugOptions() {
		// Defaults
		options.put(DRAW_BRAKING_GRAPH, false);
		options.put(DRAW_GRIDLINES, false);
		options.put(DRAW_FORCE_INDICATORS, false);
		options.put(DRAW_LANE_BOUNDING_BOXES, false);
		options.put(DRAW_ROLLOVER, false);
	}
	
	public void set(String key, boolean v) {
		options.put(key, Boolean.valueOf(v));
	}
	
	public boolean get(String key) {
		Boolean b = options.get(key);
		if (b == null) throw new IllegalArgumentException("Invalid debug option: '" + key + "'");
		return b;
	}

	private HashMap<String, ArrayList<OptionListener>> listeners = new HashMap<>();
	public interface OptionListener { public void stateChanged(boolean state); }
	public void addListener(String opt, OptionListener l) {
		ArrayList<OptionListener> list = listeners.get(opt);
		if (list == null) {
			list = new ArrayList<>();
			listeners.put(opt, list);
		}
		list.add(l);
	}
	public boolean removeListener(String opt, OptionListener l) {
		ArrayList<OptionListener> list = listeners.get(opt);
		if (list == null) return false;
		return list.remove(l);
	}
}
