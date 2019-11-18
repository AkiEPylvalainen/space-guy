package entity;

import effect.SpinLine;
import effect.Explosion;
import level.Level;
import utility.*;

public class Projectile extends GameObject {
    
    public static final int PLASMA = 0;
    public static final int SHURIKEN = 1;
    public static final int MAGIC = 2;
    
    private long _trailTimer;
    
    public Projectile(double x, double y, int type, int id, Level level, AnimationSet animations) {
        super(x, y, type, id, level, animations);
        _ignoresGravity = true;
        
        if(id == PLASMA) {
            _offsetX = 2;
            _offsetY = 4;
            _width = 12;
            _height = 8;
            setAnimation("plasma");
        }
        else if(id == SHURIKEN) {
            _offsetX = 1;
            _offsetY = 1;
            _width = 14;
            _height = 14;
            setAnimation("shuriken");
            _trailTimer = 100;
        }
        else if(id == MAGIC) {
            _offsetX = 1;
            _offsetY = 1;
            _width = 14;
            _height = 14;
            setAnimation("magic");
            _trailTimer = 100;
        }
    }
    
    public Projectile(int type, int id, Level level, AnimationSet animations) {
        this(0, 0, type, id, level, animations);
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        
        if(_id == SHURIKEN) {
            _trailTimer += deltaTime;
            
            if(_trailTimer >= 100 / Math.sqrt(_velX * _velX + _velY * _velY) * 3) {
                _trailTimer = 0;
                _level.addEffect(new SpinLine(left() + _width / 2, top() + _height / 2, _velX > 0));
            }
        }
    }
    
    @Override
    protected void checkObjectCollision() {
        for(int i = 0; i < _level.getObjects().size(); i++) {
            GameObject o = _level.getObjects().get(i);
            if(o != this) {
                if((_type == PLAYER_BULLET && o._type == ENEMY) || (_type == ENEMY_BULLET && o._type == PLAYER)) {
                    GameCharacter character = (GameCharacter)o;
                    if(!character.isDead() && collides(character)) {
                        if(!character.isFlinching()) {
                            character.hit(this);
                        }
                        _removed = true;
                        explode(4, 16);
                    }
                }
            }
        }
    }
    
    @Override
    public void obstruct(Sides side, Match match) {
        int tileX = (int)(match.x1 / _level.getMap().getTileSize());
        int tileY = (int)(match.y1 / _level.getMap().getTileSize());
        
        if(_level.getMap().isSolid(tileX, tileY)) {
            super.obstruct(side, match);

            if(side == Sides.LEFT || side == Sides.RIGHT || side == Sides.TOP || side == Sides.BOTTOM) {
                _removed = true;
                explode(4, 16);
            }
        }
    }
    
    @Override
    public void levelBounds(Sides side) {
        if(right() < _level.getBounds().left() || left() > _level.getBounds().right()) {
            _removed = true;
        }
    }
    
    public void explode(double radius, double maxRadius) {
        _level.addEffect(new Explosion(left() + _width / 2, top() + _height / 2, radius, maxRadius));
    }
}
