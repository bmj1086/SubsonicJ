package objects;

import java.awt.MenuItem;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TrayRightClickMenu extends JPopupMenu {
	
	public TrayRightClickMenu() {
		initComponents();
	}

	private void initComponents() {
		nowPlayingLabelJMI = new JMenuItem("NowPlaying:", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		nowPlayingLabelJMI.setEnabled(false);
		
		nowPlayingArtistJMI = new JMenuItem("", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		nowPlayingSongJMI = new JMenuItem("", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
	
		
		
		playPauseJMI = new JMenuItem("Play", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		stopJMI = new JMenuItem("Play", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		skipForwardJMI = new JMenuItem("Play", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		skipBackJMI = new JMenuItem("Play", new ImageIcon(getClass().getResource(
			"/res/Play-15.png")));
		
		add(playPauseJMI);
		
		
	}
	
	
	/* Create components */
	JMenuItem nowPlayingLabelJMI;
	JMenuItem nowPlayingArtistJMI;
	JMenuItem nowPlayingSongJMI;
	JMenuItem playPauseJMI;
	JMenuItem stopJMI;
	JMenuItem skipForwardJMI;
	JMenuItem skipBackJMI;
	/* end create components */
}
