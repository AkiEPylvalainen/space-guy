package behavior;

import effect.SmokeCloud;
import entity.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import utility.*;

public class Ninja extends StateBehavior {
    
    public static final int WAIT          = 0;
    public static final int SHURIKEN_WARN = 1;
    public static final int SHURIKEN      = 2;
    public static final int DASH_WARN     = 3;
    public static final int DASH          = 4;
    public static final int WALL_RUN      = 5;
    public static final int VANISH        = 6;
    public static final int RAIN          = 7;
    public static final int SHURIKEN_WAIT = 8;
    public static final int APPEAR        = 9;
    public static final int FALL          = 10;
    
    private long _timer;
    
    private List<Projectile> _projectiles = new ArrayList<>();
    
    private GameCharacter _target;
    
    private int _rainCount;
    private int _maxRainCount = 5;
    private int _rainDelay = 500;
    
    private double _dashSpeed = 3;
    
    private boolean _showShuriken;
    
    @Override
    public void begin() {
        super.begin();
        _target = _level.getPlayer();
    }
    
    @Override
    protected void populateStates() {
        _queue.add(SHURIKEN_WARN);
        _queue.add(WAIT);
        _queue.add(SHURIKEN);
        _queue.add(SHURIKEN_WAIT);
        _queue.add(DASH_WARN);
        _queue.add(WAIT);
        _queue.add(DASH);
        _queue.add(WALL_RUN);
        _queue.add(VANISH);
        _queue.add(WAIT);
        _queue.add(RAIN);
        _queue.add(SHURIKEN_WAIT);
        _queue.add(APPEAR);
        _queue.add(FALL);
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
        if(state == SHURIKEN_WARN) {
            _object.setExtraAnim(2);
            _showShuriken = true;
            _finished = true;
        }
        else if(state == SHURIKEN) {
            _projectiles.add(_object.shoot());
            _object.setExtraAnim(0);
            _showShuriken = false;
            _finished = true;
        }
        else if(state == DASH_WARN) {
            _object.setExtraAnim(1);
            _finished = true;
        }
        else if(state == DASH) {
            _object.setVelX(_object.isFacingRight() ? _dashSpeed : -_dashSpeed);
            _object.setExtraAnim(0);
        }
        else if(state == WALL_RUN) {
            _object.setIgnoringGravity(true);
            _object.setVelX(0);
            _object.setVelY(-_dashSpeed);
        }
        else if(state == VANISH) {
            _object.setVelY(0);
            _object.setVisible(false);
            _level.addEffect(new SmokeCloud(_object.getCenterX(), _object.getCenterY()));
            _finished = true;
        }
        else if(state == APPEAR) {
            _object.setX(_target.getX());
            _object.setIgnoringGravity(false);
            _object.setVisible(true);
            _level.addEffect(new SmokeCloud(_object.getCenterX(), _object.getCenterY()));
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
        else if(state == RAIN) {
            _timer += deltaTime;
            
            if(_timer >= _rainDelay) {
                _timer = 0;
                _rainCount++;
                
                rainShuriken();
                
                if(_rainCount >= _maxRainCount) {
                    _rainCount = 0;
                    _finished = true;
                }
            }
        }
        else if(state == SHURIKEN_WAIT) {
            if(_projectiles.isEmpty()) {
                _finished = true;
            }
        }
        else if(state == FALL) {
            if(_object.getVelY() == 0 && !_object.isFalling()) {
                _finished = true;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        if(_showShuriken) {
            _object.getAnimations().getSheet().draw(g, "shuriken-1", (int)_object.getX(), (int)(_object.getY() - 16));
        }
    }
    
    private void rainShuriken() {
        Projectile p = _object.shoot();
        
        p.setX(Globals.rangedRandom(2 * _level.getMap().getTileSize(), 15 * _level.getMap().getTileSize()));
        p.setY(24);
        
        double destX = _target.getX();
        double destY = _target.getY();
        
        double len = Globals.distance(p.getX(), p.getY(), destX, destY);
        
        p.setVelX((destX - p.getX()) / len * 2);
        p.setVelY((destY - p.getY()) / len * 2);
        
        _projectiles.add(p);
        
        _level.addEffect(new SmokeCloud(p.getCenterX(), p.getCenterY()));
    }
    
    @Override
    public void obstruct(Sides side, Match match) {
        int tileX = (int)(match.x1 / _level.getMap().getTileSize());
        int tileY = (int)(match.y1 / _level.getMap().getTileSize());
        
        if(_level.getMap().isSolid(tileX, tileY)) {
            if(_current == DASH) {
                if(side == Sides.LEFT || side == Sides.RIGHT) {
                    _object.lockFacing(true);
                    _finished = true;
                }
            }
            else if(_current == WALL_RUN) {
                if(side == Sides.TOP) {
                    _object.lockFacing(false);
                    _finished = true;
                }
            }
        }
    }
}
