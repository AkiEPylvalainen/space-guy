package behavior;

import entity.GameObject;
import utility.Globals;

public class Shooter extends Behavior {
    
    private GameObject _target;
    
    private boolean _cooldown;
    private long _cooldownTimer;
    private long _cooldownDelay = 1000;
    
    private int _sight = 64;
    
    public Shooter() {}
    
    @Override
    public void begin() {
        _target = _level.getPlayer();
    }

    @Override
    public void update(double deltaTime) {
        if(_cooldown) {
            _cooldownTimer += deltaTime;
            
            if(_cooldownTimer >= _cooldownDelay) {
                _cooldownTimer = 0;
                _cooldown = false;
            }
        }
        else {
            if(_target != null) {
                if(seesTarget()) {
                    _object.shoot();
                    _cooldown = true;
                }
            }
        }
    }
    
    private boolean seesTarget() {
        return Globals.distance(_object.getX(), _target.getX()) <= _sight &&
                ((_object.getX() < _target.getX() && _object.isFacingRight()) || (_object.getX() > _target.getX() && !_object.isFacingRight()));
    }
}
