package model;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import datasource.MonitoringDownloadGateway;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsRowDataGatewayMock;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Test class to test the UploadLocalMonitoringCommand
 * 
 * @author amy
 */
class TestUploadLocalMonitoringCommand {

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Tests that a monitoringDTO can be sent to the UploadLocalMonitoringCommand
	 * and that it will be written to the database.
	 */
	@Test
	void testUploadPendingClientChangesCommand() {
		// creates a new mock database, gets the next id, and creates the data to go
		// into the new DTO
		MonitoringsMockDatabase mock = MonitoringsMockDatabase.getSingleton();
		List<MonitoringDTO> localMonitorings = new ArrayList<>();
		int numberOfMonitorings = 3;

		int monitoringID = mock.getAutoIncrementMax() + 1;
		LocalDateTime date = LocalDateTime.now();
		Client client = new Client("Amy Jones", 3);
		CaseWorker caseworker = new CaseWorker("Greg Black", 3);
		List<String> questions = Arrays.asList("Question 1", "Question 2");
		List<String> answers = Arrays.asList("Answer 1", "Answer 2");
		MonitoringStatusEnum status = MonitoringStatusEnum.SCHEDULED;
		int version = 2;
		boolean isLocal = true;
		boolean isDownloaded = true;

		// uses the above info to create 3 monitorings into the mock database. This
		// gives
		// the test a remote version number to check against
		MonitoringsRowDataGatewayMock gateway;
		for (int i = 0; i < numberOfMonitorings; i++) {
			gateway = new MonitoringsRowDataGatewayMock(null, date, null, client.getClientID(),
					caseworker.getCaseWorkerID(), questions, answers, status, version, isLocal, isDownloaded);
		}

		// creates a new list of DTOs with the above info to send to the
		// UploadLocalMonitoringCommand
		for (int i = 0; i < numberOfMonitorings; i++) {
			localMonitorings.add(new MonitoringDTO(monitoringID + i, date, date, null, client, caseworker, questions,
					answers, status, 2, isLocal, isDownloaded));
		}

		// changes the editable fields of the local DTO to ensure that the info in the
		// mock database is getting updated with the local info
		List<String> newAnswers = Arrays.asList("New Answer 1", "New Answer 2");
		for (MonitoringDTO m : localMonitorings) {
			m.setAnswers(newAnswers);
			m.setStatus(MonitoringStatusEnum.PENDING_FOR_REVIEW);
		}

		// sets the version check to false to ignore version checking for this test
		Boolean flag = false;

		// calls the UploadLocalMonitoringCommand
		UploadLocalMonitoringCommand command = new UploadLocalMonitoringCommand(localMonitorings, flag);
		command.execute();

		// creates a new gateway and checks that the fields in the mock database match
		// the updated fields of the local DTO
		MonitoringsRowDataGatewayMock rowMock;

		int count = 0;
		for (MonitoringDTO m : localMonitorings) {
			rowMock = new MonitoringsRowDataGatewayMock(monitoringID + count);
			assertNotNull(rowMock.getVisitDate());
			assertEquals(rowMock.getAnswers(), m.getAnswers());
			assertEquals(rowMock.getStatus(), m.getStatus());
			assertEquals(rowMock.getVersion(), m.getVersion() + 1);
			count++;
		}
	}

}
