package com.david.framework.desktop;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import com.david.framework.Input;

public class DesktopInput implements Input, MouseListener, MouseMotionListener, KeyListener {
    float scaleX, scaleY;

    boolean[] touchDown = new boolean[20];
    int[] touchX = new int[20];
    int[] touchY = new int[20];
    boolean[] keysDown = new boolean[256];

    List<TouchEvent> touchEventsBuffer = new ArrayList<>();
    List<TouchEvent> touchEventsResult = new ArrayList<>();
    List<KeyEvent> keyEventsBuffer = new ArrayList<>();
    List<KeyEvent> keyEventsResult = new ArrayList<>();

    public DesktopInput(JPanel panel, float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    private int sx(int x) { return (int)(x * scaleX); }
    private int sy(int y) { return (int)(y * scaleY); }

    // --- MouseListener ---

    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (this) {
            TouchEvent ev = new TouchEvent();
            ev.type = TouchEvent.TOUCH_DOWN;
            ev.x = sx(e.getX());
            ev.y = sy(e.getY());
            ev.pointer = 0;
            touchDown[0] = true;
            touchX[0] = ev.x;
            touchY[0] = ev.y;
            touchEventsBuffer.add(ev);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (this) {
            TouchEvent ev = new TouchEvent();
            ev.type = TouchEvent.TOUCH_UP;
            ev.x = sx(e.getX());
            ev.y = sy(e.getY());
            ev.pointer = 0;
            touchDown[0] = false;
            touchX[0] = ev.x;
            touchY[0] = ev.y;
            touchEventsBuffer.add(ev);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        synchronized (this) {
            TouchEvent ev = new TouchEvent();
            ev.type = TouchEvent.TOUCH_DRAGGED;
            ev.x = sx(e.getX());
            ev.y = sy(e.getY());
            ev.pointer = 0;
            touchX[0] = ev.x;
            touchY[0] = ev.y;
            touchEventsBuffer.add(ev);
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    // --- KeyListener ---

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        synchronized (this) {
            int code = e.getKeyCode();
            if (code >= 0 && code < 256) keysDown[code] = true;
            KeyEvent ev = new KeyEvent();
            ev.type = KeyEvent.KEY_DOWN;
            ev.keyCode = code;
            ev.keyChar = e.getKeyChar();
            keyEventsBuffer.add(ev);
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        synchronized (this) {
            int code = e.getKeyCode();
            if (code >= 0 && code < 256) keysDown[code] = false;
            KeyEvent ev = new KeyEvent();
            ev.type = KeyEvent.KEY_UP;
            ev.keyCode = code;
            ev.keyChar = e.getKeyChar();
            keyEventsBuffer.add(ev);
        }
    }

    @Override public void keyTyped(java.awt.event.KeyEvent e) {}

    // --- Input interface ---

    @Override
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= 256) return false;
        return keysDown[keyCode];
    }

    @Override
    public boolean isTouchDown(int pointer) {
        if (pointer < 0 || pointer >= 20) return false;
        return touchDown[pointer];
    }

    @Override
    public int getTouchX(int pointer) {
        if (pointer < 0 || pointer >= 20) return 0;
        return touchX[pointer];
    }

    @Override
    public int getTouchY(int pointer) {
        if (pointer < 0 || pointer >= 20) return 0;
        return touchY[pointer];
    }

    @Override public float getAccelX() { return 0; }
    @Override public float getAccelY() { return 0; }
    @Override public float getAccelZ() { return 0; }

    @Override
    public synchronized List<KeyEvent> getKeyEvents() {
        keyEventsResult.clear();
        keyEventsResult.addAll(keyEventsBuffer);
        keyEventsBuffer.clear();
        return keyEventsResult;
    }

    @Override
    public synchronized List<TouchEvent> getTouchEvents() {
        touchEventsResult.clear();
        touchEventsResult.addAll(touchEventsBuffer);
        touchEventsBuffer.clear();
        return touchEventsResult;
    }
}
