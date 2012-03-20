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
  private int currentStartTime;
  private int currentEndTime;
  private int noOfDrivers;
  private int[noOfDrivers][2] minsWorkedSoFar;
  
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
                int avgWorkHrs) {
                
    startDate = requiredStartDate;
   
    /* Get info needed to generate TimeSlots 
     * *******************************************************************/ 
   
    // Number of drivers 
    int noOfDrivers = availableDrivers[0].length;
    
    // Number of services 
    int noOfServicesW = serviceInfoWeekday[0].length;
    int noOfServicesSat = serviceInfoSat[0].length;
    int noOfServicesSun = serviceInfoSun[0].length;
    
    // Time service leaves depot on Monday 
    currentStartTime = serviceInfoWeekday[1][0];   
    
    // Time earliest service gets back to depot
    currentEndTime = serviceInfoWeekday[2][0];
    
    // Initialize array of time worked so far for each driver
    for (int driver = 0; driver < noOfDrivers; driver++) {
      minsWorkedSoFar[driver][0] = 0;   // During this day
      minsWorkedSoFar[driver][1] = 0;   // During this week
    } 
    
    // The current date we are on
    currentDate = startDate;
    
    /* WEEKDAYS
     * ********************************************************************/
    for (int weekday = 0; weekday < 5; weekday++)
      generateWeekday(noOfDrivers, 
                      noOfServicesW,
                      currentStartTime,
                      currentEndTime,
                      currentDate);
    /* 
     * Take a driver
     * Try assigning this driver while keeping track of time worked so far
     * If not possible to add more
     * Take next driver
     * and add the timeslot object to the ArrayList
     * 
     * */
    
    // The first driver in availableDrivers
    int currentDriver = 0;
    int shiftDurationToday = minsWorkedSoFar[currentDriver][0];
    int shiftDurationWeek = minsWorkedSoFar[currentDriver][1];
    
    for (int weekday = 0; weekday < 5; weekday++) {
      
      // The number of services we have managed to assign to one driver
      noOfServicesAssigned = 0;
      firstServiceAssigned = 0;
      
      for (int currentService = 0; currentService < noOfServices; currentService++) {
        
        // The duration of the this service 
        int durationOfThisService = serviceInfoWeekday[currentService][3];
        
        if (constraintsCheck(availableDrivers[0][currentDriver],
                             currentDate,
                             durationOfThisService,
                             shiftDurationToday,
                             shiftDurationWeek) &&
            (shiftDurationToday + durationOfThisService) < avgWorkHrs) {
          
          // Increment time worked today
          shiftDurationToday += durationOfThisService;
          
          // Update end time
          currentEndTime = serviceInfoWeekday[2][currentService];
          
          // Increment noOfServicesAssigned
          noOfServicesAssigned++;
        } // if we can assign this service
        
        else {
          
          int[noOfSerivesAssigned] services;
          for (int service = firstServiceAssigned; 
               service < (firstServiceAssigned + noOfServicesAssigned); 
               service++) {
            services[service] = serviceInfoWeekday[0][service];
          } // for every service assigned
          
          TimeSlot thisSlot = new TimeSlot(currentDate,
                                           currentStartTime,
                                           currentEndTime,
                                           availableDrivers[0][currentDriver],
                                           services);
          timeslots.add(thisSlot);
          
          
          // Add values to the minsWorkedSoFar matrix
          minsWorkedSoFar[currentDriver][0] += shiftDurationToday;
          minsWorkedSoFar[currentDriver][1] += minsWorkedSoFar[currentDriver][0];
          
          // The start time of the next TimeSlot 
          // is the start time of the next service
          currentStartTime = serviceInfoWeekday[1][currentService+1];
          
          // The first service that will be assigned to the next driver
          firstServiceAssigned += (firstServiceAssigned + noOfServicesAssigned + 1);
          
          // Take the next driver from the list
          currentDriver++;
          
        } // if we cannot assign this service
     
     } // for each service
     
     // Go to the next Date
     DataMining.getNextDate(currentDate);
     
    } // for five weekdays
    
    /* SATURDAY
     * ********************************************************************/
    
    /* SUNDAY
     * ********************************************************************/
    
  } // Roster
  
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
    return timeslots;
  }
  
} // class Roster
