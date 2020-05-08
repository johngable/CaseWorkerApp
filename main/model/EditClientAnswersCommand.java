package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datasource.MonitoringsRowDataGatewayFactory;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import reports.ClientAnswerReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringStatusEnum;

/**
 * Implements the Command interface Called from the command executer when the
 * client answer needs to be changed.
 * 
 * @author michaelpermyashkin
 *
 */
public class EditClientAnswersCommand implements Command {

	private int monitoringID; // holds the name of the client
	private List<String> answers = new ArrayList<>();
	private int caseWorkerID;
	private Client client;

	private ModelFacade remote = ModelFacade.getSingleton(); // Command Queue
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();

	/**
	 * @param clientName the name of the client
	 * @param updateQ1   their new answer to question 1
	 * @param updateQ2   their new answer to question 2
	 */
	public EditClientAnswersCommand(int monitoringID, Client client, List<String> newAnswers,
			int caseWorkerID) {
		this.monitoringID = monitoringID;
		this.client = client;
		this.caseWorkerID = caseWorkerID;
		answers.addAll(newAnswers);
	}

	/**
	 * Makes and instance of the data source gateway and passes the client name in
	 * Updates the clients current answers with the new
	 * 
	 * Immediately gets the new answers and generates a report and is sent to the
	 * Report Observer Connector
	 */
	@Override
	public void execute() {
		MonitoringsRowDataGateway monitoringsGateway = MonitoringsRowDataGatewayFactory.getGateway(monitoringID, client.getClientName());

		// if this is the first visit, we set visit date to current date and time
		if (monitoringsGateway.getVisitDate() == null) {
			monitoringsGateway.setVisitDateTime(LocalDateTime.now());
		}

		// set answers
		monitoringsGateway.setAnswers(answers);
		monitoringsGateway.setStatus(MonitoringStatusEnum.IN_PROGRESS);

		ClientAnswerReport report = new ClientAnswerReport(answers);
		roc.sendReport(report);
	}

}
