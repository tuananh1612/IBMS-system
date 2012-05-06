import java.util.ArrayList;
import java.util.Date;


public class PathTiming {

	public Date startTime;
	public int hour;
	public ArrayList<int[]> shortestPath;
	//The format of int array in shortestPath is 2 element: 
	//the stop and the route use that number(?)
	public int numberOfNodes;
	TimetableInfo.timetableKind kind;
	public int[] timeArray;
	//For reference purpose. Contain the service number of recommended bus
	public int[] serviceArray;
	
	public PathTiming(ArrayList<int[]> requiredShortestPath, Date requiredTime,
						int requiredHour) {
		shortestPath = requiredShortestPath;
		numberOfNodes = requiredShortestPath.size();
		startTime = requiredTime;
		hour = requiredHour;
		kind = TimetableInfo.timetableKind(startTime);
	}
	
	//Get the timing of the route
	public void getTiming() {
		database.openBusDatabase();
		//The current format of the shortest path is 2 stop each, the first
		//at index 2k is the start, the next one at 2k + 1 is the end on
		//one route, with k is the number of routes required in the path
		//Get the sequence of the route, get the index of start point and 
		//end point. Search through services time, find the service that
		//time at the start point is past the requiredTime (stored in 
		//variable hour). Get the time of that service at the end point.
		//Record in the timeArray.
		//Return the timeArray.
		//Repeat until the end of the path.
		//May record the route order in an array.
		int routeNumber = shortestPath.size() / 2;
		timeArray = new int[shortestPath.size()];
		serviceArray = new int[shortestPath.size()];
		for (int i = 0; i < timeArray.length; i ++) {
			timeArray[i] = 0;
			serviceArray[i] = 0;
		}
		//Check if the shortestPath is not null? (here or before passing
		//to the PathTimming class
		int[] routes = BusStopInfo.getRoutes();
		for (int i = 0; i < routeNumber; i ++) {
			int[] temp;
			temp = shortestPath.get(i * 2);
			int startPoint = temp[0];
			int currentRoute = temp[1];
			temp = shortestPath.get(i * 2 + 1);
			int endPoint = temp[0];
			updateTimeArray1Route(currentRoute, startPoint, endPoint, i);
		}
	}
	
	public void updateTimeArray1Route(int routeNumber, int start, 
									int end, int currentIndex) {
		//int[] sequence = BusStopInfo.getBusStops(routeNumber);
		int startLocation = start;
		int endLocation = end;
		//Get the index of start and end points in the route sequence
		/*
		for (int i = 0; i < sequence.length; i ++) {
			if (start == sequence[i]) {
				startLocation = i;
			}
			if (end == sequence[i]) {
				endLocation = i;
			}
		}
		*/
		//Get the services time and compare to get the service number.
		int[] servicesList = TimetableInfo.getServices(routeNumber, kind);
		int serviceNumber = 0;
		int currentTime;
		//Set the time for comparison
		if (currentIndex == 0) {
			currentTime = hour;
		}
		else {
			currentTime = timeArray[currentIndex * 2 - 1];
		}
		int[] serviceTime;
		int[] timingPoint;
		for (int i = 0; i < servicesList.length; i ++) {
			serviceTime = TimetableInfo.getServiceTimes(routeNumber, 
					kind, servicesList[i]);
			timingPoint = TimetableInfo.getTimingPoints(routeNumber, 
					kind, servicesList[i]);
			//Check if the start point is in the timingPoint array
			boolean startIsIn = false;
			boolean endIsIn = false;
			for (int j = 0; j < timingPoint.length; j ++) {
				if (timingPoint[j] == start) {
					startIsIn = true;
					startLocation = j;
				}
				if (timingPoint[j] == end) {
					endIsIn = true;
					endLocation = j;
				}
			}
			if ((startIsIn) && (endIsIn)) {
				serviceNumber = servicesList[i];
				break;
			}
		}
		serviceTime = TimetableInfo.getServiceTimes(routeNumber, 
				kind, serviceNumber);
		timeArray[currentIndex * 2] = serviceTime[startLocation];
		timeArray[currentIndex * 2 + 1] = serviceTime[endLocation];
		serviceArray[currentIndex * 2] = serviceNumber;
		serviceArray[currentIndex * 2 + 1] = serviceNumber;
	}
	
	//Check if the array found is possible. Return true if all elements
	//are not 0 (the time to reach the end of the route is found)
	public boolean checkTimeArray() {
		int result = 1;
		for (int i = 0; i < timeArray.length; i ++) {
			result = result * timeArray[i];
		}
		return (result != 0);
	}
	
	public int[] getTimeArray() {
		return timeArray;
	}
	
	public int[] getServiceArray() {
		return serviceArray;
	}
}
