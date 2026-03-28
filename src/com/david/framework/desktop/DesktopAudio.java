package com.david.framework.desktop;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import com.david.framework.Audio;
import com.david.framework.Music;
import com.david.framework.Sound;

public class DesktopAudio implements Audio {

    @Override
    public Music newMusic(String filename) {
        try {
            InputStream in = DesktopAudio.class.getResourceAsStream("/" + filename);
            if (in == null)
                throw new RuntimeException("Couldn't load music: " + filename);
            AudioInputStream ais = AudioSystem.getAudioInputStream(in);
            return new DesktopMusic(ais);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music: " + filename, e);
        }
    }

    @Override
    public Sound newSound(String filename) {
        try {
            InputStream in = DesktopAudio.class.getResourceAsStream("/" + filename);
            if (in == null)
                throw new RuntimeException("Couldn't load sound: " + filename);
            AudioInputStream ais = AudioSystem.getAudioInputStream(in);
            AudioFormat format = ais.getFormat();
            byte[] audioData = ais.readAllBytes();
            ais.close();
            return new DesktopSound(audioData, format);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound: " + filename, e);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load sound: " + filename, e);
        }
    }
}
