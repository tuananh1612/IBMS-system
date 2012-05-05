import java.util.ArrayList;
import java.util.Date;

public class Simulator {

	private String areaCode;
	private String stopName;
	private int areaID;
	private int stopID;
	private int time;
	private Date date;
	private TimetableInfo.timetableKind kind;
	private ArrayList<int[]> servicesNumber;
	private String[] status = {"normal", "normal", "normal", "normal",
			"normal", "normal", "normal", "normal", "normal", "delayed"};
	private String[] subStatus = {"broken", "clouded wind shield", "road closure",
			"clouded wind shield", "road closure", "extreme weather condition"};
	
	public Simulator(String requireAreaCode, String requireName, int requireTime,
						Date requireDate) {
		areaCode = requireAreaCode;
		stopName = requireName;
		time = requireTime;
		date = requireDate;
		kind = TimetableInfo.timetableKind(date);
	}
	
	//Get the route coming to this stop. Get the number of service
	//expected within a certain time frame. Run randomizer to get status
	//and delay time (if needed)
	public String[][] working() {
		database.openBusDatabase();
		stopID = BusStopInfo.findBusStop(areaCode, stopName);
		//stopID = BusStopInfo.findBusStop("SKP", "Bus Station");
		//System.out.println(stopID);
		int[] routes = BusStopInfo.getRoutes(stopID);
		String fullName = BusStopInfo.getFullName(stopID);
		DataManager manager = new DataManager();
		ArrayList<String> nameList = manager.getStopNames();
		int index = nameList.indexOf(fullName);
		int[][] stopIDList = manager.getStopID();
		int[] currentIDList = stopIDList[index];
		//Use the routes found to locate all services, get the number sequence
		//of the bus stop. Compare the time, if within range, add the service
		//number, route number and time to the array
		for (int i = 0; i < routes.length; i ++) {
			for (int j = 0; j < currentIDList.length; j ++) {
				if ((currentIDList[j] != 0) && 
						(BusStopInfo.isTimingPointOnRoute(currentIDList[j], routes[i]))) {
					getServices(routes[i], currentIDList[j]);
				}
			}
		}
		//Run the randomizer. Assign status. Output.
		int statusIndex;
		//The serviceStatus array is a 2-d array. It contain status (normal or delayed)
		//as the first element, the second element is the reason and the last is
		//the delayed time.
		//The max time is currently set as 20 min. This is represented as 33 in the
		//database. (I'm not sure about this representation. The last digits in the
		//database run up 97 I think, so I think they run from 00 to 99, hence the
		//format here.
		String[][] serviceStatus = new String[servicesNumber.size()][3];
		for (int i = 0; i < serviceStatus.length; i ++) {
			statusIndex = (int)(Math.random() * 10);
			serviceStatus[i][0] = status[statusIndex];
			if (statusIndex == 9) {
				statusIndex = (int)(Math.random() * 6);
				serviceStatus[i][1] = subStatus[statusIndex];
				if (statusIndex == 0) {
					serviceStatus[i][2] = "";
				}
				else {
					serviceStatus[i][2] = "" + (8 + (int)(Math.random() * 25));
				}
			}
			else {
				serviceStatus[i][1] = "";
				serviceStatus[i][2] = "";
			}
		}
		return serviceStatus;
	}
	
	public void getServices(int requiredRoute, int requiredID) {
		//Get all the services of the route
		int[] services = TimetableInfo.getServices(requiredRoute, kind);
		//Process requiredID to get the index of the stop
		int index = 0;
		int[] IDs = BusStopInfo.getBusStops(requiredRoute);
		while (IDs[index] < requiredID) {
			index ++;
		}
		//Get the time for each services
		int[] temp;
		for (int i = 0; i < services.length; i ++) {
			temp = TimetableInfo.getServiceTimes(requiredRoute, kind, services[i]);
			for (int j = 0; j < temp.length; j ++) {
				if ((temp[j] >= (time - 33)) && (temp[j] <= (time + 33))) {
					int[] newEntry = new int[3];
					newEntry[0] = services[i];
					newEntry[1] = requiredRoute;
					newEntry[2] = temp[j];
					servicesNumber.add(newEntry);
				}
			}
		}
	}
	
	/*
	public static void main(String args[]) {
		working();
	}
	*/
}
