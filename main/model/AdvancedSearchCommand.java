package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayFactory;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import reports.MonitoringListReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Advanced Search Command. This command can take in a variety of material to
 * filter by, (some can be left out) and tells the datasource what to retrieve.
 * 
 * @author johngable
 *
 */
public class AdvancedSearchCommand implements Command {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); 
	private Client client; 
	private MonitoringStatusEnum status; 
	private LocalDateTime dateFrom; 
	private LocalDateTime dateTo; 
	private String dateType; 
	private int filters; // bit count to determine what filters are being used
	private int caseWorkerID = -1; // caseworkerID to search by for when a caseworker is logged in

	/**
	 * Main constructor for supervisors logged in that takes no caseworker id's
	 * @param client - Client to search by
	 * @param status - status to search by
	 * @param dateFrom - date to reach back to
	 * @param dateTo - date to go up to
	 * @param dateType - what type of date to search by (visit, due, etc)
	 */
	public AdvancedSearchCommand(Client client, MonitoringStatusEnum status, LocalDateTime dateFrom,
			LocalDateTime dateTo, String dateType) {

		if (client != null) {
			this.client = client;
			filters++;
		}
		if (status != null) {
			this.status = status;
			filters += 2;
		}
		if (dateFrom != null & dateTo != null) {
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			filters += 4;
		}
		if (dateType != null) {
			this.dateType = dateType;
		}

	}

	/**
	 * Main constructor for caseworkers logged in
	 * @param caseWorkerID - ID of caseworker to search by
	 * @param client - Client to search by
	 * @param status - status to search by
	 * @param dateFrom - date to reach back to
	 * @param dateTo - date to go up to
	 * @param dateType - what type of date to search by (visit, due, etc)
	 */
	public AdvancedSearchCommand(int caseWorkerID, Client client, MonitoringStatusEnum status, LocalDateTime dateFrom,
			LocalDateTime dateTo, String dateType) {
		this.caseWorkerID = caseWorkerID;
		if (client != null) {
			this.client = client;
			filters++;
		}
		if (status != null) {
			this.status = status;
			filters += 2;
		}
		if (dateFrom != null & dateTo != null) {
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			filters += 4;
		}
		if (dateType != null) {
			this.dateType = dateType;
		}

	}

	/**
	 * Checks the bits of filters and determines which datasource
	 * method must be called with the associated information. 
	 */
	@Override
	public void execute() {

		MonitoringsTableDataGateway gateway = MonitoringsTableDataGatewayFactory.getGateway();

		List<MonitoringDTO> monitorings = new ArrayList<>();

		switch (filters) {
		case 1:
			monitorings = gateway.getMonitoringsByClientId(caseWorkerID, client.getClientID());
			break;
		case 2:
			monitorings = gateway.getMonitoringsByStatus(caseWorkerID, status);
			break;
		case 3:
			monitorings = gateway.filterByNameStatus(caseWorkerID, client, status);
			break;
		case 4:
			monitorings = gateway.getMonitoringsByDate(caseWorkerID, dateFrom.toLocalDate(), dateTo.toLocalDate(),
					dateType);
			break;
		case 5:
			monitorings = gateway.filterByNameDate(caseWorkerID, client, dateFrom.toLocalDate(), dateTo.toLocalDate(),
					dateType);
			break;
		case 6:
			monitorings = gateway.filterByStatusDate(caseWorkerID, status, dateFrom.toLocalDate(), dateTo.toLocalDate(),
					dateType);
			break;
		case 7:
			monitorings = gateway.filterByNameStatusDate(caseWorkerID, client, status, dateFrom.toLocalDate(),
					dateTo.toLocalDate(), dateType);
			break;
		}

		MonitoringListReport report = new MonitoringListReport(monitorings);
		roc.sendReport(report);

	}

}
