
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;

public class PlanJourney {
  
  // Initialize the journey array
  private int[][] journey;
  
  public PlanJourney(String startStop,
                     String endStop,
                     Date startDate,
                     int time) {
    
    // Get a graph representing all possible routes
		DataManager manager = new DataManager(startDate);
		manager.createRouteGraph();
    int[][] stopsGraph = manager.getRouteGraph();
    
		/* Use the StopID array from DataManager
		 * to map the graph ID-s to the correct bus stops */
		int[][] IDmap = manager.getStopID();
		ArrayList<String> StringMap = manager.getStopNames();
		
		int[] stringIndexes = findStringIndexes(StringMap, startStop, endStop);
		int startStopGraphIndex = stringIndexes[0];
		int endStopGraphIndex = stringIndexes[1];
		
    // Find shortest path in the graph
    ShortestPath shortest = new ShortestPath(startStopGraphIndex,
                                             endStopGraphIndex,
                                             startDate,
                                             time,
                                             stopsGraph);
    int[][] stopsInShortest = shortest.getStops();
    int[] timesInShortest = shortest.getTimes();
		
    
    // Populate journey[][]
    for (int i = 0; i < stopsInShortest.length; i++) {
      journey[i][0] = IDmap[stopsInShortest[i][0]][0];
      journey[i][1] = IDmap[stopsInShortest[i][1]][0];
      journey[i][2] = IDmap[timesInShortest[i]][0];
    }
  
  } // constructor
	
	private int[] findStringIndexes(ArrayList<String> map, String a, String b) {
		int[] indexes = new int[2];
		int aInt = -1;
		int bInt = -1;
		for (int i = 0; i < map.size(); i++) {
			if (map.get(i) == a)
				aInt = i;
			if (map.get(i) == b)
				bInt = i;
		}
		indexes[0] = aInt;
		indexes[1] = bInt;
		return indexes;
	} // findStringIndexes
	
  public int[][] getJourney() {
    return journey;
  } // getter method 
} // class
