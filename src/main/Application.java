/* ************* LIST ALL TODOS FOR THE ENTIRE APPLICATION HERE *************
 * get user on application start and if the user is authenticated to use the
 * * jukebox controls pop up a message that says "You're authenticated to use 
 * * jukebox control which may perform better, would you like to use jukebox 
 * * control? (This can be disabled later in Edit > Settings."
 *
 * when performing getIndexes store indexes to local xml file After this file
 * * is stored getting indexes will take little, to no time, using 
 * * ifModifiedSince as an argument. This will only return a result if the artist
 * * collection has changed since the given time. If nothing returns, load 
 * * the indexes from the local file.
 *
 * **************************************************************************
 * **************************************************************************
 * **************************************************************************
 */

package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.Properties;

import javax.swing.JPanel;

import objects.TrayRightClickMenu;
import objects.windows.FloatingMessage;
import objects.windows.MainWindow;
import objects.windows.ServerInfoDialog;
import objects.windows.SplashDialog;

public class Application {

	Properties properties = new Properties();

	static FloatingMessage popup = null;
	public static MainWindow mainWindow = null;
	public static SplashDialog splashDialog = null;
	public static ServerInfoDialog serverInfoDialog = null;
	public static Properties decoratedProperties;
	public static Properties undecoratedProperties;
	public static TrayRightClickMenu trayRightClickMenu;

	// colors to use in the application
	public static Color AppColor_Dark = new Color(34, 34, 34);
	public static Color AppColor_Text = new Color(204, 204, 204);
	public static Color AppColor_Border = new Color(65, 65, 65);
	public static Color AppColor_SelBgndClr = new Color(50, 50, 50);

	public static void loadUIProperties() {
		// declarations
		decoratedProperties = new Properties();
		undecoratedProperties = new Properties();

		// decorated window
		decoratedProperties.put("logoString", "SubsonicJ");
		decoratedProperties.put("windowDecoration", "on");

		// undecorated window
		undecoratedProperties.put("logoString", "SubsonicJ");
		undecoratedProperties.setProperty("windowDecoration", "off");

	}

	public static void setStatus(String string) {
		mainWindow.setStatus(string);
	}

	public static void showNowPlaying(boolean bool) {
		mainWindow.showNowPlaying(bool);
		if (bool) {
			mainWindow.setNowPlayingLabels();
			
		}
	}

	public static void setNowPlayingLabels() {
		mainWindow.setNowPlayingLabels();
	}

//	public static void showMessage(String message, long length,
//			boolean disposable) {
//		Main.setWindowDecorations(false);
//		if (popup != null) {
//			popup.dispose();
//		}
//		popup = new FloatingMessage(length, disposable);
//		popup.setMessage(message);
//		popup.setLocationRelativeTo(mainWindow);
//		popup.setVisible(true);
//		Main.setWindowDecorations(true);
//	}
//
//	public static void showMessage(String message, Component parent,
//			int pointOnParent, long length, boolean disposable) {
//		Main.setWindowDecorations(false);
//		if (popup != null) {
//			popup.dispose();
//		}
//		popup = new FloatingMessage(length, disposable);
//		popup.timeToLive = length;
//		popup.setMessage(message);
//		setPopupLocation(parent, pointOnParent);
//		popup.setVisible(true);
//		Main.setWindowDecorations(true);
//	}

	public static void closeMessage(String message) {
		if (popup != null && popup.getMessage() == message) {
			popup.dispose();
		}
	}

	private static void setPopupLocation(Component parent, int pointOnParent) {
		if (pointOnParent == FloatingMessage.TOP_LEFT) {
			popup.setLocation(new Point(parent.getLocationOnScreen().x
					- popup.getWidth(), parent.getLocationOnScreen().y
					- popup.getHeight()));
		} else if (pointOnParent == FloatingMessage.TOP_RIGHT) {
			popup.setLocation(new Point(parent.getLocationOnScreen().x
					+ parent.getWidth(), parent.getLocationOnScreen().y
					- popup.getHeight()));
		} else if (pointOnParent == FloatingMessage.BOTTOM_RIGHT) {
			popup.setLocation(new Point(parent.getLocationOnScreen().x
					+ parent.getWidth(), parent.getLocationOnScreen().y
					+ parent.getWidth()));
		} else if (pointOnParent == FloatingMessage.BOTTOM_LEFT) {
			popup.setLocation(new Point(parent.getLocationOnScreen().x
					- popup.getWidth(), parent.getLocationOnScreen().y
					+ parent.getWidth()));
		} else if (pointOnParent == FloatingMessage.CENTER_PARENT) {
			popup.setLocationRelativeTo(parent);
		}

	}

	public static FloatingMessage setLoading(JPanel panel) {
		FloatingMessage msg = new FloatingMessage(0, false);
		msg.setBounds(panel.getLocationOnScreen().x, panel.getLocationOnScreen().y, panel.getWidth(), panel.getHeight());
		msg.setVisible(true);
		return msg;
		
		
		//panel.get
//		Main.setWindowDecorations(false);
//		FloatingMessage loadingPane = new FloatingMessage(0, true);
//		loadingPane.setBounds(parentPanel.getLocationOnScreen().x,
//				parentPanel.getLocationOnScreen().y, parentPanel.getWidth(),
//				parentPanel.getHeight());
//		loadingPane.setVisible(true);
	}
}
