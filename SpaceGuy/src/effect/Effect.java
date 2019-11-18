package effect;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Effect {
    
    protected double _x, _y;
    protected Color _color = Color.WHITE;
    protected boolean _removed;
    
    public Effect(double x, double y) {
        _x = x;
        _y = y;
    }
    
    public abstract void update(double deltaTime);
    
    public abstract void draw(Graphics2D g);
    
    public boolean shouldRemove() { return _removed; }
    
    public double getX() { return _x; }
    
    public void setX(double x) { _x = x; }
    
    public double getY() { return _y; }
    
    public void setY(double y) { _y = y; }
    
    protected AlphaComposite makeTransparent(float alpha) {
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }
}
