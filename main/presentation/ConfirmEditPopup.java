package presentation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JTextPane;

import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsRowDataGatewayMock;
import model.CaseWorker;
import model.Client;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class ConfirmEditPopup implements ActionListener{

	private JFrame frame;
	private JButton btnCancel;
	private JButton btnConfirm;
	private MonitoringDTO monitoring;
	private int caseWorkerID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int monitoringID = 1;
					LocalDateTime date = LocalDateTime.now();
					Client client = new Client("Amy Jones", 3);
					CaseWorker caseworker = new CaseWorker("Greg Black", 3);
					List<String> questions = Arrays.asList("Question 1", "Question 2");
					List<String> answers = Arrays.asList("Answer 1", "Answer 2");
					MonitoringStatusEnum status = MonitoringStatusEnum.SCHEDULED;
					int version = 2;
					boolean isLocal = true;
					boolean isDownloaded = true;

					MonitoringDTO localDTO = new MonitoringDTO(monitoringID, date, date, null, client, caseworker, questions,
							answers, status, 2, isLocal, isDownloaded);
					
					ConfirmEditPopup window = new ConfirmEditPopup(localDTO, localDTO.getCaseWorker().getCaseWorkerID());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfirmEditPopup(MonitoringDTO monitoring, int caseWorkerID) {
		this.caseWorkerID = caseWorkerID;
		this.monitoring = monitoring;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 187);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLblconfirmeditpopup = new JLabel("Are you sure?");
		lblLblconfirmeditpopup.setFont(new Font("Lucida Grande", Font.BOLD, 22));
		lblLblconfirmeditpopup.setBounds(139, 16, 164, 31);
		frame.getContentPane().add(lblLblconfirmeditpopup);
		
		JLabel lblNewLabel = new JLabel("This monitoring is downloaded on another machine");
		lblNewLabel.setBounds(49, 49, 332, 39);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblYourChangesMay = new JLabel("Your changes may overwrite work in progress or be overwritten");
		lblYourChangesMay.setBounds(26, 77, 407, 26);
		frame.getContentPane().add(lblYourChangesMay);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(49, 115, 117, 29);
		btnCancel.addActionListener(this);
		frame.getContentPane().add(btnCancel);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.setBounds(276, 115, 117, 29);
		btnConfirm.addActionListener(this);
		frame.getContentPane().add(btnConfirm);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			frame.dispose();
		}
		
		if (e.getSource() == btnConfirm) {
			MonitoringEditingWindow caseWorkerWindow = new MonitoringEditingWindow(monitoring, caseWorkerID);
			frame.dispose();
		}
		
	}
}
