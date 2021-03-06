import java.util.ArrayList;
import java.util.Date;


public class DataManager {

	public int[][] routeGraph;
	public ArrayList<String> stopName;
	public int[][] stopID;
	TimetableInfo.timetableKind kind;
	
	public DataManager(Date requiredDate) {
		kind = TimetableInfo.timetableKind(requiredDate);
	}
	
	public void createRouteGraph() {
		database.openBusDatabase();
		int[] routes = BusStopInfo.getRoutes();
		int[] busStops1 = BusStopInfo.getBusStops(routes[0]);
		int[] busStops2 = BusStopInfo.getBusStops(routes[1]);
		int[] busStops3 = BusStopInfo.getBusStops(routes[2]);
		int[] busStops4 = BusStopInfo.getBusStops(routes[3]);
		
		//Add the name of stops into the list, repeat this for all routes
		/*
		for (int i = 0; i < busStops1.length; i ++) {
			stopName.add(BusStopInfo.getFullName(busStops1[i]));
		}
		for (int i = 0; i < busStops2.length; i ++) {
			stopName.add(BusStopInfo.getFullName(busStops2[i]));
		}
		for (int i = 0; i < busStops3.length; i ++) {
			stopName.add(BusStopInfo.getFullName(busStops3[i]));
		}
		for (int i = 0; i < busStops4.length; i ++) {
			stopName.add(BusStopInfo.getFullName(busStops4[i]));
		}
		*/
		createStopNameList();
		createStopIDArray();
		//Create the routeGraph
		//check the names, get their indexes, mark their connections
		routeGraph = new int[stopName.size()][stopName.size()];
		//Initialise the matrix to 0
		for (int i = 0; i < routeGraph.length; i ++) {
			for (int j = 0; j < routeGraph.length; j ++) {
				routeGraph[i][j] = 0;
			}
		}
		//Mark their connections
		//int temp;
		//int duration;
		/*
		for (int i = 0; i < routeGraph.length; i ++) {
			for (int j = 0; j < 10; j ++) {
				if (stopID[i][j] != 0) {
					if (stopID[i][j] < busStops1[busStops1.length - 1]) {
						if ((temp = BusStopInfo.getNextStop(stopID[i][j], routes[0])) != 0) {
							String name = BusStopInfo.getFullName(temp);
							duration = getDurationToNextStop(routes[0], stopID[i][j], temp);
							//routeGraph[i][stopName.indexOf(name)] = 1;
							if (routeGraph[i][stopName.indexOf(name)] != 0) {
								if (routeGraph[i][stopName.indexOf(name)] > duration) {
									routeGraph[i][stopName.indexOf(name)] = duration;
								}
							}
							else {
								routeGraph[i][stopName.indexOf(name)] = duration;
							}
						}
					}
					else if (stopID[i][j] < busStops2[busStops2.length - 1]) {
						if ((temp = BusStopInfo.getNextStop(stopID[i][j], routes[1])) != 0) {
							String name = BusStopInfo.getFullName(temp);
							duration = getDurationToNextStop(routes[1], stopID[i][j], temp);
							//routeGraph[i][stopName.indexOf(name)] = 1;
							//routeGraph[i][stopName.indexOf(name)] = duration;
							if (routeGraph[i][stopName.indexOf(name)] != 0) {
								if (routeGraph[i][stopName.indexOf(name)] > duration) {
									routeGraph[i][stopName.indexOf(name)] = duration;
								}
							}
							else {
								routeGraph[i][stopName.indexOf(name)] = duration;
							}
						}
					}
					else if (stopID[i][j] < busStops3[busStops3.length - 1]) {
						if ((temp = BusStopInfo.getNextStop(stopID[i][j], routes[2])) != 0) {
							String name = BusStopInfo.getFullName(temp);
							duration = getDurationToNextStop(routes[2], stopID[i][j], temp);
							//routeGraph[i][stopName.indexOf(name)] = 1;
							//routeGraph[i][stopName.indexOf(name)] = duration;
							if (routeGraph[i][stopName.indexOf(name)] != 0) {
								if (routeGraph[i][stopName.indexOf(name)] > duration) {
									routeGraph[i][stopName.indexOf(name)] = duration;
								}
							}
							else {
								routeGraph[i][stopName.indexOf(name)] = duration;
							}
						}
					}
					else {
						if ((temp = BusStopInfo.getNextStop(stopID[i][j], routes[3])) != 0) {
							String name = BusStopInfo.getFullName(temp);
							duration = getDurationToNextStop(routes[3], stopID[i][j], temp);
							//routeGraph[i][stopName.indexOf(name)] = 1;
							//routeGraph[i][stopName.indexOf(name)] = duration;
							if (routeGraph[i][stopName.indexOf(name)] != 0) {
								if (routeGraph[i][stopName.indexOf(name)] > duration) {
									routeGraph[i][stopName.indexOf(name)] = duration;
								}
							}
							else {
								routeGraph[i][stopName.indexOf(name)] = duration;
							}
						}
					}
				}
			}
			
		}
		*/
		for (int i = 0; i < routeGraph.length; i ++) {
			//Loop through the IDs array. Check for routes
			for (int j = 0; j < 10; j ++) {
				if (stopID[i][j] != 0) {
					for (int k = 0; k < routes.length; k ++) {
						if (BusStopInfo.isTimingPointOnRoute(stopID[i][j], routes[k])) {
							//Check if there is a next stop. If there is get the name.
							//From the name, get index. Get duration, assign smallest.
							int tempID;
							if ((tempID = BusStopInfo.getNextStop(
									stopID[i][j], routes[k])) != 0) {
								String name = BusStopInfo.getFullName(tempID);
								int index = stopName.indexOf(name);
								int duration = getDurationToNextStop(routes[k], 
										stopID[i][j], tempID);
								System.out.println("duration: " + duration);
								if (routeGraph[i][index] != 0) {
									if (routeGraph[i][index] > duration) {
										routeGraph[i][index] = duration;
									}
								}
								else {
									routeGraph[i][index] = duration;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public int[][] getRouteGraph() {
		return routeGraph;
	}
	
	public ArrayList<String> getStopNames() {
		return stopName;
	}
	
	public int[][] getStopID() {
		return stopID;
	}
	
	public int getDurationToNextStop(int routeNumber, int start, int end) {
		//Get the services of the route in a day. Get the service with
		//both start and end in the list.
		//Get the time.
		int[] services = TimetableInfo.getServices(routeNumber, kind);
		boolean startIsIn = false;
		boolean endIsIn = false;
		int[] timingPoint;
		int[] serviceTime;
		int serviceNumber = -1;
		int startIndex = 0;
		int endIndex = 0;
		int duration = 0;
		int temp;
		//System.out.println("Start: " + start);
		//System.out.println("End: " + end);
		//System.out.println();
		for (int i = 0; i < services.length; i ++) {
			//timingPoint = TimetableInfo.getTimingPoints(routeNumber, 
			//		kind, services[i]);
			timingPoint = TimetableInfo.getTimingPoints(routeNumber, kind, i);
			//startIsIn = false;
			//endIsIn = false;
			startIndex = -1;
			endIndex = -1;
			for (int j = 0; j < timingPoint.length; j ++) {
				if (timingPoint[j] == start) {
					//startIsIn = true;
					startIndex = j;
				}
				if (timingPoint[j] == end && startIndex < j && startIndex >= 0) {
					//endIsIn = true;
					endIndex = j;
					serviceNumber = i;
					//System.out.println("Service: " + serviceNumber);
					serviceTime = TimetableInfo.getServiceTimes(routeNumber, 
								kind, serviceNumber);
					temp = serviceTime[endIndex] - serviceTime[startIndex];
					if (duration == 0) {
						duration = temp;
					}
					else if (duration > temp) {
						duration = temp;
					}
				}
			}
		}
		return duration;
	}
	
	public void createStopNameList() {
		stopName = new ArrayList<String>();
		int[] routes = BusStopInfo.getRoutes();
		int[] busStops1 = BusStopInfo.getBusStops(routes[0]);
		int[] busStops2 = BusStopInfo.getBusStops(routes[1]);
		int[] busStops3 = BusStopInfo.getBusStops(routes[2]);
		int[] busStops4 = BusStopInfo.getBusStops(routes[3]);
		for (int i = 0; i < busStops1.length; i ++) {
			if (!stopName.contains(BusStopInfo.getFullName(busStops1[i]))) {
				stopName.add(BusStopInfo.getFullName(busStops1[i]));
			}
		}
		for (int i = 0; i < busStops2.length; i ++) {
			//stopName.add(BusStopInfo.getFullName(busStops2[i]));
			if (!stopName.contains(BusStopInfo.getFullName(busStops2[i]))) {
				stopName.add(BusStopInfo.getFullName(busStops2[i]));
			}
		}
		for (int i = 0; i < busStops3.length; i ++) {
			//stopName.add(BusStopInfo.getFullName(busStops3[i]));
			if (!stopName.contains(BusStopInfo.getFullName(busStops3[i]))) {
				stopName.add(BusStopInfo.getFullName(busStops3[i]));
			}
		}
		for (int i = 0; i < busStops4.length; i ++) {
			//stopName.add(BusStopInfo.getFullName(busStops4[i]));
			if (!stopName.contains(BusStopInfo.getFullName(busStops4[i]))) {
				stopName.add(BusStopInfo.getFullName(busStops4[i]));
			}
		}
	}
	
	public void createStopIDArray() {
		int[] routes = BusStopInfo.getRoutes();
		int[] busStops1 = BusStopInfo.getBusStops(routes[0]);
		int[] busStops2 = BusStopInfo.getBusStops(routes[1]);
		int[] busStops3 = BusStopInfo.getBusStops(routes[2]);
		int[] busStops4 = BusStopInfo.getBusStops(routes[3]);
		stopID = new int[stopName.size() * 2][10];
		for (int i = 0; i < stopName.size(); i ++) {
			for (int j = 0; j < 10; j ++) {
				stopID[i][j] = 0;
			}
		}
		//Get the list of different ID that corresponding to the same stop
		for (int i = 0; i < stopName.size(); i ++) {
			int index = 0;
			for (int j = 0; j < busStops1.length; j ++) {
				if ((stopName.get(i)).compareTo(BusStopInfo.getFullName(busStops1[j])) == 0) {
					stopID[i*2][index] = busStops1[j];
					stopID[i*2+1][index] = routes[0];
					index ++;
				}
			}
			for (int j = 0; j < busStops2.length; j ++) {
				if ((stopName.get(i)).compareTo(BusStopInfo.getFullName(busStops2[j])) == 0) {
					stopID[i*2][index] = busStops2[j];
					stopID[i*2+1][index] = routes[1];
					index ++;
				}
			}
			for (int j = 0; j < busStops3.length; j ++) {
				if ((stopName.get(i)).compareTo(BusStopInfo.getFullName(busStops3[j])) == 0) {
					stopID[i*2][index] = busStops3[j];
					stopID[i*2+1][index] = routes[2];
					index ++;
				}
			}
			for (int j = 0; j < busStops4.length; j ++) {
				if ((stopName.get(i)).compareTo(BusStopInfo.getFullName(busStops4[j])) == 0) {
					stopID[i*2][index] = busStops4[j];
					stopID[i*2+1][index] = routes[3];
					index ++;
				}
			}
		}
	}
	
	public void checkGraph() {
		for (int i = 0; i < routeGraph.length; i ++) {
			System.out.print("Index " + i + ": ");
			for (int j = 0; j < routeGraph.length; j ++) {
				System.out.print(routeGraph[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < stopName.size(); i ++) {
			System.out.println(stopName.get(i));
		}
		System.out.println();
		for (int i = 0; i < stopID.length; i ++) {
			for (int j = 0; j < 10; j ++) {
				System.out.print(stopID[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < stopName.size(); i ++) {
			if (i == 19 || i == 15 || i == 13 || i == 7) {
				System.out.println(stopName.get(i));
			}
		}
	}
}
