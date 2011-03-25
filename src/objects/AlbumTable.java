package objects;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import objects.windows.MainWindow;

import servercontact.Server;

@SuppressWarnings("serial")
public class AlbumTable extends JTable {
	
	static final String[] columnNames = {"", "Album", "albumID", "coverArtID"};
    static String ARTIST_ID = null;
    static int IMAGE_SIZE = 75;
    public Object[][] ALBUM_INFO = null;
    public int albumCount = 0;
	
	public AlbumTable(String incomingArtistID){
		//super();
		ARTIST_ID = incomingArtistID;
		Server.currentMusicDirectoryID = incomingArtistID;
        initComponents();
        addMouseMotionListener(getMouseMotionListener());
	}

	private MouseMotionListener getMouseMotionListener() {
		MouseMotionListener theReturn = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(columnAtPoint(e.getPoint()) == 0 ||
						columnAtPoint(e.getPoint()) == 1) {
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
		Image[] albumImages = Server.getAlbumImages(ARTIST_ID, IMAGE_SIZE);
        String[] albumNames = Server.getAlbumNames(ARTIST_ID);
        String[] albumCoverIDs = Server.getAlbumCoverIDs(ARTIST_ID);
        String[] albumIDs = Server.getAlbumIDs(ARTIST_ID);

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

        //Object[][] ALBUM_INFO = {albumImages, albumNames};

        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        AlbumArtRenderer albumArtRend = new AlbumArtRenderer();

        DefaultTableCellRenderer albumNameCellRend = new DefaultTableCellRenderer();
        albumNameCellRend.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);

        model.setDataVector(ALBUM_INFO, columnNames);
        setModel(model);
        
        setFocusable(false);
        setBorder(new LineBorder(new Color(34, 34, 34)));
        setForeground(Color.WHITE);
        setBackground(new Color(34, 34, 34));
        setRowHeight(IMAGE_SIZE);
        setShowGrid(false);
        setRowMargin(10);
        getColumnModel().setColumnMargin(10);
        setTableHeader(null);
        setFont(new Font("Tahoma", Font.BOLD, 13));
        setSelectionForeground(Color.WHITE);
        setSelectionBackground(new Color(50, 50, 50));

        getColumnModel().getColumn(0).setCellRenderer(albumArtRend);
        getColumnModel().getColumn(0).setMinWidth(IMAGE_SIZE + 2);
        getColumnModel().getColumn(0).setMaxWidth(IMAGE_SIZE + 2);
        getColumnModel().getColumn(0).setWidth(IMAGE_SIZE + 2);
        getColumnModel().getColumn(0).setResizable(false);

        getColumnModel().getColumn(1).setResizable(true);
        getColumnModel().getColumn(1).setMinWidth(100);
        getColumnModel().getColumn(1).setCellRenderer(albumNameCellRend);

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
	
	class AlbumArtRenderer extends DefaultTableCellRenderer {

        public AlbumArtRenderer() {
            super();
            setHorizontalTextPosition(CENTER);

        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Image) {
                setIcon(new ImageIcon(((Image) value)));
            }

        }
    }
}
