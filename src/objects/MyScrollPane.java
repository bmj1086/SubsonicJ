package objects;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;

public class MyScrollPane extends JScrollPane {

	public MyScrollPane(JTable table) {
		super(table);
		MatteBorder border = new MatteBorder(0, 0, 0, 1, new Color(34, 34, 34));
		setBorder(border);
		getViewport().setBackground(new Color(34, 34, 34));
	}
}
