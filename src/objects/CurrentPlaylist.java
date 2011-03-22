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

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import main.Application;
import mp3player.MP3Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.AppSettings;

public class CurrentPlaylist {

	// static CurrentSong currentSong = new CurrentSong();
	static MP3Player player = null;

	private static List<Properties> playlistProps = new ArrayList<Properties>(0);

	// marker to show what position currently in use by the playlistProps
	private static int currentPosition = 0;

	private static boolean repeatPlay = false;

	public static boolean isActive() {
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
		playlistProps.add(properties);
		if (play) {
			playSongAtCurrentPosition();
		}

	}

	public static void playNextSong() {
		if (!playlistProps.isEmpty()) {
			if (currentPosition < getPlaylistMaxIndex()) {
				currentPosition++;
				playSongAtCurrentPosition();
			} else {
				if (repeatPlay) {
					currentPosition = 0;
					playSongAtCurrentPosition();
				} else if (!repeatPlay) {
					System.out.println("CurrentPlaylist: End of playlist");
					Application.mainWindow.setStatus("End of playlist");
					Application.showNowPlaying(false);
					Application.mainWindow.setPlayButtonPressed(false);
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
			playSongAtCurrentPosition();
		}
	}

	public static void playSong(int i) {
		currentPosition = i;
		playSongAtCurrentPosition();
	}
	
	public static void playSongAtCurrentPosition() {
		/** adding an invokeLater method to avoid conflicts with playing multiple songs */
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				while (isActive()) {
					try {
						Thread.sleep(100);
					} catch (Exception ignore) {

					}
				}
				final Properties currentSongProps = playlistProps.get(currentPosition);
				player = new MP3Player(currentSongProps);
				
				/** set title and status to match current song */
				String songName = currentSongProps.getProperty("title");
				String artistName = currentSongProps.getProperty("artist");
				Application.setStatus("Playing " + songName + " by " + artistName);
				Application.mainWindow.setTitle(songName + " - " + artistName);
				
				player.play();
				player.addLineListener(createLineListener());
				
			}
		});
		
		

	}

	private static LineListener createLineListener() {
		return new LineListener() {

			@Override
			public void update(LineEvent event) { //
				if (event.getType() == LineEvent.Type.START) {
					Application.setNowPlayingLabels();
					Application.showNowPlaying(true);
					Application.mainWindow.setPlayButtonPressed(true);
					/** set current song duration on main window */
					Application.mainWindow.setTrackDuration(Integer.parseInt(player.songProperties.getProperty("duration")));
					
				} else if (event.getType() == LineEvent.Type.CLOSE) {
					Application.mainWindow.setTrackPosition(0);
					
					if (player.songCompleted && !playlistProps.isEmpty()) { 
						CurrentPlaylist.playNextSong();
					} 
					
					// this should only happen when the player pushed the stop button on the main window
					else if (!player.songCompleted && playlistProps.isEmpty()) { 
						Application.showNowPlaying(false);
						Application.mainWindow.setPlayButtonPressed(false);
					}
				}
			}
		};
	}

	public static int getPlaylistCount() {
		return playlistProps.size();
	}
	
	public static int getPlaylistMaxIndex() {
		return playlistProps.size() - 1;
	}

	public static void stopCurrentSong() {
		if (player != null) {
			player.stop();
		}
	}

	public static void stopAndClearPlaylist() {
		clearPlaylist();
		stopCurrentSong();
		Application.mainWindow.setPlayButtonPressed(false);
	}
	
	public static boolean isEmpty() {
		return playlistProps.isEmpty();
	}

	public static void pause() {
		player.pause();
		Application.mainWindow.setPlayButtonPressed(false);
	}

	public static void unPause() {
		player.unpause();
		Application.mainWindow.setPlayButtonPressed(true);
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
