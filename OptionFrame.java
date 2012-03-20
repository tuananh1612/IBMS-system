import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionFrame extends JFrame implements ActionListener {

	private static int driverID;
	private JPanel content;
	private JButton holidayRequest;
	private JButton rostering;
	private JButton report;
	private JButton cancelButton;
	private JButton logoutButton;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionFrame frame = new OptionFrame(0);
					frame.setTitle("Options frame");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public OptionFrame(int ID) {
		driverID = ID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = new JPanel();
		content.setLayout(new GridLayout(2, 1, 5, 5));
		JPanel northPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		holidayRequest = new JButton("Request Holiday");
		holidayRequest.addActionListener(this);
		rostering = new JButton("Create roster");
		rostering.addActionListener(this);
		report = new JButton("Report");
		report.addActionListener(this);
		northPanel.add(holidayRequest);
		northPanel.add(rostering);
		northPanel.add(report);
		content.add(northPanel);
		JPanel southPanel = new JPanel(new GridBagLayout());
		cancelButton = new JButton("Exit");
		cancelButton.addActionListener(this);
		cancelButton.setPreferredSize(new Dimension(90, 30));
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(this);
		logoutButton.setPreferredSize(new Dimension(90, 30));
		southPanel.add(cancelButton);
		southPanel.add(logoutButton);
		content.add(southPanel);
		/*
		content.add(holidayRequest);
		content.add(rostering);
		content.add(viewRoster);
		content.add(report);
		content.add(OKButton);
		content.add(cancelButton);
		content.add(logoutButton);
		*/
		setContentPane(content);
		pack();
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == holidayRequest) {
			HolidayRequestID nextFrame = new HolidayRequestID();
			nextFrame.setTitle("ID request");
			nextFrame.setVisible(true);
		}
		if (event.getSource() == rostering) {
			RosteringFrame nextFrame = new RosteringFrame(driverID);
			nextFrame.setTitle("Create roster");
			nextFrame.setVisible(true);
		}
		if (event.getSource() == report) {
			ReportFrame nextFrame = new ReportFrame(driverID);
			nextFrame.setTitle("Report");
			nextFrame.setVisible(true);
		}
		if (event.getSource() == cancelButton) {
			System.exit(0);
		}
		if (event.getSource() == logoutButton) {
			HolidayRequestID nextFrame = new HolidayRequestID();
			nextFrame.setTitle("Request ID");
			nextFrame.setVisible(true);
		}
		this.dispose();
	}
}
