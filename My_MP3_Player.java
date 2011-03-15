//line.getMicrosecondPosition();


/* 
http://bmjones.no-ip.org/music/rest/stream.view?u=Guest&p=notbrett&v=1.5.0&c=noapp&id=533a5c4d757369635c4120537461746963204c756c6c6162795c416e6420446f6e277420466f7267657420746f20427265617468655c3037202d204120537461746963204c756c6c616279202d20416e6420446f6e7420466f7267657420546f2042726561746865202d205468652053686f6f74696e67205374617220546861742044657374726f792e6d7033
*/

/*
URL url = new URL("http://bmjones.no-ip.org/music/rest/stream.view?u=Brett&p=%3a%3a1212Gh&v=1.5.0&c=noapp&id=533a5c4d757369635c4120537461746963204c756c6c6162795c416e6420446f6e277420466f7267657420746f20427265617468655c3037202d204120537461746963204c756c6c616279202d20416e6420446f6e7420466f7267657420546f2042726561746865202d205468652053686f6f74696e67205374617220546861742044657374726f792e6d7033");
*/			

package testingaudioplayback;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;


public class Main {

	// some lock somewhere...
	static Object lock = new Object();
	// some paused variable
	volatile static boolean paused = false;
	// on the user thread:
	public void userPressedPause() {
	 paused = true;
	}
	 
	public void userPressedPlay() {
	 synchronized(lock) {
	  paused = false;
	  lock.notifyAll();
	 }
	}
	
	public static void main(String[] args) {
		AudioInputStream din = null;
		try {
			URL url = new URL("http://bmjones.no-ip.org/music/rest/stream.view?u=Brett&p=%3a%3a1212Gh&v=1.5.0&c=noapp&id=533a5c4d757369635c4120537461746963204c756c6c6162795c416e6420446f6e277420466f7267657420746f20427265617468655c3037202d204120537461746963204c756c6c616279202d20416e6420446f6e7420466f7267657420546f2042726561746865202d205468652053686f6f74696e67205374617220546861742044657374726f792e6d7033");
			AudioInputStream in = AudioSystem.getAudioInputStream(url);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				
				Control control[] = line.getControls();
				
/***************testing area***********************/
/***************testing area***********************/
/***************testing area***********************/
				if (baseFormat instanceof TAudioFormat)
				{
					Map properties = ((TAudioFormat)baseFormat).properties();
				    String key = "mp3.framerate.fps";
				    Float val = (Float) properties.get(key);
				    System.out.println(val);
				}
				
				
				
				/*
			 	 Master Gain with current value: 0.0 dB (range: -80.0 - 6.0206)
				 Mute Control with current value: False
				 Balance with current value: 0.0  (range: -1.0 - 1.0)
				 Pan with current value: 0.0  (range: -1.0 - 1.0)
				*/
				
				final FloatControl volControl = ((FloatControl)control[0]);
				final BooleanControl muteControl = ((BooleanControl)control[1]);
				
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						
						
						
						
						
						//volControl.setValue(volControl.getValue() - 5);
						//muteControl.setValue(!muteControl.getValue());
						
						//System.out.println(line.get);
//						int timeSec = line.getFramePosition() % 1000;
//						int timeMin = line.getFramePosition() - timeSec;
//						if (timeSec < 9) {
//							System.out.println(timeSec + ":0" + timeSec);
//						} else {
//							System.out.println(timeMin + ":" + timeSec);
//						}
						
					}
				}, 0, 3000);
				
				
/*************** end testing area ***********************/
/*************** end testing area ***********************/
/*************** end testing area ***********************/
				
				int nBytesRead;
				synchronized (lock) {
					while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
						while (paused) {
							if(line.isRunning()) {
								line.stop();
							}
							try {
								lock.wait();
							}
							catch(InterruptedException e) {
							}
						}
					
						if(!line.isRunning()) {
							line.start();
						}
						line.write(data, 0, nBytesRead);
					}
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(din != null) {
				try { din.close(); } catch(IOException e) { }
			}
		}
	}

	
}