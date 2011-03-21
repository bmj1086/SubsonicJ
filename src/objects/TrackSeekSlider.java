package objects;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JSlider;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class TrackSeekSlider extends JSlider {
	public TrackSeekSlider(){
		setFocusable(false);
		setSnapToTicks(false);
		setBorder(null);
		setValue(0);
		
		UIManager.getLookAndFeelDefaults().put("Slider.horizontalThumbIcon",new Icon(){
		        
			@Override
		    public int getIconHeight() {
		            return 0;
		    }
		    @Override
		    public int getIconWidth() {
		           return 0;
		    }
		    @Override
		    public void paintIcon(Component c, Graphics g, int x, int y) {
		    	//do nothing
		    }
	    });
	}
}
