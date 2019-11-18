package behavior;

import entity.GameObject;
import level.TileResolver;
import utility.Globals;
import utility.Match;
import utility.Sides;

public class Bully extends StateBehavior {
    
    public static final int WALK = 0;
    public static final int PUSH = 1;
    public static final int WAIT = 2;
    
    private GameObject _target;
    private double _moveSpeed;
    private long _timer;
    private int _sight = 64;
    
    public Bully(double moveSpeed) {
        _moveSpeed = moveSpeed;
    }
    
    @Override
    public void begin() {
        super.begin();
        _target = _level.getPlayer();
    }
    
    @Override
    protected void populateStates() {
        _queue.add(WALK);
        _queue.add(PUSH);
        _queue.add(WAIT);
    }
    
    @Override
    protected void beginState(int state) {
        if(state == WALK) {
            _object.moveX(_object.isFacingRight() ? _moveSpeed : -_moveSpeed);
        }
        else if(state == PUSH) {
            _object.setVelX(Math.signum(_object.getVelX()) * _moveSpeed * 2.5);
        }
    }
    
    @Override
    protected void updateState(int state, double deltaTime) {
        if(state == WALK) {
            if(checkPit()) {
                _object.moveX(-_object.getVelX());
            }
            
            if(_target != null) {
                if(seesTarget()) {
                    _finished = true;
                }
            }
        }
        else if(state == PUSH) {
            if(_target != null) {
                if(_object.collides(_target) || checkPit()) {
                    _object.setVelX(0);
                    _finished = true;
                }
            }
        }
        else if(state == WAIT) {
            _timer += deltaTime;
            
            if(_timer >= 1500) {
                _timer = 0;
                _object.setFacingRight(!_object.isFacingRight());
                _finished = true;
            }
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
    
    private boolean seesTarget() {
        return Globals.distance(_object.getX(), _target.getX()) <= _sight &&
                ((_object.getX() < _target.getX() && _object.isFacingRight()) || (_object.getX() > _target.getX() && !_object.isFacingRight()));
    }
}
