/*************** TODO *****************\
 * need to make the playListList used as random.
 */


package objects;

import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.Application;


public class CurrentPlaylist {

	static CurrentSong currentSong = new CurrentSong();
	
	//static String[][] playlistData = null;
	private static List<Properties> playlistProps = new ArrayList<Properties>(0);
	private static int currentPosition = 0;
	//private static List<Integer> playedSongPositions = new ArrayList<Integer>(0);
	private static List<Integer> playListList = null;
	private static boolean randomPlay = false;
	private static boolean repeatPlay = false;
	//static boolean queued = 

	public static boolean isQueued(){
		return currentSong.isPaused() || currentSong.isPlaying();
	}
	
	public static boolean isPaused(){
		return currentSong.isPaused();
	}
	
	public static void clearPlaylist(){
		playlistProps.clear();
		currentPosition = 0;
	}
	
	public static void setRandomPlayback(boolean set){
		randomPlay = set;
		setupPlayList(set);
		Application.setStatus("Random playback: " + randomPlay);
	}

	public static boolean isRandomPlay(){
		return randomPlay;
	}
	
	public static void setRepeatPlayback(boolean set){
		repeatPlay = set;
		Application.setStatus("Repeat playback: " + repeatPlay);
	}
	
	public static boolean isRepeatPlay(){
		return repeatPlay;
	}
		
	private static void setupPlayList(boolean isRandom) {
		if (isRandom) {
			Random random = new Random();
			int count = getPlaylistCount();
			playListList = new ArrayList<Integer>(count);
			for (int i = 0; i < count; i++) {
				playListList.add(random.nextInt(count), i);
			}
		} else {
			int count = getPlaylistCount();
			playListList = new ArrayList<Integer>(count);
			for (int i = 0; i < count; i++) {
				playListList.add(i, i);
			}
		}
		
		
	}
	
	public static void addSongToPlaylist(String songID, String parentID, boolean bool){
		Document doc = Server.getMusicDirectory(parentID);
		NodeList songNodeList = doc.getElementsByTagName("child");
		Properties songProperties = new Properties();
		
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
		addSongToPlaylist(songProperties, bool);
	}
	
	public static void addSongToPlaylist(Properties properties, boolean bool){
		if(!playlistProps.contains(properties)){
			playlistProps.add(properties);
			System.out.println("CurrentPlaylist: Added song to playlist successfully");
			Application.setStatus("Added song to playlist successfully. Currently " + getPlaylistCount() + " songs in the playlist");
			System.out.println("CurrentPlaylist: Currently " + getPlaylistCount() + " songs in the playlist");
			if (bool) {
				currentSong.setProperties(properties);
				currentSong.playSong();	
			}
		} else {
			Application.setStatus(properties.getProperty("title") + " already exists in playlist");
		}
	}

	public static void skipToNextSong(){
		boolean play = true;
		if (currentPosition < getPlaylistCount() - 1) {
			currentPosition++;
		} else {
			if (repeatPlay) {
				currentPosition = 0;
				setupPlayList(randomPlay);
			} else {
				System.out.println("CurrentPlaylist: End of playlist");
				Application.mainWindow.setStatus("End of playlist");
				play = false;
			}
		}
		stopCurrentSong();
		if (play) {
			playCurrentSong();
		}
	}

	public static void playCurrentSong() {
		if (getPlaylistCount() > currentPosition) {
			currentSong.setProperties(playlistProps.get(currentPosition));
			currentSong.playSong();
		} else {
			System.out.println("CurrentPlaylist: No song in current playlist");
			Application.mainWindow.setStatus("No song in current playlist");
		}
		
	}

	public static int getPlaylistCount() {
		return playlistProps.size();
	}
	
	public static void stopCurrentSong() {
		currentSong.stopSong();
	}
	
	public static void togglePause() {
		currentSong.togglePause();
	}

	public static InputStream getInputStream() {
		return currentSong.getInputStream();
	}
	
	public static ImageIcon getCurrentAlbumArt(int size) {
		return currentSong.getAlbumArt(size);
	}
	
	public static String getCurrentAlbumName(){
		return currentSong.getAlbumName();
	}

	public static String getCurrentArtistName() {
		return currentSong.getArtistName();
	}

	public static String getCurrentTrackTitle() {
		return currentSong.getTrackTitle();
	}

	
	public static void skipToPreviousSong() {
		if (currentPosition == 0) {
			currentPosition = getPlaylistCount() - 1;
		} else {
			currentPosition--;
		}
		stopCurrentSong();
		playCurrentSong();
	}
	
}
