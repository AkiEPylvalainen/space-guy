package event;

import utility.Delegate;

public class ActionEvent extends Event {
    
    private Delegate _delegate;
    
    public ActionEvent(Delegate delegate) {
        _delegate = delegate;
    }
    
    @Override
    public void begin(EventPlayer eventPlayer) {
        super.begin(eventPlayer);
        _delegate.function();
        _finished = true;
    }
}
