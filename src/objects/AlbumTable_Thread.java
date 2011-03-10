package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import servercontact.Server;

public class AlbumTable_Thread implements Runnable {

    static final String[] columnNames = {"", "Album", "albumID", "coverArtID"};
    static String ARTIST_ID = null;
    static int IMAGE_SIZE = 100;
    public JScrollPane SCROLLPANETABLE = null;
    public JTable TABLE = null;
    public Object[][] ALBUM_INFO = null;
    public int albumCount = 0;

    Thread thread;

    public AlbumTable_Thread(String incomingArtistID, int size) {
        ARTIST_ID = incomingArtistID;
        IMAGE_SIZE = size;
    }

    public void init() {
        //RUNNING = true;
        thread = new Thread(this);
        thread.start();

    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void run() {
        Image[] albumImages = Server.getAlbumImages(ARTIST_ID, IMAGE_SIZE);
        String[] albumNames = Server.getAlbumNames(ARTIST_ID);
        String[] albumCoverIDs = Server.getAlbumCoverIDs(ARTIST_ID);
        String[] albumIDs = Server.getAlbumIDs(ARTIST_ID);

        ALBUM_INFO = new Object[albumNames.length][columnNames.length];
        albumCount = albumImages.length;

        for (int i = 0; i < albumNames.length; i++) {
            ALBUM_INFO[i][0] = albumImages[i];
            ALBUM_INFO[i][1] = albumNames[i];
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

        JTable table = new JTable(model);

        AlbumArtRenderer albumArtRend = new AlbumArtRenderer();

        DefaultTableCellRenderer albumNameCellRend = new DefaultTableCellRenderer();
        albumNameCellRend.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);

        model.setDataVector(ALBUM_INFO, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(new LineBorder(new Color(34, 34, 34)));
        scrollPane.getViewport().setBackground(new Color(34, 34, 34));

        table.setFocusable(false);
        table.setBorder(new LineBorder(new Color(34, 34, 34)));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(34, 34, 34));
        table.setRowHeight(IMAGE_SIZE);
        table.setShowGrid(false);
        table.setRowMargin(10);
        table.getColumnModel().setColumnMargin(10);
        table.setTableHeader(null);
        table.setFont(new Font("Tahoma", Font.BOLD, 13));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionBackground(new Color(50, 50, 50));

        table.getColumnModel().getColumn(0).setCellRenderer(albumArtRend);
        table.getColumnModel().getColumn(0).setMinWidth(IMAGE_SIZE + 2);
        table.getColumnModel().getColumn(0).setMaxWidth(IMAGE_SIZE + 2);
        table.getColumnModel().getColumn(0).setWidth(IMAGE_SIZE + 2);
        table.getColumnModel().getColumn(0).setResizable(false);

        table.getColumnModel().getColumn(1).setResizable(true);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setCellRenderer(albumNameCellRend);

        // hides the album ID column
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);
        table.getColumnModel().getColumn(2).setResizable(false);
        // hides the cover art ID column
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setWidth(0);
        table.getColumnModel().getColumn(3).setResizable(false);

        TABLE = table;
        SCROLLPANETABLE = scrollPane;
        //RUNNING = false;
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
