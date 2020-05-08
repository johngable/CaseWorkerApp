package datasource;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the CaseWorkerTableDataGateway
 * @author am2319
 *
 */
class CaseWorkerTableDataGatewayTest {

	/**
	 * Tests the CaseWorkerTableDataGateway to ensure that it correctly retrieves caseworker names
	 * from the CaseWorkerEnum using the supervisor ID
	 */
	@Test
	void testTableDataGateway() {
	  
	  CaseWorkerTableDataGateway caseworkerGateway = new CaseWorkerTableDataGateway();
	  for (SupervisorEnum s : SupervisorEnum.values()) {
	    List<String> expectedCaseworkerNames = new ArrayList<>();
	    for (CaseWorkerEnum c : CaseWorkerEnum.values()) {
	      if (c.getSupervisorID() == s.getID()) {
	        expectedCaseworkerNames.add(c.getCaseWorkerName());
	      }
	    }
	    
	    List<String> caseworkerNames = caseworkerGateway.getCaseWorkerList(s.getID());
	    
	    for (String name : caseworkerNames) {
	      assertTrue(expectedCaseworkerNames.contains(name));
	    }
	  }
	}
	
	/**
   * Tests the CaseWorkerTableDataGateway to ensure that it correctly retrieves caseworker IDs
   * from the CaseWorkerEnum using the supervisor ID
   */
  @Test
  void testTableDataGatewayID() {
    
    CaseWorkerTableDataGateway caseworkerGateway = new CaseWorkerTableDataGateway();
    for (SupervisorEnum s : SupervisorEnum.values()) {
      List<Integer> expectedCaseworkerNames = new ArrayList<>();
      for (CaseWorkerEnum c : CaseWorkerEnum.values()) {
        if (c.getSupervisorID() == s.getID()) {
          expectedCaseworkerNames.add(c.getCaseWorkerID());
        }
      }
      
      List<Integer> caseworkerNames = caseworkerGateway.getCaseWorkerListID(s.getID());
      
      for (Integer id : caseworkerNames) {
        assertTrue(expectedCaseworkerNames.contains(id));
      }
    }
  }

}
