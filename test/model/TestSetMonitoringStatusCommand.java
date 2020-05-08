package model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.ClientEnum;
import datasource.MonitoringsEnum;
import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import reports.ClientMonitoringStatusReport;
import sharedData.MonitoringStatusEnum;

/**
 * 
 * @author amitybrown
 * 
 *         tests that the set monitoring status command actually changes the
 *         status
 */

class TestSetMonitoringStatusCommand {

	@BeforeEach
	void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	@Test
	void testSetMonitoringStatusCommand() {
		for (MonitoringsEnum m : MonitoringsEnum.values()) {
			Client client = null;
			for (ClientEnum c : ClientEnum.values()) {
				if (c.getClientID() == m.getClientId()) {
					client = new Client(c.getClientName(), c.getClientID());
				}
			}
			
			SetMonitoringStatusCommand command = new SetMonitoringStatusCommand(m.getMonitoringID(), client, MonitoringStatusEnum.APPROVED);
			command.execute();
			MonitoringsRowDataGatewayMock rowMock = new MonitoringsRowDataGatewayMock(m.getMonitoringID());
			assertEquals(rowMock.getStatus(), MonitoringStatusEnum.APPROVED);
		}

	}
}