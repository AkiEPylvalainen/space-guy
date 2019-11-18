package utility;

public class Animation {
    
    public String[] frames;
    public long delay;
    
    public Animation(long delay, String... frames) {
        this.delay = delay;
        this.frames = frames;
    }
    
    public String getKeyFrame(double time) {
        return frames[(int)(time / delay) % frames.length];
    }
}
