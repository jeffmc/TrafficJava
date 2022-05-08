package net.mcmillan.traffic.debug;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.mcmillan.traffic.simulation.TrafficSimulation;

public class ControlPanel {

	private static final int SECTION_SEPERATION = 10;
	private static final int BUTTON_SEPERATION = 3;
	
	private JFrame frame = new JFrame("Control Panel");
	public JFrame getFrame() { return frame; }
	
	private TrafficSimulation sim;
	public void setSimulation(TrafficSimulation s) { 
		sim = s;
		updateStateBtns();
		highwayTable.setSimulation(s);
	}
	
	private static final String[] carTypes = new String[] { "Car", "SUV", "Semi" };
	private JButton[] carBtns = new JButton[carTypes.length];
	
	private static final String PLAY_STR = "Play", PAUSE_STR = "Pause", STEP_STR = "Step";
	private JButton playBtn, pauseBtn, stepBtn;
	
	private JPanel trafficPane;
	private HighwayTable highwayTable;
	
	public ControlPanel() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		frame.add(makeAdderPane());
		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
		frame.add(makeStatePane());
		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
		frame.add(makeTrafficPane());
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel makeAdderPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Add Vehicles"));
		ActionListener l = (e) -> { // TODO: Make functionality for more classes of vehicles here!
			sim.addCar();
		};
		for (int i=0;i<carTypes.length;i++) {
			JButton btn = new JButton(carTypes[i]);
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn.addActionListener(l);
			pane.add(btn);
			if (i<carTypes.length-1) pane.add(Box.createHorizontalStrut(BUTTON_SEPERATION));
			carBtns[i] = btn;
		}
		return pane;
	}
	private JPanel makeStatePane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "State"));
		
		playBtn = new JButton(PLAY_STR);
		playBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		playBtn.addActionListener((e) -> {
			sim.play();
			updateStateBtns();
		});
		playBtn.setToolTipText("Continously run the simulation");
		pauseBtn = new JButton(PAUSE_STR);
		pauseBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		pauseBtn.addActionListener((e) -> {
			sim.pause();
			updateStateBtns();
		});
		pauseBtn.setToolTipText("Stop the simulation");
		stepBtn = new JButton(STEP_STR);
		stepBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		stepBtn.addActionListener((e) -> {
			sim.step();
		});
		stepBtn.setToolTipText("Tick the simulation once");
		
		pane.add(playBtn);
		pane.add(Box.createHorizontalStrut(BUTTON_SEPERATION));
		pane.add(pauseBtn);
		pane.add(Box.createHorizontalStrut(BUTTON_SEPERATION));
		pane.add(stepBtn);
		
		return pane;
	}
	
	private JPanel makeTrafficPane() {
		trafficPane = new JPanel();
		trafficPane.setLayout(new BoxLayout(trafficPane, BoxLayout.LINE_AXIS));
		trafficPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Traffic"));
		highwayTable = new HighwayTable();
		trafficPane.add(highwayTable.getComponent());
		return trafficPane;
	}
	
	private void updateStateBtns() {
		boolean p = sim.isPaused();
		stepBtn.setEnabled(p);
		playBtn.setEnabled(p);
		pauseBtn.setEnabled(!p);
	}
}
