package net.mcmillan.traffic.event;

public class Event {
	
	public int code; // XXXXOOOO, X's are device type, O's are event type
	private String lbl;
	public String getLabel() { return lbl; }
	private Object[] data;
	
	public Event(int eventcode) {
		this(eventcode, null);
	}
	
	public Event(int eventcode, Object[] data) {
		code = eventcode;
		lbl = eventTypeFromCode(code);
	}
	
	public static String eventTypeFromCode(int eventcode) {
		switch (eventcode) {
		case 0x00:
			return "KeyTyped";
		case 0x01:
			return "KeyReleased";
		case 0x02:
			return "KeyPressed";
		case 0x10:
			return "MouseReleased";
		case 0x11:
			return "MousePressed";
		case 0x12:
			return "MouseExited";
		case 0x13:
			return "MouseEntered";
		case 0x14:
			return "MouseClicked";
		case 0x15:
			return "MouseMoved";
		case 0x16:
			return "MouseDragged";
		case 0x17:
			return "MouseWheelMoved";
		}
		throw new IllegalArgumentException("Invalid Event Code: " + eventcode);
	}
	
	@Override
	public String toString() {
		return lbl;
	}
	
}
