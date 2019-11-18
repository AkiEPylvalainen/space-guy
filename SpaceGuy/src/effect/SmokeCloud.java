package effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import utility.Globals;

public class SmokeCloud extends Effect {
    
    private List<Particle> _particles = new ArrayList<>();
    private float _alpha = 1f;
    
    public SmokeCloud(double x, double y) {
        super(x, y);
        
        int rand = (int)Globals.rangedRandom(16, 32);
        
        for(int i = 0; i < rand; i++) {
            double spd = Globals.rangedRandom(0.1, 0.5);
            double angle = Math.random() * (Math.PI * 2);
            Particle p = new Particle(0, 0, 12, Math.cos(angle) * spd, Math.sin(angle) * spd);
            _particles.add(p);
        }
    }
    
    @Override
    public void update(double deltaTime) {
        for(int i = 0; i < _particles.size(); i++) {
            Particle p = _particles.get(i);
            p.x += p.dx;
            p.y += p.dy;
            
            p.radius -= 0.5;
            
            if(p.radius <= 0) {
                _particles.remove(i);
                i--;
            }
        }
        
        _alpha -= 0.05;
        
        if(_alpha <= 0 || _particles.isEmpty()) {
            _removed = true;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setComposite(makeTransparent(_alpha));
        g.setColor(Color.WHITE);
        
        for(int i = 0; i < _particles.size(); i++) {
            Particle p = _particles.get(i);
            g.fillOval((int)(_x + p.x - p.radius), (int)(_y + p.y - p.radius), (int)(p.radius * 2), (int)(p.radius * 2));
        }
        
        g.setComposite(makeTransparent(1));
    }
}
