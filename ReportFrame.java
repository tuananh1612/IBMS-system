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
import javax.swing.JComboBox;

public class ReportFrame extends JFrame implements ActionListener {

	JPanel contentPanel;
	private JComboBox optionList;
	private JButton OKButton;
	private JButton backButton;
	private JTextField day1, day2, month1, month2, year1, year2, IDField;
	String[] option = {"Ill driver", "Broken bus"};
	private JLabel startLabel, endLabel, IDLabel;
	private int driverID;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReportFrame frame = new ReportFrame(0);
					frame.setTitle("Report frame");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ReportFrame(int ID) {
		driverID = ID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(3, 1, 5, 5));
		JPanel northPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel subNorth1 = new JPanel();
		optionList = new JComboBox(option);
		optionList.addActionListener(this);
		optionList.setPreferredSize(new Dimension(160, 30));
		subNorth1.add(optionList);
		northPanel.add(subNorth1);
		JPanel subNorth2 = new JPanel(new GridBagLayout());
		IDLabel = new JLabel("ID:");
		IDLabel.setPreferredSize(new Dimension(50, 30));
		IDField = new JTextField("");
		IDField.setPreferredSize(new Dimension(110, 30));
		subNorth2.add(IDLabel);
		subNorth2.add(IDField);
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
		backButton.setPreferredSize(new Dimension(70, 30));
		OKButton = new JButton("Continue");
		OKButton.addActionListener(this);
		OKButton.setPreferredSize(new Dimension(90, 30));
		southPanel.add(backButton);
		southPanel.add(OKButton);
		contentPanel.add(midPanel);
		contentPanel.add(southPanel);
		setContentPane(contentPanel);
		pack();
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == backButton) {
			OptionFrame nextFrame = new OptionFrame(driverID);
			nextFrame.setTitle("Options frame");
			nextFrame.setVisible(true);
		}
		if (event.getSource() == OKButton) {
			
		}
		this.dispose();
	}
}
