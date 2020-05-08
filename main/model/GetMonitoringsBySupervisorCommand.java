package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import datasource.CaseWorkerEnum;
import datasource.CaseWorkerRowDataGateway;
import datasource.CaseWorkerTableDataGateway;
import datasource.ClientEnum;
import datasource.ClientRowDataGateway;
import datasource.ClientTableDataGateway;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import reports.ClientMonitoringStatusReport;
import reports.MonitoringListReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Executes command to get the monitoring by various parameters as needed
 * 
 * @author Michael Permyashkin
 */
public class GetMonitoringsBySupervisorCommand implements Command {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Used to send report
	private int supervisorID;

	/**
	 * Create the command
	 * 
	 * @param clientName the name of the client whose status you want
	 */
	public GetMonitoringsBySupervisorCommand(int supervisorID) {
		this.supervisorID = supervisorID;
	}

	/**
	 * Execute command to get all the monitorings for a specific supervisor
	 */
	@Override
	public void execute() {
		// gets gateway based on mode - TEST or PRODUCTION
		MonitoringsTableDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			monitoringsGateway = new MonitoringsTableDataGatewayMock();
		} else {
			monitoringsGateway = new MonitoringsTableDataGatewayRDS();
		}
		
		// for every caseworker that reports to supervisor, get all monitorings
		List<MonitoringDTO> monitorings = new ArrayList<>();
		for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
			if (cw.getSupervisorID() == supervisorID) {
				List<MonitoringDTO> temp = monitoringsGateway.getMonitoringsByCaseWorkerID(cw.getCaseWorkerID());
				for (MonitoringDTO m : temp) {
					monitorings.add(m);
				}
			}
		}
		MonitoringListReport monitoringReport = new MonitoringListReport(monitorings);
		roc.sendReport(monitoringReport);
	}
}
