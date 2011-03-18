package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;

import settings.Application;

//VS4E -- DO NOT REMOVE THIS LINE!
public class FloatingMessage extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel rootPanel;
	private JLabel messageLabel;
	public long timeToLive = 0;
	public boolean DISPOSABLE = false;
	
	public static int TOP_LEFT = 0;
	public static int TOP_RIGHT = 1;
	public static int BOTTOM_RIGHT = 2;
	public static int BOTTOM_LEFT = 3;
	public static final int CENTER_PARENT = 4;

	public FloatingMessage(long length, boolean disposable) {
		DISPOSABLE = disposable;
		timeToLive = length;
		initComponents();
		if (length > 0) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					startDieTimer();
				}
			});

		}
	}

	private void initComponents() {
		setFont(new Font("Dialog", Font.PLAIN, 15));
		setUndecorated(true);
		//setBackground(Color.white);
		setResizable(true);
		//setForeground(Color.black);
		setAlwaysOnTop(true);
		setLayout(new GroupLayout());
		add(getRootPanel(), new Constraints(new Bilateral(0, 0, 0),
				new Bilateral(0, 0, 0)));
		setContentPane(rootPanel);
		setSize(125, 20);
	}

	private JLabel getMessageLabel() {
		if (messageLabel == null) {
			messageLabel = new JLabel();
			messageLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
			messageLabel.setForeground(Application.AppColor_Text);
			messageLabel.setBackground(Application.AppColor_Dark);
			messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			messageLabel.setText("");
			messageLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(DISPOSABLE) {
						dispose();
					}
				}
			});
		}
		return messageLabel;
	}

	private JPanel getRootPanel() {
		if (rootPanel == null) {
			rootPanel = new JPanel();
			rootPanel.setBackground(Application.AppColor_Dark);
			rootPanel.setLayout(new GroupLayout());
			rootPanel.add(getMessageLabel(), new Constraints(new Bilateral(0,
					0, 41), new Bilateral(0, 0, 16)));
		}
		return rootPanel;
	}

	private void startDieTimer() {

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				dispose();
			}
		}, timeToLive);

	}

	public void setMessage(String message) {
		messageLabel.setText(message);
		Dimension size = new Dimension(
				messageLabel.getPreferredSize().width + 10,
				messageLabel.getPreferredSize().height + 5);
		messageLabel.setSize(size);
		setSize(size);
	}

	public String getMessage() {
		return messageLabel.getText();
	}

}
