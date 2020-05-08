package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import model.CaseWorker;
import model.Client;
import model.GetMonitoringsBySupervisorCommand;
import model.ModelFacade;
import reports.ClientMonitoringStatusReport;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Builds the window display for Supervisors
 * 
 * @author Everyone
 *
 */
public class SupervisorWindow extends AbstractUserWindow implements State, ReportObserver, ActionListener {

	private JButton btnSelectCaseWorker; // button to select highlighted caseworker

	private JComboBox<CaseWorker> caseWorkerDropDown;
	private DefaultComboBoxModel<CaseWorker> caseWorkerModel; // model to populate caseworker drop down

	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Singleton Report Observer Connector

	private CaseWorker selectedCaseWorker; // object of caseworker selected in combo box

	private String selectedCaseWorkerName; // highlighted caseworker in drop down menu


	private JLabel lblDateFrom;

	private JLabel lblDateTo;

	/**
	 * Constructor calls methods to register as report observer and to build the
	 * window displayed to supervisors
	 */
	public SupervisorWindow(int supervisorID, String supervisorName) {
		super();
		supervisorAuthenticated = true; // tells abstract class SUP logged in
		// columns shown in the table
		monitorModel.addColumn("ID");
		monitorModel.addColumn("VisitDate");
		monitorModel.addColumn("DueDate");
		monitorModel.addColumn("Client");
		monitorModel.addColumn("Caseworker");
		monitorModel.addColumn("Status");
		monitoringsTable.setModel(monitorModel);
		monitoringsTable.getColumnModel().getColumn(0).setMaxWidth(120);
		monitoringsTable.getColumnModel().getColumn(1).setMaxWidth(200);
		monitoringsTable.getColumnModel().getColumn(2).setMaxWidth(200);
		monitoringsTable.getColumnModel().getColumn(5).setMinWidth(150);
		monitoringsTable.getColumnModel().getColumn(5).setMaxWidth(300);

		this.supervisorID = supervisorID;
		registerObservers(); // registers window to observe all relevant reports
		setBounds(200, 200, 1100, 625);
		remote.queueCommand(new GetMonitoringsBySupervisorCommand(supervisorID));
	}

