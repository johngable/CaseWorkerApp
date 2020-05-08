package reports;

import java.util.ArrayList;
import java.util.List;

import sharedData.MonitoringDTO;

/**
 * Report holds a list of monitoring objects
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringListReport  implements Report {
	private static List<MonitoringDTO> monitoring = new ArrayList<>(); // list of monitoring objects

	/**
	 * Constructor takes a list of monitorings
	 * 
	 * @param monitorings - list of monitoring
	 */
	public MonitoringListReport(List<MonitoringDTO> monitorings) {
		this.monitoring = monitorings;
	}

	/**
	 * Getting method to retrieve the monitoring list
	 * 
	 * @return List<Object> - list of monitorings
	 */
	public List<MonitoringDTO> getMonitorings() {
		return monitoring;
	}
}
