import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList; 

/* THIS IS VERY MUCH UNFINISHED!! 
 * lol look at all the comments.. anyway they should make clear 
 * what i'm trying to achieve */


/* A class that will generate a driver timetable 
 * based on the information it gets from DataMining */
public class Roster {

  /* Just hard coding this at the moment 
   * until we're able to to get it from DataMining 
   * 
   * Reminder: we use the concept of an average working time 
   * to guarantee fairness between all the drivers.
   * During rostering we try to make the duration of each shift
   * as close to this as possible */
  public int AVERAGE_WORKING_HOURS = 5;
  
  /* Note: I have no memory of how these class variables had to
   * be initialized, am I doing it right? */
  public Date startDate;            // Start date of timetable
  public TimeSlot[] timeSlots;      // the timeslots making up the timetable
  
  /* Create a timetable object for a specific service
   * 7 days from a required startDate,
   * using only drivers from a specified list 
   * 
   * currently does not assign buses */
  public Roster(Date requiredStartDate, 
                int[][] requiredService,   
                int[][] availableDrivers) {
    
    startDate = requiredStartDate;
   
    /* Get info needed to generate timeslots */ 
    
    /* Need to initialize the current start time as 
     * the earliest time on startDate when the bus first leaves the depot */
    int currentStartTime = requiredService[0][0];    // time service leaves depot on Monday 
    int currentEndTime;
   
    /* Build an array that contains all of the iterations
     * of this service during the specified dates.
     * Determining the type of day is easy
     * since we know that startDate is always a Monday */
    int[][] iterations;
    for (int i = 0; i < 7; i++) {
      /* If it's a weekday */
      if (i < 6) {
        getWeekdayIterations();
        // append to iterations
      }
      /* If it's a Saturday */
      if (i == 6) {
        getSatIterations();
        // append to iterations
      }
      /* If it's a Sunday */
      if (i == 7) {
        getSunIterations();
        // append to iterations
      }
    }
    
    /* The number of iterations during this week for this service */
    int numberOfIterations = iterations.length();
   
    /* 
     * Begin by going through each driver.
     * Try assigning as many iterations to a driver as you can
     * while not breaching the constraints OR
     * exceeding the average working hours OR
     * until we're out of iterations
     * 
     * */
    while (!timetablePopulated) {
        
      /* Get the duration of the next iteration in the list */
      int durationOfNextIteration = iterations[0][0] - iterations[0][1];
      
      for (int i = 0; i < availableDrivers.length(); i++ ) {
        int shiftDurationSoFar = 0;
        currentStartTime; 
        while (constraintsCheck && 
               (shiftDurationSoFar + durationOfNextIteration) <
                AVERAGE_WORKING_HOURS) {
          shiftDurationSoFar += durationOfNextIteration;
          
          // modify currentStartTime, currentEndTime accordingly
        } // while constraints OK and average hrs not exceeded
        
        /* Save timeslot to array */
        TimeSlot thisSlot = new TimeSlot(currentStartTime, 
                                         currentEndTime,
                                         availableDrivers[i]);
        timeSlots[] = thisSlot;
      } // for each driver
    } // while the entire timetable has not been populated 
      
  } // Roster
  
  /* Confirm that assigning a timeslot to a driver
   * will not breach the constraints (TRUE if assignment is allowed)
   * based on a Driver object, and the date and time
   * of the timeslot required*/
  private boolean constraintsCheck(int driver,
                                   Date requiredDate,
                                   int iterationDuration,
                                   int minsWorkedSoFarToday,
                                   int minsWorkedSoFarThisWeek) {
    // Use methods from Constraints class
    return true;
  } // Constraints
  
  /* Check whether the all of the slots 
   * from startDate to endDate have been filled */
  private boolean timetablePopulated() {
    // Have we gone through all of the iterations in the array?
    return true;
    // else return false;
  } // timetablePopulated
    
  /* Initialize an array of iterations that correspond to this service
   * 
   * iteration -- the route a service takes to and from the depot 
   * (because shifts need to start and end at the depot) */
  private int[][] getWeekdayIterations() { 
    
  } // getWeekdayIterations
  
  private int[][] getSatIterations() { 
    
  } // getSatIterations
  
  private int[][] getSunIterations() { 
    
  } // getSunIterations

} class Roster
