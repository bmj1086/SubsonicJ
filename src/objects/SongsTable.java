package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import main.Application;
import main.Main;
import mp3player.CurrentPlaylist;

import objects.windows.MainWindow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;

@SuppressWarnings("serial")
public class SongsTable extends JTable {
	static final String[] columnNames = { "PlayPause", "AddToPlaylist",
			"Track", "Song", "Duration", "songID", "bitRate", "parent" };
	static String ALBUM_ID = null;
	static ImageIcon playIcon = new ImageIcon(
			Main.class.getResource("/res/Play-15.png"));
	static ImageIcon addIcon = new ImageIcon(
			Main.class.getResource("/res/Add-to-playlist-15.png"));
	static ImageIcon playIconHover = new ImageIcon(
			Main.class.getResource("/res/Play-15-hover.png"));
	static ImageIcon addIconHover = new ImageIcon(
			Main.class.getResource("/res/Add-to-playlist-15-hover.png"));

	public String ALBUM_IMAGE_ID = null;
	public Object[][] SONGS_INFO = null;
	public int SONG_COUNT = 0;
	
	public int mouseOverRow = 0;
	public int mouseOverCol = 0;

	public SongsTable(String albumID) {
		super();
		ALBUM_ID = albumID;
		ALBUM_IMAGE_ID = Server.getCoverArt(albumID);
		initComponents();
	}

