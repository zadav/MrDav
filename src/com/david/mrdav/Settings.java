package com.david.mrdav;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.david.framework.FileIO;

public class Settings {
    private static boolean soundEnabled = true;
    private static int[] highscores = new int[]{ 100, 80, 50, 30, 10 };

    public static boolean isSoundEnabled() { return soundEnabled; }
    public static void setSoundEnabled(boolean enabled) { soundEnabled = enabled; }
    public static int[] getHighscores() { return highscores; }

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(".mrdav")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException | NumberFormatException e) {
            // Use defaults if file doesn't exist or is malformed
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException e) {}
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mrdav")));
            out.write(Boolean.toString(soundEnabled));
            out.newLine();
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.newLine();
            }
        } catch (IOException e) {
            // Silently skip if save fails
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException e) {}
            }
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highscores[i] < score) {
                for (int j = 4; j > i; j--) {
                    highscores[j] = highscores[j - 1];
                }
                highscores[i] = score;
                break;
            }
        }
    }
}
