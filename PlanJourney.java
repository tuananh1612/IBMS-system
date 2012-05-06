
import java.util.Date;
import java.util.ArrayList;

public class PlanJourney {
  
  // Initialize the journey array
  private int[][] journey;
  
  public PlanJourney(int startStop,
                     int endStop,
                     Date startDate,
                     int time) {
    
    // Get a graph representing all possible routes
		DataManager manager = new DataManager(startDate);
		manager.createRouteGraph();
    int[][] stopsGraph = manager.getRouteGraph();
    
    // Find shortest path in the graph
    ShortestPath shortest = new ShortestPath(startStop,
                                             endStop,
                                             startDate,
                                             time,
                                             stopsGraph);
    int[][] stopsInShortest = shortest.getStops();
    int[] timesInShortest = shortest.getTimes();
		
		/* Use the StopID array from DataManager
		 * to map the graph ID-s to the correct bus stops */
		int[][] IDmap = manager.getStopID();
    
    // Populate journey[][]
    for (int i = 0; i < stopsInShortest.length; i++) {
      journey[i][0] = IDmap[stopsInShortest[i][0]][0];
      journey[i][1] = IDmap[stopsInShortest[i][1]][0];
      journey[i][2] = IDmap[timesInShortest[i]][0];
    }
  
  } // constructor
  
  public int[][] getJourney() {
    return journey;
  } // getter method 
} // class
