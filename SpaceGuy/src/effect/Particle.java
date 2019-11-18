package effect;

public class Particle {
    
    public double x;
    public double y;
    public double dx;
    public double dy;
    public double radius;

    public Particle(double x, double y, double radius, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
    }
}
