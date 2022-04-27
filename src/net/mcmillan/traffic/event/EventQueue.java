package net.mcmillan.traffic.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import net.mcmillan.traffic.debug.EventQueueObserver;

public class EventQueue {

	private LinkedList<Event> queue;
	private LinkedList<Event> unloaded;
	private AtomicBoolean unloading = new AtomicBoolean(false);
	
	
	private ArrayList<EventQueueObserver> observers;
	
	public EventQueue() {
		queue = new LinkedList<>();
		unloaded = new LinkedList<>();
		observers = new ArrayList<>();
	}
	
	public void addObserver(EventQueueObserver obs) { observers.add(obs); }
	
	public boolean removeObserver(EventQueueObserver obs) { return observers.remove(obs); }
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	// Adds event to the end of queue
	public void push(Event e) {
		if (unloading.get()) {
			try {
				queue.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
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
	
	public void unload() {
		unloading.set(true);
		Collections.copy(unloaded, queue);
		queue.clear();
		unloading.set(false);
	}
	
	public boolean unloadedEmpty() {
		return unloaded.isEmpty();
	}
	
//	public LinkedList<Event> list() { return queue; }
	
	public void printList() {
		for (Event e : queue)
			System.out.println(e.getLabel());
	}
}
