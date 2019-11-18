package data;

import java.io.*;
import java.util.List;

public class SaveLoad {
    
    public static SaveLoad instance = new SaveLoad();
    
    private SaveLoad() {}
    
    public void save(List<ScoreEntry> entries) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved/save.dat"));
            
            ScoreEntry[] array = new ScoreEntry[entries.size()];
            
            for(int i = 0; i < array.length; i++) {
                array[i] = entries.get(i);
            }
            
            oos.writeObject(array);
            
            oos.flush();
            oos.close();
        }
        catch(IOException e) {}
    }
    
    public void load(List<ScoreEntry> entries) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved/save.dat"));
            
            ScoreEntry[] array = (ScoreEntry[])ois.readObject();
            
            for(int i = 0; i < array.length; i++) {
                entries.add(array[i]);
            }
            
            ois.close();
        }
        catch(IOException | ClassNotFoundException e) {}
    }
}
