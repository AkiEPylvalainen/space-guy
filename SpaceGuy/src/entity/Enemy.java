package entity;

import behavior.*;
import effect.BossExplosion;
import level.Level;
import utility.*;

public class Enemy extends GameCharacter {
    
    public static final int GOON = 0;
    public static final int PARA = 1;
    public static final int BULLY = 2;
    public static final int NINJA = 3;
    public static final int WITCH = 4;
    
    private int _receivedScore;
    
    public Enemy(double x, double y, int id, Level level, AnimationSet animations) {
        super(x, y, ENEMY, id, level, animations);
        _moveSpeed = 0.75;
        _facingRight = false;
        _flinchDelay = 150;
        calculateSize(2, 2);
        
        if(_id == GOON) {
            setAnimation("goon-walk");
            
            _health = _maxHealth = 1;
            _receivedScore = 100;
            addBehavior(new PendulumWalk(_moveSpeed, true));
        }
        else if(_id == PARA) {
            setAnimation("para-fly");
            
            _health = _maxHealth = 2;
            _receivedScore = 200;
            addBehavior(new YoyoFlyer());
            addBehavior(new Shooter());
        }
        else if(_id == BULLY) {
            setAnimation("bully-walk");
            
            _health = _maxHealth = 3;
            _receivedScore = 300;
            addBehavior(new Bully(_moveSpeed));
        }
        else if(_id == NINJA) {
            setAnimation("ninja-idle");
            
            _health = _maxHealth = 20;
            _receivedScore = 2000;
            _projectileID = Projectile.SHURIKEN;
            addBehavior(new Ninja(), false);
        }
        else if(_id == WITCH) {
            setAnimation("witch-idle");
            
            _health = _maxHealth = 20;
            _receivedScore = 2000;
            _projectileID = Projectile.MAGIC;
            addBehavior(new Witch(), false);
        }
    }
    
    @Override
    protected void checkAnimation() {
        if(_id == BULLY) {
            if(_velX != 0) {
                setAnimation("bully-walk");
            }
            else {
                setAnimation("bully-idle");
            }
        }
        else if(_id == NINJA) {
            if(_shooting) {
                setAnimation("ninja-shoot");
            }
            else if(_velY != 0 && _ignoresGravity) {
                setAnimation("ninja-wallrun");
            }
            else if(_velX != 0) {
                setAnimation("ninja-dash");
            }
            else if(_extraAnim == 1) {
                setAnimation("ninja-dash-warn");
            }
            else if(_extraAnim == 2) {
                setAnimation("ninja-shoot-warn");
            }
            else {
                setAnimation("ninja-idle");
            }
        }
        else if(_id == WITCH) {
            if(_shooting && _ignoresGravity) {
                setAnimation("witch-midair-shoot");
            }
            else if(_shooting) {
                setAnimation("witch-shoot");
            }
            else if(_ignoresGravity) {
                setAnimation("witch-midair");
            }
            else if(_extraAnim == 1) {
                setAnimation("witch-shoot-warn");
            }
            else {
                setAnimation("witch-idle");
            }
        }
    }
    
    @Override
    public void hit(GameObject o) {
        alterHealth(-1);
        
        if(_health <= 0) {
            _dead = true;
            _removed = true;
            
            _level.passMessage(new MSG(MSG.TYPE.SCORE, _receivedScore));
            
            if(_id == NINJA || _id == WITCH) {
                _level.passMessage(new MSG(MSG.TYPE.BOSS_DEFEAT, 0));
                _level.addEffect(new BossExplosion(left() + _width / 2, top() + _height / 2));
            }
            else {
                if(Math.random() <= 0.4) {
                    _level.passMessage(new MSG(MSG.TYPE.ITEM, _x, _y, Item.HEALTH));
                }
            }
        }
        else {
            setFlinching(true);
        }
    }
    
    @Override
    public void obstruct(Sides side, Match match) {
        int tileX = (int)(match.x1 / _level.getMap().getTileSize());
        int tileY = (int)(match.y1 / _level.getMap().getTileSize());
        
        if(_level.getMap().isSolid(tileX, tileY)) {
            super.obstruct(side, match);
        }
    }
}
