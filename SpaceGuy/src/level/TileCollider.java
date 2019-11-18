package level;

import entity.GameObject;
import java.util.List;
import java.util.Map;
import utility.Match;
import utility.Matrix;
import utility.Sides;

public class TileCollider {
    
    private TileResolver _resolver;
    
    public TileCollider(Matrix grid, int tileSize, Map<Character, Tile> tiles) {
        _resolver = new TileResolver(grid, tileSize, tiles);
    }
    
    public void checkX(GameObject entity) {
        double x;
        
        if(entity.getVelX() > 0) {
            x = entity.right();
        }
        else if(entity.getVelX() < 0) {
            x = entity.left();
        }
        else {
            return;
        }
        
        List<Match> matches = _resolver.searchByRange(x, x, entity.top(), entity.bottom());
        
        for(int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            
            //if(!_resolver.isSolid(match.tile)) return;
            if(!_resolver.isReactable(match.tile)) return;
            
            if(entity.getVelX() > 0) {
                if(entity.right() > match.x1) {
                    entity.obstruct(Sides.RIGHT, match);
                }
            }
            else if(entity.getVelX() < 0) {
                if(entity.left() < match.x2) {
                    entity.obstruct(Sides.LEFT, match);
                }
            }
        }
    }
    
    public void checkY(GameObject entity) {
        double y;
        
        if(entity.getVelY() > 0) {
            y = entity.bottom();
        }
        else if(entity.getVelY() < 0) {
            y = entity.top();
        }
        else {
            return;
        }
        
        List<Match> matches = _resolver.searchByRange(entity.left(), entity.right(), y, y);
        
        for(int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            
            //if(!_resolver.isSolid(match.tile)) return;
            if(!_resolver.isReactable(match.tile)) return;
            
            if(entity.getVelY() > 0) {
                if(entity.bottom() > match.y1) {
                    entity.obstruct(Sides.BOTTOM, match);
                }
            }
            else if(entity.getVelY() < 0) {
                if(entity.top() < match.y2) {
                    entity.obstruct(Sides.TOP, match);
                }
            }
        }
    }
    
    public TileResolver getResolver() { return _resolver; }
}