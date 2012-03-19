import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


 // class holds information about driver.

public class Driver
{
  private int AVAILABLE_HOLIDAYS = 25;
  private int MAX_DRIVERS_ON_HOLIDAYS = 10;

  private String driverName = null;
  private String driverNumber = null;
  private int driverIndex;
  private int noOfHolidaysTaken;
  private int holidayDuration;
  
  // requires drivers' full name and his/her ID number.
 
  public Driver(String reqName, String reqNumber)
                throws InvalidArgumentException
  {
    try
    {
      this.setDriverIndex( DBBridge.getDriverIndex(reqName, reqNumber) );
    } 
    catch (InvalidQueryException exception)
    {
      throw new InvalidArgumentException( exception.getMessage() );
    } 
    
    driverName = reqName;
    driverNumber = reqNumber;
    
    setHolidaysTaken( this.getHolidaysTaken() );
  }
  
  private int getDriverIndex()
  {
    return driverIndex;
  }
  
  private void setDriverIndex( int reqDriverIndex )
  {
    driverIndex = reqDriverIndex;
  }
  
  private void setHolidaysDuration(int reqDuration)
  {
    holidayDuration = reqDuration;
  }
  
  private int getHolidaysDuration()
  {
    return holidayDuration;
  }
  
  
  //Return number of holidays left.
  
  public int getHolidaysLeft()
  {
    return AVAILABLE_HOLIDAYS - getHolidaysTaken();
  } 
  
  
  //Returns number of holidays taken.
  
  public int getHolidaysTaken()
  {
    return DBBridge.getHolidaysTaken( this.getDriverIndex() );
  }
  
  
  private void setHolidaysTaken( int reqHolTaken)
  {
    noOfHolidaysTaken = reqHolTaken;
  }
  

  
  //Returns NULL when holidays are free to take. If validation fails,
  //it returns list of dates which are not available.
 
  public Date[] validateHolidays(Date startDate, Date endDate)
                                 throws InvalidArgumentException,
                                        NotEnoughHolidaysException
  {
    if ( startDate.before( DBBridge.getTodaysDate() ) )
    throw new InvalidArgumentException("Start date needs to be in the future");
    
    if ( startDate.after( endDate ) )
      throw new InvalidArgumentException("End date before start date");
    
    Calendar calendar = Calendar.getInstance();
  	Date today = calendar.getTime();
    
    setHolidaysDuration( this.daysBetween(startDate, endDate) );
    
    if (noOfHolidaysTaken + holidayDuration > AVAILABLE_HOLIDAYS)
      throw new NotEnoughHolidaysException("Wanted holidays: "
                  + getHolidaysDuration()
                  + " | available holidays: " + getHolidaysLeft());
    
    Date holidayDates[] = getHolidaysDates(startDate, endDate);
    
    Date datesNotValid[] = new Date[ this.getHolidaysDuration() ];
    
    int notValidDateCounter = 0;
    
    for (Date date : holidayDates)
      if (! DBBridge.isAvailable( this.getDriverIndex(), date ) )
        datesNotValid[ notValidDateCounter++ ] = date;
    
    if (notValidDateCounter > 0)
    {
      Date notValidDatesOnly[] = new Date[ notValidDateCounter ];
      
      int index = 0;
      
      for (Date date : datesNotValid)
        if (date != null)
          notValidDatesOnly[index++] = date;
          
      return notValidDatesOnly;
    }
    
    int index = 0;
    
    for (Date date : holidayDates)
      if ( DBBridge.getDriversOnHoliday( date ) >= MAX_DRIVERS_ON_HOLIDAYS)
        datesNotValid[index++] = date;
    
    if (index > 0)
      return datesNotValid;
      
    return null;
  } //validateHolidays
  
  private Date[] getHolidaysDates(Date startDate, Date endDate)
  {
    Date dates[] = new Date[ this.getHolidaysDuration() ];
    
    long interval = 1000 * 60 * 60 * 24; // 1 hour in millis * 24 hours = 1day
    long endTime = endDate.getTime();
    long currentTime = startDate.getTime();
    int index = 0;
    
    while (currentTime <= endTime)
    {
      dates[index] = new Date(currentTime);
      index++;
      currentTime += interval;
    }
    
    return dates;
  }
  
  //Returns number of days between two dates.
  private int daysBetween(Date startDate, Date endDate)
  {
    Calendar cal1 = new GregorianCalendar();
    Calendar cal2 = new GregorianCalendar();

    cal1.setTime( startDate );
    cal1.set( Calendar.HOUR, 0);
    cal1.set( Calendar.MINUTE, 0);
    cal1.set( Calendar.SECOND, 0);
    
    cal2.setTime( endDate );
    cal2.set( Calendar.HOUR, 0);
    cal2.set( Calendar.MINUTE, 0);
    cal2.set( Calendar.SECOND, 0);
    
    return (int)((cal2.getTime().getTime() - cal1.getTime().getTime() )
                / (24 * 3600 * 1000) + 1);
	} // daysBetween
  
  
  // Returns NULL when holidays are booked. If validation booking process fails,
  //it returns list of dates which are not available.
  
  public Date[] bookHolidays(Date startDate, Date endDate)
                             throws InvalidArgumentException,
                                    NotEnoughHolidaysException
  {
    Date[] bookDates;
    
    bookDates = validateHolidays(startDate, endDate);

    if (bookDates != null)
      return bookDates;
    
    bookDates = getHolidaysDates(startDate, endDate);
    
    DBBridge.setHolidaysTaken(this.getDriverIndex(),
                        this.getHolidaysTaken() + this.getHolidaysDuration());
    
    for (Date date : bookDates)
      DBBridge.setAvailable(this.getDriverIndex(), date, false);
    
    return null;
  }
  
} 
