package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Server;


public class CurrentPlaylist {

	static String[][] playlistData = null;
	static List<Properties> playlistProps = new ArrayList<Properties>(0);
	static int currentPosition = 0;
	static List<Integer> playedSongPositions = new ArrayList<Integer>(0);
	static List<Integer> playListList = null;
	static boolean randomPlay = false;
	static boolean repeatPlay = false;
	
	public static void clearPlaylist(){
		playlistProps = new ArrayList<Properties>(0);
		playedSongPositions = new ArrayList<Integer>(0);
		currentPosition = 0;
	}
	
	public static void setRandomPlayback(boolean set){
		randomPlay = set;
		setupPlayList(set);
		
	}

	public static boolean isRandomPlayback(){
		return randomPlay;
	}
	
	public static void setRepeatPlayback(boolean set){
		randomPlay = set;
	}
	
	public static boolean isRepeatPlayback(){
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
	
	public static void addSongToPlaylist(String songID, String parentID, boolean play){
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
		addSongToPlaylist(songProperties, play);
	}
	
	public static void addSongToPlaylist(Properties properties, boolean play){
		playlistProps.add(properties);
	}

	public static void playNextSong(){
		boolean play = true;
		if (currentPosition < getPlaylistCount() - 1) {
			currentPosition++;
		} else {
			if (repeatPlay) {
				currentPosition = 0;
				setupPlayList(randomPlay);
			} else {
				System.out.println("CurrentPlaylist: End of playlist");
				play = false;
			}
		}
		
		if (play) {
			int nextSongInt = playListList.get(currentPosition);
			CurrentSong.setProperties(playlistProps.get(nextSongInt));
		}
	}

	public static int getPlaylistCount() {
		return playlistData.length;
	}
}
