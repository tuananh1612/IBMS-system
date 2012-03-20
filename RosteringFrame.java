import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RosteringFrame extends JFrame implements ActionListener {

	JPanel contentPanel;
	private JButton backButton;
	private JButton createButton, viewButton;
	private JTextField day1, day2, month1, month2, year1, year2, periodField;
	String[] option = {"Ill driver", "Broken bus"};
	private JLabel startLabel, endLabel, periodLabel;
	private int driverID;
	private Date date1, date2;
	int startDate, startMonth, startYear, endDate, endMonth, endYear;
	ArrayList<TimeSlot> createdRoster;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RosteringFrame frame = new RosteringFrame(0);
					frame.setTitle("Report frame");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public RosteringFrame(int ID) {
		driverID = ID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(3, 1, 5, 5));
		JPanel northPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel subNorth1 = new JPanel();
		periodLabel = new JLabel("Current period: ");
		periodField = new JTextField();
		periodLabel.setPreferredSize(new Dimension(105, 30));
		periodField.setPreferredSize(new Dimension(105, 30));
		subNorth1.add(periodLabel);
		subNorth1.add(periodField);
		northPanel.add(subNorth1);
		JPanel subNorth2 = new JPanel(new GridBagLayout());
		createButton = new JButton("Create roster");
		viewButton = new JButton("View roster");
		createButton.setPreferredSize(new Dimension(110, 30));
		viewButton.setPreferredSize(new Dimension(100, 30));
		subNorth2.add(createButton);
		subNorth2.add(viewButton);
		northPanel.add(subNorth2);
		contentPanel.add(northPanel);
		//Mid panel
		JPanel midPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel subMid1 = new JPanel(new GridBagLayout());
		JPanel subMid2 = new JPanel(new GridBagLayout());
		startLabel = new JLabel("Start:");
		day1 = new JTextField();
		month1 = new JTextField();
		year1 = new JTextField();
		startLabel.setPreferredSize(new Dimension(40, 30));
		day1.setPreferredSize(new Dimension(40, 30));
		month1.setPreferredSize(new Dimension(40, 30));
		year1.setPreferredSize(new Dimension(40, 30));
		subMid1.add(startLabel);
		subMid1.add(day1);
		subMid1.add(month1);
		subMid1.add(year1);
		midPanel.add(subMid1);
		endLabel = new JLabel("End:");
		day2 = new JTextField();
		month2 = new JTextField();
		year2 = new JTextField();
		endLabel.setPreferredSize(new Dimension(40, 30));
		day2.setPreferredSize(new Dimension(40, 30));
		month2.setPreferredSize(new Dimension(40, 30));
		year2.setPreferredSize(new Dimension(40, 30));
		subMid2.add(endLabel);
		subMid2.add(day2);
		subMid2.add(month2);
		subMid2.add(year2);
		midPanel.add(subMid2);
		//South panel
		JPanel southPanel = new JPanel(new GridBagLayout());
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		backButton.setPreferredSize(new Dimension(80, 30));
		southPanel.add(backButton);
		contentPanel.add(midPanel);
		contentPanel.add(southPanel);
		setContentPane(contentPanel);
		pack();
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == backButton) {
			OptionFrame nextFrame = new OptionFrame(driverID);
			nextFrame.setTitle("Option Frame");
			nextFrame.setVisible(true);
			this.dispose();
		}
		if (event.getSource() == createButton) {
			if (checkEmptyFields()) {
				JOptionPane.showMessageDialog(this, "Please fill in all fields");
			}
			else {
				startDate = Integer.parseInt(day1.getText());
				startMonth = Integer.parseInt(month1.getText());
				startYear = Integer.parseInt(year1.getText());
				endDate = Integer.parseInt(day2.getText());
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
					date1 = new Date(startYear, startMonth - 1, startDate);
					date2 = new Date(endYear, endMonth - 1, endDate);
					if (date1.before(database.today())) {
						JOptionPane.showMessageDialog(this, "Please select a future date.");
					}
					else if (date2.before(database.today())) {
						JOptionPane.showMessageDialog(this, "Please select a future date.");
					}
					else {
						DataMining miner = new DataMining(date1, date2);
						miner.getDriverList();
					}
				}
			}
		}
		if (event.getSource() == viewButton) {
			ViewFrame nextFrame = new ViewFrame(driverID);
			nextFrame.setTitle("View current roster");
			nextFrame.setVisible(true);
			this.dispose();
		}
	}
	
	public void writeRoster(ArrayList<TimeSlot> currentRoster) {
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter("Roster.txt"));
			output.write(date1.toString() + " - " + date2.toString() + "\n");
			for (int i = 0; i < currentRoster.size(); i ++) {
				output.write(currentRoster.get(i).toString() + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (day1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill all fields.");
			return true;
		}
		else if (day2.getText().isEmpty()) {
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
}
