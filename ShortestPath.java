import java.util.ArrayList; 
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collections;

/* ShortestPath.java
 * 
 * A class that uses Dijkstra's shortest path algorithm
 * to find the shortest path from node x to node y
 * in a given graph.
 * 
 * It then calls PathTiming with the given start and end
 * times to assign appropriate times to each node.
 * 
 * Person responsible: Elise
 */
public class ShortestPath {
  
  private static ArrayList<int[]> stopsAndRoutesInShortest;
	public static ArrayList<Integer> stopsInShortest;
  private static int[] timesInShortest;
  private static PriorityQueue<QItem> Q;
  private static int[][] IDmap;
  private Date currentDate;
  
  public ShortestPath() {
	  
  }
	
  public ShortestPath(int startStop,
                      int endStop,
                      Date startDate,
                      int time,
                      int[][] stopsGraph,
											int[][] givenIDmap) {
    
		IDmap = givenIDmap;
		currentDate = startDate;
		
    stopsInShortest = new ArrayList<Integer>();
		stopsInShortest = findShortestPath(startStop,
                                       endStop,
                                       stopsGraph);
																			 
		System.out.println("stopsInShortest.size(): " + stopsInShortest.size());
		System.out.println("stopsGraph:");
		for (int i = 0; i < stopsGraph.length; i++)
			for (int j = 0; j < stopsGraph.length; j++)
				System.out.println("stopsGraph["+i+"]["+j+"]: " + stopsGraph[i][j]);
			
		for (int i = 0; i < stopsInShortest.size(); i++)
			System.out.println("stopsInShortest[" + i + "]: " + stopsInShortest.get(i));
	 
		ArrayList<Integer> stopIDs = findStopIDs(IDmap, stopsInShortest);
		int[] routesInShortest = assignRoutes(stopIDs);
		
		for (int i = 0; i < routesInShortest.length; i++)
			System.out.println("routesInShortest[" + i + "]: " + routesInShortest[i]);
			
		saveStopsAndRoutes(stopsInShortest, 
										   routesInShortest);
		
		PathTiming timing = new PathTiming(stopsAndRoutesInShortest,
																			 startDate,
                                       time);
		timing.getTiming();
		timesInShortest = timing.getTimeArray();
  }
  
  /* Naive Dijkstra's to find shortest path */
  private ArrayList<Integer> findShortestPath(int start,
                                              int end,
                                              int[][] graph) {
    int graphsize = graph.length;
    int INF = graphsize + 1;
    int[] distance = new int[graphsize];
    int[] previous = new int[graphsize];
    int node;
    
    for (node = 0; node < graphsize; node++) {
      distance[node] = INF;
      previous[node] = -1;
    }
    distance[start] = 0;
    
    /* Initialize a priority queue with the source */
    QItem sourceItem = new QItem(start, 0);
    Comparator<QItem> comparator = new QItemComparator();
    Q = new PriorityQueue<QItem>(graphsize, comparator);
    Q.add(sourceItem);
    
    int u;
    
    while(Q.size() > 0) {
      QItem head = Q.poll();
      u = head.getIndex();
      
      if (distance[u] == INF || u == end)
        break;
      
      /* Go through neighbors of u */
      for (int n = 0; n < graphsize; n++) {
        if (graph[u][n] != 0) {
          int v = n;
          int alt = distance[u] + graph[u][n];
          if (alt < distance[v]) {
            distance[v] = alt;
            previous[v] = u;
            QItem newItem = new QItem(v, distance[v]);
            Q.add(newItem);
          }
        }
      }
    }
                                   
    ArrayList<Integer> seq = new ArrayList<Integer>();
    u = end;
    while (previous[u] != -1) {
      seq.add(u);
      u = previous[u];
    }
		// Add source to the end, then reverse
		seq.add(start);
		Collections.reverse(seq);
    
    return seq;
  } // findShortestPath
	
