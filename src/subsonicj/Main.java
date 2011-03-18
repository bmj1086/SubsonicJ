/*
 * String urlStr = "http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.1.0&c=myapp&id=433a5c55736572735c42726574745c4d757369635c46696e63685c5768617420497420497320746f204275726e5c3133205768617420497420497320746f204275726e205b2d5d2e776d61&maxBitRate=96";
 * JLayerPlayer player = new JLayerPlayer();
 * player.testPlay(urlStr);
 *
 * //temporary to save the file
try {
XMLSerializer serializer = new XMLSerializer();
serializer.setOutputCharStream(
new FileWriter(Preferences.appDirectory + "albumList.xml"));
serializer.serialize(doc);

} catch (Exception ex) {
Log.print(ex);
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
import javazoom.jl.player.advanced.*;

import servercontact.Media;
import servercontact.Server;
import settings.Preferences;
import settings.UI;
import debug.Log;

public class Main {

    private static String TAG = "Main";
    
    public static void main(String[] args) {
        createAppDirectories();
        loadLookAndFeel();
        UI.loadUIProperties();
        //loadSplashScreen(); // Disable when developing to avoid waiting on splash
        loadServerForm();
        loadMainWindow();

    }


	private static void loadLookAndFeel() {
    	try {
    		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
    	    Log.print(TAG, "Custom LookAndFeel set");
            
        }
        catch (Exception ex) {
    	    Log.print(TAG, "Can't load Look and Feel...Exiting application");
    	    Log.print(TAG, ex.toString());
            System.exit(0);
            
        }
    	
    }

    public static void setWindowDecorations(boolean on) {
    	//changes the property for decorated window
    	if (on) {
    		AluminiumLookAndFeel.setCurrentTheme(UI.decoratedProperties);
		} else {
			AluminiumLookAndFeel.setCurrentTheme(UI.undecoratedProperties);
		}
		
		
	}

	private static void createAppDirectories() {
        String[] dirs = new String[]{
            Preferences.appDirectory,
            Preferences.serversDirectory,
            Preferences.settingsDirectory
        };

        for (int i = 0; i < dirs.length; i++) {
            File directory = new File(dirs[i]);
            if (!directory.exists()) {
                try {
                    directory.mkdirs();
                } catch (Exception e) {
    	            Log.print(TAG, "Can't create directories for application");
                    Log.print(TAG, e.toString());
                    Log.print(TAG, "Shutting Down...");
                    System.exit(0);
                }
            }
        }
    }

    private static void loadServerForm() {
        setWindowDecorations(true);
        UI.serverInfoDialog = new ServerInfoDialog(null, true);
        UI.serverInfoDialog.setLocationRelativeTo(null);
        if (!Server.CONNECTED) {
        	Log.print(TAG, "Server not auto connected or no server information found");
        	Log.print(TAG, "Initializing the server info dialog...");
        	UI.serverInfoDialog.setVisible(true);
            
		}
        
    }

    private static void loadMainWindow() {
    	if (Server.CONNECTED) {
        	setWindowDecorations(true);
        	UI.mainWindow = new MainWindow();
        	UI.mainWindow.setVisible(true);			
		} else {
			System.exit(0);
		}

		
    }

    private static void loadSplashScreen() {
    	Log.print(TAG, "Loading splash screen");
        setWindowDecorations(false);
        UI.splashDialog = new SplashDialog(null, true);
        UI.splashDialog.setLocationRelativeTo(null);
        UI.splashDialog.setVisible(true);
        
    }
}
