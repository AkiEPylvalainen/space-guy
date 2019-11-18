package behavior;

import entity.GameCharacter;
import java.awt.Graphics2D;
import level.Level;
import utility.Match;
import utility.Sides;

public abstract class Behavior {
    
    protected GameCharacter _object;
    protected Level _level;
    protected boolean _enabled;
    
    public Behavior() {}
    
    public abstract void begin();
    
    public abstract void update(double deltaTime);
    
    public void draw(Graphics2D g) {}
    
    public void obstruct(Sides side, Match match) {}
    
    public void levelBounds(Sides side) {}
    
    public boolean isEnabled() { return _enabled; }
    
    public void setEnabled(boolean enabled) { _enabled = enabled; }
    
    public void setObject(GameCharacter o, Level level) {
        _object = o;
        _level = level;
    }
}
