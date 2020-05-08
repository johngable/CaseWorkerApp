package datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a gateway for ClientEnum.
 * 
 * Currently it is used to get the list of client names or IDs when given their
 * caseworkerID
 * 
 * @author Michael Umbelina
 */
public class ClientTableDataGateway {

	/**
	 * This method will take in an integer of the caseworker's ID and return a list
	 * of client names that are associated with that ID
	 * 
	 * @param caseworkerID the ID of the caseworker who the clients are associated
	 *                     with
	 * @return clientList - List of client names matched from the given ID
	 */
	public List<String> getClientList(int caseworkerID) {
		List<String> clientList = new ArrayList<>();

		for (ClientEnum element : ClientEnum.values()) {
			if (element.getCaseWorkerID() == caseworkerID) {
				clientList.add(element.getClientName());
			}
		}

		return clientList;
	}

	/**
	 * Gets a list of client IDs that are associated with a caseworker ID
	 * 
	 * @param caseworkerID the ID of the caseworker who the clients are monitored by
	 * @return clientIDs the list of client IDs for a given caseworker
	 */
	public List<Integer> getClientListID(int caseworkerID) {
		List<Integer> clientIDs = new ArrayList<>();

		for (ClientEnum element : ClientEnum.values()) {
			if (element.getCaseWorkerID() == caseworkerID) {
				clientIDs.add(element.getClientID());
			}
		}

		return clientIDs;
	}
}
