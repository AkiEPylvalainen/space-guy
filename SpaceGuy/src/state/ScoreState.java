package state;

import data.SaveLoad;
import data.ScoreEntry;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.Input;
import main.Loader;
import utility.OptionList;

public class ScoreState extends GameState {
    
    private Font _font = Loader.instance.font;
    
    private List<ScoreEntry> _entries = new ArrayList<>();
    
    private int _boardX = 32;
    private int _boardY = 48;
    private int _boardOffsetX;
    
    private int _titleX;
    private int _titleY;
    private String _title = "High score";
    
    private OptionList _options;
    
    private int _max = 8;
    
    public ScoreState(GameStateManager gsm) {
        super(gsm);
        
        _boardOffsetX = gsm.getScreenWidth() - 128;
        
        _titleX = gsm.getScreenWidth() / 2;
        _titleY = 32;
        
        _options = new OptionList(gsm.getScreenWidth() / 2, gsm.getScreenHeight() - 32, _font);
        _options.setOptions("Back");
    }
    
    @Override
    public void init() {
        SaveLoad.instance.load(_entries);
        sort();
    }
    
    @Override
    public void close() {
        _entries.clear();
        _options.reset();
    }
    
    @Override
    public void update(double deltaTime) {
        if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
            if(_options.getSelected() == 0) {
                getGSM().pop();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(_font);
        
        g.drawString(_title, _titleX - g.getFontMetrics().stringWidth(_title) / 2, _titleY);
        
        for(int i = 0; i < _max; i++) {
            int y = _boardY + i * (_font.getSize() + 8) + _font.getSize();
            
            g.drawString((i + 1) + "", _boardX, y);
            
            if(i < _entries.size()) {
                ScoreEntry entry = _entries.get(i);
                g.drawString(entry.name, _boardX + 32, y);
                g.drawString(String.format("%06d", entry.score), _boardX + _boardOffsetX, y);
            }
            else {
                g.drawString("-", _boardX + 32, y);
                g.drawString("-", _boardX + _boardOffsetX, y);
            }
        }
        
        _options.draw(g);
    }
    
    public void addEntry(String name, int score) {
        _entries.add(new ScoreEntry(name, score));
        sort();
        SaveLoad.instance.save(_entries);
    }
    
    private void sort() {
        for(int i = 0; i < _entries.size() - 1; i++) {
            for(int j = i + 1; j < _entries.size(); j++) {
                if(_entries.get(i).score < _entries.get(j).score) {
                    Collections.swap(_entries, i, j);
                }
            }
        }
        
        if(_entries.size() > _max) {
            int count = _entries.size() - _max;
            
            for(int i = 0; i < count; i++) {
                _entries.remove(_entries.size() - 1);
            }
        }
    }
}
