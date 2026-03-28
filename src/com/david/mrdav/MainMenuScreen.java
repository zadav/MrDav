package com.david.mrdav;

import com.david.framework.Game;
import com.david.framework.Screen;
import com.david.framework.Input.TouchEvent;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        var g = game.getGraphics();
        for (var event : game.getInput().getTouchEvents()) {
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.setSoundEnabled(!Settings.isSoundEnabled());
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                }
                if (inBounds(event, 64, 220, 192, 42)) {
                    game.setScreen(new GameScreen(game));
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    return;
                }
                if (inBounds(event, 64, 220 + 42, 192, 42)) {
                    game.setScreen(new HighScoreScreen(game));
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    return;
                }
                if (inBounds(event, 64, 220 + 84, 192, 42)) {
                    game.setScreen(new HelpScreen(game, Assets.help1, () ->
                        new HelpScreen(game, Assets.help2, () ->
                            new HelpScreen(game, Assets.help3, () ->
                                new MainMenuScreen(game)))));
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    return;
                }
                if (inBounds(event, 256, 416, 64, 64)) {
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    System.exit(0);
                }
            }
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1
            && event.y > y && event.y < y + height - 1;
    }

    @Override
    public void present(float deltaTime) {
        var g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.logo, 32, 20);
        g.drawPixmap(Assets.mainMenu, 64, 220);
        if (Settings.isSoundEnabled())
            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        else
            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 128, 64, 64);
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override public void resume() {}
    @Override public void dispose() {}
}
