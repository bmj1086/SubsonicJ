package objects.windows;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Timer;
import java.util.TimerTask;

import main.Application;

@SuppressWarnings("serial")
public class SplashDialog extends javax.swing.JDialog {

	private javax.swing.JLabel label;
    
    public SplashDialog(java.awt.Frame parent, boolean modal) {
        setModal(modal);
    	initComponents();
    	startDieTimer();
        
    }

    private void initComponents() {
        label = new javax.swing.JLabel();
        setUndecorated(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setSize(530, 350);
        getContentPane().setLayout(null);

        label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/application-splash.png"))); // NOI18N
        label.setBounds(0, 0, 530, 350);
        getContentPane().add(label);
        addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent e) {
				System.out.println(e);
				System.out.println("SplashDialog: Closing splash screen");
            	System.exit(0); 
			}
		});
    }

    private void startDieTimer() {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if (Application.mainWindow != null) {
					timer.cancel();
					dispose();
				}
				
			}
		}, 1000, 100); 
		
	}

	public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SplashDialog dialog = new SplashDialog(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }

    
}
