package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import datasource.CaseWorkerEnum;
import datasource.ClientEnum;
import model.AdvancedSearchCommand;
import model.Client;
import model.GetMonitoringsByCaseWorkerCommand;
import model.GetMonitoringsBySupervisorCommand;
import model.ModelFacade;
import reports.ClientMonitoringStatusReport;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;
import javax.swing.JCheckBox;

/**
 * An abstract window for the specific user roles to extend off of
 * 
 * @author Michael Umbelina
 */
public abstract class AbstractUserWindow extends JFrame implements ActionListener, State, ReportObserver {
	boolean caseWorkerAuthenticated = false;
	boolean supervisorAuthenticated = false;

	JPanel mainPanel;
	JButton btnClearFilters; // button to clear filters on table
	JButton btnSearch; // button to search for monitorings

	DefaultComboBoxModel<Client> searchClientModel;
	Client selectedClient; // object of client selected in combo box
	String selectedClientName; // highlighted client in drop down menu
	JButton btnSelectClient; // button to select highlighted client
	JComboBox<Client> clientDropDown;
	DefaultComboBoxModel<Client> clientModel; // model to populate client drop down
	JComboBox<Client> clientSearchDropDown;
	JComboBox<String> statusSearchDropDown;
	JComboBox<String> dateSearchDropDown;
	JDateChooser datePickerFrom = new JDateChooser();
	JDateChooser datePickerTo = new JDateChooser();

	JButton btnSelectAndReviewMontoring; // button to select highlighted monitorings
	List<MonitoringDTO> monitoringsList; // list of ALL monitorings that the supervisor can see and filter through
	JTable monitoringsTable; // table that holds the monitorings with various fields displayed
	DefaultTableModel monitorModel; // modeling to populate monitoring table
	TableRowSorter<DefaultTableModel> rowSorter; // Used to sort and filter table
	JButton btnResetSearch;
	public boolean checked;
	int supervisorID;
	int caseWorkerID;

	List<Client> cList;
	private JCheckBox checkAllToggle;

	/**
	 * Creates the abstract user window for concrete windows to extend off of
	 */
	public AbstractUserWindow() {
		initializeMainPanel(); // sets window settings
		createMenuPanel();
		createMonitoringsPane(); // creates tab panel which has 2 tabs > View and Edit
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Client selected from menu - filters table accordingly
		if (e.getSource() == btnSelectClient) {
			// first option is the default text
			if (clientDropDown.getSelectedIndex() != 0) {
				selectedClient = (Client) clientDropDown.getSelectedItem();
				selectedClientName = selectedClient.toString();
				RowFilter<DefaultTableModel, Object> filterClient = RowFilter.regexFilter(selectedClientName,
						monitorModel.findColumn("Client"));
				rowSorter.setRowFilter(filterClient);
			}
		}
		// Client selected from search dropdown - filter table to match the dropdown
		if (e.getSource() == btnSearch) {
			selectedClient = null;
			if (clientSearchDropDown.getSelectedIndex() != 0) {
				selectedClient = (Client) clientSearchDropDown.getSelectedItem();
				selectedClientName = selectedClient.getClientName();

			}

			LocalDateTime dateFrom = null;
			LocalDateTime dateTo = null;
			String dateType = null;
			MonitoringStatusEnum status = null;
			
			if (dateSearchDropDown.getSelectedIndex() != 0) {
				dateType = (String) dateSearchDropDown.getSelectedItem();
				dateType = Character.toLowerCase(dateType.charAt(0)) +
		                   (dateType.length() > 1 ? dateType.substring(1) : "");
				dateType = dateType.replace(" ", "");
		
				if (datePickerFrom.getDate() != null) {
					dateFrom = datePickerFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				}
				if (datePickerTo.getDate() != null) {
					dateTo = datePickerTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				}
			}
			
			if(statusSearchDropDown.getSelectedIndex() != 0) {
				for(MonitoringStatusEnum m: MonitoringStatusEnum.values()) {
					if(m.getDescription().equals(statusSearchDropDown.getSelectedItem().toString())) {
						status = m;
					}
				}
			}
			if(caseWorkerAuthenticated) {
				AdvancedSearchCommand advSearch = new AdvancedSearchCommand(caseWorkerID, selectedClient, status, dateFrom, dateTo, dateType);
				ModelFacade.getSingleton().queueCommand(advSearch);
			}else {	
				AdvancedSearchCommand advSearch = new AdvancedSearchCommand(selectedClient, status, dateFrom, dateTo, dateType);
				ModelFacade.getSingleton().queueCommand(advSearch);
			}
			
			

		}
		// reset search sets monitoring table to show all monitorings not approved
		if (e.getSource() == btnResetSearch) {
			rowSorter.setRowFilter(null); // removes all filters
			clientSearchDropDown.setSelectedIndex(0);
			statusSearchDropDown.setSelectedIndex(0);
			dateSearchDropDown.setSelectedIndex(0);
			if(supervisorAuthenticated == true) {
				ModelFacade.getSingleton().queueCommand(new GetMonitoringsBySupervisorCommand(supervisorID));
			}
			else {
				ModelFacade.getSingleton().queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
			}
		}
		// Button to clear the filters for monitorings table
		if (e.getSource() == btnClearFilters) {
			rowSorter.setRowFilter(null); // removes all filters
			populateClientDropDown(monitoringsList);
			clientModel.setSelectedItem(clientModel.getElementAt(0));
		}
		// selects or deselects all checkboxes
		if (e.getSource() == checkAllToggle) {
			for (int i = 0; i < monitoringsTable.getRowCount(); i++) {
				if (checked == false) {
					monitorModel.setValueAt(Boolean.TRUE, i, 0);
				} else {
					monitorModel.setValueAt(Boolean.FALSE, i, 0);
				}
			}
			if (checked == false) {
				checked = true;
			} else {
				checked = false;
			}
		}
	}

