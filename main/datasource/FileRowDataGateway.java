package datasource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
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
 * Row gateway for downloaded files
 * 
 * @author michaelpermyashkin
 *
 */
public class FileRowDataGateway extends MonitoringsRowDataGateway {

	private Client client;
	private int monitoringID;
	private Object monitoringListObject;
	private MonitoringDTO monitoringDTO;
	private JSONObject monitoringJSONObject;
	private List<JSONObject> fileMonitoringObjects = new ArrayList<>();

	/**
	 * checks to see if the file already exists
	 * @param monitoringID
	 * @return the monitoringDTO if exists  or null if it doesn't
	 */
	public static MonitoringDTO ifFileExists(int monitoringID, String clientName) {
		clientName = clientName.replace(" ", "");
		File monitoringFile; 
		if (OptionsManager.getSingleton().isUsingMockDataSource()) {
			monitoringFile = new File("testDownloads/" + clientName + "/" + monitoringID + ".json");
		} else {
			monitoringFile = new File("downloads/" + clientName + "/" +monitoringID + ".json");
		}
		
		if(monitoringFile.exists()) {
			
			return MonitoringDownloadGateway.getMonitoringDataFromFileByID(monitoringID, clientName);
		}
		return null;
	}
	
	/**
	 * 
	 * @param client       - client object whomst monitoring we want to find
	 * @param monitoringID - the id of monitoring we want to find
	 */
	public FileRowDataGateway(int monitoringID, String clientName) {
		this.monitoringID = monitoringID;

		JSONParser parser = new JSONParser();
		clientName = clientName.replace(" ", "");

		try {
			FileReader file = new FileReader("downloads/" + clientName + "/" + monitoringID + ".json");
			monitoringListObject = parser.parse(file);
			file.close();
			JSONArray arr = null;
			try {
				arr = (JSONArray) new JSONTokener(monitoringListObject.toString()).nextValue();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// iterate through file until we find the right monitoringID
			// when we find the correct ID, we convert that one to a MonitoringDTO
			for (int i = 0; i < arr.length(); i++) {
				if (arr.getJSONObject(i).get("monitoringID").equals(monitoringID)) {
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
					monitoringDTO = new MonitoringDTO(monitoringJSONObject.getInt("monitoringID"), visitDate, dueDate,
							approvedDate, retrievedClientObject, retrievedCaseWorkerObject, questions, answers, status,
							monitoringJSONObject.getInt("version"), monitoringJSONObject.getBoolean("isLocal"),
							monitoringJSONObject.getBoolean("isDownloaded"));
				} else {
					fileMonitoringObjects.add(arr.getJSONObject(i));
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

	/**
	 * @return monitoringID of monitoring
	 */
	@Override
	public int getMonitoringID() {
		return monitoringDTO.getMonitoringID();
	}

	/**
	 * @return Visit date of monitoring
	 */
	@Override
	public LocalDateTime getVisitDate() {
		return monitoringDTO.getVisitDate();
	}

	/**
	 * @return Due date of monitoring
	 */
	@Override
	public LocalDateTime getDueDate() {
		return monitoringDTO.getDueDate();
	}

	/**
	 * @return Approved date of monitoring
	 */
	@Override
	public LocalDateTime getApprovedDate() {
		return monitoringDTO.getApprovedDate();
	}

	/**
	 * @return client in monitoring
	 */
	@Override
	public Client getClient() {
		return monitoringDTO.getClient();
	}

	/**
	 * @return caseworker in monitoring
	 */
	@Override
	public CaseWorker getCaseworker() {
		return monitoringDTO.getCaseWorker();
	}

	/**
	 * @return questions in monitoring
	 */
	@Override
	public List<String> getQuestions() {
		return monitoringDTO.getQuestions();
	}

	/**
	 * @return answers in monitoring
	 */
	@Override
	public List<String> getAnswers() {
		return monitoringDTO.getAnswers();
	}

	/**
	 * @return status of monitoring
	 */
	@Override
	public MonitoringStatusEnum getStatus() {
		return monitoringDTO.getStatus();
	}

	/**
	 * @return version of monitoring
	 */
	@Override
	public int getVersion() {
		return monitoringDTO.getVersion();
	}

	/**
	 * Sets monitoring version
	 * 
	 * @param visitDateTime
	 */
	@Override
	public void setVisitDateTime(LocalDateTime visitDateTime) {
		monitoringDTO.setVisitDate(visitDateTime);
		setStatus(MonitoringStatusEnum.PENDING_FOR_REVIEW);
		monitoringDTO.setIsEdited(true);
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	/**
	 * Sets the answers in the file and calls downloading gateway to rewrite file
	 * 
	 * @param newAnswers
	 */
	@Override
	public void setAnswers(List<String> newAnswers) {
		monitoringDTO.setAnswers(newAnswers);
		setStatus(MonitoringStatusEnum.PENDING_FOR_REVIEW);
		monitoringDTO.setIsEdited(true);
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	/**
	 * Set status of monitoring
	 * 
	 * @param newStatus
	 */
	@Override
	public void setStatus(MonitoringStatusEnum newStatus) {
		monitoringDTO.setStatus(newStatus);
		monitoringDTO.setIsEdited(true);
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	/**
	 * Sets monitoring version
	 * 
	 * @param version
	 */
	@Override
	public void setVersion(int version) {
		monitoringDTO.setVersion(version);
		monitoringDTO.setIsEdited(true);
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	/**
	 * @return - in the file gateway this doesn't apply so we return -1
	 */
	@Override
	public int getAutoIncrementMax() {
		// not applicable in file gateway
		return -1;
	}

	@Override
	public void setMultiple(List<String> answers, MonitoringStatusEnum status, LocalDateTime visitDate, int version) {
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	@Override
	public void setIsDownloaded(boolean isDownloaded) {
		monitoringDTO.setIsDownloaded(true);
		monitoringDTO.setIsEdited(true);
		MonitoringDownloadGateway downloadGateway = new MonitoringDownloadGateway(monitoringDTO, fileMonitoringObjects);
	}

	/**
	 * Not applicable in file gateway
	 */
	@Override
	public MonitoringDTO getMonitoringForDownload() {
		return null;
	}
}