	private void initComponents() {
		addMouseMotionListener(getMouseMotionListener());
		Document doc = Server.getMusicDirectory(ALBUM_ID);
		Server.currentMusicDirectoryID = ALBUM_ID;
		NodeList songNodes = doc.getElementsByTagName("child");
		SONG_COUNT = songNodes.getLength();
		System.out.println("SongsTable: Found " + SONG_COUNT + " songs");
		SONGS_INFO = new Object[SONG_COUNT][columnNames.length];

		// sets the current data on the main window
		MainWindow.currentTableData = new String[songNodes.getLength()];

		for (int i = 0; i < songNodes.getLength(); i++) {
			Element songNode = (Element) songNodes.item(i);
			SONGS_INFO[i][2] = songNode.getAttribute("track");
			SONGS_INFO[i][3] = songNode.getAttribute("title");
			SONGS_INFO[i][4] = convertDurationToString(songNode
					.getAttribute("duration"));
			SONGS_INFO[i][5] = songNode.getAttribute("id");
			MainWindow.currentTableData[i] = songNode.getAttribute("id");
			SONGS_INFO[i][6] = songNode.getAttribute("bitRate");
			SONGS_INFO[i][7] = songNode.getAttribute("parent");
		}

		DefaultTableModel model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setDataVector(SONGS_INFO, columnNames);
		setModel(model);

		setBorder(new LineBorder(new Color(34, 34, 34)));
		setFocusable(false);
		removeEditor();

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				Point click = new Point(me.getX(), me.getY());
				int column = columnAtPoint(click);
				int row = rowAtPoint(click);
				if (column == 0) {
					//System.out.println("Playing " + getValueAt(row, 3));
					String songID = getValueAt(row, 5).toString();
					String parentID = getValueAt(row, 7).toString();
					//String songName = getValueAt(row, 3).toString();
					CurrentPlaylist.stopAndClearPlaylist();
					CurrentPlaylist.addSongToPlaylist(songID, parentID, true);

				} else if (column == 1) {
					String songID = getValueAt(row, 5).toString();
					String parentID = getValueAt(row, 7).toString();
					String songName = getValueAt(row, 3).toString();
					CurrentPlaylist.addSongToPlaylist(songID, parentID, false);
					Application.setStatus("Added " + songName
							+ " to playlist successfully");

				}

			}
		});
		// setup table information
		setForeground(Color.WHITE);
		setBackground(new Color(34, 34, 34));
		setRowHeight(30);
		setShowGrid(false);
		setRowMargin(5);
		getColumnModel().setColumnMargin(5);
		setTableHeader(null);
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setSelectionForeground(Color.WHITE);
		setSelectionBackground(new Color(50, 50, 50));

		// play song column
		getColumnModel().getColumn(0).setCellRenderer(new PlayButtonRenderer());
		getColumnModel().getColumn(0).setMinWidth(25);
		getColumnModel().getColumn(0).setMaxWidth(25);
		getColumnModel().getColumn(0).setWidth(25);

		// add to playlist column
		getColumnModel().getColumn(1).setCellRenderer(new AddToPlaylistRenderer());
		getColumnModel().getColumn(1).setCellEditor(new AddButtonEditor(new JCheckBox()));
		getColumnModel().getColumn(1).setMinWidth(25);
		getColumnModel().getColumn(1).setMaxWidth(25);
		getColumnModel().getColumn(1).setWidth(25);

		// song track number column
		getColumnModel().getColumn(2).setMinWidth(30);
		getColumnModel().getColumn(2).setMaxWidth(30);
		getColumnModel().getColumn(2).setWidth(30);

		// song duration column
		getColumnModel().getColumn(4).setMinWidth(70);
		getColumnModel().getColumn(4).setMaxWidth(70);
		getColumnModel().getColumn(4).setWidth(70);

		// loop to hide columns after duration may change this in the future to
		// show more info.
		// --- could possibly have settings to show different information for
		// user settings.

		for (int i = 5; i < getColumnCount(); i++) {
			getColumnModel().getColumn(i).setMinWidth(0);
			getColumnModel().getColumn(i).setMaxWidth(0);
			getColumnModel().getColumn(i).setWidth(0);
		}

	}

	private MouseMotionListener getMouseMotionListener() {
		MouseMotionListener theReturn = new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseOverRow = rowAtPoint(e.getPoint());
				mouseOverCol = columnAtPoint(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// unused

			}

		};
		return theReturn;
	}

	private String convertDurationToString(String attribute) { // convert
																// duration to
																// minutes
		int durationInt = Integer.parseInt(attribute);
		int secs = durationInt % 60;
		int mins = (durationInt - secs) / 60;
		String secsStr = Integer.toString(secs);
		if (secs < 10) {
			secsStr = "0" + secsStr;
		}
		String durationStr = Integer.toString(mins) + ":" + secsStr;
		return durationStr;
	}

	class PlayButtonRenderer extends JButton implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText("");
			setIcon(playIcon);
			setBorderPainted(false);
			setContentAreaFilled(false);

			return this;
		}
	}

	class AddButton extends JButton {
		public AddButton(String text, ImageIcon icon) {
			super(text, icon);
			setBorderPainted(false);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	class PlayPauseRenderer extends DefaultTableCellRenderer {
		public PlayPauseRenderer() {
			setHorizontalTextPosition(CENTER);
			setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
			setSize(20, 20);
		}

		@Override
		public void setValue(Object value) {
			if (value instanceof Image) {
				setIcon(new ImageIcon(((Image) value)));
			}
		}
	}

	class AddToPlaylistRenderer extends JButton implements TableCellRenderer {

		public AddToPlaylistRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
//			if (row == mouseOverRow && column == mouseOverCol) {
//				setIcon(addIconHover);
//			} else {
//				setIcon(addIcon);
//			}
			
			setIcon(addIcon);
			setText((value == null) ? "" : value.toString());
			setContentAreaFilled(false);
			setRolloverIcon(addIconHover);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			return this;
		}

	}

	class AddButtonEditor extends DefaultCellEditor {
		protected JButton button;

		private String label;

		private boolean isPushed;

		public AddButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setBorderPainted(false);
			button.setIcon(addIcon);
			button.setRolloverIcon(addIconHover);
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			button.setOpaque(true);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				//
				//
				JOptionPane.showMessageDialog(button, label + ": Ouch!");
				// System.out.println(label + ": Ouch!");
			}
			isPushed = false;
			return new String(label);
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
}
