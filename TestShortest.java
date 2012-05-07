import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;

public class TestShortest {
	public static void main(String[] args) {
		String startStop = "Glossop, Henry Street";
		String endStop = "Strines, Royal Oak";
		Date startDate = new Date();
		int time = 0700;
		
		PlanJourney plan = new PlanJourney(startStop, endStop, startDate, time);
		int[][] journey = plan.getJourney();
		
		for (int i = 0; i < journey.length; i++) {
			System.out.println("Take route " + journey[i][1] + " from stop " 
												 + journey[i][0] + " at " + journey[i][2] + ".");
		}
		
	} // main
} // TestShortest
