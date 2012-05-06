import java.util.ArrayList; 
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

/* ShortestPath.java
 * 
 * A class that uses any shortest path algorithm
 * to find the shortest path from node x to node y
 * in a given graph.
 * 
 * It then calls PathTiming with the given start and end
 * times to assign appropriate times to each node.
 * 
 * Person responsible: Elise
 */
public class ShortestPath {
  
  private ArrayList<int[]> stopsAndRoutesInShortest;
  private int[] timesInShortest;
  private PriorityQueue<QItem> Q;
  
  public ShortestPath(int startStop,
                      int endStop,
                      Date startDate,
                      int time,
                      int[][] stopsGraph) {
    
    ArrayList<Integer> stopsInShortest = findShortestPath(startStop,
                                         endStop,
                                         stopsGraph);
	  int[] routesInShortest = assignRoutes(stopsInShortest);
		PathTiming timing = new PathTiming(stopsAndRoutesInShortest,
																			 startDate,
                                       time);
		timing.getTiming();
		
		timesInShortest = timing.getTimeArray();
		stopsAndRoutesInShortest = saveStopsAndRoutes(stopsInShortest, 
																									timesInShortest);
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
        if (graph[u][n] == 1) {
          int v = n;
          int alt = distance[u] + 1;
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
    
    return seq;
  } // findShortestPath
	
	/* Assign route numbers to shortest path of stop ID-s */
	private int[] assignRoutes(ArrayList<Integer> stops) {
		return new int[stops.size()];	
	} // assignRoutes
  
	/* Save stops and routes into single ArrayList */
	private ArrayList<int[]> saveStopsAndRoutes(ArrayList<Integer> stops, 
																							int[] routes) {
		ArrayList<int[]> stopsAndRoutes = new ArrayList<int[]>();
		int len = stops.size();
		for (int i = 0; i < len; i++) {
			int stop = stops.get(i);
			int[] item = {stop, routes[i]};
			stopsAndRoutes.add(item);
		} // for
		return stopsAndRoutes;
	} // saveStopsAndRoutes
	
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
