package datasource;

import java.util.List;

import sharedData.MonitoringStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test data for monitorings Enum to store monitorings for the clients.
 */
public enum MonitoringsEnum {

	/**
	 * 
	 */
	Client1Monitoring(formatDate(1, 0), formatDate(1, 0), formatDate(1, 0), 1, 3, MonitoringStatusEnum.APPROVED, 1,
			false, false),
	/**
	 * 
	 */
	Client1Monitoring2(formatDate(1, 0), formatDate(1, 0), null, 1, 3,
			MonitoringStatusEnum.PENDING_FOR_REVIEW, 1, false, false),
	/**
	 * 
	 */
	Client1Monitoring3(formatDate(1, 0), formatDate(1, 0), null, 1, 3,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),
	/**
	 * 
	 */
	Client1Monitoring4(formatDate(1, 3), formatDate(1, 12), null, 1, 3,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),
	/**
	 * 
	 */
	Client1Monitoring5(formatDate(0, 10), formatDate(0, 13), formatDate(0, 5), 1, 3, MonitoringStatusEnum.APPROVED, 1,
			false, false),
	/**
	 * /**
	 * 
	 */
	Client2Monitoring(formatDate(1, 1), formatDate(0, 24), null, 2, 4,
			MonitoringStatusEnum.PENDING_FOR_REVIEW, 1, false, false),
	/**
	 * 
	 */
	Client3Monitoring(formatDate(0, 21), formatDate(0, 18), formatDate(0, 12), 2, 4, MonitoringStatusEnum.APPROVED,
			1, false, false),
	/**
	 * 
	 */
	Client3Monitoring2(formatDate(0, 30), formatDate(0, 29), null, 3, 4,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),
	/**
	 * 
	 */
	Client4Monitoring(formatDate(0, 2), formatDate(0, 0), null, 4, 5,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),

	/**
	 * 
	 */
	Client5Monitoring(formatDate(0, 12), formatDate(0, 11), null, 5, 5,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),

	/**
	 * 
	 */
	Client5Monitoring2(formatDate(1, 24), formatDate(1, 1), null, 5, 5,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),
	/**
	 * 
	 */
	Client6Monitoring(formatDate(0, 0), formatDate(0, 0), null, 6, 6,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),

	/**
	 * 
	 */
	Client9Monitoring(formatDate(1, 1), formatDate(0, 28), formatDate(0, 0), 9, 3,
			MonitoringStatusEnum.PENDING_FOR_CORRECTION, 1, false, false),
	/**
	 * 
	 */
	Client9Monitoring2(formatDate(0, 25), formatDate(0, 22), null, 9, 3,
			MonitoringStatusEnum.PENDING_FOR_REVIEW, 1, false, false);

	private LocalDateTime visitDate;// the date that monitorings are updated
	private LocalDateTime dueDate; // the date that the monitoring is due
	private LocalDateTime approvedDate; // the date that the monitoring is approved
	private int clientId; // the clients unique Id
	private int caseWorkerID;// The case worker ID for the case worker
	private List<String> clientAnswers; // the clients answers to the questions
	private MonitoringStatusEnum status; // the status of the monitoring
	private int version; // version number of the monitoring
	private boolean isLocallyEdited; // changed locally = true
	private boolean isDownloaded; // true if someone has downloaded the file and not uploaded

