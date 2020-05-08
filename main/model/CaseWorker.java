package model;

/**
 * Caseworker object that stores name and caseworker id
 * 
 * @author kimberlyoneill
 *
 */
public class CaseWorker implements java.io.Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + caseWorkerID;
		result = prime * result + ((caseWorkerName == null) ? 0 : caseWorkerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaseWorker other = (CaseWorker) obj;
		if (caseWorkerID != other.caseWorkerID)
			return false;
		if (caseWorkerName == null) {
			if (other.caseWorkerName != null)
				return false;
		} else if (!caseWorkerName.equals(other.caseWorkerName))
			return false;
		return true;
	}

	private String caseWorkerName;
	private int caseWorkerID;

	public CaseWorker() {}
	
	/**
	 * 
	 * @param name - caseWorker name
	 * @param id - caseWorker ID
	 */
	public CaseWorker(String name, int id) {
		this.caseWorkerName = name;
		this.caseWorkerID = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getCaseWorkerName() {
		return caseWorkerName;
	}

	/**
	 * 
	 * @return
	 */
	public int getCaseWorkerID() {
		return caseWorkerID;
	}

	@Override
	public String toString() {
		return this.caseWorkerName;
	}

	/**
	 * 
	 * @param caseWorkerName
	 */
	public void setCaseWorkerName(String caseWorkerName) {
		this.caseWorkerName = caseWorkerName;
	}

	/**
	 * 
	 * @param caseWorkerID
	 */
	public void setCaseWorkerID(int caseWorkerID) {
		this.caseWorkerID = caseWorkerID;
	}
}
