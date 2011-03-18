package objects;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.Application;

public class PlayAllButton extends JLabel implements Runnable {

	Thread t;

	public PlayAllButton() {
		setForeground(new Color(204, 204, 204));
		setText("Play All");
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				init();
			}
		});
	}

	protected void init() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		} else {
			t.interrupt();
			t = new Thread(this);
			t.start();
		}

	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		System.out.println("Adding songs to playlist.");
		CurrentPlaylist.clearPlaylist();
		String[] currentIDs = Application.mainWindow.getVisibleData();
		Document doc = Server.getMusicDirectory(currentIDs[0]);
		NodeList songNodes = doc.getElementsByTagName("child");
		if (songNodes.getLength() > 0) {
			boolean play = true;
			for (int i = 0; i < currentIDs.length; i++) {
				doc = Server.getMusicDirectory(currentIDs[i]);
				songNodes = doc.getElementsByTagName("child");
				for (int j = 0; j < songNodes.getLength(); j++) {
					Element element = (Element) songNodes.item(j);
					String songid = element.getAttribute("id");
					String parentid = element.getAttribute("parent");
					CurrentPlaylist.addSongToPlaylist(songid, parentid, play);
					play = false;
				}
			}

		} else {
			boolean play = false;
			for (int i = 0; i < currentIDs.length; i++) {
				for (int j = 0; j < songNodes.getLength(); j++) {
					String songid = currentIDs[i];
					//CurrentPlaylist.addSongToPlaylist(songid, parentid, play);
				}
			}
		}
		System.out.println(CurrentPlaylist.getPlaylistCount()
				+ " songs added to playlist - took "
				+ (System.currentTimeMillis() - start) + "ms");
	}
}
