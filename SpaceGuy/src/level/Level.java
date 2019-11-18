package level;

import effect.Effect;
import effect.Trail;
import entity.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import main.Game;
import utility.*;

public class Level {
    
    private List<GameObject> _objects = new ArrayList<>();
    private List<Effect> _effects = new ArrayList<>();
    private List<Trail> _trails = new ArrayList<>();
    private List<Spawn> _spawns = new ArrayList<>();
    
    private TileMap _map;
    private TileCollider _collider;
    private Camera _camera;
    private Bounds _bounds = new Bounds();
    private AnimationSet _animations;
    
    private Queue<MSG> _messages;
    
    private List<String[]> _rooms = new ArrayList<>();
    
    public String[] bossRoom;
    
    private int _partCount = 1;
    private int _maxPart = 15;
    
    private boolean _locked;
    private boolean _endless;
    
    public Level(int screenWidth, int screenHeight, Queue<MSG> messages, SpriteSheet tileSheet, AnimationSet animations) {
        _map = new TileMap(screenWidth, screenHeight, tileSheet);
        _collider = new TileCollider(_map.getMatrix(), _map.getTileSize(), _map.getTiles());
        _camera = new Camera(screenWidth, screenHeight, _bounds);
        _messages = messages;
        _animations = animations;
    }
    
    public void build(String[] map) {
        _map.load(map);
        
        _bounds.width = map[0].length() * _map.getTileSize();
        _bounds.height = map.length * _map.getTileSize();
        
        populateSpawns();
        
        for(Spawn s : _spawns) {
            if(_camera.inBorder(s.x, 16)) {
                s.obj = addEnemy(s.x, s.y, s.id);
            }
        }
    }
    
    private void populateSpawns() {
        for(int i = 0; i < _map.getHeight(); i++) {
            for(int j = 0; j < _map.getWidth(); j++) {
                int x = _map.getTileSize() * j;
                int y = _map.getTileSize() * i;
                char index = _map.getMatrix().get(j, i);
                
                if(index == 'P') {
                    addPlayer(x, y);
                }
                else if(index == 'G') {
                    _spawns.add(new Spawn(x, y, Enemy.GOON));
                }
                else if(index == 'F') {
                    _spawns.add(new Spawn(x, y, Enemy.PARA));
                }
                else if(index == 'B') {
                    _spawns.add(new Spawn(x, y, Enemy.BULLY));
                }
            }
        }
    }
    
    private String[] appendRoom(String[] room) {
        String[] tempLines = new String[_map.getHeight()];
        
        for(int i = 0; i < tempLines.length; i++) {
            tempLines[i] = "";
            for(int j = 0; j < room[0].length(); j++) {
                tempLines[i] += _map.getMatrix().get(j + room[0].length(), i);
            }

            tempLines[i] += room[i];
        }
        
        return tempLines;
    }
    
    public void setRoom(String[] room) {
        _map.load(room);
        
        _bounds.width = _map.getMatrix().getWidth() * _map.getTileSize();
        _bounds.height = _map.getMatrix().getHeight() * _map.getTileSize();
        
        for(int i = 0; i < _objects.size(); i++) {
            GameObject o = _objects.get(i);
            o.setX(o.getX() - Game.WIDTH);
            
            if(o.getX() <= -16 || o.getX() >= _bounds.width + 16) {
                _objects.remove(i);
                i--;
            }
        }
        
        for(int i = 0; i < _effects.size(); i++) {
            Effect e = _effects.get(i);
            e.setX(e.getX() - Game.WIDTH);
            
            if(e.getX() <= -24 || e.getX() >= _bounds.width + 24) {
                _effects.remove(i);
                i--;
            }
        }
        
        for(int i = 0; i < _trails.size(); i++) {
            Trail t = _trails.get(i);
            t.setX(t.getX() - Game.WIDTH);
            
            if(t.getX() <= -16 || t.getX() >= _bounds.width + 16) {
                _trails.remove(i);
                i--;
            }
        }
        
        _spawns.clear();
        
        populateSpawns();
    }
    
