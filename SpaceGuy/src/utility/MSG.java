package utility;

public class MSG {
    
    public enum TYPE {
        SCORE, PIT, DEATH, ITEM, ENTER_BOSS, BOSS_DEFEAT
    }
    
    public double x;
    public double y;
    public TYPE type;
    public int value;
    
    public MSG(TYPE type, int value) {
        this.type = type;
        this.value = value;
    }
    
    public MSG(TYPE type, double x, double y, int value) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.value = value;
    }
}
