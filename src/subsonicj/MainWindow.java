package subsonicj;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import objects.*;

import org.w3c.dom.*;

import servercontact.Server;
import settings.Preferences;
import settings.UI;

public class MainWindow extends JFrame {

	public static String[][] ARTIST_IDs = null;
	public static Object[][] CURRENT_ALBUM_IDs = null;
	public static Object[][] CURRENT_SONGS_DATA = null;
	public static String CURRENT_ATRIST = null;
	static JLabel loadingLabel = new JLabel("Loading...");
	ShowSongsThread showSongsThread = new ShowSongsThread();
	ShowAlbumsThread showAlbumsThread = new ShowAlbumsThread(); 
	public static LoadIndexes_Thread loadIndexesThread = null;


	public MainWindow() {
		initComponents();
		loadIndexes();

	}

	private void loadIndexes() {
		if (loadIndexesThread == null){
			loadIndexesThread = new LoadIndexes_Thread();
			
		}
		if (loadIndexesThread.isRunning()) {
			int response = 
				JOptionPane.showConfirmDialog(this, "Already loading. Try again?", 
						"Confirm", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				loadIndexesThread.stop();
				loadIndexes();
			} else {
				
			}
			
		} else {
			loadIndexesThread.init();
		}
		
		
	}

	private void initComponents() {
		mainPanel = new JPanel();
		appLogoLabel = new JLabel();
		albumArtistPanel = new JPanel();
		albumArtLabel = new JLabel();
		linksPanel = new JPanel();
		playAllLabel = new JLabel();
		addAllToPlaylistLabel = new JLabel();
		artistListScrollPane = new JScrollPane();
		artistList = new JList();
		artistFilterTextField = new JTextField();
		artistLabel = new JLabel();
		albumLabel = new JLabel();
		statusPanel = new JPanel();
		statusLabel = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setTitle("SubsonicJ");
		setIconImage(new ImageIcon(Main.class.getResource("/res/Application-256.png")).getImage());
		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentShown(java.awt.event.ComponentEvent evt) {
				formComponentShown(evt);
			}
		});

		mainPanel.setBackground(UI.AppColor_Dark);

		appLogoLabel.setIcon(new ImageIcon(getClass().getResource("/res/application-logo.png")));
		appLogoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				appLogoLabelMouseClicked(evt);
			}
		});

		albumArtistPanel.setLayout(null);
		albumArtistPanel.setBackground(UI.AppColor_Dark);
