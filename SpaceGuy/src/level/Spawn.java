package level;

import entity.GameObject;

public class Spawn {
    
    public int x;
    public int y;
    public int id;
    
    public GameObject obj;
    
    public Spawn(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
}
