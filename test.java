import java.util.ArrayList;
import java.util.Date;

public class test {

	public static void main(String args[]) {
		database.openBusDatabase();
		DataManager manager = new DataManager(new Date());
		manager.createRouteGraph();
		//manager.checkGraph();
		/*
		int[] routes = BusStopInfo.getRoutes(770);
		System.out.print("Routes: ");
		for (int i = 0; i < routes.length; i ++) {
			System.out.print(routes[i] + " ");
		}
		System.out.println();
		*/
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(0);
		temp.add(1);
		ArrayList<Integer> result = new ArrayList<Integer>();
		ShortestPath client = new ShortestPath();
		int[][] map = manager.getStopID();
		for (int i = 0; i < 2; i ++) {
			for (int j = 0; j < 10; j ++) {
				System.out.print(map[i*2][j] + " ");
			}
			System.out.println();
			for (int j = 0; j < 10; j ++) {
				System.out.print(map[i*2+1][j] + " ");
			}
			System.out.println();
		}
		result = client.findStopIDs(map, temp);
		System.out.println("Length: " + result.size());
	}
}
