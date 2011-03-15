package objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class PlayButton extends JToggleButton implements ActionListener{
	
	public PlayButton(){
		//super();
		setSelected(false);
		setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
		setSelectedIcon(new ImageIcon(getClass().getResource("/res/Pause-48.png")));
		setRolloverEnabled(false);
		setBorderPainted(true);
		setFocusable(false);
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		setEnabled(false);
		if(CurrentPlaylist.isQueued() && !CurrentPlaylist.isPaused()){
			CurrentPlaylist.pause();
			setPlaying(false);
		} else {
			if(CurrentPlaylist.isPaused()){
				CurrentPlaylist.unPause();
				setPlaying(true);
			} else {
				CurrentPlaylist.playCurrentSong();
				setPlaying(true);
			}
		}
		setEnabled(true);
	}
	
	public void setPlaying(boolean playing){
		setSelected(playing);
	}
	
	public boolean isPlaying(){
		return isSelected();
	}
	
}
