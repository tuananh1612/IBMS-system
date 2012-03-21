import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class HolidayRequestDay extends JFrame implements ActionListener {

	private JPanel contentPane;
	private int remainDays, taken;
	private int limit = 25;
	private int startDate, startMonth, startYear;
	private int endDate, endMonth, endYear;
	private int ID;
	private JTextField date1, month1, year1, date2, month2, year2;
	private java.util.Date startDay, endDay, tempDay;
	private String list;
	private final int[] monthArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	private final int[] dayArray1 = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private final int[] dayArray2 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolidayRequestDay frame = new HolidayRequestDay(0);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HolidayRequestDay(int driverID) {
		database.openBusDatabase();
		//ID = 2012;
		ID = driverID;
		taken = DriverInfo.getHolidaysTaken(ID);
		remainDays = limit - taken;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel northPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel northPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//Add code here - Get an array of date, get them in a string
		
		JLabel label1 = new JLabel("Holidays taken:");
		JTextArea dayList = new JTextArea(7, 30);
		JScrollPane scrollPane = new JScrollPane(dayList); 
		dayList.setEditable(false);
		JLabel label2 = new JLabel("Remaining days:");
		JLabel remainDay = new JLabel();
		remainDay.setText("" + remainDays);
		northPanel1.add(label2);
		northPanel1.add(remainDay);
		northPanel2.add(label1);
		//northPanel2.add(dayList);
		northPanel2.add(scrollPane);
		northPanel.add(BorderLayout.NORTH, northPanel1);
		northPanel.add(BorderLayout.SOUTH, northPanel2);
		JPanel centerPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//JPanel centerPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label3 = new JLabel("Staring date:");
		date1 = new JTextField();
		date1.setPreferredSize(new Dimension(30, 30));
		month1 = new JTextField();
		month1.setPreferredSize(new Dimension(30, 30));
		year1 = new JTextField();
		year1.setPreferredSize(new Dimension(60, 30));
		JLabel label4 = new JLabel("Until:");
		date2 = new JTextField();
		date2.setPreferredSize(new Dimension(30, 30));
		month2 = new JTextField();
		month2.setPreferredSize(new Dimension(30, 30));
		year2 = new JTextField();
		year2.setPreferredSize(new Dimension(60, 30));
		centerPanel1.add(label3);
		centerPanel1.add(date1);
		centerPanel1.add(month1);
		centerPanel1.add(year1);
		centerPanel1.add(label4);
		centerPanel1.add(date2);
		centerPanel1.add(month2);
		centerPanel1.add(year2);
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton OKButton = new JButton("OK");
		JButton CancelButton = new JButton("Cancel");
		OKButton.setActionCommand("OK");
		CancelButton.setActionCommand("Cancel");
		OKButton.addActionListener(this);
		CancelButton.addActionListener(this);
		OKButton.setPreferredSize(new Dimension(100, 30));
		CancelButton.setPreferredSize(new Dimension(100, 30));
		southPanel.add(OKButton);
		southPanel.add(CancelButton);
		contentPane.add(BorderLayout.NORTH, northPanel);
		contentPane.add(BorderLayout.CENTER, centerPanel1);
		//contentPane.add(BorderLayout.CENTER, centerPanel2);
		contentPane.add(BorderLayout.SOUTH, southPanel);
		setContentPane(contentPane);
		//pack();
	}

	public void actionPerformed(ActionEvent event) {
		if ("OK".equals(event.getActionCommand())) {
			if (checkEmptyFields()) {
				
			}
			else {
				startDate = Integer.parseInt(date1.getText());
				startMonth = Integer.parseInt(month1.getText());
				startYear = Integer.parseInt(year1.getText());
				endDate = Integer.parseInt(date2.getText());
				endMonth = Integer.parseInt(month2.getText());
				endYear = Integer.parseInt(year2.getText());
				if (!checkDate(startDate, startMonth, startYear)) {
				}
				else if (!checkDate(endDate, endMonth, endYear)) {
				}
				else if (!checkDate(startYear, endYear)) {
				}
				else if (!checkDate(startDate, endDate)) {
				}
				else if (!checkDate(startMonth, endMonth)) {
				}
				//startDay = new Date(startYear, startMonth - 1, startDate);
				//endDay = new Date(endYear, endMonth - 1, endDate);
				else {
					endYear -= 1900;
					startYear -= 1900;
					endMonth -= 1;
					startMonth -= 1;
					startDay = new Date(startYear, startMonth - 1, startDate);
					endDay = new Date(endYear, endMonth - 1, endDate);
					if (startDay.before(database.today())) {
						JOptionPane.showMessageDialog(this, "Please select a future date.");
					}
					else if (endDay.before(database.today())) {
						JOptionPane.showMessageDialog(this, "Please select a future date.");
					}
					else {
						setHoliday();
						//System.out.println(startDay.toString());
						//System.out.println(endDay.toString());
						//System.out.println(taken);
						HolidayConfirm nextFrame = new HolidayConfirm(list);
						nextFrame.setVisible(true);
						this.dispose();
					}
				}
			}
		}
		else if ("Cancel".equals(event.getActionCommand())) {
			System.exit(0);
		}
	}
	
	public boolean checkDate(int day, int month, int year) {
		if (month > 12) {
			JOptionPane.showMessageDialog(this, "Please check the month.");
			return false;
		}
		else if (month == 2) {
			if ((year % 4) == 0) {
				if ((day > 29) || (day < 1)) {
					JOptionPane.showMessageDialog(this, "Please check the date.");
					return false;
				}
				else {
					return true;
				}
				
			}
			else {
				if ((day > 28) || (day < 1)) {
					JOptionPane.showMessageDialog(this, "Please check the date.");
					return false;
				}
				else {
					return true;
				}
			}
		}
		else if ((month ==1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12)) {
			if ((day > 31) || (day < 1)) {
				JOptionPane.showMessageDialog(this, "Please check the date.");
				return false;
			}
			else {
				return true;
			}
		}
		else if ((month == 4) || (month == 6) || (month == 9)
				|| (month == 11)) {
			if ((day > 30) || (day < 1)) {
				JOptionPane.showMessageDialog(this, "Please check the date.");
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}
	
	public boolean checkDate(int year1, int year2) {
		if (year2 < year1) {
			JOptionPane.showMessageDialog(this, "Please check the fields.");
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean checkEmptyFields() {
		if (date1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (date2.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (month1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (month2.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (year1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (year2.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setHoliday() {
		int number = 0;
		if (startYear == endYear) {
			if (startMonth == endMonth) {
				number = endDate - startDate + 1;
				if (number > remainDays) {
					JOptionPane.showMessageDialog(this, "Please check the fields.");
				}
				else {
					for (int i = startDate; i <= endDate; i++) {
						tempDay = new Date(startYear, startMonth, i);
						DriverInfo.setAvailable(ID, tempDay, false);
						list = list + tempDay.toString() + "\n";
						taken = DriverInfo.getHolidaysTaken(ID);
						if (!DriverInfo.isAvailable(ID, tempDay)) {
						  taken ++;
						  //System.out.println("test");
						}
						DriverInfo.setHolidaysTaken(ID, taken);
					}
				}
			}
			//Month different
			else {
				if ((startYear % 4) == 0) {
					for (int i = startMonth; i <= endMonth; i ++) {
						number = endDate + dayArray2[i];
					}
					number = number - startDate + 1;
					if (number > remainDays) {
						JOptionPane.showMessageDialog(this, "Please check the fields.");
					}
					else {
						for (int i = startDate; i <= dayArray2[startMonth - 1]; i++) {
							tempDay = new Date(startYear, startMonth, i);
							DriverInfo.setAvailable(ID, tempDay, false);
							list = list + tempDay.toString() + "\n";
							taken = DriverInfo.getHolidaysTaken(ID);
							if (!DriverInfo.isAvailable(ID, tempDay)) {
								taken ++;
							}
							DriverInfo.setHolidaysTaken(ID, taken);
						}
						for (int i = 1; i <= endDate; i ++) {
							tempDay = new Date(startYear, endMonth, i);
							DriverInfo.setAvailable(ID, tempDay, false);
							list = list + tempDay.toString() + "\n";
							taken = DriverInfo.getHolidaysTaken(ID);
							if (!DriverInfo.isAvailable(ID, tempDay)) {
								taken ++;
							}
							DriverInfo.setHolidaysTaken(ID, taken);
						}
					}
				}
				else {
					for (int i = startMonth; i <= endMonth; i ++) {
						number = endDate + dayArray1[i];
					}
					number = number - startDate + 1;
					if (number > remainDays) {
						JOptionPane.showMessageDialog(this, "Please check the fields.");
					}
					else {
						for (int i = startDate; i <= dayArray1[startMonth - 1]; i++) {
							tempDay = new Date(startYear, startMonth, i);
							DriverInfo.setAvailable(ID, tempDay, false);
							list = list + tempDay.toString() + "\n";
							taken = DriverInfo.getHolidaysTaken(ID);
							if (!DriverInfo.isAvailable(ID, tempDay)) {
								taken ++;
							}
							DriverInfo.setHolidaysTaken(ID, taken);
						}
						for (int i = 1; i <= endDate; i ++) {
							tempDay = new Date(startYear, endMonth, i);
							DriverInfo.setAvailable(ID, tempDay, false);
							list = list + tempDay.toString() + "\n";
							taken = DriverInfo.getHolidaysTaken(ID);
							if (!DriverInfo.isAvailable(ID, tempDay)) {
								taken ++;
							}
							DriverInfo.setHolidaysTaken(ID, taken);
						}
					}
				}
			}
		}
		//Year
		else {
			number = endDate - startDate + 32;
			if (number > remainDays) {
				JOptionPane.showMessageDialog(this, "Please check the fields.");
			}
			else {
				for (int i = startDate; i <= 31; i++) {
					tempDay = new Date(startYear, startMonth, i);
					DriverInfo.setAvailable(ID, tempDay, false);
					list = list + tempDay.toString() + "\n";
					taken = DriverInfo.getHolidaysTaken(ID);
					if (!DriverInfo.isAvailable(ID, tempDay)) {
						taken ++;
					}
					DriverInfo.setHolidaysTaken(ID, taken);
				}
				for (int i = 1; i < endDate; i ++) {
					tempDay = new Date(endYear, endMonth, i);
					DriverInfo.setAvailable(ID, tempDay, false);
					list = list + tempDay.toString() + "\n";
					taken = DriverInfo.getHolidaysTaken(ID);
					if (!DriverInfo.isAvailable(ID, tempDay)) {
						taken ++;
					}
					DriverInfo.setHolidaysTaken(ID, taken);
				}
			}
		}
	}
}
