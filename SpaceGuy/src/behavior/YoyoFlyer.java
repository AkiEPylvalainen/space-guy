package behavior;

import utility.Match;
import utility.Sides;

public class YoyoFlyer extends Behavior {
    
    private double _angle;
    private double _magnitude = -1.5;
    private double _speed = 0.04;
    
    public YoyoFlyer() {}
    
    @Override
    public void begin() {
        _object.setIgnoringGravity(true);
    }
    
    @Override
    public void update(double deltaTime) {
        _angle = (_angle + _speed) % (Math.PI * 2);
        
        _object.setVelY(Math.cos(_angle) * _magnitude);
    }

    @Override
    public void obstruct(Sides side, Match match) {}

    @Override
    public void levelBounds(Sides side) {}
}
