package net.mcmillan.traffic.debug;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.mcmillan.traffic.debug.table.HighwayTable;
import net.mcmillan.traffic.simulation.TrafficSimulation;

public class ControlPanel {

	private static final int SECTION_SEPERATION = 10;
	private static final int BUTTON_SEPERATION = 3;
	
	private JFrame frame = new JFrame("Control Panel");
	public JFrame getFrame() { return frame; }
	
	private TrafficSimulation sim;
	public void setSimulation(TrafficSimulation s) { 
		sim = s;
		updateDebugBtns();
		updateStateBtns();
		highwayTable.setSimulation(s);
	}
	
	private static final String[] carTypes = new String[] { "Car", "SUV", "Semi" };
	private JButton[] carBtns = new JButton[carTypes.length];
	
	private JButton playPauseBtn, stepBtn;
	private static final String PLAY_STR = "Play", PLAY_TOOLTIP = "Continously run the simulation",
			PAUSE_STR = "Pause", PAUSE_TOOLTIP = "Stop the simulation",
			STEP_STR = "Step", STEP_TOOLTIP = "Tick the simulation once",
			MERGE_TO_ROOT_STR = "Merge to Root", MAX_SPLIT_STR = "Split to Max Depth", RANDOMIZE_STR = "Randomize tree";
	
	private JPanel trafficPane;
	private HighwayTable highwayTable;
	
	private HashMap<String, JToggleButton> debugBtns = new HashMap<>();
	
	public ControlPanel() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		frame.add(makeAdderPane());
		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
		frame.add(makeStatePane());
		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
//		frame.add(makeQuadtreePane());
//		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
		frame.add(makeDebugOptionsPane());
		frame.add(Box.createVerticalStrut(SECTION_SEPERATION));
		frame.add(makeTrafficPane());
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel makeAdderPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Add Vehicles"));
		ActionListener l = (e) -> sim.addCar(); // TODO: Make functionality for more classes of vehicles here!
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
		
		playPauseBtn = new JButton(PLAY_STR);
		playPauseBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		playPauseBtn.addActionListener((e) -> {
			sim.togglePlayPause();
			updateStateBtns();
		});
		stepBtn = new JButton(STEP_STR);
		stepBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		stepBtn.addActionListener((e) -> {
			sim.step();
			updateStateBtns();
		});
		stepBtn.setToolTipText(STEP_TOOLTIP);
		
		pane.add(playPauseBtn);
		pane.add(Box.createHorizontalStrut(BUTTON_SEPERATION));
		pane.add(stepBtn);
		
		return pane;
	}

	private void updateStateBtns() {
		boolean p = sim.isPaused();
		stepBtn.setEnabled(p);
		playPauseBtn.setText(p?PLAY_STR:PAUSE_STR);
		playPauseBtn.setToolTipText(p?PLAY_TOOLTIP:PAUSE_TOOLTIP);
	}
	
	/*private JPanel makeQuadtreePane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Quadtree"));
		JButton mergeBtn = new JButton(MERGE_TO_ROOT_STR);
		mergeBtn.addActionListener((e)->sim.highway.getQuadtreeRoot().attemptMerge());
		JButton splitBtn = new JButton(MAX_SPLIT_STR);
		splitBtn.addActionListener((e)->sim.highway.getQuadtreeRoot().maxSplit());
		JButton randomizeBtn = new JButton(RANDOMIZE_STR);
		randomizeBtn.addActionListener((e)->sim.highway.getQuadtreeRoot().randomize());

		mergeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		randomizeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(mergeBtn);
		pane.add(Box.createVerticalStrut(BUTTON_SEPERATION));
		pane.add(splitBtn);
		pane.add(Box.createVerticalStrut(BUTTON_SEPERATION));
		pane.add(randomizeBtn);
		return pane;
	}*/
	
	private JPanel makeDebugOptionsPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Debug Options"));
		for (String opt : DebugOptions.OPTIONS) {
			JToggleButton btn = new JToggleButton(opt);
			btn.addActionListener((e) -> sim.debugOptions.set(opt, btn.isSelected()));
			debugBtns.put(opt, btn);
			pane.add(btn);
		}
		return pane;
	}
	
	private void updateDebugBtns() {
		for (String opt : DebugOptions.OPTIONS) {
			debugBtns.get(opt).setSelected(sim.debugOptions.get(opt));
		}
	}
	
	private JPanel makeTrafficPane() {
		trafficPane = new JPanel();
		trafficPane.setLayout(new BoxLayout(trafficPane, BoxLayout.LINE_AXIS));
		trafficPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Traffic"));
		highwayTable = new HighwayTable();
		trafficPane.add(highwayTable.getComponent());
		return trafficPane;
	}
	
}
