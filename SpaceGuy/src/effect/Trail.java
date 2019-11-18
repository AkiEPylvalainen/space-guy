package effect;

import java.awt.Graphics2D;
import utility.SpriteSheet;

public class Trail extends Effect {
    
    private SpriteSheet _sheet;
    private String _sprite;
    private float _alpha = 0.6f;
    private boolean _mirrored;
    
    public Trail(double x, double y, SpriteSheet sheet, String sprite, boolean mirrored) {
        super(x, y);
        _sheet = sheet;
        _sprite = sprite;
        _mirrored = mirrored;
    }

    @Override
    public void update(double deltaTime) {
        _alpha -= 0.03f;
        
        if(_alpha <= 0) {
            _alpha = 0;
            _removed = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setComposite(makeTransparent(_alpha));
        _sheet.draw(g, _sprite, (int)_x, (int)_y, _mirrored, false);
        g.setComposite(makeTransparent(1));
    }
}
