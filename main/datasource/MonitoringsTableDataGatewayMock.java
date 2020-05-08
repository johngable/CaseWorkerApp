package datasource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Table data gateway to select monitorings by their current status
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsTableDataGatewayMock extends MonitoringsTableDataGateway {

	/**
	 * Static function that returns a list of monitoring IDs based on status.
	 * 
	 * @param status - takes a string which is the status by which we would like to
	 *               match
	 * @return List<Integer> - contains clientIDs
	 */
	public List<MonitoringDTO> getMonitoringsByStatus(MonitoringStatusEnum status) {
		return (MonitoringsMockDatabase.getSingleton()).selectStarByStatus(status);
	}

	/**
	 * Returns a list of monitoring ids that match the passed in client id
	 * 
	 * @param clientId an int that is unique for the client
	 * @return a list of monitoring ids specific to that client id
	 */
	public List<MonitoringDTO> getMonitoringsByClientId(int clientId) {
		return (MonitoringsMockDatabase.getSingleton()).selectStarByClientID(clientId);
	}

	/**
	 * Returns a list of monitoring ids that match the passed in client id
	 * 
	 * @param caseWorkerID an int that is unique for the caseworker
	 * @return a list of monitoring ids specific to that caseworker id
	 */
	public List<MonitoringDTO> getMonitoringsByCaseWorkerID(int caseWorkerID) {
		return (MonitoringsMockDatabase.getSingleton()).selectStarByCaseWorkerID(caseWorkerID);
	}

	@Override
	public List<MonitoringDTO> getMonitoringsByClient(Client client) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * List of clients that have downloaded data
	 * 
	 * @return list of clients
	 */
	@Override
	public List<Client> getDownloadedClients() {
		List<MonitoringDTO> downloadedList = (MonitoringsMockDatabase.getSingleton()).selectStarByIsDownload(true);
		List<Client> clientList = new ArrayList<>();
		if (downloadedList != null) {
			for (MonitoringDTO m : downloadedList) {
				if (!clientList.contains(m.getClient())) {
					clientList.add(m.getClient());
				}
			}
			return clientList;
		}
		return clientList;
	}

	@Override
	public List<MonitoringDTO> filterByNameStatus(int caseWorkerID, Client client, MonitoringStatusEnum status) {
		List<MonitoringDTO> nameList = MonitoringsMockDatabase.getSingleton()
				.selectStarByClientID(client.getClientID());
		List<MonitoringDTO> statusList = MonitoringsMockDatabase.getSingleton().selectStarByStatus(status);
		List<MonitoringDTO> completed = new ArrayList<>();
		for (MonitoringDTO m1 : nameList) {
			for (MonitoringDTO m2 : statusList) {
				if (m1 == m2) {
					completed.add(m1);
				}
			}
		}

		return completed;

	}

	@Override
	public List<MonitoringDTO> getMonitoringsByStatus(int caseWorkerID, MonitoringStatusEnum status) {
		List<MonitoringDTO> statusList = MonitoringsMockDatabase.getSingleton().selectStarByStatus(status);
		List<MonitoringDTO> completed = new ArrayList<>();
		if (caseWorkerID == -1) {
			return statusList;
		} else {
			for (MonitoringDTO m1 : statusList) {
				if (m1.getCaseWorker().getCaseWorkerID() == caseWorkerID) {
					completed.add(m1);
				}

			}
		}
		return completed;
	}

	@Override
	public List<MonitoringDTO> getMonitoringsByClientId(int caseWorkerID, int clientId) {
		return MonitoringsMockDatabase.getSingleton().selectStarByClientID(clientId);

	}

	@Override
	public List<MonitoringDTO> getMonitoringsByDate(int caseWorkerID, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {

		return MonitoringsMockDatabase.getSingleton().selectStarByDateFromDateTo(
				MonitoringsMockDatabase.getSingleton().selectStarByCaseWorkerID(caseWorkerID), dateFrom, dateTo,
				dateType);

	}

	@Override
	public List<MonitoringDTO> filterByNameStatusDate(int caseWorkerID, Client client, MonitoringStatusEnum status,
			LocalDate dateFrom, LocalDate dateTo, String dateType) {

		List<MonitoringDTO> temp = filterByNameStatus(caseWorkerID, client, status);

		return MonitoringsMockDatabase.getSingleton().selectStarByDateFromDateTo(temp, dateFrom, dateTo, dateType);

	}

	@Override
	public List<MonitoringDTO> filterByNameDate(int caseWorkerID, Client client, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {

		List<MonitoringDTO> temp = getMonitoringsByClientId(client.getClientID());

		return MonitoringsMockDatabase.getSingleton().selectStarByDateFromDateTo(temp, dateFrom, dateTo, dateType);

	}

	@Override
	public List<MonitoringDTO> filterByStatusDate(int caseWorkerID, MonitoringStatusEnum status, LocalDate dateFrom,
			LocalDate dateTo, String dateType) {

		List<MonitoringDTO> temp = getMonitoringsByStatus(caseWorkerID, status);

		return MonitoringsMockDatabase.getSingleton().selectStarByDateFromDateTo(temp, dateFrom, dateTo, dateType);

	}

}
