package utility;

import entity.Enemy;
import entity.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HUD {
    
    private int _x;
    private int _y;
    private int _width;
    private int _height;
    private Font _font;
    
    private Player _player;
    private int _score;
    
    private int _healthX;
    private int _healthY;
    private int _healthWidth = 6;
    private int _healthHeight = 8;
    
    private int _scoreX;
    private int _scoreY;
    
    private Enemy _boss;
    private int _bossX;
    private int _bossY;
    
    public HUD(int x, int y, int w, int h, Font font) {
        _x = x;
        _y = y;
        _width = w;
        _height = h;
        _font = font;
        
        _healthX = _x + 68;
        _healthY = _y + 12;
        
        _scoreX = _x + _width - 104;
        _scoreY = _y + 12;
        
        _bossX = _x + _width - 14;
        _bossY = 20;
    }
    
    public void clear() {
        _player = null;
        _score = 0;
        _boss = null;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(_x, _y, _width, _height);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(_x + 2, _y + 2, _width - 4, _height - 4, 5, 5);
        g.setStroke(new BasicStroke(1));
        
        if(_player != null) {
            g.setColor(Color.GREEN);
            
            for(int i = 0; i < _player.getHealth(); i++) {
                g.fillRect(_healthX + i * (_healthWidth + 1) + 2, _healthY + 2, _healthWidth, _healthHeight - 3);
            }
            
            g.setColor(Color.WHITE);
            g.drawRect(_healthX, _healthY, (_healthWidth + 1) * _player.getMaxHealth() + 2, _healthHeight);
            
            g.setFont(_font);
            g.drawString("Health: ", _x + 8, _healthY + _font.getSize());
            g.drawString("Score: " + String.format("%06d", _score), _scoreX, _scoreY + _font.getSize());
        }
        
        if(_boss != null) {
            g.setColor(Color.BLACK);
            g.fillRect(_bossX, _bossY, 8, 4 * _boss.getMaxHealth() + 1);
            
            g.setColor(Color.MAGENTA.darker());
            
            for(int i = 0; i < _boss.getHealth(); i++) {
                g.fillRect(_bossX + 1, _bossY + 4 * (_boss.getMaxHealth() - 1) - i * 4 + 1, 8 - 2, 4 - 1);
            }
        }
    }
    
    public void setPlayer(Player player) {
        _player = player;
    }
    
    public void setScore(int score) {
        _score = score;
    }
    
    public void setBoss(Enemy boss) {
        _boss = boss;
    }
}
