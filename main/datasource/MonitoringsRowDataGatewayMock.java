package datasource;

import java.time.LocalDateTime;
import java.util.List;

import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Row data gateway that gets a client answers to the monitoring.
 * 
 * @author Ian
 *
 */
public class MonitoringsRowDataGatewayMock extends MonitoringsRowDataGateway {
	private int monitoringID;
	private MonitoringsMockDatabase database;

	/**
	 * Constructor for monitoring row data gateway
	 * 
	 * @param monitoringID the specific monitoring id this gateway should manage
	 */
	public MonitoringsRowDataGatewayMock(int monitoringID) {
		this.monitoringID = monitoringID;
		database = MonitoringsMockDatabase.getSingleton();
	}

	/** 
	 * Constructor used to create a new monitoring 
	 * 
	 * @param visitDateTime
	 * @param dueDateTime
	 * @param approvedDateTime
	 * @param clientID
	 * @param caseworkerID
	 * @param questions
	 * @param answers
	 * @param status
	 * @param isLocallyEdited TODO
	 */
	public MonitoringsRowDataGatewayMock(LocalDateTime visitDateTime, LocalDateTime dueDateTime,
			LocalDateTime approvedDateTime, int clientID, int caseworkerID, List<String> questions,
			List<String> answers, MonitoringStatusEnum status, int version, boolean isLocallyEdited, boolean isDownloaded) {
		
		// caseWorker object
		CaseWorker caseWorker = null;
		for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
			if (cw.getCaseWorkerID() == caseworkerID) {
				caseWorker = new CaseWorker(cw.getCaseWorkerName(), cw.getCaseWorkerID());
			}
		}
		// client object
		Client client = null;
		for (ClientEnum c : ClientEnum.values()) {
			if (c.getClientID() == clientID) {
				client = new Client(c.getClientName(), c.getClientID());
			}
		}
		
		MonitoringDTO monitoring = new MonitoringDTO(-1, visitDateTime,  dueDateTime,
				 approvedDateTime,  client,  caseWorker, questions,
				answers,  status, version, isLocallyEdited, isDownloaded);

		database = MonitoringsMockDatabase.getSingleton();
		database.createNewMonitoring(monitoring);
	}

	/**
	 * Returns a list of client answers
	 * 
	 * @return the list of client answers
	 */
	public List<String> getClientAnswers() {
		return database.selectStarByMonitoringID(monitoringID).getAnswers();
	}

	/**
	 * @see MonitoringsRowDataGateway#getMonitoringID()
	 */
	@Override
	public int getMonitoringID() {
		return monitoringID;
	}

	/**
	 * @see MonitoringsRowDataGateway#getVisitDate()
	 */
	@Override
	public LocalDateTime getVisitDate() {
		return database.selectStarByMonitoringID(monitoringID).getVisitDate();
	}

	/**
	 * @see MonitoringsRowDataGateway#getDueDate()
	 */
	@Override
	public LocalDateTime getDueDate() {
		return database.selectStarByMonitoringID(monitoringID).getDueDate();
	}

	/**
	 * @see MonitoringsRowDataGateway#getApprovedDate()
	 */
	@Override
	public LocalDateTime getApprovedDate() {
		return database.selectStarByMonitoringID(monitoringID).getApprovedDate();
	}

	/**
	 * @see MonitoringsRowDataGateway#getClientID()
	 */
	@Override
	public Client getClient() {
		return database.selectStarByMonitoringID(monitoringID).getClient();
	}

	/**
	 * @see MonitoringsRowDataGateway#getCaseworkerID()
	 */
	@Override
	public CaseWorker getCaseworker() {
		return database.selectStarByMonitoringID(monitoringID).getCaseWorker();
	}

	/**
	 * @see MonitoringsRowDataGateway#getQuestions()
	 */
	@Override
	public List<String> getQuestions() {
		return database.selectStarByMonitoringID(monitoringID).getQuestions();
	}

	/**
	 * @see MonitoringsRowDataGateway#getAnswers()
	 */
	@Override
	public List<String> getAnswers() {
		return database.selectStarByMonitoringID(monitoringID).getAnswers();
	}

	/**
	 * @see MonitoringsRowDataGateway#getStatus()
	 */
	@Override
	public MonitoringStatusEnum getStatus() {
		return database.selectStarByMonitoringID(monitoringID).getStatus();
	}
	
	/**
	 * @see MonitoringsRowDataGateway#getVersion()
	 */
	@Override
	public int getVersion() {
		return database.selectStarByMonitoringID(monitoringID).getVersion();
	}

	/**
	 * @see MonitoringsRowDataGateway#setAnswers(List)
	 */
	@Override
	public void setAnswers(List<String> newAnswers) {
		database.updateAnswersByMonitoringID(monitoringID, newAnswers);
		
		setVersion(getVersion() + 1);
	}

	/**
	 * 
	 * @see MonitoringsRowDataGateway#setStatus(MonitoringStatusEnum)
	 */
	@Override
	public void setStatus(MonitoringStatusEnum newStatus) {
		database.updateStatusByMonitoringID(monitoringID, newStatus);
		
		setVersion(getVersion() + 1);
	}

	/**
	 * @see MonitoringsRowDataGateway#setVisitDate(LocalDateTime)
	 */
	@Override
	public void setVisitDateTime(LocalDateTime visitDateTime) {
		database.updateVisitDateByMonitoringID(monitoringID, visitDateTime);
		
		setVersion(getVersion() + 1);
	}
	
	/**
	 * @see MonitoringsRowDataGateway#setVersion(int)
	 */
	@Override
	public void setVersion(int version) {
		database.updateVersionByMonitoringID(monitoringID, version);
	}

	/**
	 * @see MonitoringsRowDataGateway#setIsDownloaded(boolean)
	 */
	@Override
	public void setIsDownloaded(boolean isDownloaded) {
		database.selectStarByMonitoringID(monitoringID).setIsDownloaded(isDownloaded);
	}
	
	/**
	 * Calls the other setters when completely updating a monitoring
	 * 
	 * @param answers   - the updated answers
	 * @param status    - the updated status
	 * @param visitDate - the new visit date
	 * @param version   - the updated version number
	 */
	public void setMultiple(List<String> answers, MonitoringStatusEnum status, LocalDateTime visitDate, int version) {
		int oldVersion = version;
		
		setAnswers(answers);
		setStatus(status);
		setVisitDateTime(visitDate);
		setVersion(oldVersion + 1);
		setIsDownloaded(false);
	}
	
	/**
	 * @see MonitoringsRowDataGateway#getAutoIncrementMax()
	 */
	@Override
	public int getAutoIncrementMax() {
		return database.getAutoIncrementMax();
	}
	
	/**
	 *@return the locally edited status
	 */
	public boolean getIsLocallyEdited() {
		return database.selectStarByMonitoringID(monitoringID).getIsLocallyEdited();
	}

	@Override
	public MonitoringDTO getMonitoringForDownload() {
		setIsDownloaded(true);
		return database.selectStarByMonitoringID(monitoringID);
	}
}
