import java.text.DateFormat;    
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList; 

/* A class that will generate a driver timetable 
   * based on the information it gets from DataMining */
public class Roster {
  
  public Date startDate;                // start date of timetable
  private Date currentDate;             // the day we are on
  private int dayIndex;                 // how far are we in the 7 day period
  
  private int currentStartTime;         // current start of TimeSlot
  private int currentEndTime;           // current end of TimeSlot
  
	private int[][] drivers;              // drivers in this roster
  private int noOfDrivers;              // how many there are
  private int currentDriver;            // the one we are assigning to
	private int previousDriver;           // the one we were assigning to before
  
  private int minsWorkedSoFar[][];      // time worked by each driver during current day and week
  private int avgWorkHrs;               // shift duration limit
  
  public ArrayList<TimeSlot> timeSlots; // the timeslots making up the timetable
  
  /* Create a timetable object for a specific route
   * 7 days from a required startDate,
   * using only drivers from a specified list 
   * 
   * currently does not assign buses */
  public Roster(Date requiredStartDate,
                int[][] serviceInfoWeekday,
                int[][] serviceInfoSat,
                int[][] serviceInfoSun,
                int[][] availableDrivers,
                int requiredAvgWorkHrs) {
                
    /* Prepare arrays to pass to generateDay 
     * **************************************************/
    
    // Number of services 
    int noOfServicesW = serviceInfoWeekday.length;
    int noOfServicesSat = serviceInfoSat.length;
    int noOfServicesSun = serviceInfoSun.length;
    
    // Service ID-s
    int[] allServicesW = getArrayCol(serviceInfoWeekday, noOfServicesW, 0);
    int[] allServicesSat = getArrayCol(serviceInfoSat, noOfServicesSat, 0);
    int[] allServicesSun = getArrayCol(serviceInfoSun, noOfServicesSun, 0);
    
    // Start times of all the services
    int[] startTimesW = getArrayCol(serviceInfoWeekday, noOfServicesW, 1);
    int[] startTimesSat = getArrayCol(serviceInfoSat, noOfServicesSat, 1);
    int[] startTimesSun = getArrayCol(serviceInfoSun, noOfServicesSun, 1);
    
    // End times of all the services
    int[] endTimesW = getArrayCol(serviceInfoWeekday, noOfServicesW, 2);
    int[] endTimesSat = getArrayCol(serviceInfoSat, noOfServicesSat, 2);
    int[] endTimesSun = getArrayCol(serviceInfoSun, noOfServicesSun, 2);
    
    // Durations of all the services
    int durationsW[] = getArrayCol(serviceInfoWeekday, noOfServicesW, 3);
    int durationsSat[] = getArrayCol(serviceInfoSat, noOfServicesSat, 3);
    int durationsSun[]= getArrayCol(serviceInfoSun, noOfServicesSun, 3);
    
    /* Initialize class variables 
     * **************************************************/
    
    startDate = requiredStartDate;
    currentDate = startDate;
    dayIndex = 0;
    
    currentStartTime = startTimesW[0];   
    currentEndTime = endTimesW[0];
    
    drivers = new int[noOfDrivers][9];
    drivers = availableDrivers;
    noOfDrivers = availableDrivers.length;
		currentDriver = 0;
    previousDriver = noOfDrivers - 1;
    
		minsWorkedSoFar = new int[noOfDrivers][2];
    for (int driver = 0; driver < noOfDrivers; driver++) {
      minsWorkedSoFar[driver][0] = 0;   // During this day
      minsWorkedSoFar[driver][1] = 0;   // During this week
    } 
    avgWorkHrs = requiredAvgWorkHrs;
		timeSlots = new ArrayList<TimeSlot>();
    
    /* Generate the roster
     * **************************************************/
    for (int weekday = 0; weekday < 5; weekday++) {
      generateDay(noOfServicesW, durationsW, startTimesW, endTimesW, allServicesW);
      nextDate();
    }
    generateDay(noOfServicesSat, durationsSat, startTimesSat, endTimesSat, allServicesSat);
    nextDate();
    generateDay(noOfServicesSun, durationsSun, startTimesSun, endTimesSun, allServicesSun);
    nextDate();
  
  } // Roster
  
