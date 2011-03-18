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
import settings.Preferences;

@SuppressWarnings("serial")
public class CurrentSong {
	
	static Properties songProperties = new Properties();
	static Player PLAYER = null;
	static final Media media = new Media();
	public static boolean PAUSED = false;
	
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
		media.init();
		
	}
	
	public static void stopSong(){
		media.stop();
		
	}
	
	public static void pauseSong(){
		media.pause();
		
	}
	
	public static InputStream getInputStream() {
		URL url = null;
		try {
			String songID = songProperties.getProperty("id");
			String urlS = Preferences.SERVER_ADDRESS + "/rest/stream.view?u="
					+ Preferences.SERVER_USERNAME + "&p=" + Preferences.SERVER_PASSWORD
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
	
	

	
}
