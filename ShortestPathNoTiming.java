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
public class ShortestPathNoTiming {
  
  private static ArrayList<Integer> stopsShortest;
  private static PriorityQueue<QItem> Q;
  
  public ShortestPathNoTiming(int startStop,
                      int endStop,
                      int[][] stopsGraph) {
    
    stopsShortest = new ArrayList<Integer>();
		stopsShortest = findShortestPath(startStop,
                                     endStop,
                                     stopsGraph);
																			 
		System.out.println("stopsShortest.size(): " + stopsShortest.size());
		System.out.println("stopsGraph:");
		for (int i = 0; i < stopsGraph.length; i++)
			for (int j = 0; j < stopsGraph.length; j++)
				System.out.println("stopsGraph["+i+"]["+j+"]: " + stopsGraph[i][j]);
		
		
		for (int i = 0; i < stopsShortest.size(); i++)
			System.out.println("stopsShortest[" + i + "]: " + stopsShortest.get(i));
	  
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
	
	/* Convert the ArrayList of stops and routes into a regular array
	 * and return it */
  public int[] getStops() {
		int len = stopsShortest.size();
		int[] stops = new int[len];
		for (int i = 0; i < len; i++) {
			int thisItem = stopsShortest.get(i);
			stops[i] = thisItem;
		}
    return stops;
  } // getStops
  
} // class
