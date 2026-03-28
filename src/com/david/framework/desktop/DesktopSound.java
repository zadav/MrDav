package com.david.framework.desktop;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import com.david.framework.Sound;

public class DesktopSound implements Sound {
    byte[] audioData;
    AudioFormat format;

    public DesktopSound(byte[] audioData, AudioFormat format) {
        this.audioData = audioData;
        this.format = format;
    }

    @Override
    public void play(float volume) {
        try {
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    audioData.length / format.getFrameSize());
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            try {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20.0);
                gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB)));
            } catch (Exception ignored) {}
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP)
                    clip.close();
            });
            clip.start();
        } catch (Exception e) {
            // Silently ignore audio failures
        }
    }

    @Override
    public void dispose() {
        // Nothing to release; each play() creates its own Clip
    }
}
