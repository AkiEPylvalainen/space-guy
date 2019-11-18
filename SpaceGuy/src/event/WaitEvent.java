package event;

public class WaitEvent extends Event {
    
    protected long _timer;
    protected long _delay;
    
    public WaitEvent(long delay) {
        _delay = delay;
    }
    
    @Override
    public void begin(EventPlayer eventPlayer) {
        super.begin(eventPlayer);
        if(_delay <= 0) {
            _finished = true;
        }
    }
    
    @Override
    public void update(double deltaTime) {
        _timer += deltaTime;
            
        if(_timer >= _delay) {
            _finished = true;
        }
    }
}
