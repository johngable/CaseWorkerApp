package reports;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.Client;

class TestClientListReport {

	@Test
	void testClientListReport() {
		Client c1 = new Client("client1", 1);
		Client c2 = new Client("client2", 2);
		List<Client> clients = new ArrayList<Client>();
		clients.add(c1);
		clients.add(c2);
		ClientListReport clientReport = new ClientListReport(clients);
		assertEquals(clientReport.getClientList(), clients);
	}

}
