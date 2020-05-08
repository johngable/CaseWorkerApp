package datasource;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the SupervisorRowDataGateway class
 * 
 * @author am2319
 */
abstract class TestSupervisorRowDataGateway {

	abstract SupervisorRowDataGateway getGateway(int id);

	/**
	 * Make sure the finder constructor finds the gateway and it returns everything
	 * it should
	 */
	@Test
	public void testFinderConstructor() {
		SupervisorRowDataGateway gateway = getGateway(SupervisorEnum.Supervisor2.getID());
		assertEquals(SupervisorEnum.Supervisor2.getName(), gateway.getName());
		assertEquals(SupervisorEnum.Supervisor2.getID(), gateway.getID());
	}
}
