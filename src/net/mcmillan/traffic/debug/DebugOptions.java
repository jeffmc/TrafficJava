package net.mcmillan.traffic.debug;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugOptions {

	public static final String 
			DRAW_BRAKING = "Draw Braking",
			DRAW_GRIDLINES = "Draw Gridlines",
			DRAW_FORCE_INDICATORS = "Draw Force Indicators",
			DRAW_LANE_BOUNDING_BOXES = "Draw Lane Bounding Boxes",
			DRAW_ROLLOVER = "Draw Rollover",
			DRAW_FRONT_DISTANCE = "Draw Front Distance";
	
	public static final String[] OPTIONS = new String[] {
			DRAW_BRAKING,
			DRAW_GRIDLINES,
			DRAW_FORCE_INDICATORS,
			DRAW_LANE_BOUNDING_BOXES,
			DRAW_ROLLOVER,
			DRAW_FRONT_DISTANCE,
		};
	
	private HashMap<String, Boolean> options = new HashMap<>();
	
	public DebugOptions() {
		// Defaults
		options.put(DRAW_BRAKING, false);
		options.put(DRAW_GRIDLINES, false);
		options.put(DRAW_FORCE_INDICATORS, false);
		options.put(DRAW_LANE_BOUNDING_BOXES, false);
		options.put(DRAW_ROLLOVER, false);
		options.put(DRAW_FRONT_DISTANCE, false);
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
