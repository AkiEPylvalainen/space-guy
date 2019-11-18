package effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BossExplosion extends Effect {
    
    private List<Particle> _particles = new ArrayList<>();
    private float _alpha = 1f;
    
    public BossExplosion(double x, double y) {
        super(x, y);
        
        for(int i = 0; i < 8; i++) {
            double angle = (Math.PI / 4) * i;
            _particles.add(new Particle(0, 0, 4, Math.cos(angle) * 2, Math.sin(angle) * 2));
        }
    }

    @Override
    public void update(double deltaTime) {
        for(int i = 0; i < _particles.size(); i++) {
            Particle p = _particles.get(i);
            p.x += p.dx;
            p.y += p.dy;
            
            p.radius += 1;
            
            if(p.radius >= 16) {
                p.radius = 4;
            }
        }
        
        _alpha -= 0.01;
        
        if(_alpha <= 0) {
            _particles.clear();
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
