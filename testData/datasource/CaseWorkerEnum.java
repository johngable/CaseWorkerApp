package datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum to hold CaseWorker name and client list. ID matches UserLoginDataEnum
 * 
 * @author am2319
 *
 */
public enum CaseWorkerEnum {

	gblack(3, "Greg Black", 1), 
	sstone(4, "Susan Stone", 1), 
	zwilliams(5, "Zach Williams", 2), 
	pLawrence(6, "Peter Lawrence", 1);

	private final int ID; // CaseWorker ID number. Matches ID in UserLoginDataEnum
	private final String name; // CaseWorker name
	private int supervisorID; // ID of the supervisor

	/**
	 * Constructor for the CaseWorkerEnum
	 * 
	 * @param ID           - caseworker id
	 * @param name         - name of caseworker
	 * @param supervisorID - id of supervisor that the caseworker reports to
	 */
	private CaseWorkerEnum(int ID, String name, int supervisorID) {
		this.ID = ID;
		this.name = name;
		this.supervisorID = supervisorID;
	}

	/**
	 * Returns a CaseWorker's ID
	 * 
	 * @return ID of the caseworker
	 */
	public int getCaseWorkerID() {
		return this.ID;
	}

	/**
	 * Returns a CaseWorker's name
	 * 
	 * @return name of caseworker
	 */
	public String getCaseWorkerName() {
		return this.name;
	}

	/**
	 * Returns the supervisors ID
	 * 
	 * @return supervisorID - id of the caseworkers supervisor
	 */
	public int getSupervisorID() {
		return this.supervisorID;
	}
}
