package behavior;

import level.TileResolver;
import utility.Match;
import utility.Sides;

public class PendulumWalk extends Behavior {
    
    private double _moveSpeed;
    private boolean _turnOnPit;
    
    public PendulumWalk(double moveSpeed, boolean turnOnPit) {
        _moveSpeed = moveSpeed;
        _turnOnPit = turnOnPit;
    }
    
    @Override
    public void begin() {
        _object.moveX(-_moveSpeed);
    }
    
    @Override
    public void update(double deltaTime) {
        if(_turnOnPit && checkPit()) {
            _object.moveX(-_object.getVelX());
        }
    }
    
    private boolean checkPit() {
        if(_object.getVelY() != 0) return false;
        
        double x;
        
        if(_object.getVelX() > 0) {
            x = _object.right();
        }
        else if(_object.getVelX() < 0) {
            x = _object.left();
        }
        else {
            return false;
        }
        
        TileResolver resolver = _level.getCollider().getResolver();
        
        return !_level.getMap().isSolid((int)resolver.toIndex(x), (int)resolver.toIndex(_object.bottom()));
    }
    
    @Override
    public void obstruct(Sides side, Match match) {
        int tileX = (int)(match.x1 / 16);
        int tileY = (int)(match.y1 / 16);
        
        if(_level.getMap().isSolid(tileX, tileY)) {
            if(side == Sides.LEFT) {
                _object.moveX(_object.getMoveSpeed());
            }
            else if(side == Sides.RIGHT) {
                _object.moveX(-_object.getMoveSpeed());
            }
        }
    }
    
    @Override
    public void levelBounds(Sides side) {
        if(side == Sides.LEFT) {
            _object.moveX(_object.getMoveSpeed());
        }
        else if(side == Sides.RIGHT) {
            _object.moveX(-_object.getMoveSpeed());
        }
    }
}
