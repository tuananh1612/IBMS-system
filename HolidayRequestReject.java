import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;


public class HolidayRequestReject extends JFrame implements ActionListener {

	private JPanel contentPane;
	private int ID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolidayRequestReject frame = new HolidayRequestReject(0);
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
	public HolidayRequestReject(int driverID) {
		ID = driverID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		JLabel answer = new JLabel();
		answer.setText("Your request has been rejeted");
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton backButton = new JButton("Back");
		backButton.setActionCommand("Back");
		backButton.addActionListener(this);
		backButton.setPreferredSize(new Dimension(100, 30));
		southPanel.add(backButton);
		centerPanel.add(answer);
		contentPane.add(BorderLayout.NORTH, centerPanel);
		contentPane.add(BorderLayout.SOUTH, southPanel);
		setContentPane(contentPane);
	}

	public void actionPerformed(ActionEvent event) {
		if ("Back".equals(event.getActionCommand())) {
			HolidayRequestDay nextFrame = new HolidayRequestDay(ID);
			nextFrame.setVisible(true);
			this.dispose();
		}
	}
}
