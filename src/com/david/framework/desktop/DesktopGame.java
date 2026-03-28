package com.david.framework.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.david.framework.Audio;
import com.david.framework.FileIO;
import com.david.framework.Game;
import com.david.framework.Input;
import com.david.framework.Screen;
import com.david.mrdav.LoadingScreen;

public class DesktopGame implements Game, Runnable {

    static final int FB_WIDTH = 320;
    static final int FB_HEIGHT = 480;
    static final int SCALE = 2;

    BufferedImage frameBuffer;
    JFrame frame;
    JPanel panel;
    DesktopGraphics graphics;
    DesktopAudio audio;
    DesktopInput input;
    DesktopFileIO fileIO;
    Screen screen;
    volatile boolean running = false;
    Thread gameThread;

    public DesktopGame() {
        frameBuffer = new BufferedImage(FB_WIDTH, FB_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = new DesktopGraphics(frameBuffer);
        audio = new DesktopAudio();
        fileIO = new DesktopFileIO();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(frameBuffer, 0, 0, FB_WIDTH * SCALE, FB_HEIGHT * SCALE, null);
            }
        };
        panel.setPreferredSize(new Dimension(FB_WIDTH * SCALE, FB_HEIGHT * SCALE));
        panel.setBackground(Color.BLACK);

        // Scale mouse coordinates from panel space (640x960) back to framebuffer space (320x480)
        float scaleX = (float) FB_WIDTH / (FB_WIDTH * SCALE);
        float scaleY = (float) FB_HEIGHT / (FB_HEIGHT * SCALE);
        input = new DesktopInput(panel, scaleX, scaleY);

        frame = new JFrame("MrDav");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        screen = new LoadingScreen(this);
    }

    public void start() {
        running = true;
        gameThread = new Thread(this);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        while (running) {
            float deltaTime = (System.nanoTime() - startTime) / 1_000_000_000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
            panel.repaint();

            try {
                Thread.sleep(16); // ~60 fps cap
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public Input getInput() { return input; }

    @Override
    public FileIO getFileIO() { return fileIO; }

    @Override
    public com.david.framework.Graphics getGraphics() { return graphics; }

    @Override
    public Audio getAudio() { return audio; }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() { return screen; }

    @Override
    public Screen getStartScreen() { return screen; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DesktopGame game = new DesktopGame();
            game.start();
        });
    }
}
