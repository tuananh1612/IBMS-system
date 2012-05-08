import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;

public class TestShortest {
	public static void main(String[] args) {
		String startStop = "Glossop, Henry Street";
		String endStop = "Strines, Royal Oak";
		Date startDate = new Date();
		int time = 0700;
		
	//	PlanJourney plan = new PlanJourney(startStop, endStop, startDate, time);
	//	int[][] journey = plan.getJourney();
	//	
	//	for (int i = 0; i < journey.length; i++) {
	//		System.out.println("Take route " + journey[i][1] + " from stop " 
	//											 + journey[i][0] + " at " + journey[i][2] + ".");
	//	}
		
    // Get a graph representing all possible routes
		DataManager manager = new DataManager(startDate);
		manager.createRouteGraph();
    int[][] stopsGraph = manager.getRouteGraph(); 
		
		/* Use the StopID array from DataManager
		 * to map the graph ID-s to the correct bus stops */
		int[][] IDmap = manager.getStopID();
		
		/* Testing Dijkstra's only */
		int[][] testGraph = new int[6][6];
		testGraph[0][0] = 0;
		testGraph[0][1] = 1;
		testGraph[0][2] = 0;
		testGraph[0][3] = 0;
		testGraph[0][4] = 0;
		testGraph[0][5] = 1;
		testGraph[1][0] = 1;
		testGraph[1][1] = 0;
		testGraph[1][2] = 1;
		testGraph[1][3] = 0;
		testGraph[1][4] = 0;
		testGraph[1][5] = 0;
		testGraph[2][0] = 0;
		testGraph[2][1] = 1;
		testGraph[2][2] = 0;
		testGraph[2][3] = 0;
		testGraph[2][4] = 0;
		testGraph[2][5] = 1;
		testGraph[3][0] = 1;
		testGraph[3][1] = 0;
		testGraph[3][2] = 0;
		testGraph[3][3] = 0;
		testGraph[3][4] = 0;
		testGraph[3][5] = 1;
		testGraph[4][0] = 0;
		testGraph[4][1] = 0;
		testGraph[4][2] = 0;
		testGraph[4][3] = 0;
		testGraph[4][4] = 0;
		testGraph[4][5] = 1;
		testGraph[5][0] = 1;
		testGraph[5][1] = 0;
		testGraph[5][2] = 0;
		testGraph[5][3] = 1;
		testGraph[5][4] = 1;
		testGraph[5][5] = 0;
		
	//	ShortestPathNoTiming test = new ShortestPathNoTiming(0, 4, testGraph);
	//	int[] testShortest = test.getStops();
	//	
	//	System.out.println("testShortest.length: " + testShortest.length);
	//	for (int i = 0; i < testShortest.length; i++)
	//		System.out.println("testShortest[" + i + "]: " + testShortest[i]);
		
		database.openBusDatabase();
		int[] testGetRoutes = BusStopInfo.getRoutes(770);
		System.out.println("Routes for 0:");
		for (int index = 0; index < testGetRoutes.length; index++)
			System.out.println(testGetRoutes[index]);
		
		ShortestStopsAndRoutes testStopsAndRoutes = new ShortestStopsAndRoutes(0, 4, testGraph, IDmap);
		int[][] testShortestWithRoutes = testStopsAndRoutes.getStops();
		
		System.out.println("testShortestWithRoutes.length: " + testShortestWithRoutes.length);
		for (int i = 0; i < testShortestWithRoutes.length; i++)
			for (int j = 0; j < 2; j++)
				System.out.println("testShortestWithRoutes[" + i + "][" + j + "]: " + testShortestWithRoutes[i][j]);
	} // main
} // TestShortest
