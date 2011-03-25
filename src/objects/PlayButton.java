package objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import main.Application;
import mp3player.CurrentPlaylist;

@SuppressWarnings("serial")
public class PlayButton extends JToggleButton {

	public PlayButton() {
		// super();
		setSelected(false);
		setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
		setSelectedIcon(new ImageIcon(getClass().getResource(
				"/res/Pause-48.png")));
		setRolloverEnabled(false);
		setBorderPainted(true);
		setFocusable(false);

		// do not change the selected state when clicking
		// the CurrentPlaylist actions will changed the
		// selected state of the play button.

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// consume the actions of the mouse click
				e.consume();
				
				// if song is playing then pause
				if (CurrentPlaylist.isActive() && !CurrentPlaylist.isPaused()) {
					CurrentPlaylist.pause();
				}
				
				// if song is paused then unpause
				else if (CurrentPlaylist.isActive() && CurrentPlaylist.isPaused()) { 
					CurrentPlaylist.unPause();
				}
				
				// if stopped and playlist isn't empty then start at the beginning of the playlist
				else if (!CurrentPlaylist.isActive() && !CurrentPlaylist.isEmpty()) {
					CurrentPlaylist.playSong(0);
				} 
				
				// if stopped and playlist is empty
				else if (CurrentPlaylist.isEmpty()) { 
					System.out.println("PlayButton: No song in current playlist");
					Application.mainWindow.setStatus("Playlist is empty");
					Application.mainWindow.setPlayButtonPressed(false);
				}
			}

		});
	}

	public void setPlaying(boolean playing) {
		setSelected(playing);
	}

	public boolean isPlaying() {
		return isSelected();
	}

}