	/* Assign route numbers to shortest path of stop ID-s */
	private int[] assignRoutes(ArrayList<Integer> stops) {
		int noOfStops = stops.size();
		ArrayList<Integer> pathRoutes = new ArrayList<Integer>();
		
		database.openBusDatabase();
		
		// For each stop, get the routes which visit it
		ArrayList<int[]> routes = new ArrayList<int[]>();
		for (int i = 0; i < noOfStops; i++) { 
			routes.add(BusStopInfo.getRoutes(stops.get(i)));
		}
		for (int i = 0; i < noOfStops; i++) { 
			int[] thisRoutes = routes.get(i);
			System.out.println("Routes for stop ID " + stops.get(i) + ": ");
			for (int j = 0; j < thisRoutes.length; j++)
				System.out.println(thisRoutes[j]);
		}
		
		// Begin assigning from the first stop
		int currentStop = 0;
		
		// While more stops exist
		while (currentStop < noOfStops) {
			int[] routesVisitingThisStop = routes.get(currentStop);
			int longestRunningRoute = routesVisitingThisStop[0];
			int maxNoOfStops = 0;
			
			// For each route of this stop
			for (int r = 0; r < routesVisitingThisStop.length; r++) {
				
				// Check how far we can go
				int runsNoOfStops = 1;
				int nextStop = currentStop + 1;
				
				if (nextStop < noOfStops) {
					int[] routesVisitingNextStop = routes.get(nextStop);
					boolean stop = false;
					while (sameRouteAvailable(routesVisitingThisStop[r],
																		routesVisitingNextStop)
								 && !stop) {
						runsNoOfStops++;
						if (nextStop + 1 < noOfStops) {
							nextStop++;
							routesVisitingNextStop = routes.get(nextStop);
						}
						else 
							stop = true;
					} // while
				} 
				
				// Update max
				if (runsNoOfStops > maxNoOfStops) {
					maxNoOfStops = runsNoOfStops;
					longestRunningRoute = routesVisitingThisStop[r];
				} 
			} // for
			
			System.out.println("maxNoOfStops: " + maxNoOfStops);
			// Save this route to pathRoutes for each stop it reaches
			// pathRoutes will have one extra item for each time we change routes
			for (int s = 0; s < maxNoOfStops; s++)
				pathRoutes.add(longestRunningRoute);
			
			// Update onStop
			currentStop += (maxNoOfStops);
		} // while
		
		int[] pathRoutesArray = new int[pathRoutes.size()];
		Iterator<Integer> iterator = pathRoutes.iterator();
		for (int i = 0; i < pathRoutesArray.length; i++)
			pathRoutesArray[i] = iterator.next().intValue();
		return pathRoutesArray;	
	} // assignRoutes
	
	/* Check whether a given route exists in a list of routes */
	private boolean sameRouteAvailable(int route, int[] others) {
		for (int i = 0; i < others.length; i++) {
			if (route == others[i])
				return true;
		}	
		return false;	
	} // sameRouteAvailable
  
	/* Save stops and routes into single ArrayList */
	private void saveStopsAndRoutes(ArrayList<Integer> stops, 
																							int[] routes) {
		stopsAndRoutesInShortest = new ArrayList<int[]>();
		int len = routes.length;
		int currentStop = 0;
		for (int i = 0; i < len; i++) {
			int stop = stops.get(currentStop);
			int[] item = {stop, routes[i]};
			stopsAndRoutesInShortest.add(item);
			// Only update currentStop if the route stays the same
			if (i < len - 1 && routes[i] == routes[i+1])
				currentStop++;
		} // for
	} // saveStopsAndRoutes
	
