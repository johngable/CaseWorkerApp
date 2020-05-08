package sharedData;

import java.time.LocalDateTime;
import java.util.List;

import model.CaseWorker;
import model.Client;

/**
 * DTO carrying information about a single monitoring
 * 
 * @author merlin
 *
 */
public class MonitoringDTO implements java.io.Serializable {

	private int monitoringID;
	private LocalDateTime visitDate;
	private LocalDateTime dueDate;
	private LocalDateTime approvedDate;
	private Client client;
	private CaseWorker caseWorker;
	private List<String> questions;
	private List<String> answers;
	private MonitoringStatusEnum status;
	private int version;
	private boolean isLocallyEdited;
	private boolean isDownloaded;
	private boolean isLocal = false;

	/**
	 * Default constructor needed for serializer
	 */
	public MonitoringDTO() {
	}

	/**
	 * @param monitoringID TODO
	 * @param questions    the questions that were asked
	 * @param answers      the answers that were given
	 * @param status       the current status of this monitoring
	 * @param isLocallyEdited TODO
	 * @param date         When the monitoring occurred
	 * @param clientID     the client the monitoring refers to
	 * @param caseWorkerID the casework who took the monitoring
	 */
	public MonitoringDTO(int monitoringID, LocalDateTime visitDate, LocalDateTime dueDate, LocalDateTime approvedDate,
			Client client, CaseWorker caseWorker, List<String> questions, List<String> answers,
			MonitoringStatusEnum status, int version, boolean isLocallyEdited, boolean isDownloaded) {
		super();
		this.monitoringID = monitoringID;
		this.visitDate = visitDate;
		this.dueDate = dueDate;
		this.approvedDate = approvedDate;
		this.client = client;
		this.caseWorker = caseWorker;
		this.questions = questions;
		this.answers = answers;
		this.status = status;
		this.version = version;
		this.isLocallyEdited = isLocallyEdited;
		this.isDownloaded = isDownloaded;
	}	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((approvedDate == null) ? 0 : approvedDate.hashCode());
		result = prime * result + ((caseWorker == null) ? 0 : caseWorker.hashCode());
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + monitoringID;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((visitDate == null) ? 0 : visitDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonitoringDTO other = (MonitoringDTO) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (approvedDate == null) {
			if (other.approvedDate != null)
				return false;
		} else if (!approvedDate.equals(other.approvedDate))
			return false;
		if (caseWorker == null) {
			if (other.caseWorker != null)
				return false;
		} else if (!caseWorker.equals(other.caseWorker))
			return false;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (monitoringID != other.monitoringID)
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		if (status != other.status)
			return false;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		return true;
	}

	/**
	 * @return the answers the caseworker gave to the questions
	 */
	public List<String> getAnswers() {
		return answers;
	}

	/**
	 * @return the unique ID of the caseworker who took this monitoring
	 */
	public CaseWorker getCaseWorker() {
		return caseWorker;
	}

	/**
	 * @return the unique ID of the client this monitoring refers to
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @return the time and date when the monitoring visit occurred
	 */
	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	/**
	 * @return the time and date when the monitoring listing was created
	 */
	public LocalDateTime getDueDate() {
		return dueDate;
	}

	/**
	 * @return the time and date when the monitoring listing was approved
	 */
	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	/**
	 * @return this monitoring's unique ID
	 */
	public int getMonitoringID() {
		return monitoringID;
	}

	/**
	 * @return the questions that this monitoring contains
	 */
	public List<String> getQuestions() {
		return questions;
	}

	/**
	 * @return the current status of this monitoring
	 * @see MonitoringStatusEnum
	 */
	public MonitoringStatusEnum getStatus() {
		return status;
	}
	
	/**
	 * @return the version number of the monitoring
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * @return the edited status
	 */
	public boolean getIsLocallyEdited() {
		return isLocallyEdited;
	}
	
	/**
	 * @return whether monitoring is downloaded
	 */
	public boolean getIsDownloaded() {
		return isDownloaded;
	}
	
	/**
	 * @return whether the monitoring is a local file
	 */
	public boolean getIsLocal() {
		return isLocal;
	}

	/**
	 * 
	 * @param monitoringID
	 */
	public void setMonitoringID(int monitoringID) {
		this.monitoringID = monitoringID;
	}

	/**
	 * 
	 * @param visitDate
	 */
	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	/**
	 * 
	 * @param dueDate
	 */
	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * 
	 * @param approvedDate
	 */
	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

	/**
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * 
	 * @param caseWorker
	 */
	public void setCaseWorker(CaseWorker caseWorker) {
		this.caseWorker = caseWorker;
	}

	/**
	 * @param answers the answers the caseworker gave to the questions
	 */
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	/**
	 * 
	 * @param questions
	 */
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

	/**
	 * 
	 * @param status the new status of this monitoring
	 */
	public void setStatus(MonitoringStatusEnum status) {
		this.status = status;
	}
	
	/**
	 * @param version the new version number of this monitoring
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	/**
	 * @param isEdited changes the edited status
	 */
	public void setIsEdited(boolean isEdited) {
		this.isLocallyEdited = isEdited;
	}
	
	/**
	 * @param isDownloaded set to true on download
	 */
	public void setIsDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	
	/**
	 * @param isLocal set to true if monitoring is a local file
	 */
	public void setIsLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
}
