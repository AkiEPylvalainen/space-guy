package effect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class SpinLine extends Effect {
    
    private double _radius = 10;
    private double _angle = 0;
    private double _arc = 225;
    private double _speed = 20;
    private float _alpha = 1f;
    
    public SpinLine(double x, double y, boolean clockWise) {
        super(x, y);
        _color = Color.WHITE;
        _speed = clockWise ? -_speed : _speed;
    }
    
    @Override
    public void update(double deltaTime) {
        _angle = (_angle + _speed) % 360;
        
        _radius -= 0.2;
        _alpha -= 0.05;
        
        if(_radius <= 0 || _alpha <= 0) {
            _removed = true;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        int x = (int)(_x - _radius);
        int y = (int)(_y - _radius);
        int diameter = (int)(_radius * 2);
        
        g.setComposite(makeTransparent(_alpha));
        g.setColor(_color);
        g.setStroke(new BasicStroke(2));
        g.drawArc(x, y, diameter, diameter, (int)_angle, (int)_arc);
        g.setStroke(new BasicStroke(1));
        g.setComposite(makeTransparent(1f));
    }
}
