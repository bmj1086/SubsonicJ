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

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;
import subsonicj.Main;


public class SongTable_Thread implements Runnable {
	
    static final String[] columnNames = {"PlayPause", "AddToPlaylist", "Track", "Song", "Duration", "songID", "bitRate", "parent"};
    static String ALBUM_ID = null;
    static ImageIcon playIcon = new ImageIcon(Main.class.getResource("/res/Play-15.png"));
    static ImageIcon addIcon = new ImageIcon(Main.class.getResource("/res/Add-to-playlist-15.png"));
    public String ALBUM_IMAGE_ID = null;
    public Object[][] SONGS_INFO = null;
    public JScrollPane SCROLLPANETABLE = null;
    public int SONG_COUNT = 0;

	Thread thread;

    public SongTable_Thread(String albumID, String albumImageID) {
        ALBUM_ID = albumID;
        ALBUM_IMAGE_ID = albumImageID;
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

    @Override
    public void run() {
        Document doc = Server.getMusicDirectory(ALBUM_ID);
        NodeList songNodes = doc.getElementsByTagName("child");
        SONG_COUNT = songNodes.getLength();
    	SONGS_INFO = new Object[SONG_COUNT][columnNames.length];

        for (int i = 0; i < songNodes.getLength(); i++) {
            Element songNode = (Element) songNodes.item(i);
            SONGS_INFO[i][2] = songNode.getAttribute("track");
            SONGS_INFO[i][3] = songNode.getAttribute("title");
            SONGS_INFO[i][4] = convertDurationToString(songNode.getAttribute("duration"));
            SONGS_INFO[i][5] = songNode.getAttribute("id");
            SONGS_INFO[i][6] = songNode.getAttribute("bitRate");
            SONGS_INFO[i][7] = songNode.getAttribute("parent");
        }

        DefaultTableModel model = new DefaultTableModel() {
        	
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable table = new JTable(model);
        
        model.setDataVector(SONGS_INFO, columnNames);

        MyScrollPane scrollPane = new MyScrollPane(table);
        
        table.setBorder(new LineBorder(new Color(34, 34, 34)));
        table.setFocusable(false);
        table.removeEditor();

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                Point click = new Point(me.getX(), me.getY());
                int column = table.columnAtPoint(click);
                int row = table.rowAtPoint(click);
                if (column == 0) {
                    System.out.println("Playing " + table.getValueAt(row, 3));
                    CurrentSong.setProperties(table.getValueAt(row, 5).toString(), table.getValueAt(row, 7).toString());
                    CurrentSong.playSong();
                    
                }

            }
        });
        // setup table information
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(34, 34, 34));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setRowMargin(5);
        table.getColumnModel().setColumnMargin(5);
        table.setTableHeader(null);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionBackground(new Color(50, 50, 50));

        //play song column
        table.getColumnModel().getColumn(0).setCellRenderer(new PlayButtonRenderer());
        table.getColumnModel().getColumn(0).setMinWidth(25);
        table.getColumnModel().getColumn(0).setMaxWidth(25);
        table.getColumnModel().getColumn(0).setWidth(25);

        // add to playlist column
        table.getColumnModel().getColumn(1).setCellRenderer(new AddButtonRenderer());
        table.getColumnModel().getColumn(1).setMinWidth(25);
        table.getColumnModel().getColumn(1).setMaxWidth(25);
        table.getColumnModel().getColumn(1).setWidth(25);

        // song track number column
        table.getColumnModel().getColumn(2).setMinWidth(30);
        table.getColumnModel().getColumn(2).setMaxWidth(30);
        table.getColumnModel().getColumn(2).setWidth(30);

        // song duration column
        table.getColumnModel().getColumn(4).setMinWidth(70);
        table.getColumnModel().getColumn(4).setMaxWidth(70);
        table.getColumnModel().getColumn(4).setWidth(40);

        // loop to hide columns after duration may change this in the future to show more info.
        // --- could possibly have settings to show different information for user settings.

        for (int i= 5; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(0);
            table.getColumnModel().getColumn(i).setMaxWidth(0);
            table.getColumnModel().getColumn(i).setWidth(0);
        }
        SCROLLPANETABLE = scrollPane;
        

    }

    private String convertDurationToString(String attribute) { //convert duration to minutes
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

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("");
            setIcon(playIcon);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            return this;
        }
    }

    class AddButtonRenderer extends JButton implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("");
            setIcon(addIcon);
            setBorderPainted(false);
            setContentAreaFilled(false);
            return this;
        }
    }

    class AddButton extends JButton {
        public AddButton(String text, ImageIcon icon) {
            super(text, icon);
            setBorderPainted(false);
        }
    }

    class PlayPauseRenderer extends DefaultTableCellRenderer {
        public PlayPauseRenderer() {
            super();
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

    class AddToPlaylistRenderer extends DefaultTableCellRenderer {

        public AddToPlaylistRenderer() {
            super();
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
}
