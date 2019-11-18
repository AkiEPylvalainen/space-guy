package entity;

import behavior.Behavior;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import level.Level;
import utility.AnimationSet;
import utility.Match;
import utility.Sides;

public abstract class GameCharacter extends GameObject {
    
    protected List<Behavior> _behaviors = new ArrayList<>();
    
    protected boolean _jumping;
    protected double _jumpSpeed = -6.75;
    protected double _stopJumpSpeed;
    
    protected boolean _dead;
    
    protected boolean _flinching;
    protected long _flinchTimer;
    protected long _flinchDelay;
    
    protected boolean _hurt;
    protected long _hurtTimer;
    protected long _hurtDelay = 500;
    
    protected boolean _shooting;
    protected long _shootTimer;
    protected long _shootDelay = 200;
    
    protected int _extraAnim;
    
    protected int _health;
    protected int _maxHealth = 10;
    
    protected int _projectileType = ENEMY_BULLET;
    protected int _projectileID = Projectile.PLASMA;
    protected int _projectileSpeed = 3;
    
    public GameCharacter(double x, double y, int type, int id, Level level, AnimationSet animations) {
        super(x, y, type, id, level, animations);
    }
    
    @Override
    public void checkMoving() {
        if(!_ignoresGravity) {
            if(_jumping && !_falling) {
                _velY = _jumpSpeed;
                _falling = true;
            }

            if(_falling) {
                if(_velY < 0 && !_jumping) {
                    _velY += _stopJumpSpeed;
                }
                
                _velY += _gravity;
                if(_velY >= _maxFallSpeed) {
                    _velY = _maxFallSpeed;
                }
            }
        }
    }
    
    @Override
    public void update(double deltaTime) {
        if(_flinching) {
            _flinchTimer += deltaTime;
            
            _visible = _flinchTimer / 50 % 2 != 0;
            
            if(_flinchTimer >= _flinchDelay) {
                _flinchTimer = 0;
                _flinching = false;
                _visible = true;
            }
        }
        
        if(_hurt) {
            _hurtTimer += deltaTime;
            
            if(_hurtTimer >= _hurtDelay) {
                _hurtTimer = 0;
                _hurt = false;
            }
        }
        
        if(_shooting) {
            _shootTimer += deltaTime;
            
            if(_shootTimer >= _shootDelay) {
                _shootTimer = 0;
                _shooting = false;
            }
        }
        
        super.update(deltaTime);
        
        for(int i = 0; i < _behaviors.size(); i++) {
            if(_behaviors.get(i).isEnabled()) {
                _behaviors.get(i).update(deltaTime);
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        
        for(int i = 0; i < _behaviors.size(); i++) {
            if(_behaviors.get(i).isEnabled()) {
                _behaviors.get(i).draw(g);
            }
        }
    }
    
    public abstract void hit(GameObject o);
    
    @Override
    public void obstruct(Sides side, Match match) {
        super.obstruct(side, match);
        
        for(int i = 0; i < _behaviors.size(); i++) {
            if(_behaviors.get(i).isEnabled()) {
                _behaviors.get(i).obstruct(side, match);
            }
        }
    }
    
    @Override
    public void levelBounds(Sides side) {
        super.levelBounds(side);
        
        for(int i = 0; i < _behaviors.size(); i++) {
            if(_behaviors.get(i).isEnabled()) {
                _behaviors.get(i).levelBounds(side);
            }
        }
    }
    
    @Override
    public void stopMoving() {
        _velX = 0;
        _velY = 0;
        _jumping = false;
    }
    
    public Projectile shoot() {
        Projectile p = new Projectile(_projectileType, _projectileID, _level, getAnimations());
        
        if(_facingRight) {
            p.left(right());
        }
        else {
            p.right(left());
        }
        
        p.top(top());
        
        p.setVelX(isFacingRight() ? _projectileSpeed : -_projectileSpeed);
        
        _level.addProjectile(p);
        
        _shooting = true;
        
        return p;
    }
    
    public void jump(boolean jumping) { _jumping = jumping; }
    
    public void alterHealth(int add) {
        _health += add;

        if(_health < 0) {
            _health = 0;
        }
        if(_health >= _maxHealth) {
            _health = _maxHealth;
        }
    }
    
    public int getHealth() { return _health; }
    
    public int getMaxHealth() { return _maxHealth; }
    
    public boolean isDead() { return _dead; }
    
    @Override
    public boolean canCollide() { return !_dead; }
    
    public void addBehavior(Behavior behavior, boolean activate) {
        _behaviors.add(behavior);
        behavior.setObject(this, _level);
        
        if(activate) {
            behavior.setEnabled(true);
            behavior.begin();
        }
    }
    
    public void addBehavior(Behavior behavior) {
        _behaviors.add(behavior);
        behavior.setObject(this, _level);
        behavior.setEnabled(true);
        behavior.begin();
    }
    
    public void activateBehavior() {
        for(int i = 0; i < _behaviors.size(); i++) {
            _behaviors.get(i).setEnabled(true);
            _behaviors.get(i).begin();
        }
    }
    
    public boolean isFlinching() { return _flinching; }
    
    public void setFlinching(boolean flinching) {
        _flinching = flinching;
        _flinchTimer = 0;
    }
    
    public void setExtraAnim(int anim) {
        _extraAnim = anim;
    }
    
    @Override
    public void setVisible(boolean visible) {
        _visible = visible;
        _flinching = false;
        _flinchTimer = 0;
    }
}
