package objects;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class OpaquePanel extends JPanel {
	
	public OpaquePanel()
    {
        super();
        setLayout(null);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Color ppColor = new Color(255, 10, 250, 70); //r,g,b,alpha
        g.setColor(ppColor);
        g.fillRect(0,0,260,500); //x,y,width,height
    } 
}
