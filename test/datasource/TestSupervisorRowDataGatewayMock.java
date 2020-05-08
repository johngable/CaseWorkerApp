package datasource;

/**
 * @author merlin
 *
 */
public class TestSupervisorRowDataGatewayMock extends TestSupervisorRowDataGateway {

	@Override
	SupervisorRowDataGateway getGateway(int id) {
		return new SupervisorRowDataGatewayMock(id);
	}

}
