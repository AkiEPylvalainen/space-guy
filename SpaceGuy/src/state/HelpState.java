package state;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import main.Input;
import main.Loader;
import utility.OptionList;

public class HelpState extends GameState {
    
    private String[][] _help = {
        {"Arrow Left",  "Move left"},
        {"Arrow Right", "Move right"},
        {"Space",       "Jump"},
        {"X",           "Shoot"},
        {"Enter",       "Pause"},
        {"Esc",         "Close game"}
    };
    
    private int _helpX;
    private int _helpY;
    private int _helpOffsetX;
    
    private int _titleX;
    private int _titleY;
    private String _title = "Help";
    
    private OptionList _options;
    
    private Font _font = Loader.instance.font;
    
    public HelpState(GameStateManager gsm) {
        super(gsm);
        _helpX = 32;
        _helpY = 48;
        _helpOffsetX = gsm.getScreenWidth() - 128;
        
        _titleX = gsm.getScreenWidth() / 2;
        _titleY = 32;
        
        _options = new OptionList(gsm.getScreenWidth() / 2, gsm.getScreenHeight() - 32, _font);
        _options.setOptions("Back");
    }
    
    @Override
    public void close() {
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
        
        g.setFont(_font);
        g.setColor(Color.WHITE);
        
        g.drawString(_title, _titleX - g.getFontMetrics().stringWidth(_title) / 2, _titleY);
        
        for(int i = 0; i < _help.length; i++) {
            int y = _helpY + i * (_font.getSize() + 8) + _font.getSize();
            g.drawString(_help[i][0], _helpX, y);
            g.drawString(_help[i][1], _helpX + _helpOffsetX, y);
        }
        
        _options.draw(g);
    }
}
