package servercontact;

import java.io.InputStream;

import objects.CurrentPlaylist;
import objects.CurrentSong;
import settings.Application;
import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;;

public class Media implements Runnable {

	InputStream SONG_INPUT_STREAM;
	Player PLAYER = null;
	Thread t;

	public void init() {
		stop();
		SONG_INPUT_STREAM = CurrentPlaylist.getInputStream();;
		t = new Thread(this);
		t.start();
		
	}

	public boolean isActive() {
		if (PLAYER == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void stop() {
		if (t != null) {
			Application.showNowPlaying(false);
			PLAYER.close();
			try {
				Thread.sleep(100);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void togglePause(){ // true pauses false plays
		PLAYER.playing = !PLAYER.playing;
		
	}
	
	
	public boolean isPaused(){ // true pauses false plays
		if (t != null) {
			return !PLAYER.playing;
		} else {
			return false;
		}
	}

	public void run() {
		try {
			Application.showNowPlaying(true);
			PLAYER = new Player(SONG_INPUT_STREAM);
			PLAYER.play();
			
			if (PLAYER.isComplete()){ // when current song finishes go to the next song.
				CurrentPlaylist.skipToNextSong();
			}
			
		} catch (JavaLayerException ex) {
			System.out.println("Can't start song");
		}
	}


}
