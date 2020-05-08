package model;

/**
 * Client object holds name of client and their ID
 * 
 * @author michaelpermyashkin
 *
 */
public class Client implements java.io.Serializable {
	private String clientName;
	private int clientID;

	/**
	 * Default constructor needed for serializer
	 */
	public Client() {
	}

	public Client(String name, int id) {
		this.clientName = name;
		this.clientID = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clientID;
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
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
		Client other = (Client) obj;
		if (clientID != other.clientID)
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		return true;
	}

	/**
	 * 
	 * @return String of clients name
	 */
	public String getClientName() {
		return this.clientName;
	}

	/**
	 * 
	 * @return int of client ID
	 */
	public int getClientID() {
		return this.clientID;
	}

	/**
	 * @return String of name of client
	 */
	public String toString() {
		return this.clientName;
	}

	/**
	 * 
	 * @param clientName
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * 
	 * @param clientID
	 */
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
}
