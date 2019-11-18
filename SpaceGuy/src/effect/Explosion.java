package effect;

import java.awt.Graphics2D;

public class Explosion extends Effect {
    
    private double _radius, _maxRadius;
    private double _speed = 1.5;
    
    public Explosion(double x, double y, double radius, double maxRadius) {
        super(x, y);
        _radius = radius;
        _maxRadius = maxRadius;
    }
    
    @Override
    public void update(double deltaTime) {
        _radius += _speed;
        
        if(_radius >= _maxRadius) {
            _removed = true;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        int x = (int)(_x - _radius);
        int y = (int)(_y - _radius);
        int diameter = (int)(_radius * 2);
        
        g.setColor(_color);
        g.fillOval(x, y, diameter, diameter);
    }
}
