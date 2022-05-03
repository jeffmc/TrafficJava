package net.mcmillan.traffic.gfx;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class ControlPanel {

	private JFrame frame = new JFrame("Control Panel");
	public JFrame getFrame() { return frame; }
	
	private TrafficSimulation sim;
	public void setSimulation(TrafficSimulation s) { 
		sim = s; 
	}
	
	private static final String[] carTypes = new String[] { "Car", "SUV", "Semi" };
	private JButton[] carBtns = new JButton[carTypes.length];
	
	public ControlPanel() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = new Dimension(200, 200);
		frame.setMinimumSize(size);
		frame.setPreferredSize(size);
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Add Vehicles"));
		ActionListener l = (e) -> {
			sim.addCar();
		};
		for (int i=0;i<carTypes.length;i++) {
//			long start = System.currentTimeMillis();
			JButton btn = new JButton("Add " + carTypes[i]);
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn.addActionListener(l);
			pane.add(btn);
			pane.add(Box.createRigidArea(new Dimension(0, 5)));
			carBtns[i] = btn;
//			System.out.println("Took " + (System.currentTimeMillis()-start) + "ms!");
		}
		frame.add(pane);
		
		frame.pack();
		frame.setVisible(true);
	}
}
