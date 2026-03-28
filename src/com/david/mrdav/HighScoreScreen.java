package com.david.mrdav;

import com.david.framework.Game;
import com.david.framework.Graphics;
import com.david.framework.Screen;
import com.david.framework.Input.TouchEvent;

public class HighScoreScreen extends Screen {

    private final String[] lines = new String[5];

    public HighScoreScreen(Game game) {
        super(game);
        var highscores = Settings.getHighscores();
        for (int i = 0; i < 5; i++) {
            lines[i] = (i + 1) + "." + highscores[i];
        }
    }

    @Override
    public void update(float deltaTime) {
        for (var event : game.getInput().getTouchEvents()) {
            if (event.type == TouchEvent.TOUCH_UP && event.x < 64 && event.y > 416) {
                if (Settings.isSoundEnabled())
                    Assets.click.play(1);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        var g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.mainMenu, 64, 20, 0, 42, 196, 42);

        int y = 100;
        for (var line : lines) {
            drawText(g, line, 20, y);
            y += 50;
        }
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
    }

    private void drawText(Graphics g, String line, int x, int y) {
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == ' ') {
                x += 20;
                continue;
            }
            int srcX, srcWidth;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }
            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}
