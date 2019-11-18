package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import main.Input;
import main.Loader;
import utility.OptionList;

public class TitleState extends GameState {
    
    public enum STATE {
        ENTER, MENU
    }
    
    private String _title = "Space Guy";
    private Font _titleFont = new Font("Arial", Font.BOLD, 40);
    private int _titleX;
    private int _titleY;
    
    private OptionList _options;
    
    private Font _font = Loader.instance.font;
    
    private STATE _state = STATE.ENTER;
    private long _enterTimer;
    private long _enterDelay = 750;
    private String _enterText = "Press enter";
    
    public TitleState(GameStateManager gsm) {
        super(gsm);
        _titleX = gsm.getScreenWidth() / 2;
        _titleY = 0;
        
        _options = new OptionList(gsm.getScreenWidth() / 2, gsm.getScreenHeight() / 2 + 40, _font);
        _options.setOptions("Play", "Score", "Help", "Exit");
    }
    
    @Override
    public void resume() {
        _options.reset();
    }
    
    @Override
    public void update(double deltaTime) {
        if(_state == STATE.ENTER) {
            if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
                _state = STATE.MENU;
            }
            
            _enterTimer += deltaTime;
            
            if(_enterTimer >= _enterDelay) {
                _enterTimer = 0;
            }
        }
        else if(_state == STATE.MENU) {
            if(Input.isKeyPressed(KeyEvent.VK_DOWN)) {
                _options.move(1);
            }
            if(Input.isKeyPressed(KeyEvent.VK_UP)) {
                _options.move(-1);
            }
            if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
                switch(_options.getSelected()) {
                    case 0: getGSM().push("mode"); break;
                    case 1: getGSM().push("score"); break;
                    case 2: getGSM().push("help"); break;
                    case 3: System.exit(0); break;
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
        
        g.setFont(_titleFont);
        g.setColor(Color.WHITE);
        g.drawString(_title, _titleX - g.getFontMetrics().stringWidth(_title) / 2, _titleY + _titleFont.getSize());
        
        if(_state == STATE.ENTER) {
            if(_enterTimer / (_enterDelay / 2) % 2 == 0) {
                g.setFont(_font);
                g.setColor(Color.WHITE);
                g.drawString(_enterText, _options.getX() - g.getFontMetrics().stringWidth(_enterText) / 2, _options.getY() + 8 + _font.getSize());
            }
        }
        else if(_state == STATE.MENU) {
            _options.draw(g);
        }
    }
}
