package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class OptionList {
    
    private String[] _options;
    private Font _font;
    private int _x;
    private int _y;
    private int _selected;
    
    public OptionList(int x, int y, Font font) {
        _x = x;
        _y = y;
        _font = font;
    }
    
    public void draw(Graphics2D g) {
        g.setFont(_font);
        
        for(int i = 0; i < _options.length; i++)  {
            g.setColor(_selected == i ? Color.RED : Color.WHITE);
            g.drawString(_options[i], _x - g.getFontMetrics().stringWidth(_options[i]) / 2, _y + i * (_font.getSize() + 8) + _font.getSize());
        }
    }
    
    public void setOptions(String... options) {
        _options = options;
    }
    
    public void move(int dir) {
        _selected += dir;
        
        if(_selected >= _options.length) {
            _selected = 0;
        }
        else if(_selected < 0) {
            _selected = _options.length - 1;
        }
    }
    
    public void reset() {
        _selected = 0;
    }
    
    public int getX() { return _x; }
    
    public int getY() { return _y; }
    
    public int getSelected() { return _selected; }
}
