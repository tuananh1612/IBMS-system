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


public class HolidayIDReject extends JFrame implements ActionListener {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolidayIDReject frame = new HolidayIDReject();
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
	public HolidayIDReject() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		//JLabel answer = new JLabel("The entered ID does not exist.");
		JLabel answer = new JLabel();
		answer.setText("ID does not exist");
		JButton backButton = new JButton("Back");
		backButton.setActionCommand("Back");
		backButton.addActionListener(this);
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		backButton.setPreferredSize(new Dimension(100, 30));
		southPanel.add(backButton);
		centerPanel.add(answer);
		contentPane.add(BorderLayout.NORTH, centerPanel);
		contentPane.add(BorderLayout.SOUTH, southPanel);
		setContentPane(contentPane);
	}

	public void actionPerformed(ActionEvent event) {
		if ("Back".equals(event.getActionCommand())) {
			HolidayRequestID nextFrame = new HolidayRequestID();
			nextFrame.setVisible(true);
			this.dispose();
		}
	}
}