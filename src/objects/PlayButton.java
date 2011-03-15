package objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class PlayButton extends JButton {
	
	public PlayButton(){
		super();
		setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
		setBorderPainted(true);
		setFocusable(false);
		
	}
	
	public void setPaused(boolean paused){
		if(paused){
			setIcon(new ImageIcon(getClass().getResource("/res/Pause-48.png")));
		} else {
			setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
		}
		
	}
	
}
