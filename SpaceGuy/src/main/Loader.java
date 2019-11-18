package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import utility.AnimationSet;
import utility.SpriteSheet;

public class Loader {
    
    public static Loader instance = new Loader();
    
    public Font font;
    
    public SpriteSheet entitySheet;
    public SpriteSheet tileSheet;
    
    public AnimationSet animations = new AnimationSet();
    
    private Loader() {}
    
    public String[] loadLevel(String file) {
        List<String> lines = getLines("/levels/" + file + ".txt");
        
        String[] map = new String[lines.size()];
        
        for(int i = 0; i < map.length; i++) {
            map[i] = lines.get(i);
        }
        
        return map;
    }
    
    public String[] mergeLevels(String ... files) {
        List<String>[] arrays = new List[files.length];
        
        for(int i = 0; i < files.length; i++) {
            arrays[i] = getLines("/levels/" + files[i] + ".txt");
        }
        
        return mergeMaps(arrays);
    }
    
    private String[] mergeMaps(List<String> ... array) {
        String[] map = new String[array[0].size()];
        
        for(int i = 0; i < map.length; i++) {
            map[i] = "";
            for(int j = 0; j < array.length; j++) {
                for(int k = 0; k < array[j].get(0).length(); k++) {
                    map[i] += array[j].get(i).charAt(k);
                }
            }
        }
        
        return map;
    }
    
    public BufferedImage loadImage(String filepath) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(filepath));
        }
        catch(IOException e) {
            return null;
        }
    }
    
    public void loadFont(String path, float size) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/" + path)).deriveFont(size);
        }
        catch(IOException | FontFormatException e) {}
    }
    
    private List<String> getLines(String path) {
        try {
            try (InputStream in = getClass().getResourceAsStream(path)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                
                List<String> lines = new ArrayList<>();
                
                String line;
                while((line = br.readLine()) != null) {
                    lines.add(line);
                }
                
                br.close();
                
                return lines;
            }
        }
        catch(IOException | NumberFormatException e) {
            return null;
        }
    }
}
