import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.text.DefaultEditorKit.CopyAction;


public class PathTiming {

	public Date startTime;
	public int hour;
	public static ArrayList<int[]> shortestPath;
	public static ArrayList<int[]> workingPath;
	public static ArrayList<int[]> backUpPath;
	//The format of int array in shortestPath is 2 element: 
	//the stop and the route use that number(?)
	public int numberOfNodes;
	TimetableInfo.timetableKind kind;
	public int[] timeArray;
	//For reference purpose. Contain the service number of recommended bus
	public int[] serviceArray;
	
	public PathTiming(ArrayList<int[]> requiredShortestPath, Date requiredTime,
						int requiredHour) {
		shortestPath = new ArrayList<int[]>();
		shortestPath = requiredShortestPath;
		numberOfNodes = requiredShortestPath.size();
		startTime = requiredTime;
		hour = requiredHour;
		kind = TimetableInfo.timetableKind(startTime);
		backUpPath = new ArrayList<int[]>();
		workingPath = new ArrayList<int[]>();
		backUpPath.addAll(requiredShortestPath);
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
		convertPath();
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
			//serviceTime = TimetableInfo.getServiceTimes(routeNumber, 
			//		kind, servicesList[i]);
			//timingPoint = TimetableInfo.getTimingPoints(routeNumber, 
			//		kind, servicesList[i]);
			serviceTime = TimetableInfo.getServiceTimes(routeNumber, kind, i);
			timingPoint = TimetableInfo.getTimingPoints(routeNumber, kind, i);
			//Check if the start point is in the timingPoint array
			boolean startIsIn = false;
			boolean endIsIn = false;
			startLocation = start;
			for (int j = 0; j < timingPoint.length; j ++) {
				if (timingPoint[j] == start) {
					startIsIn = true;
					startLocation = j;
				}
				if ((startLocation < j) &&(timingPoint[j] == end)) {
					endIsIn = true;
					endLocation = j;
					break;
				}
			}
			if ((startIsIn) && (endIsIn)) {
				//serviceNumber = servicesList[i];
				serviceNumber = i;
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
	
	public void convertPath() {
		//Convert the stop IDs to suitable IDs for processing
		int noOfRoute = shortestPath.size() / 2;
		DataManager manager = new DataManager(startTime);
		ArrayList<String> stopName = manager.getStopNames();
		int[][] stopIDs = manager.getStopID();
		int[] routes = BusStopInfo.getRoutes();
		int routeNumber;
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < shortestPath.size(); i ++) {
			//Get the name of the stop
			String name = BusStopInfo.getFullName((shortestPath.get(i))[0]);
			temp.add(name);
		}
		//Resolve stop IDs in pair
		for (int i = 0; i < noOfRoute; i ++) {
			int startIndex = stopName.indexOf(temp.get(i * 2));
			int endIndex = stopName.indexOf(temp.get(i * 2 + 1));
			ArrayList<Integer> temp1 = new ArrayList<Integer>();
			ArrayList<Integer> temp2 = new ArrayList<Integer>();
			//Get the suitable IDs for start and end points
			for (int j = 0; j < 10; j ++) {
				if (BusStopInfo.isTimingPointOnRoute(stopIDs[startIndex][j], 
						(shortestPath.get(i * 2))[1])) {
					temp1.add(stopIDs[startIndex][j]);
				}
			}
			for (int j = 0; j < 10; j ++) {
				if (BusStopInfo.isTimingPointOnRoute(stopIDs[endIndex][j], 
						(shortestPath.get(i * 2 + 1))[1])) {
					temp2.add(stopIDs[endIndex][j]);
				}
			}
			//Check and register final ID to shortestPath
			int[] startEntry = new int[2];
			int[] endEntry = new int[2];
			startEntry[1] = (shortestPath.get(i * 2))[1];
			endEntry[1] = (shortestPath.get(i * 2 + 1))[1];
			if (temp1.size() == 1) {
				startEntry[0] = temp1.get(0);
			}
			if (temp2.size() == 1) {
				endEntry[0] = temp2.get(0);
			}
			//There are more than one IDs
			if ((temp1.size() > 1) && (temp2.size() == 1)) {
				int k = 0;
				while (temp1.get(k) > temp2.get(0)) {
					if (BusStopInfo.getNextStop(temp1.get(k), startEntry[1]) != 0) {
						startEntry[0] = temp1.get(k);
						break;
					}
					k ++;
				}
			}
			else if ((temp1.size() == 1) && (temp2.size() > 1)) {
				int k = 0;
				while (temp1.get(0) > temp2.get(k)) {
					k ++;
				}
				endEntry[0] = temp2.get(k);
			}
			else {
				boolean found = false;
				for (int t = 0; t < temp1.size(); t ++) {
					for (int k = 0; k < temp2.size(); k ++) {
						if (isMatched(startEntry[1], temp1.get(t), temp2.get(k))) {
							startEntry[0] = temp1.get(t);
							endEntry[0] = temp2.get(k);
							break;
						}
					}
					if (found) {
						break;
					}
				}
			}
			workingPath.add(startEntry);
			workingPath.add(endEntry);
		}
		shortestPath.clear();
		shortestPath.addAll(workingPath);
	}
	
	public boolean isMatched(int routeNumber, int start, int end) {
		boolean result;
		boolean found = false;
		int startIndex, endIndex;
		if (end < start) {
			result = false;
		}
		else {
			//Check if there is a service run from start to end
			int[] services = TimetableInfo.getServices(routeNumber, kind);
			for (int i = 0; i < services.length; i ++) {
				//int[] sequence = TimetableInfo.getTimingPoints(routeNumber, 
				//		kind, services[i]);
				int[] sequence = TimetableInfo.getTimingPoints(routeNumber, kind, i);
				startIndex = start;
				found = false;
				for (int k = 0; k < sequence.length; k ++) {
					if (sequence[k] == start) {
						startIndex = k;
					}
					if ((sequence[k] == end) && (k > startIndex)) {
						endIndex = k;
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
			if (found) {
				result = true;
			}
			else {
				result = false;
			}
		}
		return result;
	}
}
