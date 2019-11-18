package effect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class MagicBlow extends Effect {
    
    private List<Particle> _particles = new ArrayList<>();
    private float _alpha = 1f;
    private double _orbitRadius = 4;
    private double _angle;
    
    private double _radius = 24;
    
    public MagicBlow(double x, double y) {
        super(x, y);
        
        for(int i = 0; i < 8; i++) {
            double angle = (Math.PI * 2 / 8) * i;
            _particles.add(new Particle(Math.cos(angle) * _orbitRadius, Math.sin(angle) * _orbitRadius, 4, 0, 0));
        }
        
        _color = new Color(100, 0, 200);
    }
    
    @Override
    public void update(double deltaTime) {
        _angle = (_angle + 0.1) % 360;
        
        for(int i = 0; i < _particles.size(); i++) {
            Particle p = _particles.get(i);
            double angle = _angle + (Math.PI * 2 / 8) * i;
            
            p.x = Math.cos(angle) * _orbitRadius;
            p.y = Math.sin(angle) * _orbitRadius;
        }
        
        _radius -= 2;
        
        if(_radius <= 0) {
            _radius = 0;
        }
        
        _orbitRadius += 1.25;
        
        _alpha -= 0.035;
        
        if(_alpha <= 0) {
            _alpha = 0;
            _removed = true;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setComposite(makeTransparent(_alpha));
        g.setColor(_color);
        
        for(int i = 0; i < _particles.size(); i++) {
            Particle p = _particles.get(i);
            int px = (int)(_x + p.x - p.radius);
            int py = (int)(_y + p.y - p.radius);
            
            g.setColor(Color.BLACK);
            g.fillOval(px, py, (int)(p.radius * 2), (int)(p.radius * 2));
            
            g.setColor(_color);
            g.setStroke(new BasicStroke(2));
            g.drawOval(px, py, (int)(p.radius * 2), (int)(p.radius * 2));
            g.setStroke(new BasicStroke(1));
        }
        
        g.setComposite(makeTransparent(1));
        
        g.setColor(Color.BLACK);
        g.fillOval((int)(_x - _radius), (int)(_y - _radius), (int)(_radius * 2), (int)(_radius * 2));
        
        g.setColor(_color);
        g.setStroke(new BasicStroke(2));
        g.drawOval((int)(_x - _radius), (int)(_y - _radius), (int)(_radius * 2), (int)(_radius * 2));
        g.setStroke(new BasicStroke(1));
    }
}
