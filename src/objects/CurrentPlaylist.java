/*************** TODO *****************\
 * need to make the playListList used as random.
 * change the process of adding a song to the playlist.
   - Only add the songID to the playlist instead of 
   - sending a query to gather all of the songs information
   - this is causing a long delay in adding songs to the playlist
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

import mp3player.MP3Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.Application;


public class CurrentPlaylist {

	//static CurrentSong currentSong = new CurrentSong();
	static MP3Player player = null;
	
	
	//static String[][] playlistData = null;
	private static List<Properties> playlistProps = new ArrayList<Properties>(0);
	private static int currentPosition = 0;
	//private static List<Integer> playedSongPositions = new ArrayList<Integer>(0);
	private static List<Integer> playListList = null;
	private static boolean randomPlay = false;
	private static boolean repeatPlay = false;
	//static boolean queued = 

	public static boolean isQueued(){
		if (player == null) {
			return false;
		} else {
			return player.isPlaying() || player.isPaused();
		}
	}
	
	public static boolean isPaused(){
		if(player != null){
			return player.isPaused();
		} else {
			return false;
		}
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
	
	public static void addSongToPlaylist(Properties properties, boolean play){
		//if(!playlistProps.contains(properties)){
			playlistProps.add(properties);
			if (play) {
				playCurrentSong();
				Application.mainWindow.setPlaying(true);
			}
		//} else {
		//	Application.setStatus(properties.getProperty("title") + " already exists in playlist");
		//}
	}

	public static void skipToNextSong(){
		if(playlistProps.size() > 0){
			boolean play = true;
			if (currentPosition < getPlaylistCount() - 1) {
				currentPosition++;
				stopCurrentSong();
			} else {
				if (repeatPlay) {
					currentPosition = 0;
					setupPlayList(randomPlay);
					stopCurrentSong();
				} else {
					System.out.println("CurrentPlaylist: End of playlist");
					Application.mainWindow.setStatus("End of playlist");
					play = false;
				}
			}
			if (play) {
				playCurrentSong();
			}
		}
	}
	
	public static void skipToPreviousSong() {
		if(playlistProps.size() > 0){
			if (currentPosition == 0) {
				currentPosition = getPlaylistCount() - 1;
			} else {
				currentPosition--;
			}
			stopCurrentSong();
			playCurrentSong();
		}
	}
	
	public static void playCurrentSong() {
		if (isQueued()) {
			stopCurrentSong();
		}
		if (getPlaylistCount() > currentPosition) {
			player = new MP3Player(playlistProps.get(currentPosition));
			player.play();
			Application.setNowPlayingLabels();
		} else {
			System.out.println("CurrentPlaylist: No song in current playlist");
			Application.mainWindow.setStatus("No song in current playlist");
		}
		
	}

	public static int getPlaylistCount() {
		return playlistProps.size();
	}
	
	public static void stopCurrentSong() {
		if(player != null){
			player.stop();
			player = null;
		}
	}
	
	public static void pause() {
		player.pause();
	}
	
	public static void unPause() {
		player.unPause();
	}
	
	public static ImageIcon getCurrentAlbumArt(int size) {
		String albumArtID = player.songProperties.getProperty("coverArt");
		return new ImageIcon(Server.getCoverArt(albumArtID, size));
	}
	
	public static String getCurrentAlbumName(){
		return player.songProperties.getProperty("album");
	}

	public static String getCurrentArtistName() {
		return player.songProperties.getProperty("artist");
	}

	public static String getCurrentTrackTitle() {
		return player.songProperties.getProperty("title");
	}


	
	
}
