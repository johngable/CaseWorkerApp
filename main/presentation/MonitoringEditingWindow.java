package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.Client;
import model.EditClientAnswersCommand;

import model.ModelFacade;
import model.SetMonitoringStatusCommand;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;
import javax.swing.JTextArea;
import javax.swing.JSeparator;

/**
 * The window that shows clients and their monitorings
 * 
 * @Author Everyone
 *
 */
public class MonitoringEditingWindow extends JFrame implements State, ActionListener {

	private ModelFacade remote = ModelFacade.getSingleton(); // Command Queue

	private JPanel mainPanel; // main window panel
	// View Tab
	private JPanel viewTabPanel;
	// Edit Tab
	private JPanel editTabPanel;

	private int monitoringID;

	private JLabel dateLabel;

	private JLabel caseWorkerLabel;

	private JLabel currentClientLabel;

	private String caseWorkerName;

	private String clientName;

	private LocalDateTime visitDate;

	private int caseWorkerID;

	private Client client;

	private JButton btnSave;

	private JButton btnSubmit;

	private List<String> questions;

	private List<String> answers;

	private List<String> answersOriginal;

	private List<JTextField> answerEditList = new ArrayList<>();

	private List<JTextField> answerViewList = new ArrayList<>();

	private List<JTextArea> questionEditList = new ArrayList<>();

	private List<JTextArea> questionViewList = new ArrayList<>();

	private JButton btnViewNext;

	private JButton btnViewPrevious;

	private JButton btnEditNext;

	private JButton btnEditPrevious;

	private int pageCounter = 1; // initialize the display to page 1

	private JLabel lblEditPage;

	private JLabel lblViewPage;

