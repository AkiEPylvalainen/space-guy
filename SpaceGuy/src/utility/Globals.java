package utility;

import java.awt.Color;

public class Globals {
    
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public static double distance(double a, double b) {
        return Math.sqrt(Math.pow(b - a, 2));
    }
    
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    public static double rangedRandom(double min, double max) {
        return min + (Math.random() * ((max - min) + 1));
    }
    
    public static Color randomColor() {
        int r = (int)(Math.random() * 255);
        int g = (int)(Math.random() * 255);
        int b = (int)(Math.random() * 255);
        return new Color(r, g, b);
    }
    
    public double interpolate(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }
}
