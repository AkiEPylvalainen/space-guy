package entity;

import level.Level;
import utility.AnimationSet;

public class Item extends GameObject {
    
    public static final int HEALTH = 0;
    public static final int POWERUP = 1;
    
    public Item(double x, double y, int id, Level level, AnimationSet animations) {
        super(x, y, ITEM, id, level, animations);
        calculateSize(2, 4);
        setAnimation("energy");
    }
}
