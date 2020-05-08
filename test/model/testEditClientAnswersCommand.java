package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.ClientEnum;
import datasource.MonitoringsEnum;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import reports.ClientAnswerReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringStatusEnum;

/**
 * Tests the EditClientAnswersCommand to ensure all observers receive the
 * reports generated and each report contains valid information
 * 
 * @author michaelpermyashkin
 *
 */
class testEditClientAnswersCommand implements ReportObserver {

	// singleton used by test class to register and observe ClientListReport
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	// list holds the list of client answers returned from Data source
	private List<String> expected = new ArrayList<>();
	
	private boolean reportRecieved; // true if we successfully got report

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}
	
	/**
	 * Registers the test as an observer of the client answer report, executes the
	 * editClientAnswers command and asserts the report is correct
	 */
	@Test
	void test() {
		roc.registerObserver(this, ClientAnswerReport.class);
		expected.add("Some updated answer 1...");
		expected.add("Some updated answer 2...");
		// for every monitoring we update the answers 
		for (MonitoringsEnum m : MonitoringsEnum.values()) {
			reportRecieved = false;
			// set new answers
			Client client = null;
			for (ClientEnum c : ClientEnum.values()) {
				if (c.getClientID() == m.getClientId()) {
					client = new Client(c.getClientName(), c.getClientID());
				}
			}
			EditClientAnswersCommand cmd = new EditClientAnswersCommand(m.getMonitoringID(), client, expected, m.getCaseWorkerID());
			cmd.execute();

			// assert we got a report after command executed
			assertTrue(reportRecieved);
			
			MonitoringsRowDataGatewayMock rowMock = new MonitoringsRowDataGatewayMock(m.getMonitoringID());
			// check DB has correct updated answers
			assertEquals(expected, rowMock.getAnswers());
			// after edit, monitoring should now be sent to supervisor for review
			assertEquals(MonitoringStatusEnum.PENDING_FOR_REVIEW, rowMock.getStatus());
		}
	}

	/**
	 * This method is added since the test is made an observer of a report.
	 * 
	 * When a report is received, the list of answers is saved to an instance
	 * variable and compared to the answers that were given to the command to update
	 */
	@Override
	public void receiveReport(Report report) {
		if (report.getClass() == ClientAnswerReport.class) {
			ClientAnswerReport answerReport = (ClientAnswerReport) report;
			try {
				reportRecieved = true;
				List<String> recieved = answerReport.getAnswers();
				// check that the answers were updated
				assertEquals(expected, recieved);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
