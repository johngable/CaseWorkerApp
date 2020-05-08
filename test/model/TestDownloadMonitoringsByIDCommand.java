package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.MonitoringDownloadGateway;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsRowDataGatewayMock;
import sharedData.MonitoringDTO;

/**
 * Test the command which downloads monitoring for a given list of monitoringIDs
 * 
 * @author michaelpermyashkin
 *
 */
class TestDownloadMonitoringsByIDCommand {

	private MonitoringsRowDataGatewayMock rowMock;

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Tests that we can download monitorings for a list of monitoring IDs. Once
	 * downloaded we assert that we are able to read the downloaded file and the
	 * values match what we went to be downloaded
	 */
	@Test
	void testDownloadMonitoringsByIDCommand() {
		List<Integer> monitoringIDsToDownload = Arrays.asList(1, 2);
		DownloadMonitoringsByIDCommand cmd = new DownloadMonitoringsByIDCommand(monitoringIDsToDownload, 3);
		cmd.execute();

		for (int id : monitoringIDsToDownload) {
			rowMock = new MonitoringsRowDataGatewayMock(id);
			MonitoringDTO expected = rowMock.getMonitoringForDownload();

			
			
			// call method to search the file for the monitoringID
			MonitoringDTO recieved = MonitoringDownloadGateway
					.getMonitoringDataFromFileByID(expected.getMonitoringID(), expected.getClient().getClientName());

			// assert we found the right monitoring by comparing ID's
			assertEquals(expected.getMonitoringID(), recieved.getMonitoringID());
			// visit date
			assertEquals(expected.getVisitDate(), recieved.getVisitDate());
			// due date
			assertEquals(expected.getDueDate(), recieved.getDueDate());
			// approved date
			assertEquals(expected.getApprovedDate(), recieved.getApprovedDate());
			// caseworker
			assertTrue(recieved.getCaseWorker().equals(expected.getCaseWorker()));
			// Client
			assertTrue(recieved.getClient().equals(expected.getClient()));
			// status
			assertEquals(expected.getStatus(), recieved.getStatus());
		}
	}
}
