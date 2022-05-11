package net.mcmillan.traffic.debug.table;

import java.awt.Color;

import javax.swing.JLabel;

public class JColorLabel extends JLabel {
	public Color c;
	
	public JColorLabel(Color col) {
		c = col;
	}
	
	@Override
	public Color getBackground() {
		return c;
	}
	
	@Override
	public boolean isOpaque() {
		return true;
	}
}
