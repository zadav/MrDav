package com.david.mrdav;

import java.util.function.Supplier;

import com.david.framework.Game;
import com.david.framework.Pixmap;
import com.david.framework.Screen;
import com.david.framework.Input.TouchEvent;

public class HelpScreen extends Screen {

    private final Pixmap helpImage;
    private final Supplier<Screen> nextScreen;

    public HelpScreen(Game game, Pixmap helpImage, Supplier<Screen> nextScreen) {
        super(game);
        this.helpImage = helpImage;
        this.nextScreen = nextScreen;
    }

    @Override
    public void update(float deltaTime) {
        for (var event : game.getInput().getTouchEvents()) {
            if (event.type == TouchEvent.TOUCH_UP && event.x > 256 && event.y > 416) {
                game.setScreen(nextScreen.get());
                if (Settings.isSoundEnabled())
                    Assets.click.play(1);
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        var g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(helpImage, 64, 100);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}
