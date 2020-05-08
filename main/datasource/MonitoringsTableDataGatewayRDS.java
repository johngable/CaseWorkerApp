package datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Real MonitoringsTableDataGatewayRDS to execute SQL statements on the DB
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsTableDataGatewayRDS extends MonitoringsTableDataGateway {

	/**
	 * Initializes the Monitorings table with values and drops the table if exists
	 * prior to initialization
	 */
	public void initializeMonitoringsTable() {

		String sqlDropMonitoringsTable = "DROP TABLE IF EXISTS Monitorings;";
		String sqlCreateMonitoringsTable = "CREATE TABLE Monitorings" + "("
				+ "monitoringID INT NOT NULL AUTO_INCREMENT, " + "visitDate DATE, " + "visitTime TIME, "
				+ "dueDate DATE, " + "dueTime TIME, " + "approvedDate DATE, " + "approvedTime TIME, "
				+ "clientID INT NOT NULL, " + "caseWorkerID INT NOT NULL, " + "question1 VARCHAR(250), "
				+ "question2 VARCHAR(250), " + "question3 VARCHAR(250), " + "question4 VARCHAR(250), "
				+ "question5 VARCHAR(250), " + "question6 VARCHAR(250), " + "question7 VARCHAR(250), "
				+ "question8 VARCHAR(250), " + "answer1 VARCHAR(250), " + "answer2 VARCHAR(250), "
				+ "answer3 VARCHAR(250), " + "answer4 VARCHAR(250), " + "answer5 VARCHAR(250), "
				+ "answer6 VARCHAR(250), " + "answer7 VARCHAR(250), " + "answer8 VARCHAR(250), "
				+ "status VARCHAR(50) NOT NULL, " + "version INT NOT NULL, " + "isDownloaded INT NOT NULL,"
				+ "PRIMARY KEY(monitoringID)" + ");";
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			// Drop the table if exists first
			statement.executeUpdate(sqlDropMonitoringsTable);
			// Create new Monitorings Table
			statement.executeUpdate(sqlCreateMonitoringsTable);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Static function that returns a list of monitoring based on status.
	 * 
	 * @param status - takes a string which is the status by which we would like to
	 *               match
	 * @return List<MonitoringDTO> - contains monitoring that match the requested
	 *         status
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByStatus(int caseWorkerID, MonitoringStatusEnum status) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();

		String statusAsString = status.getDescription();

		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE status = '" + statusAsString + "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND status = '" + statusAsString
					+ "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

	/**
	 * Static function that returns a list of monitoring based on status.
	 * 
	 * @param clientID - takes a int which is the clientID by which we would like to
	 *                 match
	 * @return List<MonitoringDTO> - contains monitoring that match the requested
	 *         status
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByClientId(int caseWorkerID, int clientId) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE clientID = " + clientId + ";";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND clientID = " + clientId
					+ ";";
		}
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

	/**
	 * Function that returns a list of monitorings for a caseworker
	 * 
	 * @param caseWorkerID - The ID for a given caswowkrer that we would like to
	 *                     search for
	 * @return List<MonitoringDTO> - contains the monitorings that match the
	 *         requested caseworkerID
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByCaseWorkerID(int caseWorkerID) {
		// gets monitorings to download
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String select = new String("SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + ";");
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(select);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		return monitoringList;
	}

	/**
	 * Function that returns a list of monitorings for a caseworker
	 * 
	 * @param clientID - The ID for a given client that we would like to search for
	 * @return List<MonitoringDTO> - contains the monitorings that match the
	 *         requested caseworkerID
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByDate(int caseWorkerID, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE " + dateType + " >= '" + dateFrom + "' AND " + dateType + " <= '"
					+ dateTo + "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseworkerID = " + caseWorkerID + " AND " + dateType + " >= '"
					+ dateFrom + "' AND " + dateType + " <= '" + dateTo + "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		// for each monitoring to be downloaded, set isDownloaded --> TRUE
		for (MonitoringDTO m : monitoringList) {
			MonitoringsRowDataGatewayRDS rowRDS = new MonitoringsRowDataGatewayRDS(m.getMonitoringID());
			rowRDS.setIsDownloaded(true);
		}

		return monitoringList;
	}

	/**
	 * Returns list of clients who currently have monitorings that have been
	 * uploaded
	 */
	@Override
	public List<Client> getDownloadedClients() {
		List<Client> clientList = new ArrayList<>();

		String sql = new String("SELECT * FROM Monitorings WHERE isDownloaded = 1;");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Client client = getClient(rs);

				if (!clientList.contains(client)) {
					clientList.add(client); // adds client to list
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return clientList;
	}

	/**
	 * @param rs - result set from query
	 * @return MonitoringStatusEnum - monitoring status
	 */
	private MonitoringStatusEnum getStatus(ResultSet rs) {
		MonitoringStatusEnum monitoringStatus = null;
		String status;
		try {
			status = rs.getString("status");
			for (MonitoringStatusEnum m : MonitoringStatusEnum.values()) {
				if (status.equals(m.getDescription())) {
					monitoringStatus = m;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monitoringStatus;
	}

	/**
	 * @param rs - result set from query
	 * @return List<String> - list of answers
	 */
	private List<String> getAnswers(ResultSet rs) {
		List<String> answers = new ArrayList<>();
		try {
			for (int i = 1; i <= 8; i++) {
				if (rs.getString("answer" + i) != null) {
					answers.add(rs.getString("answer" + i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answers;
	}

	/**
	 * @param rs - result set from query
	 * @return List<String> - list of questions
	 */
	private List<String> getQuestions(ResultSet rs) {
		List<String> questions = new ArrayList<>();
		try {
			for (int i = 1; i <= 8; i++) {
				if (rs.getString("question" + i) != null) {
					questions.add(rs.getString("question" + i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}

	/**
	 * @param rs - result set from query
	 * @return CaseWorker - object of Caseworker with name and ID
	 */
	private CaseWorker getCaseWorker(ResultSet rs) {
		CaseWorker caseWorker;
		try {
			int caseWorkerID = rs.getInt("caseWorkerID");
			// temporarily we search the caseWorker enum to get their name until we store it
			// in a DB
			String caseWorkerName = null;
			for (CaseWorkerEnum c : CaseWorkerEnum.values()) {
				if (c.getCaseWorkerID() == caseWorkerID) {
					caseWorkerName = c.getCaseWorkerName();
					caseWorker = new CaseWorker(caseWorkerName, caseWorkerID);
					// we return caseWorker object if both ID and name have been found
					return caseWorker;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// if previous return doesnt happen, we return null
		return null;
	}

	/**
	 * @param rs - result set from query
	 * @return Client - object of client with name and ID
	 */
	private Client getClient(ResultSet rs) {
		Client client;
		try {
			int clientID = rs.getInt("clientID");
			// temporarily we search the client enum to get their name until we store it
			// in a DB
			String clientName = null;
			for (ClientEnum c : ClientEnum.values()) {
				if (c.getClientID() == clientID) {
					clientName = c.getClientName();
					client = new Client(clientName, clientID);
					// we return client object if both ID and name have been found
					return client;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// if previous return doesnt happen, we return null
		return null;
	}

	/**
	 * @param rs - result set from query
	 * @return int - monitoringID
	 */
	private int getMonitoringID(ResultSet rs) {
		int monitoringID = -1;
		try {
			monitoringID = rs.getInt("monitoringID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monitoringID;
	}

	/**
	 * @param rs - result set from query
	 * @return LocalDateTime - date monitoring was completed
	 */
	private LocalDateTime getVisitDate(ResultSet rs) {
		LocalDateTime dateTime = null;
		try {
			if (rs.getDate("visitDate") != null && rs.getTime("visitTime") != null) {
				LocalDate date = rs.getDate("visitDate").toLocalDate();
				LocalTime time = rs.getTime("visitTime").toLocalTime();
				dateTime = LocalDateTime.of(date, time);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @param rs - result set from query
	 * @return LocalDateTime - date monitoring was due
	 */
	private LocalDateTime getDueDate(ResultSet rs) {
		LocalDateTime dateTime = null;
		try {
			if (rs.getDate("dueDate") != null && rs.getTime("dueTime") != null) {
				LocalDate date = rs.getDate("dueDate").toLocalDate();
				LocalTime time = rs.getTime("dueTime").toLocalTime();
				dateTime = LocalDateTime.of(date, time);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @param rs - result set from query
	 * @return LocalDateTime - date monitoring was approved
	 */
	private LocalDateTime getApprovedDate(ResultSet rs) {
		LocalDateTime dateTime = null;
		try {
			if (rs.getDate("approvedDate") != null && rs.getTime("approvedTime") != null) {
				LocalDate date = rs.getDate("approvedDate").toLocalDate();
				LocalTime time = rs.getTime("approvedTime").toLocalTime();
				dateTime = LocalDateTime.of(date, time);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @param rs - result set from query
	 * @return int - version
	 */
	private int getVersion(ResultSet rs) {
		int version = -1;
		try {
			version = rs.getInt("version");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * @param rs - result set from query
	 * @return int - version
	 */
	private boolean getIsDownloaded(ResultSet rs) {
		int isDownloaded = 0;
		try {
			isDownloaded = rs.getInt("isDownloaded");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (isDownloaded == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<MonitoringDTO> getMonitoringsByClient(Client client) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * filter monitorings by all three advanced search criteria
	 * 
	 * @param client
	 * @param status
	 * @param date
	 * @return
	 */
	public List<MonitoringDTO> filterByNameStatusDate(int caseWorkerID, Client client, MonitoringStatusEnum status,
			LocalDate dateFrom, LocalDate dateTo, String dateType) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String statusAsString = status.getDescription();

		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE clientID = " + client.getClientID() + " AND status = '"
					+ statusAsString + "' AND " + dateType + " >= '" + dateFrom + "' AND " + dateType + " <= '" + dateTo
					+ "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND clientID = "
					+ client.getClientID() + " AND status = '" + statusAsString + "' AND " + dateType + " >= '"
					+ dateFrom + "' AND " + dateType + " <= '" + dateTo + "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

	/**
	 * filter monitorings by name and status for advanced search
	 * 
	 * @param client
	 * @param status
	 * @return
	 */
	public List<MonitoringDTO> filterByNameStatus(int caseWorkerID, Client client, MonitoringStatusEnum status) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String statusAsString = status.getDescription();
		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE clientID = " + client.getClientID() + " AND status = '"
					+ statusAsString + "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND clientID = "
					+ client.getClientID() + " AND status = '" + statusAsString + "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

	/**
	 * filter monitorings by name and date for advanced search
	 * 
	 * @param client
	 * @param date
	 * @return
	 */
	public List<MonitoringDTO> filterByNameDate(int caseWorkerID, Client client, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();

		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE clientID = " + client.getClientID() + "' AND " + dateType + " >= '"
					+ dateFrom + "' AND " + dateType + " <= '" + dateTo + "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND clientID = "
					+ client.getClientID() + "' AND " + dateType + " >= '" + dateFrom + "' AND " + dateType + " <= '"
					+ dateTo + "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

	/**
	 * filter monitorings by status and date for advanced search
	 * 
	 * @param status
	 * @param date
	 * @return
	 */
	public List<MonitoringDTO> filterByStatusDate(int caseWorkerID, MonitoringStatusEnum status, LocalDate dateFrom,
			LocalDate dateTo, String dateType) {
		List<MonitoringDTO> monitoringList = new ArrayList<>();
		String statusAsString = status.getDescription();
		String sql = null;
		if (caseWorkerID == -1) {
			sql = "SELECT * FROM Monitorings WHERE status = '" + statusAsString + "' AND " + dateType + " >= '"
					+ dateFrom + "' AND " + dateType + " <= '" + dateTo + "';";
		} else {
			sql = "SELECT * FROM Monitorings WHERE caseWorkerID = " + caseWorkerID + " AND status = '" + statusAsString
					+ "' AND " + dateType + " >= '" + dateFrom + "' AND " + dateType + " <= '" + dateTo + "';";
		}

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonitoringDTO monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));

				monitoringList.add(monitoring); // adds monitoringDTO to list
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringList;
	}

}
