package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import level.Level;
import utility.*;

public abstract class GameObject {
    
    public static final int PLAYER        = 0;
    public static final int ENEMY         = 1;
    public static final int ITEM          = 2;
    public static final int PLAYER_BULLET = 3;
    public static final int ENEMY_BULLET  = 4;
    
    protected double _x, _y;
    protected double _velX, _velY;
    protected int _offsetX, _offsetY;
    protected int _width = 16, _height = 16;
    
    protected int _type;
    protected int _id;
    
    protected boolean _removed;
    protected boolean _visible = true;
    protected boolean _obstructing = true;
    protected boolean _facingRight = true;
    protected boolean _facingLocked;
    protected boolean _falling = true;
    protected boolean _ignoresGravity;
    protected double _maxFallSpeed = 8;
    protected double _gravity = 0.3;
    protected double _moveSpeed = 2;
    
    protected Level _level;
    protected AnimationSet _animations;
    protected String _currentAnim = "";
    protected long _animTimer;
    
    public GameObject(double x, double y, int type, int id, Level level, AnimationSet animations) {
        _x = x;
        _y = y;
        _type = type;
        _id = id;
        _level = level;
        _animations = animations;
    }
    
    public Rectangle getCollisionBox() {
        return new Rectangle((int)left(), (int)top(), (int)_width, (int)_height);
    }
    
    public boolean collides(GameObject other) {
        return getCollisionBox().intersects(other.getCollisionBox());
    }
    
    protected void checkMoving() {
        if(!_ignoresGravity) {
            if(_falling) {
                _velY += _gravity;
                if(_velY >= _maxFallSpeed) {
                    _velY = _maxFallSpeed;
                }
            }
        }
    }
    
    protected void checkObjectCollision() {}
    
    protected void checkAnimation() {}
    
    public void update(double deltaTime) {
        checkMoving();
        
        checkObjectCollision();
        
        _falling = true;
        
        _x += _velX;
        if(_obstructing) {
            _level.getCollider().checkX(this);
        }
        
        _y += _velY;
        if(_obstructing) {
            _level.getCollider().checkY(this);
        }
        
        if(_obstructing) {
            if(left() < _level.getBounds().left()) {
                levelBounds(Sides.LEFT);
            }
            else if(right() > _level.getBounds().right()) {
                levelBounds(Sides.RIGHT);
            }
        }
        
        if(top() > _level.getBounds().bottom() + 8) {
            fallInPit();
        }
        
        checkAnimation();
        
        _animTimer = (long)((_animTimer + deltaTime) % _animations.getAnimTime(_currentAnim));
    }
    
    public void draw(Graphics2D g) {
        _animations.getSheet().draw(g, getSprite(), (int)_x, (int)_y, !_facingRight, false);
    }
    
    public String getSprite() {
        return _animations.get(_currentAnim).getKeyFrame(_animTimer);
    }
    
    public void obstruct(Sides side, Match match) {
        if(side == Sides.LEFT) {
            left(match.x2);
            _velX = 0;
        }
        else if(side == Sides.RIGHT) {
            right(match.x1);
            _velX = 0;
        }
        else if(side == Sides.TOP) {
            top(match.y2);
            _velY = 0;
        }
        else if(side == Sides.BOTTOM) {
            top(match.y1 - _height);
            _velY = 0;
            _falling = false;
        }
    }
    
    public void levelBounds(Sides side) {
        if(side == Sides.LEFT) {
            left(_level.getBounds().left());
            _velX = 0;
        }
        else if(side == Sides.RIGHT) {
            left(_level.getBounds().right() - _width);
            _velX = 0;
        }
    }
    
    protected void calculateSize(int offsetX, int offsetY) {
        _offsetX = offsetX;
        _offsetY = offsetY;
        _width = (int)(16 - _offsetX * 2);
        _height = (int)(16 - _offsetY);
    }
    
    protected void fallInPit() {
        _removed = true;
    }
    
    public void stopMoving() {
        _velX = 0;
        _velY = 0;
    }
    
    public void remove() {
        _removed = true;
    }
    
    public void moveX(double velX) {
        _velX = velX;
        
        if(!_facingLocked) {
            _facingRight = velX > 0;
        }
    }
    
    public boolean canCollide() { return true; }
    
    public double left() { return _x + _offsetX; }
    
    public void left(double x) { _x = x - _offsetX; }
    
    public double right() { return _x + _width + _offsetX; }
    
    public void right(double x) { _x = x - (_width + _offsetX); }
    
    public double top() { return _y + _offsetY; }
    
    public void top(double y) { _y = y - _offsetY; }
    
    public double bottom() { return _y + _height + _offsetY; }
    
    public void bottom(double y) { _y = y - (_height + _offsetY); }
    
    public double getX() { return _x; }
    
    public double getY() { return _y; }
    
    public void setX(double x) { _x = x; }
    
    public void setY(double y) { _y = y; }
    
    public double getWidth() { return _width; }
    
    public double getHeight() { return _height; }
    
    public double getVelX() { return _velX; }
    
    public void setVelX(double velX) { _velX = velX; }
    
    public double getVelY() { return _velY; }
    
    public void setVelY(double velY) { _velY = velY; }
    
    public double getOffsetX() { return _offsetX; }
    
    public double getOffsetY() { return _offsetY; }
    
    public double getMoveSpeed() { return _moveSpeed; }
    
    public boolean shouldRemove() { return _removed; }
    
    public boolean isFalling() {return _falling; }
    
    public boolean isVisible() { return _visible; }
    
    public void setVisible(boolean visible) { _visible = visible; }
    
    public boolean isFacingRight() { return _facingRight; }
    
    public void setFacingRight(boolean facingRight) {
        if(!_facingLocked) {
            _facingRight = facingRight;
        }
    }
    
    public int getType() { return _type; }
    
    public int getId() { return _id; }
    
    public void setIgnoringGravity(boolean ignoresGravity) {
        _ignoresGravity = ignoresGravity;
    }
    
    public AnimationSet getAnimations() { return _animations; }
    
    protected void setAnimation(String anim) {
        if(!_currentAnim.equals(anim)) {
            _currentAnim = anim;
            _animTimer = 0;
        }
    }
    
    protected void swapAnimation(String anim) {
        if(!_currentAnim.equals(anim)) {
            _currentAnim = anim;
        }
    }
    
    public void lockFacing(boolean lock) { _facingLocked = lock; }
    
    public double getCenterX() { return left() + _width / 2; }
    
    public double getCenterY() { return top() + _height / 2; }
}
