package objects;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javazoom.jl.player.Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Media;
import servercontact.Server;
import servercontact.Settings;
import settings.Application;

@SuppressWarnings("serial")
public class CurrentSong {
	
	static Properties songProperties = new Properties();
	static Player PLAYER = null;
	static final Media media = new Media();
	public static boolean PAUSED = false;
	public static boolean isQueued = false;
	
	public static void setProperties(String songID, String parentID){
		Document doc = Server.getMusicDirectory(parentID);
		NodeList songNodeList = doc.getElementsByTagName("child");
		
		for (int i = 0; i < songNodeList.getLength(); i++) {
			Element artistNode = (Element) songNodeList.item(i);
			
			if (artistNode.getAttribute("id").equals(songID)) {
				NamedNodeMap attributes = artistNode.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node theAttribute = attributes.item(j);
					songProperties.put(
							theAttribute.getNodeName().toString(), 
							theAttribute.getNodeValue().toString());
				}
			}
			
			
		}
		
	}
	
	public static void setProperties(Properties props){
		songProperties = props;
	}
	
	public static void playSong() {
		Application.mainWindow.setPlaying();
		isQueued = true;
		media.init();
		
	}
	
	public static void stopSong(){
		isQueued = false;
		media.stop();
		
	}
	
	public static void pauseOrUnpauseSong(){
		isQueued = true;
		media.pause();
		
	}
	
	public static InputStream getInputStream() {
		URL url = null;
		try {
			String songID = songProperties.getProperty("id");
			String urlS = Settings.SERVER_ADDRESS + "/rest/stream.view?u="
					+ Settings.SERVER_USERNAME + "&p=" + Settings.SERVER_PASSWORD
					+ "&v=1.5&c=SubsonicJ" + "&id=" + songID;
			url = new URL(urlS);
			return url.openStream();
		} catch (Exception e) {
			System.out.println("CurrentSong: Can't get stream from URL");
			System.out.println(url);
			e.printStackTrace();
			return null;
		} 
	}

	public static boolean isActive() {
		System.out.println("CurrentSong: isActive = " + PLAYER != null);
		return PLAYER != null;
	}
	
	

	
}
