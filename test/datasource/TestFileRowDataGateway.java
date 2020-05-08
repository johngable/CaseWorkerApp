package datasource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.CaseWorker;
import model.Client;
import model.OptionsManager;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Tests the FileRowDataGateway
 * 
 * @author michaelpermyashkin
 *
 */
class TestFileRowDataGateway {

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Tests that we can change answers in the local file
	 */
	@Test
	void testFileRowDataGatewaySetAnswers() {
		int monitoringID = 99;
		Client client = new Client("client", 1);
		CaseWorker caseWorker = new CaseWorker("caseworker", 2);
		LocalDateTime visit = MonitoringsEnum.formatDate(0, 6);
		LocalDateTime due = MonitoringsEnum.formatDate(0, 2);
		LocalDateTime approved = MonitoringsEnum.formatDate(0, 0);
		List<String> questions = new ArrayList<>(Arrays.asList("This is question 1", "This is question 2"));
		List<String> answers = new ArrayList<>(Arrays.asList("answer 1", "answer 2"));
		MonitoringStatusEnum status = MonitoringStatusEnum.PENDING_FOR_CORRECTION;
		int version = 1;

		MonitoringDTO monitoring1 = new MonitoringDTO(monitoringID, visit, due, approved, client, caseWorker, questions,
				answers, status, version, false, false);
		List<MonitoringDTO> toDownload = new ArrayList<>();
		toDownload.add(monitoring1);

		// try to download the monitoring and assert that the file has the correct updated answers
		try {
			MonitoringDownloadGateway downloadGW = new MonitoringDownloadGateway(toDownload);
			MonitoringDTO fromFile = MonitoringDownloadGateway.getMonitoringDataFromFileByID(monitoringID, client.getClientName());
			
			assertEquals(fromFile, toDownload.get(0));
			
			//Testing a static method that returns a dto if the file exists
			MonitoringDTO ifExists = FileRowDataGateway.ifFileExists(monitoringID, client.getClientName());
			assertEquals(ifExists, toDownload.get(0));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
