package state;

import entity.*;
import event.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Queue;
import level.Camera;
import level.Level;
import level.Tile;
import main.Input;
import main.Loader;
import utility.*;

public class LevelState extends GameState implements EventPlayer {
    
    private Level _level;
    private Player _player;
    private OptionList _options;
    
    private Color _bgColor = new Color(92, 148, 252).brighter();
    private Color _pauseColor = new Color(0, 0, 0, 100);
    private Font _font = Loader.instance.font;
    
    private Queue<MSG> _messages = new ArrayDeque<>();
    private Queue<Event> _events = new ArrayDeque<>();
    private Event _currentEvent;
    
    private boolean _paused;
    
    private int _mode = 0;
    private int _boss = Enemy.NINJA;
    
    private HUD _hud;
    
    private int _score;
    
    public LevelState(GameStateManager gsm) {
        super(gsm);
        
        _level = new Level(gsm.getScreenWidth(), gsm.getScreenHeight(), _messages, Loader.instance.tileSheet, Loader.instance.animations);
        
        _options = new OptionList(gsm.getScreenWidth() / 2, gsm.getScreenHeight() / 2 - 24, _font);
        _options.setOptions("Resume", "Help", "Exit");
        
        _hud = new HUD(0, gsm.getScreenHeight() - 32, gsm.getScreenWidth(), 32, _font);
    }
    
    @Override
    public void init() {
        _score = 0;
        _paused = false;
        _options.reset();
        
        if(_mode == 0) {
            _level.setEndless(true);
            _level.build(Loader.instance.mergeLevels("start-1", "level-1"));
        }
        if(_mode == 1) {
            _level.setEndless(false);
            _level.build(Loader.instance.mergeLevels("start-1", "level-1"));
        }
        else if(_mode == 2) {
            _level.build(Loader.instance.mergeLevels("boss"));
            _level.getMap().replace(':', '|');
            _level.addPlayer(2 * _level.getMap().getTileSize(), 10 * _level.getMap().getTileSize());
            
            _events.add(new WaitEvent(500));
            _events.add(new BossEvent(_boss));
        }
        
        _level.addRoom(Loader.instance.mergeLevels("level-1"));
        _level.addRoom(Loader.instance.mergeLevels("level-2"));
        _level.addRoom(Loader.instance.mergeLevels("level-3"));
        _level.addRoom(Loader.instance.mergeLevels("level-4"));
        _level.addRoom(Loader.instance.mergeLevels("level-5"));
        
        _level.bossRoom = Loader.instance.mergeLevels("boss");
        
        _player = _level.getPlayer();
        
        _hud.setPlayer(_player);
        _hud.setScore(_score);
        
        _level.getMap().addTile('#', Tile.TYPE.GROUND, true, true, true, "ground");
        _level.getMap().addTile('=', Tile.TYPE.GROUND, true, true, true, "grass");
        _level.getMap().addTile(':', Tile.TYPE.DOOR, true, false, false, "ground");
        _level.getMap().addTile('|', Tile.TYPE.GROUND, true, true, true, "door");
        _level.getMap().addTile('?', Tile.TYPE.CRATE, true, true, true, "crate");
    }
    
    @Override
    public void close() {
        _level.clear();
        _hud.clear();
        _events.clear();
        _messages.clear();
        _player = null;
    }
    
    @Override
    public void update(double deltaTime) {
        if(_paused) {
            if(Input.isKeyPressed(KeyEvent.VK_DOWN)) {
                _options.move(1);
            }
            if(Input.isKeyPressed(KeyEvent.VK_UP)) {
                _options.move(-1);
            }
            if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
                switch(_options.getSelected()) {
                    case 0: _paused = false; break;
                    case 1: getGSM().push("help"); break;
                    case 2: getGSM().pop(); break;
                }
            }
        }
        else {
            if(_player != null && _currentEvent == null) {
                if(Input.isKeyDown(KeyEvent.VK_LEFT)) {
                    _player.setLeft(true);
                }
                if(Input.isKeyDown(KeyEvent.VK_RIGHT)) {
                    _player.setRight(true);
                }
                if(Input.isKeyPressed(KeyEvent.VK_SPACE)) {
                    _player.jump(true);
                }
                if(Input.isKeyReleased(KeyEvent.VK_SPACE)) {
                    _player.jump(false);
                }
                if(Input.isKeyPressed(KeyEvent.VK_X)) {
                    _player.shoot();
                }
                if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
                    _paused = true;
                }
            }
            
            while(_currentEvent == null || _currentEvent.isFinished()) {
                if(_events.peek() == null) { // no event queued up
                    _currentEvent = null;
                    break;
                }
                else { // event queued up
                    _currentEvent = _events.poll();
                    _currentEvent.begin(this);
                }
            }
            
            _level.update(deltaTime);
            
            if(_currentEvent != null) {
                _currentEvent.update(deltaTime);
            }
            
            while(_messages.peek() != null) {
                MSG msg = _messages.poll();
                
                if(msg.type == MSG.TYPE.SCORE) {
                    _score += msg.value;
                    _hud.setScore(_score);
                }
                else if(msg.type == MSG.TYPE.ITEM) {
                    Item i = _level.addItem(msg.x, msg.y, 0);
                    i.setVelY(-3);
                }
                else if(msg.type == MSG.TYPE.ENTER_BOSS) {
                    double rand = Math.random();
                    
                    if(rand <= 0.5) {
                        _boss = Enemy.NINJA;
                    }
                    else {
                        _boss = Enemy.WITCH;
                    }
                    
                    _events.add(new EnterBossEvent());
                    _events.add(new WaitEvent(500));
                    _events.add(new BossEvent(_boss));
                }
                else if(msg.type == MSG.TYPE.BOSS_DEFEAT) {
                    _player.stopMoving();
                    _level.removeProjectiles();
                    _hud.setBoss(null);
                    
                    _events.add(new WaitEvent(3000));
                    _events.add(new ActionEvent(this::goToNameInput));
                }
                else if(msg.type == MSG.TYPE.DEATH) {
                    _events.add(new WaitEvent(2500));
                    _events.add(new ActionEvent(this::goToNameInput));
                }
                else if(msg.type == MSG.TYPE.PIT) {
                    _events.add(new WaitEvent(2500));
                    _events.add(new ActionEvent(this::goToNameInput));
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(_bgColor);
        g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
        
        _level.draw(g);
        
        _hud.draw(g);
        
        if(_paused) {
            g.setColor(_pauseColor);
            g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
            
            _options.draw(g);
        }
    }
    
    private void goToNameInput() {
        getGSM().pop();
        
        if(_mode != 2) {
            getGSM().push("name");
            ((NameInputState)getGSM().getCurrent()).setScore(_score);
        }
    }
    
    public void setMode(int mode) {
        _mode = mode;
        
        close();
        init();
    }
    
    public void setBoss(int boss) {
        _boss = boss;
        
        close();
        init();
    }
    
    @Override
    public Camera getCamera() { return _level.getCamera(); }
    
    @Override
    public Level getLevel() { return _level; }
    
    @Override
    public Player getPlayer() { return _player; }
    
    @Override
    public HUD getHUD() { return _hud; }
}