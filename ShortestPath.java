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
	
  public ShortestPath(int startStop,
                      int endStop,
                      Date startDate,
                      int time,
                      int[][] stopsGraph,
											int[][] givenIDmap) {
    
		IDmap = givenIDmap;
		
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
	private ArrayList<Integer> findStopIDs(int[][] map, ArrayList<Integer> indexes) {
		System.out.println("map.length: " + map.length + ", map[0].length: " + map[0].length + ", map[5].length: " + map[5].length);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < indexes.size(); i++) {
			ids.add(map[indexes.get(i)][0]);
		}
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