	// Sample questions
	private static String q1 = "Are all the individual's identified health care needs being addressed?";
	private static String q2 = "Have there been changes observed in the individual's overall health functioning?";
	private static String q3 = "Have there been any medication changes or has this consumer experienced any side effects and/or adverse drug reactions to any medications?";
	private static String q4 = "Are there any additional health and safety issues or barriers affecting the person's wellbeing?";
	private static String q5 = "Are the house, site, household furnishings and appliances in a good condition? Is the consumer's room appropriately individualized and appointed?";
	private static String q6 = "Is necessary adaptive equipment available, in good condition, and being used?";
	private static String q7 = "Is the home/setting/community full accessible as it relates to the individual's needs, mobility, vision, etc.?";
	private static String q8 = "Is the residence clean, hygienic and ordor free?";
	private static List<String> questions = Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8);;
	private static String a1 = "Yes.";
	private static String a2 = "No changes in observed health";
	private static String a3 = "No changes in medication for client";
	private static String a4 = "No concerns";
	private static String a5 = "Yes household is well kept and organized.";
	private static String a6 = "Available but rarely used";
	private static String a7 = "Yes. Home is comfortable for the client";
	private static String a8 = "Yes. Well kept, organized and cleaned often";
	private static List<String> answers = Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8);;

	/**
	 * Constructor for monitoring enum
	 * 
	 * @param clientId      the client's id
	 * @param clientAnswers the client's answers
	 * @param status        the status of the monitoring
	 */
	private MonitoringsEnum(LocalDateTime visitDate, LocalDateTime dueDate, LocalDateTime approvedDate, int clientId,
			int caseWorkerID, MonitoringStatusEnum status,
			int version, boolean isLocallyEdited, boolean isDownloaded) {
		this.visitDate = visitDate;
		this.dueDate = dueDate;
		this.approvedDate = approvedDate;
		this.clientId = clientId;
		this.caseWorkerID = caseWorkerID;
		this.status = status;
		this.version = version;
		this.isLocallyEdited = isLocallyEdited;
	}

	/**
	 * More dynamic way of generating dates for monitoring enum
	 * 
	 * @param minusMonth - how many months ago
	 * @param minusDay   - how many days ago
	 * @return - formatted LocalDateTime object
	 */
	public static LocalDateTime formatDate(int minusMonth, int minusDay) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formatDateTime = LocalDateTime.now().minusMonths(minusMonth).minusDays(minusDay).format(formatter);
		LocalDateTime dateTime = LocalDateTime.parse(formatDateTime, formatter);
		return dateTime;
	}

	/**
	 * @return the clients id
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @return the list of client answers
	 */
	public List<String> getClientAnswers() {
		return clientAnswers;
	}

	/**
	 * sets the client answers
	 * 
	 * @param clientAnswers the client answers that will be used to set the answers
	 */
	public void setClientAnswers(List<String> clientAnswers) {
		this.clientAnswers = clientAnswers;
	}

	/**
	 * @return the status of the monitoring
	 */
	public MonitoringStatusEnum getStatus() {
		return status;
	}

	/**
	 * get a unique ID number for this monitoring
	 * 
	 * @return the enum's ordinal value + 1 (so no one is user zero)
	 */
	public int getMonitoringID() {
		return this.ordinal() + 1;
	}

	/**
	 * get a visit date for the monitorings
	 * 
	 * @return date
	 */
	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	/**
	 * get a due date for the monitorings
	 * 
	 * @return date
	 */
	public LocalDateTime getDueDate() {
		return dueDate;
	}

	/**
	 * get an approved date for the monitorings
	 * 
	 * @return date
	 */
	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	/**
	 * get a case worker ID for the specific case worker
	 * 
	 * @return caseWorkerID
	 */
	public int getCaseWorkerID() {
		return caseWorkerID;
	}

	/**
	 * get the questions to be answered by the client
	 * 
	 * @return questions
	 */
	public List<String> getQuestions() {
		return questions;
	}

	/**
	 * get the answers to be answered by the client
	 * 
	 * @return answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	
	/**
	 * get the version number of the monitoring
	 * 
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * gets whether monitoring was changes locally
	 * 
	 * @return boolean --> True/False
	 */
	public boolean getIsLocallyEdited() {
		return isLocallyEdited;
	}

	/**
	 * gets whether monitoring was downloaded to another machine and is not yet
	 * uploaded
	 * 
	 * @return boolean --> True/False
	 */
	public boolean getIsDownloaded() {
		return isDownloaded;
	}
}
