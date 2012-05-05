
import java.util.Date;
import java.util.ArrayList;

public class PlanJourney {
  
  // Initialize the journey array
  private int[][] journey;
  
  public PlanJourney(int startStop,
                     int endStop,
                     Date startDate,
                     int time) {
    
    // Get a relevant list of services
    // String stops[] = {startStop, endStop};
    // ArrayList<Integer[]> services = new ArrayList<Integer[]>();
    // services = DataManager.getServicesFor(stops, startTime);
   
    // Get a graph representing all possible routes
		DataManager manager = new DataManager("Dave");
		manager.createRouteGraph();
    int[][] stopsGraph = DataManager.getRouteGraph();
    
    // Find shortest path in the graph
    ShortestPath shortest = new ShortestPath(startStop,
                                             endStop,
                                             startDate,
                                             time,
                                             stopsGraph);
    int[][] stopsInShortest = shortest.getStops();
    int[] timesInShortest = shortest.getTimes();
    
    // Populate journey[][]
    for (int i = 0; i < stopsInShortest.length; i++) {
      journey[i][0] = stopsInShortest[i][0];
      journey[i][1] = stopsInShortest[i][1];
      journey[i][2] = timesInShortest[i];
    }
  
  } // constructor
  
  public int[][] getJourney() {
    return journey;
  } // getter method 
} // class
