package event;

import entity.Player;
import level.Camera;
import level.Level;
import main.Game;

public class EnterBossEvent extends Event {
    
    enum STATE {
        TRANSITION, DOOR_SHUT
    }
    
    private Player _player;
    private Camera _camera;
    private Level _level;
    
    private double _destX;
    
    private STATE _state = STATE.TRANSITION;
    
    private long _timer;
    private int _count;
    
    public EnterBossEvent() {}
    
    @Override
    public void begin(EventPlayer eventPlayer) {
        super.begin(eventPlayer);
        
        _player = eventPlayer.getPlayer();
        _camera = eventPlayer.getCamera();
        _level = eventPlayer.getLevel();
        
        _destX = _camera.getX() - Game.WIDTH / 2 - (_player.getWidth() / 2 - 2);
        
        _player.stopMoving();
        
        _camera.setTarget(null);
        
        _level.setLocked(true);
    }
    
    @Override
    public void update(double deltaTime) {
        if(_state == STATE.TRANSITION) {
            _camera.move(1);

            _player.setX(_player.getX() + 0.3);

            if(_camera.getX() <= _destX) {
                _level.setRoom(_level.bossRoom);
                _camera.setTarget(_player);
                _state = STATE.DOOR_SHUT;
            }
        }
        else if(_state == STATE.DOOR_SHUT) {
            _timer += deltaTime;
            
            if(_timer >= 150) {
                _timer = 0;
                _level.getMap().set(0, 7 +_count, '|');
                _count++;
                
                if(_count >= 4) {
                    _level.setLocked(false);
                    _finished = true;
                }
            }
        }
    }
}
