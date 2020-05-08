package datasource;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test the mock version of this gateway
 * @author merlin
 *
 */
public class TestMonitoringsTableDataGatewayMock extends TestMonitoringsTableDataGateway {

	
	/**
	 * make sure the mock db is ready to go
	 */
	@BeforeEach
	public void setUp()
	{
		MonitoringsMockDatabase.resetSingleton();
	}
	
	/**
	 * @see TestMonitoringsTableDataGateway#getGateway()
	 */
	@Override
	public MonitoringsTableDataGateway getGateway() {
		return new MonitoringsTableDataGatewayMock();
	}

}
