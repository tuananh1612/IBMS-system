import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class HolidayRequestDay extends JFrame implements ActionListener {

	private JPanel contentPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel northPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel northPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label1 = new JLabel("Holidays taken:");
		JTextArea dayList = new JTextArea(7, 30);
		JLabel label2 = new JLabel("Remaining days:");
		JLabel remainDay = new JLabel();
		northPanel1.add(label2);
		northPanel1.add(remainDay);
		northPanel2.add(label1);
		northPanel2.add(dayList);
		northPanel.add(BorderLayout.NORTH, northPanel1);
		northPanel.add(BorderLayout.SOUTH, northPanel2);
		JPanel centerPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//JPanel centerPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label3 = new JLabel("Staring date:");
		JTextField date1 = new JTextField();
		date1.setPreferredSize(new Dimension(30, 30));
		JTextField month1 = new JTextField();
		month1.setPreferredSize(new Dimension(30, 30));
		JTextField year1 = new JTextField();
		year1.setPreferredSize(new Dimension(60, 30));
		JLabel label4 = new JLabel("Until:");
		JTextField date2 = new JTextField();
		date2.setPreferredSize(new Dimension(30, 30));
		JTextField month2 = new JTextField();
		month2.setPreferredSize(new Dimension(30, 30));
		JTextField year2 = new JTextField();
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
			this.dispose();
		}
		else if ("Cancel".equals(event.getActionCommand())) {
			System.exit(0);
		}
	}
}
