import java.util.ArrayList;
import java.util.Date;

public class test {

	public static void main(String args[]) {
		database.openBusDatabase();
		DataManager manager = new DataManager(new Date());
		manager.createRouteGraph();
		manager.checkGraph();
	}
}
