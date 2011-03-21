package objects.windows;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class SplashDialog extends javax.swing.JDialog {

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
        
    }

    private void startDieTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				dispose();
				
			}
		},3000, 3000); 
		
	}

	public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SplashDialog dialog = new SplashDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                    	System.out.println("SplashDialog: Closing splash screen");
                    	System.exit(0);                        
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables

}
