package datasource;

import java.time.LocalDateTime;
import java.util.List;

import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Defines the methods required by a row data gateway for the monitorings table
 * 
 * @author merlin
 *
 */
public abstract class MonitoringsRowDataGateway {
	
	/**
	 * @return the row's unique id
	 */
	public abstract int getMonitoringID();

	/**
	 * @return the visit date of the monitoring
	 */
	public abstract LocalDateTime getVisitDate();

	/**
	 * @return the due date of the monitoring
	 */
	public abstract LocalDateTime getDueDate();

	/**
	 * @return the approval date of the monitoring
	 */
	public abstract LocalDateTime getApprovedDate();

	/**
	 * @return the client the monitoring refers to
	 */
	public abstract Client getClient();

	/**
	 * @return the caseworker who took the monitoring
	 */
	public abstract CaseWorker getCaseworker();

	/**
	 * @return the questions that were asked
	 */
	public abstract List<String> getQuestions();

	/**
	 * @return the answers the caseworker gave to the questions
	 */
	public abstract List<String> getAnswers();

	/**
	 * @return the status of a monitoring
	 */
	public abstract MonitoringStatusEnum getStatus();
	
	/**
	 * @return the version of a monitoring
	 */
	public abstract int getVersion();

	/**
	 * @return the status of a monitoring
	 */
	public abstract void setVisitDateTime(LocalDateTime visitDateTime);

	/**
	 * update the caseworker's answers
	 * 
	 * @param newAnswers these should overwrite the pre-existing answers
	 */
	public abstract void setAnswers(List<String> newAnswers);

	/**
	 * update the monitoring's status
	 * 
	 * @param newStatus the new status for the monitoring
	 */
	public abstract void setStatus(MonitoringStatusEnum newStatus);
	
	/**
	 * update the version number
	 * 
	 * @param version the new version number
	 */
	public abstract void setVersion(int version);
	
	/**
	 * update multiple fields in a monitoring (visitDate, answers, status, version)
	 * 
	 * @param answers   - the updated answers
	 * @param status    - the updated status
	 * @param visitDate - the new visit date
	 * @param version   - the updated version number
	 */
	public abstract void setMultiple(List<String> answers, MonitoringStatusEnum status, LocalDateTime visitDate, int version);
	
	/**
	 * 
	 * @return id of last monitoringID in table
	 */
	public abstract int getAutoIncrementMax();

	/**
	 * Method that sets flag when monitoring is being downloaded and resets flag on upload
	 * @param isDownloaded
	 */
	public abstract void setIsDownloaded(boolean isDownloaded);
	
	public abstract MonitoringDTO getMonitoringForDownload();
}
