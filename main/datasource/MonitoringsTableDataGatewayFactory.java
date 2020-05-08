package datasource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.Client;
import model.OptionsManager;
import sharedData.MonitoringDTO;

/**
 * MonitoringsTableDataGatewayFactory is a factory that returns the appropriate
 * gateway that commands use based on mode, local file availability and
 * connection
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsTableDataGatewayFactory {

	public static MonitoringsTableDataGateway getGateway(Client client) {
		MonitoringsTableDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			// In test mode --> Mock
			monitoringsGateway = new MonitoringsTableDataGatewayMock();
		} else {
			String clientName = client.getClientName().replace(" ", "");
			Path path = Paths.get("downloads/" + clientName + ".json");

			if (Files.exists(path)) {
				// client has a downloaded file
				monitoringsGateway = new FileTableDataGateway();
			} else {
				// client doesn't have a downloaded file
				monitoringsGateway = new MonitoringsTableDataGatewayRDS();
			}
		}
		return monitoringsGateway;
	}

	public static MonitoringsTableDataGateway getGateway() {
		MonitoringsTableDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			// In test mode --> Mock
			monitoringsGateway = new MonitoringsTableDataGatewayMock();
		} else {

			monitoringsGateway = new MonitoringsTableDataGatewayRDS();
		}
		return monitoringsGateway;
	}
}
