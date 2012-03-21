import java.text.DateFormat;    
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList; 

/* A class that will generate a driver timetable 
   * based on the information it gets from DataMining */
public class Roster {
  
  public Date startDate;                     // Start date of timetable
  public ArrayList<TimeSlot> timeSlots;      // the timeslots making up the timetable
  
  private Date currentDate;
  private int currentStartTime;              // current start of TimeSlot
  private int currentEndTime;                // current end of TimeSlot
  private int noOfDrivers;
  private int minsWorkedSoFar[][];
  private int currentDriver;             		// The first driver in availableDrivers
	private int previousDriver;
	private int[] drivers;                     // Drivers in this roster
  
  int avgWorkHrs;
  
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
                
    startDate = requiredStartDate;
    avgWorkHrs = requiredAvgWorkHrs;
		
		// Initialize the timeSlots arraylist
		timeSlots = new ArrayList<TimeSlot>();
		
		// Number of drivers 
    noOfDrivers = availableDrivers.length;
		
		// An array of driver ID's
    drivers = new int[noOfDrivers];
		for (int d = 0; d < noOfDrivers; d++) 
			drivers[d] = availableDrivers[d][0];
    
		// Current and previous drivers
		currentDriver = 0;
    previousDriver = noOfDrivers - 1;
    
    // Number of services 
    int noOfServicesW = serviceInfoWeekday.length;
    int noOfServicesSat = serviceInfoSat.length;
    int noOfServicesSun = serviceInfoSun.length;
    
    // All of the services
    int[] allServicesW = serviceInfoWeekday[0];
    int[] allServicesSat = serviceInfoSat[0];
    int[] allServicesSun = serviceInfoSun[0];
    
    // Durations of all the services
    int durationsW[] = new int[noOfServicesW];
		for (int i = 0; i < noOfServicesW; i++) 
			durationsW[i] = serviceInfoWeekday[i][3];
    int durationsSat[] = new int[noOfServicesSat];
		for (int j = 0; j < noOfServicesSat; j++) 
			durationsSat[j] = serviceInfoSat[j][3];
    int durationsSun[]= new int[noOfServicesSun];
		for (int k = 0; k < noOfServicesSun; k++) 
			durationsSun[k] = serviceInfoSun[k][3];
    
    // Start times of all the services
    int[] startTimesW = new int[noOfServicesW];
		for (int i = 0; i < noOfServicesW; i++) 
			startTimesW[i] = serviceInfoWeekday[i][1];
    int[] startTimesSat = new int[noOfServicesSat];
		for (int i = 0; i < noOfServicesSat; i++) 
			startTimesSat[i] = serviceInfoSat[i][1];
    int[] startTimesSun = new int[noOfServicesSun];
		for (int i = 0; i < noOfServicesSun; i++) 
			startTimesSun[i] = serviceInfoSun[i][1];
    
    // End times of all the services
    int[] endTimesW = new int[noOfServicesW];
		for (int i = 0; i < noOfServicesW; i++) 
			endTimesW[i] = serviceInfoWeekday[i][1];
    int[] endTimesSat = new int[noOfServicesSat];
		for (int i = 0; i < noOfServicesSat; i++) 
			endTimesSat[i] = serviceInfoSat[i][1];
    int[] endTimesSun = new int[noOfServicesSun];
		for (int i = 0; i < noOfServicesSun; i++) 
			endTimesSun[i] = serviceInfoSun[i][1];
    
    // Time service leaves depot on weekdays
    currentStartTime = startTimesW[0];   
    
    // Time earliest service gets back to depot on weekdays
    currentEndTime = endTimesW[0];
		
    // Initialize array of time worked so far for each driver
		minsWorkedSoFar = new int[noOfDrivers][2];
    for (int driver = 0; driver < noOfDrivers; driver++) {
      minsWorkedSoFar[driver][0] = 0;   // During this day
      minsWorkedSoFar[driver][1] = 0;   // During this week
    } 
    
    // The current date we are on
    currentDate = startDate;
    
		System.out.println("Previous driver index: " + previousDriver);
		System.out.println("Current driver index: " + currentDriver);
		
    /* WEEKDAYS
     * ********************************************************************/
    for (int weekday = 0; weekday < 5; weekday++) {
			System.out.println("ENTERING WEEKDAY LOOP");
      generateDay(noOfServicesW, durationsW, startTimesW, endTimesW, allServicesW);
    }
    /* SATURDAY
     * ********************************************************************/
    generateDay(noOfServicesSat, durationsSat, startTimesSat, endTimesSat, allServicesSat);
    
    /* SUNDAY
     * ********************************************************************/
    generateDay(noOfServicesSun, durationsSun, startTimesSun, endTimesSun, allServicesSun);
  
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
    System.out.println("Previous driver index: " + previousDriver);
    System.out.println("Current driver index: " + currentDriver);
															 
    int shiftDurationToday = minsWorkedSoFar[currentDriver][0];
    int shiftDurationWeek = minsWorkedSoFar[currentDriver][1];
    
