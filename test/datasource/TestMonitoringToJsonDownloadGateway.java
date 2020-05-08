package datasource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.CaseWorker;
import model.Client;
import model.OptionsManager;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Tests the downloading gateway used to download monitorings
 * 
 * @author michaelpermyashkin
 *
 */
class TestMonitoringToJsonDownloadGateway {
	private List<MonitoringDTO> monitorings = new ArrayList<>();

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}
	
	/**
	 * Tests that we can download a monitoringDTO list and are able to extract the
	 * values from the json file
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	void testDownloadGateway() throws IOException, ParseException {
		int monitoringID1 = 999;
		Client client = new Client("client", 1);
		CaseWorker caseWorker = new CaseWorker("caseworker", 2);
		LocalDateTime visit = MonitoringsEnum.formatDate(0, 0);
		LocalDateTime due = MonitoringsEnum.formatDate(0, 0);
		LocalDateTime approved = MonitoringsEnum.formatDate(0, 0);
		List<String> questions = new ArrayList<>(Arrays.asList("This is question 1", "This is question 2"));
		List<String> answers = new ArrayList<>(Arrays.asList("answer 1", "answer 2"));
		MonitoringStatusEnum status = MonitoringStatusEnum.PENDING_FOR_CORRECTION;
		int version = 1;

		MonitoringDTO monitoring1 = new MonitoringDTO(monitoringID1, visit, due, approved, client, caseWorker,
				questions, answers, status, version, false, true);
		monitorings.add(monitoring1);

		// constructor takes list of monitorings to download
		MonitoringDownloadGateway gateway = new MonitoringDownloadGateway(monitorings);
		MonitoringDTO downloadedMonitoring1 = MonitoringDownloadGateway.getMonitoringDataFromFileByID(monitoringID1, client.getClientName());

		assertEquals(monitoringID1, downloadedMonitoring1.getMonitoringID());

		// assert that remaining fields are the same as original monitoring values
		assertEquals(visit, downloadedMonitoring1.getVisitDate());
		assertEquals(due, downloadedMonitoring1.getDueDate());
		assertEquals(approved, downloadedMonitoring1.getApprovedDate());

		assertTrue(downloadedMonitoring1.getCaseWorker().equals(caseWorker));

		assertTrue(downloadedMonitoring1.getClient().equals(client));

		assertEquals(status, downloadedMonitoring1.getStatus());
	}
}
