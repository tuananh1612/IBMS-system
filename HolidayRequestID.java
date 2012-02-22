import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class HolidayRequestID extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField IDField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolidayRequestID frame = new HolidayRequestID();
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
	public HolidayRequestID() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		JLabel IDLabel = new JLabel("ID: ");
		IDField = new JTextField();
		IDField.setPreferredSize(new Dimension(200, 30));
		JButton OKButton = new JButton("OK");
		JButton CancelButton = new JButton("Cancel");
		OKButton.setActionCommand("OK");
		CancelButton.setActionCommand("Cancel");
		OKButton.addActionListener(this);
		CancelButton.addActionListener(this);
		OKButton.setPreferredSize(new Dimension(100, 30));
		CancelButton.setPreferredSize(new Dimension(100, 30));
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		northPanel.add(IDLabel);
		northPanel.add(IDField);
		contentPane.add(BorderLayout.NORTH, northPanel);
		southPanel.add(OKButton);
		southPanel.add(CancelButton);
		contentPane.add(BorderLayout.SOUTH, southPanel);
		setContentPane(contentPane);
	}

	public void actionPerformed(ActionEvent event) {
		if ("OK".equals(event.getActionCommand())) {
			String ID = IDField.getText();
			//Connect to database
			database.openBusDatabase();
			int driverID = DriverInfo.findDriver(ID);
			if (driverID == 0) {
				HolidayIDReject rejectFrame = new HolidayIDReject();
				rejectFrame.setVisible(true);
			}
			else {
				HolidayRequestDay nextFrame = new HolidayRequestDay(driverID);
				nextFrame.setVisible(true);
			}
			this.dispose();
		}
		else if ("Cancel".equals(event.getActionCommand())) {
			System.exit(0);
		}
	}
}
