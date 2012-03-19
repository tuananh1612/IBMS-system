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
  public Date endDate;              // End date of timetable
  public TimeSlot[] timeSlots;      // the timeslots making up the timetable
  
  /* Create a timetable object for a specific service
   * from a required startDate to an endDate,
   * using only drivers from a specified list 
   * 
   * currently does not assign buses */
  public Roster(Date requiredStartDate, 
                Date requiredEndDate, 
                int[][] requiredService,   
                int[][] availableDrivers) {
    startDate = requiredStartDate;
    endDate = requiredEndDate;
   
    /* Get info needed to generate timeslots */ 
    
    /* Need to initialize the current start time as 
     * the earliest time on startDate when the bus first leaves the depot */
    int currentStartTime;   // NEEDS CODE
    int currentEndTime; 
    
    /* Initialize an array of iterations that correspond to this service
     * 
     * It will need to first loop through five weekdays, then Sat and Sun.
     * All iterations would still be in the same array
     * and iteratively assigned to drivers later.
     * 
     * iteration -- the route a service takes to and from the depot 
     * (because shifts need to start and end at the depot) */
    int[] iterations;       // NEEDS CODE
   
    /* 
     * Begin by going through each driver.
     * Try assigning as many iterations to a driver as you can
     * while not breaching the constraints OR
     * exceeding the average working hours OR
     * until where out of iterations
     * 
     * */
    while (!timetablePopulated) {
      for (int i = 0; i < availableDrivers.length(); i++ ) {
        int shiftDurationSoFar = 0;
        while (constraintsCheck && 
               shiftDurationSoFar < AVERAGE_WORKING_HOURS) {
          // assign an iteration
          // modify currentStartTime, currentEndTime accordingly
        } // while constraints OK and average hrs not exceeded
        
        /* Save timeslot to array */
        TimeSlot thisSlot = new TimeSlot(currentStartTime, 
                                         currentEndTime,
                                         availableDrivers[i]);
        timeSlots[i] = thisSlot;
      } // for each driver
    } // while the entire timetable has not been populated 
      
  } // Roster
  
  /* Confirm that assigning a timeslot to a driver
   * will not breach the constraints (TRUE if assignment is allowed)
   * based on a Driver object, and the date and time
   * of the timeslot required*/
  private boolean constraintsCheck(Driver driver,
                                   Date requiredDate,
                                   int requiredStartTime,
                                   int requiredEndTime) {
    // Use methods from Constraints class
    return true;
  } // Constraints
  
  /* Check whether the all of the slots 
   * from startDate to endDate have been filled */
  private boolean timetablePopulated() {
    // Have we gone through all of the iterations in the array?
    return false;
    // else return true;
  } // timetablePopulated
} // class Roster
