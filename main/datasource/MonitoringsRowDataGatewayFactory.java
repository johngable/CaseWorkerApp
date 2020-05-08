package datasource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.Client;
import model.OptionsManager;
import sharedData.MonitoringDTO;

/**
 * Factory that returns the appropriate gateway based on mode and file
 * availability
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsRowDataGatewayFactory {

	public static MonitoringsRowDataGateway getGateway(int monitoringID, String clientName) {
		MonitoringsRowDataGateway monitoringsGateway;
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			// In test mode --> Mock
			monitoringsGateway = new MonitoringsRowDataGatewayMock(monitoringID);
		} else {
			clientName = clientName.replace(" ",  "");
			Path path = Paths.get("downloads/" + clientName + "/" + monitoringID + ".json");
			// check if file for client exists
			if (Files.exists(path)) {
				MonitoringDTO doesExistInFile = MonitoringDownloadGateway.getMonitoringDataFromFileByID(monitoringID, clientName);
				// the monitoringID was found in file --> return file gateway
				if (doesExistInFile != null) {
					// monitoring exists in a local file downloaded --> FileRowDataGateway
					monitoringsGateway = new FileRowDataGateway(monitoringID, clientName);
				} else {
					// the monitoringID wasn't found in file --> return RDS gateway
					monitoringsGateway = new MonitoringsRowDataGatewayRDS(monitoringID);
				}
			} else {
				// file not found --> return RDS gateway
				monitoringsGateway = new MonitoringsRowDataGatewayRDS(monitoringID);
			}
		}
		return monitoringsGateway;
	}

}
