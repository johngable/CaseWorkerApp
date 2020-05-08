package datasource;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests the CaseWorkerRowDataGateway
 * 
 * @author am2319
 *
 */
class CaseWorkerRowDataGatewayTest {

	/**
	 * Tests that a row pulled by the gateway matches the corresponding row in the
	 * enum
	 */
	@Test
	void testRowDataGateway() {
		
		for (CaseWorkerEnum expected : CaseWorkerEnum.values()) {
			CaseWorkerRowDataGateway testGateway = new CaseWorkerRowDataGateway(expected.getCaseWorkerID());
			assertEquals(testGateway.getID(), expected.getCaseWorkerID());
			assertEquals(testGateway.getName(), expected.getCaseWorkerName());
			assertEquals(testGateway.getSupervisorID(), expected.getSupervisorID());
		}
	}

}