	/**
	 * Used to populate monitorings table with all monitorings except those that
	 * have been approved
	 * 
	 * @param monitoringsList2 - List of monitorings from the MonitoringListReport
	 */
	protected void resetMonitoringTableToDefault(List<MonitoringDTO> monitoringsList) {
		// remove the current list of monitorings from the tablemodel
		monitorModel.setRowCount(0);
		// refill with all monitorings from MonitoringsListReport
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
		for (MonitoringDTO m : monitoringsList) {
			if (m.getStatus() != MonitoringStatusEnum.APPROVED) {
				// Add * if the monitoring has local edits
				Vector<Object> row = new Vector<Object>();
				if (caseWorkerAuthenticated) {
					row.add(Boolean.FALSE);
				}
				if (m.getIsLocal()) {
					if (m.getIsLocallyEdited()) {
						row.add("**" + m.getMonitoringID() + " (local)");
					} else {
						row.add(m.getMonitoringID() + " (local)");
					}
				} else {
					row.add(m.getMonitoringID());
				}
				/* Visit date */
				if (m.getVisitDate() != null) {
					row.add(m.getVisitDate().toLocalDate().format(formatter)); // DD/MM/YYYY
				} else {
					row.add("");
				}
				/* Due date */
				if (m.getDueDate() != null) {
					row.add(m.getDueDate().toLocalDate().format(formatter)); // DD/MM/YYYY
				} else {
					row.add("");
				}
				row.add(m.getClient());
				row.add(m.getCaseWorker());
				row.add(m.getStatus().getDescription());
				monitorModel.addRow(row);
			}
		}
	}

	/**
	 * Used to populate monitorings table with all monitorings contained in the
	 * MonitoringsListReport including ALL statuses
	 * 
	 * @param monitoringsList2 - List of monitorings from the MonitoringListReport
	 */
	protected void fillMonitoringTableWithAllAvailable(List<MonitoringDTO> monitoringsList) {
		// remove the current list of monitorings from the tablemodel
		monitorModel.setRowCount(0);
		// refill with all monitorings from MonitoringsListReport
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
		for (MonitoringDTO m : monitoringsList) {
			// Add * if the monitoring has local edits
			Vector<Object> row = new Vector<Object>();
			if (caseWorkerAuthenticated) {
				row.add(Boolean.FALSE);
			}
			if (m.getIsLocal()) {
				if (m.getIsLocallyEdited()) {
					row.add("**" + m.getMonitoringID() + " (local)");
				} else {
					row.add(m.getMonitoringID() + " (local)");
				}
			} else {
				row.add(m.getMonitoringID());
			}
			/* Visit date */
			if (m.getVisitDate() != null) {
				row.add(m.getVisitDate().toLocalDate().format(formatter)); // DD/MM/YYYY
			} else {
				row.add("");
			}
			/* Due date */
			if (m.getDueDate() != null) {
				row.add(m.getDueDate().toLocalDate().format(formatter)); // DD/MM/YYYY
			} else {
				row.add("");
			}
			row.add(m.getClient());
			row.add(m.getCaseWorker());
			row.add(m.getStatus().getDescription());
			monitorModel.addRow(row);
		}
	}

