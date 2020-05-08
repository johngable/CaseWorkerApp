package presentation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.UIManager;

import model.Client;
import model.ModelFacade;
import model.SetMonitoringStatusCommand;
import sharedData.MonitoringStatusEnum;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JRadioButton;

import sharedData.MonitoringDTO;

/**
 * Window that opens with monitoring is selected from the table the supervisor
 * window. Allows supervisor to see the answers and is able to approve or reject
 * the report
 * 
 * @author michaelpermyashkin, tl9649
 *
 */
public class MonitoringSummaryWindow extends JFrame implements ActionListener {

	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue

	private JPanel mainPanel; // main window panel that holds all contents
	private JLabel windowLabel; // labels the window

	private JTextField answer1Field; // holds answer 1 in a text field
	private JTextField answer2Field; // holds answer 1 in a text field

	private int monitoringID; // ID of monitoring being reviewed
	private LocalDateTime visitDate;
	private int clientID;
	private int caseWorkerID;
	private int supervisorID;
	
	private JLabel question1Label;
	private JLabel question2Label;
	private JLabel caseWorkerLabel;
	private JLabel currentClientLabel;
	private JLabel dateLabel;
	// values passed in from the current report
	private String question1;
	private String question2;
	private List<String> questions;
	private String answer1;
	private String answer2;
	private List<String> answers;
	private String caseWorkerName;
	private String clientName;

	private ButtonGroup group; // button group for toggle radio buttons
	private JButton btnSubmit;
	private JRadioButton toggleApprove;
	private JRadioButton toggleReject;

	private Client client;
	

	/**
	 * Constructor called from the super visor window and takes in the answers from
	 * monitoring selected.
	 */
	public MonitoringSummaryWindow(MonitoringDTO monitoring, int supervisorID) {
		this.supervisorID = supervisorID;
		this.monitoringID = monitoring.getMonitoringID();
		this.questions = monitoring.getQuestions();
		this.question1 = questions.get(0);
		this.question2 = questions.get(1);
		this.answers = monitoring.getAnswers();
		this.answer1 = answers.get(0);
		this.answer2 = answers.get(1);
		this.client = monitoring.getClient();
		this.caseWorkerID = monitoring.getCaseWorker().getCaseWorkerID();
		this.caseWorkerName = monitoring.getCaseWorker().getCaseWorkerName();
		this.clientName = monitoring.getClient().getClientName();
		this.visitDate = monitoring.getVisitDate();
		initializeMainWindowPanel();
		createAnswerFields();
		createMonitoringLabels();
		createButtons();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeMainWindowPanel() {
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

		windowLabel = new JLabel("Monitoring Summary");
		windowLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		windowLabel.setBounds(152, 11, 162, 30);
		mainPanel.add(windowLabel);
	}
	
	private void createMonitoringLabels() {
		caseWorkerLabel = new JLabel("CaseWorker: " + caseWorkerName);
		caseWorkerLabel.setBounds(42, 35, 373, 20);
		mainPanel.add(caseWorkerLabel);
		currentClientLabel = new JLabel("Client: " + clientName);
		currentClientLabel.setBounds(42, 50, 373, 20);
		mainPanel.add(currentClientLabel);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		if (visitDate != null) {
			dateLabel = new JLabel("Visit Date: " + visitDate.toLocalDate().format(formatter));
		} else {
			dateLabel = new JLabel("Visit Date: N/A"); 
		}
		dateLabel.setBounds(42, 65, 373, 20);
		mainPanel.add(dateLabel);
	}

	/**
	 * Creates fields that hold the answer values
	 */
	private void createAnswerFields() {
		/* Answer 1 */
		question1Label = new JLabel(question1);
		question1Label.setBounds(42, 92, 373, 20);
		mainPanel.add(question1Label);

		answer1Field = new JTextField();
		answer1Field.setText(answer1);
		answer1Field.setEditable(false);
		answer1Field.setBounds(42, 112, 373, 30);
		mainPanel.add(answer1Field);
		answer1Field.setColumns(10);

		/* Answer 2 */
		question2Label = new JLabel(question2);
		question2Label.setBounds(42, 154, 373, 20);
		mainPanel.add(question2Label);

		answer2Field = new JTextField();
		answer2Field.setText(answer2);
		answer2Field.setEditable(false);
		answer2Field.setColumns(10);
		answer2Field.setBounds(42, 174, 373, 30);
		mainPanel.add(answer2Field);
	}

	/**
	 * Build buttons to determine if report is approved or rejected and a submit
	 * button to complete review
	 */
	private void createButtons() {
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(267, 230, 89, 23);
		btnSubmit.addActionListener(this);
		mainPanel.add(btnSubmit);

		toggleApprove = new JRadioButton("Approve");
		toggleApprove.setForeground(new Color(0, 128, 0));
		toggleApprove.setBounds(110, 216, 141, 23);
		toggleApprove.setActionCommand("approve");
		mainPanel.add(toggleApprove);

		toggleReject = new JRadioButton("Reject");
		toggleReject.setForeground(new Color(220, 20, 60));
		toggleReject.setBounds(110, 251, 141, 23);
		toggleReject.setActionCommand("reject");
		mainPanel.add(toggleReject);

		// Button group allows only one option to be selected
		group = new ButtonGroup();
		group.add(toggleApprove);
		group.add(toggleReject);
	}

	/**
	 * Handles action events in the UI by checking the source
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Supervisor selected status and wants to change the status of monitoring
		if (e.getSource() == btnSubmit) {
			// checks that one of the options is selected on click
			if (group.getSelection() != null) {
				// supervisor approved monitoring
				if (group.getSelection().getActionCommand() == "approve") {
					remote.queueCommand(new SetMonitoringStatusCommand(monitoringID, client, MonitoringStatusEnum.APPROVED));
					SchedulingWindow schedule = new SchedulingWindow(clientID, caseWorkerID, supervisorID);
				}
				// supervisor rejected monitoring
				if (group.getSelection().getActionCommand() == "reject") {
					remote.queueCommand(new SetMonitoringStatusCommand(monitoringID, client, MonitoringStatusEnum.PENDING_FOR_CORRECTION));
				}
			}
			dispose();
		}

	}

	/**
	 * Launch the application when the window is run stand alone
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MonitoringSummaryWindow window = new MonitoringSummaryWindow(1, "Question 1", "Question 2",
//							"Hard coded answer 1", "Hard coded answer 2", 1, 1);
//					window.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
}