	/* Convert indexes into actual bus stop ID's */
	public ArrayList<Integer> findStopIDs(int[][] map, ArrayList<Integer> indexes) {
		System.out.println("map.length: " + map.length + ", map[0].length: " + map[0].length + ", map[5].length: " + map[5].length);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<int[]> mediumList = new ArrayList<int[]>();
		System.out.println("Input indexes:");
		for (int index = 0; index < indexes.size(); index++)
			System.out.println(indexes.get(index));
			
		//The mediumList contains int array size 2 as element. The first index
		//is the stopID, the second index is the route number
		/*
		for (int i = 0; i < indexes.size(); i++) {
			ids.add(map[indexes.get(i)][0]);
		}
		*/
		//Loop through the array
		//int[][] stopID = map;
		for (int i = 0; i < indexes.size() - 1; i ++) {
			int startPoint = indexes.get(i);
			int endPoint = indexes.get(i+1);
			System.out.println(startPoint);
			System.out.println(endPoint);
			System.out.println("Size: " + indexes.size());
			ArrayList<int[]> startPointID = new ArrayList<int[]>();
			ArrayList<int[]> endPointID = new ArrayList<int[]>();
			
			//Add stops' IDs to their respective list
			for (int j = 0; j < 10; j ++) {
				int[] temp = new int[2];
				if (map[startPoint*2][j] != 0) {
					temp[0] = map[startPoint*2][j];
					temp[1] = map[startPoint*2+1][j];
					startPointID.add(temp);
				}
			}
			for (int j = 0; j < 10; j ++) {
				int[] temp = new int[2];
				if (map[endPoint*2][j] != 0) {
					temp[0] = map[endPoint*2][j];
					temp[1] = map[endPoint*2+1][j];
					endPointID.add(temp);
				}
			}
			//Look for matching route number
			ArrayList<int[]> matchList = new ArrayList<int[]>();
			//matchList use matchEntry below as element. The format of 
			//matchEntry is index 0: startPoint, index 1: endPoint,
			//index 2: route number.
			int[] matchEntry = new int[3];
			//Loop through startList
			System.out.println(startPointID.size());
			System.out.println(endPointID.size());
			/*
			for (int j = 0; j < startPointID.size(); j ++) {
				System.out.print(startPointID.get(j)[0] + " ");
				System.out.print(startPointID.get(j)[1] + " | ");
			}
			System.out.println();
			for (int j = 0; j < endPointID.size(); j ++) {
				System.out.print(endPointID.get(j)[0] + " ");
				System.out.print(endPointID.get(j)[1] + " | ");
			}
			*/
			for (int j = 0; j < startPointID.size(); j ++) {
				//Loop through endList
				for (int k= 0; k < endPointID.size(); k ++) {
					//Check if route number match
					if ((startPointID.get(j))[1] == (endPointID.get(k))[1]) {
						//Check if the ID is different
						if ((startPointID.get(j))[0] < (endPointID.get(k))[0]) {
							matchEntry[0] = startPointID.get(j)[0];
							matchEntry[1] = endPointID.get(k)[0];
							matchEntry[2] = startPointID.get(j)[1];
							matchList.add(matchEntry);
						}
					}
				}
			}
			//Look through the matchList
			//Get the previous stop before startPoint
			//If there is no previous stop, assign the first pair in matchList
			int[] temp1 = new int[2];
			int[] temp2 = new int[2];
			int[] temp3 = new int[2];
			if (i == 0) {
				//Add to mediumList
				temp1[0] = matchList.get(0)[0];
				temp1[1] = matchList.get(0)[2];
				mediumList.add(temp1);
				temp2[0] = matchList.get(0)[1];
				temp2[1] = matchList.get(0)[2];
				mediumList.add(temp2);
			}
			else {
				//Get the previous stop
				temp3 = matchList.get(i-1);
				boolean found = false;
				for (int j = 0; j < matchList.size(); j ++) {
					found = false;
					//Match route
					if (temp3[1] == matchList.get(j)[2]) {
						temp1[0] = matchList.get(j)[1];
						temp1[1] = matchList.get(j)[2];
						mediumList.add(temp1);
						found = true;
						break;
					}
				}
				//Check if no match found
				if (!found) {
					//Add the first pair as new entry
					temp1[0] = matchList.get(0)[0];
					temp1[1] = matchList.get(0)[2];
					mediumList.add(temp1);
					temp2[0] = matchList.get(0)[1];
					temp2[1] = matchList.get(0)[2];
					mediumList.add(temp2);
				}
			}
		}
		//Shorten the list to get only stopIds
		for (int i = 0; i < mediumList.size(); i ++) {
			ids.add(mediumList.get(i)[0]);
		}
		//Check if the format is correct with the rest of the code
		System.out.println("Output IDs:");
		System.out.println("ids.size(): " + ids.size());
		for (int index = 0; index < ids.size(); index++)
			System.out.println(ids.get(index));
			
		return ids;
	} // findStopIDs
	
	/* Convert the ArrayList of stops and routes into a regular array
	 * and return it */
  public int[][] getStops() {
		int len = stopsAndRoutesInShortest.size();
		int[][] stopsAndRoutes = new int[len][2];
		for (int i = 0; i < len; i++) {
			int[] thisItem = stopsAndRoutesInShortest.get(i);
			stopsAndRoutes[i][0] = thisItem[0];
			stopsAndRoutes[i][1] = thisItem[1];
		}
    return stopsAndRoutes;
  } // getStops
  
  public int[] getTimes() {
    return timesInShortest;
  } // getTimes
  
} // class
