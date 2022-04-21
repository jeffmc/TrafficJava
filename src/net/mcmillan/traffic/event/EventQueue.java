package net.mcmillan.traffic.event;

import java.util.ArrayList;
import java.util.LinkedList;

public class EventQueue {

	private LinkedList<Event> queue;
	
	private ArrayList<EventQueueObserver> observers;
	
	public EventQueue() {
		queue = new LinkedList<>();
		observers = new ArrayList<>();
	}
	
	public void addObserver(EventQueueObserver obs) { observers.add(obs); }
	
	public boolean removeObserver(EventQueueObserver obs) { return observers.remove(obs); }
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	// Adds event to the end of queue
	public void push(Event e) {
		queue.add(e);
		for (EventQueueObserver o : observers)
			o.eventPushed(e, queue.size() - 1);
//		printList();
	}
	
	// Removes the first event of the queue
	public Event pop() {
		Event popped = queue.pop();
		for (EventQueueObserver o : observers)
			o.eventRemoved(popped, 0);
		return popped;
	}
	
	public LinkedList<Event> list() { return queue; }
	
	public void printList() {
		for (Event e : queue)
			System.out.println(e.getLabel());
	}
}
