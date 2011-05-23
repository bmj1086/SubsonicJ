package objects.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import main.Application;
import mp3player.CurrentPlaylist;
import objects.AddAllButton;
import objects.AlbumTable;
import objects.GetArtistList;
import objects.MyScrollPane;
import objects.PlayAllButton;
import objects.PlayButton;
import objects.SongsTable;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

import servercontact.Server;
import config.AppConfig;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	// private static final long serialVersionUID = 1L;
	private JLabel appLogoLabel;
	public JPanel mainPanel;
	private JList artistList;
	private JScrollPane artistScrollPane;
	public JPanel statusPanel;
	public JPanel linksPanel;
	private JLabel selectedAlbumArtLabel;
	public JPanel albumsSongsPanel;
	public JPanel mediaControlPanel;
	private PlayButton playButton;
	private JButton stopButton;
	private JButton skipBackButton;
	private JTextField artistFilterTextField;
	private JButton skipForwardButton;
	private JLabel selectedAlbumNameLabel;
	private JLabel selectedAlbumArtistLabel;
	private JButton repeatButton;
	private JButton shuffleButton;
	public JPanel selectedAlbumInfoPanel;

	/* button icons */
	static ImageIcon pauseButtonIcon = new ImageIcon(
			MainWindow.class.getResource("/res/Pause-48.png"));
	static ImageIcon playButtonIcon = new ImageIcon(
			MainWindow.class.getResource("/res/Play-48.png"));

	// ** now playing area ** \\
	private JSeparator nowPlayingSeparator;
	private JLabel nowPlayingAlbumLabelLabel;
	private JLabel nowPlayingTitleLabelLabel;
	private JLabel nowPlayingArtistLabelLabel;
	private JLabel nowPlayingAlbumLabel;
	private JLabel nowPlayingArtistLabel;
	private JLabel nowPlayingTitleLabel;
	private JLabel nowPlayingAlbumArtLabel;

	public static String[][] ARTIST_IDs = null;
	public static String[] currentTableData = null;
	public static Object[][] CURRENT_ALBUM_IDs = null;
	public static Object[][] CURRENT_SONGS_DATA = null;
	public static String CURRENT_ATRIST = null;

	ShowSongsThread showSongsThread = new ShowSongsThread();
	ShowAlbumsThread showAlbumsThread = new ShowAlbumsThread();
	GetArtistList getArtistIndex = null;

	private JLabel addAllButton;
	private JSeparator jSeparator0;
	private PlayAllButton playAllButton;
	public JPanel breadcrumbPanel;
	private JLabel statusLabel;
	private JSlider volumeSlider;
	private JProgressBar trackProgressBar;
	private JLabel trackPositionLabel;
	private JSeparator jSeparator1;
	private JLabel jLabel0;
	private JSeparator jSeparator2;
	private JLabel jLabel1;
	private JToggleButton volumeToggleButton;

	/*
	 * for dev usage I use this area to test things by assigning them to the app
	 * logo mouse click event MAKE SURE YOU REMOVE ANYTHING YOU ADD HERE BEFORE
	 * PUSHING
	 */
	protected void appLogoMouseClicked(MouseEvent e) {
		// going to use a refresh button to refresh the artists

	}

	/* end dev usage */

	public MainWindow() {
		initComponents();
		showNowPlaying(false);
		showSelectedAlbumInfo(false);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				loadIndexes();
			}
		});

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
		setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/res/Application-256-trans.png")));
		setEnabled(true);
		setMinimumSize(new Dimension(984, 575));
		setPreferredSize(new Dimension(984, 575));
		setBackground(new Color(34, 34, 34));
		setLayout(new GroupLayout());
		add(getMainPanel(), new Constraints(new Bilateral(0, 0, 326),
				new Bilateral(0, 0, 545)));
	}

	private JToggleButton getJToggleButton0() {
		if (volumeToggleButton == null) {
			volumeToggleButton = new JToggleButton();
			volumeToggleButton.setText("");
			//volumeToggleButton.setBackground(Application.AppColor_Dark);
			volumeToggleButton.setContentAreaFilled(false);
			//volumeToggleButton.setFocusable(false);
			volumeToggleButton.setFocusPainted(false);
			volumeToggleButton.setSelectedIcon(new ImageIcon(getClass().getResource(
				"/res/Mute-24.png")));
			volumeToggleButton.setIcon(new ImageIcon(getClass().getResource(
				"/res/Vol-24.png")));
			volumeToggleButton.setBorder(null);
			
			volumeToggleButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (volumeToggleButton.isSelected()) {
						CurrentPlaylist.mute();
					} else {
						CurrentPlaylist.unmute();
					}
				}
			});
		}
		return volumeToggleButton;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setForeground(new Color(204, 204, 204));
			jLabel1.setText("Comment");
		}
		return jLabel1;
	}

	private JSeparator getJSeparator2() {
		if (jSeparator2 == null) {
			jSeparator2 = new JSeparator();
			jSeparator2.setForeground(new Color(102, 102, 102));
			jSeparator2.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparator2;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setForeground(new Color(204, 204, 204));
			jLabel0.setText("Shuffle All");
		}
		return jLabel0;
	}

	private JSeparator getJSeparator1() {
		if (jSeparator1 == null) {
			jSeparator1 = new JSeparator();
			jSeparator1.setForeground(new Color(102, 102, 102));
			jSeparator1.setOrientation(SwingConstants.VERTICAL);
			jSeparator1.setPreferredSize(new Dimension(1, 0));
		}
		return jSeparator1;
	}

	private JLabel getTrackPositionLabel() {
		if (trackPositionLabel == null) {
			trackPositionLabel = new JLabel();
			trackPositionLabel.setText("0:00 / 0:00");
			trackPositionLabel.setForeground(Application.AppColor_Text);
		}
		return trackPositionLabel;
	}

	private JProgressBar getTrackProgressBar() {
		if (trackProgressBar == null) {
			trackProgressBar = new JProgressBar();
			trackProgressBar.setBackground(new Color(50, 50, 50));
			trackProgressBar.setBorder(new EmptyBorder(1, 1, 1, 1));
			trackProgressBar.setForeground(new Color(65, 65, 65));
			trackProgressBar.setStringPainted(false);
			trackProgressBar.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					int width = (int) trackProgressBar.getVisibleRect()
							.getWidth();
					int duration = trackProgressBar.getMaximum();
					int mouseX = e.getX();
					// float secsPerPixel = (duration / width);
					double toSet = mouseX * (duration / width);
					System.out.println(toSet);
				}
			});

		}
		return trackProgressBar;
	}

	private JSlider getVolumeSlider() {
		if (volumeSlider == null) {
			volumeSlider = new JSlider();
			volumeSlider.setFocusable(false);
			volumeSlider.setMinimum(-60);
			volumeSlider.setMaximum(5);
			volumeSlider.setValue(0);
			volumeSlider.setMinorTickSpacing(5);
			volumeSlider.setMajorTickSpacing(20);
			volumeSlider.setSnapToTicks(false);
			volumeSlider.setPaintTicks(false);
			volumeSlider.setPaintLabels(false);
			volumeSlider.setForeground(Application.AppColor_Text);
			volumeSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					CurrentPlaylist.setVolume(volumeSlider.getValue());

				}
			});
		}
		return volumeSlider;
	}

	private JLabel getStatusLabel() {
		if (statusLabel == null) {
			statusLabel = new JLabel();
			statusLabel.setForeground(new Color(102, 102, 102));
			statusLabel.setText("");
		}
		return statusLabel;
	}

	private JPanel getJPanel0() {
		if (breadcrumbPanel == null) {
			breadcrumbPanel = new JPanel();
			breadcrumbPanel.setBackground(new Color(34, 34, 34));
			breadcrumbPanel.setLayout(new GroupLayout());
		}
		return breadcrumbPanel;
	}

	private JLabel getPlayAllButton() {
		if (playAllButton == null) {
			playAllButton = new PlayAllButton();
		}
		return playAllButton;
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

	private JLabel getAddAllButton() {
		if (addAllButton == null) {
			addAllButton = new AddAllButton();
		}
		return addAllButton;
	}

	private JLabel getNowPlayingAlbumArtLabel() {
		if (nowPlayingAlbumArtLabel == null) {
			nowPlayingAlbumArtLabel = new JLabel();
			nowPlayingAlbumArtLabel.setText("");
		}
		return nowPlayingAlbumArtLabel;
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
			nowPlayingArtistLabelLabel
					.setFont(new Font("Tahoma", Font.BOLD, 11));
			nowPlayingArtistLabelLabel.setForeground(new Color(150, 150, 150));
			nowPlayingArtistLabelLabel.setText("Artist:");
		}
		return nowPlayingArtistLabelLabel;
	}

	private JLabel getNowPlayingTitleLabelLabel() {
		if (nowPlayingTitleLabelLabel == null) {
			nowPlayingTitleLabelLabel = new JLabel();
			nowPlayingTitleLabelLabel
					.setFont(new Font("Tahoma", Font.BOLD, 11));
			nowPlayingTitleLabelLabel.setForeground(new Color(150, 150, 150));
			nowPlayingTitleLabelLabel.setText("Title:");
		}
		return nowPlayingTitleLabelLabel;
	}

	private JLabel getNowPlayingAlbumLabelLabel() {
		if (nowPlayingAlbumLabelLabel == null) {
			nowPlayingAlbumLabelLabel = new JLabel();
			nowPlayingAlbumLabelLabel
					.setFont(new Font("Tahoma", Font.BOLD, 11));
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
			// nowPlayingSeparator.setMaximumSize(new Dimension(1, 32767));
		}
		return nowPlayingSeparator;
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
		// Application.showMessage("Loading artists", artistList,
		// FloatingMessage.CENTER_PARENT, 0, false);
		// LoadingPane glassPane = new LoadingPane();
		// glassPane.setGlassPane(artistList.getRootPane());
		setStatus("Loading artists from server");

		if (getArtistIndex == null) {
			getArtistIndex = new GetArtistList();
		}
		if (getArtistIndex.isRunning()) {
			int response = JOptionPane.showConfirmDialog(this,
					"Already loading. Try again?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				getArtistIndex.stop();
				loadIndexes();
			} else {

			}

		} else {
			artistList.removeAll();
			getArtistIndex.init();

		}

		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (!getArtistIndex.isRunning()) {
					timer.cancel();
					artistList.setModel(getArtistIndex.artistList.getModel());
					// artistList.repaint();
					artistList
							.addListSelectionListener(new MyListSelectionListener());
					Application.closeMessage("Loading artists");
					setStatus(artistList.getModel().getSize()
							+ " artists found at " + AppConfig.SERVER_ADDRESS);
					// artistScrollPane.getRootPane().setGlassPane(null);
				}

			}
		}, 100, 100);

	}

	private JButton getShuffleButton() {
		if (shuffleButton == null) {
			shuffleButton = new JButton();
			shuffleButton.setIcon(new ImageIcon(getClass().getResource(
					"/res/Shuffle-48.png")));
			shuffleButton.setBorderPainted(true);
			shuffleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			shuffleButton.setFocusable(false);
			shuffleButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					CurrentPlaylist.shuffle();
					Application.setStatus("Playlist Shuffled");
				}
			});
		}
		return shuffleButton;
	}

	private JButton getRepeatButton() {
		if (repeatButton == null) {
			repeatButton = new JButton();
			repeatButton.setIcon(new ImageIcon(getClass().getResource(
					"/res/Repeat-48.png")));
			repeatButton.setBorderPainted(true);
			repeatButton.setFocusable(false);
			repeatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// TODO set button to toggle button style.
			repeatButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					CurrentPlaylist.setRepeatPlayback(!CurrentPlaylist
							.isRepeatPlay());
					String message = "Repeat ";
					message = (CurrentPlaylist.isRepeatPlay()) ? "Repeat on"
							: "Repeat off";
					Application.setStatus(message);
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
			selectedAlbumArtistLabel
					.setFont(new Font("Tahoma", Font.ITALIC, 12));
			selectedAlbumArtistLabel.setForeground(new Color(204, 204, 204));
			selectedAlbumArtistLabel.setText("artist");
		}
		return selectedAlbumArtistLabel;
	}

	private JButton getSkipForwardButton() {
		if (skipForwardButton == null) {
			skipForwardButton = new JButton();
			skipForwardButton.setIcon(new ImageIcon(getClass().getResource(
					"/res/Skip-Forward-48.png")));
			skipForwardButton.setBorderPainted(true);
			skipForwardButton.setFocusable(false);
			skipForwardButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					CurrentPlaylist.stopCurrentSong();
					CurrentPlaylist.playNextSong();
				}
			});
		}
		return skipForwardButton;
	}

	private JButton getSkipBackButton() {
		if (skipBackButton == null) {
			skipBackButton = new JButton();
			skipBackButton.setIcon(new ImageIcon(getClass().getResource(
					"/res/Skip-Back-48.png")));
			skipBackButton.setBorderPainted(true);
			skipBackButton.setFocusable(false);
			skipBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
			stopButton.setIcon(new ImageIcon(getClass().getResource(
					"/res/Stop-48.png")));
			stopButton.setBorderPainted(true);
			stopButton.setFocusable(false);
			stopButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (CurrentPlaylist.isActive()) {
						CurrentPlaylist.mute();
						CurrentPlaylist.stopAndClearPlaylist();
						showNowPlaying(false);
					}
				}
			});
		}
		return stopButton;
	}

	private PlayButton getPlayButton() {
		if (playButton == null) {
			playButton = new PlayButton();
		}
		return playButton;
	}

	public void setNowPlayingLabels() {
		nowPlayingAlbumLabel.setText(CurrentPlaylist.getCurrentAlbumName());
		nowPlayingAlbumArtLabel.setIcon(CurrentPlaylist
				.getCurrentAlbumArt(nowPlayingAlbumArtLabel.getWidth()));
		nowPlayingArtistLabel.setText(CurrentPlaylist.getCurrentArtistName());
		nowPlayingTitleLabel.setText(CurrentPlaylist.getCurrentTrackTitle());
		showNowPlaying(true);
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
			mediaControlPanel.add(getNowPlayingAlbumArtLabel(), new Constraints(new Leading(323, 59, 12, 12), new Leading(9, 53, 12, 12)));
			mediaControlPanel.add(getNowPlayingTitleLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(12, 12, 12)));
			mediaControlPanel.add(getNowPlayingArtistLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(29, 12, 12)));
			mediaControlPanel.add(getNowPlayingAlbumLabelLabel(), new Constraints(new Leading(394, 12, 12), new Leading(46, 12, 12)));
			mediaControlPanel.add(getNowPlayingTitleLabel(), new Constraints(new Bilateral(440, 12, 40), new Leading(12, 12, 12)));
			mediaControlPanel.add(getNowPlayingArtistLabel(), new Constraints(new Bilateral(440, 12, 47), new Leading(29, 12, 12)));
			mediaControlPanel.add(getNowPlayingAlbumLabel(), new Constraints(new Bilateral(440, 12, 50), new Leading(46, 12, 12)));
			mediaControlPanel.add(getNowPlayingSeparator(), new Constraints(new Leading(307, 2, 10, 10), new Leading(9, 53, 10, 10)));
			mediaControlPanel.add(getTrackPositionLabel(), new Constraints(new Leading(223, 70, 68, 68), new Leading(51, 10, 10)));
			mediaControlPanel.add(getTrackProgressBar(), new Constraints(new Leading(2, 209, 74, 74), new Leading(55, 9, 10, 10)));
		}
		return mediaControlPanel;
	}

	private JPanel getAlbumsSongsPanel() {
		if (albumsSongsPanel == null) {
			albumsSongsPanel = new JPanel();
			albumsSongsPanel.setBackground(new Color(34, 34, 34));
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
			linksPanel.add(getAddAllButton(), new Constraints(new Leading(85, 12, 12), new Leading(5, 12, 12)));
			linksPanel.add(getPlayAllButton(), new Constraints(new Leading(12, 47, 12, 12), new Leading(5, 10, 10)));
			linksPanel.add(getVolumeSlider(), new Constraints(new Trailing(12, 91, 142, 142), new Leading(5, 12, 12)));
			linksPanel.add(getJSeparator0(), new Constraints(new Leading(71, 179, 179), new Leading(5, 16, 12, 12)));
			linksPanel.add(getJSeparator1(), new Constraints(new Leading(136, 10, 10), new Leading(5, 16, 12, 12)));
			linksPanel.add(getJLabel0(), new Constraints(new Leading(149, 173, 173), new Leading(5, 12, 12)));
			linksPanel.add(getJSeparator2(), new Constraints(new Leading(218, 1, 10, 10), new Leading(5, 16, 12, 12)));
			linksPanel.add(getJLabel1(), new Constraints(new Leading(231, 173, 173), new Leading(5, 12, 12)));
			linksPanel.add(getJToggleButton0(), new Constraints(new Trailing(109, 23, 10, 10), new Leading(3, 20, 12, 12)));
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
			selectedAlbumArtLabel.setBorder(BorderFactory.createMatteBorder(1,
					1, 1, 1, new Color(102, 102, 102)));
			selectedAlbumArtLabel.setLayout(new GroupLayout());
		}
		return selectedAlbumArtLabel;
	}

	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = new JPanel();
			statusPanel.setBackground(new Color(34, 34, 34));
			statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
					new Color(50, 50, 50)));
			statusPanel.setLayout(new GroupLayout());
			statusPanel.add(getStatusLabel(), new Constraints(new Bilateral(12,
					12, 67), new Leading(3, 10, 10)));
		}
		return statusPanel;
	}

	private JScrollPane getArtistScrollPane() {
		if (artistScrollPane == null) {
			artistScrollPane = new JScrollPane();
			artistScrollPane.setBackground(new Color(34, 34, 34));
			artistScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1,
					1, new Color(45, 45, 45)));
			artistScrollPane.setViewportView(getArtistList());
		}
		return artistScrollPane;
	}

	private JList getArtistList() {
		if (artistList == null) {
			artistList = new JList();
			artistList.setBackground(new Color(34, 34, 34));
			artistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultListModel listModel = new DefaultListModel();
			artistList.setCellRenderer(new GetArtistList.ArtistCellRenderer());
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
			appLogoLabel.setIcon(new ImageIcon(getClass().getResource(
					"/res/application-logo.png")));
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
			artistFilterTextField.setBorder(new LineBorder(new Color(102, 102,
					102), 1, true));
			artistFilterTextField.addKeyListener(getFilterKeyListener());
		}
		return artistFilterTextField;
	}

	private KeyListener getFilterKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				final String filterText = ((JTextField) e.getSource()).getText().toLowerCase();
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						DefaultListModel model = (DefaultListModel) artistList.getModel();; 
						
						if (filterText.length() > 1) {
							GetArtistList.filteredItems.clear();
							for (int i = 0; i < GetArtistList.unfilteredItems.size(); i++) {
								if (GetArtistList.unfilteredItems.get(i).toLowerCase().contains(filterText)){
									GetArtistList.filteredItems.add(GetArtistList.unfilteredItems.get(i));
									System.out.println("Adding " + GetArtistList.unfilteredItems.get(i));
								}
							}
							if (!GetArtistList.filteredItems.isEmpty()) {
								model.removeAllElements();
								for (int j = 0; j < GetArtistList.filteredItems.size(); j++) {
									model.add(j, GetArtistList.filteredItems.get(j));
								}
							}
						} else {
							model.removeAllElements();
							for (int k = 0; k < GetArtistList.unfilteredItems.size(); k++) {
								model.add(k, GetArtistList.unfilteredItems.get(k));
							}
						}
						artistList.setModel(model);
						
					}
				});
				
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//
			}
		};
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
			mainPanel.add(getMediaControlPanel(), new Constraints(new Bilateral(325, 12, 305), new Leading(12, 70, 153, 153)));
			mainPanel.add(getAlbumsSongsPanel(), new Constraints(new Bilateral(325, 197, 462), new Bilateral(156, 32, 0)));
			mainPanel.add(getJPanel0(), new Constraints(new Bilateral(325, 197, 0), new Leading(124, 26, 10, 10)));
			mainPanel.add(getSelectedAlbumInfoPanel(), new Constraints(new Trailing(0, 0, 0), new Trailing(32, 402, 10, 10)));
			mainPanel.add(getArtistFilterTextField(), new Constraints(new Leading(12, 303, 10, 10), new Leading(91, 28, 61, 256)));
		}
		return mainPanel;
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}


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
			albumsSongsPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			albumsSongsPanel.removeAll();
			String loadingMessage = "Loading songs";
			// Application.showMessage(loadingMessage, albumsSongsPanel,
			// FloatingMessage.CENTER_PARENT, 0, false);
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
			setStatus(table.SONG_COUNT + " songs loaded for " + albumName);
			selectedAlbumArtLabel.setIcon(new ImageIcon(Server.getCoverArt(
					table.ALBUM_IMAGE_ID,
					selectedAlbumArtLabel.getPreferredSize().height)));
			CURRENT_SONGS_DATA = getSongsData(table);
			scrollPane.setVisible(true);
			Application.closeMessage(loadingMessage);
			albumsSongsPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
				try {
					JList list = (JList) evt.getSource();
					//int selectedIndex = evt.getLastIndex();
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
			albumsSongsPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			albumsSongsPanel.removeAll();
			String loadingMessage = "Loading albums";
			// Application.showMessage(loadingMessage, albumsSongsPanel,
			// FloatingMessage.CENTER_PARENT, 0, false);
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
							
							boolean isDir = Server.containsDirectories(albumID);
							
							if (isDir) {
								showAlbums(albumID, artistName);
							} else {
								showSongs(albumID, albumName, artistName);
							}
							
							table.setEnabled(false);
						}

					});
			albumsSongsPanel.removeAll();
			albumsSongsPanel.setLayout(null);
			albumsSongsPanel.add(scrollPane);
			setStatus(table.albumCount + " albums loaded for " + artistName);
			Application.closeMessage(loadingMessage);
			albumsSongsPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void showSongs(String albumID, String albumName, String artistName) {
		if (showSongsThread.isRunning()) {
			showSongsThread.stop();
		}
		showSongsThread.init(albumID, albumName, artistName);

	}

	public void setStatus(String string) {
		statusLabel.setText(string);

	}

	public void setPlayButtonPressed(boolean b) {
		playButton.setPlaying(b);
	}

	public String[] getVisibleData() {
		return currentTableData;

	}

	public void setTrackDuration(int duration) {
		int mins = (int) Math.floor(duration / 60);
		int secs = (int) (duration % 60);
		String secsStr = "0";

		if (secs < 10) {
			secsStr += Integer.toString(secs);
		} else {
			secsStr = Integer.toString(secs);
		}

		String durStr = Integer.toString(mins) + ":" + secsStr;
		trackPositionLabel.setText("0:00 / " + durStr);
		trackProgressBar.setMaximum(duration);

	}

	public void setTrackPosition(int position) {
		// duration
		String[] tmp = trackPositionLabel.getText().split("/");
		String durationStr = tmp[tmp.length - 1];

		// current position
		int mins = (int) Math.floor(position / 60);
		int secs = (int) (position - (mins * 60));
		String secsStr = null;
		if (secs < 10) {
			secsStr = "0" + Integer.toString(secs);
		} else {
			secsStr = Integer.toString(secs);
		}

		final String current = Integer.toString(mins) + ":" + secsStr;
		trackPositionLabel.setText(current + " /" + durationStr); // durationStr
																	// already
																	// contains
																	// the space
		trackProgressBar.setValue(position);

	}

}
