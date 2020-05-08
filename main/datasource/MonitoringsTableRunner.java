package datasource;

/**
 * Runner class that rebuilds the monitoring table using the monitoring values
 * in the MonitoringsEnum
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsTableRunner {

	static MonitoringsTableDataGatewayRDS tableRDS = new MonitoringsTableDataGatewayRDS(); // MonitoringsTableRDS
	static MonitoringsRowDataGatewayRDS rowRDS; // MonitoringsRowRDS

	public static void main(String[] args) {
		// Drop and Create new Monitorings Table}
		tableRDS.initializeMonitoringsTable();

		// Populate Monitorings Table from Enum values via RowRDS
		for (MonitoringsEnum m : MonitoringsEnum.values()) {
			// pass monitoring values into MonitoringRowDataGatewayRDS
			rowRDS = new MonitoringsRowDataGatewayRDS(m.getVisitDate(), m.getDueDate(), m.getApprovedDate(), m.getClientId(), m.getCaseWorkerID(),
					m.getQuestions(), m.getAnswers(), m.getStatus(), m.getVersion(), m.getIsDownloaded());
		}
	}
}
