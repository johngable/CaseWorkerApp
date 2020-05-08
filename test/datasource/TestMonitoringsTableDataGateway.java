package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Tests for the MonitoringsTableDataGateway
 * 
 * @author michaelpermyashkin
 *
 */
abstract class TestMonitoringsTableDataGateway extends DatabaseTest {

	public abstract MonitoringsTableDataGateway getGateway();

	/**
	 * Verifies that we can retrieve monitorings by the client ID they are
	 * associated with
	 */
	@Test
	void testByClientIds() {
		MonitoringsTableDataGateway gateway = getGateway();
		for (ClientEnum c : ClientEnum.values()) {
			List<MonitoringDTO> actual = gateway.getMonitoringsByClientId(-1, c.getClientID());
			if (actual != null) {
				for (MonitoringDTO m : actual) {
					if (m.getClient() != null) {
						assertEquals(c.getClientID(), m.getClient().getClientID());
					}

				}
			}
		}
	}

	/**
	 * Verifies that we can retrieve monitorings by their status
	 */
	@Test
	void testByStatus() {
		MonitoringsTableDataGateway gateway = getGateway();
		for (MonitoringStatusEnum s : MonitoringStatusEnum.values()) {
			List<MonitoringDTO> actual = gateway.getMonitoringsByStatus(-1, s);
			if (actual != null) {
				for (MonitoringDTO m : actual) {
					assertEquals(s, m.getStatus());
				}
			}
		}
	}

	/**
	 * Verifies that we can retrieve monitorings by the client ID they are
	 * associated with
	 */
	@Test
	void testByCaswWorkerIds() {
		MonitoringsTableDataGateway gateway = getGateway();
		for (CaseWorkerEnum cw : CaseWorkerEnum.values()) {
			List<MonitoringDTO> actual = gateway.getMonitoringsByCaseWorkerID(cw.getCaseWorkerID());
			if (actual != null) {
				for (MonitoringDTO m : actual) {
					if (m.getCaseWorker() != null) {
						assertEquals(cw.getCaseWorkerID(), m.getCaseWorker().getCaseWorkerID());
					}
				}
			}
		}
	}

//	/**
//	 * Tests that we can pass in a date and we will get all monitorings for a given
//	 * caseWorker that the visit date occurred on or after that date
//	 */
//	@Test
//	void testSelectMonitoringByDate() {
//		MonitoringsTableDataGateway gateway = getGateway();
//		LocalDate requestedDate = LocalDate.now().minusMonths(1);
//		for (ClientEnum c : ClientEnum.values()) {
//			List<MonitoringDTO> monitorings = gateway.getMonitoringsByDate(-1, requestedDate, c.getClientID());
//			for (MonitoringDTO m : monitorings) {
//				if (m.getVisitDate() != null) {
//					assertEquals(m.getClient().getClientID(), c.getClientID());
//					assertTrue(m.getVisitDate().toLocalDate().isEqual(requestedDate) || m.getVisitDate().toLocalDate().isAfter(requestedDate));
//				}
//			}
//		}
//	}
}
