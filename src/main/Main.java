/*
 * String urlStr = "http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.1.0&c=myapp&id=433a5c55736572735c42726574745c4d757369635c46696e63685c5768617420497420497320746f204275726e5c3133205768617420497420497320746f204275726e205b2d5d2e776d61&maxBitRate=96";
 * JLayerPlayer player = new JLayerPlayer();
 * player.testPlay(urlStr);
 *
 * //temporary to save the file
try {
				Source source = new DOMSource(doc);
		        Result result = new StreamResult(file);
		        Transformer xformer = TransformerFactory.newInstance().newTransformer();
		        xformer.transform(source, result);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
 *
 *
 */

		


package main;

import java.io.File;

import javax.swing.UIManager;

import objects.windows.MainWindow;
import objects.windows.ServerInfoDialog;
import objects.windows.SplashDialog;
import servercontact.Server;
import settings.AppSettings;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

public class Main {

	public static void main(String[] args) {
		System.out.println(System.getProperty("d3d"));
		createAppDirectories();
		loadLookAndFeel();
		Application.loadUIProperties();
		// loadSplashScreen(); // Disable when developing to avoid waiting on
		// splash
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
		String[] dirs = new String[] { AppSettings.appDirectory,
				AppSettings.serversDirectory, AppSettings.settingsDirectory };

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
		if (Server.CONNECTED) {
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
		Application.splashDialog = new SplashDialog(null, true);
		Application.splashDialog.setLocationRelativeTo(null);
		Application.splashDialog.setVisible(true);

	}
}