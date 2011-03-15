package objects;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;
import servercontact.Settings;

//VS4E -- DO NOT REMOVE THIS LINE!
public class MainWindowNew extends JFrame {

	//private static final long serialVersionUID = 1L;
	private JLabel appLogoLabel;
	private JPanel mainPanel;
	private JList artistList;
	private JScrollPane artistScrollPane;
	private JPanel statusPanel;
	private JPanel linksPanel;
	private JLabel selectedAlbumArtLabel;
	private JPanel albumsSongsPanel;
	private JPanel mediaControlPanel;
	private PlayButton playButton;
	private JButton stopButton;
	private JButton skipBackButton;
	private JTextField artistFilterTextField;
	private JButton skipForwardButton;
	private JLabel selectedAlbumNameLabel;
	private JLabel statusLabel;
	private JLabel selectedAlbumArtistLabel;
	private JButton repeatButton;
	private JButton shuffleButton;
	private JPanel selectedAlbumInfoPanel;
	private TrackSeekSlider trackSeekSlider;
	
	/* button icons */
	static ImageIcon pauseButtonIcon = new ImageIcon(MainWindowNew.class.getResource("/res/Pause-48.png"));
	static ImageIcon playButtonIcon = new ImageIcon(MainWindowNew.class.getResource("/res/Play-48.png"));
	
	// ** now playing area ** \\
	private JSeparator nowPlayingSeparator;
	private JLabel nowPlayingAlbumLabelLabel;
	private JLabel nowPlayingTitleLabelLabel;
	private JLabel nowPlayingArtistLabelLabel;
	private JLabel nowPlayingAlbumLabel;
	private JLabel nowPlayingArtistLabel;
	private JLabel nowPlayingTitleLabel;
	private JLabel trackLabel;
	private JLabel nowPlayingAlbumArtLabel;
	
	
	public static String[][] ARTIST_IDs = null;
	public static Object[][] CURRENT_ALBUM_IDs = null;
	public static Object[][] CURRENT_SONGS_DATA = null;
	public static String CURRENT_ATRIST = null;
	ShowSongsThread showSongsThread = new ShowSongsThread();
	ShowAlbumsThread showAlbumsThread = new ShowAlbumsThread();
	public static LoadIndexes_Thread loadIndexesThread = null;
	private JLabel jLabel1;
	private JSeparator jSeparator0;
	private JLabel jLabel0;
	private JPanel jPanel0;
	
	/* for dev usage 
	 * I use this area to test things by assigning them to the app logo
	 * mouse click event MAKE SURE YOU REMOVE ANYTHING YOU ADD HERE BEFORE PUSHING
	 */
		protected void appLogoMouseClicked(MouseEvent e) {
			// going to use a refresh button to refresh the artists
			
			
		}
	/* end dev usage 
	 * 
	 */
	
	
	public MainWindowNew() {
		initComponents();
		loadIndexes();
		showNowPlaying(false);
		showSelectedAlbumInfo(false);
	}

	public void showNowPlaying(boolean bool) {
		nowPlayingAlbumArtLabel.setVisible(bool);
		nowPlayingAlbumLabel.setVisible(bool);
		nowPlayingAlbumLabelLabel.setVisible(bool);
		nowPlayingArtistLabel.setVisible(bool);
		nowPlayingArtistLabelLabel.setVisible(bool);
		nowPlayingTitleLabel.setVisible(bool);
		nowPlayingTitleLabelLabel.setVisible(bool);
	}


