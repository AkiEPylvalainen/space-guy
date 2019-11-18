package event;

import entity.Player;
import level.Camera;
import level.Level;
import utility.HUD;

public interface EventPlayer {
    
    public Camera getCamera();
    
    public Level getLevel();
    
    public Player getPlayer();
    
    public HUD getHUD();
}
