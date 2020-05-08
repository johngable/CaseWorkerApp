package datasource;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import sharedData.MonitoringStatusEnum;

/**
 * Test the mock version of the gateway
 * @author merlin
 *
 */
public class TestMonitoringsRowDataGatewayMock extends TestMonitoringsRowDataGateway {

	/** 
	 * make sure the mock database is set up for each test
	 */
	@BeforeEach
	public void setup()
	{
		MonitoringsMockDatabase.resetSingleton();
	}

	@Override
	MonitoringsRowDataGateway getCreateGateway(LocalDateTime visitDate, LocalDateTime dueDate,
			LocalDateTime approvedDate, int clientID, int caseWorkerID, List<String> questions, List<String> answers,
			MonitoringStatusEnum status, int version, boolean isDownloaded) {
		return new MonitoringsRowDataGatewayMock(visitDate, dueDate, approvedDate, clientID, caseWorkerID, questions, answers,
				status, version, false, false);
	}

	@Override
	MonitoringsRowDataGateway getRetrieveGateway(int monitoringID) {
		return new MonitoringsRowDataGatewayMock(monitoringID);
	}
}
