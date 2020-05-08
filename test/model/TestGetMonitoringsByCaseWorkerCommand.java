package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.CaseWorkerEnum;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsTableDataGatewayMock;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Tests GetMonitoringsByCaseWorkerCommand
 * 
 * @author Michael Umbelina
 *
 */
class TestGetMonitoringsByCaseWorkerCommand implements ReportObserver {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	private List<MonitoringDTO> recieved;

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Test the execute method to see if it returns the list of monitorings for a
	 * caseworker
	 */
	@Test
	void testExecute() {
		roc.registerObserver(this, MonitoringListReport.class);
		for (CaseWorkerEnum c : CaseWorkerEnum.values()) {
			recieved = null;
			GetMonitoringsByCaseWorkerCommand command = new GetMonitoringsByCaseWorkerCommand(c.getCaseWorkerID());
			command.execute();

			MonitoringsTableDataGatewayMock tableMock = new MonitoringsTableDataGatewayMock();
			List<MonitoringDTO> expected = tableMock.getMonitoringsByCaseWorkerID(c.getCaseWorkerID());
			assertEquals(expected, recieved);
		}
	}

	/**
	 * Receives the report from the command
	 */
	@Override
	public void receiveReport(Report report) {
		if (report.getClass() == MonitoringListReport.class) {
			MonitoringListReport answer = (MonitoringListReport) report;
			recieved = answer.getMonitorings();
		}
	}

}
