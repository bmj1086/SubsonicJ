package objects;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import servercontact.Server;

public class AlbumCover extends JLabel {

    public static int TINY = 25;
    public static int SMALL = 100;
    public static int LARGE = 256;

    public AlbumCover(String albumID, Point index, int size) {
        super();
        setTheBounds(index, size);
        setImage(albumID, size);
        setClick();
    }

    private void setTheBounds(Point point, int size) {
        int spacerSpaceX = 10 * (point.x + 1);
        int labelSpace = 15 * point.y;
        int spacerSpaceY = (10 * point.y) + labelSpace;
        int x = spacerSpaceX + (size * point.x);
        int y = spacerSpaceY + (size * point.y);
        setBounds(x, y, size, size);
    }

    private void setImage(String albumID, int size) {
        Image image = Server.getCoverArt(albumID, size);
        setIcon(new ImageIcon(image));
    }

    private void setClick() {
        // TODO: add an onclick listener to open the songs list
    }

//    public static JLabel createLabel(String albumID, Point index, int size){
//        Image image = Server.getCoverArt(albumID, size);
//        JLabel label = new JLabel("", new ImageIcon(image), JLabel.LEADING);
//        int spacerSpaceX = 10 * (index.x + 1);
//        int spacerSpaceY = 10 * (index.y + 1);
//        int x = spacerSpaceX + (size * index.x);
//        int y = spacerSpaceY + (size * index.y);
//        label.setBounds(x, y, size, size);
//        return label;
//    }
}
