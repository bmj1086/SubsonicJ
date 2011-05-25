/* README 
 * Class used to play a single file. Must be disposed of after single use
 * Use the CurrentPlaylist class to store songs and create instances of
 * this class to play songs and control playback.
 * 
 * Controls for this are as follows: use Control control[] = line.getControls();
 * to get the controls
 * 
	- control[0]: Master Gain with current value: 0.0 dB (range: -80.0 - 6.0206)
	- control[1]: Mute Control with current value: False
	- control[2]: Balance with current value: 0.0  (range: -1.0 - 1.0)
	- control[3]: Pan with current value: 0.0  (range: -1.0 - 1.0)
 * 
 */

package mp3player;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

import config.AppConfig;

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.PlaybackListener;
import main.Application;
import servercontact.Server;

public class MP3Player implements Runnable{
	
	/****** Variable declarations ******/
	// thread used to play music
	Thread t = null;
	
	// sync lock used to sync the pausing process 
	static Object lock = new Object();

	// variable used to stop playback
	static boolean stop = false;
	
	// variable used to pause playback
	static boolean paused = false;
	
	// source line (decoded link to mp3 stream)
	SourceDataLine line = null;
	
	// decoded format
	AudioFormat decodedFormat = null;
	
	// audio inputStream
	AudioInputStream din = null;
	
	// adjust the volume of the audio stream
	FloatControl volControl = null;
	
	// used to mute the audio stream
	BooleanControl muteControl = null;
	
	/* song properties. These should come from the attributes returned
	 * in the xml file from the subsonic API. Check Server class. */
	public Properties songProperties = new Properties();
	
	/* Listener for the playback process */
    private PlaybackListener listener;
	
    /* The AudioDevice the audio samples are written to. */
    private AudioDevice audio;
    
    /** If the user initiated the stop by pressing skip (forward of back) or by
	 * pressing the stop button then the song didn't complete on it's own and 
	 * will not automatically go to the next song in queue. If the song completes
	 * then the song will continue to the next queue */
    public static boolean songCompleted;
	
    
	/****** End variable declarations ******/
	
	
	public MP3Player(Properties properties){
		URL url = null;
		try{
			songProperties = properties;
			String songID = songProperties.getProperty("id");
			url = Server.getStreamURL(songID);
			//use this to test. This url works.
			//url = new URL("http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.5.0&c=noapp&id=533a5c4d757369635c4120537461746963204c756c6c6162795c416e6420446f6e277420466f7267657420746f20427265617468655c3037202d204120537461746963204c756c6c616279202d20416e6420446f6e7420466f7267657420546f2042726561746865202d205468652053686f6f74696e67205374617220546861742044657374726f792e6d7033");
			AudioInputStream in = AudioSystem.getAudioInputStream(url);
			AudioFormat baseFormat = in.getFormat();
			decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception ex){
			System.out.println("MP3Player: URL = " + url);
			ex.printStackTrace();
		}
	}
	
	
	// the current position of the line in microseconds
	public long getPosition() {
		if (line != null) {
			return line.getMicrosecondPosition();
		} else {
			return 0;
		}
	}
	
	// initializes the current instance and starts the thread
	public void play(){
		if(t == null){
			t = new Thread(this);
			line.addLineListener(createLineListener());
			t.start();
		}
	}
	
	// changes the stop variable to stop the current song from
	// playing. Also ends the instance of this class
	public void stop() {
		if (isPlaying() || isPaused()) {
			stop = true;
		}
	}

	// changes the pause variable to true and causes the line reading to 
	// pause
	public void pause() {
		paused = true;
	}
	
	// changes the pause variable to false and causes the line
	// to continue reading
	public void unpause() {
		synchronized(lock) {
			paused = false;
			lock.notifyAll();
		}
	}

	// sets mute
	public void mute(){
		try {
			muteControl.setValue(true);
		} catch (Exception ignore) {
			// ignore
		}
	}
	
	
	public void unmute(){
		try {
			muteControl.setValue(false);
		} catch (Exception ignore) {
			// ignore
		}
	}

	
	public void setVolume(float gain){ //(range: -80.0db to 6.0206db)
		if (volControl != null) {
			volControl.setValue(gain);
		}
	}

	
	public boolean isPlaying(){
		if(line != null && (line.isRunning() && !isPaused())){
			return true;
		} else {
			return false;
		}
	}

	
	public boolean isPaused() {
		return paused;
	}
	
	
	@Override
	public void run() {
		try{
			if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				Control control[] = line.getControls();
				volControl = ((FloatControl)control[0]);
				volControl.setValue(AppConfig.getVolume());
				muteControl = ((BooleanControl)control[1]);
				
				
				int nBytesRead;
				synchronized (lock) {
					// added !stop to the while loop to stop the reading if
					// the user has requested to stop the song
					while ((nBytesRead = din.read(data, 0, data.length)) != -1 && !stop) {
						while (paused && !stop) {
							if(line.isRunning()) {
								line.stop();
							}
							try {
								lock.wait();
							}
							catch(InterruptedException ignore) {
							}
						}
						if (stop) {
							line.stop();
							break;
						}
						// added !stop to prevent frame read if user pushed stop
						// while the song is paused
						if(!line.isRunning() && !stop) {
							line.start();
						}
						
						// sets the track position on the main screen.
						int position = convertPositionToSeconds(line.getMicrosecondPosition());
						line.write(data, 0, nBytesRead);
						Application.mainWindow.setTrackPosition(position);
					}
				}
			} else {
				
			}
		} catch (Exception ex){
			// on error the playlist should continue
			ex.printStackTrace();
			Application.setStatus("Error during playback");
		}
		finally {
			if(din != null) {
				try { din.close(); } catch(IOException e) { }
			}
			/** check songCompleted declaration above for explanation */
			songCompleted = !stop;
			stop = false;
//			if (stop){
//				stop = false;
//				songCompleted = false;
//			} else {
//				songCompleted = true;
//			}
			clearLine();
		}
		
	}
	
	
	private void clearLine() {
		try {
			line.drain();
			line.flush();
			line.stop();
			line.close();
			din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void addLineListener(LineListener lineListener) {
		line.addLineListener(lineListener);
	}

	
	private int convertPositionToSeconds(long microsecondPosition) {
		double theReturnDouble = microsecondPosition / Math.pow(10, 6);
		int theReturn = (int) Math.floor(theReturnDouble);
		return theReturn;
	}
	
	
	private static LineListener createLineListener() {
		return new LineListener() {

			@Override
			public void update(LineEvent event) { //
				if (event.getType() == LineEvent.Type.START) {
					CurrentPlaylist.songStarted();
					
				} else if (event.getType() == LineEvent.Type.CLOSE) {
					CurrentPlaylist.songStopped(songCompleted);
				}
			}
		};
	}
}
