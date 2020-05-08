package datasource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * File Table Data Gateway to get monitorings from downloaded files
 * 
 * @author Ian Leiby
 * 
 */
public class FileTableDataGateway extends MonitoringsTableDataGateway {

	private Object monitoringListObject;
	private JSONObject monitoringJSONObject;

	public FileTableDataGateway() {
	}

	/**
	 * Returns list of monitorings we have downloaded based on client
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByClient(Client client) {
		List<MonitoringDTO> monitoringsByClient = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String[] pathnames;
		String path = "downloads/" + client.getClientName() + "/";
		File downloadsFolder = new File(path);
		pathnames = downloadsFolder.list();

		for (String pathname : pathnames) {
			String filePath = path + pathname;

			try {
				monitoringListObject = parser.parse(new FileReader(filePath));
				JSONArray arr = null;
				try {
					arr = (JSONArray) new JSONTokener(monitoringListObject.toString()).nextValue();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				for (int i = 0; i < arr.length(); i++) {
					monitoringJSONObject = arr.getJSONObject(i);
					if (client.getClientID() == monitoringJSONObject.getJSONObject("client").getInt("clientID")) {
						MonitoringDTO monitoringDTO = convertJsonObjectToMonitoringDTO(monitoringJSONObject);
						monitoringsByClient.add(monitoringDTO);
					}
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return monitoringsByClient;
	}

	/**
	 * Gets all locally downloaded clients of a given caseworker
	 * 
	 * @param caseWorkerID
	 * @return list of clients that are downloaded
	 */
	public List<Client> getDownloadedClients(int caseWorkerID) {
		List<Client> monitoringsByClient = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String[] clientpathnames;
		String downloadpath = "downloads/";
		File downloadsFolder = new File(downloadpath);
		clientpathnames = downloadsFolder.list();

		for (String clientpathname : clientpathnames) {
			String clientpath = "downloads/" + clientpathname + "/";
			File clientFolder = new File(clientpath);
			String[] monitoringpathnames = clientFolder.list();
			String filePath = clientpath + monitoringpathnames[0];
			try {
				monitoringListObject = parser.parse(new FileReader(filePath));
				JSONObject obj = null;
				try {
					obj = (JSONObject) new JSONTokener(monitoringListObject.toString()).nextValue();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String cwName = monitoringJSONObject.getJSONObject("caseWorker").getString("caseWorkerName");
				int cwID = monitoringJSONObject.getJSONObject("caseWorker").getInt("caseWorkerID");
				CaseWorker caseWorker = new CaseWorker(cwName, cwID);
				if (caseWorkerID == caseWorker.getCaseWorkerID()) {
					String clientName = monitoringJSONObject.getJSONObject("client").getString("clientName");
					int clientID = monitoringJSONObject.getJSONObject("client").getInt("clientID");
					Client client = new Client(clientName, clientID);
					monitoringsByClient.add(client);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return monitoringsByClient;
	}

	/**
	 * Searches downloaded files for monitorings where status matches requested
	 * 
	 * @param - monitoringStatus we want to match
	 * @return list of monitorings with that status
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByStatus(int caseWorkerID, MonitoringStatusEnum monitoringStatus) {
		List<MonitoringDTO> monitoringsByStatus = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String[] clientpathnames;
		String downloadpath = "downloads/";
		File downloadsFolder = new File(downloadpath);
		clientpathnames = downloadsFolder.list();

		for (String clientpathname : clientpathnames) {
			String clientpath = "downloads/" + clientpathname + "/";
			File clientFolder = new File(clientpath);
			String[] monitoringpathnames = clientFolder.list();
			for (String monitoringpath : monitoringpathnames) {
				String filePath = monitoringpath + monitoringpathnames;
				try {
					monitoringListObject = parser.parse(new FileReader(filePath));
					JSONArray arr = null;
					try {
						arr = (JSONArray) new JSONTokener(monitoringListObject.toString()).nextValue();
					} catch (JSONException e) {
						e.printStackTrace();
					}

					for (int i = 0; i < arr.length(); i++) {
						monitoringJSONObject = arr.getJSONObject(i);
						// if the status matches what we are searching for --> convert to DTO and add to
						// list
						if (monitoringStatus.toString().equals(monitoringJSONObject.getString("status"))) {
							MonitoringDTO monitoringDTO = convertJsonObjectToMonitoringDTO(monitoringJSONObject);
							monitoringsByStatus.add(monitoringDTO);
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		return monitoringsByStatus;
	}

	/**
	 * Returns list of monitorings from the file downloaded for the client
	 * 
	 * @param - ClientID whose monitorings we want
	 * @return - List of monitorings for the client from the downloaded file
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByClientId(int caseWorkerID, int clientID) {
		Client client = null;
		for (ClientEnum c : ClientEnum.values()) {
			if (c.getClientID() == clientID) {
				client = new Client(c.getClientName(), c.getClientID());
			}
		}
		return getMonitoringsByClient(client);
	}

	/**
	 * Returns list of monitorings from the file downloaded for the caseworker
	 * 
	 * @param - CaseWorkerID whomst monitorings we want
	 * @return - List of monitorings for the caseworker from the downloaded file
	 */
	@Override
	public List<MonitoringDTO> getMonitoringsByCaseWorkerID(int caseWorkerID) {
		// get all clients for caseworker
		List<Client> clientList = new ArrayList<>();
		Client client = null;
		for (ClientEnum c : ClientEnum.values()) {
			if (c.getCaseWorkerID() == caseWorkerID) {
				client = new Client(c.getClientName(), c.getClientID());
				clientList.add(client);
			}
		}
		// get all monitorings for each client in the list
		List<MonitoringDTO> monitoringsForCaseWorker = new ArrayList<>();
		for (Client c : clientList) {
			monitoringsForCaseWorker.addAll(getMonitoringsByClient(c));
		}
		return monitoringsForCaseWorker;
	}

	/**
	 * Method returns all monitorings from all dates before the given date
	 * 
	 * @param date     - date 1 month ago
	 * @param clientID of client we want monitorings for
	 * @return list of monitorings for the client from downloaded file
	 */
	public List<MonitoringDTO> getMonitoringsByDate(int caseWorkerID, LocalDate date, int clientID) {
		List<MonitoringDTO> monitoringsByDate = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String[] clientpathnames;
		String downloadpath = "downloads/";
		File downloadsFolder = new File(downloadpath);
		clientpathnames = downloadsFolder.list();

		for (String clientpathname : clientpathnames) {
			String clientpath = "downloads/" + clientpathname + "/";
			File clientFolder = new File(clientpath);
			String[] monitoringpathnames = clientFolder.list();
			for (String monitoringpath : monitoringpathnames) {
				String filePath = monitoringpath + monitoringpathnames;
				try {
					monitoringListObject = parser.parse(new FileReader(filePath));
					JSONArray arr = null;
					try {
						arr = (JSONArray) new JSONTokener(monitoringListObject.toString()).nextValue();
					} catch (JSONException e) {
						e.printStackTrace();
					}

					for (int i = 0; i < arr.length(); i++) {
						monitoringJSONObject = arr.getJSONObject(i);
						// if the status matches what we are searching for --> convert to DTO and add to
						// list
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate d = LocalDate.parse(monitoringJSONObject.getString("visitDate"), formatter);
						if (d.isBefore(date)) {
							MonitoringDTO monitoringDTO = convertJsonObjectToMonitoringDTO(monitoringJSONObject);
							monitoringsByDate.add(monitoringDTO);
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		return monitoringsByDate;
	}

	/**
	 * Converts JSONObject to monitoringDTO
	 * 
	 * @param monitoringJSONObject2 - object to convert
	 * @return MonitoringDTO from json object
	 */
	private MonitoringDTO convertJsonObjectToMonitoringDTO(JSONObject monitoringJSONObject) {
		// caseworker
		JSONObject retrievedCaseWorker = monitoringJSONObject.getJSONObject("caseWorker");
		CaseWorker retrievedCaseWorkerObject = new CaseWorker(retrievedCaseWorker.getString("caseWorkerName"),
				retrievedCaseWorker.getInt("caseWorkerID"));
		// client
		JSONObject retrievedClient = monitoringJSONObject.getJSONObject("client");
		Client retrievedClientObject = new Client(retrievedClient.getString("clientName"),
				retrievedClient.getInt("clientID"));
		// Date fields
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime visitDate = null;
		LocalDateTime dueDate = null;
		LocalDateTime approvedDate = null;
		if (monitoringJSONObject.has("visitDate")) {
			String visitDateStr = monitoringJSONObject.getString("visitDate").replace("T", " ");
			visitDate = LocalDateTime.parse(visitDateStr, formatter);
		}
		if (monitoringJSONObject.has("dueDate")) {
			String dueDateStr = monitoringJSONObject.getString("dueDate").replace("T", " ");
			dueDate = LocalDateTime.parse(dueDateStr, formatter);
		}
		if (monitoringJSONObject.has("approvedDate")) {
			String approvedDateStr = monitoringJSONObject.getString("approvedDate").replace("T", " ");
			approvedDate = LocalDateTime.parse(approvedDateStr, formatter);
		}
		// questions
		JSONArray questionsArr = monitoringJSONObject.getJSONArray("questions");
		List<String> questions = new ArrayList<>();
		for (int j = 0; j < questionsArr.length(); j++) {
			questions.add(questionsArr.getString(j));
		}
		// answers
		JSONArray answersArr = monitoringJSONObject.getJSONArray("answers");
		List<String> answers = new ArrayList<>();
		for (int j = 0; j < answersArr.length(); j++) {
			answers.add(answersArr.getString(j));
		}
		// status
		MonitoringStatusEnum status = null;
		for (MonitoringStatusEnum monitoringStatus : MonitoringStatusEnum.values()) {
			if (monitoringStatus.toString().equals(monitoringJSONObject.getString("status"))) {
				status = monitoringStatus;
			}
		}

		MonitoringDTO monitoringDTO = new MonitoringDTO(monitoringJSONObject.getInt("monitoringID"), visitDate, dueDate,
				approvedDate, retrievedClientObject, retrievedCaseWorkerObject, questions, answers, status,
				monitoringJSONObject.getInt("version"), monitoringJSONObject.getBoolean("isLocal"),
				monitoringJSONObject.getBoolean("isDownloaded"));

		return monitoringDTO;
	}

	@Override
	public List<Client> getDownloadedClients() {
		return null;
	}

	@Override
	public List<MonitoringDTO> filterByNameStatus(int caseWorkerID, Client client, MonitoringStatusEnum status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitoringDTO> getMonitoringsByDate(int caseWorkerID, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitoringDTO> filterByNameStatusDate(int caseWorkerID, Client client, MonitoringStatusEnum status,
			LocalDate dateFrom, LocalDate dateTo, String dateType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitoringDTO> filterByNameDate(int caseWorkerID, Client client, LocalDate dateFrom, LocalDate dateTo,
			String dateType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitoringDTO> filterByStatusDate(int caseWorkerID, MonitoringStatusEnum status, LocalDate dateFrom,
			LocalDate dateTo, String dateType) {
		// TODO Auto-generated method stub
		return null;
	}

}
