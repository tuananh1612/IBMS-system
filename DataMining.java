import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataMining {

	int[][] driverList;
	//Service list, format: serviceID, start time, end time, duration
	int[][] serviceList11, serviceList12, serviceList13;
	int[][] serviceList21, serviceList22, serviceList23;
	int[][] serviceList31, serviceList32, serviceList33;
	int[][] serviceList41, serviceList42, serviceList43;
	int[][] serviceTime;
	int[][] driverList1, driverList2, driverList3, driverList4;
	Date startDate, endDate, tempDate;
	int totalServiceTime;
	
	public static void main(String args[]) {
		
	}
	
	public DataMining(Date date1, Date date2) {
		startDate = date1;
		endDate = date2;
	}
	
	@SuppressWarnings("deprecation")
	private void getNextDate(Date date1) {
		if (date1.getYear() % 4 == 0) {
			if ((date1.getMonth() == 2) && (date1.getDate() == 29)) {
				date1.setDate(1);
				date1.setMonth(3);
			}
		}
		else {
			if ((date1.getMonth() == 2) && (date1.getDate() == 28)) {
				date1.setDate(1);
				date1.setMonth(3);
			}
		}
		if ((date1.getMonth() == 1) && (date1.getMonth() == 3) && (date1.getMonth() == 5)
				&& (date1.getMonth() == 7) && (date1.getMonth() == 8) && (date1.getMonth() == 10)) {
			if (date1.getDate() == 31) {
				date1.setDate(1);
				date1.setMonth(date1.getMonth() + 1);
			}
			else {
				date1.setDate(date1.getDate() + 1);
			}
		}
		else if (date1.getMonth() == 12) {
			if (date1.getDate() == 31) {
				date1.setDate(1);
				date1.setMonth(1);
				date1.setYear(date1.getYear() + 1);
			}
			else {
				date1.setDate(date1.getDate() + 1);
			}
		}
		else {
			if (date1.getDate() == 30) {
				date1.setDate(1);
				date1.setMonth(date1.getMonth() + 1);
			}
			else {
				date1.setDate(date1.getDate() + 1);
			}
		}
	}
	
	public void getDriverList() {
		database.openBusDatabase();
		int[] driverID = DriverInfo.getDrivers();
		driverList = new int[driverID.length][9];
		for (int i = 0; i < driverID.length; i ++) {
			driverList[i][0] = driverID[i];
			for (int j = 2; j < 9; j ++) {
				driverList[i][j] = 0;
			}
		}
		//Loop the holiday table for the number of holiday
		for (int i = 0; i < driverID.length; i ++) {
			driverList[i][1] = DriverInfo.getHolidaysTaken(driverID[i]);
		}
		//Add codes here for recoding holiday
		tempDate = startDate;
		for (int j = 2; j < 9; j ++) {
			for (int i = 0; i < driverID.length; i ++) {
				if (!DriverInfo.isAvailable(driverID[i], tempDate)) {
					driverList[i][j] = 1;
				}
			}
			getNextDate(tempDate);
		}
		//Sorting
		java.util.Arrays.sort(driverList, new java.util.Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return b[1] - a[1];
		    }
		});
		int numDriver1 = (int)(serviceTime[0][3] / totalServiceTime * driverID.length);
		int numDriver2 = (int)(serviceTime[1][3] / totalServiceTime * driverID.length);
		int numDriver3 = (int)(serviceTime[2][3] / totalServiceTime * driverID.length);
		int numDriver4 = driverID.length - numDriver1 - numDriver2 - numDriver3;
		driverList1 = new int[numDriver1][9];
		driverList2 = new int[numDriver2][9];
		driverList3 = new int[numDriver3][9];
		driverList4 = new int[numDriver4][9];
		int count = 0;
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;
		int index4 = 0;
		while (driverList[count][1] != 0) {
			if (count % 4 == 0) {
				copyArray(driverList, driverList1, 9, count, index1);
				count ++;
				index1 ++;
			}
			if (count % 4 == 1) {
				copyArray(driverList, driverList2, 9, count, index2);
				count ++;
				index2 ++;
			}
			if (count % 4 == 2) {
				copyArray(driverList, driverList3, 9, count, index3);
				count ++;
				index3 ++;
			}
			if (count % 4 == 3) {
				copyArray(driverList, driverList4, 9, count, index4);
				count ++;
				index4 ++;
			}
		}
		for (int i = index1; i < numDriver1; i ++) {
			for (int j = 0; j < 9; j ++) {
				driverList1[i][j] = driverList[count][j];
				count ++;
			}
		}
		for (int i = index2; i < numDriver2; i ++) {
			for (int j = 0; j < 9; j ++) {
				driverList1[i][j] = driverList[count][j];
				count ++;
			}
		}
		for (int i = index3; i < numDriver3; i ++) {
			for (int j = 0; j < 9; j ++) {
				driverList1[i][j] = driverList[count][j];
				count ++;
			}
		}
		for (int i = index4; i < numDriver4; i ++) {
			for (int j = 0; j < 9; j ++) {
				driverList1[i][j] = driverList[count][j];
				count ++;
			}
		}
	}
	
	private void copyArray(int[][] orignal, int[][] destination, int range, 
			int index1, int index2) {
		for (int i = 0; i < range; i ++) {
			destination[index2][i] = orignal[index1][i];
		}
	}
	
	public int[][] getDriverList1() {
		return driverList1;
	}
	
	public int[][] getDriverList2() {
		return driverList2;
	}
	
	public int[][] getDriverList3() {
		return driverList3;
	}
	
	public int[][] getDriverList4() {
		return driverList4;
	}
	
	public void getServiceList() {
		//database.openBusDatabase();
		int numberOfService = TimetableInfo.getNumberOfServices(65, 
				TimetableInfo.timetableKind.weekday);
		serviceTime = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; i < 4; j++) {
				serviceTime[i][j] = 0;
			}
		}
		//Route 65, weekday
		serviceList11 = new int[numberOfService][4];
		int[] service = TimetableInfo.getServices(65, TimetableInfo.timetableKind.weekday);
		for (int i = 0; i < service.length; i ++) {
			serviceList11[i][0] = service[i];
		}
		int[] timeList;
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(65, 
					TimetableInfo.timetableKind.weekday, serviceList11[i][0]);
			serviceList11[i][1] = timeList[0];
			serviceList11[i][2] = timeList[timeList.length - 1];
			serviceList11[i][3] = serviceList11[i][2] - serviceList11[i][1];
			serviceTime[0][0] += serviceList11[i][3];
		}
		//Route 65, saturday
		numberOfService = TimetableInfo.getNumberOfServices(65, 
				TimetableInfo.timetableKind.saturday);
		serviceList12 = new int[numberOfService][4];
		service = TimetableInfo.getServices(65, TimetableInfo.timetableKind.saturday);
		for (int i = 0; i < service.length; i ++) {
			serviceList12[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(65, 
					TimetableInfo.timetableKind.saturday, serviceList12[i][0]);
			serviceList12[i][1] = timeList[0];
			serviceList12[i][2] = timeList[timeList.length - 1];
			serviceList12[i][3] = serviceList12[i][2] - serviceList12[i][1];
			serviceTime[0][1] += serviceList12[i][3];
		}
		//Route 65, sunday
		numberOfService = TimetableInfo.getNumberOfServices(65, 
				TimetableInfo.timetableKind.sunday);
		serviceList13 = new int[numberOfService][4];
		service = TimetableInfo.getServices(65, TimetableInfo.timetableKind.sunday);
		for (int i = 0; i < service.length; i ++) {
			serviceList13[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(65, 
					TimetableInfo.timetableKind.sunday, serviceList13[i][0]);
			serviceList13[i][1] = timeList[0];
			serviceList13[i][2] = timeList[timeList.length - 1];
			serviceList13[i][3] = serviceList13[i][2] - serviceList13[i][1];
			serviceTime[0][2] += serviceList13[i][3];
		}
		//Route 66, weekday
		numberOfService = TimetableInfo.getNumberOfServices(66, 
				TimetableInfo.timetableKind.weekday);
		serviceList21 = new int[numberOfService][4];
		service = TimetableInfo.getServices(66, TimetableInfo.timetableKind.weekday);
		for (int i = 0; i < service.length; i ++) {
			serviceList21[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(66, 
					TimetableInfo.timetableKind.sunday, serviceList21[i][0]);
			serviceList21[i][1] = timeList[0];
			serviceList21[i][2] = timeList[timeList.length - 1];
			serviceList21[i][3] = serviceList21[i][2] - serviceList21[i][1];
			serviceTime[1][0] += serviceList21[i][3];
		}
		//Route 66, saturday
		numberOfService = TimetableInfo.getNumberOfServices(66, 
				TimetableInfo.timetableKind.saturday);
		serviceList13 = new int[numberOfService][4];
		service = TimetableInfo.getServices(66, TimetableInfo.timetableKind.saturday);
		for (int i = 0; i < service.length; i ++) {
			serviceList22[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(66, 
					TimetableInfo.timetableKind.saturday, serviceList22[i][0]);
			serviceList22[i][1] = timeList[0];
			serviceList22[i][2] = timeList[timeList.length - 1];
			serviceList22[i][3] = serviceList22[i][2] - serviceList22[i][1];
			serviceTime[1][1] += serviceList22[i][3];
		}
		//Route 66, sunday
		numberOfService = TimetableInfo.getNumberOfServices(66, 
				TimetableInfo.timetableKind.sunday);
		serviceList23 = new int[numberOfService][4];
		service = TimetableInfo.getServices(66, TimetableInfo.timetableKind.sunday);
		for (int i = 0; i < service.length; i ++) {
			serviceList23[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(66, 
					TimetableInfo.timetableKind.sunday, serviceList23[i][0]);
			serviceList23[i][1] = timeList[0];
			serviceList23[i][2] = timeList[timeList.length - 1];
			serviceList23[i][3] = serviceList23[i][2] - serviceList23[i][1];
			serviceTime[1][2] += serviceList23[i][3];
		}
		//Route 67, weekday
		numberOfService = TimetableInfo.getNumberOfServices(67, 
				TimetableInfo.timetableKind.weekday);
		serviceList23 = new int[numberOfService][4];
		service = TimetableInfo.getServices(67, TimetableInfo.timetableKind.weekday);
		for (int i = 0; i < service.length; i ++) {
			serviceList31[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(67, 
					TimetableInfo.timetableKind.weekday, serviceList31[i][0]);
			serviceList31[i][1] = timeList[0];
			serviceList31[i][2] = timeList[timeList.length - 1];
			serviceList31[i][3] = serviceList31[i][2] - serviceList31[i][1];
			serviceTime[2][0] += serviceList31[i][3];
		}
		//Route 67, saturday
		numberOfService = TimetableInfo.getNumberOfServices(67, 
				TimetableInfo.timetableKind.saturday);
		serviceList32 = new int[numberOfService][4];
		service = TimetableInfo.getServices(67, TimetableInfo.timetableKind.saturday);
		for (int i = 0; i < service.length; i ++) {
			serviceList32[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(67, 
					TimetableInfo.timetableKind.saturday, serviceList32[i][0]);
			serviceList32[i][1] = timeList[0];
			serviceList32[i][2] = timeList[timeList.length - 1];
			serviceList32[i][3] = serviceList32[i][2] - serviceList32[i][1];
			serviceTime[2][1] += serviceList32[i][3];
		}
		//Route 67, sunday
		numberOfService = TimetableInfo.getNumberOfServices(67, 
				TimetableInfo.timetableKind.sunday);
		serviceList33 = new int[numberOfService][4];
		service = TimetableInfo.getServices(67, TimetableInfo.timetableKind.sunday);
		for (int i = 0; i < service.length; i ++) {
			serviceList33[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(67, 
					TimetableInfo.timetableKind.sunday, serviceList33[i][0]);
			serviceList33[i][1] = timeList[0];
			serviceList33[i][2] = timeList[timeList.length - 1];
			serviceList33[i][3] = serviceList33[i][2] - serviceList33[i][1];
			serviceTime[2][2] += serviceList33[i][3];
		}
		//Route 68, weekday
		numberOfService = TimetableInfo.getNumberOfServices(68, 
				TimetableInfo.timetableKind.weekday);
		serviceList41 = new int[numberOfService][4];
		service = TimetableInfo.getServices(68, TimetableInfo.timetableKind.weekday);
		for (int i = 0; i < service.length; i ++) {
			serviceList41[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(68, 
					TimetableInfo.timetableKind.weekday, serviceList41[i][0]);
			serviceList41[i][1] = timeList[0];
			serviceList41[i][2] = timeList[timeList.length - 1];
			serviceList41[i][3] = serviceList41[i][2] - serviceList41[i][1];
			serviceTime[3][0] += serviceList41[i][3];
		}
		//Route 68, saturday
		numberOfService = TimetableInfo.getNumberOfServices(68, 
				TimetableInfo.timetableKind.saturday);
		serviceList42 = new int[numberOfService][4];
		service = TimetableInfo.getServices(68, TimetableInfo.timetableKind.saturday);
		for (int i = 0; i < service.length; i ++) {
			serviceList42[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(68, 
					TimetableInfo.timetableKind.saturday, serviceList42[i][0]);
			serviceList42[i][1] = timeList[0];
			serviceList42[i][2] = timeList[timeList.length - 1];
			serviceList42[i][3] = serviceList42[i][2] - serviceList42[i][1];
			serviceTime[3][1] += serviceList42[i][3];
		}
		//Route 68, sunday
		numberOfService = TimetableInfo.getNumberOfServices(68, 
				TimetableInfo.timetableKind.sunday);
		serviceList43 = new int[numberOfService][4];
		service = TimetableInfo.getServices(68, TimetableInfo.timetableKind.sunday);
		for (int i = 0; i < service.length; i ++) {
			serviceList43[i][0] = service[i];
		}
		for (int i = 0; i < service.length; i ++) {
			timeList = TimetableInfo.getServiceTimes(68, 
					TimetableInfo.timetableKind.sunday, serviceList43[i][0]);
			serviceList43[i][1] = timeList[0];
			serviceList43[i][2] = timeList[timeList.length - 1];
			serviceList43[i][3] = serviceList43[i][2] - serviceList43[i][1];
			serviceTime[3][2] += serviceList43[i][3];
		}
	}
	
	public void getTotalServiceTime() {
		totalServiceTime = 0;
		for (int i = 0; i < 4; i ++) {
			for (int j = 0; j < 3; j ++) {
				serviceTime[i][3] += serviceTime[i][j];
			}
			totalServiceTime += serviceTime[i][3];
		}
	}
	
	//Functions to return service list
	public int[][] getServiceList65Weekday() {
		return serviceList11;
	}
	
	public int[][] getServiceList65Saturday() {
		return serviceList12;
	}
	
	public int[][] getServiceList65Sunday() {
		return serviceList13;
	}
	
	public int[][] getServiceList66Weekday() {
		return serviceList21;
	}
	
	public int[][] getServiceList66Saturday() {
		return serviceList22;
	}
	
	public int[][] getServiceList66Sunday() {
		return serviceList23;
	}
	
	public int[][] getServiceList67Weekday() {
		return serviceList31;
	}
	
	public int[][] getServiceList67Saturday() {
		return serviceList32;
	}
	
	public int[][] getServiceList67Sunday() {
		return serviceList33;
	}
	
	public int[][] getServiceList68Weekday() {
		return serviceList41;
	}
	
	public int[][] getServiceList68Saturday() {
		return serviceList42;
	}
	
	public int[][] getServiceList68Sunday() {
		return serviceList43;
	}
}