package objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class PlayButton extends JToggleButton{
	
	public PlayButton(){
		//super();
		setSelected(false);
		setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
		setSelectedIcon(new ImageIcon(getClass().getResource("/res/Pause-48.png")));
		setRolloverEnabled(false);
		setBorderPainted(true);
		setFocusable(false);
		
		// do not change the selected state when clicking
		// the CurrentPlaylist actions will changed the 
		// selected state of the play button.
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				e.consume();
				if(CurrentPlaylist.isQueued() && !CurrentPlaylist.isPaused()){
					CurrentPlaylist.pause();
				} else {
					if(CurrentPlaylist.isPaused()){
						CurrentPlaylist.unPause();
					} else {
						CurrentPlaylist.playCurrentSong();
					}
				}
			}
		});
	}
	
	public void setPlaying(boolean playing){
		setSelected(playing);
	}
	
	public boolean isPlaying(){
		return isSelected();
	}
	
}