  /* 
   * Take a driver
   * Try assigning this driver while keeping track of time worked so far
   * If not possible to add more
   * Take next driver
   * and add the timeslot object to the ArrayList
   * 
   * */
  private void generateDay(int noOfServices, 
                           int[] durations, 
                           int[] startTimes, 
                           int[] endTimes,
                           int[] allServices) {
															 
    // Update minsWorkedSoFar for this week 
    // before resetting minsWorkedSoFar for today
    minsWorkedSoFar[currentDriver][1] += minsWorkedSoFar[currentDriver][0];
    int shiftDurationToday = 0;
    int shiftDurationWeek = minsWorkedSoFar[currentDriver][1];
    
    // The number of services we have managed to assign to one driver
    int noOfServicesAssigned = 0;
    int firstServiceAssigned = 0;
		
		// Keep track of breaks
		int breakDuration[] = new int[noOfDrivers];
		boolean breakTaken[] = new boolean[noOfDrivers];
		for (int br = 0; br < noOfDrivers; br ++) {
			breakTaken[br] = false;
			breakDuration[br] = 0;
		}
    
    for (int currentService = 0; currentService < noOfServices; currentService++) {
     		 
      // The duration of the this service 
      int durationOfThisService = durations[currentService];
      
      boolean assignmentSuccessful = false; 
      
      int failCounter = 0; // If this exceeds noOfDrivers, we need to accept that a service simply cannot be assigned
      
      while (!assignmentSuccessful && failCounter < noOfDrivers) {
        if (constraintsCheck(drivers[currentDriver],
                             dayIndex,
                             durationOfThisService,
                             minsWorkedSoFar[currentDriver][0],
                             minsWorkedSoFar[currentDriver][1],
                             breakTaken[currentDriver])) {
          
          // Increment time worked today
          shiftDurationToday += durationOfThisService;
          
          // Update end time
          currentEndTime = endTimes[currentService];
          
          // Increment noOfServicesAssigned
          noOfServicesAssigned++;
          
          // If the previous driver has been on break, incr breakDuration
          if (breakTaken[previousDriver]) {
            breakDuration[previousDriver] += durationOfThisService;
          }
          
          // Add values to the minsWorkedSoFar matrix
          minsWorkedSoFar[currentDriver][0] = shiftDurationToday;
          
          assignmentSuccessful = true;
          
          // If this was the last service of the day, 
          // we need to cut the flow and save the timeslot 
          // even if the driver could take more
          if (currentService == noOfServices - 1) {
            saveSlot(firstServiceAssigned, 
                     noOfServicesAssigned, 
                     allServices);  
          }
          
        } // if we can assign this service
        
        /* If we cannot assign this service */
        else {
          
          // Only generate timeslot if actual services were assigned
          if (noOfServicesAssigned != 0 ) {
            
            saveSlot(firstServiceAssigned, 
                     noOfServicesAssigned, 
                     allServices);  
                     
            // The start time of the next TimeSlot 
            // is the start time of the next service 
            // (only needed if there is a next service..)
            if (currentService < (noOfServices - 1))
              currentStartTime = startTimes[currentService];
            
            // The first service that will be assigned to the next driver
            if ((firstServiceAssigned + noOfServicesAssigned + 1) < noOfServices) {
              firstServiceAssigned = (firstServiceAssigned + noOfServicesAssigned + 1);
            }
            noOfServicesAssigned = 0;
            
          }
          
          // If the previous driver had been on break 
          // and the duration of the break has been more than an hour
          // we can go back to them and assign new services
          if (breakTaken[previousDriver] 
              && breakDuration[previousDriver] >= 60) {
              currentDriver = previousDriver;
          }
          else {
            // Take the next driver from the list
            // or start over from the list of drivers if we've gone through all of them once
            if (currentDriver < noOfDrivers - 1) currentDriver++;
            else currentDriver = 0;
          }
          
          // Now update the previous driver
          if (currentDriver > 0) previousDriver = currentDriver -1;
          else previousDriver = noOfDrivers - 1;
          
          failCounter++;
        }
      } // while !assignmentSuccessful
        
    } // for each service
   
  } // generateDay
 
  /* Confirm that assigning a timeslot to a driver
   * will not breach the constraints (TRUE if assignment is allowed)
   * based on a Driver object, and the date and time
   * of the timeslot required*/
  private boolean constraintsCheck(int[] driver,
                                   int dayIndex,
                                   int iterationDuration,
                                   int minsWorkedSoFarToday,
                                   int minsWorkedSoFarThisWeek,
      				                     boolean breakTaken) {
    if (!Constraints.maximumDrivingTimeDay(minsWorkedSoFarToday, 
			                                     iterationDuration)) {
      return false;
    }
    else if(!Constraints.maximumDrivingTimeWeek(minsWorkedSoFarThisWeek,
     						                                iterationDuration)) {
      return false;
    }
    else if(!Constraints.minsWorkedContinuosly(minsWorkedSoFarToday, 
                                               iterationDuration, 
                                               breakTaken)) {
      return false;
    }
    
    if ((minsWorkedSoFarToday + iterationDuration) > avgWorkHrs) { 
      return false;
    }
    if (driver[dayIndex + 2] == 1)  {
      return false;
    }
    else	      
      return true;
  } // Constraints
  
  /* Return the ArrayList */
  public ArrayList<TimeSlot> getTimeslots() {
    return timeSlots;
  }
  
  /* Take a column of a 2d array 
   * and return it as a regular array */
  private int[] getArrayCol(int[][] sourceArray, int length, int colIndex) {
    int[] column = new int[length];
		for (int i = 0; i < length; i++) 
			column[i] = sourceArray[i][colIndex];
    return column;
  } // getArrayCol
  
  /* Create a TimeSlot and add it to the Roster object */
  private void saveSlot(int startFrom, 
                        int noAssigned, 
                        int[] allServices) {
                        
    int services[] = new int[noAssigned];
    for (int service = 0; service < noAssigned; service++) {
      services[service] = allServices[startFrom];
      startFrom++;
    }
    
    TimeSlot thisSlot = new TimeSlot(currentDate,
                                     currentStartTime,
                                     currentEndTime,
                                     drivers[currentDriver][0],
                                     services);
    timeSlots.add(thisSlot);
  
  } // saveSlot
  
  /* Go to next date */
  private void nextDate() {
    dayIndex++;
    DataMining.getNextDate(currentDate);
  } // nextDate
} // class Roster
