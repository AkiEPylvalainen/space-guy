package event;

public class Event {
    
    private EventPlayer _eventPlayer;
    
    protected boolean _finished;
    
    public void begin(EventPlayer eventPlayer) {
        _eventPlayer = eventPlayer;
    }
    
    public void update(double deltaTime) {}
    
    public boolean isFinished() { return _finished; }
    
    public EventPlayer getEventPlayer() { return _eventPlayer; }
}
