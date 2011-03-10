package servercontact;

import java.io.InputStream;

import objects.CurrentSong;
import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;;

public class Media implements Runnable {

	InputStream SONG_INPUT_STREAM;
	Player PLAYER = null;
	Thread t;

	public void init() {
		stop();
		SONG_INPUT_STREAM = CurrentSong.getInputStream();;
		t = new Thread(this);
		t.start();
		
	}

	
	public void stop() {
		if (t != null) {
			PLAYER.close();
			try {
				Thread.sleep(100);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pause(){ // true pauses false plays
		if (t != null) {
			PLAYER.playing = !PLAYER.playing;
		}
	}

	public void run() {
		try {
			PLAYER = new Player(SONG_INPUT_STREAM);
			PLAYER.play();
			
		} catch (JavaLayerException ex) {
			System.out.println("Can't start song");
		}
	}

}
