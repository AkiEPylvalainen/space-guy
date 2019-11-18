package utility;

public class Bounds {
    
    public double x;
    public double y;
    public double width;
    public double height;
    
    public double left() { return x; }
    
    public double right() { return x + width; }
    
    public double top() { return y; }
    
    public double bottom() { return y + height; }
}
