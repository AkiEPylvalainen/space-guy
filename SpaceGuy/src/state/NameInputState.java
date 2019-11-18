package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import main.Input;
import main.Loader;

public class NameInputState extends GameState {
    
    private Font _font = Loader.instance.font;
    
    private String[] _letters = {
        "abcdefghij",
        "klmnopqrst",
        "uvwxyz    "
    };
    
    private int _tableX = 64;
    private int _tableY = 112;
    
    private String _name = "";
    
    private int _nameX = 64;
    private int _nameY = 88;
    
    private int _cursorX = 0;
    private int _cursorY = 0;
    
    private int _score;
    
    public NameInputState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(double deltaTime) {
        if(Input.isKeyPressed(KeyEvent.VK_LEFT)) {
            if(_cursorX > 0) {
                _cursorX--;
            }
            else {
                _cursorX = _letters[0].length() - 1;
            }
        }
        if(Input.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if(_cursorX < _letters[0].length() - 1) {
                _cursorX++;
            }
            else {
                _cursorX = 0;
            }
        }
        if(Input.isKeyPressed(KeyEvent.VK_UP)) {
            if(_cursorY > 0) {
                _cursorY--;
            }
            else {
                _cursorY = _letters.length;
            }
        }
        if(Input.isKeyPressed(KeyEvent.VK_DOWN)) {
            if(_cursorY < _letters.length) {
                _cursorY++;
            }
            else {
                _cursorY = 0;
            }
        }
        if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
            if(_cursorY == _letters.length) {
                getGSM().pop();
                getGSM().push("score");
                ((ScoreState)getGSM().getCurrent()).addEntry(_name, _score);
            }
            else {
                _name += _letters[_cursorY].charAt(_cursorX);
            }
        }
        if(Input.isKeyPressed(KeyEvent.VK_BACK_SPACE)) {
            String temp = _name;
            _name = "";
            
            for(int i = 0; i < temp.length() - 1; i++) {
                _name += temp.charAt(i);
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
        
        g.setFont(_font);
        g.setColor(Color.WHITE);
        
        g.drawString("Your score: " + String.format("%06d", _score), 64, 32);
        g.drawString("Enter your name", 64, 56);
        
        String nameLine = _name;
        
        for(int i = 0; i < 21 - _name.length(); i++) {
            nameLine += "-";
        }
        
        g.drawString(nameLine, _nameX, _nameY);
        
        for(int i = 0; i < _letters.length; i++) {
            for(int j = 0; j < _letters[0].length(); j++) {
                g.drawString(_letters[i].charAt(j) + "", _tableX + 16 * j, _tableY + 16 * i);
            }
        }
        
        g.drawString("end", _tableX + 16 * 8, _tableY + 16 * 3);
        
        g.setColor(Color.RED);
        
        if(_cursorY < _letters.length) {
            g.drawRect(_tableX + 16 * _cursorX - 4, _tableY + 16 * _cursorY - 12, 15, 15);
        }
        else {
            g.drawRect(_tableX + 16 * 8 - 4, _tableY + 16 * 3 - 12, 30, 15);
        }
    }
    
    public void setScore(int score) {
        _score = score;
    }
}
