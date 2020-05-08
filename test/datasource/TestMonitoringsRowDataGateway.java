package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.OptionsManager;
import sharedData.MonitoringStatusEnum;

/**
 * Test class to test getting the client answers
 * 
 * @author Ian
 *
 */
abstract class TestMonitoringsRowDataGateway extends DatabaseTest {

	// values from existing monitoring
	abstract MonitoringsRowDataGateway getCreateGateway(LocalDateTime visitDate, LocalDateTime dueDate,
			LocalDateTime approvedDate, int clientID, int caseWorkerID, List<String> questions, List<String> answers,
			MonitoringStatusEnum status, int version, boolean isDownloaded);

	// get values from existing monitoring
	abstract MonitoringsRowDataGateway getRetrieveGateway(int monitoringID);

	/**
	 * Test to ensure that we can fill a complete row in the monitorings table
	 */
	@Test
	void testCreatorConstructor() {
		List<String> answers = Arrays.asList("answer 1", "answer 2");
		List<String> questions = Arrays.asList("question 1", "question 2");
		LocalDateTime dueDate = MonitoringsEnum.formatDate(0, 0);

		MonitoringsRowDataGateway gateway = getCreateGateway(null, dueDate, null, 1, 4, questions, answers,
				MonitoringStatusEnum.SCHEDULED, 1, false);

		int monitoringID = gateway.getAutoIncrementMax(); // monitoringID we created
		MonitoringsRowDataGateway retreived = getRetrieveGateway(monitoringID);

		assertEquals(retreived.getAnswers(), answers);
		assertEquals(retreived.getQuestions(), questions);
		assertEquals(retreived.getDueDate(), dueDate);
		assertEquals(retreived.getStatus(), MonitoringStatusEnum.SCHEDULED);
		assertEquals(retreived.getVersion(), 1);
	}

	/**
	 * Tests to see if it successfully gets the correct information for a specific
	 * id.
	 */
	@Test
	void testGetMonitorings() {
		// if using RDS test, we need to run table runner to reset to known state
		if (!OptionsManager.getSingleton().isUsingMockDataSource()) {
			MonitoringsTableRunner runner = new MonitoringsTableRunner();
			runner.main(null);
		}
		for (MonitoringsEnum monitoring : MonitoringsEnum.values()) {
			MonitoringsRowDataGateway gateway = getRetrieveGateway(monitoring.getMonitoringID());
			assertEquals(monitoring.getMonitoringID(), gateway.getMonitoringID());
			assertEquals(monitoring.getVisitDate(), gateway.getVisitDate());
			assertEquals(monitoring.getDueDate(), gateway.getDueDate());
			assertEquals(monitoring.getApprovedDate(), gateway.getApprovedDate());
			assertEquals(monitoring.getClientId(), gateway.getClient().getClientID());
			assertEquals(monitoring.getCaseWorkerID(), gateway.getCaseworker().getCaseWorkerID());
			assertEquals(monitoring.getQuestions(), gateway.getQuestions());
			assertEquals(monitoring.getClientAnswers(), gateway.getAnswers());
			assertEquals(monitoring.getVersion(), gateway.getVersion());
		}

	}

	/**
	 * Tests that monitoring answers can be modified
	 */
	@Test
	void testSetAnswers() {
		// if using RDS test, we need to run table runner to reset to known state
		if (!OptionsManager.getSingleton().isUsingMockDataSource()) {
			MonitoringsTableRunner runner = new MonitoringsTableRunner();
			runner.main(null);
		}

		List<String> newAnswers = Arrays.asList("updated answer", "updated answer 2");
		for (MonitoringsEnum monitoring : MonitoringsEnum.values()) {
			MonitoringsRowDataGateway gateway = getRetrieveGateway(monitoring.getMonitoringID());
			// before the change - new answers will not match what is currently stored
			assertNotEquals(newAnswers, gateway.getAnswers());
			int testVersion = gateway.getVersion() + 1;
			// update the new answers
			gateway.setAnswers(newAnswers);
			// now the new answers will match what the monitoring holds
			assertEquals(newAnswers, gateway.getAnswers());
			assertEquals(testVersion, gateway.getVersion());
		}
	}

	/**
	 * Test that the status can be set and the version is incremented
	 */
	@Test
	void testSetStatus() {
		// if using RDS test, we need to run table runner to reset to known state
		if (!OptionsManager.getSingleton().isUsingMockDataSource()) {
			MonitoringsTableRunner runner = new MonitoringsTableRunner();
			runner.main(null);
		}

		for (MonitoringsEnum monitoring : MonitoringsEnum.values()) {
			MonitoringsRowDataGateway gateway = getRetrieveGateway(monitoring.getMonitoringID());

			// getting a status that isn't the current one
			MonitoringStatusEnum newStatus = gateway.getStatus();
			for (MonitoringStatusEnum s : MonitoringStatusEnum.values()) {
				if (newStatus != s) {
					newStatus = s;
					break;
				}
			}

			assertNotEquals(newStatus, gateway.getStatus());
			int testVersion = gateway.getVersion() + 1;

			gateway.setStatus(newStatus);

			assertEquals(newStatus, gateway.getStatus());
			assertEquals(testVersion, gateway.getVersion());
		}
	}
	
	/**
	 * Test that the visit date and time can be set
	 */
	@Test
	void testSetVisitDateTime() {
		// if using RDS test, we need to run table runner to reset to known state
		if (!OptionsManager.getSingleton().isUsingMockDataSource()) {
			MonitoringsTableRunner runner = new MonitoringsTableRunner();
			runner.main(null);
		}

		for (MonitoringsEnum monitoring : MonitoringsEnum.values()) {
			MonitoringsRowDataGateway gateway = getRetrieveGateway(monitoring.getMonitoringID());
			
			LocalDateTime date = LocalDateTime.now();
			date = date.withNano(0);

			int testVersion = gateway.getVersion() + 1;
			
			gateway.setVisitDateTime(date);
			
			assertEquals(date, gateway.getVisitDate());
			assertEquals(testVersion, gateway.getVersion());
		}
	}
	
}
