/*
 * Some Samples
 * http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.1.0&c=myapp&id=433a5c55736572735c42726574745c4d757369635c46696e63685c5768617420497420497320746f204275726e5c3031204e657720426567696e6e696e67732e776d61&maxBitRate=96
 * http://bmjones.no-ip.org/music/rest/ping.view?u=Guest&p=notbrett&v=1.1.0&c=myapp
 * http://bmjones.no-ip.org/music/rest/getMusicDirectory.view?u=Guest&p=notbrett&v=1.1.0&c=myapp&id=433a5c55736572735c42726574745c4d757369635c46696e63685c5768617420497420497320746f204275726e
 *
 */
package servercontact;

//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.awt.Image;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.*;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import subsonicj.Main;

public class Server {

	public static boolean CONNECTED = false;
	
	public static boolean testConnection(String serverAddressS,
			String serverUserS, String serverPasswordS) {
		try {
			String urlS = serverAddressS + "/rest/getIndexes.view?u="
					+ serverUserS + "&p=" + serverPasswordS
					+ "&v=1.5&c=SubsonicJ";
			URL url = new URL(urlS);
			URLConnection ucon = url.openConnection();
			String htmlContents = getResponseData(ucon);

			// if nothing returned return false
			if (htmlContents.equals("")) {
				return false;
			} else if (htmlContents.contains("error code")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
	}

	public static void connectToServer(String string) {
		Properties serverProperties = Settings.loadServerInfoFromFile(string);
		Settings.SERVER_NAME = serverProperties.getProperty("serverName");
		Settings.SERVER_USERNAME = serverProperties.getProperty("username");
		Settings.SERVER_PASSWORD = serverProperties.getProperty("password");
		Settings.SERVER_ADDRESS = serverProperties.getProperty("serverAddress");

		CONNECTED = true;
	}

	public static void getLicense() {
	}

	public static void getMusicFolders() {
	}

	public static void getNowPlaying() {
	}

	public static Document getIndexes() {
		try {
			String urlS = Settings.SERVER_ADDRESS + "/rest/getIndexes.view?u="
					+ Settings.SERVER_USERNAME + "&p="
					+ Settings.SERVER_PASSWORD + "&v=1.5&c="
					+ Settings.APPLICATION_NAME;
			URL url = new URL(urlS);
			InputStream is = url.openStream();
			Document doc = parse(is);
			return doc;
		} catch (Exception ex) {
			System.out.println("Server: ");
			ex.printStackTrace();
			return null;
		}

	}

	public static Document getMusicDirectory(String artistOrAlbumID) {
		try {
			String urlS = Settings.SERVER_ADDRESS
					+ "/rest/getMusicDirectory.view?u="
					+ Settings.SERVER_USERNAME + "&p="
					+ Settings.SERVER_PASSWORD + "&v=1.5&c=SubsonicJ" + "&id="
					+ artistOrAlbumID;
			URL url = new URL(urlS);
			InputStream is = url.openStream();
			Document doc = parse(is);
			return doc;
		} catch (Exception ex) {
			System.out.println("Server: ");
			System.out.println(ex);
			return null;
		}
	}

	public static void search() {
	}

	public static void search2() {
	}

	public static void getPlaylists() {
	}

	public static void getPlaylist() {
	}

	public static void createPlaylist() {
	}

	public static void deletePlaylist() {
	}

	public static void download() {
	}

	public static URL getStreamURL(String songID) {
		URL url = null;
		try {
			String urlS = Settings.SERVER_ADDRESS + "/rest/stream.view?u="
					+ Settings.SERVER_USERNAME + "&p=" + Settings.SERVER_PASSWORD
					+ "&v=1.5&c=SubsonicJ" + "&id=" + songID;
			url = new URL(urlS);
			return url;
		} catch (Exception e) {
			System.out.println("CurrentSong: Can't get stream from URL");
			System.out.println(url);
			e.printStackTrace();
			return null;
		} 
		
		
	}

	public static Image getCoverArt(String albumArtID, int size) {
		Image image = null;
		try {
			String urlS = Settings.SERVER_ADDRESS + "/rest/getCoverArt.view?u="
					+ Settings.SERVER_USERNAME + "&p="
					+ Settings.SERVER_PASSWORD + "&v=1.5&c=SubsonicJ" + "&id="
					+ albumArtID + "&size=" + size;
			URL url = new URL(urlS);
			image = ImageIO.read(url);
		} catch (Exception ex) {
			System.out.println("Server: ");
			System.out.println(ex);
			return null;
		}
		return image;
	}

	public static String getCoverArt(String albumID) {
		String theReturn = null;
		Document doc = getMusicDirectory(albumID);
		NodeList indexes = doc.getElementsByTagName("child");
		if (indexes.getLength() > 0) {
			Element node = (Element) indexes.item(0);
			theReturn = node.getAttribute("coverArt");
		}
		return theReturn;
	}

	public static void scrobble() {
	}

	public static void changePassword() {
	}

	public static void getUser() {
	}

	public static void createUser() {
	}

	public static void deleteUser() {
	}

	public static void getChatMessages() {
	}

	public static void addChatMessage() {
	}

	public static void getAlbumList() {
	}

	public static int getAlbumCount(String artistID) {
		int theReturn = 0;
		Document doc = getMusicDirectory(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		theReturn = indexes.getLength();
		return theReturn;
	}

	public static void getRandomSongs() {
	}

	public static void getLyrics() {
	}

	public static void jukeboxControl() {
	}

	private static String getResponseData(URLConnection conn) {
		try {
			StringBuffer sb = new StringBuffer();
			String data = "";
			InputStream is = conn.getInputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}

			data = sb.toString();
			is.close();
			is = null;
			sb = null;
			System.gc();
			return data;
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	public static Document parse(InputStream is) {
		Document ret = null;
		DocumentBuilderFactory domFactory;
		DocumentBuilder builder;

		try {
			domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setValidating(false);
			domFactory.setNamespaceAware(false);
			builder = domFactory.newDocumentBuilder();

			ret = (Document) builder.parse(is);
		} catch (Exception ex) {
			System.out.println("Server: Unable to load XML");
			System.out.println(ex);
		}
		return ret;
	}

	public static Image[] getAlbumImages(String artistID, int size) {
		Image[] images = null;

		Document doc = getMusicDirectory(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		images = new Image[indexes.getLength()];

		for (int i = 0; i < indexes.getLength(); i++) {
			Element index = (Element) indexes.item(i);
			String coverArtID = index.getAttribute("coverArt");
			try {
				String urlS = Settings.SERVER_ADDRESS
						+ "/rest/getCoverArt.view?u="
						+ Settings.SERVER_USERNAME + "&p="
						+ Settings.SERVER_PASSWORD + "&v=1.5&c=SubsonicJ"
						+ "&id=" + coverArtID + "&size=" + size;

				URL url = new URL(urlS);
				images[i] = ImageIO.read(url);

			} catch (Exception ex) {
				System.out.println("Server: ");
				System.out.println(ex);
				return null;
			}
		}
		return images;
	}

	public static String[] getAlbumNames(String artistID) {
		String[] names = null;

		Document doc = getMusicDirectory(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		names = new String[indexes.getLength()];

		for (int i = 0; i < indexes.getLength(); i++) {
			Element index = (Element) indexes.item(i);
			String albumName = index.getAttribute("title");
			names[i] = albumName;
		}
		return names;
	}

	public static String[] getAlbumIDs(String artistID) {
		String[] albumIDs = null;

		Document doc = getMusicDirectory(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		albumIDs = new String[indexes.getLength()];

		for (int i = 0; i < indexes.getLength(); i++) {
			Element index = (Element) indexes.item(i);
			String albumName = index.getAttribute("id");
			albumIDs[i] = albumName;
		}
		return albumIDs;
	}

	public static String[] getAlbumCoverIDs(String artistID) {
		String[] coverArtIDs = null;

		Document doc = getMusicDirectory(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		coverArtIDs = new String[indexes.getLength()];

		for (int i = 0; i < indexes.getLength(); i++) {
			Element index = (Element) indexes.item(i);
			String albumName = index.getAttribute("coverArt");
			coverArtIDs[i] = albumName;
		}
		return coverArtIDs;
	}
}
