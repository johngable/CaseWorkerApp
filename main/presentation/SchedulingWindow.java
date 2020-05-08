package presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import model.CreateNewMonitoringByDateCommand;
import model.SetMonitoringStatusCommand;
import sharedData.MonitoringStatusEnum; 
import model.ModelFacade;

/**
 * Will build window for scheduling the next monitoring
 * after the current one is approved
 * 
 * @author tl9649
 *
 */
public class SchedulingWindow extends JFrame implements ActionListener {

	private JPanel mainPanel; //main window panel that holds all contents
	private JLabel windowLabel; //labels the window
	private JLabel nextDate;
	private JButton btnOK;
	private JButton btnCancel;
	private int monitoringID; // ID of last monitoring to create next monitoring
	private LocalDateTime date;
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private int clientID;
	private int caseWorkerID;
	private int supervisorID;

	/**
	 * Constructor called from the MonitoringSummaryWindow
	 * @param monitoringID
	 */
	public SchedulingWindow(int clientID, int caseWorkerID, int supervisorID) {
		date = LocalDateTime.now();
		this.clientID = clientID;
		this.caseWorkerID = caseWorkerID;
		this.supervisorID = supervisorID;
		calculateDate();
		initializeMainWindowPanel();
		createButtons();
		setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame
	 */
	public void initializeMainWindowPanel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setBounds(100, 100, 472, 299);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // close window without terminating app

		mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);

		windowLabel = new JLabel("Monitoring Scheduling");
		windowLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		windowLabel.setBounds(152, 11, 200, 30);
		mainPanel.add(windowLabel);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		nextDate = new JLabel("Next Monitoring will be " + date.toLocalDate().format(formatter));
		nextDate.setFont(new Font("Tahoma", Font.BOLD, 15));
		nextDate.setBounds(50, 111, 400, 30);
		mainPanel.add(nextDate);
	}
	
	/**
	 * Creates the buttons for the window
	 */
	private void createButtons() {
		btnOK = new JButton("OK");
		btnOK.setBounds(67, 200, 89, 23);
		btnOK.addActionListener(this);
		mainPanel.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(267, 200, 89, 23);
		btnCancel.addActionListener(this);
		mainPanel.add(btnCancel);
	}
	
	private void calculateDate() {
		date = date.plusDays(30);
	}
	
	/**
	 * Handles action events in the UI by checking the source
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnOK) {
			remote.queueCommand(new CreateNewMonitoringByDateCommand(date, clientID, caseWorkerID, supervisorID)); 
			dispose();
		}
		else if(e.getSource() == btnCancel) {
			dispose();
		}
	}


	/**
	 * Launch the application when the window is run stand alone
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SchedulingWindow window = new SchedulingWindow(1, 1, 1);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
