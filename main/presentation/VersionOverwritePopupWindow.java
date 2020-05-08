package presentation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sharedData.MonitoringDTO;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import model.GetMonitoringsByCaseWorkerCommand;
import model.ModelFacade;
import model.UploadLocalMonitoringCommand;

public class VersionOverwritePopupWindow {

	private JFrame frame;
	private MonitoringDTO failedDTO;
	private int caseWorkerID;
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue

	/**
	 * Create the application.
	 */
	public VersionOverwritePopupWindow(MonitoringDTO failedDTO, int caseWorkerID) {
		this.failedDTO = failedDTO;
		this.caseWorkerID = caseWorkerID;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 452, 283);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Version Conflict");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblNewLabel.setBounds(96, 6, 239, 29);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("The monitoring you are trying to upload is an");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(44, 45, 352, 29);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("older version than what exists in the database.");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(96, 64, 247, 34);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Client: ");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setBounds(115, 110, 48, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		String clientName = failedDTO.getClient().getClientName();
		JLabel lblClientName = new JLabel(clientName);
		lblClientName.setBounds(173, 110, 192, 20);
		frame.getContentPane().add(lblClientName);
		
		JLabel lblNewLabel_3_1 = new JLabel("Monitoring ID:");
		lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3_1.setBounds(65, 137, 98, 14);
		frame.getContentPane().add(lblNewLabel_3_1);
		
		int monitoringID = failedDTO.getMonitoringID();
		JLabel lblMonitoringID = new JLabel("" + monitoringID);
		lblMonitoringID.setBounds(173, 136, 192, 20);
		frame.getContentPane().add(lblMonitoringID);
		
		JLabel lblNewLabel_4 = new JLabel("What would you like to do?");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_4.setBounds(133, 175, 177, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel.setBounds(65, 200, 118, 23);
		frame.getContentPane().add(btnCancel);
		
		JButton btnUpload = new JButton("Upload Anyway");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remote.queueCommand(new UploadLocalMonitoringCommand(failedDTO, false));
				remote.queueCommand(new GetMonitoringsByCaseWorkerCommand(caseWorkerID));
				frame.dispose();
			}
		});
		btnUpload.setBounds(254, 200, 118, 23);
		frame.getContentPane().add(btnUpload);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
