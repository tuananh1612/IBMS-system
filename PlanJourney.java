/* PlanJourney.java
 * 
 * Main class to connect all of the functionality
 * First creates a graph, then applies a shortest path algorithm
 * to it before adding timing.
 * 
 * TO GET JOURNEY:
 * First construct a PlanJourney object
 * giving it the arguments int startStop, int endStop
 * Date startTime, Date endTime
 * Then get the journey[][] array with getJourney().
 * 
 * See doc http://goo.gl/Ezg7s for details
 * 
 * Person responsible: Elise
 * 
 */

import java.util.ArrayList; 

public class PlanJourney {
  
  // Initialize the journey array
  private int[][] journey;
  
  public PlanJourney(int startStop,
                     int endStop,
                     Date startTime,
                     Date endTime) {
    
    // Get a relevant list of services
    int stops[] = {startStop, endStop};
    ArrayList<Integer[]> services = new ArrayList<Integer[]>();
    services = DataManager.getServicesFor(stops, startTime);
   
    // Create a graph with these services
    ArrayList<BusStop> stopsGraph = new ArrayList<BusStop>();
    stopsGraph = DataManager.createGraph(services);
    
    // Find shortest path in the graph
    ShortestPath shortest = new ShortestPath(startStop,
                                             endStop,
                                             startTime,
                                             endTime,
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
  
  public int[][] getJourney {
    return journey;
  } // getter method
} // class