	/**
	 * This method receives all events that occur in the window. The source of each
	 * event is determined and handles accordingly
	 * 
	 * @param e event that occurred in window
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		// Case worker selected from menu - filters table accordingly
		if (e.getSource() == btnSelectCaseWorker && caseWorkerDropDown.getSelectedIndex() != 0) {
			// first option is the default text
			selectedCaseWorker = (CaseWorker) caseWorkerModel.getSelectedItem();
			selectedCaseWorkerName = selectedCaseWorker.getCaseWorkerName();
			// Updates filters with current selected names
			RowFilter<DefaultTableModel, Object> filterCaseWorker = RowFilter.regexFilter(selectedCaseWorkerName,
					monitorModel.findColumn("Caseworker"));
			rowSorter.setRowFilter(filterCaseWorker);

			clientModel.removeAllElements();
			Client dummyCLabel = new Client("Filter by client...", -1);
			clientModel.addElement(dummyCLabel);
			List<Client> cList = new ArrayList<Client>();
			for (MonitoringDTO m : monitoringsList) {
				Client client = m.getClient();
				CaseWorker caseWorker = m.getCaseWorker();
				if (selectedCaseWorker.equals(caseWorker)) {
					if (!cList.contains(client)) {
						cList.add(client);
					}
				}
			}
			for (Client c : cList) {
				clientModel.addElement(c);
			}

		}
		// Client selected from menu
		if (e.getSource() == btnSelectAndReviewMontoring && monitoringsTable.getSelectedRow() != -1) {
			int selectedRow = monitoringsTable.getSelectedRow(); // selected row in table

			int monitoringIndex = monitorModel.findColumn("ID");
			int selectedMonitoringID = (int) monitoringsTable.getValueAt(selectedRow, monitoringIndex);

			for (MonitoringDTO monitoring : monitoringsList) {
				if (monitoring.getMonitoringID() == selectedMonitoringID) {
					MonitoringSummaryWindow monitoringSummaryWindow = new MonitoringSummaryWindow(monitoring,
							supervisorID);
				}
			}

		}
		// Button to clear the filters for monitorings table
		if (e.getSource() == btnClearFilters) {
			caseWorkerModel.setSelectedItem(caseWorkerModel.getElementAt(0));
		}
		
	
	}

	/**
	 * Creates the drop down menu containing a list of caseworkers who report to the
	 * current supervisor who is authenticated
	 */
	private void createCaseWorkerMenu() {
		caseWorkerDropDown = new JComboBox<CaseWorker>();
		caseWorkerModel = new DefaultComboBoxModel<CaseWorker>();
		caseWorkerDropDown.setModel(caseWorkerModel);
		// override to populate the JComboBox with a CaseWorker Object
		caseWorkerDropDown.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof CaseWorker) {
					CaseWorker caseWorker = (CaseWorker) value;
					setText(caseWorker.getCaseWorkerName()); // displays only the name
				}
				return this;
			}
		});
		caseWorkerDropDown.setBounds(10, 55, 236, 28);
		mainPanel.add(caseWorkerDropDown);

		btnSelectCaseWorker = new JButton("Select Caseworker");
		btnSelectCaseWorker.setBounds(10, 90, 150, 28);
		mainPanel.add(btnSelectCaseWorker);
		btnSelectCaseWorker.addActionListener(this);
	}

	/**
	 * Creates the drop down menu containing a list of clients after a caseworker is
	 * selected
	 */
	protected void createClientMenu() {
		super.createClientMenu();
		clientDropDown.setBounds(10, 145, 236, 28);
		btnSelectClient.setBounds(10, 185, 121, 28);
	}

	/**
	 * Creates panel to holds menus and calls methods to build the menus
	 */
	protected void createMenuPanel() {
		JLabel lblFilterMonitorings = new JLabel("Filter");
		lblFilterMonitorings.setHorizontalAlignment(SwingConstants.CENTER);
		lblFilterMonitorings.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblFilterMonitorings.setBounds(43, 10, 166, 29);
		mainPanel.add(lblFilterMonitorings);

		JLabel lblAdvancedSearch = new JLabel("Advanced Search");
		lblAdvancedSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdvancedSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblAdvancedSearch.setBounds(43, 290, 166, 29);
		mainPanel.add(lblAdvancedSearch);

		lblDateFrom = new JLabel("From:");
		lblDateFrom.setHorizontalAlignment(SwingConstants.LEFT);
		lblDateFrom.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblDateFrom.setBounds(10, 400, 50, 29);
		mainPanel.add(lblDateFrom);

		lblDateTo = new JLabel("To:");
		lblDateTo.setHorizontalAlignment(SwingConstants.LEFT);
		lblDateTo.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblDateTo.setBounds(125, 400, 50, 29);
		mainPanel.add(lblDateTo);

		btnClearFilters = new JButton("Clear Filters");
		Color DARK_GREY = new Color(50, 50, 50);
		btnClearFilters.setBackground(DARK_GREY);
		btnClearFilters.setForeground(Color.WHITE);
		btnClearFilters.addActionListener(this);
		btnClearFilters.setBounds(12, 235, 236, 28);
		mainPanel.add(btnClearFilters);

		createCaseWorkerMenu(); // menu which holds list of caseworkers
		createClientMenu(); // menu which holds list of clients

		// Searching
		clientSearchDropDown.setBounds(10, 310, 236, 28);
		statusSearchDropDown.setBounds(10, 340, 236, 28);
		dateSearchDropDown.setBounds(10, 370, 236, 28);
		btnSearch.setBounds(10, 475, 236, 28);

		// sets the bounds and default date for the datepickers
		datePickerFrom.setBounds(10, 420, 100, 35);
		datePickerTo.setBounds(125, 420, 100, 35);


		mainPanel.add(datePickerFrom);
		mainPanel.add(datePickerTo);

		btnResetSearch.setBackground(DARK_GREY);
		btnResetSearch.setForeground(Color.WHITE);
		btnResetSearch.addActionListener(this);
		btnResetSearch.setBounds(10, 510, 236, 28);
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
	 * Populates drop down of client names for filtering
	 * 
	 * @param monitoringsList2 - Monitorings to be displayed
	 */
	private void populateCaseWorkerDropDown(List<MonitoringDTO> monitoringsList) {
		caseWorkerModel.removeAllElements();
		CaseWorker dummyCWLabel = new CaseWorker("Filter by caseworker...", -1);
		caseWorkerModel.addElement(dummyCWLabel);

		List<CaseWorker> cwList = new ArrayList<CaseWorker>();
		for (MonitoringDTO m : monitoringsList) {
			CaseWorker c = m.getCaseWorker();
			if (cwList.size() == 0) {
				cwList.add(c);
			}
			for (int i = 0; i < cwList.size(); i++) {
				if (c.getCaseWorkerID() == cwList.get(i).getCaseWorkerID()) {
					break;
				}
				if (i == cwList.size() - 1) {
					cwList.add(c);
				}
			}
		}
		for (CaseWorker cw : cwList) {
			caseWorkerModel.addElement(cw);
		}
	}

	/**
	 * Method that receives reports that the supervisor window registered to observe
	 * 
	 * @param report - generic report object is passed in and type casted if the
	 *               report class is a ClientAnswerReport
	 */
	@Override
	public void receiveReport(Report report) {
		super.receiveReport(report);
		// MonitoringListReport contains a list of all monitorings
		if (report.getClass() == MonitoringListReport.class) {
			MonitoringListReport rep = (MonitoringListReport) report;
			List<MonitoringDTO> supMonitoringsList = rep.getMonitorings();
			populateCaseWorkerDropDown(supMonitoringsList);
		}
	}

	/**
	 * Registers to observe relevant reports
	 */
	private void registerObservers() {
		roc.registerObserver(this, MonitoringListReport.class);
		roc.registerObserver(this, ClientMonitoringStatusReport.class);
	}

	/**
	 * Method to set frame visible (needed for state machine)
	 * 
	 * @param boolean b from state machine
	 */
	public void setWindowVisible(boolean b) {
		this.setVisible(b);
	}
}
