package datasource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
import model.OptionsManager;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Gateway used to download monitoring objects as json
 * 
 * @author michaelpermyashkin
 *
 */
public class MonitoringDownloadGateway {

	private static JSONArray monitoringList = new JSONArray(); // JSON array to write to file

	/**
	 * 
	 */
	public MonitoringDownloadGateway() {
	}

	/**
	 * Downloads list of monitorings to files names by monitoring ID
	 * 
	 * @param monitorings - list of monitoring DTO's to download
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public MonitoringDownloadGateway(List<MonitoringDTO> monitorings)
			throws FileNotFoundException, IOException, ParseException {
		monitoringList = new JSONArray();

		for (MonitoringDTO m : monitorings) {
			serializeToXML(m);

			// download monitoring object list
			if (OptionsManager.getSingleton().isUsingMockDataSource()) {
				// if in test mode we write to different directory
				String clientName = m.getClient().getClientName();
				clientName = clientName.replace(" ", "");
				// make client directory if it doesn't exist
				File clientDir = new File("testDownloads/" + clientName);
				if (!clientDir.exists()) {
					clientDir.mkdir();
				}

				Path path = Paths.get(clientDir + "/" + m.getMonitoringID() + ".json");
				try {
					Files.deleteIfExists(path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try (FileWriter file = new FileWriter(clientDir + "/" + m.getMonitoringID() + ".json", true)) {

					file.write(monitoringList.toString());
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				

			} else {
				String clientName = m.getClient().getClientName();
				clientName = clientName.replace(" ", "");
				// make client directory if it doesn't exist
				File clientDir = new File("downloads/" + clientName);
				if (!clientDir.exists()) {
					clientDir.mkdir();
				}

				Path path = Paths.get(clientDir + "/" + m.getMonitoringID() + ".json");
				try {
					Files.deleteIfExists(path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try (FileWriter file = new FileWriter(clientDir + "/" + m.getMonitoringID() + ".json", true)) {

					file.write(monitoringList.toString());
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Used when a single monitoring is changed in the file, we only want to rewrite
	 * that part
	 * 
	 * @param monitoring             - monitoringDTO that changed
	 * @param monitoringListFromFile - unchanged JSONObjects from file to right back
	 */
	public MonitoringDownloadGateway(MonitoringDTO monitoring, List<JSONObject> monitoringListFromFile) {
		monitoringList = new JSONArray();
		if (monitoringListFromFile != null) {
			for (JSONObject o : monitoringListFromFile) {
				monitoringList.put(o);
			}
		}
		serializeToXML(monitoring);
		// download monitoring object list
		for (Object obj : monitoringList) {
			if (obj instanceof JSONObject) {
				JSONObject o = (JSONObject) obj;
				if (OptionsManager.getSingleton().isUsingMockDataSource()) {
					// if in test mode we write to different directory
					String clientName = o.getJSONObject("client").getString("clientName");
					clientName = clientName.replace(" ",  "");
					File clientDir = new File("testDownloads/" + clientName);
					if (!clientDir.exists()) {
						clientDir.mkdir();
					}

					Path path = Paths.get(clientDir + "/" + o.getInt("monitoringID") + ".json");
					try {
						Files.deleteIfExists(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try (FileWriter file = new FileWriter(path.toString(), true)) {

						file.write(monitoringList.toString());
						file.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					String clientName = o.getJSONObject("client").getString("clientName");
					clientName = clientName.replace(" ",  "");
					File clientDir = new File("downloads/" + clientName);
					if (!clientDir.exists()) {
						clientDir.mkdir();
					}

					Path path = Paths.get(clientDir + "/" + o.getInt("monitoringID") + ".json");
					try {
						Files.deleteIfExists(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try (FileWriter file = new FileWriter(path.toString(), true)) {

						file.write(monitoringList.toString());
						file.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles serialization of each monitoring that will be downloaded
	 * 
	 * @param monitoring - MonitoringDTO to serialize
	 * @throws IOException
	 */
	private static void serializeToXML(MonitoringDTO monitoring) {
		// create the monitoring object
		JSONObject monitoringDetailsJSON = new JSONObject();
		monitoringDetailsJSON.put("monitoringID", monitoring.getMonitoringID());
		monitoringDetailsJSON.put("visitDate", monitoring.getVisitDate());
		monitoringDetailsJSON.put("dueDate", monitoring.getDueDate());
		monitoringDetailsJSON.put("approvedDate", monitoring.getApprovedDate());
		monitoringDetailsJSON.put("version", monitoring.getVersion());

		// create client object
		JSONObject clientDetailsJSON = new JSONObject();
		clientDetailsJSON.put("clientID", monitoring.getClient().getClientID());
		clientDetailsJSON.put("clientName", monitoring.getClient().getClientName());
		// add client json object to monitoring json object
		monitoringDetailsJSON.put("client", clientDetailsJSON);

		// create caseworker object
		JSONObject caseWorkerDetailsJSON = new JSONObject();
		caseWorkerDetailsJSON.put("caseWorkerID", monitoring.getCaseWorker().getCaseWorkerID());
		caseWorkerDetailsJSON.put("caseWorkerName", monitoring.getCaseWorker().getCaseWorkerName());
		// add caseworker json object to monitoring json object
		monitoringDetailsJSON.put("caseWorker", caseWorkerDetailsJSON);

		monitoringDetailsJSON.put("questions", monitoring.getQuestions());
		monitoringDetailsJSON.put("answers", monitoring.getAnswers());
		monitoringDetailsJSON.put("status", monitoring.getStatus());
		monitoringDetailsJSON.put("isLocal", monitoring.getIsLocallyEdited());
		monitoringDetailsJSON.put("isDownloaded", monitoring.getIsDownloaded());

		// Add monitorings to list
		monitoringList.put(monitoringDetailsJSON);
	}

	/**
	 * Method to help with searching file for monitoring by monitoringID
	 * 
	 * @param monitoringID - the ID of monitoring we want to find in file
	 * @return - MonitoringDTO of the monitoring we matched
	 */
	public static MonitoringDTO getMonitoringDataFromFileByID(int monitoringID, String clientName) {
		JSONParser parser = new JSONParser();
		Object jsonObj;
		File clientDir;
		clientName = clientName.replace(" ", "");

		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			clientDir = new File("testDownloads/" + clientName);
		} else {
			clientDir = new File("downloads/" + clientName);
		}
		try {
			if (clientDir.exists()) {
				JSONArray arr = null;
				File filePath = new File(clientDir+"/"+ + monitoringID + ".json");
			
				if (filePath.exists()) {
					FileReader fileReader = new FileReader(clientDir + "/" + monitoringID + ".json");
					jsonObj = parser.parse(fileReader);
					fileReader.close();
					try {
						arr = (JSONArray) new JSONTokener(jsonObj.toString()).nextValue();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// iterate through file until we find the right monitoringID
					for (int i = 0; i < arr.length(); i++) {
						JSONObject monitoringJSONObject = arr.getJSONObject(i);

						if (monitoringJSONObject.get("monitoringID").equals(monitoringID)) {
							monitoringJSONObject = arr.getJSONObject(i);
							// caseworker
							JSONObject retrievedCaseWorker = monitoringJSONObject.getJSONObject("caseWorker");
							CaseWorker retrievedCaseWorkerObject = new CaseWorker(
									retrievedCaseWorker.getString("caseWorkerName"),
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
							MonitoringStatusEnum status = null;
							for (MonitoringStatusEnum monitoringStatus : MonitoringStatusEnum.values()) {
								if (monitoringStatus.toString().equals(monitoringJSONObject.getString("status"))) {
									status = monitoringStatus;
								}
							}
							return new MonitoringDTO(monitoringJSONObject.getInt("monitoringID"), visitDate, dueDate,
									approvedDate, retrievedClientObject, retrievedCaseWorkerObject, questions, answers,
									status, monitoringJSONObject.getInt("version"),
									monitoringJSONObject.getBoolean("isLocal"),
									monitoringJSONObject.getBoolean("isDownloaded"));
						}
					}
					
				} else {
					// directory exists but monitoring file not found
					return null;
				}
			} else {
				// client directory doesn't exist and client has no downloaded data
				return null;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return null; 
	}
}
