package utility;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {
    
    private Map<String, Animation> _animations = new HashMap<>();
    
    private SpriteSheet _sheet;
    
    public void add(String index, Animation animation) {
        _animations.put(index, animation);
    }
    
    public Animation get(String index) { return _animations.get(index); }
    
    public long getAnimTime(String index) {
        return _animations.get(index).delay * _animations.get(index).frames.length;
    }
    
    public void setSheet(SpriteSheet sheet) { _sheet = sheet; }
    
    public SpriteSheet getSheet() { return _sheet; }
}
