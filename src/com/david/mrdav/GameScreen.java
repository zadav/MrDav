package com.david.mrdav;

import java.util.List;

import com.david.framework.Game;
import com.david.framework.Graphics;
import com.david.framework.Input.TouchEvent;
import com.david.framework.Pixmap;
import com.david.framework.Screen;

public class GameScreen extends Screen {

    enum GameState {
        Ready, Running, Paused, GameOver
    }

    GameState state = GameState.Ready;
    World world;
    int oldScore = 0;
    String score = "0";

    public GameScreen(Game game) {
        super(game);
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        var touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (state == GameState.Ready)    updateReady(touchEvents);
        if (state == GameState.Running)  updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)   updatePaused(touchEvents);
        if (state == GameState.GameOver) updateGameOver(touchEvents);
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        for (var event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 && event.y >= 200 && event.y <= 264) {
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        for (var event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        if (Settings.isSoundEnabled())
                            Assets.click.play(1);
                        state = GameState.Running;
                        return;
                    }
                    if (event.y > 148 && event.y < 196) {
                        if (Settings.isSoundEnabled())
                            Assets.click.play(1);
                        game.setScreen(new MainMenuScreen(game));
                        return;
                    }
                }
            }
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        for (var event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y < 64) {
                    if (Settings.isSoundEnabled())
                        Assets.click.play(1);
                    state = GameState.Paused;
                    return;
                }
            }
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (event.x < 64 && event.y > 416)  world.snake.turnLeft();
                if (event.x > 256 && event.y > 416) world.snake.turnRight();
            }
        }
        world.update(deltaTime);
        if (world.gameOver) {
            if (Settings.isSoundEnabled())
                Assets.bitten.play(1);
            state = GameState.GameOver;
        }
        if (oldScore != world.score) {
            oldScore = world.score;
            score = String.valueOf(oldScore);
            if (Settings.isSoundEnabled())
                Assets.eat.play(1);
        }
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if (!touchEvents.isEmpty())
            state = GameState.Running;
    }

    @Override
    public void present(float deltaTime) {
        var g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        drawWorld(world);
        if (state == GameState.Ready)   drawReadyUI();
        if (state == GameState.Running) drawRunningUI();
        if (state == GameState.Paused)  drawPausedUI();
        if (state == GameState.GameOver) drawGameOverUI();

        drawText(g, score, g.getWidth() / 2 - score.length() * 20, g.getHeight() - 42);
    }

    private void drawWorld(World world) {
        var g = game.getGraphics();
        var snake = world.snake;
        var head = snake.parts.get(0);
        var stain = world.stain;

        Pixmap stainPixmap = switch (stain.type()) {
            case TYPE_1 -> Assets.stain1;
            case TYPE_2 -> Assets.stain2;
            case TYPE_3 -> Assets.stain3;
        };
        g.drawPixmap(stainPixmap, stain.x() * 32, stain.y() * 32);

        for (int i = 1; i < snake.parts.size(); i++) {
            var part = snake.parts.get(i);
            g.drawPixmap(Assets.tail, part.x() * 32, part.y() * 32);
        }

        Pixmap headPixmap = switch (snake.direction) {
            case UP    -> Assets.headUp;
            case LEFT  -> Assets.headLeft;
            case DOWN  -> Assets.headDown;
            case RIGHT -> Assets.headRight;
        };
        int hx = head.x() * 32 + 16;
        int hy = head.y() * 32 + 16;
        g.drawPixmap(headPixmap, hx - headPixmap.getWidth() / 2, hy - headPixmap.getHeight() / 2);
    }

    private void drawReadyUI() {
        var g = game.getGraphics();
        g.drawPixmap(Assets.ready, 47, 100);
        g.drawLine(0, 416, 480, 416, 0xFF000000);
    }

    private void drawRunningUI() {
        var g = game.getGraphics();
        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, 0xFF000000);
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }

    private void drawPausedUI() {
        var g = game.getGraphics();
        g.drawPixmap(Assets.pause, 80, 100);
        g.drawLine(0, 416, 480, 416, 0xFF000000);
    }

    private void drawGameOverUI() {
        var g = game.getGraphics();
        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, 0xFF000000);
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

    @Override
    public void pause() {
        if (state == GameState.Running)
            state = GameState.Paused;
        if (world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override public void resume() {}
    @Override public void dispose() {}
}
