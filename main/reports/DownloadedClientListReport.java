package reports;

import java.util.ArrayList;
import java.util.List;

import model.Client;

/**
 * Holds list of clients who have data currently downloaded
 * 
 * @author michaelpermyashkin
 *
 */
public class DownloadedClientListReport implements Report {

	private List<Client> clientList = new ArrayList<>();

	/**
	 * 
	 * @param clientList - to add to report
	 */
	public DownloadedClientListReport(List<Client> clientList) {
		this.clientList.addAll(clientList);
	}

	/**
	 * Getter
	 * 
	 * @return list of clients
	 */
	public List<Client> getClientList() {
		return this.clientList;
	}
}
