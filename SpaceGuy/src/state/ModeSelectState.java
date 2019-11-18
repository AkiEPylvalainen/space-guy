package state;

import entity.Enemy;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import main.Input;
import main.Loader;
import utility.OptionList;

public class ModeSelectState extends GameState {

    private OptionList _options;
    
    private Font _font = Loader.instance.font;
    
    public ModeSelectState(GameStateManager gsm) {
        super(gsm);
        
        _options = new OptionList(gsm.getScreenWidth() / 2, gsm.getScreenHeight() / 2 - 32, _font);
        _options.setOptions("Endless stage", "Finite stage", "Ninja boss", "Witch boss", "Back");
    }
    
    @Override
    public void update(double deltaTime) {
        if(Input.isKeyPressed(KeyEvent.VK_DOWN)) {
            _options.move(1);
        }
        if(Input.isKeyPressed(KeyEvent.VK_UP)) {
            _options.move(-1);
        }
        if(Input.isKeyPressed(KeyEvent.VK_ENTER)) {
            switch(_options.getSelected()) {
                case 0:
                    getGSM().push("level");
                    ((LevelState)getGSM().getCurrent()).setMode(0);
                    break;
                case 1:
                    getGSM().push("level");
                    ((LevelState)getGSM().getCurrent()).setMode(1);
                    break;
                case 2:
                    getGSM().push("level");
                    ((LevelState)getGSM().getCurrent()).setMode(2);
                    ((LevelState)getGSM().getCurrent()).setBoss(Enemy.NINJA);
                    break;
                case 3:
                    getGSM().push("level");
                    ((LevelState)getGSM().getCurrent()).setMode(2);
                    ((LevelState)getGSM().getCurrent()).setBoss(Enemy.WITCH);
                    break;
                case 4: getGSM().pop(); break;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getGSM().getScreenWidth(), getGSM().getScreenHeight());
        
        _options.draw(g);
    }
    
}
