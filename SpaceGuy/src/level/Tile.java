package level;

public class Tile {
    
    public enum TYPE {
        GROUND, DOOR, CRATE
    }
    
    public TYPE type;
    public boolean solid;
    public boolean reactable;
    public boolean visible;
    public String sprite;
    
    public Tile(TYPE type, boolean reactable, boolean solid, boolean visible, String sprite) {
        this.type = type;
        this.reactable = reactable;
        this.solid = solid;
        this.visible = visible;
        this.sprite = sprite;
    }
}
