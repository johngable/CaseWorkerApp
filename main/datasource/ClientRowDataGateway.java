package datasource;

/**
 * 
 * @author Amity Brown
 *
 */

public class ClientRowDataGateway {
	private ClientEnum clientEnum = null;

	/**
	 * constructor for the client row data gateway
	 * @param clientID
	 */
	public ClientRowDataGateway(int clientID) {
		for (ClientEnum cEnum : ClientEnum.values()) {
			if (cEnum.getClientID() == clientID) {
				clientEnum = cEnum;
			}
		}
	}

	/**
	 * gets the name of the client
	 * @return client name
	 */
	public String getClientName() {
		return clientEnum.getClientName();
	}

	/**
	 * gets the id of the caseworker that oversees the client
	 * @return caseworker id
	 */
	public int getCaseWorkerID() {
		return clientEnum.getCaseWorkerID();
	}

	/**
	 * gets the id of the client
	 * @return client id
	 */
	public int getClientID() {
		return clientEnum.getClientID();
	}

}
