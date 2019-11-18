package utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheet {
    
    private Map<String, int[]> _sprites = new HashMap<>();
    private Map<String, String[]> _groups = new HashMap<>();
    private BufferedImage _image;
    private int _size;
    
    public SpriteSheet(BufferedImage image, int size) {
        _image = image;
        _size = size;
    }
    
    public void define(String name, int x, int y) {
        _sprites.put(name, new int[]{x, y, _size, _size});
    }
    
    public void defineTile(String name, int x, int y) {
        _sprites.put(name, new int[]{x * _size, y * _size, _size, _size});
    }
    
    public void defineGroup(String name, String... frames) {
        _groups.put(name, frames);
    }
    
    public void draw(Graphics2D g, String name, int x, int y) {
        int[] coords = _sprites.get(name);
        
        draw(g, x, y, coords[2], coords[3], coords[0], coords[1], false, false);
    }
    
    public void draw(Graphics2D g, String name, int x, int y, boolean flippedX, boolean flippedY) {
        int[] coords = _sprites.get(name);
        
        draw(g, x, y, coords[2], coords[3], coords[0], coords[1], flippedX, flippedY);
    }
    
    public void draw(Graphics2D g, int x, int y, int w, int h, int srcX, int srcY, boolean flippedX, boolean flippedY) {
        int srcX1 = srcX + (flippedX ? w : 0);
        int srcY1 = srcY + (flippedY ? h : 0);
        int srcX2 = srcX + (flippedX ? 0 : w);
        int srcY2 = srcY + (flippedY ? 0 : h);
        
        g.drawImage(_image, x, y, x + w, y + h, srcX1, srcY1, srcX2, srcY2, null);
    }
    
    public String[] getGroup(String name) { return _groups.get(name); }
    
    public int getSize() { return _size; }
}
