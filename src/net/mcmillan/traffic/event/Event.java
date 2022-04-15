package net.mcmillan.traffic.event;

public class Event {
	
	public static final int KEY_TYPED = 0x00, KEY_RELEASED = 0x01, KEY_PRESSED = 0x02,
			MOUSE_RELEASED = 0x10, MOUSE_PRESSED = 0x11, MOUSE_EXITED = 0x12, MOUSE_ENTERED = 0x13,
			MOUSE_CLICKED = 0x14, MOUSE_MOVED = 0x15, MOUSE_DRAGGED = 0x16, MOUSE_WHEEL_MOVED = 0x17;
	
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
		if (code == KEY_TYPED) lbl += ": '" + data[0] + "'";
	}
	
	public static String eventTypeFromCode(int eventcode) {
		switch (eventcode) {
		case KEY_TYPED:
			return "KeyTyped";
		case KEY_RELEASED:
			return "KeyReleased";
		case KEY_PRESSED:
			return "KeyPressed";
		case MOUSE_RELEASED:
			return "MouseReleased";
		case MOUSE_PRESSED:
			return "MousePressed";
		case MOUSE_EXITED:
			return "MouseExited";
		case MOUSE_ENTERED:
			return "MouseEntered";
		case MOUSE_CLICKED:
			return "MouseClicked";
		case MOUSE_MOVED:
			return "MouseMoved";
		case MOUSE_DRAGGED:
			return "MouseDragged";
		case MOUSE_WHEEL_MOVED:
			return "MouseWheelMoved";
		}
		throw new IllegalArgumentException("Invalid Event Code: " + eventcode);
	}
	
	@Override
	public String toString() {
		return lbl;
	}
	
}
