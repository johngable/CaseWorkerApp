package model;

import java.util.ArrayList;
import java.util.List;

import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayFactory;
import reports.DownloadedClientListReport;
import reports.ReportObserverConnector;

/**
 * Gets a list of clients who have data that has been downloaded and not yet
 * reuploaded
 * 
 * @author Tehmi Lowe
 *
 */
public class GetDownloadedClientsCommand implements Command {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();

	/**
	 * Gets gateway from factory and calls method to get all clients who currently
	 * have data downloaded Sends a DownloadedClientListReport with the list
	 * returned
	 */
	@Override
	public void execute() {
		MonitoringsTableDataGateway gateway = MonitoringsTableDataGatewayFactory.getGateway();
		List<Client> clients = gateway.getDownloadedClients();
		
		DownloadedClientListReport report = new DownloadedClientListReport(clients);
		roc.sendReport(report);
	}
}
