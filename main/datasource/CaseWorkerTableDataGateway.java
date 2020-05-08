package datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Table data gateway for CaseWorker data. Retrieves a list of names or IDs for
 * provided the provided supervisorID
 * 
 * @author am2319
 *
 */
public class CaseWorkerTableDataGateway {
  private CaseWorkerEnum cworkerEnum = null;

  /**
   * Gets the list of names from the CaseWorkerEnum using the supervisorID
   * 
   * @param caseworkers
   *          the given supervisorID
   * @return caseWorkerList a list of caseworker names matching the supervisorID
   */
  public static List<String> getCaseWorkerList(int supervisorID) {
    List<String> caseWorkerList = new ArrayList<>();
    for (CaseWorkerEnum element : CaseWorkerEnum.values()) {
      if (element.getSupervisorID() == supervisorID) {
        caseWorkerList.add(element.getCaseWorkerName());
      }
    }
    return caseWorkerList;
  }

  /**
   * Gets the list of caseworker ID from the CaseWorkerEnum using the supervisorID
   * 
   * @param supervisorID
   *          the given supervisorID
   * @return caseWorkerList a list of caseworkerIDs matching the supervisorID
   */
  public static List<Integer> getCaseWorkerListID(int supervisorID) {
    List<Integer> caseWorkerList = new ArrayList<>();
    for (CaseWorkerEnum element : CaseWorkerEnum.values()) {
      if (element.getSupervisorID() == supervisorID) {
        caseWorkerList.add(element.getCaseWorkerID());
      }
    }
    return caseWorkerList;
  }
}
