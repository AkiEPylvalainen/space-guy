package state;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GameStateManager {
    
    private Map<String, GameState> _states = new HashMap<>();
    private Stack<GameState> _stack = new Stack();
    private int _screenWidth;
    private int _screenHeight;
    
    public GameStateManager(int screenWidth, int screenHeight) {
        _screenWidth = screenWidth;
        _screenHeight = screenHeight;
    }
    
    public void add(String name, GameState state) {
        _states.put(name, state);
    }
    
    public void push(String name) {
        if(!_stack.isEmpty()) {
            _stack.peek().pause();
        }
        
        _stack.push(_states.get(name));
        _stack.peek().init();
    }
    
    public void pop() {
        if(!_stack.isEmpty()) {
            _stack.pop().close();
        }
        if(!_stack.isEmpty()) {
            _stack.peek().resume();
        }
    }
    
    public GameState getCurrent() { return _stack.peek(); }
    
    public int getScreenWidth()   { return _screenWidth; }
    
    public int getScreenHeight()  { return _screenHeight; }
}
