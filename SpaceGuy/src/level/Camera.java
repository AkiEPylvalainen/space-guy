package level;

import entity.GameObject;
import utility.Bounds;
import utility.Globals;

public class Camera {
    
    private double _x, _y;
    private double _offsetX;
    private GameObject _target;
    
    private Bounds _bounds;
    private Bounds _viewBounds = new Bounds();
    
    public Camera(int screenWidth, int screenHeight, Bounds bounds) {
        _bounds = bounds;
        _viewBounds.width = screenWidth;
        _viewBounds.height = screenHeight;
    }
    
    public void update() {
        if(_target != null) {
            calculatePosition();
        }
    }
    
    public void calculatePosition() {
        _x += (_viewBounds.width / 2 - _target.left() - _target.getCollisionBox().width / 2 - _x + _offsetX);
        _y += (_viewBounds.height / 2 - _target.getY() - _y);

        _x = Globals.clamp(_x, _viewBounds.width - _bounds.right(), _bounds.left());
        _y = Globals.clamp(_y, _viewBounds.height - _bounds.bottom(), _bounds.top());

        _viewBounds.x = -(int)_x;
        _viewBounds.y = _viewBounds.height - _bounds.bottom();
    }
    
    public void setTarget(GameObject target) {
        _target = target;
        
        if(_target != null) {
            calculatePosition();
        }
    }
    
    public double distanceFromBorder(double startX) {
        double deltaX = _viewBounds.left() + _viewBounds.width / 2 - startX;
        return Math.sqrt(deltaX * deltaX);
    }
    
    public boolean inBorder(double startX, double offset) {
        return distanceFromBorder(startX) <= _viewBounds.width / 2 + offset;
    }
    
    public boolean outBorder(double startX, double offset) {
        return distanceFromBorder(startX) >= _viewBounds.width / 2 + offset;
    }
    
    public boolean outBorder(double startX, double offset1, double offset2) {
        double dist = distanceFromBorder(startX);
        return dist >= _viewBounds.width / 2 + offset1 && dist <= _viewBounds.width / 2 + offset2;
    }
    
    public double getX() { return _x; }
    
    public double getY() { return _y; }
    
    public Bounds getView() { return _viewBounds; }
    
    public void move(double dx) {
        _x = Globals.clamp(_x - dx, _viewBounds.width - _bounds.right(), _bounds.left());
        _viewBounds.x = -(int)_x;
    }
}
