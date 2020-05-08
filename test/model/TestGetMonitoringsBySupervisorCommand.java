package model;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;

import datasource.CaseWorkerEnum;
import datasource.CaseWorkerTableDataGateway;
import datasource.ClientTableDataGateway;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import datasource.SupervisorEnum;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Tests GetMonitoringsBySupervisorCommand
 * 
 * @author John G
 *
 */
class TestGetMonitoringsBySupervisorCommand implements ReportObserver {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	private List<MonitoringDTO> reportResponse;
	private GetMonitoringsBySupervisorCommand command;

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Tests that we can get all monitorings for every supervisor using their ID
	 */
	@Test
	void testExecute() {
		roc.registerObserver(this, MonitoringListReport.class);
		MonitoringsTableDataGatewayMock tableMock = new MonitoringsTableDataGatewayMock();

		// test commmand for every supervisor
		for (SupervisorEnum s : SupervisorEnum.values()) {
			// execute command
			command = new GetMonitoringsBySupervisorCommand(s.getID());
			command.execute();

			// using all supervisors caseworkers, get all monitorings
			List<MonitoringDTO> expectedListForSupervisor = new ArrayList<>();
			for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
				if (cw.getSupervisorID() == s.getID()) {
					expectedListForSupervisor.addAll(tableMock.getMonitoringsByCaseWorkerID(cw.getCaseWorkerID()));
				}
			}
			// check that list in report matches monitoring list we expect
			assertThat("Failed", reportResponse, is(expectedListForSupervisor));
		}
	}

	/**
	 * Report recieved holds list of monitorings for a supervisor
	 */
	public void receiveReport(Report report) {
		if (report.getClass() == MonitoringListReport.class) {
			MonitoringListReport answer = (MonitoringListReport) report;
			reportResponse = answer.getMonitorings();
		}
	}
}
