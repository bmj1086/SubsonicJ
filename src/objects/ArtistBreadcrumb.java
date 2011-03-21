package objects;

import java.awt.Font;

import javax.swing.JLabel;

public class ArtistBreadcrumb extends JLabel {
	
	public ArtistBreadcrumb(String text, int index) {
		setText(text);
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		
	}
}
