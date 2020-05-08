package sharedData;

/**
 * A list of all legal monitoring statuses
 * @author merlin
 *
 */
public enum MonitoringStatusEnum {

	/**
	 * Waiting for review by a supervisor
	 */
	PENDING_FOR_REVIEW("Pending for Review"),
	/**
	 * Approved by the supervisor
	 */
	APPROVED("Approved"),
	/**
	 * Waiting for a correction that has been requested by the supervisor
	 */
	PENDING_FOR_CORRECTION("Pending for Correction"),
	/**
	 * Next monitoring for a client
	 */
	SCHEDULED("Scheduled"),
	/**
	 * A monitoring is in progress
	 */
	IN_PROGRESS("In Progress");
	
	
	private String description;
	
	private MonitoringStatusEnum(String desc)
	{
		description = desc;
	}

	/**
	 * @return a string describing the status
	 */
	public String getDescription() {
		return description;
	}
}
