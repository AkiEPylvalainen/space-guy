package utility;

public class Matrix {
    
    private char[][] _grid;
    
    public void create(int width, int height) {
        _grid = new char[height][width];
    }
    
    public char get(int x, int y) {
        return isValid(x, y) ? _grid[y][x] : ' ';
    }
    
    public void set(int x, int y, char value) {
        if(isValid(x, y)) {
            _grid[y][x] = value;
        }
    }
    
    public boolean isValid(int x, int y) {
        return x >= 0 && x < _grid[0].length && y >= 0 && y < _grid.length;
    }
    
    public int getWidth() { return _grid[0].length; }
    
    public int getHeight() { return _grid.length; }
}
