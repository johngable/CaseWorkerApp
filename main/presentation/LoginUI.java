package presentation;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import model.AuthenticateCommand;
import model.ModelFacade;
import reports.LoginFailedReport;
import reports.LoginSuccessReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;

/**
 * the UI screen for logging into the system
 */
public class LoginUI implements ReportObserver, State {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private ModelFacade remote = ModelFacade.getSingleton();
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	private JLabel errorMessageLabel = new JLabel("Invalid Credentials. Please try again");
	private JLabel successMessageLabel = new JLabel("Successful Login");
	private String username;
	private String password;

	/**
	 * Launch the window
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					LoginUI window = new LoginUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the window
	 */
	public LoginUI() {
		initialize();

	}

	/**
	 * Initialize the contents of the window: username and password fields and
	 * submit button
	 */
	private void initialize() {
		roc.registerObserver(this, LoginSuccessReport.class);
		roc.registerObserver(this, LoginFailedReport.class);
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(170, 97, 216, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText("CaseWorker1");

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(63, 99, 89, 15);
		frame.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(63, 147, 89, 15);
		frame.getContentPane().add(lblPassword);

		JButton btnSubmit = new JButton("Submit");
		AbstractAction buttonPressed = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				username = textField.getText();
				char[] charArr = passwordField.getPassword();
				password = String.valueOf(charArr);
				remote.queueCommand(new AuthenticateCommand(username, password));
			}
		};
		btnSubmit.addActionListener(buttonPressed);
		btnSubmit.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "Enter_pressed");
		btnSubmit.getActionMap().put("Enter_pressed", buttonPressed);
		
		btnSubmit.setBounds(263, 238, 117, 25);
		frame.getContentPane().add(btnSubmit);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 16));
		lblLogin.setBounds(206, 27, 59, 25);
		frame.getContentPane().add(lblLogin);

		passwordField = new JPasswordField();
		passwordField.setBounds(170, 145, 216, 19);
		passwordField.setText("password");
		frame.getContentPane().add(passwordField);
		/*
		 * Added code for hidden password
		 */
		JCheckBox checkPassword = new JCheckBox("Show Password");
		checkPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkPassword.isSelected()) {
					passwordField.setEchoChar((char) 0);
				} else {
					passwordField.setEchoChar('*');
				}
			}
		});

		checkPassword.setBounds(166, 165, 216, 19);
		frame.getContentPane().add(checkPassword);
		frame.setLocationRelativeTo(null);
	}
	// End Password hidden icon

	/**
	 * @author Ryan Hippenstiel This method shows the failed message if login
	 *         credentials were invalid
	 */
	public void loginFailedMessage() {
		errorMessageLabel.setBounds(90, 69, 269, 19);
		errorMessageLabel.setForeground(Color.red);
		frame.getContentPane().add(errorMessageLabel);
		frame.validate();
		frame.repaint();
	}

	/**
	 * @author Ryan Hippenstiel This method shows the success message if login
	 *         credentials were valid
	 */
	public void loginSuccess() {
		frame.remove(errorMessageLabel);
		successMessageLabel.setBounds(170, 69, 269, 19);
		successMessageLabel.setForeground(Color.green);
		frame.getContentPane().add(successMessageLabel);
		frame.validate();
		frame.repaint();
	}

	/**
	 * Method to set frame visible (needed for state machine)
	 * 
	 * @param b from state machine
	 */
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	/**
	 * @see reports.ReportObserver#receiveReport(reports.Report)
	 */
	@Override
	public void receiveReport(Report report) {
		if (report.getClass() == LoginSuccessReport.class) {
			try {

				loginSuccess();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (report.getClass() == LoginFailedReport.class) {

			loginFailedMessage();

		}
	}

	/**
	 * Set the state of the Launcher to this
	 * 
	 * @param machine the Launcher controlling the window
	 */
	public void doAction(Launcher machine) {
		machine.setState(this);
	}
}
