import java.util.Date;


 //"Bridge" between user interface (UI) and database (DB).
 
public class DBBridge {

	private static boolean connectionOpen = false;

	public static int getDriversOnHoliday(Date day) 
{

          return database.busDatabase.record_count("*",
	"driver_availability", "day", day);

	}

	public static boolean isDriverValid(String reqName, String reqNumber)
			throws InvalidArgumentException
			 {
		try 
		{
			Integer.parseInt(reqNumber);
		}
		 catch (NumberFormatException e)
		 {
			throw new InvalidArgumentException("Not a number");
		}

		if (!connectionOpen)
			database.openBusDatabase();

		String nameInDatabase = null;

		try 
		{
			int ID = DriverInfo.findDriver(reqNumber);
			nameInDatabase = DriverInfo.getName(ID);
		} 
		
		catch (InvalidQueryException e)
		 {
			if (e.getMessage().equals("Nonexistent driver"))
				return false;
			throw e;
		}

		return nameInDatabase.equals(reqName);
	}

	public static int getDriverIndex(String reqName, String reqNumber)
			throws InvalidArgumentException 
			{
		try 
		{
			Integer.parseInt(reqNumber);
		}
		 catch (NumberFormatException e) {
			throw new InvalidArgumentException("Not a number");
		}

		if (!connectionOpen)
	       database.openBusDatabase();

		int ID = DriverInfo.findDriver(reqNumber);
		String nameInDatabase = DriverInfo.getName(ID);

		if (!nameInDatabase.equals(reqName))
			throw new InvalidArgumentException("Wrong name");

		return ID;
	}

	public static Date getTodaysDate() {
		if (!connectionOpen)
			database.openBusDatabase();

		return database.today();
	}
	

	public static int getHolidaysTaken(int driver)
	{
	  if (!connectionOpen)
		  database.openBusDatabase();
	  
	  return DriverInfo.getHolidaysTaken(driver);
	}
	

	 // Sets the new value of holidays taken.

	public static void setHolidaysTaken(int driver, int value)
	{
	  if (!connectionOpen)
		  database.openBusDatabase();
	  
	  DriverInfo.setHolidaysTaken(driver, value);
	}
	

	 //Returns TRUE if given driver is available on given date, false otherwise.

	public static boolean isAvailable(int driver, Date date)
	{

		
		return DriverInfo.isAvailable(driver, date);
	}
	
  
   //Set whether a driver is available on a given date.

  public static void setAvailable(int driver, Date date, boolean available)
  {
    if (!connectionOpen)
		  database.openBusDatabase();
		
		DriverInfo.setAvailable(driver, date, available);
  }

} 
