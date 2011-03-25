package objects;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.Application;

public class LoadingPane extends JPanel implements ActionListener {
	private Container contentPane;
	private boolean inited;
	JPasswordField pass;

	public LoadingPane(Container contentPane) {
		this.contentPane = contentPane;
		setLayout(null);

	}

	private void init() {
		inited = true;
		setBounds(contentPane.getX(), contentPane.getY(), 100, 25);  //contentPane.getWidth(), contentPane.getHeight());
		setMaximumSize(getSize());
		setMinimumSize(getSize());
		
	}

	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);// makes sure MyGlass's widgets are drawn
									// automatically

		if (!inited) {
			init();
		}

		Graphics2D g = (Graphics2D) gr;
		// create (fake) transparency
		AlphaComposite transparent = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, .7f);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(transparent);
		// draw the contents of the JFrame's content pane upon our glass pane.
		contentPane.paint(gr);
		Color bgColor = new Color(50, 50, 50, 50);
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		AlphaComposite solid = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1f);
		g.setComposite(solid);
		g.setColor(Color.black);
		g.drawString("Glass pane string", 50, 100);
		g.drawString("which is drawn manually!", 50, 120);
	}

	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
	}
}
