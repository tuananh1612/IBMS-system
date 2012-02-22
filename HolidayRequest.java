import java.text.ParseException;
import javax.swing.JTextField;
import java.util.Calendar;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.Container;
import java.awt.GridLayout;



public class HolidayRequest extends JFrame implements ActionListener 
{
    private final int SIZE = 25;
    //number of holidays
    
    private final JTextField input1 = new JTextField(SIZE);
    private final JTextField input2 = new JTextField(SIZE);
    private final JTextField input3 = new JTextField(SIZE);
    private final JTextField input4 = new JTextField(SIZE);
    private final JTextField validJTextField = new JTextField(SIZE);    
    private final JTextField startHolJTextField = new JTextField(SIZE);
    private final JTextField endHolJTextField = new JTextField(SIZE);
    private final JTextField datesAvailable = new JTextField(SIZE);
    private final JTextField bookedOrNot = new JTextField(SIZE);
    
    private JButton bookJButton;    
    private JButton datesCheckJButton; 
    private JButton validateJButton;
    
    private Driver driver;
    private Date holidayStartDate;
    private Date holidayEndDate;
    private boolean show = true;
    Container contents;
    
    public HolidayRequest() 
    {
        //the title of the window
        setTitle("Holiday Request Form");
        contents = getContentPane();
        contents.setLayout(new GridLayout(0, 1));
	
	// Driver's Details
        contents.add(new JLabel("Driver Name"));
        contents.add(input1);
        contents.add(new JLabel("Driver Number"));
        contents.add(input2);
	validateJButton = new JButton("Log in");
	contents.add(validateJButton);
        validateJButton.addActionListener(this);
	contents.add(validJTextField);
	validJTextField.setText("");
	validJTextField.setEditable(false);
	
	// Enter Holiday start date
	contents.add(startHolJTextField);
	startHolJTextField.setText("Holiday Start Date (dd/mm/yyyy)");
	startHolJTextField.setEnabled(true);
        startHolJTextField.setEditable(false);	
        contents.add(input3);
	input3.setEnabled(false);
	
	// Enter Holiday end date
	contents.add(endHolJTextField);
	endHolJTextField.setText("Holiday End Date (dd/mm/yyyy)");
	endHolJTextField.setEnabled(true);
	endHolJTextField.setEditable(false);	  
        contents.add(input4);
        input4.setEnabled(false);
	
	
        //Submit the dates to check
        datesCheckJButton = new JButton("Submit Dates");
        contents.add(datesCheckJButton);
        datesCheckJButton.addActionListener(this);
	datesCheckJButton.setEnabled(false);
	
	//Result of holiday Availability
        contents.add(datesAvailable);
	datesAvailable.setText("");
	datesAvailable.setEditable(false);
        	
	//Book holiday
	bookJButton = new JButton("Book This Holiday");
	contents.add(bookJButton);
	bookJButton.addActionListener(this);
	bookJButton.setEnabled(false);
        
	// Result of booking
	contents.add(bookedOrNot);
	bookedOrNot.setEnabled(false);
	bookedOrNot.setEditable(false);
	    
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }//HolidayRequest
    
