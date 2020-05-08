package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayFactory;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import reports.ClientAnswerReport;
import reports.ClientMonitoringStatusReport;
import reports.MonitoringListReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringStatusEnum;

/**
 * Sets the status of monitoring selected in Supervisor Review Monitorings
 * Window
 * 
 * @author michaelpermyashkin
 *
 */
public class SetMonitoringStatusCommand implements Command {

	// instance of the ROC to send the packaged report
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	// list of monitorings which each have a status
	private List<Object[]> monitorings = new ArrayList<>();
	// list of monitorings that need to be reviewed
	private List<Object[]> monitoringsPendingReview = new ArrayList<>();
	private int monitoringID;
	private MonitoringStatusEnum newStatus;
	private Client client;

	/**
	 * The constructor sets the new status and then generates a new monitorings
	 * report will all monitorings where the status is "Pending Review." This report
	 * is sent to the supervisor to update the table of monitorings
	 * 
	 * @param id        - ID of monitoring to set status
	 * @param newStatus - string of new status to be set
	 */
	public SetMonitoringStatusCommand(int monitoringID, Client client, MonitoringStatusEnum newStatus) {
		this.monitoringID = monitoringID;
		this.newStatus = newStatus;
		this.client = client;
	}

	/**
	 * Builds new list of monitorings that need to be reviewed and sends a new
	 * report of monitorings
	 */
	@Override
	public void execute() {
		MonitoringsRowDataGateway monitoringsGateway = MonitoringsRowDataGatewayFactory.getGateway(monitoringID, client.getClientName());
		
		monitoringsGateway.setStatus(newStatus);
		ClientMonitoringStatusReport monitoringReport = new ClientMonitoringStatusReport(newStatus);
		roc.sendReport(monitoringReport);
	}
}