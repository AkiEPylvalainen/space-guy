package behavior;

import java.util.ArrayDeque;
import java.util.Queue;
import utility.Match;
import utility.Sides;

public abstract class StateBehavior extends Behavior {
    
    protected Queue<Integer> _queue = new ArrayDeque<>();
    protected int _current = -1;
    protected boolean _finished;
    
    @Override
    public void begin() {
        populateStates();
    }
    
    protected abstract void populateStates();
    
    @Override
    public void update(double deltaTime) {
        while(_current == -1 || _finished) {
            if(_queue.peek() == null) { // no states queued up
                populateStates();
                _current = !_queue.isEmpty() ? _queue.peek() : -1;
                break;
            }
            else { // states queued up
                _finished = false;
                _current = _queue.poll();
                beginState(_current);
            }
        }
        
        if(_current >= 0) {
            updateState(_current, deltaTime);
        }
    }
    
    protected abstract void beginState(int state);
    
    protected abstract void updateState(int state, double deltaTime);
    
    @Override
    public void obstruct(Sides side, Match match) {}
    
    @Override
    public void levelBounds(Sides side) {}
}
