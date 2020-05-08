package reports;

import java.util.List;

import model.Client;
/**
 * Report that holds a list of clients.
 * @author Ian
 *
 */
public class ClientListReport implements Report{
	private List<Client> clientList;
	
	/**
	 * Constructor that takes in a list of Client objects
	 * @param clientList the list of Client objects
	 */
	public ClientListReport(List<Client> clientList) {
		this.clientList = clientList;
	}

	/**
	 * @return the list of Clients
	 */
	public List<Client> getClientList() {
		return clientList;
	}
}
