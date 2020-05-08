package datasource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Is a mock db table. We need this because there is a mock row data gateway and
 * a mock table data gateway. Changes made in one need to be reflected in the
 * other . . .
 * 
 * @author merlin
 *
 */
public class MonitoringsMockDatabase {

	private HashMap<Integer, List<MonitoringDTO>> dataByClientID;
	private HashMap<Integer, List<MonitoringDTO>> dataByCaseWorkerID;
	private HashMap<Integer, MonitoringDTO> dataByMonitoringID;
	private HashMap<Integer, List<MonitoringDTO>> dataByDownloaded;
	private HashMap<MonitoringStatusEnum, List<MonitoringDTO>> dataByStatus;
	public static int lastMonitoringID = 0; // count of last assigned ID
	private static MonitoringsMockDatabase singleton;

	public static MonitoringsMockDatabase getSingleton() {
		if (singleton == null) {
			singleton = new MonitoringsMockDatabase();
		}
		return singleton;
	}

	/**
	 * 
	 */
	private MonitoringsMockDatabase() {
		resetData();
	}

	public static void resetSingleton() {
		singleton = null;
	}

	/**
	 * rebuild the data from the MonitoringsEnum
	 */
	private void resetData() {
		dataByClientID = new HashMap<Integer, List<MonitoringDTO>>();
		dataByCaseWorkerID = new HashMap<Integer, List<MonitoringDTO>>();
		dataByStatus = new HashMap<MonitoringStatusEnum, List<MonitoringDTO>>();
		dataByMonitoringID = new HashMap<Integer, MonitoringDTO>();
		dataByDownloaded = new HashMap<Integer, List<MonitoringDTO>>();
		for (MonitoringsEnum e : MonitoringsEnum.values()) {
			// build client object
			Client client = null;
			for (ClientEnum cl : ClientEnum.values()) {
				if (cl.getClientID() == e.getClientId()) {
					client = new Client(cl.getClientName(), cl.getClientID());
				}
			}
			// build caseWorker object
			CaseWorker caseWorker = null;
			for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
				if (cw.getCaseWorkerID() == e.getCaseWorkerID()) {
					caseWorker = new CaseWorker(cw.getCaseWorkerName(), cw.getCaseWorkerID());
				}
			}
			MonitoringDTO row = new MonitoringDTO(e.getMonitoringID(), e.getVisitDate(), e.getDueDate(),
					e.getApprovedDate(), client, caseWorker, e.getQuestions(), e.getClientAnswers(), e.getStatus(),
					e.getVersion(), e.getIsLocallyEdited(), e.getIsDownloaded());

			insertRowByMonitoringID(e.getMonitoringID(), row);
			insertRowByClientID(e.getClientId(), row);
			insertRowByCaseWorkerID(e.getCaseWorkerID(), row);
			insertRowByStatus(e.getStatus(), row);
			insertRowByDownloaded(e.getIsDownloaded(), row);

			lastMonitoringID++;
		}
	}

	/**
	 * method to create a new monitoring
	 * 
	 * @param monitoring - MonitoringDTO to add
	 */
	void createNewMonitoring(MonitoringDTO monitoring) {
		lastMonitoringID++;
		monitoring.setMonitoringID(lastMonitoringID);
		insertRowByMonitoringID(monitoring.getMonitoringID(), monitoring);
		insertRowByClientID(monitoring.getClient().getClientID(), monitoring);
		insertRowByCaseWorkerID(monitoring.getCaseWorker().getCaseWorkerID(), monitoring);
		insertRowByStatus(monitoring.getStatus(), monitoring);
	}

	/**
	 * HashMap of monitorings to retrieve by monitoring ID
	 * 
	 * @param monitoringID - monitoring ID
	 * @param row          - monitoring to add
	 */
	private void insertRowByMonitoringID(int monitoringID, MonitoringDTO row) {
		dataByMonitoringID.put(monitoringID, row);
	}

	/**
	 * HashMap of monitorings to retrieve by client ID
	 * 
	 * @param clientID - client id
	 * @param row      - monitoring to add
	 */
	private void insertRowByClientID(int clientID, MonitoringDTO row) {
		List<MonitoringDTO> data = new ArrayList<MonitoringDTO>();
		if (dataByClientID.containsKey(clientID)) {
			data = dataByClientID.get(clientID);
		}
		data.add(row);
		dataByClientID.put(clientID, data);
	}

	/**
	 * HashMap of monitorings to retrieve by caseworker ID
	 * 
	 * @param caseworkerID - caseworker id
	 * @param row          - monitoring to add
	 */
	private void insertRowByCaseWorkerID(int caseWorkerID, MonitoringDTO row) {
		List<MonitoringDTO> data = new ArrayList<MonitoringDTO>();
		if (dataByCaseWorkerID.containsKey(caseWorkerID)) {
			data = dataByCaseWorkerID.get(caseWorkerID);
		}
		data.add(row);
		dataByCaseWorkerID.put(caseWorkerID, data);
	}

	/**
	 * HashMap of monitorings to retrieve by monitoring status
	 * 
	 * @param status - monitoring status
	 * @param row    - monitoring to add
	 */
	private void insertRowByStatus(MonitoringStatusEnum status, MonitoringDTO row) {
		List<MonitoringDTO> data = new ArrayList<MonitoringDTO>();
		if (dataByStatus.containsKey(status)) {
			data = dataByStatus.get(status);
		}
		data.add(row);
		dataByStatus.put(status, data);
	}

	/**
	 * HashMap of monitorings to retrieve by download status
	 * 
	 * @param isDownloaded - TRUE/FALSE
	 * @param row          - Monitoring DTO to add
	 */
	private void insertRowByDownloaded(boolean isDownloaded, MonitoringDTO row) {
		int searchKey;
		if (isDownloaded) {
			searchKey = 2;
		} else {
			searchKey = 1;
		}

		List<MonitoringDTO> data = new ArrayList<MonitoringDTO>();
		if (dataByDownloaded.containsKey(searchKey)) {
			data = dataByDownloaded.get(searchKey);
		}
		data.add(row);
		dataByDownloaded.put(searchKey, data);
	}

	/**
	 * @param i the ID of the client we are interested in
	 * @return all of the monitorings for that client
	 */
	public List<MonitoringDTO> selectStarByClientID(int i) {
		return dataByClientID.get(i);
	}

	/**
	 * Selects all monitorings for a caseworker by ID
	 * 
	 * @param i - caseworkerID
	 * @return - List<MonitoringDTO> for caseworker
	 */
	public List<MonitoringDTO> selectStarByCaseWorkerID(int i) {
		return dataByCaseWorkerID.get(i);
	}

	/**
	 * @param i the ID of the monitoring we are interested in
	 * @return the data of that single monitoring
	 */
	public MonitoringDTO selectStarByMonitoringID(int i) {
		return dataByMonitoringID.get(i);
	}

	/**
	 * Selects all monitorings for a caseworker by ID
	 * 
	 * @param i - caseworkerID
	 * @return - List<MonitoringDTO> for caseworker
	 */
	public List<MonitoringDTO> selectStarByIsDownload(boolean isDownloaded) {
		if (isDownloaded) {
			return dataByCaseWorkerID.get(2);
		} else {
			return dataByCaseWorkerID.get(1);
		}
	}

	/**
	 * @param monitoringID the monitoring that should be updated
	 * @param newAnswers   the new answers
	 */
	public void updateAnswersByMonitoringID(int monitoringID, List<String> newAnswers) {
		MonitoringDTO row = dataByMonitoringID.get(monitoringID);
		row.setAnswers(newAnswers);

	}

	/**
	 * @param monitoringID the monitoring that should be updated
	 * @param newStatus    the new status
	 */
	public void updateStatusByMonitoringID(int monitoringID, MonitoringStatusEnum newStatus) {
		MonitoringDTO row = dataByMonitoringID.get(monitoringID);
		row.setStatus(newStatus);
	}

	/**
	 * @param monitoringID the monitoring that should be updated
	 * @param version      the new version number
	 */
	public void updateVersionByMonitoringID(int monitoringID, int version) {
		MonitoringDTO row = dataByMonitoringID.get(monitoringID);
		row.setVersion(version);
	}

	/**
	 * @param status the status we are interested in
	 * @return the list of monitorings that match the given status
	 */
	public List<MonitoringDTO> selectStarByStatus(MonitoringStatusEnum status) {
		return dataByStatus.get(status);
	}

	/**
	 * Select monitorings where visit data is on or after given date
	 * 
	 * @param allMonitoringsForCaseWorker - all monitorings for caseworker to filter
	 *                                    date field
	 * @param pastDate                    - date we want to filter by
	 * @return - list of monitorings where visit date is on or after given date
	 */
	public List<MonitoringDTO> selectStarByDate(List<MonitoringDTO> allMonitoringsForClient, LocalDate pastDate) {
		List<MonitoringDTO> toReturn = new ArrayList<>();
		if (allMonitoringsForClient != null) {
			for (MonitoringDTO m : allMonitoringsForClient) {
				if (m.getVisitDate() != null) {
					if (m.getVisitDate().toLocalDate().isEqual(pastDate)
							|| m.getVisitDate().toLocalDate().isAfter(pastDate)) {
						toReturn.add(m);
					}
				}
			}
		}
		return toReturn;
	}

	/**
	 * @return - last monitoringID assigned
	 */
	public int getAutoIncrementMax() {
		return lastMonitoringID;
	}

	/**
	 * Sets visit date and time field
	 * 
	 * @param monitoringID  - id of monitoring to updatew
	 * @param visitDateTime - value of visit date time to set
	 */
	public void updateVisitDateByMonitoringID(int monitoringID, LocalDateTime visitDateTime) {
		MonitoringDTO row = dataByMonitoringID.get(monitoringID);
		row.setVisitDate(visitDateTime);

	}

	public void setIsDownloaded(boolean isDownloaded, int monitoringID) {
		MonitoringDTO row = dataByMonitoringID.get(monitoringID);
		row.setIsDownloaded(isDownloaded);
	}

	public List<MonitoringDTO> selectStarByDateFromDateTo(List<MonitoringDTO> allMonitoringsForClient,
			LocalDate dateFrom, LocalDate dateTo, String dateType) {
		List<MonitoringDTO> toReturn = new ArrayList<>();

		if (dateType.equals("visitDate")) {
			if (allMonitoringsForClient != null) {
				for (MonitoringDTO m : allMonitoringsForClient) {
					if (m.getVisitDate() != null) {
						if (m.getVisitDate().toLocalDate().isEqual(dateFrom)
								|| m.getVisitDate().toLocalDate().isAfter(dateFrom)) {
							if (m.getVisitDate().toLocalDate().isBefore(dateTo)) {
								toReturn.add(m);
							}
						}
					}
				}
			}
		}

		if (dateType.equals("dueDate")) {
			if (allMonitoringsForClient != null) {
				for (MonitoringDTO m : allMonitoringsForClient) {
					if (m.getDueDate() != null) {
						if (m.getDueDate().toLocalDate().isEqual(dateFrom)
								|| m.getDueDate().toLocalDate().isAfter(dateFrom)) {
							if (m.getDueDate().toLocalDate().isBefore(dateTo)) {
								toReturn.add(m);
							}
						}
					}
				}
			}
		}

		if (dateType.equals("approvedDate")) {
			if (allMonitoringsForClient != null) {
				for (MonitoringDTO m : allMonitoringsForClient) {
					if (m.getApprovedDate() != null) {
						if (m.getApprovedDate().toLocalDate().isEqual(dateFrom)
								|| m.getApprovedDate().toLocalDate().isAfter(dateFrom)) {
							if (m.getApprovedDate().toLocalDate().isBefore(dateTo)) {
								toReturn.add(m);
							}
						}
					}
				}
			}
		}

		return toReturn;
	}

}
