package settings;


public class OSDetect {

	private static final String fileSep = System.getProperty("file.separator");
    private static String OS = null;

    private static String OSName() {
        if(OS == null)
            OS = System.getProperty("os.name").toLowerCase();

        return OS;
    }

    private static boolean isWindows() 
    {
        //Windows
        return (OSName().indexOf( "win" ) >= 0); 
    }

    private static boolean isMac() {
        //Apple Macintosh
        return (OSName().indexOf( "mac" ) >= 0); 
    }
 
    private static boolean isUnix() {
        //Linux or Unix
        return (OSName().indexOf( "nix") >=0 || OSName().indexOf( "nux") >=0);
    }
    
    public static String appDirectory (String Name) {

        if (isUnix()) {
            Name = ".config" + fileSep + Name;

        } else if (isWindows()) {
            Name = "SubsonicJ";

        } else if (isMac()) {
            Name = "SubsonicJ";

        } else {
            Name = "SubsonicJ";
        }

        return System.getProperty("user.home") + fileSep + Name;
    }
}
