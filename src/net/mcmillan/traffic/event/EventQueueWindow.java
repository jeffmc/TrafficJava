package net.mcmillan.traffic.event;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class EventQueueWindow {
	
	private JFrame frame = new JFrame("Event Queue");
	private JList<Event> list = new JList<>();
	private JCheckBox[] filterBtns;
	private int[] filterMasks, filterVals;
	
	private EventListModel model = null;
	private ListModel<Event> nullModel = new ListModel<Event>() { // Null model
		@Override
		public void removeListDataListener(ListDataListener l) { }
		@Override
		public int getSize() { return 0; }
		@Override
		public Event getElementAt(int index) { return null; }
		@Override
		public void addListDataListener(ListDataListener l) { }
	};
	
	private EventQueue eventq;
	
	public EventQueueWindow(EventQueue eq) {
		frame.addWindowListener(new WindowAdapter() {
			@Override 
			public void windowClosing(WindowEvent e) {
				if (model != null) model.dispose(); // TODO: Fix nullpointerexception on closing this window!
				frame.setVisible(false);
				frame.dispose();
			}
		});
		
		JScrollPane jsp = new JScrollPane(list);
		jsp.setAutoscrolls(true);
		Dimension size = new Dimension(300,500);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setResizable(true);
		
		JPanel fp = new JPanel();
		fp.setLayout(new FlowLayout(FlowLayout.LEFT));
		String[] filterLabels = new String[] { "Keyboard","Mouse","KeyTyped","KeyReleased","KeyPressed", "MouseReleased","MousePressed",
				"MouseExited","MouseEntered","MouseClicked","MouseMoved","MouseDragged","MouseWheelMoved" };
		filterMasks = new int[] { 0xf0,0xf0, 0xff,0xff,0xff, 0xff,0xff,
				0xff,0xff,0xff,0xff,0xff,0xff };
		filterVals = new int[] {  0x00,0x10, 0x00,0x01,0x02, 0x10, 0x11,
				0x12,0x13,0x14,0x15,0x16,0x17 };
		filterBtns = new JCheckBox[filterLabels.length];
		for (int i=0;i<filterLabels.length;i++) {
			JCheckBox box = new JCheckBox(filterLabels[i]);
			filterBtns[i] = box;
			fp.add(box);
		}
		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.add(fp);
		frame.add(jsp);
		frame.pack();
		
		setEventQueue(eq);
	}
	
	public void setEventQueue(EventQueue eq) {
		if (eq != null) {
			eventq = eq;
			model = new EventListModel(eventq);
			list.setModel(model);
		} else {
			eventq = eq;
			model = null;
			list.setModel(nullModel);
		}
	}
	
	public void removeEventQueue() {
		setEventQueue(null);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	class EventListModel implements ListModel<Event>, EventQueueObserver {
		ArrayList<Event> filteredEvents = new ArrayList<>();
		private EventQueue eq;
		EventListModel(EventQueue eq) {
			if (eq == null) throw new IllegalArgumentException("Queue cannot be null");
			eq.addObserver(this);
		}
		void dispose() {
			eq.removeObserver(this);
			listeners = null;
		}
		private ArrayList<ListDataListener> listeners = new ArrayList<>();
		@Override
		public int getSize() { return filteredEvents.size(); }
		@Override
		public Event getElementAt(int index) { return filteredEvents.get(index); }
		@Override
		public void addListDataListener(ListDataListener l) { listeners.add(l); }
		@Override
		public void removeListDataListener(ListDataListener l) { listeners.remove(l); }
		@Override
		public void eventPushed(Event e, int idx) {
			boolean valid = false;
			for (int i=0;i<filterMasks.length;i++) {
				JCheckBox btn = filterBtns[i];
				if (btn.isSelected()) {
					int masked = e.code & filterMasks[i];
					if (masked == filterVals[i]) {
						valid = true;
					};
				}
			}
			if (!valid) return;
			filteredEvents.add(e);
			int fidx = filteredEvents.size()-1;
			for (ListDataListener l : listeners)
				l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, fidx, fidx+1)); // TODO: Rethink "this" use for source
			
		}
		@Override
		public void eventRemoved(Event e, int idx) {
			int fidx = filteredEvents.indexOf(e);
			if (fidx >= 0) filteredEvents.remove(fidx);
			for (ListDataListener l : listeners)
				l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, fidx, fidx+1)); // TODO: Rethink "this" use for source
		}
		
	}
}
