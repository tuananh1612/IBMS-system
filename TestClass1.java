import org.junit.*;
import static org.junit.Assert.*;

public class TestClass1 {
  private static Roster newRoster;
  private static boolean driveTimeDay;
      
      
  @Before
  public void setUp()
  {
    newRoster = new Roster();
  }

  @Test
  public void testMaxDriveTimeDayOver()  // over 10 hours per day (should be F)
  {
    assertFalse(
  }  
  
  @Test
  public void testMaxDriveTimeDayUnder() //under 10 hours per day (should be T)
  {
    assertTrue(
  }   
   
  @TEST
  public void testMaxDriveTimeWeekOver()// over 10 hours per week (should be F) 
  {
    assertFalse(
  }
    
  @TEST
  public void testMaxDriveTimeWeekUnder()// under 10 hours per week(should be T) 
  {
    assertTrue(
  }
  
  @TEST
  public void testDriverBreakOver() //cant work >5 hours continuosly(should be F)
  {
    assertFalse(
  }
  
  @TEST
  public void testDriverBreakUnder()//can work <5 hours continuosly(should be T)
  {
    assertTrue(
  }
  
   @TEST
  public void testDriverWorkingDaysOver() //cant work >5 days p week(should be F)
  {
    assertFalse(
  }
  
  @TEST
  public void testDriverWorkingDaysUnder()//can work <5 days p. week(should be T)
  {
    assertTrue(
  }
  
  
}
