import java.util.ArrayList;
import java.util.Date;

public class test {

	public static void main(String args[]) {
		database.openBusDatabase();
		DataManager manager = new DataManager(new Date());
		manager.createRouteGraph();
		manager.checkGraph();
		int[] routes = BusStopInfo.getRoutes(770);
		System.out.print("Routes: ");
		for (int i = 0; i < routes.length; i ++) {
			System.out.print(routes[i] + " ");
		}
		System.out.println();
	}
}
