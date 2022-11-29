package com;

import java.io.File;
import java.util.Optional;

import javafx.scene.control.Slider;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Player implements AudioSpectrumListener {
    Media media;
    Optional<MediaPlayer> mediaPlayer;
    float[] magnitudes;
    float[] phases;
    Boolean ready;
    Player() {
    	ready = false;
    }
    public Boolean playing() {
    	return mediaPlayer.get().getStatus().equals(Status.PLAYING);
    }
    public void play() {
    	if (mediaPlayer.isPresent()) {
            mediaPlayer.get().play();
    	}
        return;
    }
    public void pause() {
    	if (mediaPlayer.isPresent()) {
            mediaPlayer.get().pause();
    	}
        return;
    }
    public void stop() {
        mediaPlayer.get().stop();
        return;
    }
    public void volume(Slider volumeSlider) {
    	try {
    		if (mediaPlayer.isPresent()) {
    			mediaPlayer.get().volumeProperty().bindBidirectional(volumeSlider.valueProperty());
    		}
    	} catch (Exception e) {
    		// mediaPlayer still null
    	}
    }
    public void set(File song) {
    	try {
    		media = new Media(song.toURI().toString());
    		try {
    			mediaPlayer = Optional.ofNullable(new MediaPlayer(media));
    			mediaPlayer.get().setAutoPlay(true);
    			mediaPlayer.get().setAudioSpectrumListener(this); 
    	    	mediaPlayer.get().setVolume(0.5);
    			ready = true;
    		} catch (Exception e) {
        		e.printStackTrace();
    		}
    	} catch(Exception e) {
    		// not a music file picked leads to no error
    	}
        return;
    }
    public Boolean ready() {
    	return ready;
    }
    public float[] getMagnitudes() {
        return this.magnitudes;
    }
    public float[] getPhases() {
    	return this.phases;
    }
    public float getCorrection() {
    	return mediaPlayer.get().getAudioSpectrumThreshold();
    }
	@Override
	public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
		this.magnitudes = magnitudes;
		this.phases = phases;
	}
}