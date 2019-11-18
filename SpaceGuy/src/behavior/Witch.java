package behavior;

import effect.MagicBlow;
import entity.GameObject;
import entity.Projectile;
import java.util.ArrayList;
import java.util.List;
import utility.Globals;

public class Witch extends StateBehavior {
    
    public static final int SHOOT_WARN    = 0;
    public static final int WAIT          = 1;
    public static final int SHOOT         = 2;
    public static final int SHOOT_WAIT    = 3;
    public static final int TELEPORT      = 4;
    public static final int LONG_WAIT     = 5;
    public static final int APPEAR        = 6;
    public static final int RAIN          = 7;
    public static final int TELEPORT_BACK = 8;
    public static final int APPEAR_GROUND = 9;
    
    private GameObject _target;
    
    private List<Projectile> _projectiles = new ArrayList<>();
    
    private long _timer;
    
    private int _rainCount;
    private int _maxRainCount = 7;
    private int _rainDelay = 500;
    
    @Override
    public void begin() {
        super.begin();
        _target = _level.getPlayer();
    }
    
    @Override
    protected void populateStates() {
        _queue.add(SHOOT_WARN);
        _queue.add(WAIT);
        _queue.add(SHOOT);
        _queue.add(SHOOT_WAIT);
        _queue.add(WAIT);
        _queue.add(TELEPORT);
        _queue.add(LONG_WAIT);
        _queue.add(APPEAR);
        _queue.add(WAIT);
        _queue.add(RAIN);
        _queue.add(SHOOT_WAIT);
        _queue.add(WAIT);
        _queue.add(TELEPORT_BACK);
        _queue.add(LONG_WAIT);
        _queue.add(APPEAR_GROUND);
        _queue.add(LONG_WAIT);
    }
    
    @Override
    public void update(double deltaTime) {
        for(int i = 0; i < _projectiles.size(); i++) {
            if(_projectiles.get(i).shouldRemove()) {
                _projectiles.remove(i);
                i--;
            }
        }
        
        if(_object.getVelX() == 0 && _object.getVelY() == 0) {
            _object.setFacingRight(_target.getX() > _object.getX());
        }
        
        super.update(deltaTime);
    }
    
    @Override
    protected void beginState(int state) {
        if(state == SHOOT_WARN) {
            _object.setExtraAnim(1);
            _finished = true;
        }
        else if(state == SHOOT) {
            tripleShoot();
            _object.setExtraAnim(0);
            _finished = true;
        }
        else if(state == TELEPORT) {
            _level.addEffect(new MagicBlow(_object.getCenterX(), _object.getCenterY()));
            _object.setVisible(false);
            _object.setX(9 * _level.getMap().getTileSize() - 8);
            _object.setY(3 * _level.getMap().getTileSize());
            _object.setIgnoringGravity(true);
            _finished = true;
        }
        else if(state == APPEAR) {
            _level.addEffect(new MagicBlow(_object.getCenterX(), _object.getCenterY()));
            _object.setVisible(true);
            _finished = true;
        }
        else if(state == TELEPORT_BACK) {
            _level.addEffect(new MagicBlow(_object.getCenterX(), _object.getCenterY()));
            _object.setVisible(false);
            _finished = true;
        }
        else if(state == APPEAR_GROUND) {
            double rand = Math.random();
            
            if(rand <= 0.5) {
                _object.setX(15 * _level.getMap().getTileSize());
            }
            else {
                _object.setX(2 * _level.getMap().getTileSize());
            }
            
            _object.setY(10 * _level.getMap().getTileSize());
            
            _level.addEffect(new MagicBlow(_object.getCenterX(), _object.getCenterY()));
            _object.setIgnoringGravity(false);
            _object.setVisible(true);
            _finished = true;
        }
    }

    @Override
    protected void updateState(int state, double deltaTime) {
        if(state == WAIT) {
            _timer += deltaTime;
            
            if(_timer >= 200) {
                _timer = 0;
                _finished = true;
            }
        }
        if(state == LONG_WAIT) {
            _timer += deltaTime;
            
            if(_timer >= 750) {
                _timer = 0;
                _finished = true;
            }
        }
        else if(state == SHOOT_WAIT) {
            if(_projectiles.isEmpty()) {
                _finished = true;
            }
        }
        else if(state == RAIN) {
            _timer += deltaTime;
            
            if(_timer >= _rainDelay) {
                _timer = 0;
                _rainCount++;
                
                rainShoot();
                
                if(_rainCount >= _maxRainCount) {
                    _rainCount = 0;
                    _finished = true;
                }
            }
        }
    }
    
    private void tripleShoot() {
        for(int i = 0; i < 3; i++) {
            Projectile p = _object.shoot();
            
            if(!_object.isFacingRight()) {
                double angle = Math.PI + Math.PI / 10 * i;
            
                p.setVelX(Math.cos(angle) * 3);
                p.setVelY(Math.sin(angle) * 3);
            }
            else {
                double angle = Math.PI * 1.8 + Math.PI / 10 * i;
                
                p.setVelX(Math.cos(angle) * 3);
                p.setVelY(Math.sin(angle) * 3);
            }
            
            _projectiles.add(p);
        }
    }
    
    private void rainShoot() {
        Projectile p = _object.shoot();
        
        p.setX(_object.getX());
        p.top(_object.bottom() - 8);
        
        double destX = _target.getX();
        double destY = _target.getY();
        
        double len = Globals.distance(p.getX(), p.getY(), destX, destY);
        
        p.setVelX((destX - p.getX()) / len * 3);
        p.setVelY((destY - p.getY()) / len * 3);
        
        _projectiles.add(p);
    }
}
