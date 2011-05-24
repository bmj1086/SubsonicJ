package main;

import java.awt.Image;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import config.AppConfig;

public class Cache {

	public static boolean cacheExists(String artistID) {
		// TODO Auto-generated method stub
		return false;
	}

	public static Image[] getAlbumImages(String artistID, int size) {
		File artistDir = new File(AppConfig.cacheDirectory + artistID + ".xml");
		File[] files = artistDir.listFiles();
		Image[] images = new Image[files.length];
		for (int i = 0; i < files.length; i++) {
			try {
				images[i] = ImageIO.read(files[i]);
			} catch (Exception ignore) {
				// not an image, ignore
			}
		}
		
			
		Document doc = getArtistCache(artistID);
		NodeList indexes = doc.getElementsByTagName("child");
		images = new Image[indexes.getLength()];

		for (int i = 0; i < indexes.getLength(); i++) {
			Element index = (Element) indexes.item(i);
			String coverArtID = index.getAttribute("coverArt");
			try {
				String urlS = AppConfig.SERVER_ADDRESS
						+ "/rest/getCoverArt.view?u="
						+ AppConfig.SERVER_USERNAME + "&p="
						+ AppConfig.SERVER_PASSWORD + "&v=1.5&c=SubsonicJ"
						+ "&id=" + coverArtID + "&size=" + size;


				URL url = new URL(urlS);
				images[i] = ImageIO.read(url);

			} catch (Exception ex) {
				System.out.println("Server: ");
				System.out.println(ex);
				return null;
			}
		}
		return images;
	}

	private static Document getArtistCache(String artistID) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] getAlbumNames(String artistID) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] getAlbumCoverIDs(String artistID) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] getAlbumIDs(String artistID) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void addAlbumImagesToCache(Image[] images) {
//		SwingUtilities.invokeLater(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO add images to artist cache folder.
//				
//			}
//		});
		
	}

}
