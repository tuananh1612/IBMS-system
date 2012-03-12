import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
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
		}
		if (event.getSource() == createButton) {
			
		}
		if (event.getSource() == viewButton) {
			
		}
		this.dispose();
	}
}