	/**
	 * Constructor builds the window displayed by calling methods to build the
	 * window
	 * 
	 * @param answer22
	 * @param answer12
	 * @param question2
	 * @param question1
	 * @param caseWorkerName
	 */
	public MonitoringEditingWindow(MonitoringDTO monitoring, int caseWorkerID) {
		this.monitoringID = monitoring.getMonitoringID();
		// get questions from report
		this.questions = monitoring.getQuestions();
		// get questions from report
		this.answers = monitoring.getAnswers();
		answersOriginal = List.copyOf(this.answers);

		this.caseWorkerID = caseWorkerID;

		this.caseWorkerName = monitoring.getCaseWorker().getCaseWorkerName();
		this.clientName = monitoring.getClient().getClientName();
		this.visitDate = monitoring.getVisitDate();

		this.client = monitoring.getClient();

		initializeMainPanel(); // initializes preliminary settings for window display
		createTabbedPane(); // creates the pane which holds the tab panels
		createMonitoringLabels();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initializes preliminary settings for the Window
	 */
	private void initializeMainPanel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 900, 600);
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(238, 238, 238));
		mainPanel.setMaximumSize(new Dimension(32787, 32787));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
	}

	/**
	 * Places labels to display caseworkers name, client name and the visit date the
	 * monitoring happened
	 */
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
	 * Create a tabbed pane which holds the view and edit tabs
	 */
	private void createTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		tabbedPane.setBounds(256, 10, 620, 543);
		mainPanel.add(tabbedPane);

		createViewTab(tabbedPane);
		createEditTab(tabbedPane);
	}

	/**
	 * Create the tab to edit client info
	 * 
	 * @param tabbedPane the tabbed pane created by createTabbedPane
	 */
	private void createEditTab(JTabbedPane tabbedPane) {
		editTabPanel = new JPanel();
		editTabPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		tabbedPane.addTab("Edit", null, editTabPanel, null); // adds tab to main viewTabPanel
		editTabPanel.setLayout(null);

		setTabbedPaneFields(editTabPanel, answerEditList, questionEditList, answers);

		btnEditNext = new JButton("next");
		btnEditNext.addActionListener(this);
		btnEditNext.setBounds(494, 415, 99, 29);
		editTabPanel.add(btnEditNext);

		btnSave = new JButton("save");
		btnSave.addActionListener(this);
		btnSave.setBounds(395, 415, 99, 29);
		editTabPanel.add(btnSave);

		btnEditPrevious = new JButton("previous");
		btnEditPrevious.addActionListener(this);
		btnEditPrevious.setBounds(6, 415, 99, 29);
		editTabPanel.add(btnEditPrevious);

		btnSubmit = new JButton("submit");
		btnSubmit.addActionListener(this);
		btnSubmit.setBounds(455, 456, 138, 29);
		editTabPanel.add(btnSubmit);

		lblEditPage = new JLabel("Page " + pageCounter + " of 2");
		lblEditPage.setBounds(268, 6, 77, 16);
		editTabPanel.add(lblEditPage);
	}

	/**
	 * Create the tab to view client info
	 * 
	 * @param tabbedPane the tabbed pane created by createTabbedPane
	 */
	private void createViewTab(JTabbedPane tabbedPane) {
		viewTabPanel = new JPanel();
		viewTabPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		tabbedPane.addTab("View", null, viewTabPanel, null);
		viewTabPanel.setLayout(null);

		setTabbedPaneFields(viewTabPanel, answerViewList, questionViewList, answersOriginal);

		btnViewNext = new JButton("next");
		btnViewNext.addActionListener(this);
		btnViewNext.setBounds(494, 415, 99, 29);
		viewTabPanel.add(btnViewNext);

		btnViewPrevious = new JButton("previous");
		btnViewPrevious.addActionListener(this);
		btnViewPrevious.setBounds(6, 415, 99, 29);
		viewTabPanel.add(btnViewPrevious);

		lblViewPage = new JLabel("Page " + pageCounter + " of 2");
		lblViewPage.setBounds(268, 6, 77, 16);
		viewTabPanel.add(lblViewPage);
	}

	private void setTabbedPaneFields(JPanel panel, List<JTextField> answerList, List<JTextArea> questionList,
			List<String> answerToDisplay) {
		questionList.clear();
		answerList.clear();
		int j = 0;
		if (pageCounter == 2) {
			j = 4;
		}
		for (int i = 0; i < 4; i++) {
			questionList.add(i + j, new JTextArea(questions.get(i + j)));
			questionList.get(i + j).setLineWrap(true);
			questionList.get(i + j).setEditable(false);
			questionList.get(i + j).setEnabled(true);
			questionList.get(i + j).setWrapStyleWord(true);
			questionList.get(i + j).setBounds(36, 28 + 90 * i, 524, 40);
			questionList.get(i + j).setPreferredSize(new Dimension(530, 30));
			questionList.get(i + j).setBackground(new Color(238, 238, 238));
			panel.add(questionList.get(i + j));

			answerList.add(i + j, new JTextField(answerToDisplay.get(i + j)));
			if (panel == editTabPanel) {
				answerList.get(i + j).setEditable(true);
			} else {
				answerList.get(i + j).setEditable(false);
			}
			answerList.get(i + j).setEnabled(true);
			answerList.get(i + j).setBounds(36, 70 + 90 * i, 524, 30);
			answerList.get(i + j).setPreferredSize(new Dimension(530, 30));
			answerList.get(i + j).setBackground(new Color(238, 238, 238));
			panel.add(answerList.get(i + j));
		}
	}

	/**
	 * Method to set frame visible (needed for state machine)
	 * 
	 * @param b true if this screen should be visible
	 */
	public void setWindowVisible(boolean b) {
		this.setVisible(b);
	}

	/**
	 * Set the Launcher to this window
	 * 
	 * @param machine the Launcher controlling the windows
	 */
	public void doAction(Launcher machine) {
		machine.setState(this);
	}

	/**
	 * Handles button press events for the buttons
	 * 
	 * @param e event that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			List<String> newAnswers = new ArrayList<>();
			for (JTextField answerField : answerEditList) {
				newAnswers.add(answerField.getText());
			}

			newAnswers.add(answers.get(4));
			newAnswers.add(answers.get(5));
			newAnswers.add(answers.get(6));
			newAnswers.add(answers.get(7));

			remote.queueCommand(new EditClientAnswersCommand(monitoringID, client, newAnswers, caseWorkerID));
			remote.queueCommand(
					new SetMonitoringStatusCommand(monitoringID, client, MonitoringStatusEnum.PENDING_FOR_REVIEW));
			dispose();
		}

		if (e.getSource() == btnSave) {
			int j = 0;
			if (pageCounter == 2) {
				j = 4;
			}
			for (JTextField answerField : answerEditList) {
				answers.set(j, answerField.getText());
				j++;
			}
		}

		// move to next group of questions on edit tab
		if (e.getSource() == btnEditNext) {
			if (pageCounter == 1) {
				pageCounter = 2;
				int j = 4;
				for (int i = 0; i < 4; i++) {
					questionEditList.get(i).setText(questions.get(i + j));
					answerEditList.get(i).setText(answers.get(i + j));
				}
			}
			lblEditPage.setText("Page " + pageCounter + " of 2");
		}

		// move to previous group of questions on edit tab
		if (e.getSource() == btnEditPrevious) {
			if (pageCounter == 2) {
				pageCounter = 1;
				for (int i = 0; i < 4; i++) {
					questionEditList.get(i).setText(questions.get(i));
					answerEditList.get(i).setText(answers.get(i));
				}
			}
			lblEditPage.setText("Page " + pageCounter + " of 2");
		}

		// move to next group of questions on view tab
		if (e.getSource() == btnViewNext) {
			if (pageCounter == 1) {
				pageCounter = 2;
				int j = 4;
				for (int i = 0; i < 4; i++) {
					questionViewList.get(i).setText(questions.get(i + j));
					answerViewList.get(i).setText(answersOriginal.get(i + j));
				}
			}
			lblViewPage.setText("Page " + pageCounter + " of 2");
		}

		// move to previous group of questions on view tab
		if (e.getSource() == btnViewPrevious) {
			if (pageCounter == 2) {
				pageCounter = 1;
				for (int i = 0; i < 4; i++) {
					questionViewList.get(i).setText(questions.get(i));
					answerViewList.get(i).setText(answersOriginal.get(i));
				}
			}
			lblViewPage.setText("Page " + pageCounter + " of 2");
		}
	}
}
