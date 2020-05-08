package model;

import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayFactory;
import sharedData.MonitoringStatusEnum;

/**
 * Switches monitorings from in progress to pending for review
 * 
 * @author kimberlyoneill
 *
 */

public class SubmitForReviewCommand implements Command {

	private int monitoringID;
	private int caseWorkerID;
	private Client client;
	private ModelFacade remote = ModelFacade.getSingleton();

	public SubmitForReviewCommand(int monitoringID, int caseWorkerID, Client client) {
		this.monitoringID = monitoringID;
		this.caseWorkerID = caseWorkerID;
		this.client = client;
	}

	@Override
	public void execute() {
		MonitoringsRowDataGateway monitoringsGateway = MonitoringsRowDataGatewayFactory.getGateway(monitoringID, client.getClientName());
		monitoringsGateway.setStatus(MonitoringStatusEnum.PENDING_FOR_REVIEW);
		remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
	}

}