	/**
	 * Creates the drop down menu containing a list of clients after a caseworker is
	 * selected
	 */
	protected void createClientMenu() {
		clientDropDown = new JComboBox<Client>();
		clientModel = new DefaultComboBoxModel<Client>();
		clientDropDown.setModel(clientModel);
		// override to populate the JComboBox with a Client Object
		clientDropDown.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Client) {
					Client client = (Client) value;
					setText(client.getClientName()); // displays only name
				}
				return this;
			}
		});

		// drop down for filtering
		btnSelectClient = new JButton("Select Client");
		btnSelectClient.addActionListener(this);
		mainPanel.add(btnSelectClient);
		mainPanel.add(clientDropDown);

		// drop down to be used for searching
		clientSearchDropDown = new JComboBox<Client>();
		String[] statupi = { "Search By Status...", MonitoringStatusEnum.APPROVED.getDescription(),
				MonitoringStatusEnum.IN_PROGRESS.getDescription(),
				MonitoringStatusEnum.PENDING_FOR_CORRECTION.getDescription(),
				MonitoringStatusEnum.PENDING_FOR_REVIEW.getDescription(),
				MonitoringStatusEnum.SCHEDULED.getDescription() };

		statusSearchDropDown = new JComboBox<String>(statupi);

		String[] dateTypes = { "Search by Date...", "Visit Date", "Approved Date", "Due Date" };
		dateSearchDropDown = new JComboBox<String>(dateTypes);

		searchClientModel = new DefaultComboBoxModel<Client>();
		clientSearchDropDown.setModel(searchClientModel);
		clientDropDown.setRenderer(new DefaultListCellRenderer());
		btnSearch = new JButton("Search"); // button for searching
		btnSearch.addActionListener(this);

		btnResetSearch = new JButton("Reset Search");
		btnResetSearch.addActionListener(this);

		mainPanel.add(btnResetSearch);

		mainPanel.add(clientSearchDropDown);
		mainPanel.add(statusSearchDropDown);
		mainPanel.add(dateSearchDropDown);
		mainPanel.add(btnSearch);
	}

	protected abstract void createMenuPanel();

	/**
	 * Creates monitorings table that displays the monitorings that pertain to the
	 * currently authenticated supervisor
	 */
	protected void createMonitoringsPane() {
		JLabel lblMonitoringsToReview = new JLabel("Monitorings To Review");
		lblMonitoringsToReview.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblMonitoringsToReview.setBounds(505, 6, 210, 24);
		mainPanel.add(lblMonitoringsToReview);

		// create model and prevent cells from being edited
		monitorModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// caseworker has a checkbox column that should be editable
				if (caseWorkerAuthenticated && column == 0) {
					return true; // check box column is editable
				}
				return false; // rest are not editable
			}
		};

		monitoringsTable = new JTable();
		rowSorter = new TableRowSorter<DefaultTableModel>(monitorModel); // used to sort and filter table

		monitoringsTable.getTableHeader().setReorderingAllowed(false); // prevents column rearrangement
		monitoringsTable.getTableHeader().setEnabled(false); // removes reorder toggle arrow in header

		monitoringsTable.setRowSorter(rowSorter);
		// prevents the user from selecting multiple monitorings at once
		// monitoringsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		monitoringsTable.setBorder(new LineBorder(new Color(0, 0, 0)));

		JScrollPane scrollPane = new JScrollPane(monitoringsTable);
		scrollPane.setBounds(271, 42, 800, 478);

		mainPanel.add(scrollPane);

		btnSelectAndReviewMontoring = new JButton("Select and Review");
		btnSelectAndReviewMontoring.addActionListener(this);
		btnSelectAndReviewMontoring.setBounds(915, 525, 145, 30);
		mainPanel.add(btnSelectAndReviewMontoring);

		JLabel lblcheckAllToggle = new JLabel("select all");
		lblcheckAllToggle.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblcheckAllToggle.setBounds(297, 23, 250, 24);
		mainPanel.add(lblcheckAllToggle);
		checkAllToggle = new JCheckBox();
		checkAllToggle.setBounds(275, 25, 21, 23);
		checkAllToggle.addActionListener(this);
		mainPanel.add(checkAllToggle);
	}

	/**
	 * Set the StateMachine to this window
	 * 
	 * @param machine the StateMachine controlling the windows
	 */
	public void doAction(Launcher machine) {
		machine.setState(this);
	}

	/**
	 * Sets preliminary window settings
	 */
	protected void initializeMainPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainPanel = new JPanel();
		setBounds(200, 200, 1100, 625);
		mainPanel.setMaximumSize(new Dimension(60000, 60000));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
	}

	/**
	 * Populates drop down of caseworker names for filtering
	 * 
	 * @param monitoringsList2 - Monitorings to be displayed
	 */
	protected void populateClientDropDown(List<MonitoringDTO> monitoringsList2) {
		// Update the client menu to use only names that appear in monitoring table
		clientModel.removeAllElements();
		searchClientModel.removeAllElements();
		Client dummyCLabel = new Client("Filter by Client...", -1);
		Client dummyCSLabel = new Client("Search by Client...", -1);
		Client searchByAllLabel = new Client("All", -1);
		clientModel.addElement(dummyCLabel);
		searchClientModel.addElement(dummyCSLabel);
		searchClientModel.addElement(searchByAllLabel);
		cList = new ArrayList<Client>();
		for (MonitoringDTO m : monitoringsList2) {
			Client client = m.getClient();
			if (!cList.contains(client)) {
				cList.add(client);
			}
		}
		
		for(Client c : cList) {
			clientModel.addElement(c);
		}
		
		if (caseWorkerAuthenticated) {
			for (ClientEnum c : ClientEnum.values()) {
				if (c.getCaseWorkerID() == caseWorkerID) {
					Client client = new Client (c.getClientName(), c.getClientID());
					searchClientModel.addElement(client);
				}
			}
		} else {
			for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
				if (cw.getSupervisorID() == supervisorID) {
					for (ClientEnum c : ClientEnum.values()) {
						if (c.getCaseWorkerID() == cw.getCaseWorkerID()) {
							Client client = new Client (c.getClientName(), c.getClientID());
							searchClientModel.addElement(client);
						}
					}
				}
			}
		}

	}

	@Override
	public void receiveReport(Report report) {
		// MonitoringListReport contains a list of all monitorings
		if (report.getClass() == MonitoringListReport.class) {
			MonitoringListReport monitoringListReport = (MonitoringListReport) report;
			try {
				// get the list of monitorings
				monitoringsList = monitoringListReport.getMonitorings();
				resetMonitoringTableToDefault(monitoringsList);
				populateClientDropDown(monitoringsList);

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (report.getClass() == ClientMonitoringStatusReport.class) {
			ClientMonitoringStatusReport clientMonitoringStatusReport = (ClientMonitoringStatusReport) report;

			int selectedRow = monitoringsTable.getSelectedRow(); // selected row in table

			int monitoringIndex = monitorModel.findColumn("ID"); // get index of ID column
			String selectedMonitoringID = monitoringsTable.getValueAt(selectedRow, monitoringIndex).toString();
			selectedMonitoringID = selectedMonitoringID.replace("**", "");
			selectedMonitoringID = selectedMonitoringID.replace(" (local)", "");
			int selectedID = Integer.parseInt(selectedMonitoringID);
			// iterate through monitorings list and find the correct ID
			for (MonitoringDTO monitoring : monitoringsList) {
				if (monitoring.getMonitoringID() == selectedID) {
					monitoring.setStatus(clientMonitoringStatusReport.getStatus());
				}
			}
			resetMonitoringTableToDefault(monitoringsList);
		}
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
