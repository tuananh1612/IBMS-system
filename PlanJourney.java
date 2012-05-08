
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

public class PlanJourney {
  
  // Initialize the journey array
  private static int[][] journey;
  
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
		
		System.out.println("Input: " + startStop + ", " + endStop);
		System.out.println("StringMap elements: ");
		for (int i = 0; i < StringMap.size(); i++)
			System.out.println(StringMap.get(i));
			
		
		int[] stringIndexes = findStringIndexes(StringMap, startStop, endStop);
		int startStopGraphIndex = stringIndexes[0];
		int endStopGraphIndex = stringIndexes[1];
		
		System.out.println("startStopGraphIndex: " + startStopGraphIndex);
		System.out.println("endStopGraphIndex: " + endStopGraphIndex);
		
		
    // Find shortest path in the graph
    ShortestPath shortest = new ShortestPath(startStopGraphIndex,
                                             endStopGraphIndex,
                                             startDate,
                                             time,
                                             stopsGraph);
    int[][] stopsInShortest = shortest.getStops();
    int[] timesInShortest = shortest.getTimes();
		
		System.out.println("stopsInShortest.length: " + stopsInShortest.length);
		System.out.println("timesInShortest.length: " + timesInShortest.length);
		
    journey = new int[stopsInShortest.length][3];
		
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
		System.out.println("a: " + a + ", b: " + b);
		for (int i = 0; i < map.size(); i++) {
			System.out.println("map.get(" + i + "): " + map.get(i));
			if (a.equals(map.get(i))) {
				System.out.println("Match! map.get(" + i + "): " + map.get(i) 
													 + ", a: " + a);
				aInt = i;
			}
			if (b.equals(map.get(i)))
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
