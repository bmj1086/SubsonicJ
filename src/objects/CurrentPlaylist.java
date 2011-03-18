/*************** TODO *****************\
 * need to make the playListList used as random.
 * change the process of adding a song to the playlist.
   - Only add the songID to the playlist instead of 
   - sending a query to gather all of the songs information
   - this is causing a long delay in adding songs to the playlist
 */

package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.swing.ImageIcon;

import mp3player.MP3Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.AppSettings;
import settings.Application;

public class CurrentPlaylist {

	// static CurrentSong currentSong = new CurrentSong();
	static MP3Player player = null;

	// static String[][] playlistData = null;
	private static List<Properties> playlistProps = new ArrayList<Properties>(0);
	private static int currentPosition = 0;

	// private static List<Integer> playedSongPositions = new
	// ArrayList<Integer>(0);
	private static List<Integer> playListList = null;
	private static boolean repeatPlay = false;

	public static boolean isQueued() {
		if (player == null) {
			return false;
		} else {
			return player.isPlaying() || player.isPaused();
		}
	}

	public static boolean isPaused() {
		if (player != null) {
			return player.isPaused();
		} else {
			return false;
		}
	}

	public static void clearPlaylist() {
		playlistProps.clear();
		currentPosition = 0;
	}

	public static void setRepeatPlayback(boolean set) {
		repeatPlay = set;
		Application.setStatus("Repeat playback: " + repeatPlay);
	}

	public static boolean isRepeatPlay() {
		return repeatPlay;
	}

	public static void addSongToPlaylist(String songID, String parentID,
			boolean bool) {
		Document doc = Server.getMusicDirectory(parentID);
		NodeList songNodeList = doc.getElementsByTagName("child");
		Properties songProperties = new Properties();

		for (int i = 0; i < songNodeList.getLength(); i++) {
			Element artistNode = (Element) songNodeList.item(i);

			if (artistNode.getAttribute("id").equals(songID)) {
				NamedNodeMap attributes = artistNode.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node theAttribute = attributes.item(j);
					songProperties.put(theAttribute.getNodeName().toString(),
							theAttribute.getNodeValue().toString());
				}
			}

		}
		addSongToPlaylist(songProperties, bool);
	}

	public static void addSongToPlaylist(Properties properties, boolean play) {
		// if(!playlistProps.contains(properties)){
		playlistProps.add(properties);
		if (play) {
			playCurrentSong();
		}
		// } else {
		// Application.setStatus(properties.getProperty("title") +
		// " already exists in playlist");
		// }
	}

	public static void skipToNextSong() {
		if (playlistProps.size() > 0) {
			if (currentPosition < getPlaylistCount() - 1) {
				currentPosition++;
				stopCurrentSong();
				playCurrentSong();
			} else {
				if (repeatPlay) {
					currentPosition = 0;
					stopCurrentSong();
					playCurrentSong();
				} else {
					System.out.println("CurrentPlaylist: End of playlist");
					Application.mainWindow.setStatus("End of playlist");
				}
			}
		}
	}

	public static void skipToPreviousSong() {
		if (playlistProps.size() > 0) {
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
			Properties currentSongProps = playlistProps.get(currentPosition);
			player = new MP3Player(currentSongProps);
			String songName = currentSongProps.getProperty("title");
			String albumName = currentSongProps.getProperty("album");
			Application.setStatus("Playing " + songName + " by " + albumName);
			player.play();
			Application.setNowPlayingLabels();
			Application.mainWindow.setTrackDuration(Integer.parseInt(currentSongProps.getProperty("duration")));
			Application.mainWindow.setPlaying(true);
		} else {
			Application.mainWindow.setPlaying(false);
			System.out.println("CurrentPlaylist: No song in current playlist");
			Application.mainWindow.setStatus("No song in current playlist");
		}

	}

	public static int getPlaylistCount() {
		return playlistProps.size();
	}

	public static void stopCurrentSong() {
		if (player != null) {
			player.stop();
			player = null;
		}
		Application.mainWindow.setPlaying(false);
	}

	public static void pause() {
		player.pause();
		Application.mainWindow.setPlaying(false);
	}

	public static void unPause() {
		player.unPause();
		Application.mainWindow.setPlaying(true);
	}

	public static ImageIcon getCurrentAlbumArt(int size) {
		String albumArtID = player.songProperties.getProperty("coverArt");
		return new ImageIcon(Server.getCoverArt(albumArtID, size));
	}

	public static String getCurrentAlbumName() {
		return player.songProperties.getProperty("album");
	}

	public static String getCurrentArtistName() {
		return player.songProperties.getProperty("artist");
	}

	public static String getCurrentTrackTitle() {
		return player.songProperties.getProperty("title");
	}

	public static void setVolume(int value) {
		AppSettings.setVolume(value);
		if (player != null) {
			player.setVolume(value);
		}

	}

	public static int getTrackPosition() {
		try {
			long posMicro = player.getPosition();
			double theReturnDouble = posMicro / java.lang.Math.pow(10, 6);
			int theReturn = (int) Math.floor(theReturnDouble);
			System.out.println(theReturn);
			return theReturn;
		} catch (Exception e) {
			return 0;
		}

	}

	public static void shuffle() {
		Collections.shuffle(playlistProps);
		for (int i = 0; i < playlistProps.size(); i++) {
			if (player.songProperties.getProperty("title").equals(
					playlistProps.get(i).getProperty("title"))) {
				currentPosition = i;
			}
		}
	}

}
