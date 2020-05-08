package datasource;

import java.time.LocalDateTime;
import java.util.List;

import sharedData.MonitoringStatusEnum;

/**
 * 
 * @author John G.
 *
 */
class TestMonitoringsRowDataGatewayRDS extends TestMonitoringsRowDataGateway {

	/**
	 * Gateway used to retrieve fields of row for a specific monitoring
	 * 
	 */
	@Override
	MonitoringsRowDataGateway getCreateGateway(LocalDateTime visitDate, LocalDateTime dueDate,
			LocalDateTime approvedDate, int clientID, int caseWorkerID, List<String> questions, List<String> answers,
			MonitoringStatusEnum status, int version, boolean isDownloaded) {
		return new MonitoringsRowDataGatewayRDS(visitDate, dueDate, approvedDate, clientID, caseWorkerID, questions, answers,
				status, version, isDownloaded);
	}

	/**
	 * Gateway to create new monitoring
	 */
	@Override
	MonitoringsRowDataGateway getRetrieveGateway(int monitoringID) {
		return new MonitoringsRowDataGatewayRDS(monitoringID);
	}

}
