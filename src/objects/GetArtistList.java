package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.Application;
import objects.windows.MainWindow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import servercontact.Server;
import config.AppConfig;

public class GetArtistList implements Runnable {

	// the list used and set on the main window after the thread completes
	public JList artistList = null;
	public static ArrayList<String> filteredItems;
	public static ArrayList<String> unfilteredItems;

	// the thread used to run the process
	Thread thread;

	public GetArtistList() {

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
		File file = new File(AppConfig.localArtistListFileString);
		Document doc = null;
		if (file.exists()) {

			long lastModified = getLastModifiedFromFile(file);
			doc = Server.getIndexes(lastModified);
			if (doc == null) {
				try {
					System.out
							.println("GetArtists: No changes in artist list. Loading from cache file");
					doc = Server.parse(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				System.out
						.println("GetArtists: Server artists have updated, reading artist list from server");
				writeArtistCache(doc, file);
			}
		} else {
			System.out
					.println("GetArtists: Gathering artist indexes from server");
			doc = Server.getIndexes();
			writeArtistCache(doc, file);
		}

		NodeList artistNodeList = doc.getElementsByTagName("artist");
		int artistCount = artistNodeList.getLength();
		MainWindow.ARTIST_IDs = new String[artistCount][2];

		artistList = new JList(new DefaultListModel());
		artistList.setBackground(new Color(34, 34, 34));
		artistList.setFont(new Font("Tahoma", Font.PLAIN, 14));
//		artistList.setForeground(new Color(204, 204, 204));
//		artistList.setSelectionForeground(Application.AppColor_SelBgndClr);
//		artistList.setSelectionBackground(Application.AppColor_Text);
		artistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		artistList.setCellRenderer(new ArtistCellRenderer());
		DefaultListModel listModel = (DefaultListModel) artistList.getModel();

		unfilteredItems = new ArrayList<String>();
		filteredItems = new ArrayList<String>();
		for (int i = 0; i < artistNodeList.getLength(); i++) {
			Element artistNode = (Element) artistNodeList.item(i);
			int pos = listModel.getSize();

			listModel.add(pos, artistNode.getAttribute("name")); // OLD WORKING
			unfilteredItems.add(artistNode.getAttribute("name"));
			MainWindow.ARTIST_IDs[i][0] = artistNode.getAttribute("name");
			MainWindow.ARTIST_IDs[i][1] = artistNode.getAttribute("id");

		}
		artistList.setModel(listModel);
		
		// TODO set the border of the selected item to null. Can't figure it
		// out. Possibly set it not focusable with
		// listModel.elementAt(position).setFocusable(false);

	}

	@SuppressWarnings("serial")
	public static class ArtistCellRenderer extends JLabel implements ListCellRenderer {
		
		public ArtistCellRenderer() {
			setBorder(BorderFactory.createEmptyBorder());
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			setOpaque(true);
			Icon blankIcon = new Icon() {
				
				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public int getIconWidth() {
					// TODO Auto-generated method stub
					return 20;
				}
				
				@Override
				public int getIconHeight() {
					// TODO Auto-generated method stub
					return 25;
				}
			};
			
			if (isSelected) {
				setBackground(Application.AppColor_SelBgndClr);
				setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Application.AppColor_Border));
				
			}
			
			if (!isSelected) {
				//setBackground(Application.AppColor_Dark);
				setBackground(new Color(28,28,28));
				setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
				//setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			}
			
			setFont(new Font("Tahoma", Font.PLAIN, 14));
			setIcon(blankIcon);
			//setSize(getSize().width, 50);
			setForeground(Application.AppColor_Text);
			setText(value.toString());
			return this;
		}
	}


	private long getLastModifiedFromFile(File file) {
		long lastModifiedLong = 0;
		try {
			FileInputStream fis = new FileInputStream(file);
			Document doc = Server.parse(fis);
			NodeList children = doc.getElementsByTagName("indexes");
			String modifiedStr = children.item(0).getAttributes().item(0)
					.getNodeValue();
			lastModifiedLong = Long.parseLong(modifiedStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lastModifiedLong; // temp
	}

	private void writeArtistCache(Document doc, File writeToFile) {
		System.out.println("GetArtistList: Writing artist cache file");
		try {
			writeToFile.delete();
			Source source = new DOMSource(doc);
			Result result = new StreamResult(writeToFile);
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
