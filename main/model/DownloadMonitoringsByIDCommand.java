package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import datasource.MonitoringDownloadGateway;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import reports.DownloadFailedReport;
import reports.DownloadSuccessReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Command to get monitorings that were done up to a month ago by a caseworker
 * and download this list
 * 
 * @author michaelpermyashkin
 *
 */
public class DownloadMonitoringsByIDCommand implements Command {
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Singleton Report Observer Connector
	private List<MonitoringDTO> monitoringsToDownload = new ArrayList<>();
	private MonitoringDownloadGateway downloadGateway;
	private Client client;
	private int caseWorkerID;
	private List<Integer> monitoringIDs;

	/**
	 * Constructor takes ID of caseworker requesting download
	 * @param caseWorkerID
	 */
	public DownloadMonitoringsByIDCommand(List<Integer> monitoringIDs, int caseWorkerID) {
		this.monitoringIDs = monitoringIDs;
		this.caseWorkerID = caseWorkerID;
	}

	/**
	 * - Gets date for one month ago
	 * - Gets monitorings where visit date is on or after the date a month ago
	 * - Downloads monitoring list
	 * 
	 */
	@Override
	public void execute() {
		// gets gateway based on mode - TEST or PRODUCTION
		MonitoringsRowDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			for (int id : monitoringIDs) {
				monitoringsGateway = new MonitoringsRowDataGatewayMock(id);
				monitoringsToDownload.add(monitoringsGateway.getMonitoringForDownload());
			}
			
		} else {
			for (int id : monitoringIDs) {
				monitoringsGateway = new MonitoringsRowDataGatewayRDS(id);
				monitoringsToDownload.add(monitoringsGateway.getMonitoringForDownload());
			}
		}

		// Download monitorings
		try {
			downloadGateway = new MonitoringDownloadGateway(monitoringsToDownload);
			roc.sendReport(new DownloadSuccessReport());
			remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			roc.sendReport(new DownloadFailedReport());
		}
	}
}
