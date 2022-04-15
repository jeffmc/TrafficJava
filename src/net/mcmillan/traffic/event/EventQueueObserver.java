package net.mcmillan.traffic.event;

public interface EventQueueObserver {

	public void eventPushed(Event e, int idx);
	public void eventRemoved(Event e, int idx);
	
}
