package main;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import objects.windows.MainWindow;
import objects.windows.ServerInfoDialog;
import objects.windows.SplashDialog;
import servercontact.Server;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import config.AppConfig;

public class Main {

	public static void main(String[] args) {
		createAppDirectories();
		loadLookAndFeel();
		Application.loadUIProperties();

		/*** Disable when developing to avoid waiting on splash */
		loadSplashScreen();
		
		loadServerForm();
		loadMainWindow();

	}

	
	private static void loadLookAndFeel() {
		try {
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			System.out.println("Main: Custom LookAndFeel set");

		} catch (Exception ex) {
			System.out
					.println("Main: Can't load Look and Feel...Exiting application");
			System.out.println(ex);
			System.exit(0);

		}

	}

	
	public static void setWindowDecorations(boolean on) {
		// changes the property for decorated window
		if (on) {
			AluminiumLookAndFeel
					.setCurrentTheme(Application.decoratedProperties);
		} else {
			AluminiumLookAndFeel
					.setCurrentTheme(Application.undecoratedProperties);
		}

	}

	
	private static void createAppDirectories() {
		String[] dirs = new String[] { AppConfig.appDirectory,
				AppConfig.serversDirectory, AppConfig.settingsDirectory };

		for (int i = 0; i < dirs.length; i++) {
			File directory = new File(dirs[i]);
			if (!directory.exists()) {
				try {
					directory.mkdir();
				} catch (Exception e) {
					System.out
							.println("Main: Can't create directories for application");
					System.out.println(e);
					System.out.println("Main: Shutting Down...");
					System.exit(0);
				}
			}
		}
	}

	
	private static void loadServerForm() {
		setWindowDecorations(true);
		Application.serverInfoDialog = new ServerInfoDialog(null, true);
		Application.serverInfoDialog.setLocationRelativeTo(null);
		if (!Server.CONNECTED) {
			System.out
					.println("Main: server not auto connected or no server information found");
			System.out.println("Main: Initializing the server info dialog...");
			Application.serverInfoDialog.setVisible(true);

		}

	}

	
	private static void loadMainWindow() {
		if  (Server.CONNECTED) {
			setWindowDecorations(true);
			Application.mainWindow = new MainWindow();
			Application.mainWindow.setVisible(true);
		} else {
			System.exit(0);
		}
	
	}

	
	private static void loadSplashScreen() {
		System.out.println("Main: Loading splash screen");
		setWindowDecorations(false);
		Application.splashDialog = new SplashDialog(null, false);
		Application.splashDialog.setLocationRelativeTo(null);
		Application.splashDialog.setVisible(true);

	}

	
	
}