//		MatteBorder albumArtistBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
//				defaultAppColor_Border);
//		albumArtistPanel.setBorder(albumArtistBorder);

		albumArtLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UI.AppColor_Border));
		albumArtLabel.setPreferredSize(new java.awt.Dimension(150, 150));
		albumArtLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				albumArtLabelMouseClicked(evt);
			}
		});

		linksPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UI.AppColor_SelBgndClr));
		linksPanel.setBackground(UI.AppColor_Dark);

		playAllLabel.setForeground(UI.AppColor_Text);
		playAllLabel.setText("Play All");
		playAllLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		playAllLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	playAllLabelMouseClicked(evt);
            }

        });

		addAllToPlaylistLabel.setForeground(UI.AppColor_Text);
		addAllToPlaylistLabel.setText("Add All To Playlist");
		addAllToPlaylistLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		GroupLayout linksPanelLayout = new GroupLayout(linksPanel);
		linksPanel.setLayout(linksPanelLayout);
		linksPanelLayout.setHorizontalGroup(linksPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						linksPanelLayout.createSequentialGroup()
								.addContainerGap().addComponent(playAllLabel)
								.addGap(18, 18, 18)
								.addComponent(addAllToPlaylistLabel)
								.addContainerGap(353, Short.MAX_VALUE)));
		linksPanelLayout.setVerticalGroup(linksPanelLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(playAllLabel, GroupLayout.DEFAULT_SIZE, 27,
						Short.MAX_VALUE)
				.addComponent(addAllToPlaylistLabel, GroupLayout.DEFAULT_SIZE,
						25, Short.MAX_VALUE));

		artistListScrollPane.setBorder(null);

		artistList.setBackground(UI.AppColor_Dark);
		artistList.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
				UI.AppColor_Border));
		artistList.setFont(new java.awt.Font("Tahoma", 0, 14));
		artistList.setForeground(UI.AppColor_Text);
		artistList.setModel(new DefaultListModel());
		artistList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		artistList.setFocusable(false);
		artistList.setSelectionBackground(UI.AppColor_SelBgndClr);
		artistList.setSelectionForeground(UI.AppColor_Text);
		artistListScrollPane.setViewportView(artistList);

		artistFilterTextField.setFont(new java.awt.Font("Tahoma", 0, 12));
		artistFilterTextField.setHorizontalAlignment(SwingConstants.CENTER);
		artistFilterTextField.setText("Filter");
		artistFilterTextField.setBackground(UI.AppColor_Dark);
		artistFilterTextField.setForeground(UI.AppColor_Text);
		artistFilterTextField.setBorder(BorderFactory.createMatteBorder(1, 1,
				1, 1, UI.AppColor_SelBgndClr));
		artistFilterTextField.setCursor(new java.awt.Cursor(
				java.awt.Cursor.TEXT_CURSOR));

		artistFilterTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				artistFilterTextFieldKeyTyped(evt);
			}
		});

		artistLabel.setFont(new java.awt.Font("Tahoma", 2, 11));
		artistLabel.setForeground(UI.AppColor_Text);

		albumLabel.setForeground(UI.AppColor_Text);

		albumArtLabel.setVisible(false);

		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout
				.setHorizontalGroup(mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				mainPanelLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.TRAILING,
																								false)
																						.addComponent(
																								artistFilterTextField)
																						.addComponent(
																								artistListScrollPane,
																								GroupLayout.DEFAULT_SIZE,
																								295,
																								Short.MAX_VALUE))
																		.addGap(18,
																				18,
																				18)
																		.addGroup(
																				mainPanelLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								linksPanel,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								albumArtistPanel,
																								GroupLayout.DEFAULT_SIZE,
																								501,
																								Short.MAX_VALUE))
																		.addGap(18,
																				18,
																				18)
																		.addGroup(
																				mainPanelLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								albumArtLabel,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								albumLabel,
																								GroupLayout.PREFERRED_SIZE,
																								150,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								artistLabel,
																								GroupLayout.PREFERRED_SIZE,
																								150,
																								GroupLayout.PREFERRED_SIZE))
																		.addContainerGap())
														.addComponent(
																appLogoLabel,
																GroupLayout.PREFERRED_SIZE,
																172,
																GroupLayout.PREFERRED_SIZE))));
		mainPanelLayout
				.setVerticalGroup(mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								GroupLayout.Alignment.TRAILING,
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(appLogoLabel,
												GroupLayout.PREFERRED_SIZE, 52,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.TRAILING)
														.addComponent(
																linksPanel,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																artistFilterTextField,
																GroupLayout.DEFAULT_SIZE,
																29,
																Short.MAX_VALUE))
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																albumArtistPanel,
																GroupLayout.DEFAULT_SIZE,
																486,
																Short.MAX_VALUE)
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				albumArtLabel,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(0,
																				0,
																				0)
																		.addComponent(
																				artistLabel,
																				GroupLayout.PREFERRED_SIZE,
																				17,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(0,
																				0,
																				0)
																		.addComponent(
																				albumLabel,
																				GroupLayout.PREFERRED_SIZE,
																				17,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																artistListScrollPane,
																GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE,
																486,
																Short.MAX_VALUE))
										.addContainerGap()));

		statusPanel.setBackground(new Color(40, 40, 40));
		statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
				UI.AppColor_SelBgndClr));

		statusLabel.setForeground(UI.AppColor_Border);
		statusLabel.setText("\n");

		GroupLayout statusPanelLayout = new GroupLayout(statusPanel);
		statusPanel.setLayout(statusPanelLayout);
		statusPanelLayout.setHorizontalGroup(statusPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						statusPanelLayout
								.createSequentialGroup()
								.addGap(3, 3, 3)
								.addComponent(statusLabel,
										GroupLayout.PREFERRED_SIZE, 595,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(404, Short.MAX_VALUE)));
		statusPanelLayout.setVerticalGroup(statusPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(statusLabel, GroupLayout.DEFAULT_SIZE, 19,
						Short.MAX_VALUE));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(statusPanel, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(0, 0, 0)
						.addComponent(statusPanel, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));

	}

	protected void playAllLabelMouseClicked(MouseEvent evt) {
		// TODO add method information for play all
		
	}

	private void formComponentShown(java.awt.event.ComponentEvent evt) {
		// close the loading frame when the main window completely loads
		//Main..setVisible(false);
	}

	private void appLogoLabelMouseClicked(java.awt.event.MouseEvent evt) {
		// TODO testing area for dev usage only
		CurrentSong.pauseSong();
	}

	private void artistFilterTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
		if (!artistFilterTextField.getText().equals("")) {
			filterArtistView(artistFilterTextField.getText());
		}
	}

	private void albumArtLabelMouseClicked(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}

	private JLabel albumArtLabel;
	private JPanel albumArtistPanel;
	private JLabel albumLabel;
	private JTextField artistFilterTextField;
	private JLabel artistLabel;
	private JList artistList;
	private JLabel appLogoLabel;
	private JLabel playAllLabel;
	private JLabel addAllToPlaylistLabel;
	private JPanel linksPanel;
	private JScrollPane artistListScrollPane;
	private JPanel mainPanel;
	private JLabel statusLabel;
	private JPanel statusPanel;

	

	private String getArtistID(String artistName) {
		for (int i = 0; i < ARTIST_IDs.length; i++) {
			if (ARTIST_IDs[i][0].equals(artistName)) {
				return ARTIST_IDs[i][1];
			}
		}
		return null;
	}

	private void showAlbums(String id, final String artistName) {
		if (showAlbumsThread.isRunning()) {
			showAlbumsThread.stop();
		}
		showAlbumsThread.init(id, artistName);

	}
	
	private class ShowAlbumsThread implements Runnable{
		
		String artistID, artistName;
		
		Thread thread;
		
		public boolean isRunning(){
			if (thread != null) {
				return thread.isAlive();
			} else {
				return false;
			}
			
		}
		
		public void init(String artID, String artName){
			thread = new Thread(this);
			stop();
			artistID = artID;
			artistName = artName;
			thread.start();
		}
		
		public void stop(){
			if (thread != null) {
				thread.interrupt();
				System.out.println("MainWindow: Initialized thread interruption");
			}
		}
		
		@Override
		public void run(){
			showSelectedAlbumInfo(false);
			final AlbumTable table = new AlbumTable(artistID);
			MyScrollPane scrollPane = new MyScrollPane(table);
			
			CURRENT_ALBUM_IDs = table.ALBUM_INFO;
			scrollPane.setSize(albumArtistPanel.getWidth(),
					albumArtistPanel.getHeight());
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							String albumName = table.getModel()
									.getValueAt(e.getFirstIndex(), 1)
									.toString();
							String artistName = artistList
									.getSelectedValue().toString();
							String albumID = CURRENT_ALBUM_IDs[e
									.getFirstIndex()][2].toString();
							showSongs(albumID, albumName, artistName);
							table.setEnabled(false);
						}

					});
			albumArtistPanel.removeAll();
			albumArtistPanel.setLayout(null);
			albumArtistPanel.add(scrollPane);
			albumArtistPanel.validate();
			statusLabel.setText(table.albumCount
					+ " albums loaded for " + artistName);
		}
	}

	private void showSongs(String albumID, String albumName, String artistName) {
		if (showSongsThread.isRunning()) {
			showSongsThread.stop();
		}
		showSongsThread.init(albumID, albumName, artistName);
		
	}
	
	private class ShowSongsThread implements Runnable{
		
		String albumID, albumName, artistName;
		Thread thread;
		
		public void init(String albID, String albName, String artName){
			stop();
			thread = new Thread(this);
			albumID = albID;
			albumName = albName;
			artistName = artName;
			thread.start();
			
		}
		
		public boolean isRunning(){
			if (thread != null) {
				return thread.isAlive();
			} else {
				return false;
			}
			
		}
		
		public void stop(){
			if (thread != null) {
				thread.interrupt();
				System.out.println("MainWindow: Initialized thread interruption");
			}
		}
		
		@Override
		public void run(){
			setLoading(true);
			showSelectedAlbumInfo(true);
			statusLabel.setText("Loading songs for " + albumName);
			SongsTable table = new SongsTable(albumID);
			MyScrollPane scrollPane = new MyScrollPane(table);
			
			artistLabel.setText(artistName);
			albumLabel.setText(albumName);

			scrollPane.setSize(albumArtistPanel.getWidth(),
					albumArtistPanel.getHeight());
			albumArtistPanel.removeAll();
			albumArtistPanel.setLayout(null);
			albumArtistPanel.add(scrollPane);
			scrollPane.validate();
			albumArtistPanel.validate();
			statusLabel.setText(table.SONG_COUNT
					+ " songs loaded for " + albumName);
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
	    for (int i = 0 ; i < nRow ; i++)
	        for (int j = 0 ; j < nCol ; j++)
	            tableData[i][j] = dtm.getValueAt(i,j);
	    return tableData;
	}

	private void showSelectedAlbumInfo(boolean show) {
		albumArtLabel.setVisible(show);
		artistLabel.setVisible(show);
		albumLabel.setVisible(show);
	}

	private void setLoading(boolean loading) {
//		if (loading) {
//			int width = loadingFrame.getSize().width;
//			int height = loadingFrame.getSize().height;
//			int x = (albumArtistPanel.getWidth() / 2) - (width / 2);
//			int y = (albumArtistPanel.getHeight() / 2) - (height / 2);
//			loadingFrame.setLocation(x, y);
//			loadingFrame.setVisible(true);
//			
//			
//		} else {
//			try {
//				albumArtistPanel.remove(loadingLabel);
//			} catch (Exception e) {
//			}
//		}

	}

	private void filterArtistView(String text) {
		//
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
				JList list = (JList) evt.getSource();
				//Object selectedIndex = evt.getLastIndex();
				String sel = (String) list.getSelectedValue();
				String artistID = getArtistID(sel.toString());
				showAlbums(artistID, sel.toString());

			}
			if (evt.getValueIsAdjusting()) {
				// multiselect ignore
			}
		}
	}

	class SelectionListener implements ListSelectionListener {
		JTable table;

		SelectionListener(JTable table) {
			this.table = table;
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			System.out.println(table.getModel()
					.getValueAt(e.getFirstIndex(), 2));
		}

	}
	
	class LoadIndexes_Thread implements Runnable{
		
		Thread thread = new Thread();
		
		public LoadIndexes_Thread() {
			// TODO Auto-generated method stub

		}
		
		public void init(){
			thread = new Thread(this);
	        thread.start();
		}
		
		public void stop(){
			if (thread != null) {
				thread.interrupt();
			}
		}
		
		public boolean isRunning(){
			return thread.isAlive();
		}
		
		@Override
		public void run() {
			statusLabel.setText("Loading artists from server");
			Document doc = Server.getIndexes();

			NodeList artistNodeList = doc.getElementsByTagName("artist");
			int artistCount = artistNodeList.getLength();
			statusLabel.setText(artistCount + " artists found at " + Preferences.SERVER_ADDRESS);
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
					+ Preferences.SERVER_ADDRESS);
			artistList.setModel(listModel);
			albumArtistPanel.validate();
			
		}
		

	}
}
