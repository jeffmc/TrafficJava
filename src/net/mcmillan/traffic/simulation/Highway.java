package net.mcmillan.traffic.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import net.mcmillan.traffic.debug.table.HighwayDataListener;
import net.mcmillan.traffic.debug.table.HighwaySelectionListener;
import net.mcmillan.traffic.math.DVec2;
import net.mcmillan.traffic.math.ITransform2D;
import net.mcmillan.traffic.math.IVec2;
import net.mcmillan.traffic.simulation.physics.Collision;

// Meant to hold all data about vehicles and handle logic for vehicles, also utilized by TrafficRenderer
public class Highway {
	
	// Vehicles
	public ArrayList<Vehicle> vehicles = new ArrayList<>(), selectedVehicles = new ArrayList<>();
	public ArrayList<Vehicle> laneVehicles[];
	
	// Collisions
	public static final int COLLISION_BUFFER_MAX = 300;
	public ArrayList<Collision> collisionBuffer = new ArrayList<>();
	
	// Listeners
	private ArrayList<HighwayDataListener> dataListeners = new ArrayList<>();
	private ArrayList<HighwaySelectionListener> selectionListeners = new ArrayList<>();
	
	// Size
	public IVec2 size;
	
	// Lanes
	private int lanes, laneSize, highwayLength;
	private ITransform2D[] laneBoundingBoxes;
	public ITransform2D[] getLaneBoundingBoxes() { return laneBoundingBoxes; }
	
	// Constructor
	public Highway(int i_lanes, int i_laneSz, int i_highwayLength) {
		lanes = i_lanes;
		laneSize = i_laneSz;
		highwayLength = i_highwayLength;
		
		size = IVec2.make(highwayLength, lanes*laneSize);
		laneBoundingBoxes = new ITransform2D[lanes];
		IVec2 laneBox = IVec2.make(highwayLength, laneSize-1);
		for (int i=0;i<this.lanes;i++) {
			laneBoundingBoxes[i] = new ITransform2D(IVec2.make(0, i*laneSize), laneBox.copy());
		}
		
		laneVehicles = new ArrayList[lanes];
		for (int i=0;i<lanes;i++) {
			laneVehicles[i] = new ArrayList<Vehicle>();
		}
	}
	
	// Adding new vehicles
	public void addCar(Vehicle v) {
		vehicles.add(v);
		for (HighwayDataListener l : dataListeners) {
			l.vehicleAdded(vehicles.size()-1);
		}
	}
	
	public void addRandomCar() {
		Random r = new Random();
		Vehicle v = new Vehicle(
				DVec2.make(
					Math.random()/2*size.x(),
					getLaneCenteredY(r.nextInt(lanes), 48)), 
				DVec2.make(96, 48));
		addCar(v);
	}
	
	public void addCarAt(double x, double y) {
		Vehicle v = new Vehicle(
				DVec2.make(x, getLaneCenteredY(getLane(y), 48)), 
				DVec2.make(96, 48));
		addCar(v);
	}
	
	// Returns y-value of top edge of a car within a lane based on car height
	public double getLaneCenteredY(int lane, double height) {
		return (double)(lane*laneSize + laneSize/2) - (height/2);
	}
	
	public int getLane(Vehicle v) {
		return getLane(v.transform.cy());
	}
	
	public int getLane(double y) {
		return (int) (y / laneSize);
	}
	
	public int[] getLanes(Vehicle v) {
		int[] ls = new int[lanes];
		int j = 0;
		for (int i=0;i<laneBoundingBoxes.length;i++) {
			if (v.transform.intersects(laneBoundingBoxes[i])) {
				ls[j] = i;
				j++;
			}
		}
		return Arrays.copyOf(ls, j);
	}
	
	
	// Selection/deletion code
	public void selectRows(int[] indices) {
		selectedVehicles.clear();
		for (Vehicle v : vehicles) v.setSelected(false);
		for (int idx : indices) {
			Vehicle v = vehicles.get(idx);
			v.setSelected(true);
			selectedVehicles.add(v);
		}
	}
	public void selectArea(ITransform2D area) {
		selectedVehicles.clear();
		int[] selected = new int[vehicles.size()];
		int j=0;
		for (int i=0;i<vehicles.size();i++) {
			Vehicle v = vehicles.get(i);
			if (v.transform.intersects(area)) {
				v.setSelected(true);
				selectedVehicles.add(v);
				selected[j++] = i;
			} else {
				v.setSelected(false);
			}
		}
		int[] trimmed = Arrays.copyOf(selected, j);
		for (HighwaySelectionListener l : selectionListeners)
			l.selectionChanged(trimmed);
	}
	public void attemptSelectionDeletion() {
		if (vehicles.removeAll(selectedVehicles))
			for (HighwayDataListener l : dataListeners)
				l.refreshDataAndStructure();
	}
	
	// Ticking
	private long ticks = 0;
	public void update(long delta) {
		// Reset laneVehicles
		for (ArrayList<Vehicle> vs : laneVehicles)
			vs.clear();
		for (Vehicle v : vehicles) {
			int l = getLane(v);
			v.cachedLane = l;
			laneVehicles[l].add(v);
		}
		
		// Ticking
		for (ArrayList<Vehicle> vs : laneVehicles) {
			vs.sort(new Comparator<Vehicle>() {
				@Override
				public int compare(Vehicle a, Vehicle b) {
					return (int)(a.transform.x() - b.transform.x());
				}
			});
			if (vs.size() < 1) continue;
			Vehicle f = vs.get(0);
			for (int i=vs.size()-1;i>=0;i--) {
				Vehicle v = vs.get(i);
				v.pretick(ticks, f, highwayLength);
				f = v;
			}
		}
		for (Vehicle v : vehicles) v.tick();
		for (Vehicle v : vehicles) v.posttick();
		
		// Rollover
		for (Vehicle v : vehicles) 
			if (v.transform.x() > size.x()) v.transform.x(0);
		
		// Reflect update visually
		for (HighwayDataListener l : dataListeners)
			l.refreshData();
		ticks++;
	}
	
	// Listener management
	public void addDataListener(HighwayDataListener l) { dataListeners.add(l); }
	public boolean removeDataListener(HighwayDataListener l) { return dataListeners.remove(l); }
	
	public void addSelectionListener(HighwaySelectionListener l) { selectionListeners.add(l); }
	public boolean removeSelectionListener(HighwaySelectionListener l) { return selectionListeners.remove(l); }
	
}
