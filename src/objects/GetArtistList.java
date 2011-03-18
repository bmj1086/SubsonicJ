package objects;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;
import settings.AppSettings;

public class GetArtistList implements Runnable{

	// the list used and set on the main window after the thread completes
	JList artistList = null;

	// the thread used to run the process
	Thread thread;

	public GetArtistList() {
		// TODO Auto-generated method stub

	}

	public void init() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	public boolean isRunning() {
		if (thread != null) {
			return thread.isAlive();
		} else {
			return false;
		}
	}
	
	public void run() {
		File file = new File(AppSettings.localArtistListFileString);
		Document doc = null;
		if (file.exists()) {
			long lastModified = file.lastModified();
			doc = Server.getIndexes(lastModified);
			if (doc == null) {
				try {
					System.out
							.println("MainWindow: No changes in artist list. Loading from cacheFile");
					doc = Server.parse(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			doc = Server.getIndexes();
			writeArtistCache(doc, file);
		}

		NodeList artistNodeList = doc.getElementsByTagName("artist");
		int artistCount = artistNodeList.getLength();
		MainWindow.ARTIST_IDs = new String[artistCount][2];

		artistList = new JList(new DefaultListModel());
		artistList.setBackground(new Color(34, 34, 34));
		artistList.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistList.setForeground(new Color(204, 204, 204));
		artistList.setSelectionForeground(artistList.getBackground());
		artistList.setSelectionBackground(artistList.getForeground());
		artistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListModel listModel = (DefaultListModel) artistList.getModel();
		
		for (int i = 0; i < artistNodeList.getLength(); i++) {
			Element artistNode = (Element) artistNodeList.item(i);
			int pos = listModel.getSize();

			listModel.add(pos, artistNode.getAttribute("name"));
			
			MainWindow.ARTIST_IDs[i][0] = artistNode.getAttribute("name");
			MainWindow.ARTIST_IDs[i][1] = artistNode.getAttribute("id");

		}
		artistList.setModel(listModel);
		// TODO set the border of the selected item to null. Can't figure it
		// out. Possibly set it not focusable with 
		//listModel.elementAt(position).setFocusable(false);

	}

	private void writeArtistCache(Document doc, File writeToFile) {
		try {
			Source source = new DOMSource(doc);
			Result result = new StreamResult(writeToFile);
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
