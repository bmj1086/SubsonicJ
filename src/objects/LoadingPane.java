package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class LoadingPane extends JPanel {

	private JRootPane m_rootPane = null;

	// Stores the previous Glass Pane Component
	private Component m_prevGlassPane = null;
	private boolean m_handleMouseEvents = false;
	private boolean m_drawing = false;

	public LoadingPane() {
		
	}

	public void setGlassPane(JRootPane rootPane) {
		m_rootPane = rootPane;

		// store the current glass pane
		// m_prevGlassPane = m_rootPane.getGlassPane();

		// set this as new glass pane
		m_rootPane.setGlassPane(this);

		// set opaque to false, i.e. make transparent
		setOpaque(false);

		setLayout(null);

		JLabel label = new JLabel("Loading");
		label.setBounds(getWidth() / 2, getHeight() / 2, 300, 30);
		add(label);
	}

	public void removeGlassPane() {
		// set the glass pane visible false
		setVisible(false);

		// reset the previous glass pane
		m_rootPane.setGlassPane(null);
	}
	
	@Override
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Color bgColor = new Color(34, 34, 34, 50); //r,g,b,alpha
        g.setColor(bgColor);
        //g.fillRect(0,0,260,500); //x,y,width,height
    } 
}
