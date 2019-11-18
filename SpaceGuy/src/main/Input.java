package main;

import java.awt.event.KeyEvent;

public class Input {
    
    private static boolean[] _keyDown = new boolean[256];
    private static boolean[] _keyPressed = new boolean[256];
    private static boolean[] _keyReleased = new boolean[256];
    
    public static void reset() {
        for(int i = 0; i < _keyDown.length; i++) {
            _keyPressed[i] = false;
            _keyReleased[i] = false;
        }
    }
    
    public static boolean isKeyDown(int keyCode) {
        return _keyDown[keyCode];
    }
    
    public static boolean isKeyPressed(int keyCode) {
        return _keyPressed[keyCode];
    }
    
    public static boolean isKeyReleased(int keyCode) {
        return _keyReleased[keyCode];
    }
    
    public static void keyPressed(KeyEvent e) {
        if(!_keyDown[e.getKeyCode()]) {
            _keyPressed[e.getKeyCode()] = true;
        }
        _keyDown[e.getKeyCode()] = true;
    }
    
    public static void keyReleased(KeyEvent e) {
        _keyDown[e.getKeyCode()] = false;
        _keyPressed[e.getKeyCode()] = false;
        _keyReleased[e.getKeyCode()] = true;
    }
}