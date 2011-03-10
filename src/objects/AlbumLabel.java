package objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JLabel;

public class AlbumLabel extends JLabel{

    public AlbumLabel(String text, Point index, int width){
        super(text, null, JLabel.LEADING);
        setBounds(getBounds(index, width));
        setForeground(Color.WHITE);
    }

    private Rectangle getBounds(Point point, int width) {
        int spacerSpaceX = 10 * (point.x + 1);
        int spacerSpaceY = (100 * (point.y + 1));
        int x = spacerSpaceX + (width * point.x);
        int y = spacerSpaceY + (20 * point.y);
        return new Rectangle(x, y, width, 20);
    }
}
