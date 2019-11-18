package level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utility.Match;
import utility.Matrix;

public class TileResolver {
    
    private Matrix _grid;
    private int _tileSize;
    private Map<Character, Tile> _tiles;
    
    public TileResolver(Matrix grid, int tileSize, Map<Character, Tile> tiles) {
        _grid = grid;
        _tileSize = tileSize;
        _tiles = tiles;
    }
    
    public double toIndex(double pos) { return Math.floor(pos / _tileSize); }
    
    public double[] toIndexRange(double pos1, double pos2) {
        double pMax = Math.ceil(pos2 / _tileSize) * _tileSize;
        double pos = pos1;
        
        List<Double> list = new ArrayList<>();
        
        do {
            list.add(toIndex(pos));
            pos += _tileSize;
        } while(pos < pMax);
        
        double[] range = new double[list.size()];
        
        for(int i = 0; i < list.size(); i++) {
            range[i] = list.get(i);
        }
        
        return range;
    }
    
    public Match getByIndex(double indexX, double indexY) {
        char tile = _grid.get((int)indexX, (int)indexY);
        
        if(isReactable(tile)) {
            double x1 = indexX * _tileSize;
            double x2 = x1 + _tileSize;
            double y1 = indexY * _tileSize;
            double y2 = y1 + _tileSize;
            return new Match(tile, x1, x2, y1, y2);
        }
        
        return null;
    }
    
    public List<Match> searchByRange(double x1, double x2, double y1, double y2) {
        List<Match> matches = new ArrayList<>();
        
        double[] xRange = toIndexRange(x1, x2);
        double[] yRange = toIndexRange(y1, y2);
        
        for(double indexX : xRange) {
            for(double indexY : yRange) {
                Match match = getByIndex(indexX, indexY);
                
                if(match != null) {
                    matches.add(match);
                }
            }
        }
        
        return matches;
    }
    
    //public boolean isSolid(char tile) { return _tiles.containsKey(tile) ? _tiles.get(tile).solid : false; }
    
    public boolean isReactable(char tile) {
        return _tiles.containsKey(tile) ? _tiles.get(tile).reactable : false;
    }
    
    public Tile getTile(char tile) { return _tiles.get(tile); }
}
