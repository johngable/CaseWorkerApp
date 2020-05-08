package datasource;

import java.util.List;

/**
 * Row data gateway for the CaseWorkerEnum
 * @author am2319
 *
 */
public class CaseWorkerRowDataGateway {
private CaseWorkerEnum cworkerEnum = null;
	
	/**
	 * Constructor for the CaseWorkerRowDataGateway
	 * @param caseworker
	 */
	public CaseWorkerRowDataGateway(int caseworker) {
		for (CaseWorkerEnum cwEnum : CaseWorkerEnum.values()) {
			if (cwEnum.getCaseWorkerID() == caseworker) {
				cworkerEnum = cwEnum; 
			}
		}
	}
	
	/**
	 * Returns the ID of the caseworker
	 * @return CaseWorkerID
	 */
	public int getID() {
		return cworkerEnum.getCaseWorkerID();
	}
	
	/**
	 * Returns the caseworker's name
	 * @return CaseWorkerName
	 */
	public String getName() {
		return cworkerEnum.getCaseWorkerName();
	}
	
	/**
	 * Returns the caseworker's list of clients
	 * @return CaseWorkerClients
	 */
	public int getSupervisorID() {
		return cworkerEnum.getSupervisorID();
	}

}
