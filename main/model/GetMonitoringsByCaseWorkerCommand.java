package model;

import java.util.ArrayList;
import java.util.List;

import datasource.ClientEnum;
import datasource.ClientTableDataGateway;
import datasource.MonitoringDownloadGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayFactory;
import datasource.MonitoringsTableDataGatewayMock;
import datasource.MonitoringsTableDataGatewayRDS;
import reports.MonitoringListReport;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;

/**
 * Executes command to get the monitoring by various parameters as needed
 * 
 * @author Michael Permyashkin
 */
public class GetMonitoringsByCaseWorkerCommand implements Command {
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Used to send report
	private List<MonitoringDTO> monitoringsList = new ArrayList<>();
	private int caseWorkerID;

	/**
	 * Create the command
	 * 
	 * @param caseworkerID - the id of the caseworker whose monitorings you want
	 */
	public GetMonitoringsByCaseWorkerCommand(int caseWorkerID) {
		this.caseWorkerID = caseWorkerID;
	}

	/**
	 * Execute command to get all the monitorings for a specific caseworker
	 */
	@Override
	public void execute() {
		// Gets the list of clients for each caseworker
		ClientTableDataGateway clientGateway = new ClientTableDataGateway();
		ArrayList<Integer> clientIDList = new ArrayList<Integer>();
		clientIDList.addAll(clientGateway.getClientListID(caseWorkerID));

		// Gets the list of clients from the clientID list
		List<Client> clientList = new ArrayList<>();
		for (Integer clientID : clientIDList) {
			Client client = null;
			for (ClientEnum c : ClientEnum.values()) {
				if (c.getClientID() == clientID) {
					client = new Client(c.getClientName(), c.getClientID());
					clientList.add(client);
				}
			}
		}
		
		// Pulls monitorings from database first
		MonitoringsTableDataGateway gateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			gateway = new MonitoringsTableDataGatewayMock();
		} else {
			gateway = new MonitoringsTableDataGatewayRDS();
		}
		// gets all monitorings from DB for given caseworker
		List<MonitoringDTO> monitoringsDB = gateway.getMonitoringsByCaseWorkerID(caseWorkerID);

		// check if we have a local version of monitoring, if yes then replace with local version
		
		for (int i = 0; i < monitoringsDB.size(); i++) {
			int monitoringID = monitoringsDB.get(i).getMonitoringID();
			String clientName = monitoringsDB.get(i).getClient().getClientName();
			MonitoringDTO inFile = MonitoringDownloadGateway.getMonitoringDataFromFileByID(monitoringID, clientName);
			if (inFile != null) {
				inFile.setIsLocal(true);
				monitoringsList.add(inFile);
			} else {
				monitoringsList.add(monitoringsDB.get(i));
			}
		}

		MonitoringListReport monitoringReport = new MonitoringListReport(monitoringsList);
		roc.sendReport(monitoringReport);

		//remote.queueCommand(new GetLocallyDownloadedClientsCommand(caseWorkerID));
	}

	/**
	 * @return List<MonitoringDTO> - the list of monitorings
	 */
	public List<MonitoringDTO> getMonitorings() {
		return monitoringsList;
	}
}
