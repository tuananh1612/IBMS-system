import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class HolidayConfirm extends JFrame implements ActionListener {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolidayConfirm frame = new HolidayConfirm();
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
	public HolidayConfirm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		JLabel answer = new JLabel("Your request has been approved " 
				+ "for following days:");
		JTextArea dayList = new JTextArea(10, 30);
		JButton finishButton = new JButton("Finish");
		finishButton.setActionCommand("Finish");
		finishButton.addActionListener(this);
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		centerPanel.add(answer);
		centerPanel.add(dayList);
		southPanel.add(finishButton);
		contentPane.add(BorderLayout.NORTH, centerPanel);
		contentPane.add(BorderLayout.SOUTH, southPanel);
		setContentPane(contentPane);
	}

	public void actionPerformed(ActionEvent event) {
		if ("Finish".equals(event.getActionCommand())) {
			this.dispose();
			System.exit(0);
		}
	}
}
