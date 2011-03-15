/* **************************************************************************
 * **************************************************************************
 * **************************************************************************
 * ************* LIST ALL TODOS FOR THE ENTIRE APPLICATION HERE *************
 * 
 * **************************************************************************
 * **************************************************************************
 * **************************************************************************
 */


package settings;

import java.awt.Color;
import java.util.Properties;

import objects.CurrentPlaylist;
import objects.MainWindowNew;
import servercontact.Media;
import subsonicj.ServerInfoDialog;
import subsonicj.SplashDialog;

public class Application {
	
	Properties properties = new Properties();
	

    public static MainWindowNew mainWindow = null;
    public static SplashDialog splashDialog = null;
    public static ServerInfoDialog serverInfoDialog = null;
    public static Properties decoratedProperties;
    public static Properties undecoratedProperties;
    public static final Media SongPlayer = null;
	
    // colors to use in the application
    public static Color AppColor_Dark = new Color(34, 34, 34);
    public static Color AppColor_Text = new Color(204, 204, 204);
    public static Color AppColor_Border = new Color(102, 102, 102);
    public static Color AppColor_SelBgndClr = new Color(50, 50, 50);

    

    public static void loadUIProperties() {
    	// declarations
    	decoratedProperties = new Properties();
    	undecoratedProperties = new Properties();
    	
    	//decorated window
    	decoratedProperties.put("logoString", "SubsonicJ");
    	decoratedProperties.put("windowDecoration", "on");
    	
    	// undecorated window
    	undecoratedProperties.put("logoString", "SubsonicJ");
    	undecoratedProperties.setProperty("windowDecoration", "off");
		
	}
    
    public static void setStatus(String string){
    	mainWindow.setStatus(string);
    }
    
    public static void showNowPlaying(boolean bool){
    	mainWindow.showNowPlaying(bool);
    	if (bool){
    		mainWindow.setNowPlayingLabels();
    	}
    }
    
    public static void setNowPlayingLabels() {
    	mainWindow.setNowPlayingLabels();
    }

	
    public static void nextSong() {
		// for now only initialized by the Media class when the current song ends.
    	
		
	}
}
