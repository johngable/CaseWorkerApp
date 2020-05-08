package model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.CaseWorkerEnum;
import datasource.MonitoringsEnum;
import datasource.MonitoringsMockDatabase;
import datasource.SupervisorEnum;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Tests the CreateNewMonitoringByDateCommand NEED TO BE REFACTORED ONCE
 * DATASOURCE IS UPDATED
 * 
 * @author amy
 */
class TestCreateNewMonitoringByDateCommand implements ReportObserver {

	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Singleton Report Observer Connector

	LocalDateTime dueDate = MonitoringsEnum.formatDate(0, 0);
	int clientID = 1;
	int caseWorkerID = 4;
	boolean gotReport;

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Tests the CreateNewMonitoringByDateCommand that allows supervisor to schedule
	 * a new monitoring and checks that it inserts the new monitoring and receives a
	 * report.
	 */
	@Test
	void testCreateBySupervisorConstructor() {
		roc.registerObserver(this, MonitoringListReport.class);
		for (SupervisorEnum s : SupervisorEnum.values()) {
			gotReport = false;

			// create new
			CreateNewMonitoringByDateCommand insertMonitoring = new CreateNewMonitoringByDateCommand(dueDate, clientID,
					caseWorkerID, s.getID());
			insertMonitoring.execute();
			// get all
			GetMonitoringsBySupervisorCommand getMonitoringsBySupervisor = new GetMonitoringsBySupervisorCommand(
					s.getID());
			getMonitoringsBySupervisor.execute();

			// verify we got the report
			assertTrue(gotReport);
		}
	}

	/**
	 * Tests the CreateNewMonitoringByDateCommand that allows caseworker to schedule
	 * a new monitoring and checks that it inserts the new monitoring and receives a
	 * report.
	 */
	@Test
	void testCreateByCaseWorkerConstructor() {
		roc.registerObserver(this, MonitoringListReport.class);
		for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
			gotReport = false;

			// create new
			CreateNewMonitoringByDateCommand insertMonitoring = new CreateNewMonitoringByDateCommand(dueDate, clientID,
					cw.getCaseWorkerID());
			insertMonitoring.execute();
			// get all
			GetMonitoringsByCaseWorkerCommand getMonitoringsByCaseWorker = new GetMonitoringsByCaseWorkerCommand(
					cw.getCaseWorkerID());
			getMonitoringsByCaseWorker.execute();

			// verify we got the report
			assertTrue(gotReport);
		}
	}

	/**
	 * Receives a Monitoring List Report if one was sent
	 */
	@Override
	public void receiveReport(Report report) {
		if (report.getClass() == MonitoringListReport.class) {
			gotReport = true;
			MonitoringListReport newReport = (MonitoringListReport) report;
			for (MonitoringDTO o : newReport.getMonitorings()) {
				// if monitoring matches parameters we passed in when creating
				if ((o.getDueDate().equals(dueDate)) && o.getClient().getClientID() == clientID
						&& (o.getStatus().equals(MonitoringStatusEnum.SCHEDULED))) {

					// these values should be null if the monitoring was just created
					assertNull(o.getVisitDate());
					assertNull(o.getApprovedDate());
					assertEquals(o.getAnswers().get(0), "");
					assertEquals(o.getAnswers().get(1), "");
				}
			}
		}
	}
}
