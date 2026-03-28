package com.david.framework.desktop;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import com.david.framework.Music;

public class DesktopMusic implements Music {
    Clip clip;
    boolean stopped = true;
    boolean looping = false;

    public DesktopMusic(AudioInputStream ais) {
        try {
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP)
                    stopped = true;
            });
        } catch (Exception e) {
            throw new RuntimeException("Couldn't open music clip");
        }
    }

    @Override
    public void play() {
        if (clip.isRunning()) return;
        stopped = false;
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
        stopped = true;
    }

    @Override
    public void pause() {
        clip.stop();
    }

    @Override
    public void setLooping(boolean looping) {
        this.looping = looping;
        clip.loop(looping ? Clip.LOOP_CONTINUOUSLY : 0);
    }

    @Override
    public void setVolume(float volume) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20.0);
            gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB)));
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isPlaying() {
        return clip.isRunning();
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public boolean isLooping() {
        return looping;
    }

    @Override
    public void dispose() {
        clip.close();
    }
}
