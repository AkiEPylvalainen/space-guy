package state;

import java.awt.Graphics2D;

public abstract class GameState {
    
    private GameStateManager _gsm;
    
    public GameState(GameStateManager gsm) {
        _gsm = gsm;
    }
    
    public void init() {}
    
    public void resume() {}
    
    public void pause() {}
    
    public void close() {}
    
    public abstract void update(double deltaTime);
    
    public abstract void draw(Graphics2D g);
    
    protected GameStateManager getGSM() { return _gsm; }
}
