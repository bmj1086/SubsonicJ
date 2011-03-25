package objects;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import javax.swing.JLabel;

import main.Application;
import mp3player.CurrentPlaylist;
import objects.windows.FloatingMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;

public class PlayAllButton extends JLabel implements Runnable {

	// thread used to add all songs to playlist
	static Thread t;

	// local music directory ID stored to prevent getting lost if the user
	// changes directories while adding is already in progress
	String musicDirID;

	Object sync = new Object();
	// variable used to stop the thread from running
	static boolean stop = false;

	public PlayAllButton() {
		setForeground(new Color(204, 204, 204));
		setText("Play All");
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (Server.currentMusicDirectoryID != null) {
					// set the music directory before doing anything IMPORTANT
					musicDirID = Server.currentMusicDirectoryID;

					init();
				} else {
					System.out
							.println("PlayAllButton: Server.currentMusicDirectoryID is null");
					Application
							.setStatus("Playing songs failed. Reload the artist/album and try again");
				}
			}
		});
	}

	private void stop() {
		stop = true;
	}

	public boolean isAlive() {
		return t == null;
	}

	private void done() {
		stop = false;
		t = null;
		System.out.println("finished with PlayAll action");
	}

	private void addSongsToPlaylist(NodeList songNodes, boolean play,
			boolean shuffle) {
		for (int i = 0; i < songNodes.getLength(); i++) {
			if (stop) {
				System.out.println("Stopping at addSongsToPlaylist");
				break;
			}
			Element songNode = (Element) songNodes.item(i);
			String songid = songNode.getAttribute("id");
			String parentid = songNode.getAttribute("parent");
			CurrentPlaylist.addSongToPlaylist(songid, parentid, play);
			play = false;
		}
		if (!stop) {
			System.out.println(songNodes.getLength()
					+ " songs added successfully");
			Application.setStatus(songNodes.getLength()
					+ " songs added successfully");
		}
	}

	protected void init() {
		// stop the current playback if playing
		CurrentPlaylist.stopCurrentSong();

		if (t != null) {
			System.out.println("PlayAllButton: Stopping current PlayAll process");
			
			//stop the current process
			stop();
			
			// try to wait for the current process for 3 seconds
			// else continue
			double waitedSecs = 0;
			while (stop && waitedSecs < 3) {
				waitedSecs += 0.1;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// ignore
				}
			}
			System.out.println("PlayAllButton: Waited " + waitedSecs + " for process to stop");
			
		}
		
		if (t == null) {// init new PlayAll process
			t = new Thread(this);
			
			// set stop variable to false to prevent it from carrying over
			// into the new thread
			stop = false;
			t.start();
		}
	}

	@Override
	public void run() {
		CurrentPlaylist.clearPlaylist();
		// set start time
		long start = System.currentTimeMillis();

		Document doc = Server.getMusicDirectory(musicDirID);
		NodeList childNodes = doc.getElementsByTagName("child");
		Element node = (Element) childNodes.item(0);

		boolean albums = Boolean.parseBoolean(node.getAttribute("isDir"));
		boolean songs = !albums;

		if (albums) {
			boolean play = true;
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (stop) {
					break;
				}
				// for each album, add songs of album
				Element albumNode = (Element) childNodes.item(i);
				String albumDirectoryID = albumNode.getAttribute("id");
				doc = Server.getMusicDirectory(albumDirectoryID);
				NodeList songNodes = doc.getElementsByTagName("child");
				System.out.println("PlayAllButton: Adding album "
						+ albumNode.getAttribute("title") + " to playlist");
				Application.setStatus("Adding album "
						+ albumNode.getAttribute("title") + " to playlist");
				addSongsToPlaylist(songNodes, play, false);
				play = false;

			}

		} else if (songs) {
			String albumName = ((Element) childNodes.item(0))
					.getAttribute("album");
			System.out.println("PlayAllButton: Adding album " + albumName
					+ " to playlist");
			Application.setStatus("Adding album " + albumName + " to playlist");
			addSongsToPlaylist(childNodes, true, false);

		}

		// show time taken to add all songs
		System.out.println(CurrentPlaylist.getPlaylistCount()
				+ " songs added to playlist - took "
				+ (System.currentTimeMillis() - start) + "ms");
		done();
	}

}
