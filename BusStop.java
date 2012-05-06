import java.util.ArrayList;


public class BusStop {

	private String stopName;
	private ArrayList<int[]> nextStops;
	
	public BusStop(String name, int[] requiredStopID) {
		stopName = name;
	}
	
	public String getName() {
		return stopName;
	}
	
	//Add an array with 3 elements into the list. The format is:
	//stopID, route, nextStop (0 if there is no next stop)
	public void updateNextStop(int[] requiredData) {
		nextStops.add(requiredData);
	}
	
	public ArrayList<int[]> getNextStop() {
		return nextStops;
	}
}
