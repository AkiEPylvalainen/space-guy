package main;

import java.awt.Graphics2D;
import state.*;
import utility.Animation;
import utility.SpriteSheet;

public class Game extends GameApp {
    
    public static final int WIDTH = 16 * 18;
    public static final int HEIGHT = 16 * 14;
    public static final int SCALE = 4;
    
    private GameStateManager _gsm = new GameStateManager(WIDTH, HEIGHT);
    
    public Game() {
        super("Space Guy", WIDTH, HEIGHT, SCALE);
        
        Loader.instance.loadFont("megaman_2.ttf", 8);
        
        Loader.instance.entitySheet = new SpriteSheet(Loader.instance.loadImage("/sprites/entities.png"), 16);
        Loader.instance.tileSheet = new SpriteSheet(Loader.instance.loadImage("/sprites/tiles.png"), 16);
        
        Loader.instance.entitySheet.defineTile("plasma-1", 0, 0);
        Loader.instance.entitySheet.defineTile("plasma-2", 1, 0);
        Loader.instance.entitySheet.defineTile("plasma-3", 2, 0);
        Loader.instance.entitySheet.defineTile("shuriken-1", 3, 0);
        Loader.instance.entitySheet.defineTile("shuriken-2", 4, 0);
        Loader.instance.entitySheet.defineTile("magic-1", 5, 0);
        Loader.instance.entitySheet.defineTile("magic-2", 6, 0);
        Loader.instance.entitySheet.defineTile("magic-3", 7, 0);
        Loader.instance.entitySheet.defineTile("goon-1", 1, 1);
        Loader.instance.entitySheet.defineTile("goon-2", 0, 1);
        Loader.instance.entitySheet.defineTile("goon-3", 2, 1);
        Loader.instance.entitySheet.defineTile("para-fly-1", 3, 1);
        Loader.instance.entitySheet.defineTile("para-fly-2", 4, 1);
        Loader.instance.entitySheet.defineTile("bully-1", 6, 1);
        Loader.instance.entitySheet.defineTile("bully-2", 5, 1);
        Loader.instance.entitySheet.defineTile("bully-3", 7, 1);
        Loader.instance.entitySheet.defineTile("ninja-idle", 0, 2);
        Loader.instance.entitySheet.defineTile("ninja-dash-1", 1, 2);
        Loader.instance.entitySheet.defineTile("ninja-dash-2", 2, 2);
        Loader.instance.entitySheet.defineTile("ninja-dash-warn", 3, 2);
        Loader.instance.entitySheet.defineTile("ninja-shoot-warn", 4, 2);
        Loader.instance.entitySheet.defineTile("ninja-shoot", 5, 2);
        Loader.instance.entitySheet.defineTile("ninja-wallrun-1", 6, 2);
        Loader.instance.entitySheet.defineTile("ninja-wallrun-2", 7, 2);
        Loader.instance.entitySheet.defineTile("player-idle", 0, 3);
        Loader.instance.entitySheet.defineTile("player-walk-1", 1, 3);
        Loader.instance.entitySheet.defineTile("player-walk-2", 0, 3);
        Loader.instance.entitySheet.defineTile("player-walk-3", 2, 3);
        Loader.instance.entitySheet.defineTile("player-jump", 3, 3);
        Loader.instance.entitySheet.defineTile("player-fall", 4, 3);
        Loader.instance.entitySheet.defineTile("player-hurt", 5, 3);
        Loader.instance.entitySheet.defineTile("player-shoot-idle", 0, 4);
        Loader.instance.entitySheet.defineTile("player-shoot-walk-1", 1, 4);
        Loader.instance.entitySheet.defineTile("player-shoot-walk-2", 0, 4);
        Loader.instance.entitySheet.defineTile("player-shoot-walk-3", 2, 4);
        Loader.instance.entitySheet.defineTile("player-shoot-jump", 3, 4);
        Loader.instance.entitySheet.defineTile("player-shoot-fall", 4, 4);
        Loader.instance.entitySheet.defineTile("energy-1", 0, 5);
        Loader.instance.entitySheet.defineTile("energy-2", 1, 5);
        Loader.instance.entitySheet.defineTile("energy-3", 2, 5);
        Loader.instance.entitySheet.defineTile("witch-idle", 0, 6);
        Loader.instance.entitySheet.defineTile("witch-shoot-warn", 1, 6);
        Loader.instance.entitySheet.defineTile("witch-shoot", 2, 6);
        Loader.instance.entitySheet.defineTile("witch-midair", 3, 6);
        Loader.instance.entitySheet.defineTile("witch-midair-shoot", 4, 6);
        
        Loader.instance.tileSheet.defineTile("grass", 0, 0);
        Loader.instance.tileSheet.defineTile("ground", 1, 0);
        Loader.instance.tileSheet.defineTile("door", 2, 0);
        Loader.instance.tileSheet.defineTile("drygrass", 0, 1);
        Loader.instance.tileSheet.defineTile("hardland", 1, 1);
        Loader.instance.tileSheet.defineTile("crate", 2, 1);
        
        Loader.instance.animations.setSheet(Loader.instance.entitySheet);
        
        Loader.instance.animations.add("plasma", new Animation(100, "plasma-1", "plasma-2", "plasma-3"));
        Loader.instance.animations.add("shuriken", new Animation(75, "shuriken-1", "shuriken-2"));
        Loader.instance.animations.add("magic", new Animation(75, "magic-1", "magic-2", "magic-3"));
        Loader.instance.animations.add("energy", new Animation(100, "energy-1", "energy-2", "energy-3"));
        
        Loader.instance.animations.add("goon-walk", new Animation(175, "goon-1", "goon-2", "goon-3", "goon-2"));
        Loader.instance.animations.add("para-fly", new Animation(150, "para-fly-1", "para-fly-2"));
        Loader.instance.animations.add("bully-idle", new Animation(0, "bully-2"));
        Loader.instance.animations.add("bully-walk", new Animation(175, "bully-1", "bully-2", "bully-3", "bully-2"));
        
        Loader.instance.animations.add("ninja-idle", new Animation(0, "ninja-idle"));
        Loader.instance.animations.add("ninja-dash", new Animation(75, "ninja-dash-1", "ninja-dash-2"));
        Loader.instance.animations.add("ninja-dash-warn", new Animation(0, "ninja-dash-warn"));
        Loader.instance.animations.add("ninja-shoot-warn", new Animation(0, "ninja-shoot-warn"));
        Loader.instance.animations.add("ninja-shoot", new Animation(0, "ninja-shoot"));
        Loader.instance.animations.add("ninja-wallrun", new Animation(75, "ninja-wallrun-1", "ninja-wallrun-2"));
        
        Loader.instance.animations.add("witch-idle", new Animation(0, "witch-idle"));
        Loader.instance.animations.add("witch-shoot-warn", new Animation(0, "witch-shoot-warn"));
        Loader.instance.animations.add("witch-shoot", new Animation(0, "witch-shoot"));
        Loader.instance.animations.add("witch-midair", new Animation(0, "witch-midair"));
        Loader.instance.animations.add("witch-midair-shoot", new Animation(0, "witch-midair-shoot"));
        
        Loader.instance.animations.add("player-idle", new Animation(0, "player-idle"));
        Loader.instance.animations.add("player-walk", new Animation(150, "player-walk-1", "player-walk-2", "player-walk-3", "player-walk-2"));
        Loader.instance.animations.add("player-jump", new Animation(0, "player-jump"));
        Loader.instance.animations.add("player-fall", new Animation(0, "player-fall"));
        Loader.instance.animations.add("player-hurt", new Animation(0, "player-hurt"));
        
        Loader.instance.animations.add("player-shoot-idle", new Animation(0, "player-shoot-idle"));
        Loader.instance.animations.add("player-shoot-walk", new Animation(150, "player-shoot-walk-1", "player-shoot-walk-2", "player-shoot-walk-3", "player-shoot-walk-2"));
        Loader.instance.animations.add("player-shoot-jump", new Animation(0, "player-shoot-jump"));
        Loader.instance.animations.add("player-shoot-fall", new Animation(0, "player-shoot-fall"));
        
        _gsm.add("title", new TitleState(_gsm));
        _gsm.add("level", new LevelState(_gsm));
        _gsm.add("help",  new HelpState(_gsm));
        _gsm.add("score", new ScoreState(_gsm));
        _gsm.add("name",  new NameInputState(_gsm));
        _gsm.add("mode",  new ModeSelectState(_gsm));
        
        _gsm.push("title");
    }
    
    @Override
    public void init() {}
    
    @Override
    public void update(double deltaTime) {
        _gsm.getCurrent().update(deltaTime);
    }
    
    @Override
    public void draw(Graphics2D g) {
        _gsm.getCurrent().draw(g);
    }
    
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