    //Converts a string to a date
    Date StringToDate(String s) throws ParseException 
    {
        DateFormat df = DateFormat.getInstance();
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        sdf.applyPattern("dd/MM/yyyy");
        return df.parse(s);
    } //StringToDate
    
    
    public void actionPerformed(ActionEvent event) 
    {
              
       // validate the drivers details
       if(event.getSource() == validateJButton)
       {
	  // get the driver name and number	  
          String driverName = input1.getText();
	  String driverNumber = input2.getText();
	  
	  // Create new driver object, if driver does not exist an exception
	  // shall occur
	  try 
	  {
	    driver = new Driver(driverName, driverNumber);
	    input1.setEnabled(false);
	    input2.setEnabled(false);
	    validateJButton.setEnabled(false);
	    input3.setEnabled(true);
	    input4.setEnabled(true);
	    validJTextField.setText("Enter Holiday Dates. You have " + driver.getHolidaysLeft() + " holidays left.");
	    startHolJTextField.setEnabled(true);
	    endHolJTextField.setEnabled(true);
	    datesCheckJButton.setEnabled(true);		    
	  } 
	  
	  catch (InvalidArgumentException e)
	  {
	     String message = " Drive name or/and number Unkown. \n" + e.getMessage() + "\n"
		 +" Please Try Again";
	     JOptionPane pane = new JOptionPane(message);
                  JDialog dialog = pane.createDialog(new JFrame(),
                                                     "Unkown Driver ");
		                                                
                  dialog.setVisible(true);
	     
	  }   	  
      } 
      
      else if(event.getSource() == datesCheckJButton)
      {    
	 String holidayStart = input3.getText();
         String holidayEnd = input4.getText();	 
	 
	 
	 
	 // Convert strings to date objects
	 try 
	 {	    
            holidayStartDate = StringToDate(holidayStart);
            holidayEndDate = StringToDate(holidayEnd);
	 } 
	 
	 catch (ParseException ex)
	 {
            Logger.getLogger(HolidayRequest.class.getName()).log(Level.SEVERE, null, ex);
	 }  
         
	 
	 {
	    try
	    {
	       Date unavailableDates[] =
	           driver.validateHolidays(holidayStartDate,holidayEndDate);
	       // All dates are available
	       if (unavailableDates == null)
	       {
	          bookJButton.setEnabled(true);	       
	          input3.setEnabled(false);
	          input4.setEnabled(false);
	          datesAvailable.setText("Holiday available");	       
	       }
	       else
	       {
		  // Some dates are not available
		  // Extract dates from array and output to dialogue box		 
		  String message = "These dates are not available:\n";
		  DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		  for (Date date : unavailableDates)
		    message += df.format(date) + "\n";
		 		  
                  JOptionPane pane = new JOptionPane(message);
                  JDialog dialog = pane.createDialog(new JFrame(),
                                                        "Dates Not Available");
                  dialog.setVisible(true);
	        }
	 
	    } 
	   
	   catch(NotEnoughHolidaysException e)
	    {	      	     
	        String message = "You do not have enough holidays\n "
		       + "Remaining Holidays: " + driver.getHolidaysLeft();			 		  
                JOptionPane pane = new JOptionPane(message);
                JDialog dialog = pane.createDialog(new JFrame(), "Holiday hours problem");
                dialog.setVisible(true);	     	        	      	      
	    }
	   catch(InvalidArgumentException e)
	    {
	       
	      JOptionPane pane = new JOptionPane(e.getMessage());
                JDialog dialog = pane.createDialog(new JFrame(), "Invalid date(s)");
                dialog.setVisible(true);
	     }	           
	
	    } 
      } 
      else if(event.getSource() == bookJButton)
      {  
	  try
	  {
	      Date unableToBook[] = 
	         driver.bookHolidays(holidayStartDate,holidayEndDate);
	  
	      if(unableToBook == null)
	      {
	         bookedOrNot.setText("Holiday Booked");
	         bookedOrNot.setEnabled(true);	      
	      }
	      else
	      {		  	        
		  // Some dates are not available
		  // Extract dates from array and output to dialogue box		 
		  String message = "These dates are not available:\n";
		  DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		  for (Date date : unableToBook)
		    message += df.format(date) + "\n";
		 		  
                  JOptionPane pane = new JOptionPane(message);
                  JDialog dialog = pane.createDialog(new JFrame(), "Unable to"
		                                                  + " book");
                  dialog.setVisible(true);
	      }
	  } 
	  catch(NotEnoughHolidaysException e)
	  {
	     datesAvailable.setText("You do not have enough Holdays");
	  }
	  catch(InvalidArgumentException e)
	  {
	    datesAvailable.setText( e.getMessage() );
	  }
      } 
	
      	
   }  
      
       
   public static void main(String[] args) 
   {
      HolidayRequest holidayRequestForm = new HolidayRequest();
      holidayRequestForm.setVisible(true);
   }
    
}
      
