package presentation;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Client;
import model.DownloadMonitoringsByIDCommand;
import model.GetClientsByCaseWorkerCommand;
import model.GetMonitoringsByCaseWorkerCommand;
import model.ModelFacade;
import model.UploadLocalMonitoringCommand;
import reports.ClientAnswerReport;
import reports.ClientListReport;
import reports.ClientMonitoringStatusReport;
import reports.DownloadedClientListReport;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import reports.VersionCheckFailedReport;
import sharedData.MonitoringDTO;

public class CaseWorkerMonitoringsWindow extends AbstractUserWindow implements State, ReportObserver, ActionListener {

	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue

	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Singleton Report Observer Connector

	private JLabel lblFilterMonitorings;

	private JLabel lblSearchMonitorings;

	private JButton btnScheduleMonitoring;

	private JTable downloadTable;

	private JButton btnDownload;

	private JButton btnUpload;

	private List<Client> downloadedClientList = new ArrayList<>();

	private List<Client> locallyDownloadedClientList = new ArrayList<>();

	private JLabel lblDateFrom;

	private JLabel lblDateTo;

	/**
	 * Constructor calls methods to register as report observer and to build the
	 * window displayed to supervisors
	 * 
	 * @param caseWorkerName
	 * @param caseWorkerID
	 */
	public CaseWorkerMonitoringsWindow(int caseWorkerID, String caseWorkerName) {
		super();
		caseWorkerAuthenticated = true; // tells abstract class CW logged in
		// columns shown in the table
		monitorModel.addColumn(""); // extra column for checkboxes
		monitorModel.addColumn("ID");
		monitorModel.addColumn("VisitDate");
		monitorModel.addColumn("DueDate");
		monitorModel.addColumn("Client");
		monitorModel.addColumn("Caseworker");
		monitorModel.addColumn("Status");
		monitoringsTable.setModel(monitorModel);

		TableColumn tc = monitoringsTable.getColumnModel().getColumn(0);
		monitoringsTable.getColumnModel().getColumn(0).setMaxWidth(20);
		monitoringsTable.getColumnModel().getColumn(1).setMaxWidth(120);
		monitoringsTable.getColumnModel().getColumn(2).setMaxWidth(200);
		monitoringsTable.getColumnModel().getColumn(3).setMaxWidth(200);
		monitoringsTable.getColumnModel().getColumn(6).setMinWidth(150);
		monitoringsTable.getColumnModel().getColumn(6).setMaxWidth(300);

		// Will set the boolean column to render as a checkbox
		tc.setCellRenderer(monitoringsTable.getDefaultRenderer(Boolean.class));
		tc.setCellEditor(monitoringsTable.getDefaultEditor(Boolean.class));

		registerObservers(); // registers window to observe all relevant reports
		remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
		// remote.queueCommand(new GetClientsByCaseWorkerCommand(caseWorkerID));
		this.caseWorkerID = caseWorkerID;
	}

