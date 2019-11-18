package entity;

import effect.Explosion;
import level.Level;
import level.Tile;
import utility.*;

public class Player extends GameCharacter {
    
    private boolean _left;
    private boolean _right;
    
    private boolean _reacting = true;
    
    public Player(double x, double y, Level level, AnimationSet animations) {
        super(x, y, PLAYER, 0, level, animations);
        _stopJumpSpeed = 0.6;
        _moveSpeed = 2;
        _flinchDelay = 1000;
        calculateSize(3, 2);
        
        _health = _maxHealth = 10;
        _projectileType = PLAYER_BULLET;
        _projectileSpeed = 4;
        
        setAnimation("player-idle");
    }
    
    @Override
    public void checkMoving() {
        super.checkMoving();
        
        if(!_hurt) {
            if(_left) {
                _velX = -_moveSpeed;
            }
            else if(_right) {
                _velX = _moveSpeed;
            }
            else {
                _velX = 0;
            }
        }
    }
    
    @Override
    protected void checkObjectCollision() {
        for(int i = 0; i < _level.getObjects().size(); i++) {
            GameObject o = _level.getObjects().get(i);
            if(o != this && o.canCollide()) {
                if(o.collides(this)) {
                    if(o.getType() == ENEMY) {
                        if(!_flinching) {
                            hit(o);
                        }
                    }
                    else if(o.getType() == ITEM) {
                        o.remove();
                        if(o.getId() == Item.HEALTH) {
                            alterHealth(5);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void hit(GameObject o) {
        alterHealth(-1);
        
        double distX = Globals.distance(_x, o._x);
        double distY = Globals.distance(_y, o._y);
        
        if(_health <= 0) {
            _dead = true;
            _removed = true;
            _level.addEffect(new Explosion(_x + distX / 2, _y + distY / 2, 4, 20));
            _level.passMessage(new MSG(MSG.TYPE.DEATH, 0));
        }
        else {
            setFlinching(true);
            _hurt = true;
            _jumping = false;
            _velX = Math.signum(_x - o._x) * 1;
            _velY = 0;
            _level.addEffect(new Explosion(_x + distX / 2, _y + distY / 2, 4, 20));
        }
    }
    
    @Override
    public void stopMoving() {
        super.stopMoving();
        
        _left = false;
        _right = false;
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        
        _left = false;
        _right = false;
    }
    
    @Override
    protected void checkAnimation() {
        if(_hurt) {
            setAnimation("player-hurt");
        }
        else if(_shooting && _velY < 0) {
            setAnimation("player-shoot-jump");
        }
        else if(_shooting && _velY > 0 && _falling) {
            setAnimation("player-shoot-fall");
        }
        else if(_shooting && (_left || _right)) {
            if(_currentAnim.equals("player-walk")) {
                swapAnimation("player-shoot-walk");
            }
            else {
                setAnimation("player-shoot-walk");
            }
        }
        else if(_shooting){
            setAnimation("player-shoot-idle");
        }
        else if(_velY < 0) {
            setAnimation("player-jump");
        }
        else if(_velY > 0 && _falling) {
            setAnimation("player-fall");
        }
        else if(_left || _right) {
            if(_currentAnim.equals("player-shoot-walk")) {
                swapAnimation("player-walk");
            }
            else {
                setAnimation("player-walk");
            }
        }
        else {
            setAnimation("player-idle");
        }
    }
    
    @Override
    public void obstruct(Sides side, Match match) {
        int tileX = (int)(match.x1 / _level.getMap().getTileSize());
        int tileY = (int)(match.y1 / _level.getMap().getTileSize());
        
        if(_reacting && _level.getMap().isTileType(tileX, tileY, Tile.TYPE.DOOR)) {
            _level.passMessage(new MSG(MSG.TYPE.ENTER_BOSS, 0));
            _reacting = false;
        }
        else {
            super.obstruct(side, match);
        }
    }
    
    @Override
    public Projectile shoot() {
        return !_hurt ? super.shoot() : null;
    }
    
    @Override
    public void jump(boolean jumping) {
        super.jump(!_hurt && jumping);
    }
    
    public void setLeft(boolean left) {
        if(_hurt) return;
        
        if(left) {
            _right = !(_left = left);
            _facingRight = false;
        }
        else {
            _left = left;
        }
    }
    
    public void setRight(boolean right) {
        if(_hurt) return;
        
        if(right) {
            _left = !(_right = right);
            _facingRight = true;
        }
        else {
            _right = right;
        }
    }
    
    @Override
    protected void fallInPit() {
        _level.passMessage(new MSG(MSG.TYPE.PIT, 0));
        _removed = true;
    }
}
