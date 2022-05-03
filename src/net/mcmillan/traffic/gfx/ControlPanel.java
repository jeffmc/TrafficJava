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
		updateStateBtns();
	}
	
	private static final String[] carTypes = new String[] { "Car", "SUV", "Semi" };
	private JButton[] carBtns = new JButton[carTypes.length];
	
	private static final String PLAY_STR = "Play", PAUSE_STR = "Pause", STEP_STR = "Step";
	private JButton playBtn, pauseBtn, stepBtn;
	
	public ControlPanel() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		frame.add(makeAdderPane());
		frame.add(makeStatePane());
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel makeAdderPane() {
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
		pauseBtn = new JButton(PAUSE_STR);
		pauseBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		pauseBtn.addActionListener((e) -> {
			sim.pause();
			updateStateBtns();
		});
		stepBtn = new JButton(STEP_STR);
		stepBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
		stepBtn.addActionListener((e) -> {
			sim.step();
		});
		
		pane.add(playBtn);
		pane.add(pauseBtn);
		pane.add(stepBtn);
		return pane;
	}
	
	private void updateStateBtns() {
		boolean p = sim.isPaused();
		stepBtn.setEnabled(p);
		playBtn.setEnabled(p);
		pauseBtn.setEnabled(!p);
	}
}