	/**
	 * 
	 * 
	 * /** This method receives all events that occur in the window. The source of
	 * each event is determined and handles accordingly
	 * 
	 * @param e event that occurred in window
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		// Client selected from menu
		if (e.getSource() == btnSelectAndReviewMontoring && monitoringsTable.getSelectedRow() != -1) {
			int selectedRow = monitoringsTable.getSelectedRow(); // selected row in table
			int monitoringIndex = monitorModel.findColumn("ID"); // get index of ID column
			String selectedMonitoringID = monitoringsTable.getValueAt(selectedRow, monitoringIndex).toString();
			selectedMonitoringID = selectedMonitoringID.replace("**", "");
			selectedMonitoringID = selectedMonitoringID.replace(" (local)", "");
			int selectedID = Integer.parseInt(selectedMonitoringID);

			// iterate through monitorings list and find the correct ID
			for (MonitoringDTO monitoring : monitoringsList) {
				if (monitoring.getMonitoringID() == selectedID) {
					/** CHECK IF CASEWORKER IN MONITORING HAS THE ** (AKA IS LOCALLY DOWNLOADED) */
					Client clientToEdit = monitoring.getClient();
					// extra confirmation needed
					boolean found = false;
					for (Client c : downloadedClientList) {
						if (c.getClientID() == clientToEdit.getClientID()) {
							found = true;
						}
					}
					if (found) {
						ConfirmEditPopup window = new ConfirmEditPopup(monitoring, caseWorkerID);
					} else {
						MonitoringEditingWindow caseWorkerWindow = new MonitoringEditingWindow(monitoring,
								caseWorkerID);
					}
				}
			}
		}
		// schedule monitoring
		if (e.getSource() == btnScheduleMonitoring) {
			ScheduleNewMonitoringWindow scheduleMonitoringWindow = new ScheduleNewMonitoringWindow(caseWorkerID, cList);
		}
		// download
		if (e.getSource() == btnDownload) {
			List<Integer> monitoringsToDownload = getSelectedMonitoringIDsInTable();
			if (monitoringsToDownload.size() != 0)
				remote.queueCommand(new DownloadMonitoringsByIDCommand(monitoringsToDownload, caseWorkerID));
			resetMonitoringSelectionsToDefault();
		}

		// upload
		if (e.getSource() == btnUpload) {
			boolean noEditsToUpload = true;
			List<Integer> monitoringsIDsToUpload = getSelectedMonitoringIDsInTable();
			for (MonitoringDTO m : monitoringsList) {
				if (monitoringsIDsToUpload.contains(m.getMonitoringID())) {
					if (m.getIsLocallyEdited()) {
						noEditsToUpload = false;
						remote.queueCommand(new UploadLocalMonitoringCommand(m, true));
					}
				}
			}

			if (noEditsToUpload) {
				UploadEmptyPopupWindow emptyUpload = new UploadEmptyPopupWindow();
			} else {
				remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
			}
			resetMonitoringSelectionsToDefault();

		}
	}

	/**
	 * Creates the drop down menu containing a list of clients after a caseworker is
	 * selected
	 */
	protected void createClientMenu() {
		super.createClientMenu();
		// Filtering
		clientDropDown.setBounds(10, 70, 236, 28);
		btnSelectClient.setBounds(10, 110, 150, 28);
	}

	/**
	 * Returns all selected monitoring ID's in monitoring table
	 * 
	 * @return List<Integer> containing all selected monitoring ID's
	 */
	protected List<Integer> getSelectedMonitoringIDsInTable() {
		List<Integer> selectedMonitoringIDs = new ArrayList<>();
		for (int i = 0; i < monitoringsTable.getRowCount(); i++) {
			// gets all rows that have been checked for download
			if ((Boolean) monitoringsTable.getValueAt(i, 0)) {
				int monitoringIDIndex = monitorModel.findColumn("ID");
				String selectedMonitoringID = monitoringsTable.getValueAt(i, monitoringIDIndex).toString();
				selectedMonitoringID = selectedMonitoringID.replace("**", "");
				selectedMonitoringID = selectedMonitoringID.replace(" (local)", "");
				int selectedID = Integer.parseInt(selectedMonitoringID);
				selectedMonitoringIDs.add(selectedID);
			}
		}
		return selectedMonitoringIDs;
	}

	/**
	 * Removes checkmark selection from each row
	 */
	protected void resetMonitoringSelectionsToDefault() {
		for (int i = 0; i < monitoringsTable.getRowCount(); i++) {
			if ((Boolean) monitoringsTable.getValueAt(i, 0)) {
				monitoringsTable.setValueAt(Boolean.FALSE, i, 0);
			}
		}
	}

	/**
	 * Creates panel to holds menus and calls methods to build the menus
	 */
	protected void createMenuPanel() {
		lblFilterMonitorings = new JLabel("Filter");
		lblFilterMonitorings.setHorizontalAlignment(SwingConstants.CENTER);
		lblFilterMonitorings.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblFilterMonitorings.setBounds(43, 6, 166, 29);
		mainPanel.add(lblFilterMonitorings);

		lblSearchMonitorings = new JLabel("Advanced Search");
		lblSearchMonitorings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchMonitorings.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblSearchMonitorings.setBounds(43, 250, 166, 29);
		mainPanel.add(lblSearchMonitorings);

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
		btnClearFilters.setBounds(12, 180, 236, 28);
		mainPanel.add(btnClearFilters);

		// filtering
		createClientMenu(); // menu which holds list of clients

		// Searching
		clientSearchDropDown.setBounds(10, 290, 236, 28);
		statusSearchDropDown.setBounds(10, 320, 236, 28);
		dateSearchDropDown.setBounds(10, 350, 236, 28);
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

		btnScheduleMonitoring = new JButton("New Monitoring");
		btnScheduleMonitoring.setBounds(10, 545, 236, 28);
		btnScheduleMonitoring.addActionListener(this);
		mainPanel.add(btnScheduleMonitoring);

		btnDownload = new JButton("Download");
		btnDownload.addActionListener(this);
		btnDownload.setBounds(270, 525, 100, 30);
		mainPanel.add(btnDownload);

		btnUpload = new JButton("Upload");
		btnUpload.addActionListener(this);
		btnUpload.setBounds(377, 525, 100, 30);
		mainPanel.add(btnUpload);
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
	 * Method that receives reports that the supervisor window registered to observe
	 * 
	 * @param report - generic report object is passed in and type casted if the
	 *               report class is a ClientAnswerReport
	 */
	@Override
	public void receiveReport(Report report) {
		super.receiveReport(report);
		// ClientAnswerReport
		if (report.getClass() == ClientAnswerReport.class) {
			ClientAnswerReport clientAnswerReport = (ClientAnswerReport) report;
			int selectedRow = monitoringsTable.getSelectedRow(); // selected row in table
			int monitoringIndex = monitorModel.findColumn("ID"); // get index of ID column
			String selectedMonitoringID = monitoringsTable.getValueAt(selectedRow, monitoringIndex).toString();
			selectedMonitoringID = selectedMonitoringID.replace("**", "");
			selectedMonitoringID = selectedMonitoringID.replace(" (local)", "");
			int selectedID = Integer.parseInt(selectedMonitoringID);
			// iterate through monitorings list and find the correct ID
			for (MonitoringDTO monitoring : monitoringsList) {
				if (monitoring.getMonitoringID() == selectedID) {
					monitoring.setAnswers(clientAnswerReport.getAnswers());
					monitoring.setIsEdited(true);
				}
			}
		}
		// VersionCheckFailedReport
		if (report.getClass() == VersionCheckFailedReport.class) {
			VersionCheckFailedReport versionFailedReport = (VersionCheckFailedReport) report;
			VersionOverwritePopupWindow overwritePopup = new VersionOverwritePopupWindow(
					versionFailedReport.getFailedDTO(), caseWorkerID);
		}
		// DownloadedClientListReport
		if (report.getClass() == DownloadedClientListReport.class) {
			DownloadedClientListReport downloadedClientReport = (DownloadedClientListReport) report;
			downloadedClientList = downloadedClientReport.getClientList();

		}
	}

	/**
	 * Registers to observe relevant reports
	 */
	private void registerObservers() {
		roc.registerObserver(this, MonitoringListReport.class);
		roc.registerObserver(this, ClientAnswerReport.class);
		roc.registerObserver(this, ClientMonitoringStatusReport.class);
		roc.registerObserver(this, ClientListReport.class);
		roc.registerObserver(this, VersionCheckFailedReport.class);
		roc.registerObserver(this, DownloadedClientListReport.class);
	}
}
