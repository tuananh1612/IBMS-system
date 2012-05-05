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
  
  private ArrayList<Integer> stopsInShortest;
  private int[] timesInShortest;
  private PriorityQueue<QItem> Q;
  
  public ShortestPath(int startStop,
                      int endStop,
                      Date startDate,
                      int time,
                      int[][] stopsGraph) {
    
    stopsInShortest = findShortestPath(startStop,
                                       endStop,
                                       stopsGraph);
    timesInShortest = PathTiming.assignTimes(startDate,
                                             time,
                                             stopsInShortest);
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
  
  public int[][] getStops() {
			
    return stopsInShortest;
  } // getStops
  
  public int[] getTimes() {
    return timesInShortest;
  } // getTimes
  
} // class