	private void initComponents() {
		setTitle("SubsonicJ");
		setSize(1001, 575);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(MainWindowNew.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/Application-256.png")));
		setEnabled(true);
		setMinimumSize(new Dimension(984, 575));
		setPreferredSize(new Dimension(984, 575));
		setBackground(new Color(34, 34, 34));
		setLayout(new GroupLayout());
		add(getMainPanel(), new Constraints(new Bilateral(0, 0, 326), new Bilateral(0, 0, 545)));
		
	}
	
	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new JPanel();
			jPanel0.setBackground(new Color(34, 34, 34));
			jPanel0.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(102, 102, 102)));
			jPanel0.setLayout(new GroupLayout());
		}
		return jPanel0;
	}


	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setForeground(new Color(204, 204, 204));
			jLabel0.setText("Play All");
		}
		return jLabel0;
	}


	private JSeparator getJSeparator0() {
		if (jSeparator0 == null) {
			jSeparator0 = new JSeparator();
			jSeparator0.setBackground(new Color(34, 34, 34));
			jSeparator0.setForeground(new Color(102, 102, 102));
			jSeparator0.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparator0;
	}


	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setForeground(new Color(204, 204, 204));
			jLabel1.setText("Add All");
			jLabel1.setToolTipText("Add all songs below to current playlist");
		}
		return jLabel1;
	}


	private JLabel getNowPlayingAlbumArtLabel() {
		if (nowPlayingAlbumArtLabel == null) {
			nowPlayingAlbumArtLabel = new JLabel();
			nowPlayingAlbumArtLabel.setText("");
		}
		return nowPlayingAlbumArtLabel;
	}


	private JLabel getTrackLabel() {
		if (trackLabel == null) {
			trackLabel = new JLabel();
			trackLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			trackLabel.setForeground(new Color(204, 204, 204));
			trackLabel.setText("Track:");
		}
		return trackLabel;
	}


	private JLabel getNowPlayingTitleLabel() {
		if (nowPlayingTitleLabel == null) {
			nowPlayingTitleLabel = new JLabel();
			nowPlayingTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
			nowPlayingTitleLabel.setForeground(new Color(204, 204, 204));
			nowPlayingTitleLabel.setText("titleText");
		}
		return nowPlayingTitleLabel;
	}


	private JLabel getNowPlayingArtistLabel() {
		if (nowPlayingArtistLabel == null) {
			nowPlayingArtistLabel = new JLabel();
			nowPlayingArtistLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
			nowPlayingArtistLabel.setForeground(new Color(204, 204, 204));
			nowPlayingArtistLabel.setText("artistText");
		}
		return nowPlayingArtistLabel;
	}


	private JLabel getNowPlayingAlbumLabel() {
		if (nowPlayingAlbumLabel == null) {
			nowPlayingAlbumLabel = new JLabel();
			nowPlayingAlbumLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
			nowPlayingAlbumLabel.setForeground(new Color(204, 204, 204));
			nowPlayingAlbumLabel.setText("albumText");
		}
		return nowPlayingAlbumLabel;
	}


	private JLabel getNowPlayingArtistLabelLabel() {
		if (nowPlayingArtistLabelLabel == null) {
			nowPlayingArtistLabelLabel = new JLabel();
			nowPlayingArtistLabelLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			nowPlayingArtistLabelLabel.setForeground(new Color(150, 150, 150));
			nowPlayingArtistLabelLabel.setText("Artist:");
		}
		return nowPlayingArtistLabelLabel;
	}


	private JLabel getNowPlayingTitleLabelLabel() {
		if (nowPlayingTitleLabelLabel == null) {
			nowPlayingTitleLabelLabel = new JLabel();
			nowPlayingTitleLabelLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			nowPlayingTitleLabelLabel.setForeground(new Color(150, 150, 150));
			nowPlayingTitleLabelLabel.setText("Title:");
		}
		return nowPlayingTitleLabelLabel;
	}


	private JLabel getNowPlayingAlbumLabelLabel() {
		if (nowPlayingAlbumLabelLabel == null) {
			nowPlayingAlbumLabelLabel = new JLabel();
			nowPlayingAlbumLabelLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			nowPlayingAlbumLabelLabel.setForeground(new Color(150, 150, 150));
			nowPlayingAlbumLabelLabel.setText("Album:");
		}
		return nowPlayingAlbumLabelLabel;
	}


	private JSeparator getNowPlayingSeparator() {
		if (nowPlayingSeparator == null) {
			nowPlayingSeparator = new JSeparator();
			nowPlayingSeparator.setBackground(new Color(34, 34, 34));
			nowPlayingSeparator.setForeground(new Color(102, 102, 102));
			nowPlayingSeparator.setOrientation(SwingConstants.VERTICAL);
			nowPlayingSeparator.setPreferredSize(new Dimension(1, 0));
			//nowPlayingSeparator.setMaximumSize(new Dimension(1, 32767));
		}
		return nowPlayingSeparator;
	}


	private JSlider getTrackPositionSlider() {
		if (trackSeekSlider == null) {
			trackSeekSlider = new TrackSeekSlider();
			
		}
		return trackSeekSlider;
	}

	private JPanel getSelectedAlbumInfoPanel() {
		if (selectedAlbumInfoPanel == null) {
			selectedAlbumInfoPanel = new JPanel();
			selectedAlbumInfoPanel.setBackground(new Color(34, 34, 34));
			selectedAlbumInfoPanel.setLayout(new GroupLayout());
			selectedAlbumInfoPanel.add(getSelectedAlbumArtLabel(), new Constraints(new Leading(10, 10, 31), new Leading(10, 138, 10, 10)));
			selectedAlbumInfoPanel.add(getSelectedAlbumArtistLabel(), new Constraints(new Leading(10, 150, 12, 12), new Leading(154, 12, 12)));
			selectedAlbumInfoPanel.add(getSelectedAlbumNameLabel(), new Constraints(new Leading(10, 150, 12, 12), new Leading(175, 12, 12)));
		}
		return selectedAlbumInfoPanel;
	}

	private void loadIndexes() {
		if (loadIndexesThread == null) {
			loadIndexesThread = new LoadIndexes_Thread();

		}
		if (loadIndexesThread.isRunning()) {
			int response = JOptionPane.showConfirmDialog(this,
					"Already loading. Try again?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				loadIndexesThread.stop();
				loadIndexes();
			} else {

			}

		} else {
			artistList.removeAll();
			loadIndexesThread.init();
		}

	}

	private JButton getShuffleButton() {
		if (shuffleButton == null) {
			shuffleButton = new JButton();
			shuffleButton.setIcon(new ImageIcon(getClass().getResource("/res/Shuffle-48.png")));
			shuffleButton.setBorderPainted(true);
			shuffleButton.setFocusable(false);
			shuffleButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					shuffleButtonMouseClicked(e);
				}

				private void shuffleButtonMouseClicked(MouseEvent e) {
					CurrentPlaylist.setRandomPlayback(!CurrentPlaylist.isRandomPlay());
				}
			});
		}
		return shuffleButton;
	}
	
	private JButton getRepeatButton() {
		if (repeatButton == null) {
			repeatButton = new JButton();
			repeatButton.setIcon(new ImageIcon(getClass().getResource("/res/Repeat-48.png")));
			repeatButton.setBorderPainted(true);
			repeatButton.setFocusable(false);
			// TODO set button to toggle button style.
			repeatButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					repeatButtonMouseClicked(e);
				}

				private void repeatButtonMouseClicked(MouseEvent e) {
					CurrentPlaylist.setRepeatPlayback(!CurrentPlaylist.isRepeatPlay());
				}
			});
		}
		return repeatButton;
	}

	private JLabel getSelectedAlbumNameLabel() {
		if (selectedAlbumNameLabel == null) {
			selectedAlbumNameLabel = new JLabel();
			selectedAlbumNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			selectedAlbumNameLabel.setForeground(new Color(204, 204, 204));
			selectedAlbumNameLabel.setText("album");
		}
		return selectedAlbumNameLabel;
	}

	private JLabel getSelectedAlbumArtistLabel() {
		if (selectedAlbumArtistLabel == null) {
			selectedAlbumArtistLabel = new JLabel();
			selectedAlbumArtistLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
			selectedAlbumArtistLabel.setForeground(new Color(204, 204, 204));
			selectedAlbumArtistLabel.setText("artist");
		}
		return selectedAlbumArtistLabel;
	}

	private JButton getSkipForwardButton() {
		if (skipForwardButton == null) {
			skipForwardButton = new JButton();
			skipForwardButton.setIcon(new ImageIcon(getClass().getResource("/res/Skip-Forward-48.png")));
			skipForwardButton.setBorderPainted(true);
			skipForwardButton.setFocusable(false);
			skipForwardButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					skipForwardButtonMouseClicked(e);
				}

				private void skipForwardButtonMouseClicked(MouseEvent e) {
					CurrentPlaylist.skipToNextSong();
				}
			});
		}
		return skipForwardButton;
	}

	private JButton getSkipBackButton() {
		if (skipBackButton == null) {
			skipBackButton = new JButton();
			skipBackButton.setIcon(new ImageIcon(getClass().getResource("/res/Skip-Back-48.png")));
			skipBackButton.setBorderPainted(true);
			skipBackButton.setFocusable(false);
			skipBackButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					skipBackButtonMouseClicked(e);
				}

				private void skipBackButtonMouseClicked(MouseEvent e) {
					CurrentPlaylist.skipToPreviousSong();
					
				}
			});
		}
		return skipBackButton;
	}

	private JButton getStopButton() {
		if (stopButton == null) {
			stopButton = new JButton();
			stopButton.setIcon(new ImageIcon(getClass().getResource("/res/Stop-48.png")));
			stopButton.setBorderPainted(true);
			stopButton.setFocusable(false);
			stopButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					stopButtonMouseClicked(e);
				}
			});
		}
		return stopButton;
	}

	protected void stopButtonMouseClicked(MouseEvent e) {
		if (CurrentPlaylist.isQueued()){
			try {
				CurrentPlaylist.stopCurrentSong();
				playButton.setPaused(false);
			} catch (Exception ignore){
				
			}
		}
	}

	private JButton getPlayButton() {
		if (playButton == null) {
			playButton = new PlayButton();
			playButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					playButtonMouseClicked(e);
				}
			});
		}
		return playButton;
	}

	protected void playButtonMouseClicked(MouseEvent e) {
		if (CurrentPlaylist.isQueued()) {
			try {
				System.out.println("MainWindowNew: Toggling pause");
				CurrentPlaylist.togglePause();
				setPlayButtonIcon(!CurrentPlaylist.isPaused());
			} catch (Exception ex){
				System.out.println("MainWindowNew: Can't pause ");
				ex.printStackTrace();
			}
			
		} else {
			CurrentPlaylist.playCurrentSong();
		}
		
	}

	public void setNowPlayingLabels() {
		nowPlayingAlbumLabel.setText(CurrentPlaylist.getCurrentAlbumName());
		nowPlayingAlbumArtLabel.setIcon(CurrentPlaylist.getCurrentAlbumArt(nowPlayingAlbumArtLabel.getWidth()));
		nowPlayingArtistLabel.setText(CurrentPlaylist.getCurrentArtistName());
		nowPlayingTitleLabel.setText(CurrentPlaylist.getCurrentTrackTitle());
		
	}
	
	protected void setPlayButtonIcon(boolean playing) {
		if (playing) {
			playButton.setIcon(pauseButtonIcon);
		} else {
			playButton.setIcon(playButtonIcon);
		}
		
	}

	private JPanel getMediaControlPanel() {
		if (mediaControlPanel == null) {
			mediaControlPanel = new JPanel();
			mediaControlPanel.setBackground(new Color(34, 34, 34));
			mediaControlPanel.setLayout(new GroupLayout());
			mediaControlPanel.add(getSkipBackButton(), new Constraints(new Leading(0, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getStopButton(), new Constraints(new Leading(50, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getPlayButton(), new Constraints(new Leading(100, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getSkipForwardButton(), new Constraints(new Leading(150, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getShuffleButton(), new Constraints(new Leading(200, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getRepeatButton(), new Constraints(new Leading(250, 43, 10, 10), new Leading(5, 42, 11, 11)));
			mediaControlPanel.add(getTrackLabel(), new Constraints(new Leading(3, 12, 12), new Leading(52, 12, 12)));
			mediaControlPanel.add(getTrackPositionSlider(), new Constraints(new Leading(49, 244, 12, 12), new Leading(53, 14, 12, 12)));
			mediaControlPanel.add(getNowPlayingAlbumArtLabel(), new Constraints(new Leading(323, 59, 12, 12), new Leading(9, 53, 12, 12)));
			mediaControlPanel.add(getNowPlayingTitleLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(12, 12, 12)));
			mediaControlPanel.add(getNowPlayingArtistLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(29, 12, 12)));
			mediaControlPanel.add(getNowPlayingAlbumLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(46, 12, 12)));
			mediaControlPanel.add(getNowPlayingTitleLabel(), new Constraints(new Bilateral(440, 12, 40), new Leading(12, 12, 12)));
			mediaControlPanel.add(getNowPlayingArtistLabel(), new Constraints(new Bilateral(440, 12, 47), new Leading(29, 12, 12)));
			mediaControlPanel.add(getNowPlayingAlbumLabel(), new Constraints(new Bilateral(440, 12, 50), new Leading(46, 12, 12)));
			mediaControlPanel.add(getNowPlayingSeparator(), new Constraints(new Leading(307, 2, 10, 10), new Leading(9, 53, 10, 10)));
		}
		return mediaControlPanel;
	}


	private JPanel getAlbumsSongsPanel() {
		if (albumsSongsPanel == null) {
			albumsSongsPanel = new JPanel();
			albumsSongsPanel.setBackground(new Color(34, 34, 34));
			albumsSongsPanel.setLayout(new GroupLayout());
		}
		return albumsSongsPanel;
	}

	private JPanel getLinksPanel() {
		if (linksPanel == null) {
			linksPanel = new JPanel();
			linksPanel.setBackground(new Color(34, 34, 34));
			linksPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(102, 102, 102)));
			linksPanel.setForeground(new Color(204, 204, 204));
			linksPanel.setLayout(new GroupLayout());
			linksPanel.add(getJLabel1(), new Constraints(new Leading(85, 12, 12), new Leading(5, 12, 12)));
			linksPanel.add(getJSeparator0(), new Constraints(new Leading(71, 12, 12), new Leading(5, 16, 12, 12)));
			linksPanel.add(getJLabel0(), new Constraints(new Leading(12, 47, 12, 12), new Leading(5, 10, 10)));
		}
		return linksPanel;
	}


	private JLabel getSelectedAlbumArtLabel() {
		if (selectedAlbumArtLabel == null) {
			selectedAlbumArtLabel = new JLabel();
			selectedAlbumArtLabel.setBackground(new Color(34, 34, 34));
			selectedAlbumArtLabel.setMinimumSize(new Dimension(150, 150));
			selectedAlbumArtLabel.setPreferredSize(new Dimension(150, 150));
			selectedAlbumArtLabel.setMaximumSize(new Dimension(150, 150));
			selectedAlbumArtLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(102, 102, 102)));
			selectedAlbumArtLabel.setLayout(new GroupLayout());
		}
		return selectedAlbumArtLabel;
	}

	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = new JPanel();
			statusPanel.setBackground(new Color(34, 34, 34));
			statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 50)));
			statusPanel.setLayout(new GroupLayout());
			statusPanel.add(getStatusLabel(), new Constraints(new Leading(5, 300, 300, 300), new Leading(0, 20, 20, 20)));
		}
		return statusPanel;
	}

	private Component getStatusLabel() {
		if (statusLabel == null) {
			statusLabel = new JLabel();
			statusLabel.setBackground(new Color(34, 34, 34));
			statusLabel.setBorder(null);
			setStatus("Status Label");
			statusLabel.setForeground(new Color(204, 204, 204));
			statusLabel.setLayout(new GroupLayout());
		}
		return statusLabel;
	}

	private JScrollPane getArtistScrollPane() {
		if (artistScrollPane == null) {
			artistScrollPane = new JScrollPane();
			artistScrollPane.setBackground(new Color(34, 34, 34));
			artistScrollPane.setBorder(null);
			artistScrollPane.setViewportView(getArtistList());
		}
		return artistScrollPane;
	}

	private JList getArtistList() {
		if (artistList == null) {
			artistList = new JList();
			artistList.setBackground(new Color(34, 34, 34));
			artistList.setFont(new Font("Tahoma", Font.PLAIN, 14));
			artistList.setForeground(new Color(204, 204, 204));
			artistList.setSelectionForeground(artistList.getBackground());
			artistList.setSelectionBackground(artistList.getForeground());
			artistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultListModel listModel = new DefaultListModel();
			artistList.setModel(listModel);
		}
		return artistList;
	}


	private JLabel getAppLogoLabel() {
		if (appLogoLabel == null) {
			appLogoLabel = new JLabel();
			appLogoLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					appLogoMouseClicked(e);
				}
			});
			appLogoLabel.setIcon(new ImageIcon(getClass().getResource("/res/application-logo.png")));
		}
		return appLogoLabel;
	}


	private JTextField getArtistFilterTextField() {
		if (artistFilterTextField == null) {
			artistFilterTextField = new JTextField();
			artistFilterTextField.setBackground(new Color(34, 34, 34));
			artistFilterTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
			artistFilterTextField.setForeground(new Color(204, 204, 204));
			artistFilterTextField.setHorizontalAlignment(SwingConstants.CENTER);
			artistFilterTextField.setText("Filter");
			artistFilterTextField.setBorder(new LineBorder(new Color(102, 102, 102), 1, true));
		}
		return artistFilterTextField;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setBackground(new Color(34, 34, 34));
			mainPanel.setLayout(new GroupLayout());
			mainPanel.add(getAppLogoLabel(), new Constraints(new Leading(12, 302, 12, 12), new Leading(12, 61, 12, 12)));
			mainPanel.add(getStatusPanel(), new Constraints(new Bilateral(0, 0, 0), new Trailing(0, 23, 166, 472)));
			mainPanel.add(getLinksPanel(), new Constraints(new Bilateral(325, 197, 462), new Leading(91, 28, 44, 44)));
			mainPanel.add(getArtistScrollPane(), new Constraints(new Leading(13, 300, 12, 12), new Bilateral(131, 32, 22)));
			mainPanel.add(getArtistFilterTextField(), new Constraints(new Leading(12, 303, 10, 10), new Leading(91, 27, 10, 10)));
			mainPanel.add(getMediaControlPanel(), new Constraints(new Bilateral(325, 12, 305), new Leading(12, 70, 153, 153)));
			mainPanel.add(getAlbumsSongsPanel(), new Constraints(new Bilateral(325, 197, 462), new Bilateral(156, 32, 0)));
			mainPanel.add(getJPanel0(), new Constraints(new Bilateral(325, 197, 0), new Leading(124, 26, 10, 10)));
			mainPanel.add(getSelectedAlbumInfoPanel(), new Constraints(new Trailing(0, 0, 0), new Trailing(32, 402, 10, 10)));
		}
		return mainPanel;
	}


	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainWindowNew().setVisible(true);
			}
		});
	}
	
	/* CUSTOM CLASSES THAT DON'T HAVE ANYTHNG TO DO WITH THE INITIAL LOADING
	 * 
	 * 
	 * 
	 * 
	 */
	

	private class ShowSongsThread implements Runnable {

		String albumID, albumName, artistName;
		Thread thread;

		public void init(String albID, String albName, String artName) {
			stop();
			thread = new Thread(this);
			albumID = albID;
			albumName = albName;
			artistName = artName;
			thread.start();

		}

		public boolean isRunning() {
			if (thread != null) {
				return thread.isAlive();
			} else {
				return false;
			}

		}

		public void stop() {
			if (thread != null) {
				thread.interrupt();
			}
		}

		@Override
		public void run() {
			setLoading(true);
			showSelectedAlbumInfo(true);
			setStatus("Loading songs for " + albumName);
			SongsTable table = new SongsTable(albumID);
			MyScrollPane scrollPane = new MyScrollPane(table);

			selectedAlbumArtistLabel.setText(artistName);
			selectedAlbumNameLabel.setText(albumName);

			scrollPane.setSize(albumsSongsPanel.getWidth(),
					albumsSongsPanel.getHeight());
			albumsSongsPanel.removeAll();
			albumsSongsPanel.setLayout(null);
			albumsSongsPanel.add(scrollPane);
			scrollPane.validate();
			albumsSongsPanel.validate();
			setStatus(table.SONG_COUNT + " songs loaded for "
					+ albumName);
			selectedAlbumArtLabel.setIcon(new ImageIcon(Server.getCoverArt(
					table.ALBUM_IMAGE_ID,
					selectedAlbumArtLabel.getPreferredSize().height)));
			CURRENT_SONGS_DATA = getSongsData(table);
			scrollPane.setVisible(true);
			setLoading(false);
		}
	}

	protected Object[][] getSongsData(JTable table) {
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
		Object[][] tableData = new Object[nRow][nCol];
		for (int i = 0; i < nRow; i++)
			for (int j = 0; j < nCol; j++)
				tableData[i][j] = dtm.getValueAt(i, j);
		return tableData;
	}
	
	private void showSelectedAlbumInfo(boolean show) {
		selectedAlbumArtLabel.setVisible(show);
		selectedAlbumArtistLabel.setVisible(show);
		selectedAlbumNameLabel.setVisible(show);
	}

	private String getArtistID(String artistName) {
		for (int i = 0; i < ARTIST_IDs.length; i++) {
			if (ARTIST_IDs[i][0].equals(artistName)) {
				return ARTIST_IDs[i][1];
			}
		}
		return null;
	}

	class MyListSelectionListener implements ListSelectionListener {
		// This method is called each time the user changes the set of selected
		// items

		@Override
		public void valueChanged(ListSelectionEvent evt) {
			// When the user release the mouse button and completes the
			// selection,
			// getValueIsAdjusting() becomes false
			if (!evt.getValueIsAdjusting()) {
				try{
					JList list = (JList) evt.getSource();
					int selectedIndex = evt.getLastIndex();
					String value = (String) list.getSelectedValue();
					String artistID = getArtistID(value.toString());
					showAlbums(artistID, value.toString());
				} catch (Exception ignore) {
					
				}
				
			}
			if (evt.getValueIsAdjusting()) {
				// multiselect ignore
			}
		}
	}
	
	class LoadIndexes_Thread implements Runnable {

		Thread thread = new Thread();

		public LoadIndexes_Thread() {
			// TODO Auto-generated method stub

		}

		public void init() {
			thread = new Thread(this);
			thread.start();
		}

		public void stop() {
			if (thread != null) {
				thread.interrupt();
			}
		}

		public boolean isRunning() {
			return thread.isAlive();
		}

		@Override
		public void run() {
			setStatus("Loading artists from server");
			Document doc = Server.getIndexes();
			NodeList artistNodeList = doc.getElementsByTagName("artist");
			int artistCount = artistNodeList.getLength();
			setStatus(artistCount + " artists found at "
					+ Settings.SERVER_ADDRESS);
			ARTIST_IDs = new String[artistCount][2];

			DefaultListModel listModel = (DefaultListModel) artistList.getModel();

			for (int i = 0; i < artistNodeList.getLength(); i++) {
				Element artistNode = (Element) artistNodeList.item(i);
				int pos = listModel.getSize();

				listModel.add(pos, artistNode.getAttribute("name"));

				ARTIST_IDs[i][0] = artistNode.getAttribute("name");
				ARTIST_IDs[i][1] = artistNode.getAttribute("id");

			}
			artistList.addListSelectionListener(new MyListSelectionListener());
			setStatus(artistCount + " artists loaded from "
					+ Settings.SERVER_ADDRESS);
			artistList.setModel(listModel);
			artistScrollPane.validate();
			artistList.validate();
			
			// TODO set the border of the selected item to null. Can't figure it out.
			
		}

	}
	
	private void showAlbums(String id, final String artistName) {
		if (showAlbumsThread.isRunning()) {
			showAlbumsThread.stop();
		}
		showAlbumsThread.init(id, artistName);

	}
	
	private class ShowAlbumsThread implements Runnable {

		String artistID, artistName;

		Thread thread;

		public boolean isRunning() {
			if (thread != null) {
				return thread.isAlive();
			} else {
				return false;
			}

		}

		public void init(String artID, String artName) {
			thread = new Thread(this);
			stop();
			artistID = artID;
			artistName = artName;
			thread.start();
		}

		public void stop() {
			if (thread != null) {
				thread.interrupt();
			}
		}

		@Override
		public void run() {
			showSelectedAlbumInfo(false);
			final AlbumTable table = new AlbumTable(artistID);
			MyScrollPane scrollPane = new MyScrollPane(table);

			CURRENT_ALBUM_IDs = table.ALBUM_INFO;
			scrollPane.setSize(albumsSongsPanel.getWidth(),
					albumsSongsPanel.getHeight());
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							String albumName = table.getModel()
									.getValueAt(e.getFirstIndex(), 1)
									.toString();
							String artistName = artistList.getSelectedValue()
									.toString();
							String albumID = CURRENT_ALBUM_IDs[e
									.getFirstIndex()][2].toString();
							showSongs(albumID, albumName, artistName);
							table.setEnabled(false);
						}

					});
			albumsSongsPanel.removeAll();
			albumsSongsPanel.setLayout(null);
			albumsSongsPanel.add(scrollPane);
			albumsSongsPanel.validate();
			setStatus(table.albumCount + " albums loaded for "
					+ artistName);
		}
	}

	private void showSongs(String albumID, String albumName, String artistName) {
		if (showSongsThread.isRunning()) {
			showSongsThread.stop();
		}
		showSongsThread.init(albumID, albumName, artistName);

	}

	private void setLoading(boolean loading) {
		// if (loading) {
		// int width = loadingFrame.getSize().width;
		// int height = loadingFrame.getSize().height;
		// int x = (nowPlayingPanel.getWidth() / 2) - (width / 2);
		// int y = (nowPlayingPanel.getHeight() / 2) - (height / 2);
		// loadingFrame.setLocation(x, y);
		// loadingFrame.setVisible(true);
		//
		//
		// } else {
		// try {
		// nowPlayingPanel.remove(loadingLabel);
		// } catch (Exception e) {
		// }
		// }

	}

	public void setPlaying() {
		playButton.setIcon(pauseButtonIcon);
		
	}


	public void setStatus(String string) {
		statusLabel.setText(string);
		
	}
	
	
}
