package objects;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

public class AlbumArtRenderer extends DefaultTableCellRenderer{

    public AlbumArtRenderer(){
        super();
        setHorizontalTextPosition(CENTER);
        
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Image) {
            setIcon(new ImageIcon(((Image)value)));
        }

    }


}
