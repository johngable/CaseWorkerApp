package datasource;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
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
 * Real MonitoringsRowDataGatewayRDS to execute SQL statements on the DB
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringsRowDataGatewayRDS extends MonitoringsRowDataGateway {

	private int monitoringID;

	/**
	 * 
	 * @param monitoringID - ID of monitoring we want to gather data about
	 */
	public MonitoringsRowDataGatewayRDS(int monitoringID) {
		this.monitoringID = monitoringID;
	}

	/**
	 * This constructor is used to add a new entry into the Monitorings table
	 * 
	 * @param dateAndTime  - localDateTime for the monitoring which must be split
	 *                     into date and time
	 * @param clientID     - ID of client associated with monitoring
	 * @param caseworkerID - ID of caseworker associated with monitoring
	 * @param questions    - list of questions that will need to be split into
	 *                     strings
	 * @param answers      - list of clients answers to the questions that will need
	 *                     to be split into strings
	 * @param status       - a status from the enum -> we store the description and
	 *                     in the getter compare descriptions to return a status
	 */
	public MonitoringsRowDataGatewayRDS(LocalDateTime visitDateTime, LocalDateTime dueDateTime,
			LocalDateTime approvedDateTime, int clientID, int caseworkerID, List<String> questions,
			List<String> answers, MonitoringStatusEnum status, int version, boolean isDownloaded) {

		Date visitDate;
		Time visitTime;
		Date dueDate;
		Time dueTime;
		Date approvedDate;
		Time approvedTime;

		// Visit Time
		if (visitDateTime != null) {
			visitDate = Date.valueOf(visitDateTime.toLocalDate());
			visitTime = Time.valueOf(visitDateTime.toLocalTime());

		} else {
			visitDate = null;
			visitTime = null;
		}

		// Due Date
		if (dueDateTime != null) {
			dueDate = Date.valueOf(dueDateTime.toLocalDate());
			dueTime = Time.valueOf(dueDateTime.toLocalTime());
		} else {
			dueDate = null;
			dueTime = null;
		}

		// Approved Date
		if (approvedDateTime != null) {
			approvedDate = Date.valueOf(approvedDateTime.toLocalDate());
			approvedTime = Time.valueOf(approvedDateTime.toLocalTime());
		} else {
			approvedDate = null;
			approvedTime = null;
		}

		int downloaded = 0;
		// isDownloaded
		if (isDownloaded) {
			downloaded = 1;
		}

		try {

			PreparedStatement insert = DatabaseManager.getSingleton().getConnection().prepareStatement(
					"INSERT INTO Monitorings (visitDate, visitTime, dueDate, dueTime, approvedDate, approvedTime, clientID, caseWorkerID, question1, question2, question3, question4, question5, question6, question7, question8, answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, status, version, isDownloaded)"
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			insert.setDate(1, visitDate);
			insert.setTime(2, visitTime);
			insert.setDate(3, dueDate);
			insert.setTime(4, dueTime);
			insert.setDate(5, approvedDate);
			insert.setTime(6, approvedTime);
			insert.setInt(7, clientID);
			insert.setInt(8, caseworkerID);

			if (questions != null) {
				insert.setString(9, questions.get(0));
				insert.setString(10, questions.get(1));
				insert.setString(11, questions.get(2));
				insert.setString(12, questions.get(3));
				insert.setString(13, questions.get(4));
				insert.setString(14, questions.get(5));
				insert.setString(15, questions.get(6));
				insert.setString(16, questions.get(7));
			} else {
				insert.setString(9, null);
				insert.setString(10, null);
				insert.setString(11, null);
				insert.setString(12, null);
				insert.setString(13, null);
				insert.setString(14, null);
				insert.setString(15, null);
				insert.setString(16, null);
			}

			if (answers != null) {
				insert.setString(17, answers.get(0));
				insert.setString(18, answers.get(1));
				insert.setString(19, answers.get(2));
				insert.setString(20, answers.get(3));
				insert.setString(21, answers.get(4));
				insert.setString(22, answers.get(5));
				insert.setString(23, answers.get(6));
				insert.setString(24, answers.get(7));
			} else {
				insert.setString(17, null);
				insert.setString(18, null);
				insert.setString(19, null);
				insert.setString(20, null);
				insert.setString(21, null);
				insert.setString(22, null);
				insert.setString(23, null);
				insert.setString(24, null);
			}

			insert.setString(25, status.getDescription());
			insert.setInt(26, version);
			insert.setInt(27, downloaded);
			insert.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets monitoringDTO that will be downloaded. Sets isDownloaded field to true
	 */
	@Override
	public MonitoringDTO getMonitoringForDownload() {
		String sql = new String("SELECT * FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		MonitoringDTO monitoring = null;
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				monitoring = new MonitoringDTO(getMonitoringID(rs), getVisitDate(rs), getDueDate(rs),
						getApprovedDate(rs), getClient(rs), getCaseWorker(rs), getQuestions(rs), getAnswers(rs),
						getStatus(rs), getVersion(rs), false, getIsDownloaded(rs));
			}
			setIsDownloaded(true);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoring;
	}

	/**
	 * @return int - returns ID of monitoring we are looking at
	 */
	@Override
	public int getMonitoringID() {
		return monitoringID;
	}

	/**
	 * @return LocalDateTime - Date + Time monitoring was performed
	 */
	@Override
	public LocalDateTime getVisitDate() {
		LocalDate date;
		LocalTime time;
		LocalDateTime dateTime = null;

		String sql = new String(
				"SELECT visitDate, visitTime FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (rs.getDate("visitDate") != null && rs.getTime("visitTime") != null) {
					date = rs.getDate("visitDate").toLocalDate();
					time = rs.getTime("visitTime").toLocalTime();
					dateTime = LocalDateTime.of(date, time);
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @return dateTime - Date + Time a monitoring was due
	 */
	@Override
	public LocalDateTime getDueDate() {
		Statement statement;
		LocalDate date;
		LocalTime time;
		LocalDateTime dateTime = null;

		String sql = new String("SELECT dueDate, dueTime FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (rs.getDate("dueDate") != null && rs.getTime("dueTime") != null) {
					date = rs.getDate("dueDate").toLocalDate();
					time = rs.getTime("dueTime").toLocalTime();
					dateTime = LocalDateTime.of(date, time);
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @return dateTime - Date + Time a monitoring was approved
	 */
	@Override
	public LocalDateTime getApprovedDate() {
		Statement statement = null; // Statement statement = m_dbConn.createStatement();
		LocalDate date;
		LocalTime time;
		LocalDateTime dateTime = null;

		String sql = new String(
				"SELECT approvedDate, approvedTime FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (rs.getDate("approvedDate") != null && rs.getTime("approvedTime") != null) {
					date = rs.getDate("approvedDate").toLocalDate();
					time = rs.getTime("approvedTime").toLocalTime();
					dateTime = LocalDateTime.of(date, time);
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * @return int - ID of the client stored in the montoring row
	 */
	@Override
	public Client getClient() {
		int clientID = -1;

		String sql = new String("SELECT clientID FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				clientID = rs.getInt("clientID");
				// for now we build client object using the enums until we can use DB data
				Client client;
				for (ClientEnum c : ClientEnum.values()) {
					if (c.getClientID() == clientID) {
						client = new Client(c.getClientName(), c.getClientID());
						return client;
					}
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return int - ID of the caseworker stored in the montoring row
	 */
	@Override
	public CaseWorker getCaseworker() {
		int caseWorkerID = -1;

		String sql = new String("SELECT caseWorkerID FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				caseWorkerID = rs.getInt("caseWorkerID");
				// for now we build caseworker object using the enums until we can use DB data
				CaseWorker caseWorker;
				for (CaseWorkerEnum c : CaseWorkerEnum.values()) {
					if (c.getCaseWorkerID() == caseWorkerID) {
						caseWorker = new CaseWorker(c.getCaseWorkerName(), c.getCaseWorkerID());
						return caseWorker;
					}
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return List<String> - List of questions
	 */
	@Override
	public List<String> getQuestions() {
		List<String> questions = new ArrayList<>();

		String sql = new String(
				"SELECT question1, question2, question3, question4, question5, question6, question7, question8 FROM Monitorings WHERE monitoringID = "
						+ monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				for (int i = 1; i <= 8; i++) {
					if (rs.getString("question" + i) != null) {
						questions.add(rs.getString("question" + i));
					}
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return questions;
	}

	/**
	 * @return List<String> - List of answers
	 */
	@Override
	public List<String> getAnswers() {
		List<String> answers = new ArrayList<>();

		String sql = new String(
				"SELECT answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8 FROM Monitorings WHERE monitoringID = "
						+ monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				for (int i = 1; i <= 8; i++) {
					if (rs.getString("answer" + i) != null) {
						answers.add(rs.getString("answer" + i));
					}
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		if (answers.size() == 0) {
			return null;
		} else {
			return answers;
		}
	}

	/**
	 * @return MonitoringStatusEnum - current status of the monitoring
	 */
	@Override
	public MonitoringStatusEnum getStatus() {
		MonitoringStatusEnum monitoringStatus = null;

		String sql = new String("SELECT status FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String status = rs.getString("status");
				for (MonitoringStatusEnum m : MonitoringStatusEnum.values()) {
					if (status.equals(m.getDescription())) {
						monitoringStatus = m;
					}
				}
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return monitoringStatus;
	}

	/**
	 * @return version - current version of the monitoring
	 */
	@Override
	public int getVersion() {
		int version = -1;

		String sql = new String("SELECT version FROM Monitorings WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				version = rs.getInt("version");
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		return version;
	}

	/**
	 * @param List<String> - list containing new answers to update the monitoring
	 */
	@Override
	public void setAnswers(List<String> newAnswers) {
		String sql = new String("UPDATE Monitorings SET answer1 = '" + newAnswers.get(0) + "', answer2 = '"
				+ newAnswers.get(1) + "', answer3 = '" + newAnswers.get(2) + "', answer4 = '" + newAnswers.get(3)
				+ "', answer5 = '" + newAnswers.get(4) + "', answer6 = '" + newAnswers.get(5) + "', answer7 = '"
				+ newAnswers.get(6) + "', answer8 = '" + newAnswers.get(7) + "' WHERE monitoringID = " + monitoringID
				+ ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		setVersion(getVersion() + 1);
	}

	/**
	 * @param MonitoringStatusEnum - new status the monitoring should be in
	 */
	@Override
	public void setStatus(MonitoringStatusEnum newStatus) {
		String statusAsString = newStatus.getDescription();

		String sql = new String(
				"UPDATE Monitorings SET status = '" + statusAsString + "' WHERE monitoringID = " + monitoringID + ";");
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}

		setVersion(getVersion() + 1);
	}

	/**
	 * @param MonitoringStatusEnum - new status the monitoring should be in
	 */
	@Override
	public void setVisitDateTime(LocalDateTime visitDateTime) {

		LocalDate visitDate = visitDateTime.toLocalDate();
		LocalTime visitTime = visitDateTime.toLocalTime();

		String setDate = new String(
				"UPDATE Monitorings SET visitDate = '" + visitDate + "' WHERE monitoringID = " + monitoringID + ";");
		String setTime = new String(
				"UPDATE Monitorings SET visitTime = '" + visitTime + "' WHERE monitoringID = " + monitoringID + ";");
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			statement.executeUpdate(setDate);
			statement.executeUpdate(setTime);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		setVersion(getVersion() + 1);
	}

	@Override
	public void setVersion(int version) {
		String setVer = new String(
				"UPDATE Monitorings SET version = " + version + " WHERE monitoringID = " + monitoringID + ";");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			statement.executeUpdate(setVer);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates isDownlaoded field
	 */
	@Override
	public void setIsDownloaded(boolean isDownloaded) {
		String sql;
		if (isDownloaded) {
			sql = new String(
					"UPDATE Monitorings SET isDownloaded = " + 1 + " WHERE monitoringID = " + monitoringID + ";");
		} else {
			sql = new String(
					"UPDATE Monitorings SET isDownloaded = " + 0 + " WHERE monitoringID = " + monitoringID + ";");
		}
		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls the other setters when completely updating a monitoring
	 * 
	 * @param answers   - the updated answers
	 * @param status    - the updated status
	 * @param visitDate - the new visit date
	 * @param version   - the updated version number
	 */
	public void setMultiple(List<String> answers, MonitoringStatusEnum status, LocalDateTime visitDate, int version) {
		int oldVersion = version;
		setAnswers(answers);
		setStatus(status);
		setVisitDateTime(visitDate);
		setVersion(oldVersion + 1);
		setIsDownloaded(false); // isDownloaded set to false when monitoring is uploaded
	}

	/**
	 * 
	 * @return int - current value of the auto increment function
	 */
	public int getAutoIncrementMax() {
		int maxID = 0;
		String sql = new String("SELECT LAST_INSERT_ID();");

		try {
			Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				maxID = rs.getInt("LAST_INSERT_ID()");
			}
		} catch (SQLException | DatabaseException e) {
			e.printStackTrace();
		}
		return maxID;
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
				if (rs.getString("answer"+i) != null ){
					answers.add(rs.getString("answer"+i));
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
				if (rs.getString("question"+i) != null ){
					questions.add(rs.getString("question"+i));
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

}
