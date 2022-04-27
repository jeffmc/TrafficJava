package net.mcmillan.traffic.debug;

import net.mcmillan.traffic.event.Event;

public interface EventQueueObserver {

	public void eventPushed(Event e, int idx);
	public void eventRemoved(Event e, int idx);
	
}