    // The number of services we have managed to assign to one driver
    int noOfServicesAssigned = 0;
    int firstServiceAssigned = 0;
		
    System.out.println("noOfServicesAssigned: " + noOfServicesAssigned);
    System.out.println("firstServiceAssigned: " + firstServiceAssigned);
		
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
      
			System.out.println("shiftDurationToday(" + shiftDurationToday 
												+ ") + durationOfThisService("
												+ durationOfThisService + ") = "
												+ (shiftDurationToday + durationOfThisService));
			System.out.println("avgWorkMins: " + avgWorkHrs);
			
			boolean assignmentSuccessful = false;
			
      if (constraintsCheck(drivers[currentDriver],
                           currentDate,
                           durationOfThisService,
                           shiftDurationToday,
                           shiftDurationWeek,
                           breakTaken[currentDriver]) &&
          (shiftDurationToday + durationOfThisService) < avgWorkHrs) {
        
 			  System.out.println("ASSIGNING SERVICE");
        // Increment time worked today
        shiftDurationToday += durationOfThisService;
        
        // Update end time
        currentEndTime = endTimes[currentService];
        
        // Increment noOfServicesAssigned
        noOfServicesAssigned++;
				
 			  System.out.println("noOfServicesAssigned after incr: " + noOfServicesAssigned);
				
				// If the previous driver has been on break, incr breakDuration
				if (breakTaken[previousDriver])
					breakDuration[previousDriver] += durationOfThisService;
				
				assignmentSuccessful = true;
				
      } // if we can assign this service
			
      /* If we cannot assign this service */
      else {
        
 			  System.out.println("ENTERING ELSE");
 			  System.out.println("noOfServicesAssigned: " + noOfServicesAssigned);
 			  System.out.println("firstServiceAssigned: " + firstServiceAssigned);
				
        int services[] = new int[noOfServicesAssigned];
        for (int service = firstServiceAssigned; 
             service < (firstServiceAssigned + noOfServicesAssigned); 
             service++) {
          services[service] = allServices[service];
        } // for every service assigned
        
        TimeSlot thisSlot = new TimeSlot(currentDate,
                                         currentStartTime,
                                         currentEndTime,
                                         drivers[currentDriver],
                                         services);
				System.out.println(thisSlot.toString());
        timeSlots.add(thisSlot);
        
        // Add values to the minsWorkedSoFar matrix
        minsWorkedSoFar[currentDriver][0] += shiftDurationToday;
        minsWorkedSoFar[currentDriver][1] += minsWorkedSoFar[currentDriver][0];
        
        // The start time of the next TimeSlot 
        // is the start time of the next service 
				// (only needed if there is a next service..)
				if (currentService < (noOfServices - 1))
        	currentStartTime = startTimes[currentService+1];
        
        // The first service that will be assigned to the next driver
        if ((firstServiceAssigned + noOfServicesAssigned + 1) < noOfServices)
					firstServiceAssigned = (firstServiceAssigned + noOfServicesAssigned + 1);
				noOfServicesAssigned = 0;
        
				// If the previous driver had been on break 
				// and the duration of the break has been more than an hour
				// we can go back to them and assign new services
				if (breakTaken[previousDriver] 
						&& breakDuration[previousDriver] >= 60)
						currentDriver = previousDriver;
				else {
      	  // Take the next driver from the list
      	  // or start over from the list of drivers if we've gone through all of them once
      	  if (currentDriver < noOfDrivers - 1) currentDriver++;
      	  else currentDriver = 0;
				}
				
				// Now update the previous driver
		  	if (currentDriver > 0) previousDriver = currentDriver -1;
		  	else previousDriver = noOfDrivers - 1;
      } 
        
    } // for each service
   
    // Go to the next Date
    DataMining.getNextDate(currentDate);
                             
  } // generateDay
 
  /* Confirm that assigning a timeslot to a driver
   * will not breach the constraints (TRUE if assignment is allowed)
   * based on a Driver object, and the date and time
   * of the timeslot required*/
  private boolean constraintsCheck(int driver,
                                   Date requiredDate,
                                   int iterationDuration,
                                   int minsWorkedSoFarToday,
                                   int minsWorkedSoFarThisWeek,
      				                     boolean breakTaken ) {
    if (!Constraints.maximumDrivingTimeDay(minsWorkedSoFarToday, 
			                                     iterationDuration))
       return false;
    else if(!Constraints.maximumDrivingTimeWeek(minsWorkedSoFarThisWeek,
     						                                iterationDuration))
       return false;
    else if(!Constraints.minsWorkedContinuosly(minsWorkedSoFarToday, 
                                               iterationDuration, 
                                               breakTaken)) 
       return false;
    else	      
      return true;
  } // Constraints
  
  /* Return the ArrayList */
  public ArrayList<TimeSlot> getTimeslots() {
    return timeSlots;
  }
  
} // class Roster
