public class Constraints
{
  
  
  // Should this variable not be part of a Driver class?
  // The value is different for each driver
  //Boolean breakTaken = false;

  
  //2. The maximum driving time for any driver in any one day is 10 hours.
  public static boolean maximumDrivingTimeDay (int minsWorkedToday, 
      					int iterationDuration)
  {
    if((minsWorkedToday + iterationDuration)<= 600)
      return true;
    else
      return false;
  }

  //3. There can be no more than 50 hours driven by any one driver in any one week.
  public static boolean maximumDrivingTimeWeek(int minsWorkedSoFarThisWeek,
      					int iterationDuration)
  {
    if((minsWorkedSoFarThisWeek + iterationDuration)<= 3000)
      return true;
    else
     return false;
  }

  //4. A driver can drive for a maximum of 5 hours at any one time and must have
  // a break of at least one hour. Breaks can only be taken at the bus depot.
  public static boolean minsWorkedContinuosly(int minsWorkedToday,
      				       int iterationDuration, 
      				       Boolean breakTaken)
  {
  
    //if hours worked today is less than 5, the driver can still drive.
    if((minsWorkedToday + iterationDuration) <= 300) {
      return true;
    }
    //if the driving time reaches 5 hours, the driver musst take a break therefore, return false.
    else if((minsWorkedToday + iterationDuration) > 300 && breakTaken == false) {
      return false;
    }
    //if the number of hours worked is greater than 5, then they are in the second driving period.
    else if((minsWorkedToday + iterationDuration) > 300  && breakTaken == true) {
      return true;
   } 
    //otherwise something we are outside the constraint so return false.
    else return false;
  }      
      
 // public static void takeBreak(int driver)
 // {
  //  breakTaken = true;
  //}  
   
}