    public void update(double deltaTime) {
        if(!_locked) {
            for(int i = 0; i < _objects.size(); i++) {
                _objects.get(i).update(deltaTime);
                if(_objects.get(i).shouldRemove()) {
                    _objects.remove(i);
                    i--;
                }
            }

            for(int i = 0; i < _effects.size(); i++) {
                _effects.get(i).update(deltaTime);
                if(_effects.get(i).shouldRemove()) {
                    _effects.remove(i);
                    i--;
                }
            }
            
            for(int i = 0; i < _trails.size(); i++) {
                _trails.get(i).update(deltaTime);
                if(_trails.get(i).shouldRemove()) {
                    _trails.remove(i);
                    i--;
                }
            }
            
            Player player = getPlayer();

            if(_partCount < _maxPart && player != null && player.getX() + player.getWidth() / 2 >= Game.WIDTH + Game.WIDTH / 2) {
                if(!_endless) {
                    _partCount++;
                }
                
                String[] tempLines;
                
                if(_partCount < _maxPart) {
                    tempLines = appendRoom(_rooms.get((int)Globals.rangedRandom(0, 4)));
                }
                else {
                    tempLines = appendRoom(bossRoom);
                }
                
                setRoom(tempLines);
            }
            
            _camera.update();

            for(Spawn s : _spawns) {
                if(s.obj == null) {
                    if(_camera.outBorder(s.x, 16, 16 + 16)) {
                        s.obj = addEnemy(s.x, s.y, s.id);
                    }
                }
                else {
                    if(_camera.outBorder(s.obj.getX(), 16 + 8)) {
                        s.obj.remove();
                    }

                    if(s.obj.shouldRemove()) {
                        s.obj = null;
                    }
                }
            }
        }
        
        _map.update(deltaTime);
    }
    
    public void draw(Graphics2D g) {
        int camX = (int)_camera.getX();
        int camY = (int)_camera.getY();
        
        g.translate(camX, camY);
        
        _map.draw(g, camX, camY);
        
        for(int i = 0; i < _trails.size(); i++) {
            _trails.get(i).draw(g);
        }
        
        for(int i = 0; i < _objects.size(); i++) {
            if(_objects.get(i).isVisible()) {
                _objects.get(i).draw(g);
            }
        }
        
        for(int i = 0; i < _effects.size(); i++) {
            _effects.get(i).draw(g);
        }
        
        g.translate(-camX, -camY);
    }
    
    public void clear() {
        for(Spawn spawn : _spawns) {
            spawn.obj = null;
        }
        
        _spawns.clear();
        _objects.clear();
        _effects.clear();
        _trails.clear();
        _map.clear();
        _rooms.clear();
        
        _partCount = 1;
        _endless = false;
    }
    
    public void addPlayer(double x, double y) {
        Player p = new Player(x, y, this, _animations);
        _camera.setTarget(p);
        _objects.add(p);
    }
    
    public Enemy addEnemy(double x, double y, int id) {
        Enemy e = new Enemy(x, y, id, this, _animations);
        _objects.add(e);
        return e;
    }
    
    public void addProjectile(Projectile projectile) {
        _objects.add(projectile);
    }
    
    public Item addItem(double x, double y, int id) {
        Item i = new Item(x, y, id, this, _animations);
        _objects.add(i);
        return i;
    }
    
    public void addEffect(Effect e) {
        _effects.add(e);
    }
    
    public void addTrail(Trail t) {
        _trails.add(t);
    }
    
    public Player getPlayer() {
        for(int i = 0; i < _objects.size(); i++) {
            if(_objects.get(i).getType() == GameObject.PLAYER) {
                return (Player)_objects.get(i);
            }
        }
        return null;
    }
    
    public void removeProjectiles() {
        for(int i = 0; i < _objects.size(); i++) {
            if(_objects.get(i).getType() == GameObject.PLAYER_BULLET || _objects.get(i).getType() == GameObject.ENEMY_BULLET) {
                _objects.remove(i);
                i--;
            }
        }
    }
    
    public void passMessage(MSG msg) {
        _messages.add(msg);
    }
    
    public List<GameObject> getObjects() { return _objects; }
    
    public TileMap getMap() { return _map; }
    
    public TileCollider getCollider() { return _collider; }
    
    public Bounds getBounds() { return _bounds; }
    
    public Camera getCamera() { return _camera; }
    
    public void setLocked(boolean locked) { _locked = locked; }
    
    public void setEndless(boolean endless) { _endless = endless; }
    
    public void addRoom(String[] room) {
        _rooms.add(room);
    }
}
