import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class ViewFrame extends JFrame implements ActionListener {

	JPanel contentPanel;
	private JButton backButton, exitButton;
	private JTextArea table;
	private JTextField periodField;
	private JLabel periodLabel;
	private int driverID;
	ArrayList<String> createdRoster;
	String period;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewFrame frame = new ViewFrame(0);
					frame.setTitle("Report frame");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ViewFrame(int ID) {
		driverID = ID;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints contraints = new GridBagConstraints();
		JPanel northPanel = new JPanel();
		periodField = new JTextField();
		periodField.setText(period);
		periodField.setPreferredSize(new Dimension(150, 30));
		periodLabel = new JLabel("Current period:");
		periodLabel.setPreferredSize(new Dimension(110, 30));
		northPanel.add(periodLabel);
		northPanel.add(periodField);
		contraints.anchor = GridBagConstraints.NORTH;
		contraints.gridx = 0;
		contentPanel.add(northPanel, contraints);
		//Mid panel
		JPanel midPanel = new JPanel();
		table = new JTextArea(7, 30);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setEditable(false);
		for (int i = 0; i < createdRoster.size(); i ++) {
			table.append(createdRoster.get(i) + "\n");
		}
		contraints.anchor = GridBagConstraints.CENTER;
		contraints.gridx = 0;
		midPanel.add(scrollPane);
		contentPanel.add(midPanel, contraints);
		//South panel
		JPanel southPanel = new JPanel(new GridBagLayout());
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		backButton.setPreferredSize(new Dimension(80, 30));
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		exitButton.setPreferredSize(new Dimension(80, 30));
		southPanel.add(backButton);
		southPanel.add(exitButton);
		contraints.anchor = GridBagConstraints.SOUTH;
		contraints.gridx = 0;
		contentPanel.add(southPanel, contraints);
		setContentPane(contentPanel);
		pack();
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == backButton) {
			RosteringFrame nextFrame = new RosteringFrame(driverID);
			nextFrame.setTitle("Roster Frame");
			nextFrame.setVisible(true);
		}
		
		if (event.getSource() == exitButton) {
			System.exit(0);
		}
		this.dispose();
	}
	
	public void readRoster() {
		BufferedReader input;
		String line = "";
		try {
			input = new BufferedReader(new FileReader("Roster.txt"));
			period = input.readLine();
			while ((line = input.readLine()) != null) {
				createdRoster.add(line);
			}
		}
		catch (IOException exception) {
			exception.getMessage();
		}
	}
}
