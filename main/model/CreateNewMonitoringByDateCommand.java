package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import datasource.MonitoringsEnum;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import datasource.QuestionsEnum;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Command to create a new monitoring when given a date
 * 
 * @author amy
 *
 */
public class CreateNewMonitoringByDateCommand implements Command {
	private List<String> questions = new ArrayList<>();
	private LocalDateTime monitoringDate;
	private int clientID;
	private int caseWorkerID;
	private int supervisorID;
	private List<String> answers = new ArrayList<>();
	private MonitoringStatusEnum status;
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private int version = 1;

	/**
	 * Creates a new CreateNewMonitoringByDateCommand for a supervisor. Gets some info from
	 * constuctor and determines the rest.
	 * 
	 * @param monitoringDate
	 * @param clientID
	 * @param caseWorkerID
	 * @param supervisorID
	 */
	public CreateNewMonitoringByDateCommand(LocalDateTime monitoringDate, int clientID, int caseWorkerID, int supervisorID) {
		this.clientID = clientID;
		this.monitoringDate = monitoringDate;
		for (QuestionsEnum q : QuestionsEnum.values()) {
			questions.add(q.getQuestion());
			answers.add("");
		}
		this.caseWorkerID = caseWorkerID;
		this.supervisorID = supervisorID;

		this.status = MonitoringStatusEnum.SCHEDULED;
	}
	
	/**
	 * Creates a new CreateNewMonitoringByDateCommand for a caseworker. Gets some info from
	 * constuctor and determines the rest.
	 * 
	 * @param monitoringDate
	 * @param clientID
	 * @param caseWorkerID
	 */
	public CreateNewMonitoringByDateCommand(LocalDateTime monitoringDate, int clientID, int caseWorkerID) {
		this.clientID = clientID;
		this.monitoringDate = monitoringDate;
		for (QuestionsEnum q : QuestionsEnum.values()) {
			questions.add(q.getQuestion());
			answers.add("");
		}
		this.caseWorkerID = caseWorkerID;

		status = MonitoringStatusEnum.SCHEDULED;
	}

	/**
	 * Executes command to insert the new info
	 */
	@Override
	public void execute() {
		// gets gateway based on mode - TEST or PRODUCTION
		MonitoringsRowDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			monitoringsGateway = new MonitoringsRowDataGatewayMock(null, monitoringDate, null,
					clientID, caseWorkerID, questions, answers, status, version, false, false);
		} else {
			monitoringsGateway = new MonitoringsRowDataGatewayRDS(null, monitoringDate, null,
					clientID, caseWorkerID, questions, answers, status, version, false);
		}

		// used to determine if supervisors or caseworkers window needs to be updated
		if (supervisorID == 0) {
			remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
		} else {
			remote.queueCommand(new GetMonitoringsBySupervisorCommand(supervisorID));
		}
	}

}