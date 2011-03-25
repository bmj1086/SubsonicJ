package objects.windows;

import java.awt.Color;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;
import javax.swing.*;

import config.AppConfig;

import main.Application;
import main.Main;
import servercontact.Server;

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
	JLabel passwordExampleLabel = new JLabel();
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
		setTitle("SubsonicJ - Server Selection");
		setIconImage(new ImageIcon(
				Main.class.getResource("/res/Application-256.png")).getImage());
		getContentPane().setLayout(null);
		setSize(500, 490);
		setResizable(false);
		setLocationRelativeTo(null);

		//mainPanel.setBackground(new java.awt.Color(34, 34, 34));
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
		serverListComboBox.setBounds(20, 30, 240, 22);
		selectServerPanel.add(serverListComboBox);

		connectButton1.setText("Connect");
		connectButton1.setEnabled(false);
		connectButton1.setFocusable(false);
		connectButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButton1ActionPerformed(evt);
			}
		});
		connectButton1.setBounds(280, 30, 110, 22);
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
		autoSelectCheckBox1.setBounds(17, 60, 360, 20);
		//autoSelectCheckBox1.setBackground(Application.AppColor_Dark);
		autoSelectCheckBox1.setFocusable(false);
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
		createServerPanel.setOpaque(false);
		createServerPanel.setBackground(new Color(mainPanel.getBackground().getRed(),
				mainPanel.getBackground().getGreen(),
				mainPanel.getBackground().getBlue(),
				60));
		

		serverNameLabel.setForeground(new java.awt.Color(102, 102, 102));
		serverNameLabel.setText("Server Name:");
		serverNameLabel.setBounds(20, 30, 200, 20);
		createServerPanel.add(serverNameLabel);

		userNameLabel.setForeground(new java.awt.Color(102, 102, 102));
		userNameLabel.setText("Username:");
		userNameLabel.setBounds(20, 100, 200, 20);
		createServerPanel.add(userNameLabel);

		serverNameTextField.setBounds(30, 50, 150, 20);
		createServerPanel.add(serverNameTextField);
		usernameTextField.setBounds(30, 120, 150, 20);
		createServerPanel.add(usernameTextField);

		serverAddressLabel.setForeground(new java.awt.Color(102, 102, 102));
		serverAddressLabel.setText("Server Address:");
		serverAddressLabel.setBounds(200, 30, 200, 20);
		createServerPanel.add(serverAddressLabel);

		passwordLabel.setForeground(new java.awt.Color(102, 102, 102));
		passwordLabel.setText("Password:");
		passwordLabel.setBounds(200, 100, 200, 20);
		createServerPanel.add(passwordLabel);
		serverAddressTextField.setBounds(210, 50, 180, 20);
		createServerPanel.add(serverAddressTextField);
		passwordField.setBounds(210, 120, 180, 20);
		createServerPanel.add(passwordField);

		serverExampleLabel.setForeground(new java.awt.Color(90, 90, 90));
		serverExampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		serverExampleLabel.setText("e.g. http://127.10.10.10/");
		serverExampleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		serverExampleLabel.setBounds(210, 70, 180, 20);
		createServerPanel.add(serverExampleLabel);
		
		passwordExampleLabel.setForeground(new java.awt.Color(90, 90, 90));
		passwordExampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordExampleLabel.setText("Password to the server");
		passwordExampleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		passwordExampleLabel.setBounds(210, 140, 180, 20);
		createServerPanel.add(passwordExampleLabel);

		testConnectionButton.setText("Test Connection");
		testConnectionButton.setFocusPainted(false);
		testConnectionButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						testConnectionButtonActionPerformed(evt);
					}
				});
		testConnectionButton.setBounds(140, 220, 125, 22);
		createServerPanel.add(testConnectionButton);

		connectButton2.setText("Connect");
		connectButton2.setEnabled(false);
		connectButton2.setFocusPainted(false);
		connectButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButton2ActionPerformed(evt);
			}
		});
		connectButton2.setBounds(280, 220, 110, 22);
		createServerPanel.add(connectButton2);

		usernameLabel.setForeground(new java.awt.Color(90, 90, 90));
		usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		usernameLabel.setText("Server username");
		usernameLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		usernameLabel.setBounds(30, 140, 150, 20);
		createServerPanel.add(usernameLabel);

		serverNameExampleLabel.setForeground(new java.awt.Color(90, 90, 90));
		serverNameExampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		serverNameExampleLabel.setText("e.g. Home Server");
		serverNameExampleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		serverNameExampleLabel.setBounds(30, 70, 150, 20);
		createServerPanel.add(serverNameExampleLabel);

		requiredFieldsLabel.setForeground(new java.awt.Color(230, 0, 0));
		requiredFieldsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		requiredFieldsLabel.setText("**All Fields Are Required");
		requiredFieldsLabel.setBounds(210, 190, 180, 20);
		createServerPanel.add(requiredFieldsLabel);

		autoSelectCheckBox2.setForeground(new java.awt.Color(102, 102, 102));
		autoSelectCheckBox2.setOpaque(false);
		autoSelectCheckBox2
				.setText("Choose this server every time and don't ask me again");
		autoSelectCheckBox2.setBounds(27, 170, 360, 20);
		autoSelectCheckBox2.setFocusable(false);
		
		createServerPanel.add(autoSelectCheckBox2);
		createServerPanel.setBounds(40, 150, 410, 270);
		
		mainPanel.add(createServerPanel);
		
		bgIconLabel.setIcon(new ImageIcon(getClass().getResource(
				"/res/Application-256-trans.png"))); 
		bgIconLabel.setText("");
		bgIconLabel.setBounds(260, 190, 256, 256);
		mainPanel.add(bgIconLabel);

		errorLabel.setForeground(new java.awt.Color(230, 0, 0));
		errorLabel
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						errorLabelPropertyChange(evt);
					}
				});
		errorLabel.setBounds(40, 420, 250, 20);
		mainPanel.add(errorLabel);

		orLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); 
		orLabel.setForeground(new java.awt.Color(102, 102, 102));
		orLabel.setText("or");
		orLabel.setBounds(230, 130, 100, 20);
		mainPanel.add(orLabel);
		
		mainPanel.setBounds(0, 0, getWidth(), getHeight());
		getContentPane().add(mainPanel);

	}

	private void testConnectionButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
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
				AppConfig.deleteServerInfoFile(svrName);
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
				AppConfig.SERVER_NAME = serverNameS;
				AppConfig.SERVER_USERNAME = serverUserS;
				AppConfig.SERVER_PASSWORD = serverPasswordS;
				AppConfig.SERVER_ADDRESS = serverAddressS;
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

	private void connectButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		System.out
				.println("ServerInfoDialog: Saving server information for future use");
		AppConfig.storeServerInfoToFile(autoSelectCheckBox2.isSelected(),
				AppConfig.SERVER_NAME, AppConfig.SERVER_ADDRESS,
				AppConfig.SERVER_USERNAME, AppConfig.SERVER_PASSWORD);
		Server.connectToServer("auto");
		dispose();
	}

	private void connectButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		String serverName = serverListComboBox.getSelectedItem().toString();
		System.out.println("ServerInfoDialog: Attempting to connect to saved server - " + serverName);
		
		Properties serverProperties = AppConfig.loadServerInfoFromFile(serverName);
		
		boolean connectionSuccessful = Server.testConnection(serverProperties.getProperty("serverAddress"),
				serverProperties.getProperty("username"), 
				serverProperties.getProperty("password")); 
		
		if (connectionSuccessful) {
			Server.connectToServer(serverName);
			AppConfig.deleteServerInfoFile(serverName);
	
			AppConfig.storeServerInfoToFile(autoSelectCheckBox1.isSelected(),
					serverProperties.getProperty("serverName"),
					serverProperties.getProperty("serverAddress"), 
					serverProperties.getProperty("username"), 
					serverProperties.getProperty("password")); 
		
			dispose();
		} else {
			errorLabel.setText("Error connecting to server");
			serverListComboBox.removeItemAt(serverListComboBox.getSelectedIndex());
			AppConfig.deleteServerInfoFile(serverName);
			
			JOptionPane.showMessageDialog(this, "An error occured while connecting to the server." +
				System.getProperty("line.separator") +	
				"Please check the information and try again");
			String decodedPassword = null;
			
			try {
				decodedPassword = URLDecoder.decode(serverProperties.getProperty("password"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("ServerInfoDialog: Couldn't URL Decode password. No problem user can re-enter");
			}
			
			serverNameTextField.setText(serverProperties.getProperty("serverName"));
			serverAddressTextField.setText(serverProperties.getProperty("serverAddress"));
			usernameTextField.setText(serverProperties.getProperty("username"));
			passwordField.setText(decodedPassword);
			
		}
		
		
	}

	private void errorLabelPropertyChange(java.beans.PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("text")) {
			if (evt.getNewValue().toString().toLowerCase().contains("success")) {
				errorLabel.setForeground(new Color(60, 150, 30));
			} else {
				errorLabel.setForeground(Color.RED);
			}
		}
	}// GEN-LAST:event_errorLabelPropertyChange

	private void autoSelectCheckBox1ActionPerformed(
			java.awt.event.ActionEvent evt) {
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
		String[] servers = AppConfig.getSavedServers();
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
		String[] serverNamesTaken = AppConfig.getSavedServers();
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
