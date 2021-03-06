import java.text.DateFormat;    
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList; 

/* Store a TimeSlot object
 * Consists of a Date, a startTime, endTime, driverID and 
 * an array of services*/
public class TimeSlot {
  public Date date;
  public int startTime;
  public int endTime;
  public int driver;
  public int[] services;
  
  public TimeSlot(Date requiredDate,
                  int requiredStartTime,
                  int requiredEndTime,
                  int requiredDriver,
                  int[] servicesList) {
    date = requiredDate;
    startTime = requiredStartTime;
    endTime = requiredEndTime;
    driver = requiredDriver;
    services = servicesList;
  } // Constructor
  
  /* Print out the TimeSlot */
  @Override
  public String toString() {
	  String output = "Date: " + date.toString() + "\tFrom: " + startTime
	  					+ "\tTo: " + endTime + "\tDriver ID: " + driver
	  					+ "\tDriving services: ";
	  for (int i = 0; i < services.length; i ++) {
		  output += "" + services[i] + ", ";
	  }
	  output += "\n";
    /*return "\nDate: " 
            + date.toString() 
            + "\n From: " 
            + startTime 
            + "\nTo: "
            + endTime 
            + "\nDriver ID: "
            + driver
            + "\n Using services: "
            + services;*/
	  return output;
  } // toString
  
} // TimeSlot
