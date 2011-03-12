package settings;

import java.awt.Color;
import java.util.Properties;

import servercontact.Media;
import subsonicj.MainWindow;
import subsonicj.ServerInfoDialog;
import subsonicj.SplashDialog;

public class Application {
	
	Properties properties = new Properties();
	

    public static MainWindow mainWindow = null;
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
    
}
