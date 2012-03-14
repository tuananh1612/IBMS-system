public class Constraints
{

  Boolean breakTaken = false;

  
  //2. The maximum driving time for any driver in any one day is 10 hours.
  public boolean maximumDrivingTimeDay(int hoursWorkedToday)
  {
    if(hoursWorkedToday <= 10)
      return true;
    else
      return false;
  }

  //3. There can be no more than 50 hours driven by any one driver in any one week.
  public boolean maximumDrivingTimeWeek(int driver)
  {
    if(DriverInfo.getHoursThisWeek(driver) <= 50)
      return true;
    else
     return false;
  }

  //4. A driver can drive for a maximum of 5 hours at any one time and must have
  // a break of at least one hour. Breaks can only be taken at the bus depot.
  public boolean hoursWorkedContinuosly(int hoursWorkedToday)
  {
    //if hours worked today is less than 5, the driver can still drive.
    if(hoursWorkedToday < 5)
      return true;
    //if the driving time reaches 5 hours, the driver musst take a break therefore, return false.
    else if(hoursWorkedToday == 5 && breakTaken == false)
      return false;
    //if the number of hours worked is greater than 5, then they are in the second driving period.
    else if(hoursWorkedToday > 5  && breakTaken == true)
      return true;
    
    //otherwise something we are outside the constraint so return false.
    else return false;
  }      
      
  public void takeBreak(int driver)
  {
    breakTaken = true;
  }  
   
}
/* ***I havent done methods to take into account these two constraints *** */
// 1. A roster is generated for each week based upon the timetable for that week.
// 2. A driver may specify up to two resting days for each week in which they will 
//not be available for work. (We assume not all drivers will choose the same two days.)
