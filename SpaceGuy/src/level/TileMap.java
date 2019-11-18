package level;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import utility.Matrix;
import utility.SpriteSheet;

public class TileMap {
    
    private Matrix _grid = new Matrix();
    
    private Map<Character, Tile> _tiles = new HashMap<>();
    
    private int _tileSize = 16;
    
    private int _numRowsToDraw;
    private int _numColsToDraw;
    
    private SpriteSheet _sheet;
    
    public TileMap(int screenWidth, int screenHeight, SpriteSheet sheet) {
        _numRowsToDraw = screenHeight / _tileSize + 2;
        _numColsToDraw = screenWidth / _tileSize + 2;
        _sheet = sheet;
    }
    
    public void load(String[] map) {
        _grid.create(map[0].length(), map.length);
        
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length(); j++) {
                _grid.set(j, i, map[i].charAt(j));
            }
        }
    }
    
    public void clear() {
        _tiles.clear();
    }
    
    public void update(double deltaTime) {}
    
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int colOffset = (int)(-offsetX / _tileSize - 1);
        int rowOffset = (int)(-offsetY / _tileSize - 1);
        
        for(int i = rowOffset; i < rowOffset + _numRowsToDraw; i++) {
            for(int j = colOffset; j < colOffset + _numColsToDraw; j++) {
                Tile tile = getTile(j, i);
                
                if(tile != null && tile.visible) {
                    if(i != 0 && _grid.get(j, i - 1) != '#' && _grid.get(j, i) == '#') {
                        _sheet.draw(g, "grass", j * _tileSize, i * _tileSize);
                    }
                    else {
                        _sheet.draw(g, tile.sprite, j * _tileSize, i * _tileSize);
                    }
                }
            }
        }
    }
    
    public void addTile(char index, Tile.TYPE type, boolean reactable, boolean solid, boolean visible, String sprite) {
        _tiles.put(index, new Tile(type, reactable, solid, visible, sprite));
    }
    
    public void set(int x, int y, char value) { _grid.set(x, y, value); }
    
    public void replace(char oldChar, char newChar) {
        for(int i = 0; i < _grid.getHeight(); i++) {
            for(int j = 0; j < _grid.getWidth(); j++) {
                if(_grid.get(j, i) == oldChar) {
                    _grid.set(j, i, newChar);
                }
            }
        }
    }
    
    public Tile getTile(int x, int y) {
        char value = _grid.get(x, y);
        return _tiles.containsKey(value) ? _tiles.get(value) : null;
    }
    
    public Tile getTile(char value) {
        return _tiles.containsKey(value) ? _tiles.get(value) : null;
    }
    
    public boolean isSolid(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null ? tile.solid : false;
    }
    
    public boolean isTileType(int x, int y, Tile.TYPE type) {
        Tile tile = getTile(x, y);
        return tile != null ? tile.type == type : false;
    }
    
    public int getTileSize()  { return _tileSize; }
    
    public Matrix getMatrix() { return _grid; }
    
    public int getWidth()     { return _grid.getWidth(); }
    
    public int getHeight()    { return _grid.getHeight(); }
    
    public Map<Character, Tile> getTiles() { return _tiles; }
}
