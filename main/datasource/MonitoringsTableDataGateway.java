package datasource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Defines the methods required for all Monitorings Table Data Gateways
 * 
 * @author merlin
 *
 */
public abstract class MonitoringsTableDataGateway {

	/**
	 * Get all of the monitorings matching a given status
	 * 
	 * @param status the status we are interested in
	 * @return the list of matching monitorings
	 */
	public abstract List<MonitoringDTO> getMonitoringsByStatus(int caseWorkerID, MonitoringStatusEnum status);

	/**
	 * Get all of the monitorings for a given client
	 * 
	 * @param clientId the unique id of the client
	 * @return the list of matching monitorings
	 */
	public abstract List<MonitoringDTO> getMonitoringsByClientId(int caseWorkerID, int clientId);
	
	/**
	 * Get all of the monitorings for a given client
	 * 
	 * @param client object
	 * @return the list of matching monitorings
	 */
	public abstract List<MonitoringDTO> getMonitoringsByClient(Client client);


	/**
	 * Gets all of the monitoring for a given caseworker
	 * 
	 * @param caseWorkerID the ID of the caseworker assigned to the client
	 * @return the list of monitorings associated with the caseworker
	 */
	public abstract List<MonitoringDTO> getMonitoringsByCaseWorkerID(int caseWorkerID);
	
	/**
	 * Gets all monitorings after the date requested. Used to selected all monitorings that occurred in the last month
	 * 
	 * @param caseWorkerID the ID of the caseworker assigned to the client
	 * @return the list of monitorings associated with the caseworker
	 */
	public abstract List<MonitoringDTO> getMonitoringsByDate(int caseWorkerID, LocalDate dateFrom, LocalDate dateTo, String dateType);

	/**
	 * Gets all the clients that have been downloaded
	 * @return list of clients
	 */
	public abstract List<Client> getDownloadedClients();
	
	/**
	 * Returns all MonitoringDTOs that contains the client, status, and date/dateType filtered by
	 * @param client
	 * @param status
	 * @param date
	 * @param dateType
	 * @return List<MonitoringDTO> - list of monitorings containing filter info
	 */
	public abstract List<MonitoringDTO> filterByNameStatusDate(int caseWorkerID, Client client, MonitoringStatusEnum status, LocalDate dateFrom, LocalDate dateTo, String dateType);
	
	/**
	 * Returns all MonitoringDTOs that contain the client and status
	 * @param client 
	 * @param status 
	 * @return List<MonitoringDTO> - list of monitorings containing filter info
	 */
	public abstract List<MonitoringDTO> filterByNameStatus(int caseWorkerID, Client client, MonitoringStatusEnum status);
	
	/**
	 * Returns all MonitoringDTOs that contain the client and date/dateType
	 * @param client
	 * @param date
	 * @param dateType - type of date to search by
	 * @return List<MonitoringDTO> - list of monitorings containing filter info
	 */
	public abstract List<MonitoringDTO> filterByNameDate(int caseWorkerID, Client client, LocalDate dateFrom, LocalDate dateTo, String dateType);
	
	/**
	 * Returns all MonitoringDTOs that contain the status and date/dateType
	 * @param status
	 * @param date
	 * @param dateType
	 * @return List<MonitoringDTO> - list of monitorings containing filter info
	 */
	public abstract List<MonitoringDTO> filterByStatusDate(int caseWorkerID, MonitoringStatusEnum status, LocalDate dateFrom, LocalDate dateTo, String dateType);
	
}
