package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

	public static final String fileSep = System.getProperty("file.separator");
	public static final String appDirectory = System.getProperty("user.home")
			+ fileSep + "SubsonicJ" + fileSep;
	public static final String serversDirectory = appDirectory + "Servers"
			+ fileSep;
	public static final String settingsDirectory = appDirectory + "Settings"
			+ fileSep;
	public static final String cacheDirectory = appDirectory + "cache" + fileSep;
	public static Properties userSettings = null;
	public static final String userPropertiesFileS = settingsDirectory
			+ "userSettings.properties";
	public static final File userPropertiesFileF = new File(userPropertiesFileS);

	// server information
	public static String SERVER_ADDRESS = "";
	public static String SERVER_USERNAME = "";
	public static String SERVER_PASSWORD = "";
	public static String SERVER_NAME = "";
	public static final String APPLICATION_NAME = "SubsonicJ";
	public static String VERSION = "";
	private static float volume;
	public static String localArtistListFileString = cacheDirectory + fileSep + "artistCache.xml";

	
	public static boolean savePropertiesToFile(Properties props,
			String absoluteFilePath) {
		try {
			File file = new File(absoluteFilePath);
			if (file.exists()) {
				try {
					file.delete();
				} catch (Exception e) {
					//
				}
			}
			FileOutputStream fos = new FileOutputStream(file);
			props.storeToXML(fos, "Settings for SubsonicJ");
		} catch (IOException ex) {
			System.out.println("Settings: Can't save settings");
			return false;
		}
		System.out.println("Settings: Settings saved to file");
		return true;
	}

	public static void storeServerInfoToFile(boolean isAuto, String serverName,
			String serverAddress, String username, String password) {
		Properties properties = new Properties();
		properties.setProperty("serverName", serverName);
		properties.setProperty("serverAddress", serverAddress);
		properties.setProperty("username", username);
		properties.setProperty("password", password);

		if (isAuto) {
			serverName = "auto";
		}

		savePropertiesToFile(properties, serversDirectory + serverName + ".srv");

	}

	public static Properties loadServerInfoFromFile(String serverName) {
		String path = serversDirectory + serverName + ".srv";
		File file = new File(path);
		Properties tmpProperties = new Properties();

		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				tmpProperties.loadFromXML(fis);
				System.out.println("Settings: Loaded server info for "
						+ serverName);
				return tmpProperties;
			} catch (IOException ioe) {
				System.out.println("Settings: Can't load server info for "
						+ serverName);
				System.out.println(ioe);
				return null;
			}
		} else {
			System.out.println("Settings: Server info not found for "
					+ serverName);
			return null;
		}
	}

	public static Properties loadSettingsFromFile() {
		if (userPropertiesFileF.exists()) {
			try {
				FileInputStream fis = new FileInputStream(userPropertiesFileF);
				Properties tmp = new Properties();
				tmp.loadFromXML(fis);
				return tmp;
			} catch (IOException ioe) {
				System.out.println("Settings: Can't load settings - " + ioe);
				return new DefaultConfig();
			}
		} else {
			System.out.println("Settings: No user settings file exists");
			return new DefaultConfig();
		}

	}

	public static String[] getSavedServers() {
		File directory = new File(serversDirectory);
		File[] files = directory.listFiles();

		if (directory.exists()) {
			if (files != null && files.length > 0) {
				String[] theReturn = new String[files.length];
				for (int i = 0; i < files.length; i++) {
					theReturn[i] = files[i].getName().replace(".srv", "");
				}
				return theReturn;
			} else {
				return null;
			}

		} else {
			try {
				System.out.println("Settings: Directory doesn't exist.");
				System.out.println("Settings: Creating...");
				directory.createNewFile();
				System.out.println("Settings: Directory created successfully");
				return null;
			} catch (Exception e) {
				System.out.println("Settings: Can't create directory");
				System.out.println(e);
				return null;
			}
		}
	}

	public static void deleteServerInfoFile(String serverName) {
		File file = new File(serversDirectory + serverName + ".srv");
		if (!file.delete()) {
			System.out.println("Could not delete the server file");
		}

	}

	
	public static float getVolume() {
		return volume;
	}

	public static void setVolume(int value) {
		checkIfNull();
		volume = value;
	}

	private static void checkIfNull() {
		if (userSettings == null) {
			userSettings = loadSettingsFromFile();
		}
		
	}

}
