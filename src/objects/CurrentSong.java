/* ************ Class description *********************************************
 * This class is used to store the information for the current song in use.
 * Such as the song playing or paused in the queue. No playlist information
 * should be stored here. To store playlist information use the CurrentPlaylist
 * class.
 * ************ end class description *****************************************
 */

package objects;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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

public class CurrentSong {
	
	// the properties file to store all of the attributes gathered from the 
	// xml file returned by Subsonic API with the Stream.view query
	// for more info check the Server class
	Properties songProperties = new Properties();
	
	// instance of the AdvancedPlayer from the JLayer import
	//static Player PLAYER = null;
	
	// instance of Media
	static final Media media = new Media();
	
	// boolean stored to know if a song has been stored or not. Should only
	// be true if a song is playing or paused
	public boolean isQueued = false;
	
	// adds the information for the current song. Uses the songID and
	// parentID to look up the name of the song with the Server class
	// and return the xml document with the song attributes.
	public void setProperties(String songID, String parentID){
		System.out.println("CurrentSong: Setting song properties");
		Document doc = Server.getMusicDirectory(parentID);
		NodeList songNodeList = doc.getElementsByTagName("child");
		
		for (int i = 0; i < songNodeList.getLength(); i++) {
			Element artistNode = (Element) songNodeList.item(i);
			
			if (artistNode.getAttribute("id").equals(songID)) {
				System.out.println("CurrentSong: Found song information");
				NamedNodeMap attributes = artistNode.getAttributes();
				
				System.out.println("CurrentSong: Setting " + attributes.getLength() + " attributes");
				for (int j = 0; j < attributes.getLength(); j++) {
					Node theAttribute = attributes.item(j);
					songProperties.put(
							theAttribute.getNodeName().toString(), 
							theAttribute.getNodeValue().toString());
				}
			}
			
			
		}
		System.out.println("CurrentSong: Finished setting song properties");
	}
	
	// used if the properties file has already been created to set 
	// the song properties
	public void setProperties(Properties props){
		songProperties = props;
	}
	
	// plays the current song in queue.
	public void playSong() {
		Application.mainWindow.setPlaying();
		isQueued = true;
		media.init();
		
	}
	
	// stops the current song in queue and removes it from the queue
	// the CurrentPlaylist isn't affected.
	public void stopSong(){
		isQueued = false;
		media.stop();
		
	}
	
	
	public InputStream getInputStream() {
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

	public boolean isPlaying() {
		//System.out.println("CurrentSong: isActive = " + PLAYER != null);
		return media.isActive() && !media.isPaused();
	}

	public boolean isPaused() {
		if (media.isActive() && media.isPaused()) {
			return true;
		} else {
			return false;
		}
		
	}

	public ImageIcon getAlbumArt(int size) {
		String albumID = songProperties.getProperty("coverArt");
		Image albumImage = Server.getCoverArt(albumID, size);
		ImageIcon icon = new ImageIcon(albumImage);
		return icon;
	}
	
	public String getAlbumName(){
		return songProperties.getProperty("album");
	}

	public String getArtistName() {
		return songProperties.getProperty("artist");
	}

	public String getTrackTitle() {
		return songProperties.getProperty("title");
	}

	public void togglePause() {
		media.togglePause();
	}
	
	

	
}
