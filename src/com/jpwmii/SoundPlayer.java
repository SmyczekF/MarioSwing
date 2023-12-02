package com.jpwmii;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer {
    private final InputStream inputStream;
    private Clip clip;

    public SoundPlayer(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
    }

    public void play() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playLoop() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopLoop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void startLoop() {
        if (clip != null && !clip.isRunning()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else if (clip == null) {
            playLoop();
        }
    }
}
