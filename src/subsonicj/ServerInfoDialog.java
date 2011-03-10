package subsonicj;

import java.awt.Color;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;
import javax.swing.*;
import servercontact.Server;
import servercontact.Settings;

public class ServerInfoDialog extends JDialog {

	JPanel mainPanel = new JPanel();
	JPanel selectServerPanel = new JPanel();
	JComboBox serverListComboBox = new JComboBox();
	JButton connectButton1 = new JButton();
	JCheckBox autoSelectCheckBox1 = new JCheckBox();
	JPanel createServerPanel = new JPanel();
	JLabel serverNameLabel = new JLabel();
	JLabel userNameLabel = new JLabel();
	JTextField serverNameTextField = new JTextField();
	JTextField usernameTextField = new JTextField();
	JLabel serverAddressLabel = new JLabel();
	JLabel passwordLabel = new JLabel();
	JTextField serverAddressTextField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JLabel requiredFieldsLabel = new JLabel();
	JButton testConnectionButton = new JButton();
	JButton connectButton2 = new JButton();
	JLabel usernameLabel = new JLabel();
	JLabel serverNameExampleLabel = new JLabel();
	JLabel serverExampleLabel = new JLabel();
	JCheckBox autoSelectCheckBox2 = new JCheckBox();
	JLabel bgIconLabel = new JLabel();
	JLabel errorLabel = new JLabel();
	JLabel orLabel = new JLabel();

	public ServerInfoDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		loadSavedServers();

	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("SubsonicJ Server Selection");
		setIconImage(new ImageIcon(
				Main.class.getResource("/res/Application-256.png")).getImage());
		getContentPane().setLayout(null);
		setSize(530, 460);
		setResizable(false);
		setLocationRelativeTo(null);

		mainPanel.setBackground(new java.awt.Color(34, 34, 34));
		mainPanel.setLayout(null);

		selectServerPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
				"Select a Previously Saved Server",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", Font.BOLD, 13), new java.awt.Color(
						102, 102, 102)));

		selectServerPanel.setLayout(null);
		selectServerPanel.setBackground(mainPanel.getBackground());

		serverListComboBox.setFocusable(false);
		serverListComboBox.setBounds(20, 30, 240, 20);
		selectServerPanel.add(serverListComboBox);

		connectButton1.setText("Connect");
		connectButton1.setEnabled(false);
		connectButton1.setFocusPainted(false);
		connectButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButton1ActionPerformed(evt);
			}
		});
		connectButton1.setBounds(280, 30, 110, 20);
		selectServerPanel.add(connectButton1);

		autoSelectCheckBox1.setForeground(new java.awt.Color(102, 102, 102));
		autoSelectCheckBox1
				.setText("Choose this server every time and don't ask me again");

		autoSelectCheckBox1
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						autoSelectCheckBox1ActionPerformed(evt);
					}
				});
		autoSelectCheckBox1.setBounds(30, 170, 360, 20);
		selectServerPanel.add(autoSelectCheckBox1);

		selectServerPanel.setBounds(40, 30, 410, 100);
		mainPanel.add(selectServerPanel);

		createServerPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
				"Create a New Server Connection",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", Font.BOLD, 13), new java.awt.Color(
						102, 102, 102)));
		createServerPanel.setLayout(null);
		createServerPanel.setBackground(mainPanel.getBackground());

		serverNameLabel.setForeground(new java.awt.Color(102, 102, 102));
		serverNameLabel.setText("Server Name:");
		serverNameLabel.setBounds(20, 30, 200, 20);
		createServerPanel.add(serverNameLabel);

		userNameLabel.setForeground(new java.awt.Color(102, 102, 102));
		userNameLabel.setText("Username:");
		userNameLabel.setBounds(20, 90, 200, 20);
		createServerPanel.add(userNameLabel);

		serverNameTextField.setBounds(30, 50, 150, 20);
		createServerPanel.add(serverNameTextField);
		usernameTextField.setBounds(30, 110, 150, 20);
		createServerPanel.add(usernameTextField);

		serverAddressLabel.setForeground(new java.awt.Color(102, 102, 102));
		serverAddressLabel.setText("Server Address:");
		serverAddressLabel.setBounds(200, 30, 200, 20);
		createServerPanel.add(serverAddressLabel);

		passwordLabel.setForeground(new java.awt.Color(102, 102, 102));
		passwordLabel.setText("Password:");
		passwordLabel.setBounds(200, 90, 200, 20);
		createServerPanel.add(passwordLabel);
		serverAddressTextField.setBounds(210, 50, 180, 20);
		createServerPanel.add(serverAddressTextField);
		passwordField.setBounds(210, 110, 180, 20);
		createServerPanel.add(passwordField);

		requiredFieldsLabel.setForeground(new java.awt.Color(230, 0, 0));
		requiredFieldsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		requiredFieldsLabel.setText("**All Fields Are Required");
		requiredFieldsLabel.setBounds(210, 140, 180, 20);
		createServerPanel.add(requiredFieldsLabel);

		testConnectionButton.setText("Test Connection");
		testConnectionButton.setFocusPainted(false);
		testConnectionButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						testConnectionButtonActionPerformed(evt);
					}
				});
		testConnectionButton.setBounds(150, 200, 125, 22);
		createServerPanel.add(testConnectionButton);

		connectButton2.setText("Connect");
		connectButton2.setEnabled(false);
		connectButton2.setFocusPainted(false);
		connectButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButton2ActionPerformed(evt);
			}
		});
		connectButton2.setBounds(280, 200, 110, 20);
		createServerPanel.add(connectButton2);

		usernameLabel.setForeground(new java.awt.Color(90, 90, 90));
		usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		usernameLabel.setText("Server username");
		usernameLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		usernameLabel.setBounds(30, 130, 150, -1);
		createServerPanel.add(usernameLabel);

		serverNameExampleLabel.setForeground(new java.awt.Color(90, 90, 90));
		serverNameExampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		serverNameExampleLabel.setText("Name this connection");
		serverNameExampleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		serverNameExampleLabel.setBounds(0, 150, 30, 70);
		createServerPanel.add(serverNameExampleLabel);

		serverExampleLabel.setForeground(new java.awt.Color(90, 90, 90));
		serverExampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		serverExampleLabel.setText("e.g. http://127.10.10.10/");
		serverExampleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		serverExampleLabel.setBounds(210, 70, 180, 20);
		createServerPanel.add(serverExampleLabel);

		autoSelectCheckBox2.setForeground(new java.awt.Color(102, 102, 102));
		autoSelectCheckBox2
				.setText("Choose this server every time and don't ask me again");
		autoSelectCheckBox2.setBounds(30, 170, 360, 20);
		autoSelectCheckBox2.setBackground()
		createServerPanel.add(autoSelectCheckBox2);
		createServerPanel.setBounds(40, 150, 410, 240);
		mainPanel.add(createServerPanel);
		createServerPanel.getAccessibleContext().setAccessibleName(
				"Or Create a New Server Connection");

		bgIconLabel.setIcon(new ImageIcon(getClass().getResource(
				"/res/Application-256-trans.png"))); // NOI18N
		bgIconLabel.setText("bgIconLabel");
		bgIconLabel.setBounds(280, 190, 256, 256);
		mainPanel.add(bgIconLabel);

		errorLabel.setForeground(new java.awt.Color(230, 0, 0));
		errorLabel
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						errorLabelPropertyChange(evt);
					}
				});
		errorLabel.setBounds(40, 400, 250, 20);
		mainPanel.add(errorLabel);

		orLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		orLabel.setForeground(new java.awt.Color(102, 102, 102));
		orLabel.setText("OR");
		orLabel.setBounds(230, 120, -1, 20);
		mainPanel.add(orLabel);
		mainPanel.setBounds(0, 0, 530, 460);
		getContentPane().add(mainPanel);

	}

	private void testConnectionButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// view-source:http://bmjones.no-ip.org/music/rest/ping.view?u=Guest&p=notbrett&v=1.1.0&c=myapp
		String svrName = serverNameTextField.getText().trim();
		if (!serverNameAvailable(svrName)) {
			int response = JOptionPane
					.showConfirmDialog(
							rootPane,
							"The servername "
									+ svrName
									+ " has already been saved"
									+ System.getProperty("line.separator")
									+ System.getProperty("line.separator")
									+ "Would you like to overwrite the previously saved server?",
							"OOPS!", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				Settings.deleteServerInfoFile(svrName);
			} else {
				errorLabel.setText("Please select another server name and try again.");
				serverNameTextField.setText("");
			}
		}

		String serverNameS = serverNameTextField.getText().trim();
		String serverAddressS = serverAddressTextField.getText().trim();
		String serverUserS = usernameTextField.getText().trim();
		char[] serverPasswordC = passwordField.getPassword();

		// converts the password to string for passing to the url
		String serverPasswordS = "";
		for (int i = 0; i < serverPasswordC.length; i++) {
			serverPasswordS += serverPasswordC[i];
		}
		try {
			serverPasswordS = URLEncoder.encode(serverPasswordS, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			System.out
					.println("ServerInfoDialog: Can't encode password for URL");
			System.out.println(ex);
		}

		boolean correctForm = // true;
		!serverNameS.equals("") && !serverAddressS.equals("")
				&& serverAddressS.startsWith("http") && !serverUserS.equals("")
				&& !serverPasswordS.equals("");

		if (correctForm) {
			boolean connectionSuccess = Server.testConnection(serverAddressS,
					serverUserS, serverPasswordS);

			if (connectionSuccess) {
				System.out.println("ServerInfoDialog: Connection Successful");
				Settings.SERVER_NAME = serverNameS;
				Settings.SERVER_USERNAME = serverUserS;
				Settings.SERVER_PASSWORD = serverPasswordS;
				Settings.SERVER_ADDRESS = serverAddressS;
				connectButton2.setEnabled(true);
				errorLabel.setText("Connection successful");

			} else {
				System.out.println("ServerInfoDialog: Connection Failed");
				errorLabel.setText("Connection Failed. Please try again");
				connectButton2.setEnabled(false);
				// TODO: get the error from the xml returned with the connection
				// test
			}
		} else {
			System.out
					.println("ServerInfoDialog: Please fill the required Form Fields");
			errorLabel.setText("Please fill the required Form Fields");
		}

	}// GEN-LAST:event_testConnectionButtonActionPerformed

	private void connectButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectButton2ActionPerformed
		System.out
				.println("ServerInfoDialog: Saving server information for future use");
		Settings.storeServerInfoToFile(autoSelectCheckBox2.isSelected(),
				Settings.SERVER_NAME, Settings.SERVER_ADDRESS,
				Settings.SERVER_USERNAME, Settings.SERVER_PASSWORD);
		Server.connectToServer("auto");
		dispose();
	}// GEN-LAST:event_connectButton2ActionPerformed

	private void connectButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectButton1ActionPerformed
		String serverName = serverListComboBox.getSelectedItem().toString();
		System.out.println("ServerInfoDialog: Connecting to saved server - "
				+ serverName);
		Server.connectToServer(serverName);
		Properties serverProperties = Settings
				.loadServerInfoFromFile(serverName);

		Settings.deleteServerInfoFile(serverName);

		Settings.storeServerInfoToFile(autoSelectCheckBox1.isSelected(),
				serverProperties.getProperty("serverName"),
				serverProperties.getProperty("serverAddress"), // address
				serverProperties.getProperty("username"), // username
				serverProperties.getProperty("password")); // password

		dispose();
	}// GEN-LAST:event_connectButton1ActionPerformed

	private void errorLabelPropertyChange(java.beans.PropertyChangeEvent evt) {// GEN-FIRST:event_errorLabelPropertyChange
		if (evt.getPropertyName().equals("text")) {
			if (evt.getNewValue().toString().toLowerCase().contains("success")) {
				errorLabel.setForeground(Color.GREEN);
			} else {
				errorLabel.setForeground(Color.RED);
			}
		}
	}// GEN-LAST:event_errorLabelPropertyChange

	private void autoSelectCheckBox1ActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_autoSelectCheckBox1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_autoSelectCheckBox1ActionPerformed

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				ServerInfoDialog dialog = new ServerInfoDialog(new JFrame(),
						true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {

					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	private void loadSavedServers() {
		String[] servers = Settings.getSavedServers();
		if (servers != null) {
			for (int i = 0; i < servers.length; i++) {
				String serverName = servers[i].replace(".srv", "");

				if (serverName.equals("auto")) {
					Server.connectToServer("auto");
					dispose();
					break;
				} else {
					serverListComboBox.insertItemAt(serverName, 0);
				}
			}
			try {
				serverListComboBox.setSelectedIndex(0);
			} catch (Exception ignore) {

			}
		}
		boolean bool = serverListComboBox.getItemCount() > 0;
		connectButton1.setEnabled(bool);
	}

	private boolean serverNameAvailable(String serverName) {
		String[] serverNamesTaken = Settings.getSavedServers();
		if (serverNamesTaken == null || serverNamesTaken.length == 0) {
			return true;
		} else {
			for (int i = 0; i < serverNamesTaken.length; i++) {
				if (serverName.equals(serverNamesTaken[i])) {
					return false;
				}
			}
			return true;
		}
	}
}
