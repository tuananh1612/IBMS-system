import java.util.ArrayList;
import java.util.Date;


public class PathTiming {

	public Date startTime;
	public int hour;
	public ArrayList<int[]> shortestPath;
	public int numberOfNodes;
	TimetableInfo.timetableKind kind;
	
	public PathTiming(ArrayList<int[]> requiredShortestPath, Date requiredTime,
						int requiredHour) {
		shortestPath = requiredShortestPath;
		numberOfNodes = requiredShortestPath.size();
		startTime = requiredTime;
		hour = requiredHour;
		kind = TimetableInfo.timetableKind(startTime);
	}
	
	//Get the duration of the route
	public int getDuration() {
		database.openBusDatabase();
		int index = 0;
		int[] currentStop = shortestPath.get(index);
		int[] nextStop = shortestPath.get(index + 1);
		int[] services = TimetableInfo.getServices(currentStop[1], kind);
		int i = 0;
		
		return 0;
	}
}
