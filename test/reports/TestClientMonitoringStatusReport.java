package reports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sharedData.MonitoringStatusEnum;

class TestClientMonitoringStatusReport {

	/**
	 * tests if the statusReport contains the right status
	 */
	@Test
	void testClientMonitoringStatusReport() {
		for (MonitoringStatusEnum status : MonitoringStatusEnum.values()) {
			ClientMonitoringStatusReport statusReport = new ClientMonitoringStatusReport(status);
			assertEquals(status, statusReport.getStatus());
		}
	}
}
