package datasource;


/**
 * 
 * @author michaelpermyashkin
 *
 */
class TestMonitoringsTableDataGatewayRDS extends TestMonitoringsTableDataGateway {
	/**
	 * @see TestMonitoringsTableDataGateway#getGateway()
	 */
	@Override
	public MonitoringsTableDataGateway getGateway() {
		return new MonitoringsTableDataGatewayRDS();
	}
}
