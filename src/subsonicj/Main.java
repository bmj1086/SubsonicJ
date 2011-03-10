/*
 * String urlStr = "http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.1.0&c=myapp&id=433a5c55736572735c42726574745c4d757369635c46696e63685c5768617420497420497320746f204275726e5c3133205768617420497420497320746f204275726e205b2d5d2e776d61&maxBitRate=96";
 * JLayerPlayer player = new JLayerPlayer();
 * player.testPlay(urlStr);
 *
 * //temporary to save the file
try {
XMLSerializer serializer = new XMLSerializer();
serializer.setOutputCharStream(
new FileWriter(Settings.appDirectory + "albumList.xml"));
serializer.serialize(doc);

} catch (Exception ex) {
System.out.println(ex);
}
 *
 *
 */
package subsonicj;

import java.awt.Color;
import java.io.File;
import java.util.Properties;
import javax.swing.UIManager;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import servercontact.Media;
import servercontact.Server;
import servercontact.Settings;
import settings.Application;
import javazoom.jl.player.advanced.*;

public class Main {

    
    public static void main(String[] args) {
        createAppDirectories();
        loadLookAndFeel();
        Application.loadUIProperties();
        loadSplashScreen();
        loadServerForm();
        loadMainWindow();

    }


	private static void loadLookAndFeel() {
    	try {
    		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            System.out.println("LookAndFeel set");
            
        }
        catch (Exception ex) {
    	    System.out.println("Main: Can't load Look and Feel...Exiting application");
    	    System.out.println(ex);
            System.exit(0);
            
        }
    	
    }

    public static void setWindowDecorations(boolean on) {
    	//changes the property for decorated window
    	if (on) {
    		AluminiumLookAndFeel.setCurrentTheme(decoratedProperties);
		} else {
			AluminiumLookAndFeel.setCurrentTheme(undecoratedProperties);
		}
		
		
	}

	private static void createAppDirectories() {
        String[] dirs = new String[]{
            Settings.appDirectory,
            Settings.serversDirectory,
            Settings.settingsDirectory
        };

        for (int i = 0; i < dirs.length; i++) {
            File directory = new File(dirs[i]);
            if (!directory.exists()) {
                try {
                    directory.mkdir();
                } catch (Exception e) {
                    System.out.println("Main: Can't create directories for application");
                    System.out.println(e);
                    System.out.println("Main: Shutting Down...");
                    System.exit(0);
                }
            }
        }
    }

    private static void loadServerForm() {
        setWindowDecorations(true);
       	serverInfoDialog = new ServerInfoDialog(null, true);
    	serverInfoDialog.setLocationRelativeTo(null);
        serverInfoDialog.setVisible(!Server.CONNECTED);
        
    }

    private static void loadMainWindow() {
    	if (Server.CONNECTED) {
        	setWindowDecorations(true);
    		mainWindow = new MainWindow();
            mainWindow.setVisible(true);			
		} else {
			System.exit(0);
		}

		
    }

    private static void loadSplashScreen() {
        setWindowDecorations(false);
    	splashDialog = new SplashDialog(null, true);
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);
        
    }
}
