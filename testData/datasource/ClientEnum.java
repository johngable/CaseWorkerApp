package datasource;

/**
 * This stores the enums of the clients names.
 * 
 * @author tl9649
 *
 */

public enum ClientEnum {

	Amy("Amy Jones", 3), Jessica("Jessica Brown", 4), Ben("Ben Johnson", 4), William("William Baker", 5),
	George("George Miller", 5), Fred("Fred Pierce", 6), Denise("Denise White", 5), Charles("Charles Edwards", 5), Johny("Johny Abrams", 3),;

	// variable to hold the clients name
	private final String clientName;
	// holds ID of the caseworker
	private int caseWorkerID;

	/**
	 * constructor takes in name and assigns it to the instance variable
	 * 
	 * @param clientName - this is for the clients name
	 */
	private ClientEnum(String clientName, int caseWorkerID) {
		this.clientName = clientName;
		this.caseWorkerID = caseWorkerID;
	}

	/**
	 * returns the name of the client
	 * 
	 * @return name client
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * get a unique ID number for this client
	 * 
	 * @return the enum's ordinal value + 1 (so no one is user zero)
	 */
	public int getClientID() {
		return this.ordinal() + 1;
	}

	/**
	 * Gets the id of the clients caseworker
	 * 
	 * @returns integer id of the caseworker
	 */
	public int getCaseWorkerID() {
		return caseWorkerID;
	}

}
