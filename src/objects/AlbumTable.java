package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import main.Application;
import main.Cache;
import objects.windows.MainWindow;
import servercontact.Server;

@SuppressWarnings("serial")
public class AlbumTable extends JTable {

	static final String[] columnNames = { "", "Album", "albumID", "coverArtID" };
	static String ARTIST_ID = null;
	static int IMAGE_SIZE = 75;
	public Object[][] ALBUM_INFO = null;
	public int albumCount = 0;

	public AlbumTable(String incomingArtistID) {
		// super();
		ARTIST_ID = incomingArtistID;
		Server.currentMusicDirectoryID = incomingArtistID;
		initComponents();
		addMouseMotionListener(getMouseMotionListener());
	}

	private MouseMotionListener getMouseMotionListener() {
		MouseMotionListener theReturn = new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (columnAtPoint(e.getPoint()) == 0
						|| columnAtPoint(e.getPoint()) == 1) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// ignore
			}
		};
		return theReturn;
	}

	private void initComponents() {
		
		Image[] albumImages;
		String[] albumNames;
		String[] albumCoverIDs;
		String[] albumIDs;
		
		if (Cache.cacheExists(ARTIST_ID)) {
			// read from cache
			albumImages = Cache.getAlbumImages(ARTIST_ID, IMAGE_SIZE);
			albumNames = Cache.getAlbumNames(ARTIST_ID);
			albumCoverIDs = Cache.getAlbumCoverIDs(ARTIST_ID);
			albumIDs = Cache.getAlbumIDs(ARTIST_ID);
			
		} else {
			// read from server
			albumImages = Server.getAlbumImages(ARTIST_ID, IMAGE_SIZE);
			albumNames = Server.getAlbumNames(ARTIST_ID);
			albumCoverIDs = Server.getAlbumCoverIDs(ARTIST_ID);
			albumIDs = Server.getAlbumIDs(ARTIST_ID);
		}

		MainWindow.currentTableData = new String[albumNames.length];
		ALBUM_INFO = new Object[albumNames.length][columnNames.length];
		albumCount = albumImages.length;

		for (int i = 0; i < albumNames.length; i++) {
			ALBUM_INFO[i][0] = albumImages[i];
			ALBUM_INFO[i][1] = albumNames[i];
			MainWindow.currentTableData[i] = albumIDs[i];
			ALBUM_INFO[i][2] = albumIDs[i];
			ALBUM_INFO[i][3] = albumCoverIDs[i];
		}

		// Object[][] ALBUM_INFO = {albumImages, albumNames};

		DefaultTableModel model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.setDataVector(ALBUM_INFO, columnNames);
		setModel(model);
		
		setFocusable(true);
		setBorder(new LineBorder(new Color(34, 34, 34)));
        //setForeground(Color.WHITE);
		setBackground(new Color(34, 34, 34));
		setRowHeight(IMAGE_SIZE);
		setShowGrid(false);
		//setRowMargin(1);
		// getColumnModel().setColumnMargin(10);
		setTableHeader(null);

		getColumnModel().getColumn(0).setCellRenderer(new AlbumArtRenderer());
		getColumnModel().getColumn(0).setMinWidth(IMAGE_SIZE);
		getColumnModel().getColumn(0).setMaxWidth(IMAGE_SIZE);
		getColumnModel().getColumn(0).setWidth(IMAGE_SIZE);
		getColumnModel().getColumn(0).setResizable(false);

		getColumnModel().getColumn(1).setCellRenderer(new AlbumNameRenderer());
		getColumnModel().getColumn(1).setResizable(true);
		getColumnModel().getColumn(1).setMinWidth(100);

		// hides the album ID column
		getColumnModel().getColumn(2).setMinWidth(0);
		getColumnModel().getColumn(2).setPreferredWidth(0);
		getColumnModel().getColumn(2).setMaxWidth(0);
		getColumnModel().getColumn(2).setWidth(0);
		getColumnModel().getColumn(2).setResizable(false);

		// hides the cover art ID column
		getColumnModel().getColumn(3).setMinWidth(0);
		getColumnModel().getColumn(3).setPreferredWidth(0);
		getColumnModel().getColumn(3).setMaxWidth(0);
		getColumnModel().getColumn(3).setWidth(0);
		getColumnModel().getColumn(3).setResizable(false);

	}

	class AlbumArtRenderer extends JLabel implements TableCellRenderer {

		public AlbumArtRenderer() {
			super();
			setHorizontalTextPosition(RIGHT);
			
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			
			setIcon(new ImageIcon((Image)value));
			setText("");
			
			if (isSelected) {
				setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Application.AppColor_Border));
			}
			setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
			
			return this;
			
		}

	}

	class AlbumNameRenderer extends JLabel implements TableCellRenderer {

		Icon icon = new Icon() {

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getIconWidth() {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public int getIconHeight() {
				// TODO Auto-generated method stub
				return 1;
			}
		};

		public AlbumNameRenderer() {
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {

			//setBackground(new Color(28,28,28));
			setBackground(Color.WHITE);
			
			if (hasFocus || isSelected) {
				setBackground(Application.AppColor_SelBgndClr);
				System.out.println("Focused");
			} 
						
			setForeground(Application.AppColor_Text);
			setFont(new Font("Tahoma", Font.BOLD, 13));
			setIcon(icon);
			setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
			setText((String) value);

			return this;
		}

	}
}
