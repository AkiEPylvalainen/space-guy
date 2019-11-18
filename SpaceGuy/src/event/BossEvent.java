package event;

import effect.MagicBlow;
import effect.SmokeCloud;
import entity.Enemy;
import level.Level;

public class BossEvent extends Event {
    
    private Enemy _boss;
    private long _timer;
    
    private int _bossType;
    
    public BossEvent(int bossType) {
        _bossType = bossType;
    }
    
    @Override
    public void begin(EventPlayer eventPlayer) {
        super.begin(eventPlayer);
        
        Level level = eventPlayer.getLevel();
        
        int tileSize = level.getMap().getTileSize();
        
        _boss = level.addEnemy(15 * tileSize, 10 * tileSize, _bossType);
        
        if(_bossType == Enemy.NINJA) {
            level.addEffect(new SmokeCloud(_boss.getCenterX(), _boss.getCenterY()));
        }
        else if(_bossType == Enemy.WITCH) {
            level.addEffect(new MagicBlow(_boss.getCenterX(), _boss.getCenterY()));
        }
        
        eventPlayer.getHUD().setBoss(_boss);
    }
    
    @Override
    public void update(double deltaTime) {
        _timer += deltaTime;
        
        if(_timer >= 1000) {
            _boss.activateBehavior();
            _finished = true;
        }
    }
}
