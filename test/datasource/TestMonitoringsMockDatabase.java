package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * 
 * @author merlin
 *
 */
public class TestMonitoringsMockDatabase {

	/**
	 * reinitialize the data source before each test
	 */
	@BeforeEach
	public void setup() {
		MonitoringsMockDatabase x = MonitoringsMockDatabase.getSingleton();
	}

	/**
	 * Make sure we can retrieve all of the monitorings for a given client id
	 */
	@Test
	public void selectByClientID() {
		MonitoringsMockDatabase ds = MonitoringsMockDatabase.getSingleton();
		List<MonitoringDTO> results = ds.selectStarByClientID(1);

		for (MonitoringDTO m : results) {
			assertEquals(1, m.getClient().getClientID());
		}
	}

	/**
	 * Make sure we can retrieve a monitoring using its unique id
	 */
	@Test
	public void selectByMonitoringID() {
		MonitoringsMockDatabase ds = MonitoringsMockDatabase.getSingleton();
		MonitoringDTO results = ds.selectStarByMonitoringID(1);
		assertEquals(results.getMonitoringID(), MonitoringsEnum.Client1Monitoring.getMonitoringID());
	}

	/**
	 * Make sure we can change a monitoring's answers
	 */
	@Test
	public void updateAnswers() {
		MonitoringsMockDatabase ds = MonitoringsMockDatabase.getSingleton();

		List<String> newAnswers = Arrays.asList("another ridiculous answer", "silly answer again");

		MonitoringDTO before = ds.selectStarByMonitoringID(1);
		assertNotEquals(before.getAnswers(), newAnswers);

		ds.updateAnswersByMonitoringID(1, newAnswers);

		MonitoringDTO after = ds.selectStarByMonitoringID(1);
		assertEquals(after.getAnswers(), newAnswers);
	}

	/**
	 * Make sure we can retrieve a monitoring using its unique id
	 */
	@Test
	public void selectByMonitoringStatus() {
		MonitoringsMockDatabase ds = MonitoringsMockDatabase.getSingleton();
		for (MonitoringStatusEnum status : MonitoringStatusEnum.values()) {
			List<MonitoringDTO> recieved = ds.selectStarByStatus(status);

			if (recieved != null) {
				for (MonitoringDTO m : recieved) {
					assertEquals(status, m.getStatus());
				}
			}
		}
	}
}
