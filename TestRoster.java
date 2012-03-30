import java.text.DateFormat;    
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList; 
import java.util.Iterator;

/* A class to test the Rostering algorithm.
 * Hard codes some data and passes it to Roster
 * so that we can easily see whether things are going as planned */
public class TestRoster {
	public static void main(String[] args) {
		/* 
		 * Need to create the following data  
	   * 
		 * Date requiredStartDate
	   * int[][] serviceInfoWeekday
	   * int[][] serviceInfoSat
	   * int[][] serviceInfoSun
	   * int[][] availableDrivers
	   * int requiredAvgWorkHrs 
		 * 
		 * */
		
		// Initialize startDate to today
		Date startDate = Calendar.getInstance().getTime();
    
		// Initialize an array of four services
		int serviceInfoWeekday[][] =
		{
		//	 ServiceNumber			StartTime			EndTime			Duration
			{				1,								700,				730,					100		},
			{				2,								730,				830,					75		},
			{				3,								830,				915,					120		},
			{				4,								915,				945,					90		},
		};
		
		int serviceInfoSat[][] =
		{
		//	 ServiceNumber			StartTime			EndTime			Duration
			{				1,								800,				830,					30		},
			{				2,								830,				930,					90		},
			{				3,								930,				115,					25		},
			{				4,								115,				145,					150		},
		};

		int serviceInfoSun[][] = 
		{
		//	 ServiceNumber			StartTime			EndTime			Duration
			{				1,								900,				930,					30		},
			{				2,								930,				130,					60		},
			{				3,								130,				215,					45		},
			{				4,								215,				245,					30		},
		};

		// Available drivers and when they are on holiday
		int drivers[][] = 
			{ 
			//  ID    HolidaysTaken   M  T  W  T  F  S  S
				{ 1234,				3,				1, 0, 1, 1, 0, 0, 0	}, 
				{ 3526,			  1,				0, 1, 0, 0, 0, 0, 0	}, 
				{ 1783,			  1,				0, 0, 0, 0, 0, 0, 1	}, 
				{ 7392,			  2,				0, 0, 0, 0, 1, 1, 0	}, 
				{ 1024,			  0,				0, 0, 0, 0, 0, 0, 0	}, 
				{ 3126,			  4,				0, 0, 1, 1, 0, 1, 1	},
				{ 6729,			  2,				0, 1, 1, 0, 0, 0, 0	}, 
				{ 6289,			  3,				0, 1, 0, 1, 1, 0, 0	}, 
			};
		
		
		// The average working hours for each driver
		int avgWorkHours = 360;
	
		// print stuff
    System.out.println("INITIAL INFO");
    System.out.println("StartDate: " + startDate.toString());
		System.out.println("Number of drivers: " + drivers.length);
		System.out.println("Number of services: " + serviceInfoSun.length);
		System.out.println("Today's date: " + startDate.toString() + "\n\n");
    System.out.println("==============================================\n\n");
		
    // Generate a random TimeSlot for testing
    System.out.println("RANDOM TIMESLOT OBJECT:");
    int testServices[] = { 1, 2 };
    TimeSlot testTS = new TimeSlot(startDate, 700, 800, 6289, testServices);
    System.out.println(testTS + "\n\n");
    System.out.println("==============================================\n\n");
    
    // Test whether nextDate works
    System.out.println("GOING ONE DATE FORWARD");
    DataMining.getNextDate(startDate);
    System.out.println(startDate + "\n\n");
    System.out.println("==============================================\n\n");
    
    System.out.println("BEGIN ROSTER\n\n");
    System.out.println("==============================================\n\n");
    
		// Build the roster
		Roster testRoster = new Roster(startDate,
																	 serviceInfoWeekday,
																	 serviceInfoSat,
																	 serviceInfoSun,
																	 drivers,
																	 avgWorkHours);
		
		// Get the TimeSlots
		ArrayList<TimeSlot> timeslots = testRoster.getTimeslots();
		
		Iterator itr = timeslots.iterator();
		  while (itr.hasNext()){
	      System.out.println(itr.next());
			}
			
	} // main
} // class TestRoster
