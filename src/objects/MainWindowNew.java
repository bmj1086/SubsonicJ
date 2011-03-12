package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.JTable;
import javax.swing.JTextField;
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
	private JLabel albumArtLabel;
	private JPanel albumsSongsPanel;
	private JPanel mediaControlPanel;
	private JButton playButton;
	private JButton stopButton;
	private JButton skipBackButton;
	private JTextField artistFilterTextField;
	private JButton skipForwardButton;
	private JLabel playingAlbumLabel;
	private JLabel statusLabel;
	private JLabel playingArtistLabel;
	private JButton repeatButton;
	private JButton shuffleButton;
	private JPanel nowPlayingPanel;
//private static final String PREFERRED_LOOK_AND_FEEL = "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel";
	
	public static String[][] ARTIST_IDs = null;
	public static Object[][] CURRENT_ALBUM_IDs = null;
	public static Object[][] CURRENT_SONGS_DATA = null;
	public static String CURRENT_ATRIST = null;
	//static JLabel loadingLabel = new JLabel("Loading...");
	ShowSongsThread showSongsThread = new ShowSongsThread();
	ShowAlbumsThread showAlbumsThread = new ShowAlbumsThread();
	public static LoadIndexes_Thread loadIndexesThread = null;
	
	/* button icons */
	static ImageIcon pauseButtonIcon = new ImageIcon(MainWindowNew.class.getResource("/res/Pause-48.png"));
	static ImageIcon playButtonIcon = new ImageIcon(MainWindowNew.class.getResource("/res/Play-48.png"));
	
	/* for dev usage 
	 * I use this area to test things by assigning them to the app logo
	 * mouse click event
	 * 
	 * MAKE SURE YOU REMOVE ANYTHING YOU ADD HERE BEFORE PUSHING
	 * 
	 */

	protected void appLogoMouseClicked(MouseEvent e) {
		loadIndexes();
		
	}
	
	/* end dev usage */
	
	
	public MainWindowNew() {
		initComponents();
		loadIndexes();
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
	
	private JPanel getNowPlayingPanel() {
		if (nowPlayingPanel == null) {
			nowPlayingPanel = new JPanel();
			nowPlayingPanel.setBackground(new Color(34, 34, 34));
			nowPlayingPanel.setLayout(new GroupLayout());
			nowPlayingPanel.add(getAlbumArtLabel(), new Constraints(new Leading(10, 10, 31), new Leading(10, 138, 10, 10)));
			nowPlayingPanel.add(getPlayingArtistLabel(), new Constraints(new Leading(10, 11, 41), new Leading(160, 11, 11)));
			nowPlayingPanel.add(getPlayingAlbumLabel(), new Constraints(new Leading(10, 152, 10, 10), new Leading(187, 11, 11)));
		}
		return nowPlayingPanel;
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
		}
		return shuffleButton;
	}
	
	private JButton getRepeatButton() {
		if (repeatButton == null) {
			repeatButton = new JButton();
			repeatButton.setIcon(new ImageIcon(getClass().getResource("/res/Repeat-48.png")));
			repeatButton.setBorderPainted(true);
			repeatButton.setFocusable(false);
		}
		return repeatButton;
	}

	private JLabel getPlayingAlbumLabel() {
		if (playingAlbumLabel == null) {
			playingAlbumLabel = new JLabel();
			playingAlbumLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			playingAlbumLabel.setForeground(new Color(204, 204, 204));
			//jLabel1.setMinimumSize(new Dimension(sdf));
			playingAlbumLabel.setText("");
		}
		return playingAlbumLabel;
	}

	private JLabel getPlayingArtistLabel() {
		if (playingArtistLabel == null) {
			playingArtistLabel = new JLabel();
			playingArtistLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
			playingArtistLabel.setForeground(new Color(204, 204, 204));
			playingArtistLabel.setText("jLabel0");
		}
		return playingArtistLabel;
	}

	private JButton getSkipForwardButton() {
		if (skipForwardButton == null) {
			skipForwardButton = new JButton();
			skipForwardButton.setIcon(new ImageIcon(getClass().getResource("/res/Skip-Forward-48.png")));
			skipForwardButton.setBorderPainted(true);
			skipForwardButton.setFocusable(false);
		}
		return skipForwardButton;
	}

	private JButton getSkipBackButton() {
		if (skipBackButton == null) {
			skipBackButton = new JButton();
			skipBackButton.setIcon(new ImageIcon(getClass().getResource("/res/Skip-Back-48.png")));
			skipBackButton.setBorderPainted(true);
			skipBackButton.setFocusable(false);
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
		if (CurrentSong.isQueued){
			try {
				CurrentSong.stopSong();
				switchPausePlayIcon();
			} catch (Exception ignore){
				
			}
		}
	}

	private JButton getPlayButton() {
		if (playButton == null) {
			playButton = new JButton();
			playButton.setIcon(new ImageIcon(getClass().getResource("/res/Play-48.png")));
			playButton.setBorderPainted(true);
			playButton.setFocusable(false);
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
		if (CurrentSong.isQueued) {
			try {
				CurrentSong.pauseOrUnpauseSong();
				switchPausePlayIcon();
			} catch (Exception ignore){
				
			}
			
		} 
		
	}

	protected void switchPausePlayIcon() {
		if (playButton.getIcon() == playButtonIcon) {
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
		}
		return mediaControlPanel;
	}

	private JPanel getAlbumsSongsPanel() {
		if (albumsSongsPanel == null) {
			albumsSongsPanel = new JPanel();
			albumsSongsPanel.setBackground(new Color(34, 34, 34));
			//albumsSongsPanel.setMinimumSize(new Dimension(170, 2147483647));
			//albumsSongsPanel.setPreferredSize(new Dimension(0, 0));
			//albumsSongsPanel.setMaximumSize(new Dimension(170, 2147483647));
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
		}
		return linksPanel;
	}
	
	private JLabel getAlbumArtLabel() {
		if (albumArtLabel == null) {
			albumArtLabel = new JLabel();
			albumArtLabel.setBackground(new Color(34, 34, 34));
			albumArtLabel.setMinimumSize(new Dimension(150, 150));
			albumArtLabel.setPreferredSize(new Dimension(150, 150));
			albumArtLabel.setMaximumSize(new Dimension(150, 150));
			albumArtLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(102, 102, 102)));
			albumArtLabel.setLayout(new GroupLayout());
		}
		return albumArtLabel;
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
			statusLabel.setText("Status Label");
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
			mainPanel.add(getAlbumsSongsPanel(), new Constraints(new Bilateral(325, 197, 462), new Bilateral(131, 32, 0)));
			mainPanel.add(getArtistScrollPane(), new Constraints(new Leading(13, 300, 12, 12), new Bilateral(131, 32, 22)));
			mainPanel.add(getMediaControlPanel(), new Constraints(new Bilateral(325, 199, 241), new Leading(12, 38, 38)));
			mainPanel.add(getArtistFilterTextField(), new Constraints(new Leading(12, 303, 10, 10), new Leading(91, 27, 10, 10)));
			mainPanel.add(getNowPlayingPanel(), new Constraints(new Trailing(0, 0, 0), new Bilateral(131, 32, 0)));
		}
		return mainPanel;
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
//	public static void main(String[] args) {
//		//installLnF();
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				MainWindowNew frame = new MainWindowNew();
//				frame.setDefaultCloseOperation(MainWindowNew.EXIT_ON_CLOSE);
//				frame.setTitle("MainWindowNew");
//				frame.getContentPane().setPreferredSize(frame.getSize());
//				frame.pack();
//				frame.setLocationRelativeTo(null);
//				frame.setVisible(true);
//			}
//		});
//	}

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
			statusLabel.setText("Loading songs for " + albumName);
			SongsTable table = new SongsTable(albumID);
			MyScrollPane scrollPane = new MyScrollPane(table);

			playingArtistLabel.setText(artistName);
			playingAlbumLabel.setText(albumName);

			scrollPane.setSize(albumsSongsPanel.getWidth(),
					albumsSongsPanel.getHeight());
			albumsSongsPanel.removeAll();
			albumsSongsPanel.setLayout(null);
			albumsSongsPanel.add(scrollPane);
			scrollPane.validate();
			albumsSongsPanel.validate();
			statusLabel.setText(table.SONG_COUNT + " songs loaded for "
					+ albumName);
			albumArtLabel.setIcon(new ImageIcon(Server.getCoverArt(
					table.ALBUM_IMAGE_ID,
					albumArtLabel.getPreferredSize().height)));
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
		albumArtLabel.setVisible(show);
		playingArtistLabel.setVisible(show);
		playingAlbumLabel.setVisible(show);
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
					// Object selectedIndex = evt.getLastIndex();
					String sel = (String) list.getSelectedValue();
					String artistID = getArtistID(sel.toString());
					showAlbums(artistID, sel.toString());
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
			statusLabel.setText("Loading artists from server");
			Document doc = Server.getIndexes();
			NodeList artistNodeList = doc.getElementsByTagName("artist");
			int artistCount = artistNodeList.getLength();
			statusLabel.setText(artistCount + " artists found at "
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
			statusLabel.setText(artistCount + " artists loaded from "
					+ Settings.SERVER_ADDRESS);
			artistList.setModel(listModel);
			artistScrollPane.validate();
			artistList.validate();
			
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
			statusLabel.setText(table.albumCount + " albums loaded for "
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
	
	
}
